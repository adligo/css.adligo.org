package org.adligo.css.shared.models.common;

import org.adligo.css.shared.models.CssType;

/**
 * This is simple SpecifiedValue class
 * for everything after the property name,
 * which may be processed into a Integer or Double.
 * 
 * @author scott
 *
 * @param <T>
 */
public class SpecifiedValue <T>  {
  private CssType type_;
  /**
   * when the value is NOT a string
   * the original string from the css file
   * is stored as content.
   */
  private String content_;
  private T value_;
  
  public SpecifiedValue(CssType type, T value) {
    type_ = type;
    value_ = value;
  }

  public SpecifiedValue(CssType type, T value, String content) {
    type_ = type;
    value_ = value;
    content_ = content;
  }
  
  public CssType getType() {
    return type_;
  }

  public T getValue() {
    return value_;
  }
  
  public String getContent() {
    return content_;
  }
}
