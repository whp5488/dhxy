package com.kitty.system.game.fightnew.book.monster;

import java.util.Arrays;
import java.util.List;

public interface MonsterSkillConst {
  public static final int MONSTERSKILL_30042 = 30042;
  
  public static final int MONSTERSKILL_30043 = 30043;
  
  static List<Integer> getBeforeAttack() {
    return Arrays.asList(new Integer[] { Integer.valueOf(30042), Integer.valueOf(30043) });
  }
}
