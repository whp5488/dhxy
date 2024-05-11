
package com.kitty.system.game.fightnew.book.monster;

import com.kitty.system.game.fightnew.book.monster.star18.MONSTERSKILL_30043;
import com.kitty.system.game.fightnew.book.monster.star18.MONSTERSKILL_30042;
import java.util.HashMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.Map;

public class MonsterSkillProcessor
{
    static Map<Integer, MonsterSkillProcessor> monsterSkills;
    
    public static MonsterSkillProcessor getMonsterSkill(final int skillSerial) {
        return MonsterSkillProcessor.monsterSkills.get(skillSerial);
    }
    
    public void beforeAttack(final Fight fight, final FightObject effector, final Object... args) {
    }
    
    static {
        (MonsterSkillProcessor.monsterSkills = new HashMap<Integer, MonsterSkillProcessor>()).put(30042, new MONSTERSKILL_30042());
        MonsterSkillProcessor.monsterSkills.put(30043, new MONSTERSKILL_30043());
    }
}
