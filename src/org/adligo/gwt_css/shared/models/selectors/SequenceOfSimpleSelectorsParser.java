package org.adligo.gwt_css.shared.models.selectors;

import org.adligo.gwt_css.shared.models.common.BackslashEscape;


/**
 * This class parses a sequence of single selectors,
 * it has NO knowledge of comparators.
 * 
 * @author scott
 *
 */
public class SequenceOfSimpleSelectorsParser {

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
   *   bracket, parenthesis, or double quote
   * @return
   */
  public SequenceOfSimpleSelectors parse(String sequenceOfSimpleSelectorsContent) {
    SequenceOfSimpleSelectorsMutant seq = new SequenceOfSimpleSelectorsMutant();
    StringBuilder sb = new StringBuilder();
    
    //any is the default
    CssNamespace namespace = new CssNamespace("*", CssNamespaceType.ANY);
    
    /**
     * as far as I can tell a selector must pertain 
     * to at least a element (any element) or a class,
     * 
     * A simple selector is either a type selector or universal selector followed 
     * immediately by zero or more attribute selectors, ID selectors, 
     * or pseudo-classes, in any order. 
     * The simple selector matches if all of its components match.
     */
    boolean inClass = false;
    boolean inAttribute = false;
    boolean inId = false;
    boolean inColonPart = false;
    BackslashEscape backEsc = null;
    
    char [] chars = sequenceOfSimpleSelectorsContent.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
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
          if (seq.getElementName() == null) {
            if (c == '.') {
              setElement(seq, namespace, soFar);
              inClass = true;
            } else if (c == '[') {
              setElement(seq, namespace, soFar);
              inAttribute = true;
            } else if (c == ':') {
              setElement(seq, namespace, soFar);
              inColonPart = true;
            } else if (c == '|') {
              String ns = sb.toString();
              ns = ns.trim();
              if (ns.length() == 0) {
                namespace = new CssNamespace(CssNamespaceType.NONE);
              } else if ("*".equals(ns)) {
                namespace = new CssNamespace(CssNamespaceType.ANY);
              } else {
                namespace = new CssNamespace(ns, CssNamespaceType.NAMED);
              }
              sb = new StringBuilder();
            } else {
              sb.append(c);
            }
          } else if (isNewSimpleSelector(c)) {
            
          } 
    }
  }

  private void setElement(SequenceOfSimpleSelectorsMutant seq, CssNamespace namespace, String soFar) {
    if (namespace != null) {
      seq.setElementName(new ElementName(soFar, namespace));
    } else {
      if (soFar.length() == 0) {
        seq.setElementName(new ElementName("*"));
      } else {
        seq.setElementName(new ElementName(soFar));
      }
    }
  }
}
