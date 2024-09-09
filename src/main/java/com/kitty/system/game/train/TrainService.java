package com.kitty.system.game.train;

import com.kitty.system.game.train.domain.vo.FieldSeal;
import org.nutz.lang.util.NutMap;
import java.util.Collections;
import com.kitty.system.game.vo.utils.Const;
import java.util.Arrays;
import com.kitty.system.init.domain.SysDataTrainExp;
import com.kitty.system.game.train.message.ReqUpTrainGrade;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.enums.common.TRAIN;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.train.message.ReqChangeSeal;
import com.kitty.system.game.train.message.ReqMagicCircle;
import com.kitty.system.game.train.helper.UpgradeHandler;
import com.kitty.system.common.message.RespNotifyMiscEx;
import org.nutz.lang.Strings;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.train.helper.UpgradeHelper;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.train.message.RespTrainSeal;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.bag.message.RespIconCartoon;
import com.kitty.system.game.train.message.RespTrainRefine;
import java.util.Iterator;
import com.kitty.system.game.train.message.RespMagicCircle;
import java.util.ArrayList;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.train.message.RespTrain;
import com.kitty.listener.event.LoginEvent;
import java.util.List;
import java.util.stream.Collectors;
import com.kitty.system.game.train.domain.TrainSeal;
import com.kitty.system.init.domain.SysDataTrain;
import com.kitty.system.game.train.pool.TrainDataPool;
import java.util.Map;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.TrainCache;
import com.kitty.system.game.train.domain.Train;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.thread.IdWorker;
import javax.annotation.Resource;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class TrainService
{
    @Resource
    private Dao dao;
    @Resource
    private IdWorker worker;
    @Resource
    private BagService bagService;
    
    public TrainService() {
    }
    
    public Train getTrain(Long id) {
        Train train = TrainCache.TRAIN_MAP.get(id);
        if (train != null) {
            return train;
        }
        train = this.dao.fetch(Train.class, Cnd.where("r_id", "=", id));
        if (train == null) {
            train = new Train(this.worker.nextId(), id);
            Map.Entry<Integer, SysDataTrain> entry = TrainDataPool.trainMap.entrySet().stream().findFirst().orElse(null);
            train.setMagicCircle((TrainDataPool.trainMap.get(entry.getKey())).getSerial());
            train.save();
        }
        TrainCache.TRAIN_MAP.put(id, train);
        return train;
    }
    
    public Map<Long, TrainSeal> getTrainSeal(Long id) {
        Map<Long, TrainSeal> sealMap = TrainCache.TRAIN_SEAL_MAP.get(id);
        if (sealMap != null) {
            return sealMap;
        }
        List<TrainSeal> seals = this.dao.query(TrainSeal.class, Cnd.where("r_id", "=", id));
        TrainCache.TRAIN_SEAL_MAP.put(id, seals.stream().collect(Collectors.toMap(TrainSeal::getId, TrainSeal -> TrainSeal)));
        return TrainCache.TRAIN_SEAL_MAP.get(id);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        Train train = this.getTrain(role.getId());
        MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        this.refreshRefine(role.getId(), train);
        Map<Long, TrainSeal> trainSealMap = this.getTrainSeal(role.getId());
        this.respMagicCircle(role, train, trainSealMap);
        trainSealMap.forEach((key, value) -> this.refreshTrainSeal(role.getId(), value, (byte)0));
    }
    
    public void respMagicCircle(Role role, Train train, Map<Long, TrainSeal> trainSealMap) {
        byte t = 0;
        SysDataTrain dataTrain = TrainDataPool.trainMap.get(train.getMagicCircle());
        if (dataTrain != null) {
            List<Integer> objects = new ArrayList<>(dataTrain.getActiveCond().size());
            objects.addAll(dataTrain.getActiveCond());
            List<TrainSeal> sealList = trainSealMap.values().stream().filter(e -> e.getPosition() > 0).collect(Collectors.toList());
            for (TrainSeal seal : sealList) {
                objects.removeIf(e -> e == seal.getPosition());
            }
            if (objects.size() <= 0) {
                t = 1;
            }
        }
        MessagePusher.pushMessage(role.getId(), new RespMagicCircle(train.getMagicCircle(), t));
    }
    
    public void refreshRefine(long id, Train train) {
        MessagePusher.pushMessage(id, new RespTrainRefine(train.getRefine()));
    }
    
    public void updateTrainRefine(Role role, int num) {
        Train train = this.getTrain(role.getId());
        if (train.getRefine() >= 5000) {
            return;
        }
        int refine = train.getRefine() + num;
        train.setRefine((refine > 5000) ? 5000 : refine);
        train.save();
        this.refreshRefine(role.getId(), train);
    }
    
    public void addTrainSeal(Role role, TrainSeal trainSeal, boolean flag, String cause) {
        trainSeal.save();
        Map<Long, TrainSeal> sealMap = this.getTrainSeal(role.getId());
        sealMap.put(trainSeal.getId(), trainSeal);
        this.refreshTrainSeal(role.getId(), trainSeal, (byte)0);
        if (flag) {
            RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(trainSeal.getSerial());
            respIconCartoon.setOnlyId(String.valueOf(trainSeal.getId()));
            MessagePusher.pushMessage(role, respIconCartoon);
        }
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)5, (byte)1, cause + "，信物入包 - " + JsonUtils.object2String(trainSeal), role.getMarking());
    }
    
    public void refreshTrainSeal(Long id, TrainSeal value, byte type) {
        MessagePusher.pushMessage(id, new RespTrainSeal(value, type));
    }
    
    public void upgrade(Role role, long id, int type, String param, int operationType, boolean flag, String cause) {
        if (type != 25 && !SafeUtil.isUnlock(role)) {
            return;
        }
        UpgradeHandler handler = UpgradeHelper.INSTANCE.getHandler(type);
        if (handler != null) {
            String message = handler.acceptUpgrade(role, id, operationType, param, cause);
            if (!Strings.isEmpty(message) && message != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), message, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
    
    public void switchMagicCircle(Role role, ReqMagicCircle reqMagicCircle) {
        SysDataTrain sysDataTrain = TrainDataPool.trainMap.get(reqMagicCircle.getMagicCircle());
        if (sysDataTrain == null) {
            return;
        }
        Train train = this.getTrain(role.getId());
        train.setMagicCircle(sysDataTrain.getSerial());
        train.save();
        this.respMagicCircle(role, train, this.getTrainSeal(role.getId()));
    }
    
    public void changeSeal(Role role, ReqChangeSeal reqChangeSeal) {
        if (!NumberUtil.isNumber(reqChangeSeal.getId())) {
            return;
        }
        byte type = reqChangeSeal.getType();
        long id = Long.valueOf(reqChangeSeal.getId());
        if (1 == type) {
            this.wearSeal(role, id, reqChangeSeal.getPosition());
        }
        else if (0 == type) {
            this.unSeal(role, id, reqChangeSeal.getPosition());
        }
    }
    
    public void wearSeal(Role role, long id, short position) {
        Map<Long, TrainSeal> sealMap = this.getTrainSeal(role.getId());
        TrainSeal seal = sealMap.get(id);
        if (seal == null) {
            return;
        }
        sealMap.forEach((k, v) -> {
            if (v.getPosition() == position) {
                this.unSeal(role, v.getId(), position);
            }
        });
        this.executeWearSeal(role, seal, position);
    }
    
    public void executeWearSeal(Role role, TrainSeal seal, short position) {
        if (position < TRAIN.QIAN.getValue() || position > TRAIN.DUI.getValue()) {
            return;
        }
        long count = this.getTrainSeal(role.getId()).values().stream().filter(e -> e.getPosition() > 0).count();
        if (count >= 5L) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("信物数量已达上限，无法佩戴。"));
            return;
        }
        Train train = this.getTrain(role.getId());
        int sealWearLimit = getTrainLimit(train);
        if (count + 1L > (long)sealWearLimit) {
            MessagePusher.pushMessage(role.getId(), new RespHint(31, (short)0));
            MessagePusher.pushMessage(role.getId(), new RespMsg(String.format("您的阵位最低等级到达%d级后，即可佩戴%d个信物", sealWearLimit * 10, sealWearLimit)));
            return;
        }
        seal.setPosition(position);
        seal.save();
        MessagePusher.pushMessage(role.getId(), new RespTrainSeal(seal, (byte)0));
        this.respMagicCircle(role, train, this.getTrainSeal(role.getId()));
    }
    
    public void unSeal(Role role, long id, short position) {
        Map<Long, TrainSeal> trainSealMap = this.getTrainSeal(role.getId());
        TrainSeal trainSeal = trainSealMap.get(id);
        if (trainSeal == null) {
            return;
        }
        if (trainSeal.getPosition() <= 0 || trainSeal.getPosition() != position) {
            return;
        }
        trainSeal.setPosition((short)0);
        trainSeal.save();
        MessagePusher.pushMessage(role.getId(), new RespTrainSeal(trainSeal, (byte)0));
        this.respMagicCircle(role, this.getTrain(role.getId()), trainSealMap);
    }
    
    public void upTrainGrade(Role role, ReqUpTrainGrade reqUpTrainGrade) {
        if (reqUpTrainGrade.getGrade() <= 0) {
            return;
        }
        Train train = this.getTrain(role.getId());
        if (!this.trainUpGradeCheck(train, reqUpTrainGrade.getPosition(), reqUpTrainGrade.getGrade())) {
            return;
        }
        if (reqUpTrainGrade.getPosition() == TRAIN.QIAN.getValue()) {
            int grade = Math.min(train.getQianGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getQianGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升乾位等级");
            if (integer != null) {
                return;
            }
            train.setQianGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.XUN.getValue()) {
            int grade = Math.min(train.getXunGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getXunGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升巽位等级");
            if (integer != null) {
                return;
            }
            train.setXunGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.KAN.getValue()) {
            int grade = Math.min(train.getKanGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getKanGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升坎位等级");
            if (integer != null) {
                return;
            }
            train.setKanGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.GEN.getValue()) {
            int grade = Math.min(train.getGenGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getGenGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升艮位等级");
            if (integer != null) {
                return;
            }
            train.setGenGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.KUN.getValue()) {
            int grade = Math.min(train.getKunGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getKunGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升坤位等级");
            if (integer != null) {
                return;
            }
            train.setKunGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.ZHEN.getValue()) {
            int grade = Math.min(train.getZhenGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getZhenGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升震位等级");
            if (integer != null) {
                return;
            }
            train.setZhenGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.LI.getValue()) {
            int grade = Math.min(train.getLiGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getLiGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升离位等级");
            if (integer != null) {
                return;
            }
            train.setLiGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
        else if (reqUpTrainGrade.getPosition() == TRAIN.DUI.getValue()) {
            int grade = Math.min(train.getDuiGrade() + reqUpTrainGrade.getGrade(), 100);
            int num = this.getNum(grade, train.getDuiGrade());
            Integer integer = this.bagService.consumeBagGoods(role, 75406, num, true, true, "提升兑位等级");
            if (integer != null) {
                return;
            }
            train.setDuiGrade(grade);
            train.save();
            MessagePusher.pushMessage(role.getId(), new RespTrain(train));
        }
    }
    
    public int getNum(int grade, int trainGrade) {
        int num = 0;
        for (int i = 1; i <= grade - trainGrade; ++i) {
            SysDataTrainExp trainExp = TrainDataPool.trainExpMap.get(trainGrade + i);
            num += trainExp.getXun();
        }
        return num;
    }
    
    public boolean trainUpGradeCheck(Train train, int position, int addGrade) {
        List<Integer> list = null;
        int currentGrade = 0;
        if (position == TRAIN.QIAN.getValue()) {
            currentGrade = train.getQianGrade();
            list = Arrays.asList(train.getXunGrade(), train.getKanGrade(), train.getGenGrade(), train.getKunGrade(), train.getZhenGrade(), train.getLiGrade(), train.getDuiGrade());
        }
        else if (position == TRAIN.XUN.getValue()) {
            currentGrade = train.getXunGrade();
            list = Arrays.asList(train.getQianGrade(), train.getKanGrade(), train.getGenGrade(), train.getKunGrade(), train.getZhenGrade(), train.getLiGrade(), train.getDuiGrade());
        }
        else if (position == TRAIN.KAN.getValue()) {
            currentGrade = train.getKanGrade();
            list = Arrays.asList(train.getQianGrade(), train.getXunGrade(), train.getGenGrade(), train.getKunGrade(), train.getZhenGrade(), train.getLiGrade(), train.getDuiGrade());
        }
        else if (position == TRAIN.GEN.getValue()) {
            currentGrade = train.getGenGrade();
            list = Arrays.asList(train.getQianGrade(), train.getXunGrade(), train.getKanGrade(), train.getKunGrade(), train.getZhenGrade(), train.getLiGrade(), train.getDuiGrade());
        }
        else if (position == TRAIN.KUN.getValue()) {
            currentGrade = train.getKunGrade();
            list = Arrays.asList(Integer.valueOf(train.getQianGrade()), Integer.valueOf(train.getXunGrade()), Integer.valueOf(train.getKanGrade()), Integer.valueOf(train.getGenGrade()), Integer.valueOf(train.getZhenGrade()), Integer.valueOf(train.getLiGrade()), Integer.valueOf(train.getDuiGrade()));
        }
        else if (position == TRAIN.ZHEN.getValue()) {
            currentGrade = train.getZhenGrade();
            list = Arrays.asList(Integer.valueOf(train.getQianGrade()), Integer.valueOf(train.getXunGrade()), Integer.valueOf(train.getKanGrade()), Integer.valueOf(train.getGenGrade()), Integer.valueOf(train.getKunGrade()), Integer.valueOf(train.getLiGrade()), Integer.valueOf(train.getDuiGrade()));
        }
        else if (position == TRAIN.LI.getValue()) {
            currentGrade = train.getLiGrade();
            list = Arrays.asList(Integer.valueOf(train.getQianGrade()), Integer.valueOf(train.getXunGrade()), Integer.valueOf(train.getKanGrade()), Integer.valueOf(train.getGenGrade()), Integer.valueOf(train.getKunGrade()), Integer.valueOf(train.getZhenGrade()), Integer.valueOf(train.getDuiGrade()));
        }
        else if (position == TRAIN.DUI.getValue()) {
            currentGrade = train.getDuiGrade();
            list = Arrays.asList(Integer.valueOf(train.getQianGrade()), Integer.valueOf(train.getXunGrade()), Integer.valueOf(train.getKanGrade()), Integer.valueOf(train.getGenGrade()), Integer.valueOf(train.getKunGrade()), Integer.valueOf(train.getZhenGrade()), Integer.valueOf(train.getLiGrade()));
        }
        if (currentGrade >= 100) {
            MessagePusher.pushMessage(train.getRId(), new RespMsg(String.format("此阵位已升至上限%d级~", Integer.valueOf(100))));
            return false;
        }
        for (Integer gd : Const.TRAIN_GRADE) {
            if (Collections.min(list) < (int)gd && currentGrade + addGrade > gd) {
                MessagePusher.pushMessage(train.getRId(), new RespMsg(String.format("先将所有位置升级至%d等级再进行升级~", gd)));
                return false;
            }
        }
        return true;
    }
    
    public NutMap countSearEffectivityPro(Role role) {
        NutMap nutMap = new NutMap();
        if (role.getRelive() * 1000 + role.getLevel() < 1110) {
            return nutMap;
        }
        Map<Long, TrainSeal> trainSeal = this.getTrainSeal(role.getId());
        List<TrainSeal> list = trainSeal.values().stream().filter(e -> e.getPosition() > 0).collect(Collectors.toList());
        list.forEach(e -> {
            if (e.getPosition() == TRAIN.QIAN.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.QIAN.getValue()) {
                        String k4 = String.valueOf(f.getK());
                        double v8 = nutMap.getDouble(k4, 0.0);
                        nutMap.setv(k4, v8 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.XUN.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.XUN.getValue()) {
                        String k3 = String.valueOf(f.getK());
                        double v7 = nutMap.getDouble(k3, 0.0);
                        nutMap.setv(k3, v7 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.KAN.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.KAN.getValue()) {
                        String k2 = String.valueOf(f.getK());
                        double v6 = nutMap.getDouble(k2, 0.0);
                        nutMap.setv(k2, v6 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.GEN.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.GEN.getValue()) {
                        String m = String.valueOf(f.getK());
                        double v5 = nutMap.getDouble(m, 0.0);
                        nutMap.setv(m, v5 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.KUN.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.KUN.getValue()) {
                        String l = String.valueOf(f.getK());
                        double v4 = nutMap.getDouble(l, 0.0);
                        nutMap.setv(l, v4 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.ZHEN.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.ZHEN.getValue()) {
                        String j = String.valueOf(f.getK());
                        double v3 = nutMap.getDouble(j, 0.0);
                        nutMap.setv(j, v3 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.LI.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.LI.getValue()) {
                        String i = String.valueOf(f.getK());
                        double v2 = nutMap.getDouble(i, 0.0);
                        nutMap.setv(i, v2 + (double) f.getV() / 1000.0);
                    }
                });
            }
            else if (e.getPosition() == TRAIN.DUI.getValue()) {
                e.getRefine().forEach(f -> {
                    if (f.getT() == TRAIN.DUI.getValue()) {
                        String k = String.valueOf(f.getK());
                        double v = nutMap.getDouble(k, 0.0);
                        nutMap.setv(k, v + (double) f.getV() / 1000.0);
                    }
                });
            }
        });
        return nutMap;
    }
    
    public void trainPerfect(Role role) {
        if (role.getRelive() * 1000 + role.getLevel() < 1110) {
            return;
        }
        Train train = this.getTrain(role.getId());
        Map<Long, TrainSeal> trainSeal = this.getTrainSeal(role.getId());
        SysDataTrain dataTrain = TrainDataPool.trainMap.get(train.getMagicCircle());
        if (trainSeal.size() < dataTrain.getActiveCond().size()) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("所需的信物不够"));
            return;
        }
        int sealWearLimit = getTrainLimit(train);
        if (sealWearLimit < dataTrain.getActiveCond().size()) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("阵法等级不够"));
            return;
        }
        List<TrainSeal> sealList = trainSeal.values().stream().filter(e -> e.getPosition() > 0).collect(Collectors.toList());
        sealList.forEach(e -> {
            if (!dataTrain.getActiveCond().contains(Short.valueOf(e.getPosition()))) {
                this.unSeal(role, e.getId(), e.getPosition());
            }
        });
        List<Integer> objects = new ArrayList<>(dataTrain.getActiveCond().size());
        objects.addAll(dataTrain.getActiveCond());
        sealList = trainSeal.values().stream().filter(e -> e.getPosition() > 0).collect(Collectors.toList());
        sealList.forEach(e -> {
            if (dataTrain.getActiveCond().contains(Short.valueOf(e.getPosition()))) {
                objects.removeIf(o -> o == e.getPosition());
            }
        });
        for (Integer position : objects) {
            List<TrainSeal> collect = trainSeal.values().stream().filter(e -> e.getPosition() <= 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                TrainSeal seal = collect.get(0);
                this.wearSeal(role, seal.getId(), position.shortValue());
            }
        }
        this.respMagicCircle(role, train, trainSeal);
    }
    
    public static int getTrainLimit(Train train) {
        int sealWearLimit = 1;
        int min = train.getMin();
        if (min > 0) {
            sealWearLimit = Math.min(sealWearLimit + min / 10, 5);
        }
        return sealWearLimit;
    }
}
