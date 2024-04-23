package com.kitty.system.game.dirtywords.unit;

import java.util.ArrayList;
import java.util.List;

public class DirtyWordUnit {
  private final String source;
  
  private final String keyWord;
  
  private final List<Integer> indexList = new ArrayList<>();
  
  public DirtyWordUnit(String source, String word) {
    this.source = source;
    this.keyWord = word;
  }
  
  public void checkWordIndex() {
    int index = 0;
    if (this.source.length() < this.keyWord.length())
      return; 
    for (int i = 0, n = this.source.length(); i < n; i++) {
      if (this.keyWord.length() > index && this.source.charAt(i) == this.keyWord.charAt(index)) {
        this.indexList.add(Integer.valueOf(index));
        index++;
        if (foundDitryWord())
          break; 
      } 
    } 
  }
  
  public boolean foundDitryWord() {
    return (this.indexList.size() == this.keyWord.length());
  }
  
  public String getKeyWord() {
    return this.keyWord;
  }
  
  public List<Integer> getIndexList() {
    return this.indexList;
  }
}
