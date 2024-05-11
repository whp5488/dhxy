
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuSkillUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324503 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> obj != null && !obj.isFallDown()).collect(Collectors.toList());
        if (effector.isPlayer() && (mySide.size() < 7 || mySide.size() > 9)) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324503);
            final RoleSkill roleSkill = (RoleSkill)args[1];
            final Effect effect = (Effect)args[2];
            final Double percent = this.getPercent(effector);
            if (percent == null || effect == null || roleSkill == null) {
                return null;
            }
            if (SKILLATTR.SKILLATTR_ATTACK.getValue() == roleSkill.getType()) {
                if (1008 == effect.getType() && effect.getTime() > 0) {
                    final ImbueParam jg = ImbueUtil.getStatusValue(target.getImbueMap(), 1008, PRO_DICT_TYPE.RISE_ATTACK.getDist(), effect.getTime());
                    if (jg != null) {
                        jg.setValue(jg.getValue() * (1.0 + percent));
                    }
                    final ImbueParam jmz = ImbueUtil.getStatusValue(target.getImbueMap(), 1008, PRO_DICT_TYPE.HIT_RATIO.getDist(), effect.getTime());
                    if (jmz != null) {
                        jmz.setValue(jmz.getValue() * (1.0 + percent));
                    }
                }
                skillRecord.setT(target.getId());
            }
            else if (SKILLATTR.SKILLATTR_PAN.getValue() == roleSkill.getType()) {
                if (1009 == effect.getType() && effect.getTime() > 0) {
                    TianFuSkillUtils.mhChangePro(target, 1009, effect.getTime(), percent);
                    final ImbueParam kss = ImbueUtil.getStatusValue(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_SS.getDist(), effect.getTime());
                    if (kss != null) {
                        kss.setValue(kss.getValue() * (1.0 + percent));
                    }
                    final ImbueParam kgh = ImbueUtil.getStatusValue(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_GH.getDist(), effect.getTime());
                    if (kgh != null) {
                        kgh.setValue(kgh.getValue() * (1.0 + percent));
                    }
                    final ImbueParam kd = ImbueUtil.getStatusValue(target.getImbueMap(), 1009, PRO_DICT_TYPE.K_DU.getDist(), effect.getTime());
                    if (kd != null) {
                        kd.setValue(kd.getValue() * (1.0 + percent));
                    }
                }
                skillRecord.setT(target.getId());
            }
            else if (SKILLATTR.SKILLATTR_SPEED.getValue() == roleSkill.getType()) {
                if (1010 == effect.getType() && effect.getTime() > 0) {
                    final ImbueParam js = ImbueUtil.getStatusValue(target.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), effect.getTime());
                    if (js != null) {
                        js.setValue(js.getValue() * (1.0 + percent));
                    }
                }
                skillRecord.setT(target.getId());
            }
            else if (SKILLATTR.SKILLATTR_SHUAIRUO.getValue() == roleSkill.getType()) {
                if (1007 == effect.getType() && effect.getTime() > 0) {
                    TianFuSkillUtils.mhChangePro(target, 1007, effect.getTime(), percent);
                }
                skillRecord.setT(target.getId());
            }
            else if (SKILLATTR.SKILLATTR_ZHIYU.getValue() == roleSkill.getType()) {
                if (1022 == effect.getType() && effect.getTime() > 0) {
                    int value = target.getStatusSubMap().getInt(String.valueOf(1022) + "value");
                    if (value != 0) {
                        value *= (int)(1.0 + percent);
                    }
                    target.getStatusSubMap().setv(String.valueOf(1022) + "value", (Object)value);
                }
                skillRecord.setT(target.getId());
            }
        }
        return null;
    }
    
    private Double getPercent(final FightObject effector) {
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324503);
        if (tianFuObject == null) {
            return null;
        }
        final double percent = 1.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        return percent;
    }
}
