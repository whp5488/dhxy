
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Map;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74303 extends AcsrySkillProcessor
{
    @Override
    public void beforeExecuteMagic(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        if (fight.getRound() > 1 && effecter.isPlayer()) {
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            final List<Integer> acsrySkillList = playerFightObject.getAcsrySkillList();
            if (acsrySkillList.contains(74303)) {
                final RoleSkill skill = (RoleSkill)args[0];
                if (skill == null) {
                    return;
                }
                if (SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType()) {
                    final Map<Integer, Integer> abnormalRoundMap = playerFightObject.getAbnormalRoundMap();
                    int round = fight.getRound();
                    --round;
                    final Integer status = abnormalRoundMap.get(round);
                    final String skillRound = playerFightObject.getOtherMap().getString("acsrySkillRound_" + round, (String)null);
                    if (this.check(status, skillRound)) {
                        final String distStr = String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist());
                        playerFightObject.getAcsryPropertyMap().setv(distStr, (Object)(-1));
                        playerFightObject.getAcsryPropertyMap().setv(distStr + "value", (Object)1.0);
                        final String dictStr = String.valueOf(OTHER_SKILL_DICT_TYPE.DEFY_RESISTANCE_PERCENT.getDist());
                        playerFightObject.getAcsryPropertyMap().setv(dictStr, (Object)(-1));
                        final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_SS.getDist());
                        playerFightObject.getAcsryPropertyMap().setv(dictStr + "value", (Object)kx);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 74303);
                        skillRecord.setT(target.getId());
                    }
                }
            }
        }
    }
    
    private boolean check(final Integer status, final String skillRound) {
        return (status == null || !StatusConst.getControlStatus().contains(status)) && (skillRound == null || !skillRound.contains(String.valueOf(74303)));
    }
}
