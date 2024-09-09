package com.kitty.system.game.equip.make;

import org.slf4j.LoggerFactory;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.enums.war.itempro.def.PET_EQUIP_PRO_DICT_SCORE;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.init.domain.SysPetAcsry;
import java.util.stream.Collectors;
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
    
    public MakeAttribute() {
    }
    
    public static List<FiedEquip> createBaseBieEq(String baseAttr, int maxEndure, int serial, int bn, int coef, double refining) {
        try {
            List<FiedEquip> field = new ArrayList<>();
            Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
            for (String key : baseMap.keySet()) {
                int dist = CONVERT_DICT.getDist(key);
                if (dist > 0) {
                    int value = (int)baseMap.get(key);
                    if (refining > 0.0 && coef > 0) {
                        value += (int)((double)value * ((double)coef / 1000.0 * (refining / 100.0)));
                    }
                    field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(value)));
                }
                else {
                    MakeAttribute.logger.error("属性字典：%s,未找到解析", Integer.valueOf(dist));
                }
            }
            field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
            field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
            return null;
        }
    }
    
    public static List<FiedEquip> createBaseBieEq(double refining, int coef, String baseAttr, int maxEndure, int serial, int bn, boolean flag) {
        try {
            List<FiedEquip> field = new ArrayList<>();
            Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
            for (String key : baseMap.keySet()) {
                int dist = CONVERT_DICT.getDist(key);
                if (dist > 0) {
                    double value = (double)(int)baseMap.get(key) * ((double)coef / 1000.0 * (refining / 100.0));
                    field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf((int)baseMap.get(key) + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(value)))));
                }
                else {
                    MakeAttribute.logger.error("属性字典：%s,未找到解析", Integer.valueOf(dist));
                }
            }
            field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
            field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
            if (flag) {
                SysPurifyAttr purify = getPurify();
                int dist2 = CONVERT_DICT.getDist(purify.getDict());
                if (dist2 > 0) {
                    field.add(new FiedEquip(Integer.valueOf(dist2 + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { (double)purify.getLowerBound(), (double)purify.getUpperBound() }, 1, 2))))));
                }
                else {
                    MakeAttribute.logger.error("隐属性字典：{},未找到解析", Integer.valueOf(dist2));
                }
                field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
            }
            field.add(new FiedEquip(Integer.valueOf(6681), Integer.valueOf((int)refining * 1000)));
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成五阶仙器装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
            return null;
        }
    }
    
    public static SysPurifyAttr getPurify() {
        Random random = new Random();
        List<SysPurifyAttr> list = EquipDataPool.purify;
        int index = random.nextInt(list.size());
        return (SysPurifyAttr)list.get(index);
    }
    
    public static List<FiedEquip> createBaseBieEq(String baseAttr, int maxEndure, int serial, double[] rndRange, int bn) {
        if (rndRange.length != 2) {
            return null;
        }
        try {
            List<FiedEquip> field = new ArrayList<>();
            Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
            for (String key : baseMap.keySet()) {
                int dist = CONVERT_DICT.getDist(key);
                if (dist > 0) {
                    field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(rndRange) * (double)(int)baseMap.get(key) / 100.0)))));
                }
                else {
                    MakeAttribute.logger.error("属性字典：%s,未找到解析", Integer.valueOf(dist));
                }
            }
            field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
            field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成神兵装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
            return null;
        }
    }
    
    public static double createRndRange(double[] rndRange, List<Integer> percents) {
        List<Double> separates = new ArrayList<>();
        double rnd = 5.0;
        if (rndRange[0] == 5.0) {
            rnd = 1.0;
        }
        separates.add(Double.valueOf(rndRange[0] + rnd));
        separates.add(Double.valueOf(rndRange[1] - 1.0));
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
    }
    
    public static double createRndRange(double[] rndRange) {
        List<Double> separates = new ArrayList<>();
        double rnd = 5.0;
        if (rndRange[0] == 5.0) {
            rnd = 1.0;
        }
        separates.add(Double.valueOf(rndRange[0] + rnd));
        separates.add(Double.valueOf(rndRange[1] - 1.0));
        List<Integer> percents = new ArrayList<>();
        percents.add(Integer.valueOf(85));
        percents.add(Integer.valueOf(13));
        percents.add(Integer.valueOf(2));
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
    }
    
    public static double createRndRange(double[] rndRange, int lower, int upper) {
        List<Double> separates = new ArrayList<>();
        separates.add(Double.valueOf(rndRange[0] + (double)lower));
        separates.add(Double.valueOf(rndRange[1] - (double)upper));
        List<Integer> percents = new ArrayList<>();
        percents.add(Integer.valueOf(85));
        percents.add(Integer.valueOf(13));
        percents.add(Integer.valueOf(2));
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
    }
    
    public static double createRndRangeBase(double[] rndRange) {
        List<Double> separates = new ArrayList<>();
        if (rndRange[0] < 0.0) {
            separates.add(Double.valueOf(rndRange[0] + Math.abs(rndRange[0] * 0.2)));
        }
        else {
            separates.add(Double.valueOf(rndRange[0] + 5.0));
        }
        if (rndRange[1] < 0.0) {
            separates.add(Double.valueOf(rndRange[1] - Math.abs(rndRange[1] * 0.1)));
        }
        else {
            separates.add(Double.valueOf(rndRange[1] - 1.0));
        }
        List<Integer> percents = new ArrayList<>();
        if (rndRange[0] < 0.0) {
            percents.add(Integer.valueOf(1));
            percents.add(Integer.valueOf(7));
            percents.add(Integer.valueOf(92));
        }
        else {
            percents.add(Integer.valueOf(92));
            percents.add(Integer.valueOf(7));
            percents.add(Integer.valueOf(1));
        }
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
    }
    
    public static List<FiedEquip> createHighBaseBieEq(String baseAttr, int maxEndure, int serial, double factor, int bn) {
        Map<String, SysAboveAttrLib> aboveMap = EquipDataPool.above_attr_lib;
        try {
            List<FiedEquip> field = new ArrayList<>();
            String[] split;
            for (String dist : split = baseAttr.split(",")) {
                SysAboveAttrLib lib = (SysAboveAttrLib)aboveMap.get(dist);
                if (lib != null) {
                    String base = lib.getBaseAttr();
                    if (base != null && !base.isEmpty()) {
                        String[] bases = base.split(";");
                        if (bases.length > 0) {
                            Random random = new Random();
                            String curBase = bases[random.nextInt(bases.length)];
                            String[] curBaseArr = curBase.split(",");
                            if (curBaseArr.length == 3) {
                                String curKey = curBaseArr[0];
                                int curMinValue = (int)Integer.valueOf(curBaseArr[1]);
                                int curMaxValue = (int)Integer.valueOf(curBaseArr[2]);
                                int range = createHighRndRange(lib.getRndRange());
                                int curValue = curMinValue + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(range * (curMaxValue - curMinValue) / 100));
                                int pro = CONVERT_DICT.getDist(curKey);
                                if (pro > 0) {
                                    field.add(new FiedEquip(Integer.valueOf(pro + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)curValue * factor)))));
                                }
                                else {
                                    MakeAttribute.logger.error("创建高级装备基础数据属性字典：%s,未找到解析", Integer.valueOf(pro));
                                }
                            }
                        }
                    }
                }
            }
            field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
            field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            MakeAttribute.logger.error("编号：%d,生成装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
            return null;
        }
    }
    
    public static int createHighRndRange(String rndRange) {
        if (rndRange.isEmpty() || rndRange == null) {
            return 1;
        }
        double rndValue = createRndRange(new double[] { 1.0, 100.0 });
        String[] split = rndRange.split(";");
        if (split.length <= 0) {
            return 1;
        }
        List<String[]> list = new ArrayList<>();
        int length = split.length;
        int j = 0;
        while (j < length) {
            String str = split[j];
            String[] strings = str.split(",");
            if (strings.length != 3) {
                return 1;
            }
            list.add(strings);
            ++j;
        }
        int minRndValue = 1;
        int maxRndValue = 100;
        int startValue = 0;
        int i = 0;
        while (i < list.size()) {
            String[] strings = (String[])list.get(i);
            int v = (int)Integer.valueOf(strings[2]);
            if (rndValue >= (double)startValue && rndValue <= (double)(startValue + v)) {
                minRndValue = (int)Integer.valueOf(strings[0]);
                maxRndValue = (int)Integer.valueOf(strings[1]);
                break;
            }
            startValue += v;
            ++i;
        }
        return minRndValue + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(Math.random() * (double)(maxRndValue - minRndValue)));
    }
    
    public static BagEquip createBaseAcsry(SysDataAcsry acsry, int pos, List<Integer> integers) {
        try {
            double score = 0.0;
            Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
            List<FiedEquip> field = new ArrayList<>();
            Map<String, SysDataAcsryAttr> acsryAttrMap = EquipDataPool.acsryAttrMap;
            Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
            String[] split;
            for (String dict : split = acsry.getBaseAttr().split(",")) {
                SysDataAcsryAttr acsryAttr = (SysDataAcsryAttr)acsryAttrMap.get(dict);
                if (acsryAttr != null) {
                    List<FieldValue> baseAttrList = acsryAttr.getBaseAttrList();
                    int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(baseAttrList.size() - 1));
                    FieldValue fieldValue = (FieldValue)baseAttrList.get(index);
                    List<Lottery> list = (List)EquipDataPool.acsryAttrLotteryMap.get(dict);
                    List<Double> rates = new ArrayList(list.size());
                    Iterator<Lottery> iterator = list.iterator();
                    Lottery lottery;
                    while (iterator.hasNext()) {
                        lottery = (Lottery)iterator.next();
                        rates.add(lottery.getBili());
                    }
                    Lottery lottery2 = (Lottery)list.get(LotteryUtils.lottery(rates));
                    Prop prop = (Prop)acsryAttr.getProp().stream().filter(e -> lottery2.getSerial().equals(Integer.valueOf(e.getSerial()))).findFirst().orElse(null);
                    if (prop == null) {
                        MakeAttribute.logger.error("配饰数据错误");
                    }
                    else {
                        double rate = createRndRange(new double[] { (double)prop.getOnset(), (double)prop.getOver() }, integers);
                        double base = rate / 10000.0 * (double)fieldValue.getV();
                        int dist = (int)DataGlobalFunc.GetProDictType(fieldValue.getK());
                        Double odds = (Double)map.get(Integer.valueOf(dist));
                        if (odds != null) {
                            double value = base;
                            score += value * (double)odds;
                        }
                        Integer genre = (Integer)proMap.get(Integer.valueOf(fieldValue.getK() - PRO_DICT_SERIAL.PS.getSerial()));
                        if (genre.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
                            field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(base))), rate));
                        }
                        else {
                            field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Double.valueOf(base))), rate));
                        }
                    }
                }
            }
            field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(acsry.getMaxEndure())));
            field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score)) + acsry.getBaseScore())));
            BagEquip equip = new BagEquip();
            equip.setPos(pos);
            equip.setName(acsry.getName());
            equip.setPosition(acsry.getPos());
            equip.setType(acsry.getSerial());
            equip.setField(field);
            return equip;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<FiedEquip> createBaseAcsry(SysDataAcsry acsry, List<FiedEquip> original, List<Integer> integers) {
        try {
            int endure = acsry.getMaxEndure();
            double score = 0.0;
            Map<String, SysDataAcsryAttr> acsryAttrMap = EquipDataPool.acsryAttrMap;
            Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
            List<FiedEquip> field = new ArrayList<>();
            Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
            String[] split;
            for (String dict : split = acsry.getBaseAttr().split(",")) {
                SysDataAcsryAttr acsryAttr = (SysDataAcsryAttr)acsryAttrMap.get(dict);
                if (acsryAttr != null) {
                    List<FieldValue> baseAttrList = acsryAttr.getBaseAttrList();
                    int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(baseAttrList.size() - 1));
                    FieldValue fieldValue = (FieldValue)baseAttrList.get(index);
                    List<Lottery> list = (List)EquipDataPool.acsryAttrLotteryMap.get(dict);
                    List<Double> rates = new ArrayList(list.size());
                    Iterator<Lottery> iterator = list.iterator();
                    while (iterator.hasNext()) {
                    	Lottery lottery = (Lottery)iterator.next();
                        rates.add(lottery.getBili());
                    }
                    Lottery lottery2 = (Lottery)list.get(LotteryUtils.lottery(rates));
                    Prop prop = (Prop)acsryAttr.getProp().stream().filter(e -> lottery2.getSerial().equals(Integer.valueOf(e.getSerial()))).findFirst().orElse(null);
                    if (prop == null) {
                        MakeAttribute.logger.error("配饰数据错误");
                    }
                    else {
                        int rate = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { (double)prop.getOnset(), (double)prop.getOver() }, integers)));
                        double base = (double)rate / 10000.0 * (double)fieldValue.getV();
                        int dist = (int)DataGlobalFunc.GetProDictType(fieldValue.getK());
                        Double odds = (Double)map.get(Integer.valueOf(dist));
                        if (odds != null) {
                            double value = base;
                            score += value * (double)odds;
                        }
                        Integer genre = (Integer)proMap.get(Integer.valueOf(fieldValue.getK() - PRO_DICT_SERIAL.PS.getSerial()));
                        if (genre.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
                            field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(base))), (double)rate));
                        }
                        else {
                            field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Double.valueOf(base))), (double)rate));
                        }
                    }
                }
            }
            int i = 0;
            while (i < original.size()) {
                FiedEquip fiedEquip = (FiedEquip)original.get(i);
                if (fiedEquip.getType().equals(Integer.valueOf(6672))) {
                    endure = (int)fiedEquip.getValue();
                    break;
                }
                ++i;
            }
            field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score)) + acsry.getBaseScore())));
            field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(endure)));
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Integer calEquipScore(BagEquip equip) {
        double score = 0.0;
        int gam = DataGlobalFunc.getGemNum(equip);
        List<Integer> gemList = ZB_PRO_BASE_DICT.toList();
        Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
        List<Integer> redoList = EQUIP_PRO_DICT.toList();
        for (FiedEquip fiedEquip : equip.getField()) {
            if (redoList.contains(fiedEquip.getType())) {
                int dist = (int)DataGlobalFunc.GetProDictType((int)fiedEquip.getType());
                double value = (double)Math.abs((int)fiedEquip.getValue());
                double odds = (double)map.get(Integer.valueOf(dist));
                if (dist == PRO_DICT_TYPE.SPEED.getDist() && value <= 0.0) {
                    odds = 12.1;
                }
                if (gam > 0 && gemList.contains(Integer.valueOf(dist))) {
                    score += (value + value * (0.03 * (double)gam)) * odds;
                }
                else {
                    score += value * odds;
                }
            }
            else {
                continue;
            }
        }
        List<Integer> artificeList = EQUIP_PRO_LH_DICT.toList();
        for (FiedEquip fiedEquip2 : equip.getArtifice()) {
            if (artificeList.contains(fiedEquip2.getType())) {
                int dist2 = (int)DataGlobalFunc.GetProDictType((int)fiedEquip2.getType());
                double value2 = (double)(int)fiedEquip2.getValue();
                double odds2 = (double)map.get(Integer.valueOf(dist2));
                score += value2 * odds2;
            }
            else {
                continue;
            }
        }
        return Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score)));
    }
    
    public static PetEquip createPetBaseBieEq(int grade, byte productOrder, List<Integer> integers, int bd) {
        try {
            Map<Integer, SysPetAcsry> petAcsryMap = EquipDataPool.petAcsryMap;
            List<SysPetAcsry> petAcsrieList = (List)petAcsryMap.values().stream().filter(e -> e.getGrade() == grade).collect(Collectors.toList());
            SysPetAcsry petAcsry = (SysPetAcsry)petAcsrieList.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(petAcsrieList.size() - 1)));
            int subjoinScore = 0;
            if (ACC_GRADE.PRICELESS.getValue() == productOrder) {
                subjoinScore = 600;
            }
            else {
                int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(100));
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
            Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
            Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
            Map<String, SysPetAcsryAttrLib> petAcsryAttrLibMap = EquipDataPool.petAcsryAttrLibMap;
            List<FiedEquip> field = new ArrayList<>();
            int score = 0;
            for (String dict : petAcsry.getAttrLib()) {
                SysPetAcsryAttrLib attrLib = (SysPetAcsryAttrLib)petAcsryAttrLibMap.get(dict);
                if (attrLib == null) {
                    MakeAttribute.logger.error("宠物配饰生成属性错误，配饰编号 = {}，属性字典 = {}", Integer.valueOf(petAcsry.getSerial()), dict);
                }
                else {
                    List<Prop> propList = (List)attrLib.getPropByRarity().get(Byte.valueOf(productOrder));
                    List<Lottery> list = new ArrayList<>();
                    for (int i = 0; i < propList.size(); ++i) {
                        Prop prop = (Prop)propList.get(i);
                        list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf(prop.getProp())));
                    }
                    List<Double> rates = new ArrayList(list.size());
                    for (Lottery lottery : list) {
                        rates.add(lottery.getBili());
                    }
                    Lottery lottery2 = (Lottery)list.get(LotteryUtils.lottery(rates));
                    Prop prop2 = (Prop)propList.get((int)lottery2.getSerial());
                    int rate = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { (double)prop2.getOnset(), (double)prop2.getOver() }, integers)));
                    FieldValue fieldValue = (FieldValue)attrLib.getBaseAttr().get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(attrLib.getBaseAttr().size() - 1)));
                    double base = (double)rate / 10000.0 * (double)fieldValue.getV();
                    int dist = (int)DataGlobalFunc.GetProDictType(fieldValue.getK());
                    Double odds = (Double)map.get(Integer.valueOf(dist));
                    if (odds != null) {
                        double value = base;
                        score = (int)((double)score + value * (double)odds);
                    }
                    Integer genre = (Integer)proMap.get(Integer.valueOf(fieldValue.getK() - PRO_DICT_SERIAL.PET_PS.getSerial()));
                    if (genre.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
                        field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(base))), (double)rate));
                    }
                    else {
                        field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Double.valueOf(base))), (double)rate));
                    }
                }
            }
            if (ACC_GRADE.PRICELESS.getValue() == productOrder) {
                int dict2 = (int)petAcsry.getBaseAttr().get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(petAcsry.getBaseAttr().size() - 1)));
                SysPetAcsryAdvAttrLib advAttrLib = (SysPetAcsryAdvAttrLib)EquipDataPool.petAcsryAdvAttrLibMap.get(Integer.valueOf(dict2));
                if (advAttrLib != null) {
                    if (advAttrLib.getType() == 3) {
                        List<Prop> propList2 = advAttrLib.getPropList();
                        List<Lottery> list2 = new ArrayList<>();
                        for (int j = 0; j < propList2.size(); ++j) {
                            Prop prop3 = (Prop)propList2.get(j);
                            list2.add(new Lottery(Integer.valueOf(j), "", Double.valueOf(prop3.getProp())));
                        }
                        List<Double> rates2 = new ArrayList(list2.size());
                        for (Lottery lottery2 : list2) {
                            rates2.add(lottery2.getBili());
                        }
                        Lottery lottery3 = (Lottery)list2.get(LotteryUtils.lottery(rates2));
                        Prop prop = (Prop)propList2.get((int)lottery3.getSerial());
                        int rate2 = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { (double)prop.getOnset(), (double)prop.getOver() }, integers)));
                        int base3 = (int)advAttrLib.getValueList().get(0) + ((int)advAttrLib.getValueList().get(1) - (int)advAttrLib.getValueList().get(0)) * rate2 / 10000;
                        field.add(new FiedEquip(Integer.valueOf(advAttrLib.getAttrSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Integer.valueOf(base3))), (double)rate2));
                        score += advAttrLib.getScore() * base3 / (int)advAttrLib.getValueList().get(1);
                    }
                    else if (advAttrLib.getType() == 2) {
                        int base2 = (int)advAttrLib.getValueList().get(0);
                        field.add(new FiedEquip(Integer.valueOf(advAttrLib.getAttrSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Integer.valueOf(base2))), 1.0));
                        score += advAttrLib.getScore() * base2 / (int)advAttrLib.getValueList().get(1);
                    }
                    else {
                        field.add(new FiedEquip(Integer.valueOf(advAttrLib.getAttrSerial()), Integer.valueOf(1), 1.0));
                        score += advAttrLib.getScore() * (int)advAttrLib.getValueList().get(0) / (int)advAttrLib.getValueList().get(1);
                    }
                }
                else {
                    MakeAttribute.logger.error("宠物配饰生成加强属性错误，字典：{}", Integer.valueOf(dict2));
                }
            }
            field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(score + subjoinScore)))));
            return new PetEquip(petAcsry.getName(), bd, petAcsry.getSerial(), petAcsry.getPos(), productOrder, field);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<FiedEquip> calEquipScore(PetEquip petEquip) {
        try {
            int subjoinScore = 0;
            if (ACC_GRADE.PRICELESS.getValue() == petEquip.getProductOrder()) {
                subjoinScore = 600;
            }
            Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
            List<FiedEquip> field = new ArrayList<>();
            int score = 0;
            for (FiedEquip fiedEquip : petEquip.getField()) {
                double base = (double)(int)fiedEquip.getValue();
                SysPetAcsryAdvAttrLib advAttrLib = (SysPetAcsryAdvAttrLib)EquipDataPool.petAcsryAdvAttrLibMap.get(fiedEquip.getType());
                if (advAttrLib != null) {
                    score = (int)((double)score + (double)advAttrLib.getScore() * base / (double)(int)advAttrLib.getValueList().get(1));
                }
                else {
                    int dist = (int)DataGlobalFunc.GetProDictType((int)fiedEquip.getType());
                    Double odds = (Double)map.get(Integer.valueOf(dist));
                    if (odds != null) {
                        double value = base;
                        score = (int)((double)score + value * (double)odds);
                    }
                }
                field.add(fiedEquip);
            }
            field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(score + subjoinScore)))));
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        BagEquip equip = new BagEquip();
        int score = 1300;
        equip.setType(5419104);
        equip.setPosition(1);
        List<FiedEquip> field = new ArrayList<>();
        field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(18)));
        field.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(130)));
        field.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(130)));
        field.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(300)));
        field.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(300)));
        field.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_DICT.ATTACK.getPro()), Integer.valueOf(14000)));
        equip.setField(field);
        List<FiedEquip> artifice = new ArrayList<>();
        artifice.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_LH_DICT.OMIT_K_L.getPro()), Integer.valueOf(38)));
        artifice.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_LH_DICT.RISE_PJ.getPro()), Integer.valueOf(37)));
        artifice.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_LH_DICT.Q_KM.getPro()), Integer.valueOf(170)));
        artifice.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_LH_DICT.Q_KM.getPro()), Integer.valueOf(147)));
        artifice.add(new FiedEquip(Integer.valueOf(EQUIP_PRO_LH_DICT.Q_KM.getPro()), Integer.valueOf(123)));
        equip.setArtifice(artifice);
        Integer integer = calEquipScore(equip);
        System.out.println("装备评分为 = " + ((int)integer + score));
    }
    
    static {
        MakeAttribute.logger = LoggerFactory.getLogger(MakeAttribute.class);
    }
}
