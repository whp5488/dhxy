
package com.kitty.system.game.equip.make;

import org.slf4j.LoggerFactory;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.enums.war.itempro.def.PET_EQUIP_PRO_DICT_SCORE;
import com.kitty.system.enums.common.ACC_GRADE;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_LH_DICT;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.init.domain.vo.Prop;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.init.domain.SysDataAcsryAttr;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT_SCORE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.common.utils.RateRandomNumber;
import com.kitty.system.game.equip.pool.EquipDataPool;
import java.util.Random;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.common.utils.MapObjectUtil;
import java.util.ArrayList;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import org.slf4j.Logger;

public class MakeAttribute
{
    static Logger logger;
    
    public static List<FiedEquip> createBaseBieEq(final String baseAttr, final int maxEndure, final int serial, final int bn, final int coef, final double refining) {
        try {
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            final Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
            for (final String key : baseMap.keySet()) {
                final int dist = CONVERT_DICT.getDist(key);
                if (dist > 0) {
                    int value = baseMap.get(key);
                    if (refining > 0.0 && coef > 0) {
                        value += (int)(value * (coef / 1000.0 * (refining / 100.0)));
                    }
                    field.add(new FiedEquip(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial(), value));
                }
                else {
                    MakeAttribute.logger.error("属性字典：%s,未找到解析", dist);
                }
            }
            field.add(new FiedEquip(6670, bn));
            field.add(new FiedEquip(6672, maxEndure));
            return field;
        }
        catch (final Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成装备基础属性抛出的异常{}", serial, e.getMessage());
            return null;
        }
    }
    
    public static List<FiedEquip> createBaseBieEq(final double refining, final int coef, final String baseAttr, final int maxEndure, final int serial, final int bn, final boolean flag) {
        try {
            final List<FiedEquip> field = new ArrayList<>();
            final Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
            for (final String key : baseMap.keySet()) {
                final int dist = CONVERT_DICT.getDist(key);
                if (dist > 0) {
                    final double value = baseMap.get(key) * (coef / 1000.0 * (refining / 100.0));
                    field.add(new FiedEquip(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial(), baseMap.get(key) + BigDecimalUtil.decimalScaleIntegerDown(0, value)));
                }
                else {
                    MakeAttribute.logger.error("属性字典：%s,未找到解析", dist);
                }
            }
            field.add(new FiedEquip(6670, bn));
            field.add(new FiedEquip(6672, maxEndure));
            if (flag) {
                final SysPurifyAttr purify = getPurify();
                final int dist2 = CONVERT_DICT.getDist(purify.getDict());
                if (dist2 > 0) {
                    field.add(new FiedEquip(dist2 + PRO_DICT_SERIAL.EQUIP_PRO.getSerial(), BigDecimalUtil.decimalScaleIntegerDown(0, createRndRange(new double[] { purify.getLowerBound(), purify.getUpperBound() }, 1, 2))));
                }
                else {
                    MakeAttribute.logger.error("隐属性字典：{},未找到解析", dist2);
                }
                field.add(new FiedEquip(6672, maxEndure));
            }
            field.add(new FiedEquip(6681, (int)refining * 1000));
            return field;
        }
        catch (final Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成五阶仙器装备基础属性抛出的异常{}", serial, e.getMessage());
            return null;
        }
    }
    
    public static SysPurifyAttr getPurify() {
        final Random random = new Random();
        final List<SysPurifyAttr> list = EquipDataPool.purify;
        final int index = random.nextInt(list.size());
        return list.get(index);
    }
    
    public static List<FiedEquip> createBaseBieEq(final String baseAttr, final int maxEndure, final int serial, final double[] rndRange, final int bn) {
        if (rndRange.length != 2) {
            return null;
        }
        try {
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            final Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
            for (final String key : baseMap.keySet()) {
                final int dist = CONVERT_DICT.getDist(key);
                if (dist > 0) {
                    field.add(new FiedEquip(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial(), BigDecimalUtil.decimalScaleIntegerDown(0, createRndRange(rndRange) * baseMap.get(key) / 100.0)));
                }
                else {
                    MakeAttribute.logger.error("属性字典：%s,未找到解析", dist);
                }
            }
            field.add(new FiedEquip(6670, bn));
            field.add(new FiedEquip(6672, maxEndure));
            return field;
        }
        catch (final Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成神兵装备基础属性抛出的异常{}", (Object)serial, (Object)e.getMessage());
            return null;
        }
    }
    
