package org.adligo.css.shared.models.selectors;

public class NamespaceAndElement {
  public static final CssNamespace DEFAULT_NAMESPACE = new CssNamespace(CssNamespaceType.ANY);
  private CssNamespace namespace_ = DEFAULT_NAMESPACE;
  private String name_;
  
  public NamespaceAndElement(String name) {
    name_ = name;
  }

  public NamespaceAndElement(CssNamespace namespace) {
    namespace_ = namespace;
  }
  
  public NamespaceAndElement(String name, CssNamespace namespace) {
    name_ = name;
    namespace_ = namespace;
  }
  
  public CssNamespace getNamespace() {
    return namespace_;
  }
  
  public String getName() {
    return name_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
    result = prime * result + ((namespace_ == null) ? 0 : namespace_.hashCode());
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
    NamespaceAndElement other = (NamespaceAndElement) obj;
    if (name_ == null) {
      if (other.name_ != null)
        return false;
    } else if (!name_.equals(other.name_))
      return false;
    if (namespace_ == null) {
      if (other.namespace_ != null)
        return false;
    } else if (!namespace_.equals(other.namespace_))
      return false;
    return true;
  }
  
}
