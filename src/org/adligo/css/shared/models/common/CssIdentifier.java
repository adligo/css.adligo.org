package org.adligo.css.shared.models.common;

public class CssIdentifier {
  @SuppressWarnings("boxing")
  private static final int U00A0 = Unicode.getUnicodeId("00A0");
  private StringBuilder sb_ = new StringBuilder();
  private boolean valid = true;
  private BackslashEscape backslash_ = null;
  
  public CssIdentifier() {}
  
  public boolean append(char c) {
    if (backslash_ != null) {
      if (!backslash_.append(c)) {
        sb_.append(backslash_.toChar());
        backslash_ = null;
      }
      return true;
    } else {
      if (BackslashEscape.isBackslash(c)) {
        backslash_ = new BackslashEscape();
        return true;
      } else if (Character.isLetterOrDigit(c) || c == '-' || c == '_' || new String("" + c).codePointAt(0) >= U00A0) {
        sb_.append(c);
        return true;
      } else {
        if (!Whitespace.isWhitespace(c)) {
          valid = false;
        }
      }
      return false;
    }
  }
  
  public String getId() {
    return sb_.toString();
  }
  
  public boolean isValid() {
    return valid;
  }
}
