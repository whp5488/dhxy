package com.kitty.system.game.fightnew.book.pet.talent;

import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
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
    public SKILL_83029() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        double percent = 0.05;
        double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
        if (k_hl > 0.0) {
            k_hl = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hl), String.valueOf(1.0 + percent)));
        }
        else {
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(k_hl));
        double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
        if (k_fy > 0.0) {
            k_fy = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_fy), String.valueOf(1.0 + percent)));
        }
        else {
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(k_fy));
        double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
        if (k_hs > 0.0) {
            k_hs = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hs), String.valueOf(1.0 + percent)));
        }
        else {
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(k_hs));
        double k_yw = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()));
        if (k_yw > 0.0) {
            k_yw = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_yw), String.valueOf(1.0 + percent)));
        }
        else {
        }
        effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(k_yw));
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83029);
        int round = 3;
        if (args.length > 0) {
            if ((args[0] instanceof Boolean)) {
                double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 12.0 * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
                double reducePercent = 0.5;
                target.getStatusMap().setv(String.valueOf(2010), Integer.valueOf(round));
                target.getStatusMap().setv("2010value", effector.getId());
                target.getStatusMap().setv("2010skill", Integer.valueOf(83029));
                target.getStatusMap().setv("2010percent", Double.valueOf(percent));
                target.getStatusMap().setv("2010reducePercent", Double.valueOf(reducePercent));
                Effect effect = new Effect();
                effect.setType(2010);
                effect.setTime(round);
                skillRecord.setT((long)(int)target.getId());
                return effect;
            }
            if (args[0] instanceof String && "die".equals(args[0])) {
                List<FightObject> mySide = fight.getMySide((int)effector.getId());
                List<Integer> clearStatusList = new ArrayList<>();
                clearStatusList.add(Integer.valueOf(2010));
                for (FightObject unit : mySide) {
                    if (unit.getStatusMap().getInt(String.valueOf(2010)) > 0) {
                        unit.getStatusMap().remove(String.valueOf(2010));
                        unit.getStatusMap().remove("2010value");
                        unit.getStatusMap().remove("2010skill");
                        unit.getStatusMap().remove("2010percent");
                        unit.getStatusMap().remove("2010reducePercent");
                        ActionParamUtils.clearStatus(fight, effector, unit, 7, 83029, 2, clearStatusList);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        if (args != null && args.length >= 2) {
            FightObject effector = (FightObject)args[0];
            Fight fight = (Fight)args[1];
            if (fight != null && effector != null && AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31510)) {
                return 0;
            }
        }
        int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
    
    @Override
    public boolean isExcute(Fight fight, FightObject effector, Object... args) {
        List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown() && !effector.getId().equals(obj.getId())).collect(Collectors.toList());
        return mySide.size() > 0;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        int coolRound = 5;
        effector.getSkillCoolMap().setv(String.valueOf(83029), Integer.valueOf(coolRound));
        return true;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown() && !obj.getId().equals(effector.getId())).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            List<Integer> targetIds = new ArrayList<>();
            for (FightObject target : mySide) {
                targetIds.add(target.getId());
            }
            return targetIds;
        }
        return null;
    }
}
