package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.SpecifiedValue;

import java.util.Map;

/**
 * The at rule set of classes was added so that 
 * the parser will have a extendable api
 * with out the need for changes to the 
 * class returned from I_StyleSheet for a at rule
 * (instead of simply String)_.
 * 
 * Note declarations for properties never contain 
 * the colon and are trimmed (so no white space).
 * @author scott
 *
 */
public interface I_AtRule {

  public abstract String getContent();

  /**
   * this should return a empty map 
   * if there arn't any entries or the implementaion
   * contains a null for the map.
   * 
   * @return
   */
  public Map<String,SpecifiedValue<?>> getMap();
  
  public SpecifiedValue<?> getProperty(String declaration);
}