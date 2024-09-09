package com.kitty.system.game.train.pool;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysDataTrainAttr;
import java.util.List;
import com.kitty.system.init.domain.SysDataTrainSeal;
import com.kitty.system.init.domain.SysDataTrainExp;
import com.kitty.system.init.domain.SysDataTrain;
import java.util.Map;

public class TrainDataPool
{
    public static Map<Integer, SysDataTrain> trainMap;
    public static Map<Integer, SysDataTrainExp> trainExpMap;
    public static Map<Integer, SysDataTrainSeal> trainSealMap;
    public static List<SysDataTrainAttr> trainAttrList;
    public static Map<Integer, Integer> trainSkillSerialMap;
    
    public TrainDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        initTrain(initService);
        initTrainExp(initService);
        initTrainSeal(initService);
        initTrainAttr(initService);
    }
    
    public static void initTrain(InitService initService) {
        Object[] objects = initService.initTrain();
        TrainDataPool.trainMap = (Map)objects[0];
        TrainDataPool.trainSkillSerialMap = (Map)objects[1];
    }
    
    public static void initTrainExp(InitService initService) {
        TrainDataPool.trainExpMap = initService.initTrainExp();
    }
    
    public static void initTrainSeal(InitService initService) {
        TrainDataPool.trainSealMap = initService.initTrainSeal();
    }
    
    public static void initTrainAttr(InitService initService) {
        TrainDataPool.trainAttrList = initService.initTrainAttr();
    }
    
    static {
        TrainDataPool.trainMap = new ConcurrentHashMap<>();
        TrainDataPool.trainExpMap = new ConcurrentHashMap<>();
        TrainDataPool.trainSealMap = new ConcurrentHashMap<>();
        TrainDataPool.trainAttrList = new ArrayList<>();
        TrainDataPool.trainSkillSerialMap = new ConcurrentHashMap<>();
    }
}
