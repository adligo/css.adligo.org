package org.adligo.css.shared.models;

import org.adligo.css.shared.models.selectors.Selector;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ExpectedCss implements I_ExpectedCss {
  private Map<Selector,Map<String,CssType>> map_ = new HashMap<Selector,Map<String,CssType>>();
  
  public ExpectedCss(I_ExpectedCss expected) {
    Map<Selector,Map<String,CssType>> map = expected.getMap();
    Set<Entry<Selector,Map<String,CssType>>> entries = map.entrySet();
    for (Entry<Selector,Map<String,CssType>> e: entries) {
      map_.put(e.getKey(), Collections.unmodifiableMap(e.getValue()));
    }
    map_ = Collections.unmodifiableMap(map_);
  }

  @Override
  public Set<Selector> getSelectors() {
    return map_.keySet();
  }

  @Override
  public Map<String,CssType> getProperties(Collection<Selector> selectors) {
    return ExpectedCssMutant.getProperties(map_, selectors);
  }

  @Override
  public CssType getType(Selector choiceGroup, String property) {
    return ExpectedCssMutant.getType(map_, choiceGroup, property);
  }

  @Override
  public Map<Selector, Map<String, CssType>> getMap() {
    return map_;
  }
}
