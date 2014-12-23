package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.BackslashEscape;
import org.adligo.css.shared.models.common.CssIdentifier;
import org.adligo.css.shared.models.common.I_CssI18nConstants;
import org.adligo.css.shared.models.common.LineChar;
import org.adligo.css.shared.models.common.ParseSection;
import org.adligo.css.shared.models.common.SpecifiedValue;
import org.adligo.css.shared.models.selectors.Combinator;
import org.adligo.css.shared.models.selectors.CssLink;
import org.adligo.css.shared.models.selectors.Selector;
import org.adligo.css.shared.models.selectors.SequenceOfSimpleSelectors;
import org.adligo.css.shared.models.selectors.SequenceOfSimpleSelectorsParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
/**
 * Ok this is a universal stylesheet parser,
 * so I have to assume the most current version 
 * which is a little vague since stylesheets don't start with something like;
 * CSS_2.1
 * or 
 * CSS_3.0
 * http://www.w3.org/TR/CSS21/syndata.html
 * http://www.w3.org/TR/2009/PR-css3-selectors-20091215
 * 
 * The parser has to guess which css version is being used,
 * although Css 2.1 does mention core syntax will not be
 * changed in future versions.
 */
public class StyleSheetParser {
  /**
   * two types of comments 
   * http://www.w3.org/TR/CSS21/syndata.html
   * 4.1.9
   */
  private static final String COMMENT_START = "/*";
  private static final String COMMENT_END = "*/";
  private static final String COMMENT_SGML_START = "<!--";
  private static final String COMMENT_SGML_END = "-->";

  private int lineNumber_ = 1;
  private int characterNumber_ = 1;
  private Stack<ParseSection> sections_ = new Stack<ParseSection>();
  private Stack<LineChar> sectionStarts_ = new Stack<LineChar>();
  
  private I_ExpectedCss expectedCss_;
  /**
   * note this hasn't been implemented yet for at rule blocks,
   * only for selector blocks.
   */
  private Map<String,CssType> expectedCurrentBlock_;
  
  private StringBuilder sb_ = new StringBuilder();
  private ParseSection currentTopSection_;
  private LineChar currentTopSectionStart_;
  
  private BackslashEscape backslashEscape_ = null;
  private String currentAtRule_ = null;
  private SequenceOfSimpleSelectors currentSequenceOfSimpleSelectors_ = null;
  private Combinator currentCombinator_ = null;
  private Set<Selector> currentSelectors_ = null;
  private List<CssLink> currentSelectorLinks_ = null;
  
  private StyleSheetMutant styleSheet_;
  private String currentPropertyName_ = null;
  private CssIdentifier currentIdentifier_ = null;
  private Map<String,SpecifiedValue<?>> currentBlock_;
  private boolean inPropertyValue_ = false;
  private boolean inString_ = false;
  
  /**
   * for each parse error
   * keep track of the invalid line numbers for ultra clear error
   * messages like;
   * CssError line numbers 3-56 not used due to character 7 on line 37.
   */
  private LineChar currentErrorSectionStart_ = null;
  private LineChar currentErrorLocation_ = null;
  private LineChar currentErrorSectionEnd_ = null;
  private List<Throwable> warnings_;
  private I_CssI18nConstants i18nConstants_;
  private SequenceOfSimpleSelectorsParser sequenceOfSimpleSelectorsParser_ = new SequenceOfSimpleSelectorsParser();
  
  /**
   * there are special rules for @imports
   */
  private static final String AT_IMPORT = "@import";
  
  private static final char AT_CHAR = '@';
  private static final char BLOCK_START_CHAR = '{';
  private static final char BLOCK_END_CHAR = '}';
  
