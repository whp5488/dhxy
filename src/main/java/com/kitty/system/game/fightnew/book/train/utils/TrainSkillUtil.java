package com.kitty.system.game.fightnew.book.train.utils;

import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;
import com.kitty.system.game.fightnew.book.train.TrainSkillConst;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.train.domain.TrainSeal;
import java.util.Map;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.train.pojo.TrainSkillParam;
import com.kitty.system.game.train.domain.Train;
import com.kitty.system.game.train.pool.TrainDataPool;
import com.kitty.system.init.domain.SysDataTrain;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.train.TrainService;
import java.util.List;

public class TrainSkillUtil
{
    public TrainSkillUtil() {
    }
    
    public static List<Integer> getTrainSkill(long roleId) {
        TrainService trainService = (TrainService)SpringUtils.getBean(TrainService.class);
        Train train = trainService.getTrain(Long.valueOf(roleId));
        if (train.getMagicCircle() > 0) {
            SysDataTrain sysDataTrain = (SysDataTrain)TrainDataPool.trainMap.get(Integer.valueOf(train.getMagicCircle()));
            if (sysDataTrain != null) {
                return sysDataTrain.getBase_attr();
            }
        }
        return null;
    }
    
    public static TrainSkillParam getSkillParam(int skillSerial, long roleId) {
        TrainSkillParam trainSkillParam = new TrainSkillParam();
        Integer trainSerial = (Integer)TrainDataPool.trainSkillSerialMap.get(Integer.valueOf(skillSerial));
        if (trainSerial == null || (int)trainSerial <= 0) {
            return null;
        }
        SysDataTrain sysDataTrain = (SysDataTrain)TrainDataPool.trainMap.get(trainSerial);
        if (sysDataTrain != null) {
            List<Integer> power = sysDataTrain.getPower();
            if (power != null && power.size() <= 0) {
                power = null;
            }
            trainSkillParam.setPowers(power);
            List<Integer> activeCond = sysDataTrain.getActiveCond();
            if (activeCond != null && activeCond.size() <= 0) {
                activeCond = null;
            }
            trainSkillParam.setActiveConds(activeCond);
            Train train = ((TrainService)SpringUtils.getBean(TrainService.class)).getTrain(Long.valueOf(roleId));
            int count = 0;
            for (Integer ac : activeCond) {
                count += train.getPosLevelForNum((int)ac);
            }
            trainSkillParam.setPosLevelSum(count);
            double skillPercent = getSkillPercent(activeCond, roleId);
            trainSkillParam.setPercent(skillPercent);
            return trainSkillParam;
        }
        else {
            return trainSkillParam;
        }
    }
    
    public static double getSkillCount(int power, int posLevelSum, int fullLevelSum, double skillPercent) {
        String one = BigDecimalUtil.divide(String.valueOf(power), String.valueOf(100), 3);
        String two = BigDecimalUtil.multiply(one, String.valueOf(posLevelSum));
        String three = BigDecimalUtil.divide(two, String.valueOf(fullLevelSum), 3);
        String value = BigDecimalUtil.multiply(three, String.valueOf(skillPercent));
        return Double.parseDouble(value);
    }
    
    public static double getSkillPercent(List<Integer> activeConds, long roleId) {
        double percent = 0.1;
        Map<Long, TrainSeal> trainSealMap = ((TrainService)SpringUtils.getBean(TrainService.class)).getTrainSeal(Long.valueOf(roleId));
        if (trainSealMap != null && trainSealMap.size() > 0) {
            int count = 0;
            for (Map.Entry<Long, TrainSeal> entry : trainSealMap.entrySet()) {
                TrainSeal trainSeal = (TrainSeal)entry.getValue();
                if (trainSeal.getPosition() == 0) {
                    continue;
                }
                for (Integer ac : activeConds) {
                    if (trainSeal.getPosition() == (int)ac) {
                        ++count;
                    }
                    else {
                        continue;
                    }
                }
            }
            percent += (double)count * 0.1;
            if (count == activeConds.size()) {
                percent = 1.0;
            }
        }
        return percent;
    }
    
