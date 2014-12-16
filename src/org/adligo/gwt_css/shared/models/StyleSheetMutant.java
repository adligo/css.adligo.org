package org.adligo.gwt_css.shared.models;

import org.adligo.gwt_css.shared.models.common.SpecifiedValue;
import org.adligo.gwt_css.shared.models.selectors.Selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a style sheet which can 
 * only be modified in Memory. It contains
 * a tree like structure, all members of the 
 * branches and leaf (rules, values, etc.) should 
 * be immutable. 
 * 
 * @author scott
 *
 */
public class StyleSheetMutant implements I_StyleSheet {
  private Map<Selector,Map<String,SpecifiedValue<?>>> map_ = new HashMap<Selector,Map<String,SpecifiedValue<?>>>();
  /**
   * note these are always AtRuleMutants, just using the interface
   */
  private Map<String,List<I_AtRule>> atRuleMap_ = new HashMap<String,List<I_AtRule>>();
  private List<Throwable> warnings_;
  
  public static SpecifiedValue<?> getSpecifiedValue(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property) {
    Map<String,SpecifiedValue<?>> properties = map.get(selector);
    if (properties != null) {
      return properties.get(property);
    }
    return null;
  }
  
  public static String getValue(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property) {
    SpecifiedValue<?> value = getSpecifiedValue(map, selector, property);
    if (value != null) {
      return "" + value.getValue();
    }
    return null;
  }
  
  public static Integer getInteger(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property) {
    SpecifiedValue<?> value = getSpecifiedValue(map, selector, property);
    if (value != null) {
      if (value.getType() == CssType.PX) {
        return (Integer) value.getValue();
      }
    }
    return null;
  }
  
  public static Double getDouble(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property) {
    SpecifiedValue<?> value = getSpecifiedValue(map, selector, property);
    if (value != null) {
      if (value.getType() == CssType.PCT) {
        return (Double) value.getValue();
      }
    }
    return null;
  }
  
  public void putValue(Selector selector, String property, SpecifiedValue<?> value) {
    Map<String,SpecifiedValue<?>> properties = map_.get(selector);
    if (properties == null) {
      properties = new HashMap<String,SpecifiedValue<?>>();
      map_.put(selector, properties);
    }
    properties.put(property, value);
  }
  
  public void addAtRule(String identifier, AtRuleMutant rule) {
    List<I_AtRule> rules = atRuleMap_.get(identifier);
    if (rules == null) {
      rules = new ArrayList<I_AtRule>();
      atRuleMap_.put(identifier, rules);
    }
    rules.add(rule);
  }
  
  public List<I_AtRule> getAtRules(String identifier) {
    List<I_AtRule> rules = atRuleMap_.get(identifier);
    if (rules == null) {
      return Collections.emptyList();
    }
    return new ArrayList<I_AtRule>(rules);
  }
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getValue(java.lang.String, java.lang.String)
   */
  @Override
  public String getValue(Selector choiceGroup, String property) {
    return getValue(map_, choiceGroup, property);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getInteger(java.lang.String, java.lang.String)
   */
  @Override
  public Integer getInteger(Selector choiceGroup, String property) {
    return getInteger(map_, choiceGroup, property);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getDouble(java.lang.String, java.lang.String)
   */
  @Override
  public Double getDouble(Selector choiceGroup, String property) {
    return getDouble(map_, choiceGroup, property);
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getMap()
   */
  @Override
  public Map<Selector, Map<String, SpecifiedValue<?>>> getSelector() {
     return map_;
  }

  @Override
  public Map<String, List<I_AtRule>> getAtRules() {
    return atRuleMap_;
  }

  public List<Throwable> getWarnings() {
    if (warnings_ == null) {
      return Collections.emptyList();
    }
    return warnings_;
  }

  public void setWarnings(List<Throwable> warnings) {
    if (warnings_ == null) {
      warnings_ = new ArrayList<Throwable>();
    }
    warnings_.clear();
    if (warnings != null) {
      warnings_.addAll(warnings);
    }
  }
  
  public void addWarning(Throwable warning) {
    if (warnings_ == null) {
      warnings_ = new ArrayList<Throwable>();
    }
    if (warning != null) {
      warnings_.add(warning);
    }
  }
}
