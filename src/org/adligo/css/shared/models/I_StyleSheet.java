package org.adligo.css.shared.models;

import org.adligo.css.shared.models.common.SpecifiedValue;
import org.adligo.css.shared.models.selectors.Selector;

import java.util.List;
import java.util.Map;
/**
 * http://www.w3.org/TR/CSS21/syndata.html#statements
 * @author scott
 *
 */
public interface I_StyleSheet {

  /**
   * 
   * @param selector
   * @param property
   * @param def the default value if none is found,
   *   Note the style sheet should also print the stack trace
   *   of a exception when the default value is used.
   * @return
   */
  public abstract String getValue(Selector selector, String property, String def);
  public abstract String getValue(Selector selector, String property);
  /**
   * 
   * @param selector
   * @param property
   * @return if this value is available in the current style sheet,
   *  Note this should NOT print a stack trace if the property is 
   *  NOT available.
   */
  public abstract boolean hasValue(Selector selector, String property);
  public abstract boolean hasSelector(Selector selector);
  /**
   * 
   * @param selector
   * @param property
   * @param def the default value if none is found,
   *   Note the style sheet should also print the stack trace
   *   of a exception when the default value is used.
   * @return
   */
  public abstract Integer getInteger(Selector selector, String property, Integer def);
  public abstract Integer getInteger(Selector selector, String property);
  /**
   * 
   * @param selector
   * @param property
   * @param def the default value if none is found,
   *   Note the style sheet should also print the stack trace
   *   of a exception when the default value is used.
   * @return
   */
  public abstract Double getDouble(Selector selector, String property, Double def);
  public abstract Double getDouble(Selector selector, String property);
  
  /**
   * the internal map contains the declaration and property from the block
   * under a selector.
   * @return
   */
  public Map<Selector, Map<String, SpecifiedValue<?>>> getSelector();
  
  public List<I_AtRule> getAtRules(String identifier);
  
  public Map<String, List<I_AtRule>> getAtRules();
  
  public List<Throwable> getWarnings();
}