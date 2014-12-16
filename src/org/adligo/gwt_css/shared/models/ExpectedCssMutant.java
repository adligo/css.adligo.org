package org.adligo.gwt_css.shared.models;

import org.adligo.gwt_css.shared.models.selectors.Selector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * This class contains the expected 
 * nodes the application expects from
 * the css file, for 
 * file which is 'driving' the application.
 * 
 * 
 * 
 * @author scott
 *
 */
public class ExpectedCssMutant implements I_ExpectedCss {
  public static final String EXPECTED_CSS_MUTANT_REQUIRES_A = "ExpectedCssMutant requires a ";
  public static final String EXPECTED_CSS_MUTANT_REQUIRES_A_CHOICE_GROUP = EXPECTED_CSS_MUTANT_REQUIRES_A + "choice group.";
  public static final String EXPECTED_CSS_MUTANT_REQUIRES_A_PROPERTY = EXPECTED_CSS_MUTANT_REQUIRES_A + "property.";
  public static final String EXPECTED_CSS_MUTANT_REQUIRES_A_TYPE = EXPECTED_CSS_MUTANT_REQUIRES_A + "type.";
  private Map<Selector,Map<String,CssType>> map_ = new HashMap<Selector,Map<String,CssType>>();
  
  public static Set<String> getProperties(Map<Selector,Map<String,CssType>> map, Selector choiceGroup) {
    Map<String,CssType> properties = map.get(choiceGroup);
    if (properties != null) {
      return properties.keySet();
    }
    return Collections.emptySet();
  }
  
  public static CssType getType(Map<Selector,Map<String,CssType>> map, Selector choiceGroup, String property) {
    Map<String,CssType> properties = map.get(choiceGroup);
    if (properties != null) {
      return properties.get(property);
    }
    return null;
  }
  
  /**
   * Note the term selector and property come from the css 2.1 wc3 
   * candidate recomendation
   * http://www.w3.org/TR/2004/CR-CSS21-20040225/cover.html#minitoc
   * 
   * @param selector
   * @param property
   * @param type
   */
  public void addExpected(Selector choiceGroup, String property, CssType type) {
    if (choiceGroup == null) {
      throw new IllegalArgumentException(EXPECTED_CSS_MUTANT_REQUIRES_A_CHOICE_GROUP);
    }
    if (property == null) {
      throw new IllegalArgumentException(EXPECTED_CSS_MUTANT_REQUIRES_A_PROPERTY);
    }
    if (type == null) {
      throw new IllegalArgumentException(EXPECTED_CSS_MUTANT_REQUIRES_A_TYPE);
    }
    Map<String,CssType> properties = map_.get(choiceGroup);
    if (properties == null) {
      properties = new HashMap<String,CssType>();
      map_.put(choiceGroup, properties);
    }
    properties.put(property, type);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.I_ExpectedCss#getSelectors()
   */
  @Override
  public Set<Selector> getChoiceGroups() {
    return map_.keySet();
  }
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.I_ExpectedCss#getProperties(java.lang.String)
   */
  @Override
  public Set<String> getProperties(Selector selector) {
    return getProperties(map_,selector);
  }

  
  
  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.I_ExpectedCss#getType(java.lang.String, java.lang.String)
   */
  @Override
  public CssType getType(Selector choiceGroup, String property) {
    return getType(map_, choiceGroup, property);
  }

  /* (non-Javadoc)
   * @see org.adligo.gwt_css.client.I_ExpectedCss#getMap()
   */
  @Override
  public Map<Selector, Map<String, CssType>> getMap() {
    return map_;
  }
}
