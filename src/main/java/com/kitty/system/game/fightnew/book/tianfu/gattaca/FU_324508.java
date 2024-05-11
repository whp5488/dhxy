
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Optional;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324508 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (effector.getRace() != RACE.RACE_LONG.getValue() || !target.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324508);
        if (tianFuObject == null) {
            return;
        }
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        final double percenta = 3.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        final Optional<FightObject> optional = fight.getMySide(target.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getHostId() == target.getId() && obj.isPet()).findFirst();
        if (!optional.isPresent()) {
            return;
        }
        final FightObject targetPet = optional.get();
        final boolean flag = SkillAi.canAffectOnPro(percenta);
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324508);
        if (flag && SKILLATTR.SKILLATTR_ZHENJI.getValue() == roleSkill.getType()) {
            final RoleSkill skill = effector.getSkillBy(30039);
            final int dict = 1020;
            if (targetPet.getStatusValue(dict) <= 0) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30039);
                final int magic_hit_dist = PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist();
                final double[] power = sysSkill.getPower();
                final double magic_hit = BigDecimalUtil.decimalScaleDouble(3, BigDecimalUtil.decimalScaleDouble(1, power[1] + power[3] * (1.0 + Math.pow(skill.getProficient(), 0.35) * 20.0 / 100.0)) / 100.0);
                ImbueUtil.addRound(targetPet.getImbueMap(), magic_hit_dist, 1, -magic_hit);
                final ImbueParam statusValue = ImbueUtil.getStatusValue(target.getImbueMap(), dict, PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
                if (statusValue != null) {
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), dict, PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), statusValue.getSr(), statusValue.getValue());
                    targetPet.setStatus(dict, statusValue.getSr());
                    final FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetPet.getId(), true);
                    targetParam.getStatusMap().put(dict, statusValue.getSr());
                }
                skillRecord.setT(target.getId());
            }
        }
        else if (flag && SKILLATTR.SKILLATTR_POJIA.getValue() == roleSkill.getType()) {
            final RoleSkill skill = effector.getSkillBy(30033);
            final int dict = 1021;
            if (targetPet.getStatusValue(dict) <= 0) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30033);
                final double[] power2 = sysSkill.getPower();
                final double mark = BigDecimalUtil.decimalScaleObjectDown(3, BigDecimalUtil.decimalScaleObjectDown(1, power2[1] + power2[2] * (1.0 + Math.pow(skill.getProficient(), 0.35) * 2.0 / 100.0)) / 100.0);
                ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist(), 1, mark);
                final int shdk = PRO_DICT_TYPE.SH_DK.getDist();
                final ImbueParam statusValue = ImbueUtil.getStatusValue(targetPet.getImbueMap(), dict, shdk);
                if (statusValue != null) {
                    ImbueUtil.addOrUpdStatus(targetPet.getImbueMap(), dict, shdk, statusValue.getSr(), statusValue.getValue());
                    targetPet.setStatus(dict, statusValue.getSr());
                    final FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetPet.getId(), true);
                    targetParam.getStatusMap().put(dict, statusValue.getSr());
                }
                skillRecord.setT(target.getId());
            }
        }
    }
}
