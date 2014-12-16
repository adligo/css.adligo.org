package org.adligo.gwt_css.shared.models.selectors;

import java.util.List;

/**
 * Note this is a like a collection simple selectors,
 * which represents when simple selectors are appended i.e.;
 * a.foo#123
 * Represents three simple selectors a element, class and id.
 * 
 * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#simple-selectors-dfn
 * 4. Selector syntax
 * A selector is a chain of one or more sequences of simple selectors separated by combinators. 
 * One pseudo-element may be appended to the last sequence of simple selectors in a selector.
 * 
 * @author scott
 */
public interface I_SequenceOfSimpleSelectors {
  /**
   * This is either the name of the element (i.e. a type selector)
   * or the universal selector (*) which may have been implied.
   * 
   */
  public abstract ElementName getElementName();

  public abstract List<String> getClassNames();

  public abstract List<CssAttribute> getAttributes();

  public abstract List<String> getIds();

  public abstract List<CssPseudoClass<?>> getPseudoClasses();

  public abstract String getPseudoElement();

}