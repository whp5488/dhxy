package com.kitty.system.game.fightnew.book.monster;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.monster.star18.MONSTERSKILL_30042;
import com.kitty.system.game.fightnew.book.monster.star18.MONSTERSKILL_30043;
import java.util.HashMap;
import java.util.Map;

public class MonsterSkillProcessor {
  static Map<Integer, MonsterSkillProcessor> monsterSkills = new HashMap<>();
  
  static {
    monsterSkills.put(Integer.valueOf(30042), new MONSTERSKILL_30042());
    monsterSkills.put(Integer.valueOf(30043), new MONSTERSKILL_30043());
  }
  
  public static MonsterSkillProcessor getMonsterSkill(int skillSerial) {
    return monsterSkills.get(Integer.valueOf(skillSerial));
  }
  
  public void beforeAttack(Fight fight, FightObject effector, Object... args) {}
}
