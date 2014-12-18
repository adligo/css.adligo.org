package org.adligo.css.shared.models.selectors;

/**
 * This class represents either a single simple selector group
 * or a combination of single simple selector group with a combinator
 * which proceeds the simple selector group.
 * 
 * @author scott
 *
 */
public class CssLink {
  private Combinator combinator_;
  private I_SequenceOfSimpleSelectors sequence_;
  
  public CssLink(I_SequenceOfSimpleSelectors selector) {
    combinator_ = Combinator.NONE;
    sequence_ = selector;
  }

  public CssLink(Combinator combinator, I_SequenceOfSimpleSelectors selector) {
    combinator_ = combinator;
    sequence_ = selector;
  }
  
  public Combinator getCombinator() {
    return combinator_;
  }

  public I_SequenceOfSimpleSelectors getSequence() {
    return sequence_;
  }
}
