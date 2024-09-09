package com.kitty.system.game.train.helper;

import java.util.Arrays;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.LinkedHashMap;
import com.kitty.system.game.train.domain.vo.FieldSeal;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.train.make.TrainSealManage;
import com.kitty.system.game.train.pool.TrainDataPool;
import com.kitty.system.init.domain.SysDataTrainSeal;
import com.kitty.system.common.data.DataGlobalFunc;
import java.text.MessageFormat;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.train.domain.TrainSeal;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("TrainSealRefine")
public class TrainSealRefineHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "{0}数量不足";
    public static final double[] REBATE;
    public static final String[] NAME;
    public static final List<Integer> integers;
    
    public TrainSealRefineHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Long id, Integer type, String param, String cause) {
        int index = type % 1000;
        int genre = type / 1000;
        Map<Long, TrainSeal> trainSealMap = this.trainService.getTrainSeal(role.getId());
        if (trainSealMap == null || !trainSealMap.containsKey(id)) {
            return null;
        }
        TrainSeal trainSeal = trainSealMap.get(id);
        Map<Integer, Integer> consume = this.consume(genre, 75116);
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, (int)amount)) {
                    return null;
                }
            }
            else {
                Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return MessageFormat.format("{0}数量不足", TrainSealRefineHandler.NAME[index - 1]);
                }
            }
        }
        boolean deduct = this.deduct(role, consume);
        if (!deduct) {
            return "道具或者货币扣除失败";
        }
        if (genre == 1) {
            List<FieldSeal> refine2 = TrainSealManage.createRefine(TrainDataPool.trainSealMap.get(Integer.valueOf(trainSeal.getSerial())), TrainSealRefineHandler.integers);
            trainSeal.setRefineGeneralHistory(refine2);
        }
        else if (genre == 2) {
            trainSeal.getRefineHighHistory().clear();
            for (int i = 0; i < DataGlobalFunc.expend(genre); ++i) {
                List<FieldSeal> refine = TrainSealManage.createRefine(TrainDataPool.trainSealMap.get(Integer.valueOf(trainSeal.getSerial())), TrainSealRefineHandler.integers);
                trainSeal.getRefineHighHistory().add(refine);
            }
        }
        else if (genre == 3) {
            trainSeal.getRefineSuperHistory().clear();
            for (int i = 0; i < DataGlobalFunc.expend(genre); ++i) {
                List<FieldSeal> refine = TrainSealManage.createRefine(TrainDataPool.trainSealMap.get(Integer.valueOf(trainSeal.getSerial())), TrainSealRefineHandler.integers);
                trainSeal.getRefineSuperHistory().add(refine);
            }
        }
        trainSeal.save();
        this.sealPreRefine(role, trainSeal, (byte)genre);
        this.trainService.updateTrainRefine(role, DataGlobalFunc.expend(genre));
        return null;
    }
    
    public Map<Integer, Integer> consume(int type, int serial) {
        int num = DataGlobalFunc.expend(type);
        Map<Integer, Integer> map = new LinkedHashMap<>();
        map.put(serial, num);
        if (type == 1) {
            map.put(Const.RESTYPE_SILVER, 100000);
        }
        else if (type == 2) {
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (double) (100000 * num) * TrainSealRefineHandler.REBATE[type - 1]));
        }
        else if (type == 3) {
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (double) (100000 * num) * TrainSealRefineHandler.REBATE[type - 1]));
        }
        return map;
    }
    
    static {
        REBATE = new double[] { 1.0, 0.9, 0.85 };
        NAME = new String[] { "信物洗练符", "五色石" };
        integers = Arrays.asList(92, 7, 1);
    }
}
