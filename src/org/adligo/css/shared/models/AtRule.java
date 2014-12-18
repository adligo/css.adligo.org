package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.SpecifiedValue;

import java.util.Collections;
import java.util.Map;

public class AtRule implements I_AtRule {
  String content_;
  /**
   * This is the map of declarations and properties
   */
  private Map<String,SpecifiedValue<?>> map_ = null;
  
  public AtRule(I_AtRule other) {
    content_ = other.getContent();
    map_ = Collections.unmodifiableMap(other.getMap());
  }
  
  @Override
  public String getContent() {
    return content_;
  }

  @Override
  public Map<String, SpecifiedValue<?>> getMap() {
    return map_;
  }

  @Override
  public SpecifiedValue<?> getProperty(String declaration) {
   return map_.get(declaration);
  }

}
