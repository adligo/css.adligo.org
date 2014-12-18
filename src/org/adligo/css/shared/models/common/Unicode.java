package org.adligo.css.shared.models.common;

public class Unicode {

  public static Integer getUnicodeId(String hex) {
    return Integer.valueOf(hex, 16);
  }
  
  public static boolean isNewLine(char c) {
    if (c == '\n') {
      return true;
    } else if (c == '\r') {
      return true;
    }
    return false;
  }
}