  private static final char LINE_FEED_CHAR = '\n';
  private static final char CARRAGE_RETURN_CHAR = '\r';
  /**
   * This method is not thread-safe one method per thread please.
   * 
   * note this may have quite a few bugs,
   * it currently just looks for text not in {}
   * trims it and uses it as the selector, then
   * looks for : and trims it and uses it form the property.
   * 
   * @param content this is a UTF-16 string (like all of java)
   *    and most javascript (this java after a GWT compile)
   *    https://mathiasbynens.be/notes/javascript-encoding
   *    expected to be translated from a UTF-8 encoded css file.
   * @param the warnings list is mutated by this 
   *    method when there is some sort of issue parsing the 
   *    content.
   * @return
   */
  @SuppressWarnings("incomplete-switch")
  public StyleSheet parse(String content, I_ExpectedCss expected, I_CssI18nConstants constants) {
    i18nConstants_ = constants;
    expectedCss_ = expected;
    
    sb_ = new StringBuilder();
    currentAtRule_ = null;
    styleSheet_ = new StyleSheetMutant();
    
    char [] chars = content.toCharArray();
    
    
    Character lastChar = null;

    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == LINE_FEED_CHAR || c == CARRAGE_RETURN_CHAR) {
        if (lastChar == null) {
          lastChar = c;
          lineNumber_++;
          characterNumber_ = 1;
        } else {
          if ( !lastChar.equals(c) && (lastChar == LINE_FEED_CHAR || lastChar == CARRAGE_RETURN_CHAR)) {
            //do nothing, the first new line character already changed the lastChar
          } else {
            lastChar = c;
            lineNumber_++;
            characterNumber_ = 1;
          }
        }
      } else if (currentTopSection_ == null) {
        characterNumber_++;
        identifyTopSection(c);
      } else {
        characterNumber_++;
        ParseSection currentSection = sections_.peek();
        switch (currentTopSection_) {
          case COMMENT:
              processComment(c, COMMENT_END);
            break;
          case COMMENT_SGML:
              processComment(c, COMMENT_SGML_END);
            break;
          case AT_RULE:
              switch (currentSection) {
                case AT_RULE_IDENTIFER:
                case AT_RULE_CONTENT:
                  processAtRule(c, currentSection);
                  break;
                 default: 
                  processBlock(c, currentSection);
              }
            break;
          case SELECTOR_OR_BLOCK:
            switch (currentSection) {
              case SELECTOR_OR_BLOCK:
                setStacks(ParseSection.SELECTOR_GROUP);
                currentSection = ParseSection.SELECTOR_GROUP;
                //note no break
              case SELECTOR_GROUP:
                processSelectorGroup(c,currentSection);
              break;
              default:
                processBlock(c, currentSection);
            }
        }
      } 
    }
    return new StyleSheet(styleSheet_);
  }
  
  public void identifyTopSection(char c) {
    if (backslashEscape_ != null) {
      if (!backslashEscape_.append(c)) {
        sb_.append(backslashEscape_.toChar());
        if (Character.isWhitespace(c)) {
          return;
        }
      }
    }
    String soFar = sb_.toString();
    //try to identify the section;
    if (c == AT_CHAR) {
      sb_ = new StringBuilder();
      currentTopSection_ = ParseSection.AT_RULE;
      currentTopSectionStart_ = new LineChar(lineNumber_, characterNumber_);
      sections_.push(ParseSection.AT_RULE);
      sectionStarts_.push(currentTopSectionStart_);
    } else if (c == COMMENT_START.charAt(0)) {
      
      sb_ = new StringBuilder();
      sb_.append(c);
    } else if (c == COMMENT_SGML_START.charAt(0)) {
      sb_ = new StringBuilder();
      sb_.append(c);
    } else if (soFar.length() >= 1 && COMMENT_START.indexOf(soFar) == 0) {
      sb_.append(c);
      if (COMMENT_START.equals(soFar)) {
        sb_ = new StringBuilder();
        currentTopSection_ = ParseSection.COMMENT;
        sections_.push(ParseSection.COMMENT);
        sectionStarts_.push(new LineChar(lineNumber_, characterNumber_ - sb_.toString().length()));
      }
    } else if (soFar.length() >= 1 && COMMENT_SGML_START.indexOf(soFar) == 0) {
      sb_.append(c);
      if (COMMENT_SGML_START.equals(sb_.toString())) {
        sb_ = new StringBuilder();
        currentTopSection_ = ParseSection.COMMENT_SGML;
        sections_.push(ParseSection.COMMENT_SGML);
        sectionStarts_.push(new LineChar(lineNumber_, characterNumber_ - sb_.toString().length()));
      }
    } else if ( !Character.isWhitespace(c)){
      if (BackslashEscape.isBackslash(c)) {
        backslashEscape_ = new BackslashEscape();
      } else {
        //ignore whitespace characters, 
        // blocks are stored parsed under selectors and at 
        currentTopSection_ = ParseSection.SELECTOR_OR_BLOCK;
        currentTopSectionStart_ = new LineChar(lineNumber_, characterNumber_);
        sections_.push(ParseSection.SELECTOR_OR_BLOCK);
        sectionStarts_.push(currentTopSectionStart_);
        sb_.append(c);
        currentSelectorLinks_ = new ArrayList<CssLink>();
        currentSelectors_ = new HashSet<Selector>();
      }
    }
  }

  @SuppressWarnings("incomplete-switch")
  public void processAtRule(char c, ParseSection currentSection) {
    switch (currentSection) {
      case AT_RULE:
        setStacks(ParseSection.AT_RULE_IDENTIFER);
        currentIdentifier_ = new CssIdentifier();
      case AT_RULE_IDENTIFER:
        if (!currentIdentifier_.append(c)) {
          if (!currentIdentifier_.isValid()) {
            currentErrorSectionStart_ = sectionStarts_.peek();
            currentErrorLocation_ = new LineChar(lineNumber_, characterNumber_);
            currentIdentifier_ = null;
          }
          popStacks(1);
          setStacks(ParseSection.AT_RULE_CONTENT);
          sb_ = new StringBuilder();
        }
        break;
      case AT_RULE_CONTENT:
          if (backslashEscape_ != null) {
            if (!backslashEscape_.append(c)) {
              sb_.append(backslashEscape_.toChar());
              backslashEscape_ = null;
            }
          }
          if (c == '{') {
            setStacks(ParseSection.BLOCK);
            currentBlock_ = new HashMap<String,SpecifiedValue<?>>();
          } else if (BackslashEscape.isBackslash(c)) {
            backslashEscape_ = new BackslashEscape();
          } else if (c == ';') {
            if (currentErrorLocation_ == null) {
              AtRuleMutant arm = new AtRuleMutant();
              arm.setContent(sb_.toString());
              styleSheet_.addAtRule(currentAtRule_, arm);
            } else {
              appendWarning();
            }
          } else {
            sb_.append(c);
          }
        break;
    }
  }

  @SuppressWarnings("incomplete-switch")
  private void processBlock(char c, ParseSection currentSection) {
    if (!inPropertyValue_) {
      switch (currentSection) {
        case BLOCK:
          inPropertyValue_ = false;
          //ok the block just started, or a declaration and property just ended
          if (Character.isWhitespace(c)) {
            return;
          } else if (c == ':') {
            if (currentIdentifier_ != null) {
              currentPropertyName_ = currentIdentifier_.getId();
            } else {
              //I don't see what to do with empty declartion name, 
              // so just use a empty string for now
              currentPropertyName_ = "";
            }
            
          } else if (c == '}') {
            //back to top elements :)_
            popStacks(sections_.size());
          } else {
            
            currentIdentifier_ = new CssIdentifier();
            if (currentIdentifier_.append(c)) {
              setStacks(ParseSection.PROPERTY_NAME);
            }
          }
          break;
          
        case PROPERTY_NAME:
          if (!currentIdentifier_.append(c)) {
            if (c == ':') {
              currentPropertyName_ = currentIdentifier_.getId();
              currentIdentifier_ = null;
              popStacks(1);
              setStacks(ParseSection.PROPERTY_VALUE);
              inPropertyValue_ = true;
              sb_ = new StringBuilder();
            }
          }
          break;
      }
    } else {
      
      //in property value
      switch (currentSection) {
        case BRACKET:
          processPair(c, ']');
          break;
        case PARENTHESES:
          processPair(c, ')');
          break;
        case BRACES:
          processPair(c, '}');
          break;
        case DOUBLE_QUOTE:
          processQuote(c, '"');
          break;
        case SINGLE_QUOTE:
          processQuote(c, '\'');
          break;
        case PROPERTY_VALUE:
          
          if (c == '[') {
            setStacks(ParseSection.BRACKET);
          } else if (c == '(') {
            setStacks(ParseSection.PARENTHESES);
          } else if (c == '{') {
            setStacks(ParseSection.BRACES);
          } else if (c == '"') {
            inString_ = true;
            setStacks(ParseSection.DOUBLE_QUOTE);
          } else if (c == '\'') {
            inString_ = true;
            setStacks(ParseSection.SINGLE_QUOTE);
          } else if (c == ';') {
            processEndOfPropertyValue();
          } else if (c == '}') {
            //end of block;
            processEndOfPropertyValue();
            if (currentTopSection_ == ParseSection.AT_RULE) {
              AtRuleMutant arm = new AtRuleMutant();
              arm.putProperties(currentBlock_);
              currentBlock_ = null;
              styleSheet_.addAtRule(currentAtRule_, arm);
              currentAtRule_ = null;
            } else if (currentTopSection_ == ParseSection.SELECTOR_OR_BLOCK) {
              Set<Entry<String,SpecifiedValue<?>>> entries = currentBlock_.entrySet();
              for (Entry<String,SpecifiedValue<?>> e: entries) {
                for (Selector cg: currentSelectors_) {
                  styleSheet_.putValue(cg, e.getKey(), e.getValue());
                }
              }
            }
            //done with selectors section
            popStacks(sections_.size());
          } else {
            sb_.append(c);
          }
      }
    }
  }

  private void processQuote(char c, char quoteType) {
    if (BackslashEscape.isBackslash(c)) {
      backslashEscape_ = new BackslashEscape();
    } else {
      if (backslashEscape_ != null) {
        if (!backslashEscape_.append(c)) {
          sb_.append(backslashEscape_.toChar());
          backslashEscape_ = null;
        }
      } else {
         if (c == quoteType){
          sb_.append(c);
          inString_ = false;
          popStacks(1);
        } else if (c == ';'){
          //error semicolons must be escaped;
          currentErrorSectionStart_ = currentTopSectionStart_;
          currentErrorLocation_ = new LineChar(lineNumber_, characterNumber_);
          inString_ = false;
        } else {
          sb_.append(c);
        }
      }
    }
  }

  private void processPair(char c, char pairType) {
    if (BackslashEscape.isBackslash(c)) {
      backslashEscape_ = new BackslashEscape();
    } else {
      if (backslashEscape_ != null) {
        if (!backslashEscape_.append(c)) {
          sb_.append(backslashEscape_.toChar());
          backslashEscape_ = null;
        }
      } else {
        if (c == pairType){
          sb_.append(c);
          popStacks(1);
        } else if (inString_) {
          sb_.append(c);
        } else if (c == ';'){
          //error semicolons must be escaped;
          currentErrorSectionStart_ = currentTopSectionStart_;
          currentErrorLocation_ = new LineChar(lineNumber_, characterNumber_);
        } else {
          sb_.append(c);
        }
      }
    }
  }
  
  private void processEndOfPropertyValue() {
    String value = sb_.toString();
    if (expectedCurrentBlock_ != null) {
      CssType type = expectedCurrentBlock_.get(currentPropertyName_);
      if (type != null) {
        switch (type) {
          case PX:
            processPxValue(value);
            break;
          case PCT:
            processPctValue(value);
            break;
          case INTEGER:
            processIntegerValue(value);
            break;
          case DOUBLE:
            processDoubleValue(value);
            break;
          default:
            currentBlock_.put(currentPropertyName_, new SpecifiedValue<String>(CssType.ANY, value));
        }
      } else {
        currentBlock_.put(currentPropertyName_, new SpecifiedValue<String>(CssType.ANY, value));
      }
    } else {
      currentBlock_.put(currentPropertyName_, new SpecifiedValue<String>(CssType.ANY, value));
    }
  }

  private void processIntegerValue(String value) {
    StringBuilder sb = new StringBuilder();
    char [] valueChars = value.toCharArray();
    for (int i = 0; i < valueChars.length; i++) {
      char vc = valueChars[i];
      if ("p".equalsIgnoreCase("" + vc) || "x".equalsIgnoreCase("" + vc)) {
        //don't add it
      } else {
        sb.append(vc);
      }
    }
    value = sb.toString().trim();
    currentBlock_.put(currentPropertyName_, new SpecifiedValue<Integer>(CssType.INTEGER, new Integer(value)));
  }
  
  private void processDoubleValue(String value) {
    StringBuilder sb = new StringBuilder();
    char [] valueChars = value.toCharArray();
    for (int i = 0; i < valueChars.length; i++) {
      char vc = valueChars[i];
      if ("p".equalsIgnoreCase("" + vc) || "x".equalsIgnoreCase("" + vc)) {
        //don't add it
      } else {
        sb.append(vc);
      }
    }
    value = sb.toString().trim();
    currentBlock_.put(currentPropertyName_, new SpecifiedValue<Double>(CssType.DOUBLE, new Double(value)));
  }
  
  private void processPctValue(String value) {
    StringBuilder sb = new StringBuilder();
    char [] valueChars = value.toCharArray();
    for (int i = 0; i < valueChars.length; i++) {
      char vc = valueChars[i];
      if ("p".equalsIgnoreCase("" + vc) || "x".equalsIgnoreCase("" + vc)) {
        //don't add it
      } else {
        sb.append(vc);
      }
    }
    value = sb.toString().trim();
    currentBlock_.put(currentPropertyName_, new SpecifiedValue<Double>(CssType.PCT, new Double(value)));
  }
  
  private void processPxValue(String value) {
    StringBuilder sb = new StringBuilder();
    char [] valueChars = value.toCharArray();
    for (int i = 0; i < valueChars.length; i++) {
      char vc = valueChars[i];
      if ("p".equalsIgnoreCase("" + vc) || "x".equalsIgnoreCase("" + vc)) {
        //don't add it
      } else {
        sb.append(vc);
      }
    }
    value = sb.toString().trim();
    currentBlock_.put(currentPropertyName_, new SpecifiedValue<Integer>(CssType.PX, new Integer(value)));
  }
  
  private void appendWarning() {
    String errorMessage = i18nConstants_.getParsingErrorInvalidSection();
    if (currentErrorSectionStart_ != null) {
      errorMessage = errorMessage.replaceAll("<LS/>", "" +currentErrorSectionStart_.getLine());
    }
    if (currentErrorSectionEnd_ != null) {
      errorMessage = errorMessage.replaceAll("<LE/>", "" +currentErrorSectionEnd_.getLine());
    }
    if (currentErrorLocation_ != null) {
      errorMessage = errorMessage.replaceAll("<C/>", "" +currentErrorLocation_.getCharacter());
      errorMessage = errorMessage.replaceAll("<L/>", "" +currentErrorLocation_.getLine());
    }
    currentErrorSectionStart_ = null;
    currentErrorSectionEnd_ = null;
    currentErrorLocation_ = null;
    styleSheet_.addWarning(new IllegalStateException(errorMessage));
  }

  private void popStacks(int count) {
    for (int i = 0; i < count; i++) {
      sections_.pop();
      sectionStarts_.pop();
    }
  }
  
  private void setStacks(ParseSection section) {
    sections_.push(section);
    sectionStarts_.push(new LineChar(lineNumber_, characterNumber_));
  }

  private void processSelectorGroup(char c, ParseSection currentSection) {
    if (BackslashEscape.isBackslash(c)) {
      backslashEscape_ = new BackslashEscape();
    }
    
    if (backslashEscape_ != null) {
      if (backslashEscape_.append(c)) {
        //note keeping the escaped original content
        sb_.append(c);
      } else {
        backslashEscape_ = null;
      }
    } else {
      switch (currentSection) {
        case BRACKET:
          processPair(c, ']');
          break;
        case PARENTHESES:
          processPair(c, ')');
          break;
        case BRACES:
          processPair(c, '}');
          break;
        case DOUBLE_QUOTE:
          processQuote(c, '"');
          break;
        case SINGLE_QUOTE:
          processQuote(c, '\'');
          break;
        case SELECTOR_GROUP:

          if (c == ',') {
            parseSequenceOfSimpleSelectors(); 
            addLinkAndSelector(Combinator.NONE);
          } else if (c == '{') {
            parseSequenceOfSimpleSelectors(); 
            addLinkAndSelector(Combinator.NONE);
            setStacks(ParseSection.BLOCK);
            currentBlock_ = new HashMap<String,SpecifiedValue<?>>();
            if (expectedCss_ != null) {
              expectedCurrentBlock_ = expectedCss_.getProperties(currentSelectors_);
            }
          } else if (c == '[') {
            setStacks(ParseSection.BRACKET);
          } else if (c == '(') {
            setStacks(ParseSection.PARENTHESES);
            inString_ = true;
            setStacks(ParseSection.DOUBLE_QUOTE);
          } else if (c == '\'') {
            inString_ = true;
            setStacks(ParseSection.SINGLE_QUOTE);
          } else if (Character.isWhitespace(c)) {
            if (currentCombinator_ == null) {
              currentCombinator_ = Combinator.DIRECT_DESCENDANT;
            }
          } else if (c == '*') {
            currentCombinator_ = Combinator.ANY_DESCENDANT;
          } else if (c == '>') {
            currentCombinator_ = Combinator.CHILD;
          } else if (c == '+') {
            currentCombinator_ = Combinator.ADJACENT_SIBLING;
          } else if (c == '~') {
            currentCombinator_ = Combinator.GENERAL_SIBLING;
          } else {
            if (currentCombinator_ != null) {
              parseSequenceOfSimpleSelectors(); 
              addLinkAndSelector(currentCombinator_);
              currentCombinator_ = null;
            }
            sb_.append(c);
          }
      }
    }
  }

  private void addLinkAndSelector(Combinator combinator) {
    addLink(combinator);
    addSelector();
  }
  
  private void addLink(Combinator combinator) {
    currentSelectorLinks_.add(new CssLink(combinator, currentSequenceOfSimpleSelectors_));
    currentSequenceOfSimpleSelectors_ = null;
  }

  private void parseSequenceOfSimpleSelectors() {
    String seqOfSimpleSelectors = sb_.toString(); 
    sb_ = new StringBuilder();
    currentSequenceOfSimpleSelectors_ = sequenceOfSimpleSelectorsParser_.parse(seqOfSimpleSelectors);
  }

  private void addSelector() {
    Selector selector = new Selector(currentSelectorLinks_);
    currentSelectors_.add(selector);
    currentSelectorLinks_.clear();
  }


  private void processComment(char c, String commentEnd) {
    int index = commentEnd.indexOf(c);
    if (index != -1) {
      if (sb_.length() == 0 && index == 0) {
        sb_.append(c);
      } else if (sb_.length() > commentEnd.length()) {
        sb_ = new StringBuilder();
      } else {
        sb_.append(c);
        if (commentEnd.equals(sb_.toString())) {
          //end of comment
          sb_ = new StringBuilder();
          sections_.pop();
          sectionStarts_.pop();
        }
      }
    } else {
      //ignore all comments for the data model
      if (sb_.length() >= 1) {
        sb_ = new StringBuilder();
      }
    } 
  }

}
