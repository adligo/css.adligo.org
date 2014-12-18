package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.SpecifiedValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AtRuleMutant implements I_AtRule {
  private String content_;
  /**
   * This is the map of declarations and properties
   */
  private Map<String,SpecifiedValue<?>> map_ = null;
  
  public AtRuleMutant() {
    
  }


  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_AtRule#getContent()
   */
  @Override
  public String getContent() {
    return content_;
  }

  public void setContent(String content) {
    this.content_ = content;
  }
  
  public Map<String,SpecifiedValue<?>> getMap() {
    if (map_ == null) {
      return Collections.emptyMap();
    }
    return map_;
  }
  
  public void putProperty(String declaration, SpecifiedValue<?> property) {
    if (map_ == null) {
      map_ = new HashMap<String,SpecifiedValue<?>>();
    }
    map_.put(declaration, property);
  }
  
  public void putProperties(Map<String,SpecifiedValue<?>> properties) {
    if (map_ == null) {
      map_ = new HashMap<String,SpecifiedValue<?>>();
    }
    map_.clear();
    map_.putAll(properties);
  }
  
  public SpecifiedValue<?> getProperty(String declaration) {
    if (map_ == null) {
      return null;
    }
    return map_.get(declaration);
  }
}
