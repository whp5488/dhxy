
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
import java.util.stream.Collector;
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        if (21066 == effector.getSerial()) {
            FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83030);
            final List<Integer> statuss = StatusConst.getAnomalousStatus().stream().filter(status -> status != 1019).collect(Collectors.toList());
            final List<FightObject> mySide = fight.getMySide(effector.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
            final List<FightObject> mySideObject = new ArrayList<FightObject>();
            for (final FightObject fightObject : mySide) {
                final Iterator<Integer> iterator2 = statuss.iterator();
                while (iterator2.hasNext()) {
                    Integer status = iterator2.next();
                    if (fightObject.getStatusMap().getInt(String.valueOf(status)) > 0) {
                        mySideObject.add(fightObject);
                        break;
                    }
                }
            }
            final int residue = mySide.size() - mySideObject.size();
            if (residue > 0) {
                final List<Integer> listStatus = new ArrayList<Integer>();
                final double persent = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(residue), String.valueOf(mySide.size())));
                if (persent < 0.4) {
                    for (final Integer status2 : statuss) {
                        if (effector.getStatusMap().getInt(String.valueOf(status2)) > 0) {
                            effector.removeStatus(status2);
                            listStatus.add(status2);
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
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(83030);
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83030);
            final NutMap effectorStatus = effector.getStatusMap();
            final NutMap targetStatus = target.getStatusMap();
            effector.setStatusMap(targetStatus);
            target.setStatusMap(effectorStatus);
            final List<Integer> targetClearList = new ArrayList<Integer>();
            final Map<Integer, Integer> targetStatusMap = new HashMap<Integer, Integer>();
            final List<Integer> effectorClearList = new ArrayList<Integer>();
            final Map<Integer, Integer> effectorStatusMap = new HashMap<Integer, Integer>();
            final FightMessageNewService messageService = SpringUtils.getBean(FightMessageNewService.class);
            for (final Map.Entry<String, Object> entry : effectorStatus.entrySet()) {
                final String key = entry.getKey();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                final Integer value = (Integer) entry.getValue();
                if (value <= 0) {
                    continue;
                }
                effectorClearList.add(Integer.parseInt(key));
                targetStatusMap.put(Integer.parseInt(key), value);
            }
            for (final Map.Entry<String, Object> entry : targetStatus.entrySet()) {
                final String key = entry.getKey();
                if (!key.matches("^[0-9]+$")) {
                    continue;
                }
                final Integer value = (Integer) entry.getValue();
                if (value <= 0) {
                    continue;
                }
                targetClearList.add(Integer.parseInt(key));
                effectorStatusMap.put(Integer.parseInt(key), value);
            }
            final List<FiedValue> list = new ArrayList<FiedValue>();
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
            final Effect e = new Effect();
            e.setType(2009);
            e.setTime(-1);
            e.setList(list);
            skillRecord.setT(target.getId());
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 32;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        final int skillCoolTime = 5;
        final String skillS = String.valueOf(83030);
        effector.getSkillCoolMap().setv(skillS, (Object)skillCoolTime);
        return true;
    }
}
