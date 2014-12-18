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

  public abstract String getValue(Selector selector, String property);

  public abstract Integer getInteger(Selector selector, String property);

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