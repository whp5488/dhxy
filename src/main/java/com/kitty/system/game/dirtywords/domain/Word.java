package com.kitty.system.game.dirtywords.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("word")
public class Word {
  @Column
  private String word;
  
  public String getWord() {
    return this.word;
  }
  
  public void setWord(String word) {
    this.word = word;
  }
}
