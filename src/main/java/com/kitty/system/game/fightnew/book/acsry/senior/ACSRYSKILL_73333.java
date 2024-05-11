
package com.kitty.system.game.fightnew.book.acsry.senior;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73333 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        RoleSkill skill = null;
        Boolean isHit = false;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (final Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType() && effecter.isPlayer()) {
            target.getOtherMap().setv("skillRound", (Object)("73333_" + effecter.getId() + "_" + fight.getRound()));
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73333);
            skillRecord.setT(target.getId());
        }
    }
    
    @Override
    public NutMap executeReduceHp(final Fight fight, final FightObject effecter, final FightObject target, final Object... args) {
        RoleSkill skill = null;
        int hurt = 0;
        try {
            skill = (RoleSkill)args[0];
            hurt = (int)args[1];
        }
        catch (final Exception e) {
            return null;
        }
        if (skill == null) {
            return null;
        }
        if (hurt <= 0) {
            return null;
        }
        if (this.checkExcute(skill)) {
            final Object obj = effecter.getOtherMap().get("skillRound", null);
            if (obj != null) {
                final String str = obj.toString();
                final String[] splits = str.split("_");
                if (splits.length >= 3) {
                    final String roundStr = splits[2];
                    final String objIdStr = splits[1];
                    final String skillIdStr = splits[0];
                    if (roundStr.matches("^[0-9]+$") && objIdStr.matches("^[0-9]+$") && skillIdStr.matches("^[0-9]+$")) {
                        final int round = Integer.parseInt(roundStr);
                        final int objId = Integer.parseInt(objIdStr);
                        final int skillId = Integer.parseInt(skillIdStr);
                        if (round == fight.getRound() && 73333 == skillId) {
                            final FightObject fightObject = fight.queryFightObject(objId);
                            if (fightObject != null) {
                                double property = 0.15;
                                property += fightObject.getRole().getMj() / 100 * 0.01;
                                if (property > 0.3) {
                                    property = 0.3;
                                }
                                final int changeHurt = (int)(hurt * property);
                                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73333);
                                skillRecord.setT(effecter.getId());
                                return NutMap.NEW().setv("changeHurt", (Object)(-changeHurt));
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private boolean checkExcute(final RoleSkill skill) {
        return SKILLATTR.SKILLATTR_FIRE.getValue() == skill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == skill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == skill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == skill.getType() || (SKILLATTR.SKILLATTR_HENGSAO.getValue() == skill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == skill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == skill.getType()) || SKILLATTR.SKILLATTR_WULI.getValue() == skill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == skill.getType() || SKILLATTR.SKILLATTR_XIXUE.getValue() == skill.getType() || SKILLATTR.SKILLATTR_POISON.getValue() == skill.getType() || SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType();
    }
}
