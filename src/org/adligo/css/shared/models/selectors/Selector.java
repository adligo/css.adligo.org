package org.adligo.css.shared.models.selectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A immutable class to represents a group
 * of selectors which have combinators
 * between them i.e.;
 * div p + a
 * or
 * hr
 * or
 * a
 * 
 * This class is the atomic unit for 
 * identifying blocks.
 * 
 * @author scott
 *
 */
public class Selector {
  public static final String THE_FIRST_SELECTOR_IN_A_CHOICE_GROUP_MUST_HAVE_NO_COMBINATOR = "The first selector in a choice group must have no combinator.";
  private List<CssLink> chain_ = new ArrayList<CssLink>();
  
  public Selector(SequenceOfSimpleSelectors sequenceOfSimpleSelectors) {
    this(new CssLink(sequenceOfSimpleSelectors));
  }
  
  public Selector(CssLink group) {
    this(Collections.singletonList(group));
  }
  
  public Selector(List<CssLink> group) {
    int counter = 0;
    for (CssLink choice: group) {
      if (choice != null) {
        if (counter == 0) {
          if (choice.getCombinator() != Combinator.NONE) {
            throw new IllegalArgumentException(THE_FIRST_SELECTOR_IN_A_CHOICE_GROUP_MUST_HAVE_NO_COMBINATOR);
          }
        }
        counter++;
        chain_.add(choice);
      }
    }
  }
  
  public int size() {
    return chain_.size();
  }
  
  public CssLink get(int i) {
    return chain_.get(i);
  }
}
