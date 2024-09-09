package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.common.enter.FiedValue;
import java.util.Map;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import java.util.HashMap;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83030 extends SkillProcessor
{
    public SKILL_83030() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        if (21066 == effector.getSerial()) {
            FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83030);
            List<Integer> statuss = (List)StatusConst.getAnomalousStatus().stream().filter(status -> (int)status != 1019).collect(Collectors.toList());
            List<FightObject> mySide = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
            List<FightObject> mySideObject = new ArrayList<>();
            for (FightObject fightObject : mySide) {
                Iterator<Integer> iterator2 = statuss.iterator();
                while (iterator2.hasNext()) {
                	Integer status = (Integer)iterator2.next();
                    if (fightObject.getStatusMap().getInt(String.valueOf(status)) > 0) {
                        mySideObject.add(fightObject);
                        break;
                    }
                    continue;
                }
            }
            int residue = mySide.size() - mySideObject.size();
            if (residue > 0) {
                List<Integer> listStatus = new ArrayList<>();
                double persent = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(residue), String.valueOf(mySide.size())));
                if (persent < 0.4) {
                    for (Integer status2 : statuss) {
                        if (effector.getStatusMap().getInt(String.valueOf(status2)) > 0) {
                            effector.removeStatus((int)status2);
                            listStatus.add(status2);
                        }
                        else {
                            continue;
                        }
                    }
                }
                if (listStatus.size() > 0) {
                    ActionParamUtils.clearStatus(fight, effector, effector, 7, 83030, 2, listStatus);
                }
            }
        }
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(83030));
        if (skill != null) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83030);
            NutMap effectorStatus = effector.getStatusMap();
            NutMap targetStatus = target.getStatusMap();
            effector.setStatusMap(targetStatus);
            target.setStatusMap(effectorStatus);
            List<Integer> targetClearList = new ArrayList<>();
            Map<Integer, Integer> targetStatusMap = new HashMap();
            List<Integer> effectorClearList = new ArrayList<>();
            Map<Integer, Integer> effectorStatusMap = new HashMap();
            FightMessageNewService messageService = (FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class);
            for (Map.Entry<String, Object> entry : effectorStatus.entrySet()) {
                String key = (String)entry.getKey();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                Integer value = (Integer)entry.getValue();
                if ((int)value <= 0) {
                    continue;
                }
                effectorClearList.add(Integer.valueOf(Integer.parseInt(key)));
                targetStatusMap.put(Integer.valueOf(Integer.parseInt(key)), value);
            }
            for (Map.Entry<String, Object> entry : targetStatus.entrySet()) {
                String key = (String)entry.getKey();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                Integer value = (Integer)entry.getValue();
                if ((int)value <= 0) {
                    continue;
                }
                targetClearList.add(Integer.valueOf(Integer.parseInt(key)));
                effectorStatusMap.put(Integer.valueOf(Integer.parseInt(key)), value);
            }
            List<FiedValue> list = new ArrayList<>();
            if (targetClearList.size() > 0) {
                list.add(new FiedValue("targetClearStatus", (byte)7, targetClearList));
            }
            if (targetStatusMap.size() > 0) {
                list.add(new FiedValue("targetStatusMap", (byte)7, targetStatusMap));
            }
            if (effectorClearList.size() > 0) {
                list.add(new FiedValue("attaClearStatus", (byte)7, effectorClearList));
            }
            if (effectorStatusMap.size() > 0) {
                list.add(new FiedValue("attaStatusMap", (byte)7, effectorStatusMap));
            }
            Effect e = new Effect();
            e.setType(2009);
            e.setTime(-1);
            e.setList(list);
            skillRecord.setT((long)(int)target.getId());
            return e;
        }
        else {
            return null;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 32;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        int skillCoolTime = 5;
        String skillS = String.valueOf(83030);
        effector.getSkillCoolMap().setv(skillS, Integer.valueOf(skillCoolTime));
        return true;
    }
}
