package org.adligo.css.shared.models.selectors;

import org.adligo.css.shared.models.common.BackslashEscape;


/**
 * This class parses a sequence of single selectors,
 * it has NO knowledge of comparators.
 * 
 * @author scott
 *
 */
public class SequenceOfSimpleSelectorsParser {
  private boolean inClass_ = false;
  private boolean inAttribute_ = false;
  private boolean inId_ = false;
  private boolean inColonPart_ = false;
  
  private boolean inParenthesis_ = false;
  private boolean inDoubleQuote_ = false;
  /**
   * 
   * @param sequenceOfSimpleSelectorsContent
   * 
   *   Ok sequenceOfSimpleSelectorsContent should not have
   *   any nesting;
   *   http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#combinators
   *   2. Selectors (which is non-normative, but a deep dive
   *   into the rest of the spec proves);
   *   your in a simple selector which may have a non nested  
   *   bracket (attribute), parenthesis, or double quote
   * @return
   */
  public SequenceOfSimpleSelectors parse(String sequenceOfSimpleSelectorsContent) {
    resetForParse();
    SequenceOfSimpleSelectorsMutant seq = new SequenceOfSimpleSelectorsMutant();
    StringBuilder sb = new StringBuilder();
    
    //any is the default
    CssNamespace namespace = new CssNamespace(CssNamespaceType.ANY);
    
    /**
     * as far as I can tell a selector must pertain 
     * to at least a element (any element) or a class,
     * 
     * A simple selector is either a type selector or universal selector followed 
     * immediately by zero or more attribute selectors, ID selectors, 
     * or pseudo-classes, in any order. 
     * The simple selector matches if all of its components match.
     */

    BackslashEscape backEsc = null;
    
    char [] chars = sequenceOfSimpleSelectorsContent.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      boolean inClass = inClass_;
      boolean inAttribute = inAttribute_;
      boolean inColonPart = inColonPart_;
      
      if (!inDoubleQuote_) {
        if (BackslashEscape.isBackslash(c)) {
          backEsc = new BackslashEscape();
        } else {
          if (backEsc != null) {
            if (!backEsc.append(c)) {
              sb.append(backEsc.toChar());
              backEsc = null;
            }
          } else {
            String soFar = sb.toString();
            if (seq.getNamespaceAndElement() == null) {
              if (c == '.') {
                setElement(seq, namespace, soFar);
                inClass_ = true;
              } else if (c == '[') {
                setElement(seq, namespace, soFar);
                inAttribute_ = true;
              } else if (c == ':') {
                setElement(seq, namespace, soFar);
                inColonPart_ = true;
              } else if (c == '|') {
                String ns = sb.toString();
                ns = ns.trim();
                if (ns.length() == 0) {
                  namespace = new CssNamespace(CssNamespaceType.NONE);
                } else if ("*".equals(ns)) {
                  namespace = new CssNamespace(CssNamespaceType.ANY);
                } else {
                  namespace = new CssNamespace(ns);
                }
                sb = new StringBuilder();
              } else {
                sb.append(c);
              }
            } else if (isNewSimpleSelector(c, soFar, seq)) {
              
            } else {
              sb.append(c);
            }
          }
        }
      }
    }
    String soFar = sb.toString();
    if (soFar.trim().length() == 0) {
      soFar = null;
    }
    if (soFar != null) {
      if (inClass_) {
        seq.addClassName(soFar);
      } else if (inAttribute_) {
        
      } else if (inColonPart_) {
        
      } else {
        addElementNameIfNotPresent(seq, namespace, soFar);
      }
    }
    return new SequenceOfSimpleSelectors(seq);
  }

  private void resetForParse() {
    inClass_ = false;
    inAttribute_ = false;
    inId_ = false;
    inColonPart_ = false;
    
    inParenthesis_ = false;
    inDoubleQuote_ = false;
  }

  private void addSimpleSelector(String soFar, SequenceOfSimpleSelectorsMutant seq) {
    if (soFar.trim().length() == 0) {
      soFar = null;
    }
    if (soFar != null) {
      if (inClass_) {
        seq.addClassName(soFar);
      } else if (inAttribute_) {
        
      } else if (inColonPart_) {
        
      }
    }
  }
  /**
   * checks if a unescaped character is the
   * start of a new simple selector
   * @param c
   * @return
  */
  private boolean isNewSimpleSelector(char c, String soFar, SequenceOfSimpleSelectorsMutant seq ) {
    if (c == '.') {
      addSimpleSelector(soFar, seq);
      clearSimpleSelectorSection();
      inClass_ = true;
      return true;
    } else if (c == '[') {
      addSimpleSelector(soFar, seq);
      clearSimpleSelectorSection();
      inAttribute_ = true;
      return true;
    } else if (c == ':') {
      addSimpleSelector(soFar, seq);
      clearSimpleSelectorSection();
      inColonPart_ = true;
      return true;
    }
    return false;
  }

  private void clearSimpleSelectorSection() {
    inClass_ = false;
    inAttribute_ = false;
    inColonPart_ = false;
  }
  
  /**
   * 
   * @param seq
   * @param namespace should never be null
   * @param soFar
   */
  private void setElement(SequenceOfSimpleSelectorsMutant seq, CssNamespace namespace, String soFar) {
    if (soFar.trim().length() == 0) {
      soFar = null;
    }
    if (soFar == null) {
      seq.setNamespaceAndElement(new NamespaceAndElement(namespace));
    } else {
      seq.setNamespaceAndElement(new NamespaceAndElement(soFar));
    }
  }
  
  /**
   * 
   * @param seq
   * @param namespace may be null if none has been discovered
   * @param bufferContent
   */
  private void addElementNameIfNotPresent(SequenceOfSimpleSelectorsMutant seq, CssNamespace namespace, String bufferContent) {
    if (seq.getNamespaceAndElement() != null) {
      return;
    }
    if (!SequenceOfSimpleSelectorsMutant.hasNonElementNameSimpleSelector(seq)) {
      if (namespace == null) {
        seq.setNamespaceAndElement(new NamespaceAndElement(bufferContent));
      } else {
        seq.setNamespaceAndElement(new NamespaceAndElement(bufferContent, namespace));
      }
    }
  }
}
