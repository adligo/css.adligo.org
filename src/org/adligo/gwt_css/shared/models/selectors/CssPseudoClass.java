package org.adligo.gwt_css.shared.models.selectors;

/**
 * @author scott
 *
 * @param <T> usually a string
 * unless there is a expected one, with a specific 
 * value type (ie nth-child(an+b) could have
 * it's own value type.
 */
public class CssPseudoClass<T> {
  private String name_;
  private T value_;
  
  public CssPseudoClass(String name) {
    name_ = name;
  }
  
  public CssPseudoClass(String name, T value) {
    name_ = name;
    value_ = value;
  }
  
  public String getName() {
    return name_;
  }
  
  public T getValue() {
    return value_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
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
    CssPseudoClass other = (CssPseudoClass) obj;
    if (name_ == null) {
      if (other.name_ != null)
        return false;
    } else if (!name_.equals(other.name_))
      return false;
    if (value_ == null) {
      if (other.value_ != null)
        return false;
    } else if (!value_.equals(other.value_))
      return false;
    return true;
  }
  
}
