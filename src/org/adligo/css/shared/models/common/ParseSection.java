package org.adligo.css.shared.models.common;

public enum ParseSection {
  /**
   * backslash to backslash
   * ie;
   *  \/* to *\/
   */
  COMMENT, 
  /**
   * <!-- to -->
   */
  COMMENT_SGML, 
  /**
   * @ to semicolon or block,
   * In CSS, identifiers (including element names, classes, and IDs in selectors) can contain 
   * only the characters [a-zA-Z0-9] and ISO 10646 characters U+00A0 and higher, plus the 
   * hyphen (-) and the underscore (_); they cannot start with a digit, two hyphens, 
   * or a hyphen followed by a digit. Identifiers can also contain escaped characters and 
   * any ISO 10646 character as a numeric code (see next item). For instance, the identifier 
   * "B&W?" may be written as "B\&W\?" or "B\26 W\3F".
   */
  AT_RULE,
  /**
   * The identifier part of an AtRule.
   */
  AT_RULE_IDENTIFER, 
  /**
   * everything after the AT_RULE_IDENTIFER identifier
   * until a semicolon or the end of the block
   */
  AT_RULE_CONTENT, 
  /**
   * This is the parent section 
   * of selector_group and block 
   */
  SELECTOR_OR_BLOCK,
  /**
   * names of a selector and combinators
   * which eventually get parsed again into
   * CssChoiceGroups.
   */
  SELECTOR_GROUP,
  /** 
   * can be under a AT_RULE section or SELECTOR_OR_BLOCK section
   * { to }
   */
  BLOCK,
  /**
   * Parsing a property in a block i.e. the following part;
   * font-weight
   * of the following style sheet;
   * h1 { font-weight: bold }
   */
  PROPERTY_NAME,
  /**
   * Parsing a property in a block i.e. the following part;
   * bold
   * of the following style sheet;
   * h1 { font-weight: bold }
   */
  PROPERTY_VALUE,
  /**
   * ' to '
   */
  SINGLE_QUOTE, 
  /**
   * " to "
   */
  DOUBLE_QUOTE, 
  /** 
   * [ to ]
   */
  BRACKET, 
  /**
   * { to } under a block;
   */
  BRACES, 
  /**
   * ( to )
   */
  PARENTHESES
}
