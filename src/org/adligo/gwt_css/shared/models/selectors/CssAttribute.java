package org.adligo.gwt_css.shared.models.selectors;

public class CssAttribute {
  private CssAttributeType type_;
  private String name_;
  /**
   * with out the double quotes if they 
   * were present in the css file
   */
  private String value_;
  
  public CssAttribute(String name) {
    name_ = name;
    type_ = CssAttributeType.NAME;
  }
  
  public CssAttribute(String name, CssAttributeType type, String value) {
    name_ = name;
    type_ = type;
    value_ = value;
  }
  
  public CssAttributeType getType() {
    return type_;
  }
  
  public String getName() {
    return name_;
  }
  
  public String getValue() {
    return value_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
    result = prime * result + ((type_ == null) ? 0 : type_.hashCode());
    result = prime * result + ((value_ == null) ? 0 : value_.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CssAttribute other = (CssAttribute) obj;
    if (name_ == null) {
      if (other.name_ != null)
        return false;
    } else if (!name_.equals(other.name_))
      return false;
    if (type_ != other.type_)
      return false;
    if (value_ == null) {
      if (other.value_ != null)
        return false;
    } else if (!value_.equals(other.value_))
      return false;
    return true;
  }
  
}
