package org.adligo.gwt_css.shared.models.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class helps with backslash escapes;
 * 
 * see unicode escape comments on the recommendation
   * http://www.w3.org/TR/CSS21/syndata.html#statements
 * @author scott
 *
 */
public class BackslashEscape {
  private static final Set<Character> HEX_CHARS = getHexChars();
  
  public static boolean isBackslash(char c) {
    if (c ==  '\\') {
      return true;
    }
    return false;
  }
  
  @SuppressWarnings("boxing")
  private static Set<Character> getHexChars() {
    Set<Character> chars = new HashSet<Character>();
    chars.add('0');
    chars.add('1');
    chars.add('2');
    chars.add('3');
    chars.add('4');
    chars.add('5');
        
    
    chars.add('6');
    chars.add('7');
    chars.add('8');
    chars.add('9');
        
    chars.add('a');
    chars.add('b');
    chars.add('c');
    chars.add('d');
    chars.add('e');
    chars.add('f');
        
    chars.add('A');
    chars.add('B');
    chars.add('C');
    chars.add('D');
    chars.add('E');
    chars.add('F'); 
    return Collections.unmodifiableSet(chars);
  }
  // start instance code
  private StringBuilder sb_ = new StringBuilder();
  
  public BackslashEscape() {}
  

  /**
   * returns true if this was part of the 
   * backslash escape, false if the 
   * escape is completed;
   * @param c
   * @return
   */
  @SuppressWarnings("boxing")
  public boolean append(char c) {
    if (Character.isWhitespace(c)) {
      sb_.append(c);
      return false;
    } else if (HEX_CHARS.contains(c)) {
      sb_.append(c);
      if (sb_.toString().length() >= 6) {
        return false;
      } 
      return true;
    } else {
      //the "B\&W\?" case
      sb_.append(c);
      return true;
    }
  }
  
  @SuppressWarnings("boxing")
  public Character toChar() {
    String toRet = sb_.toString();
    if (toRet.length() == 1) {
      char c = toRet.charAt(0);
      if (!HEX_CHARS.contains(c)) {
        //the "B\&W\?" case
        return toRet.charAt(0);
      }
    } 
    Integer charUnicodeId = Integer.valueOf(toRet, 16);
    return Character.toChars(charUnicodeId)[0];
  }
  
  public String toOriginal() {
    return "\\" + sb_.toString();
  }
}
