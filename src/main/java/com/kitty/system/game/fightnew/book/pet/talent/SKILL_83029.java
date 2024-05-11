
package com.kitty.system.game.fightnew.book.pet.talent;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83029 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final double percent = 0.05;
        double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
        if (k_hl > 0.0) {
            k_hl = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hl), String.valueOf(1.0 + percent)));
        }
        else {
            k_hl = k_hl;
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)k_hl);
        double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
        if (k_fy > 0.0) {
            k_fy = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_fy), String.valueOf(1.0 + percent)));
        }
        else {
            k_fy = k_fy;
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)k_fy);
        double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
        if (k_hs > 0.0) {
            k_hs = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hs), String.valueOf(1.0 + percent)));
        }
        else {
            k_hs = k_hs;
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)k_hs);
        double k_yw = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()));
        if (k_yw > 0.0) {
            k_yw = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_yw), String.valueOf(1.0 + percent)));
        }
        else {
            k_yw = k_yw;
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)k_yw);
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83029);
        final int round = 3;
        if (args.length > 0) {
            if (args[0] instanceof Boolean) {
                final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 12.0 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
                final double reducePercent = 0.5;
                target.getStatusMap().setv(String.valueOf(2010), (Object)round);
                target.getStatusMap().setv("2010value", (Object)effector.getId());
                target.getStatusMap().setv("2010skill", (Object)83029);
                target.getStatusMap().setv("2010percent", (Object)percent);
                target.getStatusMap().setv("2010reducePercent", (Object)reducePercent);
                final Effect effect = new Effect();
                effect.setType(2010);
                effect.setTime(round);
                skillRecord.setT(target.getId());
                return effect;
            }
            if (args[0] instanceof String && "die".equals(args[0])) {
                final List<FightObject> mySide = fight.getMySide(effector.getId());
                final List<Integer> clearStatusList = new ArrayList<Integer>();
                clearStatusList.add(2010);
                for (final FightObject unit : mySide) {
                    if (unit.getStatusMap().getInt(String.valueOf(2010)) > 0) {
                        unit.getStatusMap().remove((Object)String.valueOf(2010));
                        unit.getStatusMap().remove((Object)"2010value");
                        unit.getStatusMap().remove((Object)"2010skill");
                        unit.getStatusMap().remove((Object)"2010percent");
                        unit.getStatusMap().remove((Object)"2010reducePercent");
                        ActionParamUtils.clearStatus(fight, effector, unit, 7, 83029, 2, clearStatusList);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        if (args != null && args.length >= 2) {
            final FightObject effector = (FightObject)args[0];
            final Fight fight = (Fight)args[1];
            if (fight != null && effector != null && AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31510)) {
                return 0;
            }
        }
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
    
    @Override
    public boolean isExcute(final Fight fight, final FightObject effector, final Object... args) {
        final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> !obj.isFallDown() && !effector.getId().equals(obj.getId())).collect(Collectors.toList());
        return mySide.size() > 0;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        final int coolRound = 5;
        effector.getSkillCoolMap().setv(String.valueOf(83029), (Object)coolRound);
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(final Fight fight, final FightObject effector) {
        final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> !obj.isFallDown() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            final List<Integer> targetIds = new ArrayList<Integer>();
            for (final FightObject target : mySide) {
                targetIds.add(target.getId());
            }
            return targetIds;
        }
        return null;
    }
}
