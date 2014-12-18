package org.adligo.css.shared.models.common;


public class LineChar {
  private int line_;
  private int character_;
  
  public LineChar(int line, int character) {
    line_ = line;
    character_ = character;
  }
  
  
  public int getLine() {
    return line_;
  }
  
  public int getCharacter() {
    return character_;
  }
  
}