    public static double createRndRange(final double[] rndRange, final List<Integer> percents) {
        final List<Double> separates = new ArrayList<Double>();
        double rnd = 5.0;
        if (rndRange[0] == 5.0) {
            rnd = 1.0;
        }
        separates.add(rndRange[0] + rnd);
        separates.add(rndRange[1] - 1.0);
        return BigDecimalUtil.decimalScaleObjectDown(2, RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents));
    }
    
    public static double createRndRange(final double[] rndRange) {
        final List<Double> separates = new ArrayList<>();
        double rnd = 5.0;
        if (rndRange[0] == 5.0) {
            rnd = 1.0;
        }
        separates.add(rndRange[0] + rnd);
        separates.add(rndRange[1] - 1.0);
        final List<Integer> percents = new ArrayList<>();
        percents.add(85);
        percents.add(13);
        percents.add(2);
        return BigDecimalUtil.decimalScaleObjectDown(2, RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents));
    }
    
    public static double createRndRange(final double[] rndRange, final int lower, final int upper) {
        final List<Double> separates = new ArrayList<Double>();
        separates.add(rndRange[0] + lower);
        separates.add(rndRange[1] - upper);
        final List<Integer> percents = new ArrayList<Integer>();
        percents.add(85);
        percents.add(13);
        percents.add(2);
        return BigDecimalUtil.decimalScaleObjectDown(2, RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents));
    }
    
    public static double createRndRangeBase(final double[] rndRange) {
        final List<Double> separates = new ArrayList<Double>();
        if (rndRange[0] < 0.0) {
            separates.add(rndRange[0] + Math.abs(rndRange[0] * 0.2));
        }
        else {
            separates.add(rndRange[0] + 5.0);
        }
        if (rndRange[1] < 0.0) {
            separates.add(rndRange[1] - Math.abs(rndRange[1] * 0.1));
        }
        else {
            separates.add(rndRange[1] - 1.0);
        }
        final List<Integer> percents = new ArrayList<Integer>();
        if (rndRange[0] < 0.0) {
            percents.add(1);
            percents.add(7);
            percents.add(92);
        }
        else {
            percents.add(92);
            percents.add(7);
            percents.add(1);
        }
        return BigDecimalUtil.decimalScaleObjectDown(2, RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents));
    }
    
    public static List<FiedEquip> createHighBaseBieEq(final String baseAttr, final int maxEndure, final int serial, final double factor, final int bn) {
        final Map<String, SysAboveAttrLib> aboveMap = EquipDataPool.above_attr_lib;
        try {
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            final String[] split2;
            final String[] split = split2 = baseAttr.split(",");
            for (final String dist : split2) {
                final SysAboveAttrLib lib = aboveMap.get(dist);
                if (lib != null) {
                    final String base = lib.getBaseAttr();
                    if (base != null) {
                        if (!base.isEmpty()) {
                            final String[] bases = base.split(";");
                            if (bases.length > 0) {
                                final Random random = new Random();
                                final String curBase = bases[random.nextInt(bases.length)];
                                final String[] curBaseArr = curBase.split(",");
                                if (curBaseArr.length == 3) {
                                    final String curKey = curBaseArr[0];
                                    final int curMinValue = Integer.valueOf(curBaseArr[1]);
                                    final int curMaxValue = Integer.valueOf(curBaseArr[2]);
                                    final int range = createHighRndRange(lib.getRndRange());
                                    final int curValue = curMinValue + BigDecimalUtil.decimalScaleIntegerDown(0, range * (curMaxValue - curMinValue) / 100);
                                    final int pro = CONVERT_DICT.getDist(curKey);
                                    if (pro > 0) {
                                        field.add(new FiedEquip(pro + PRO_DICT_SERIAL.EQUIP_PRO.getSerial(), BigDecimalUtil.decimalScaleIntegerDown(0, curValue * factor)));
                                    }
                                    else {
                                        MakeAttribute.logger.error("创建高级装备基础数据属性字典：%s,未找到解析", (Object)pro);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            field.add(new FiedEquip(6670, bn));
            field.add(new FiedEquip(6672, maxEndure));
            return field;
        }
        catch (final Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成装备基础属性抛出的异常{}", (Object)serial, (Object)e.getMessage());
            return null;
        }
    }
    
    public static int createHighRndRange(final String rndRange) {
        if (rndRange.isEmpty() || rndRange == null) {
            return 1;
        }
        final double rndValue = createRndRange(new double[] { 1.0, 100.0 });
        final String[] split = rndRange.split(";");
        if (split.length <= 0) {
            return 1;
        }
        final List<String[]> list = new ArrayList<String[]>();
        for (final String str : split) {
            final String[] strings = str.split(",");
            if (strings.length != 3) {
                return 1;
            }
            list.add(strings);
        }
        int minRndValue = 1;
        int maxRndValue = 100;
        int startValue = 0;
        for (int i = 0; i < list.size(); ++i) {
            final String[] strings = list.get(i);
            final int v = Integer.valueOf(strings[2]);
            if (rndValue >= startValue && rndValue <= startValue + v) {
                minRndValue = Integer.valueOf(strings[0]);
                maxRndValue = Integer.valueOf(strings[1]);
                break;
            }
            startValue += v;
        }
        return minRndValue + BigDecimalUtil.decimalScaleIntegerDown(0, Math.random() * (maxRndValue - minRndValue));
    }
    
    public static BagEquip createBaseAcsry(final SysDataAcsry acsry, final int pos, final List<Integer> integers) {
        try {
            double score = 0.0;
            final Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            final Map<String, SysDataAcsryAttr> acsryAttrMap = EquipDataPool.acsryAttrMap;
            final Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
            final String[] split2;
            final String[] split = split2 = acsry.getBaseAttr().split(",");
            for (final String dict : split2) {
                final SysDataAcsryAttr acsryAttr = acsryAttrMap.get(dict);
                if (acsryAttr != null) {
                    final List<FieldValue> baseAttrList = acsryAttr.getBaseAttrList();
                    final int index = RandomUtil.round(0, baseAttrList.size() - 1);
                    final FieldValue fieldValue = baseAttrList.get(index);
                    final List<Lottery> list = EquipDataPool.acsryAttrLotteryMap.get(dict);
                    final List<Double> rates = new ArrayList<Double>(list.size());
                    final Iterator<Lottery> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        rates.add(iterator.next().getBili());
                    }
                    Lottery lottery = list.get(LotteryUtils.lottery(rates));
                    final Prop prop = acsryAttr.getProp().stream().filter(e -> lottery.getSerial().equals(e.getSerial())).findFirst().orElse(null);
                    if (prop == null) {
                        MakeAttribute.logger.error("配饰数据错误");
                    }
                    else {
                        final double rate = createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers);
                        final double base = rate / 10000.0 * fieldValue.getV();
                        final int dist = DataGlobalFunc.GetProDictType(fieldValue.getK());
                        final Double odds = map.get(dist);
                        if (odds != null) {
                            final double value = base;
                            score += value * odds;
                        }
                        final Integer genre = proMap.get(fieldValue.getK() - PRO_DICT_SERIAL.PS.getSerial());
                        if (genre.equals(PRO_TYPE.Pro_Value_NUM_TYPE.getValue())) {
                            field.add(new FiedEquip(fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(0, base), rate));
                        }
                        else {
                            field.add(new FiedEquip(fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(2, base), rate));
                        }
                    }
                }
            }
            field.add(new FiedEquip(6672, acsry.getMaxEndure()));
            field.add(new FiedEquip(6679, BigDecimalUtil.decimalScaleIntegerDown(0, score) + acsry.getBaseScore()));
            final BagEquip equip = new BagEquip();
            equip.setPos(pos);
            equip.setName(acsry.getName());
            equip.setPosition(acsry.getPos());
            equip.setType(acsry.getSerial());
            equip.setField(field);
            return equip;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<FiedEquip> createBaseAcsry(final SysDataAcsry acsry, final List<FiedEquip> original, final List<Integer> integers) {
        try {
            int endure = acsry.getMaxEndure();
            double score = 0.0;
            final Map<String, SysDataAcsryAttr> acsryAttrMap = EquipDataPool.acsryAttrMap;
            final Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            final Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
            final String[] split2;
            final String[] split = split2 = acsry.getBaseAttr().split(",");
            for (final String dict : split2) {
                final SysDataAcsryAttr acsryAttr = acsryAttrMap.get(dict);
                if (acsryAttr != null) {
                    final List<FieldValue> baseAttrList = acsryAttr.getBaseAttrList();
                    final int index = RandomUtil.round(0, baseAttrList.size() - 1);
                    final FieldValue fieldValue = baseAttrList.get(index);
                    final List<Lottery> list = EquipDataPool.acsryAttrLotteryMap.get(dict);
                    final List<Double> rates = new ArrayList<Double>(list.size());
                    final Iterator<Lottery> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        rates.add( iterator.next().getBili());
                    }
                    Lottery lottery = list.get(LotteryUtils.lottery(rates));
                    final Prop prop = acsryAttr.getProp().stream().filter(e -> lottery.getSerial().equals(e.getSerial())).findFirst().orElse(null);
                    if (prop == null) {
                        MakeAttribute.logger.error("配饰数据错误");
                    }
                    else {
                        final int rate = BigDecimalUtil.decimalScaleIntegerDown(0, createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers));
                        final double base = rate / 10000.0 * fieldValue.getV();
                        final int dist = DataGlobalFunc.GetProDictType(fieldValue.getK());
                        final Double odds = map.get(dist);
                        if (odds != null) {
                            final double value = base;
                            score += value * odds;
                        }
                        final Integer genre = proMap.get(fieldValue.getK() - PRO_DICT_SERIAL.PS.getSerial());
                        if (genre.equals(PRO_TYPE.Pro_Value_NUM_TYPE.getValue())) {
                            field.add(new FiedEquip(fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(0, base), rate));
                        }
                        else {
                            field.add(new FiedEquip(fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(2, base), rate));
                        }
                    }
                }
            }
            for (int i = 0; i < original.size(); ++i) {
                final FiedEquip fiedEquip = original.get(i);
                if (fiedEquip.getType().equals(6672)) {
                    endure = fiedEquip.getValue();
                    break;
                }
            }
            field.add(new FiedEquip(6679, BigDecimalUtil.decimalScaleIntegerDown(0, score) + acsry.getBaseScore()));
            field.add(new FiedEquip(6672, endure));
            return field;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Integer calEquipScore(final BagEquip equip) {
        double score = 0.0;
        final int gam = DataGlobalFunc.getGemNum(equip);
        final List<Integer> gemList = ZB_PRO_BASE_DICT.toList();
        final Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
        final List<Integer> redoList = EQUIP_PRO_DICT.toList();
        for (final FiedEquip fiedEquip : equip.getField()) {
            if (redoList.contains(fiedEquip.getType())) {
                final int dist = DataGlobalFunc.GetProDictType(fiedEquip.getType());
                final double value = Math.abs(fiedEquip.getValue());
                double odds = map.get(dist);
                if (dist == PRO_DICT_TYPE.SPEED.getDist() && value <= 0.0) {
                    odds = 12.1;
                }
                if (gam > 0 && gemList.contains(dist)) {
                    score += (value + value * (0.03 * gam)) * odds;
                }
                else {
                    score += value * odds;
                }
            }
        }
        final List<Integer> artificeList = EQUIP_PRO_LH_DICT.toList();
        for (final FiedEquip fiedEquip2 : equip.getArtifice()) {
            if (artificeList.contains(fiedEquip2.getType())) {
                final int dist2 = DataGlobalFunc.GetProDictType(fiedEquip2.getType());
                final double value2 = fiedEquip2.getValue();
                final double odds2 = map.get(dist2);
                score += value2 * odds2;
            }
        }
        return BigDecimalUtil.decimalScaleIntegerDown(0, score);
    }
    
    public static PetEquip createPetBaseBieEq(final int grade, byte productOrder, final List<Integer> integers, final int bd) {
        try {
            final Map<Integer, SysPetAcsry> petAcsryMap = EquipDataPool.petAcsryMap;
            final List<SysPetAcsry> petAcsrieList = petAcsryMap.values().stream().filter(e -> e.getGrade() == grade).collect(Collectors.toList());
            final SysPetAcsry petAcsry = petAcsrieList.get(RandomUtil.round(0, petAcsrieList.size() - 1));
            int subjoinScore = 0;
            if (ACC_GRADE.PRICELESS.getValue() == productOrder) {
                subjoinScore = 600;
            }
            else {
                final int round = RandomUtil.round(0, 100);
                if (round <= 2) {
                    subjoinScore = 600;
                    productOrder = ACC_GRADE.PRICELESS.getValue();
                }
                else if (round <= 18) {
                    subjoinScore = 200;
                    productOrder = ACC_GRADE.RESERVE.getValue();
                }
                else {
                    productOrder = ACC_GRADE.PLAY_WITH.getValue();
                }
            }
            final Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
            final Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
            final Map<String, SysPetAcsryAttrLib> petAcsryAttrLibMap = EquipDataPool.petAcsryAttrLibMap;
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            int score = 0;
            for (final String dict : petAcsry.getAttrLib()) {
                final SysPetAcsryAttrLib attrLib = petAcsryAttrLibMap.get(dict);
                if (attrLib == null) {
                    MakeAttribute.logger.error("宠物配饰生成属性错误，配饰编号 = {}，属性字典 = {}", petAcsry.getSerial(), dict);
                }
                else {
                    final List<Prop> propList = attrLib.getPropByRarity().get(productOrder);
                    final List<Lottery> list = new ArrayList<Lottery>();
                    for (int i = 0; i < propList.size(); ++i) {
                        final Prop prop = propList.get(i);
                        list.add(new Lottery(i, "", prop.getProp()));
                    }
                    final List<Double> rates = new ArrayList<Double>(list.size());
                    for (final Lottery lottery : list) {
                        rates.add(lottery.getBili());
                    }
                    final Lottery lottery2 = list.get(LotteryUtils.lottery(rates));
                    final Prop prop2 = propList.get(lottery2.getSerial());
                    final int rate = BigDecimalUtil.decimalScaleIntegerDown(0, createRndRange(new double[] { prop2.getOnset(), prop2.getOver() }, integers));
                    final FieldValue fieldValue = attrLib.getBaseAttr().get(RandomUtil.round(0, attrLib.getBaseAttr().size() - 1));
                    final double base = rate / 10000.0 * fieldValue.getV();
                    final int dist = DataGlobalFunc.GetProDictType(fieldValue.getK());
                    final Double odds = map.get(dist);
                    if (odds != null) {
                        final double value = base;
                        score += (int)(value * odds);
                    }
                    final Integer genre = proMap.get(fieldValue.getK() - PRO_DICT_SERIAL.PET_PS.getSerial());
                    if (genre.equals(PRO_TYPE.Pro_Value_NUM_TYPE.getValue())) {
                        field.add(new FiedEquip(fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(0, base), rate));
                    }
                    else {
                        field.add(new FiedEquip(fieldValue.getK(), BigDecimalUtil.decimalScaleIntegerDown(2, base), rate));
                    }
                }
            }
            if (ACC_GRADE.PRICELESS.getValue() == productOrder) {
                final int dict2 = petAcsry.getBaseAttr().get(RandomUtil.round(0, petAcsry.getBaseAttr().size() - 1));
                final SysPetAcsryAdvAttrLib advAttrLib = EquipDataPool.petAcsryAdvAttrLibMap.get(dict2);
                if (advAttrLib != null) {
                    if (advAttrLib.getType() == 3) {
                        final List<Prop> propList2 = advAttrLib.getPropList();
                        final List<Lottery> list2 = new ArrayList<Lottery>();
                        for (int j = 0; j < propList2.size(); ++j) {
                            final Prop prop3 = propList2.get(j);
                            list2.add(new Lottery(j, "", prop3.getProp()));
                        }
                        final List<Double> rates2 = new ArrayList<Double>(list2.size());
                        for (final Lottery lottery2 : list2) {
                            rates2.add(lottery2.getBili());
                        }
                        final Lottery lottery3 = list2.get(LotteryUtils.lottery(rates2));
                        final Prop prop = propList2.get(lottery3.getSerial());
                        final int rate2 = BigDecimalUtil.decimalScaleIntegerDown(0, createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers));
                        final int base2 = advAttrLib.getValueList().get(0) + (advAttrLib.getValueList().get(1) - advAttrLib.getValueList().get(0)) * rate2 / 10000;
                        field.add(new FiedEquip(advAttrLib.getAttrSerial(), BigDecimalUtil.decimalScaleIntegerDown(2, base2), rate2));
                        score += advAttrLib.getScore() * base2 / advAttrLib.getValueList().get(1);
                    }
                    else if (advAttrLib.getType() == 2) {
                        final int base3 = advAttrLib.getValueList().get(0);
                        field.add(new FiedEquip(advAttrLib.getAttrSerial(), BigDecimalUtil.decimalScaleIntegerDown(2, base3), 1.0));
                        score += advAttrLib.getScore() * base3 / advAttrLib.getValueList().get(1);
                    }
                    else {
                        field.add(new FiedEquip(advAttrLib.getAttrSerial(), 1, 1.0));
                        score += advAttrLib.getScore() * advAttrLib.getValueList().get(0) / advAttrLib.getValueList().get(1);
                    }
                }
                else {
                    MakeAttribute.logger.error("宠物配饰生成加强属性错误，字典：{}", (Object)dict2);
                }
            }
            field.add(new FiedEquip(6679, BigDecimalUtil.decimalScaleIntegerDown(0, score + subjoinScore)));
            return new PetEquip(petAcsry.getName(), bd, petAcsry.getSerial(), petAcsry.getPos(), productOrder, field);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<FiedEquip> calEquipScore(final PetEquip petEquip) {
        try {
            int subjoinScore = 0;
            if (ACC_GRADE.PRICELESS.getValue() == petEquip.getProductOrder()) {
                subjoinScore = 600;
            }
            final Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
            final List<FiedEquip> field = new ArrayList<FiedEquip>();
            int score = 0;
            for (final FiedEquip fiedEquip : petEquip.getField()) {
                final double base = fiedEquip.getValue();
                final SysPetAcsryAdvAttrLib advAttrLib = EquipDataPool.petAcsryAdvAttrLibMap.get(fiedEquip.getType());
                if (advAttrLib != null) {
                    score += (int)(advAttrLib.getScore() * base / advAttrLib.getValueList().get(1));
                }
                else {
                    final int dist = DataGlobalFunc.GetProDictType(fiedEquip.getType());
                    final Double odds = map.get(dist);
                    if (odds != null) {
                        final double value = base;
                        score += (int)(value * odds);
                    }
                }
                field.add(fiedEquip);
            }
            field.add(new FiedEquip(6679, BigDecimalUtil.decimalScaleIntegerDown(0, score + subjoinScore)));
            return field;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(final String[] args) {
        final BagEquip equip = new BagEquip();
        final int score = 1300;
        equip.setType(5419104);
        equip.setPosition(1);
        final List<FiedEquip> field = new ArrayList<FiedEquip>();
        field.add(new FiedEquip(6670, 18));
        field.add(new FiedEquip(EQUIP_PRO_DICT.HIT_RATIO.getPro(), 130));
        field.add(new FiedEquip(EQUIP_PRO_DICT.RAGE_RATIO.getPro(), 130));
        field.add(new FiedEquip(EQUIP_PRO_DICT.CONQUER_EXTENT.getPro(), 300));
        field.add(new FiedEquip(EQUIP_PRO_DICT.CONQUER_RATIO.getPro(), 300));
        field.add(new FiedEquip(EQUIP_PRO_DICT.ATTACK.getPro(), 14000));
        equip.setField(field);
        final List<FiedEquip> artifice = new ArrayList<FiedEquip>();
        artifice.add(new FiedEquip(EQUIP_PRO_LH_DICT.OMIT_K_L.getPro(), 38));
        artifice.add(new FiedEquip(EQUIP_PRO_LH_DICT.RISE_PJ.getPro(), 37));
        artifice.add(new FiedEquip(EQUIP_PRO_LH_DICT.Q_KM.getPro(), 170));
        artifice.add(new FiedEquip(EQUIP_PRO_LH_DICT.Q_KM.getPro(), 147));
        artifice.add(new FiedEquip(EQUIP_PRO_LH_DICT.Q_KM.getPro(), 123));
        equip.setArtifice(artifice);
        final Integer integer = calEquipScore(equip);
        System.out.println("装备评分为 = " + (integer + score));
    }
    
    static {
        MakeAttribute.logger = LoggerFactory.getLogger(MakeAttribute.class);
    }
}