    public static double getPercent(long roleId, int skillSerial, int index) {
        TrainSkillParam trainSkillParam = getSkillParam(skillSerial, roleId);
        if (trainSkillParam == null) {
            return 0.0;
        }
        List<Integer> powers = trainSkillParam.getPowers();
        if (powers == null) {
            return 0.0;
        }
        Integer power = (Integer)powers.get(index);
        if (trainSkillParam.getActiveConds() == null) {
            return 0.0;
        }
        double percent = getSkillCount((int)power, trainSkillParam.getPosLevelSum(), trainSkillParam.getActiveConds().size() * 100, trainSkillParam.getPercent());
        return percent;
    }
    
    public static void roundStart(Fight fight, FightObject effector) {
        try {
            if (effector != null && effector.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)effector.getRole().getId());
                if (trainSkills != null) {
                    for (Integer trainSkill : trainSkills) {
                        if (TrainSkillConst.getRoundStartSkills().contains(trainSkill)) {
                            TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                            if (trainSkillProcessor != null) {
                                trainSkillProcessor.roundStart(fight, effector, new Object[0]);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int chageHurtMP(Fight fight, FightObject effector, FightObject target, int hurtMp, RoleSkill roleSkill) {
        int returnValue = 0;
        try {
            if (target != null && target.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)target.getRole().getId());
                if (trainSkills != null) {
                    int chageHurt = 0;
                    for (Integer trainSkill : trainSkills) {
                        if (TrainSkillConst.getchageHurtMPSkills().contains(trainSkill)) {
                            TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                            if (trainSkillProcessor != null) {
                                chageHurt += trainSkillProcessor.changeHurtMp(fight, effector, target, Integer.valueOf(hurtMp), roleSkill);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    return chageHurt;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    
    public static int chageHurt(Fight fight, FightObject effector, FightObject target, int hurt, RoleSkill roleSkill) {
        int returnValue = 0;
        try {
            if (effector != null && effector.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)effector.getRole().getId());
                if (trainSkills != null) {
                    returnValue += excuteChangeHurt(fight, effector, target, hurt, roleSkill, trainSkills, TrainSkillConst.getChangeHurtEffectorSkills());
                }
            }
            if (target != null && target.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)target.getRole().getId());
                if (trainSkills != null) {
                    returnValue += excuteChangeHurt(fight, effector, target, hurt, roleSkill, trainSkills, TrainSkillConst.getChangeHurtTargetSkills());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    
    private static int excuteChangeHurt(Fight fight, FightObject effector, FightObject target, int hurt, RoleSkill roleSkill, List<Integer> trainSkills, List<Integer> skills) {
        int chageHurt = 0;
        for (Integer trainSkill : trainSkills) {
            if (skills.contains(trainSkill)) {
                TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                if (trainSkillProcessor != null) {
                    chageHurt += trainSkillProcessor.changeHurt(fight, effector, target, Integer.valueOf(hurt), roleSkill);
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return chageHurt;
    }
    
    public static void beforeMagicAtta(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill) {
        try {
            if (effector != null && effector.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)effector.getRole().getId());
                if (trainSkills != null) {
                    for (Integer trainSkill : trainSkills) {
                        if (TrainSkillConst.getBeforeMagicAttaSkills().contains(trainSkill)) {
                            TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                            if (trainSkillProcessor != null) {
                                trainSkillProcessor.beforeMagicAtta(fight, effector, target, roleSkill);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<NutMap> addStatus(Fight fight, FightObject effector, FightObject target, int status) {
        try {
            List<NutMap> list = new ArrayList<>();
            List<FightObject> targetSide = fight.getTargetSide((int)target.getId());
            for (FightObject unit : targetSide) {
                if (unit == null) {
                    continue;
                }
                if (unit.isFallDown()) {
                    continue;
                }
                List<NutMap> targetSideList = excuteAddStatus(fight, unit, effector, target, status);
                if (targetSideList != null && targetSideList.size() > 0) {
                    list.addAll(targetSideList);
                }
                else {
                    continue;
                }
            }
            List<NutMap> targetList = excuteAddStatus(fight, target, effector, target, status);
            if (targetList != null && targetList.size() > 0) {
                list.addAll(targetList);
            }
            if (list.size() > 0) {
                return list;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static List<NutMap> excuteAddStatus(Fight fight, FightObject unit, FightObject effector, FightObject target, int status) {
        if (unit != null && unit.isPlayer()) {
            List<NutMap> list = new ArrayList<>();
            List<Integer> trainSkills = getTrainSkill((long)unit.getRole().getId());
            if (trainSkills != null) {
                for (Integer trainSkill : trainSkills) {
                    if (TrainSkillConst.getAddStatusSkills().contains(trainSkill)) {
                        TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                        if (trainSkillProcessor != null) {
                            NutMap nutMap = trainSkillProcessor.addStatus(fight, unit, target, Integer.valueOf(status));
                            if (nutMap != null) {
                                list.add(nutMap);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
            if (list.size() > 0) {
                return list;
            }
        }
        return null;
    }
    
    public static List<NutMap> reduceLifeEnd(Fight fight, FightObject effector, FightObject target, int hurt) {
        try {
            List<NutMap> list = new ArrayList<>();
            List<NutMap> targetList = excuteReduceLifeEnd(fight, target, effector, target, hurt, TrainSkillConst.getReduceLifeEndTargetSkills());
            if (targetList != null && targetList.size() > 0) {
                list.addAll(targetList);
            }
            List<NutMap> effectorList = excuteReduceLifeEnd(fight, effector, effector, target, hurt, TrainSkillConst.getReduceLifeEndEffectorSkills());
            if (effectorList != null && effectorList.size() > 0) {
                list.addAll(effectorList);
            }
            if (list.size() > 0) {
                return list;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static List<NutMap> excuteReduceLifeEnd(Fight fight, FightObject fightObject, FightObject effector, FightObject target, int hurt, List<Integer> skills) {
        if (fightObject != null && fightObject.isPlayer()) {
            List<Integer> trainSkills = getTrainSkill((long)fightObject.getRole().getId());
            if (trainSkills != null) {
                List<NutMap> list = new ArrayList<>();
                for (Integer trainSkill : trainSkills) {
                    if (skills.contains(trainSkill)) {
                        TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                        if (trainSkillProcessor != null) {
                            NutMap nutMap = trainSkillProcessor.reduceLifeEnd(fight, effector, target, Integer.valueOf(hurt));
                            if (nutMap != null) {
                                list.add(nutMap);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
                if (list.size() > 0) {
                    return list;
                }
            }
        }
        return null;
    }
    
    public static void magicAttackEnd(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, ActionDetailParam actionDetailParam) {
        try {
            if (effector != null && effector.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)effector.getRole().getId());
                if (trainSkills != null) {
                    for (Integer trainSkill : trainSkills) {
                        if (TrainSkillConst.getMagicAttackEndSkills().contains(trainSkill)) {
                            TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                            if (trainSkillProcessor != null) {
                                trainSkillProcessor.magicAttackEnd(fight, effector, target, roleSkill, actionDetailParam);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void magicAttackEndOne(Fight fight, FightObject effector, FightObject target, RoleSkill roleSkill, ActionDetailParam actionDetailParam) {
        try {
            if (effector != null && effector.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)effector.getRole().getId());
                if (trainSkills != null) {
                    for (Integer trainSkill : trainSkills) {
                        if (TrainSkillConst.getMagicAttackEndOneSkills().contains(trainSkill)) {
                            TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                            if (trainSkillProcessor != null) {
                                trainSkillProcessor.magicAttackEndOne(fight, effector, target, roleSkill, actionDetailParam);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void normalAttackAfter(Fight fight, FightObject effector, BattleUnit battleUnit) {
        try {
            if (effector != null && effector.isPlayer()) {
                List<Integer> trainSkills = getTrainSkill((long)effector.getRole().getId());
                if (trainSkills != null) {
                    for (Integer trainSkill : trainSkills) {
                        if (TrainSkillConst.getNormalAttackAfterSkills().contains(trainSkill)) {
                            TrainSkillProcessor trainSkillProcessor = TrainSkillProcessor.queryProcessor(trainSkill);
                            if (trainSkillProcessor != null) {
                                trainSkillProcessor.normalAttackAfter(fight, effector, battleUnit.getTarget(), battleUnit);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
