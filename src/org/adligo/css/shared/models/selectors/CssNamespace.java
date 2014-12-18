package org.adligo.css.shared.models.selectors;

public class CssNamespace {
  public static final String NAMED_CSS_NAMESPACE_MUST_USE_THE_STRING_NAME_CONSTRUCTOR = "Named CssNamespace must use the String name constructor.";
  private String name_;
  private CssNamespaceType type_;
  
  public CssNamespace(String name) {
    name_ = name;
    type_ = CssNamespaceType.NAMED;
  }
  
  public CssNamespace(CssNamespaceType type) {
    if (type == CssNamespaceType.NAMED) {
      throw new IllegalArgumentException(NAMED_CSS_NAMESPACE_MUST_USE_THE_STRING_NAME_CONSTRUCTOR);
    }
    type_ = type;
  }
  
  public String getName() {
    return name_;
  }
  public CssNamespaceType getType() {
    return type_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
    result = prime * result + ((type_ == null) ? 0 : type_.hashCode());
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
    CssNamespace other = (CssNamespace) obj;
    if (name_ == null) {
      if (other.name_ != null)
        return false;
    } else if (!name_.equals(other.name_))
      return false;
    if (type_ != other.type_)
      return false;
    return true;
  }
}
