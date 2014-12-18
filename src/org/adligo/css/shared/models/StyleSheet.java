package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.SpecifiedValue;
import org.adligo.css.shared.models.selectors.Selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StyleSheet implements I_StyleSheet {
  private Map<Selector,Map<String,SpecifiedValue<?>>> map_ = new HashMap<Selector,Map<String,SpecifiedValue<?>>>();
  private Map<String,List<I_AtRule>> atRuleMap_ = new HashMap<String,List<I_AtRule>>();
  private List<Throwable> warnings_;
  
  public StyleSheet(I_StyleSheet other) {
    Map<Selector,Map<String, SpecifiedValue<?>>> map = other.getSelector();
    Set<Entry<Selector,Map<String,SpecifiedValue<?>>>> entries = map.entrySet();
    for (Entry<Selector,Map<String,SpecifiedValue<?>>> e: entries) {
      map_.put(e.getKey(), Collections.unmodifiableMap(e.getValue()));
    }
    map_ = Collections.unmodifiableMap(map_);
    
    Map<String,List<I_AtRule>> atRules = other.getAtRules();
    Set<Entry<String,List<I_AtRule>>> atEntries = atRules.entrySet();
    for (Entry<String,List<I_AtRule>> e: atEntries) {
      List<I_AtRule> inRules = e.getValue();
      List<I_AtRule> myRules = new ArrayList<I_AtRule>();
      for (I_AtRule rule: inRules) {
        myRules.add(new AtRule(rule));
      }
      atRuleMap_.put(e.getKey(), Collections.unmodifiableList(myRules));
    }
    atRuleMap_ = Collections.unmodifiableMap(atRuleMap_);
    warnings_ = Collections.unmodifiableList(other.getWarnings());
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getValue(java.lang.String, java.lang.String)
   */
  @Override
  public String getValue(Selector selector, String property) {
    return StyleSheetMutant.getValue(map_, selector, property);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getInteger(java.lang.String, java.lang.String)
   */
  @Override
  public Integer getInteger(Selector selector, String property) {
    return StyleSheetMutant.getInteger(map_, selector, property);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getDouble(java.lang.String, java.lang.String)
   */
  @Override
  public Double getDouble(Selector selector, String property) {
    return StyleSheetMutant.getDouble(map_, selector, property);
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getMap()
   */
  @Override
  public Map<Selector, Map<String, SpecifiedValue<?>>> getSelector() {
    return map_;
  }

  @Override
  public List<I_AtRule> getAtRules(String identifier) {
    return atRuleMap_.get(identifier);
  }

  @Override
  public Map<String, List<I_AtRule>> getAtRules() {
    return atRuleMap_;
  }

  @Override
  public List<Throwable> getWarnings() {
    return warnings_;
  }
}
