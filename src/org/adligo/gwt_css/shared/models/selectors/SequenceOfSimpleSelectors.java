package org.adligo.gwt_css.shared.models.selectors;

import java.util.Collections;
import java.util.List;

public class SequenceOfSimpleSelectors implements I_SequenceOfSimpleSelectors {
  private ElementName elementName_;
  /**
   * 
   */
  private List<String> classNames_;
  
  private List<CssAttribute> attributes_;
  /**
   * I assume multilple id elements would look like;
   * hr#id123#id234
   * 
   * Although the notion of id generally implies uniqueness, this is in the section
   * 6.5 http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#attribute-selectors;
   * 
   * If an element has multiple ID attributes, all of them must be treated as IDs for that element for 
   * the purposes of the ID selector. Such a situation could be reached using mixtures of 
   * xml:id, DOM3 Core, XML DTDs, and namespace-specific knowledge.
   */
  private List<String> ids_;
  
  private List<CssPseudoClass<?>> pseudoClasses_;
  
  /**
   * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#w3cselgrammar
   * 10.1
   * pseudo
   * '::' starts a pseudo-element, ':' a pseudo-class 
   * Exceptions: :first-line, :first-letter, :before and :after. 
   * Note that pseudo-elements are restricted to one per selector and 
   * occur only in the last simple_selector_sequence. 
    * : ':' ':'? [ IDENT | functional_pseudo ]
   */
  private String pseudoElement_;

  public SequenceOfSimpleSelectors(I_SequenceOfSimpleSelectors other) {
    SequenceOfSimpleSelectorsMutant sm = new SequenceOfSimpleSelectorsMutant(other);
    attributes_ = Collections.unmodifiableList(other.getAttributes());
    elementName_ = sm.getElementName();
    classNames_ = Collections.unmodifiableList(other.getClassNames());
    ids_ = Collections.unmodifiableList(other.getIds());
    pseudoClasses_ = Collections.unmodifiableList(other.getPseudoClasses());
    pseudoElement_ = other.getPseudoElement();
  }
  
  /**
   * A simple constructor to attempt to make this 
   * API easy to use, many more like this are required.
   * 
   * @param className
   */
  public SequenceOfSimpleSelectors(String className) {
    initEmpties();
    if (className != null) {
      classNames_ = Collections.singletonList(className);
    }
  }
  
  /**
   * A simple constructor to attempt to make this 
   * API easy to use, many more like this are required.
   * 
   * @param className
   * @param id
   */
  public SequenceOfSimpleSelectors(String className, String id) {
    initEmpties();
    if (className != null) {
      classNames_ = Collections.singletonList(className);
    }
    if (id != null) {
      ids_ = Collections.singletonList(id);
    }
  }
  
  private void initEmpties() {
    attributes_ = Collections.emptyList();
    classNames_ = Collections.emptyList();
    ids_ = Collections.emptyList();
    pseudoClasses_ = Collections.emptyList();
  }
  
  public ElementName getElementName() {
    return elementName_;
  }

  public List<String> getClassNames() {
    return classNames_;
  }

  public List<CssAttribute> getAttributes() {
    return attributes_;
  }

  public List<String> getIds() {
    return ids_;
  }

  public List<CssPseudoClass<?>> getPseudoClasses() {
    return pseudoClasses_;
  }

  public String getPseudoElement() {
    return pseudoElement_;
  }
  @Override
  public int hashCode() {
    return SequenceOfSimpleSelectorsMutant.hashCode(this);
  }
  @Override
  public boolean equals(Object obj) {
    return SequenceOfSimpleSelectorsMutant.equals(this, obj);
  }
}
