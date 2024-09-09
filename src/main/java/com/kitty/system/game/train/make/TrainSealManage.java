package com.kitty.system.game.train.make;

import com.kitty.common.utils.RateRandomNumber;
import com.kitty.system.init.domain.vo.Prop;
import java.util.Iterator;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.system.init.domain.SysDataTrainAttr;
import java.util.Map;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.bean.Lottery;
import com.kitty.system.enums.common.TRAIN;
import java.util.ArrayList;
import com.kitty.system.game.train.domain.vo.FieldSeal;
import java.util.List;
import com.kitty.system.game.train.helper.TrainSealRefineHandler;
import com.kitty.system.game.train.pool.TrainDataPool;
import com.kitty.system.init.domain.SysDataTrainSeal;
import com.kitty.system.game.train.domain.TrainSeal;

public class TrainSealManage
{
    public TrainSealManage() {
    }
    
    public static TrainSeal createTrainSeal(int serial) {
        SysDataTrainSeal trainSeal = TrainDataPool.trainSealMap.get(serial);
        if (trainSeal == null) {
            return null;
        }
        TrainSeal seal = new TrainSeal();
        seal.setSerial(trainSeal.getSerial());
        seal.setName(trainSeal.getName());
        List<FieldSeal> refine = createRefine(trainSeal, TrainSealRefineHandler.integers);
        seal.setRefine(refine);
        return seal;
    }
    
    public static List<FieldSeal> createRefine(SysDataTrainSeal trainSeal, List<Integer> integers) {
        List<FieldSeal> list = new ArrayList<>();
        List<Lottery> lotteries = new ArrayList<>();
        lotteries.add(new Lottery(TRAIN.QIAN.getValue(), "", (double) trainSeal.getQian() * 1.0));
        lotteries.add(new Lottery(TRAIN.XUN.getValue(), "", (double) trainSeal.getXun() * 1.0));
        lotteries.add(new Lottery(TRAIN.KAN.getValue(), "", (double) trainSeal.getKan() * 1.0));
        lotteries.add(new Lottery(TRAIN.GEN.getValue(), "", (double) trainSeal.getGen() * 1.0));
        lotteries.add(new Lottery(TRAIN.KUN.getValue(), "", (double) trainSeal.getKun() * 1.0));
        lotteries.add(new Lottery(TRAIN.ZHEN.getValue(), "", (double) trainSeal.getZhen() * 1.0));
        lotteries.add(new Lottery(TRAIN.LI.getValue(), "", (double) trainSeal.getLi() * 1.0));
        lotteries.add(new Lottery(TRAIN.DUI.getValue(), "", (double) trainSeal.getDui() * 1.0));
        Map<Integer, Integer> map = PRO_DICT_TYPE.toMapAll();
        List<SysDataTrainAttr> collect = TrainDataPool.trainAttrList.stream().filter(e -> e.getType() == trainSeal.getRefineType()).collect(Collectors.toList());
        Map<Integer, SysDataTrainAttr> trainAttrMap = collect.stream().collect(Collectors.toMap(SysDataTrainAttr::getDict, SysDataTrainAttr -> SysDataTrainAttr));
        for (int i = 0; i < 3; ++i) {
            List<Double> rates = new ArrayList<>(lotteries.size());
            Iterator<Lottery> iterator = lotteries.iterator();
            Lottery lottery;
            while (iterator.hasNext()) {
                lottery = iterator.next();
                rates.add(lottery.getBili());
            }
            Lottery lottery2 = lotteries.get(LotteryUtils.lottery(rates));
            SysDataTrainAttr trainAttr = trainAttrMap.get(lottery2.getSerial());
            List<FieldValue> baseAttrList = trainAttr.getBaseAttrList();
            FieldValue fieldValue = baseAttrList.get(RandomUtil.round(0, baseAttrList.size() - 1));
            Prop prop = getProp(trainAttr.getProp());
            double rate = createRndRange(new double[] { (double)prop.getOnset(), (double)prop.getOver() }, integers);
            double base = rate / 10000.0 * (double)fieldValue.getV();
            Integer genre = map.get(fieldValue.getK());
            if (genre.equals(PRO_TYPE.Pro_Value_NUM_TYPE.getValue())) {
                list.add(new FieldSeal(lottery2.getSerial(), fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(0, base), rate));
            }
            else {
                list.add(new FieldSeal(lottery2.getSerial(), fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(2, base), rate));
            }
            lotteries.removeIf(e -> e.getSerial().equals(lottery2.getSerial()));
        }
        return list;
    }
    
    public static Prop getProp(List<Prop> propList) {
        List<Lottery> list = new ArrayList<>();
        propList.forEach(obj -> list.add(new Lottery(obj.getSerial(), "", obj.getProp() * 1.0)));
        List<Double> rates = new ArrayList<>(list.size());
        Iterator<Lottery> iterator = list.iterator();
        while (iterator.hasNext()) {
        	Lottery lottery = iterator.next();
            rates.add(lottery.getBili());
        }
        Lottery lottery2 = list.get(LotteryUtils.lottery(rates));
        return propList.stream().filter(e -> lottery2.getSerial().equals(e.getSerial())).findFirst().orElse(null);
    }
    
    public static double createRndRange(double[] rndRange, List<Integer> percents) {
        List<Double> separates = new ArrayList<>();
        double rnd = 5.0;
        if (rndRange[0] == 5.0) {
            rnd = 1.0;
        }
        separates.add(rndRange[0] + rnd);
        separates.add(rndRange[1] - 1.0);
        return BigDecimalUtil.decimalScaleObjectDown(2, RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents));
    }
}
