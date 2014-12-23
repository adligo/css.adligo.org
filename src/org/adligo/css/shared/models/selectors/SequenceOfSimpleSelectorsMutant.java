package org.adligo.css.shared.models.selectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SequenceOfSimpleSelectorsMutant implements I_SequenceOfSimpleSelectors {

  private NamespaceAndElement namespaceAndElement_;
  /**
   * 
   */
  private List<String> classNames_;
  
  private List<CssAttribute> attributes_;
  /**
   * I assume multiple id elements would look like;
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

  public static int hashCode(I_SequenceOfSimpleSelectors selector) {
    final int prime = 31;
    int result = 1;
    List<CssAttribute> attributes =  selector.getAttributes();
    if (attributes != null) {
      for (CssAttribute attribute: attributes) {
        result = prime * result + attribute.hashCode();
      }
    }
    List<String> classNames = selector.getClassNames();
    if (classNames != null) {
      for (String cn: classNames) {
        result = prime * result + cn.hashCode();
      }
    }
    NamespaceAndElement namespaceAndElement = selector.getNamespaceAndElement();
    result = prime * result + ((namespaceAndElement == null) ? 0 : namespaceAndElement.hashCode());
    List<String> ids = selector.getIds();
    if (ids != null) {
      for (String id: ids) {
        result = prime * result + id.hashCode();
      }
    }
    List<CssPseudoClass<?>> pseudoClasses = selector.getPseudoClasses();
    if (pseudoClasses != null) {
      for (CssPseudoClass<?> ps : pseudoClasses) {
        result = prime * result + ps.hashCode();
      }
    }
    String pseudoElement = selector.getPseudoElement();
    result = prime * result + ((pseudoElement == null) ? 0 : pseudoElement.hashCode());
    return result;
  }
  
  public static boolean equals(I_SequenceOfSimpleSelectors me, Object obj) {
    if (me == obj)
      return true;
    if (obj == null)
      return false;
    try {
      I_SequenceOfSimpleSelectors other = (I_SequenceOfSimpleSelectors) obj;
      List<CssAttribute> attributes = me.getAttributes();
      List<CssAttribute> otherAttributes = other.getAttributes(); 
      if (attributes == null) {
        if (otherAttributes != null) {
          if (otherAttributes.size() != 0) {
            return false;
          }
        }
      } else {
       if (otherAttributes == null) {
         if (attributes.size() == 0) {
           return false;
         }
       } else if (attributes.size() != otherAttributes.size()) {
         return false;
       } else {
         int index = 0;
         for (CssAttribute attribute: attributes) {
           if (!attribute.equals(otherAttributes.get(index++))) {
             return false;
           }
         }
       }
      }
      List<String> classNames = me.getClassNames();
      List<String> otherClassNames = other.getClassNames();
      if (classNames == null) {
        if (otherClassNames != null) {
          if (otherClassNames.size() != 0) {
            return false;
          }
        }
      } else {
        if (otherClassNames == null) {
          if (classNames.size() != 0) {
            return false;
          }
        }
        if (classNames.size() != otherClassNames.size()) {
          return false;
        } else {
          int index = 0;
          for (String cn: classNames) {
            if (!cn.equals(otherClassNames.get(index++))) {
              return false;
            }
          }
        }
      }
      NamespaceAndElement namespaceAndElement = me.getNamespaceAndElement();
      NamespaceAndElement otherElementName = other.getNamespaceAndElement();
      if (namespaceAndElement == null) {
        if (otherElementName != null) {
          return false;
        }
      } else if (!namespaceAndElement.equals(otherElementName))
        return false;
      List<String> ids = me.getIds();
      List<String> otherIds = other.getIds();
      if (ids == null) {
        if (otherIds != null) {
          if (otherIds.size() != 0) {
            return false;
          }
        }
      } else {
        if (otherIds == null) {
          if (ids.size() != 0) {
            return false;
          }
        } else if (ids.size() != otherIds.size()) {
          return false;
        } else {
          int index = 0;
          for (String id: ids) {
            if (!id.equals(otherIds.get(index++))) {
              return false;
            }
          }
        }
      }
      List<CssPseudoClass<?>> pseudoClasses = me.getPseudoClasses();
      List<CssPseudoClass<?>> otherPseudoClasses = other.getPseudoClasses();
      if (pseudoClasses == null) {
        if (otherPseudoClasses != null) {
          if (otherPseudoClasses.size() != 0) {
            return false;
          }
        }
      } else {
        if (otherPseudoClasses == null) {
          if (pseudoClasses.size() != 0) {
            return false;
          }
        } else if (pseudoClasses.size() != otherPseudoClasses.size()) {
          return false;
        } else {
          int index = 0;
          for (CssPseudoClass<?> c: pseudoClasses) {
            if (!c.equals(otherPseudoClasses.get(index++))) {
              return false;
            }
          }
        }
      }
      String pseudoElement = me.getPseudoElement();
      String otherPseudoElement = other.getPseudoElement();
      if (pseudoElement == null) {
        if (otherPseudoElement != null)
          return false;
      } else if (!pseudoElement.equals(otherPseudoElement))
        return false;
    } catch (ClassCastException x) {
      return false;
    }
    return true;
  }
  
  /**
   * This method first checks for a element name
   * @param seq
   * @return
   */
  public static boolean hasNonElementNameSimpleSelector(I_SequenceOfSimpleSelectors seq) {
    if (seq.getAttributes().size() > 0) {
      return true;
    }
    List<String> cns = seq.getClassNames();
    if (cns != null) {
      if (cns.size() > 0) {
        return true;
      }
    }
    List<String> ids = seq.getIds();
    if (ids != null) {
      if (ids.size() > 0) {
        return true;
      }
    }
    List<CssPseudoClass<?>> pcs = seq.getPseudoClasses();
    if (pcs != null) {
      if (pcs.size() > 0) {
        return true;
      }
    }
    if (seq.getPseudoElement() != null) {
      return true;
    }
    return false;
    
  }
  public SequenceOfSimpleSelectorsMutant() {}
  
  public SequenceOfSimpleSelectorsMutant(I_SequenceOfSimpleSelectors selector) {
    setNamespaceAndElement(selector.getNamespaceAndElement());
    setAttributes(selector.getAttributes());
    setClassNames(selector.getClassNames());
    setIds(selector.getIds());
    setPseudoClasses(selector.getPseudoClasses());
    setPseudoElement(selector.getPseudoElement());
  }
  
  public void addAttribute(CssAttribute attribute) {
    bankAttributes();
    attributes_.add(attribute);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.selectors.I_Selector#getElementName()
   */
  @Override
  public NamespaceAndElement getNamespaceAndElement() {
    return namespaceAndElement_;
  }

  /**
   * this should allow null for class name
   * only selectors etc.
   * @param namespaceAndElement
   */
  public void setNamespaceAndElement(NamespaceAndElement namespaceAndElement) { 
    this.namespaceAndElement_ = namespaceAndElement;
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.selectors.I_Selector#getClassNames()
   */
  @Override
  public List<String> getClassNames() {
    return classNames_;
  }

  public void addClassName(String className) {
    bankClassNames();
    classNames_.add(className);
  }
 
  public void addId(String id) {
    bankIds();
    ids_.add(id);
  }
  
  public void addPseudoClass(CssPseudoClass<?> pc) {
    bankPseudoClasses();
    pseudoClasses_.add(pc);
  }
  
  public void setClassNames(Collection<String> classNames) {
    bankClassNames();
    classNames_.clear();
    if (classNames != null) {
      for (String name: classNames) {
        if (name != null) {
          classNames_.add(name);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.selectors.I_Selector#getAttributes()
   */
  @Override
  public List<CssAttribute> getAttributes() {
    if (attributes_ == null) {
      return Collections.emptyList();
    }
    return attributes_;
  }
  
  public void setAttributes(List<CssAttribute> attributes) {
    bankAttributes();
    attributes_.clear();
    if (attributes != null) {
      for (CssAttribute attribute: attributes) {
        if (attribute != null) {
          attributes_.add(attribute);
        }
      }
    }
  }



  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.selectors.I_Selector#getIds()
   */
  @Override
  public List<String> getIds() {
    if (ids_ == null) {
      return Collections.emptyList();
    }
    return ids_;
  }

  public void setIds(List<String> ids) {
    bankIds();
    ids_.clear();
    if (ids != null) {
      for (String id: ids) {
        if (id != null) {
          ids_.add(id);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.selectors.I_Selector#getPseudoClasses()
   */
  @Override
  public List<CssPseudoClass<?>> getPseudoClasses() {
    if (pseudoClasses_ == null) {
      return Collections.emptyList();
    }
    return pseudoClasses_;
  }

  public void setPseudoClasses(List<CssPseudoClass<?>> pseudoClasses) {
    bankPseudoClasses();
    pseudoClasses_.clear();
    if (pseudoClasses != null) {
      for (CssPseudoClass<?> pc: pseudoClasses) {
        if (pc != null) {
          pseudoClasses_.add(pc);
        }
      }
    }
  }

  private void bankPseudoClasses() {
    if (pseudoClasses_ == null) {
      pseudoClasses_ = new ArrayList<CssPseudoClass<?>>();
    }
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.selectors.I_Selector#getPseudoElement()
   */
  @Override
  public String getPseudoElement() {
    return pseudoElement_;
  }

  public void setPseudoElement(String pseudoElements) {
    this.pseudoElement_ = pseudoElements;
  }

  @Override
  public int hashCode() {
    return hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
   return equals(this, obj);
  }
  
  private void bankClassNames() {
    if (classNames_ == null) {
      classNames_ = new ArrayList<String>();
    }
  }
  
  private void bankAttributes() {
    if (attributes_ == null) {
      attributes_ = new ArrayList<CssAttribute>();
    }
  }
  
  private void bankIds() {
    if (ids_ == null) {
      ids_ = new ArrayList<String>();
    }
  }
}
