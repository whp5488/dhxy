package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_75123 extends AcsrySkillProcessor
{
    public ACSRYSKILL_75123() {
    }
    
    @Override
    public NutMap beforePhysicalAttack(Fight fight, FightObject effecter, FightObject target, Object... args) {
        if (effecter.isPlayer()) {
            PlayerFightObject playerTarget = (PlayerFightObject)effecter;
            List<Integer> acsrySkillList = playerTarget.getAcsrySkillList();
            if (acsrySkillList.contains(Integer.valueOf(75123))) {
                double percent = 0.4;
                boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 75123);
                    skillRecord.setT((long)(int)effecter.getId());
                    effecter.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_GSDN_HURT.getDist()), Double.valueOf(1.0));
                }
            }
        }
        return null;
    }
}
