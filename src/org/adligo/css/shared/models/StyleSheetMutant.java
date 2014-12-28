package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.SpecifiedValue;
import org.adligo.css.shared.models.selectors.Selector;

import java.io.PrintStream;
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
  public static final String WAS_NOT_FOUND_IN_THE_STYLE_SHEET_WITH_THE_FOLLOWING_SELECTOR = "\nwas not found in the style sheet with the following selector;\n";
  public static final String THE_FOLLOWING_PROPERTY = "The following property ;\n";
  private static PrintStream ERR = System.err;
  private Map<Selector,Map<String,SpecifiedValue<?>>> map_ = new HashMap<Selector,Map<String,SpecifiedValue<?>>>();
  /**
   * note these are always AtRuleMutants, just using the interface
   */
  private Map<String,List<I_AtRule>> atRuleMap_ = new HashMap<String,List<I_AtRule>>();
  private List<Throwable> warnings_;
  
  public static boolean hasValue(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property) {
    Map<String,SpecifiedValue<?>> properties = map.get(selector);
    if (properties != null) {
      SpecifiedValue<?> sv =  properties.get(property);
      if (sv != null) {
        return true;
      }
    }
    return false;
  }
  
  public static SpecifiedValue<?> getSpecifiedValue(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property) {
    Map<String,SpecifiedValue<?>> properties = map.get(selector);
    if (properties != null) {
      return properties.get(property);
    }
    Exception e = new Exception("The following selector ;\n" + selector 
        + "\nwas not found in the style sheet.");
    e.printStackTrace(ERR);
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public static String getValue(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property, String def) {
    SpecifiedValue<String> value = (SpecifiedValue<String>) getSpecifiedValue(map, selector, property);
    if (value != null) {
      String toRet = value.getValue();
      if (toRet == null) {
        return def;
      }
      return toRet;
    }
    Exception e = new Exception(THE_FOLLOWING_PROPERTY + property 
        + WAS_NOT_FOUND_IN_THE_STYLE_SHEET_WITH_THE_FOLLOWING_SELECTOR + selector);
    e.printStackTrace(ERR);
    return null;
  }
  
  public static Integer getInteger(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property, Integer def) {
    SpecifiedValue<?> value = getSpecifiedValue(map, selector, property);
    if (value != null) {
      if (value.getType() == CssType.PX || value.getType() == CssType.INTEGER) {
        Integer toRet = (Integer) value.getValue();
        if (toRet == null) {
          return def;
        }
        return toRet;
      }
    }
    Exception e = new Exception(THE_FOLLOWING_PROPERTY + property 
        + WAS_NOT_FOUND_IN_THE_STYLE_SHEET_WITH_THE_FOLLOWING_SELECTOR + selector);
    e.printStackTrace(ERR);
    return null;
  }
  
  public static Double getDouble(Map<Selector,Map<String,SpecifiedValue<?>>> map, Selector selector, String property, Double def) {
    SpecifiedValue<?> value = getSpecifiedValue(map, selector, property);
    if (value != null) {
      CssType type = value.getType();
      if (type == CssType.PCT || type == CssType.DOUBLE) {
        Double toRet = (Double) value.getValue();
        if (toRet == null) {
          return def;
        }
        return toRet;
      }
    }
    Exception e = new Exception(THE_FOLLOWING_PROPERTY + property 
        + WAS_NOT_FOUND_IN_THE_STYLE_SHEET_WITH_THE_FOLLOWING_SELECTOR + selector);
    e.printStackTrace(ERR);
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
  public String getValue(Selector choiceGroup, String property, String def) {
    return getValue(map_, choiceGroup, property, def);
  }
  @Override
  public String getValue(Selector choiceGroup, String property) {
    return getValue(map_, choiceGroup, property, null);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getInteger(java.lang.String, java.lang.String)
   */
  @Override
  public Integer getInteger(Selector choiceGroup, String property, Integer def) {
    return getInteger(map_, choiceGroup, property, def);
  }
  @Override
  public Integer getInteger(Selector choiceGroup, String property) {
    return getInteger(map_, choiceGroup, property, null);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.models.I_StyleSheet#getDouble(java.lang.String, java.lang.String)
   */
  @Override
  public Double getDouble(Selector choiceGroup, String property, Double def) {
    return getDouble(map_, choiceGroup, property, def);
  }
  @Override
  public Double getDouble(Selector choiceGroup, String property) {
    return getDouble(map_, choiceGroup, property, null);
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

  @Override
  public boolean hasValue(Selector selector, String property) {
    return hasValue(map_, selector, property);
  }

  @Override
  public boolean hasSelector(Selector selector) {
    return map_.containsKey(selector);
  }
}
