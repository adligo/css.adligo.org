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
  
  public CssLink(I_SequenceOfSimpleSelectors sequence) {
    combinator_ = Combinator.NONE;
    sequence_ = new SequenceOfSimpleSelectors(sequence);
  }

  public CssLink(Combinator combinator, I_SequenceOfSimpleSelectors sequence) {
    combinator_ = combinator;
    sequence_ = new SequenceOfSimpleSelectors(sequence);
  }
  
  public Combinator getCombinator() {
    return combinator_;
  }

  public I_SequenceOfSimpleSelectors getSequence() {
    return sequence_;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((combinator_ == null) ? 0 : combinator_.hashCode());
    result = prime * result + ((sequence_ == null) ? 0 : sequence_.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CssLink other = (CssLink) obj;
    if (combinator_ != other.combinator_)
      return false;
    if (sequence_ == null) {
      if (other.sequence_ != null)
        return false;
    } else if (!sequence_.equals(other.sequence_))
      return false;
    return true;
  }
}
