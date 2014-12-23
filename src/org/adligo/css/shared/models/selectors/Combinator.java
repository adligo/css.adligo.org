package org.adligo.css.shared.models.selectors;

public enum Combinator {
  /**
   * This is a selector with no combinator
   * either because it is the only selector before 
   * a block, or because it is a single selector
   * uniquely identified by a comma delimitor.
   */
  NONE, 
  /**
   * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#combinators
   * section 8.1
   * div p
   */
  DIRECT_DESCENDANT, 
  /**
   * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#combinators
   * section 8.1 ie
   * div * p
   */
  ANY_DESCENDANT, 
  /**
   * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#combinators
   * section 8.1
   */
  CHILD, 
  /**
   * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#combinators
   * section 8.2.1
   */
  ADJACENT_SIBLING, 
  /**
   * http://www.w3.org/TR/2009/PR-css3-selectors-20091215/#combinators
   * section 8.3.2
   */
  GENERAL_SIBLING
  
}
