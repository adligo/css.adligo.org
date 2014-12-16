package org.adligo.gwt_css.shared.models;

import org.adligo.gwt_css.shared.models.selectors.Selector;

import java.util.Map;
import java.util.Set;

public interface I_ExpectedCss {

  public abstract Set<Selector> getChoiceGroups();

  public abstract Set<String> getProperties(Selector choiceGroup);

  public abstract CssType getType(Selector selector, String property);

  public Map<Selector, Map<String, CssType>> getMap();
}