package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Optional;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
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
    public FU_324508() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (effector.getRace() != RACE.RACE_LONG.getValue() || !target.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324508);
        if (tianFuObject == null) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        double percenta = 3.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        Optional<FightObject> optional = fight.getMySide((int)target.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getHostId() == (int)target.getId() && obj.isPet()).findFirst();
        if (!optional.isPresent()) {
            return;
        }
        FightObject targetPet = (FightObject)optional.get();
        boolean flag = SkillAi.canAffectOnPro(percenta);
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324508);
        if (flag && SKILLATTR.SKILLATTR_ZHENJI.getValue() == (int)roleSkill.getType()) {
            RoleSkill skill = effector.getSkillBy(Integer.valueOf(30039));
            int dict = 1020;
            if (targetPet.getStatusValue(dict) <= 0) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30039));
                int magic_hit_dist = PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist();
                double[] power2 = sysSkill.getPower();
                double magic_hit = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleDouble(Integer.valueOf(1), Double.valueOf(power2[1] + power2[3] * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.35) * 20.0 / 100.0))) / 100.0));
                ImbueUtil.addRound(targetPet.getImbueMap(), Integer.valueOf(magic_hit_dist), 1, -magic_hit);
                ImbueParam statusValue = ImbueUtil.getStatusValue(target.getImbueMap(), dict, PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
                if (statusValue != null) {
                    ImbueUtil.addOrUpdStatus(target.getImbueMap(), dict, PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), statusValue.getSr(), statusValue.getValue());
                    targetPet.setStatus(dict, statusValue.getSr());
                    FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)targetPet.getId(), true);
                    targetParam.getStatusMap().put(Integer.valueOf(dict), Integer.valueOf(statusValue.getSr()));
                }
                skillRecord.setT((long)(int)target.getId());
            }
        }
        else if (flag && SKILLATTR.SKILLATTR_POJIA.getValue() == (int)roleSkill.getType()) {
            RoleSkill skill = effector.getSkillBy(Integer.valueOf(30033));
            int dict = 1021;
            if (targetPet.getStatusValue(dict) <= 0) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30033));
                double[] power = sysSkill.getPower();
                double mark = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(1), Double.valueOf(power[1] + power[2] * (1.0 + Math.pow((double)(int)skill.getProficient(), 0.35) * 2.0 / 100.0))) / 100.0));
                ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), 1, mark);
                int shdk = PRO_DICT_TYPE.SH_DK.getDist();
                ImbueParam statusValue = ImbueUtil.getStatusValue(targetPet.getImbueMap(), dict, shdk);
                if (statusValue != null) {
                    ImbueUtil.addOrUpdStatus(targetPet.getImbueMap(), dict, shdk, statusValue.getSr(), statusValue.getValue());
                    targetPet.setStatus(dict, statusValue.getSr());
                    FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)targetPet.getId(), true);
                    targetParam.getStatusMap().put(Integer.valueOf(dict), Integer.valueOf(statusValue.getSr()));
                }
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
}
