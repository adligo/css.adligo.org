package org.adligo.css.shared.models.common;

public class Whitespace {
  @SuppressWarnings("boxing")
  public static boolean isWhitespace(Character c) {
    
    if (c == '\n') {
      return true;
    }
    if (c == '\r') {
      return true;
    }
    if (c == '\t') {
      return true;
    }
    if (c == ' ') {
      return true;
    }
    return false;
  }
}
