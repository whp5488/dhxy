package com.kitty.system.game.equip.make;

import com.kitty.common.bean.Lottery;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.MapObjectUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.RateRandomNumber;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT_SCORE;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_LH_DICT;
import com.kitty.system.enums.war.itempro.def.PET_EQUIP_PRO_DICT_SCORE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryAttr;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.init.domain.vo.Prop;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MakeAttribute {
  static Logger logger = LoggerFactory.getLogger(MakeAttribute.class);
  
  public static List<FiedEquip> createBaseBieEq(String baseAttr, int maxEndure, int serial, int bn, int coef, double refining) {
    try {
      List<FiedEquip> field = new ArrayList<>();
      Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
      for (String key : baseMap.keySet()) {
        int dist = CONVERT_DICT.getDist(key);
        if (dist > 0) {
          int value = ((Integer)baseMap.get(key)).intValue();
          if (refining > 0.0D && coef > 0)
            value += (int)(value * coef / 1000.0D * refining / 100.0D); 
          field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(value)));
          continue;
        } 
        logger.error("属性字典：%s,未找到解析", Integer.valueOf(dist));
      } 
      field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
      field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
      return field;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("编号：%d,生成装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
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
          double value = ((Integer)baseMap.get(key)).intValue() * coef / 1000.0D * refining / 100.0D;
          field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(((Integer)baseMap.get(key)).intValue() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(value)))));
          continue;
        } 
        logger.error("属性字典：%s,未找到解析", Integer.valueOf(dist));
      } 
      field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
      field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
      if (flag) {
        SysPurifyAttr purify = getPurify();
        int dist = CONVERT_DICT.getDist(purify.getDict());
        if (dist > 0) {
          field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { purify.getLowerBound(), purify.getUpperBound() }, 1, 2))))));
        } else {
          logger.error("隐属性字典：{},未找到解析", Integer.valueOf(dist));
        } 
        field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
      } 
      field.add(new FiedEquip(Integer.valueOf(6681), Integer.valueOf((int)refining * 1000)));
      return field;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("编号：%d,生成五阶仙器装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
      return null;
    } 
  }
  
  public static SysPurifyAttr getPurify() {
    Random random = new Random();
    List<SysPurifyAttr> list = EquipDataPool.purify;
    int index = random.nextInt(list.size());
    return list.get(index);
  }
  
  public static List<FiedEquip> createBaseBieEq(String baseAttr, int maxEndure, int serial, double[] rndRange, int bn) {
    if (rndRange.length != 2)
      return null; 
    try {
      List<FiedEquip> field = new ArrayList<>();
      Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(baseAttr);
      for (String key : baseMap.keySet()) {
        int dist = CONVERT_DICT.getDist(key);
        if (dist > 0) {
          field.add(new FiedEquip(Integer.valueOf(dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(rndRange) * ((Integer)baseMap.get(key)).intValue() / 100.0D)))));
          continue;
        } 
        logger.error("属性字典：%s,未找到解析", Integer.valueOf(dist));
      } 
      field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
      field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
      return field;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("编号：%d,生成神兵装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
      return null;
    } 
  }
  
  public static double createRndRange(double[] rndRange, List<Integer> percents) {
    List<Double> separates = new ArrayList<>();
    double rnd = 5.0D;
    if (rndRange[0] == 5.0D)
      rnd = 1.0D; 
    separates.add(Double.valueOf(rndRange[0] + rnd));
    separates.add(Double.valueOf(rndRange[1] - 1.0D));
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
  }
  
  public static double createRndRange(double[] rndRange) {
    List<Double> separates = new ArrayList<>();
    double rnd = 5.0D;
    if (rndRange[0] == 5.0D)
      rnd = 1.0D; 
    separates.add(Double.valueOf(rndRange[0] + rnd));
    separates.add(Double.valueOf(rndRange[1] - 1.0D));
    List<Integer> percents = new ArrayList<>();
    percents.add(Integer.valueOf(85));
    percents.add(Integer.valueOf(13));
    percents.add(Integer.valueOf(2));
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
  }
  
  public static double createRndRange(double[] rndRange, int lower, int upper) {
    List<Double> separates = new ArrayList<>();
    separates.add(Double.valueOf(rndRange[0] + lower));
    separates.add(Double.valueOf(rndRange[1] - upper));
    List<Integer> percents = new ArrayList<>();
    percents.add(Integer.valueOf(85));
    percents.add(Integer.valueOf(13));
    percents.add(Integer.valueOf(2));
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[1], separates, percents)));
  }
  
  public static double createRndRangeBase(double[] rndRange) {
    List<Double> separates = new ArrayList<>();
    if (rndRange[0] < 0.0D) {
      separates.add(Double.valueOf(rndRange[0] + Math.abs(rndRange[0] * 0.2D)));
    } else {
      separates.add(Double.valueOf(rndRange[0] + 5.0D));
    } 
    if (rndRange[1] < 0.0D) {
      separates.add(Double.valueOf(rndRange[1] - Math.abs(rndRange[1] * 0.1D)));
    } else {
      separates.add(Double.valueOf(rndRange[1] - 1.0D));
    } 
    List<Integer> percents = new ArrayList<>();
    if (rndRange[0] < 0.0D) {
      percents.add(Integer.valueOf(1));
      percents.add(Integer.valueOf(7));
      percents.add(Integer.valueOf(92));
    } else {
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
      String[] split = baseAttr.split(",");
      for (String dist : split) {
        SysAboveAttrLib lib = aboveMap.get(dist);
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
                int curMinValue = Integer.valueOf(curBaseArr[1]).intValue();
                int curMaxValue = Integer.valueOf(curBaseArr[2]).intValue();
                int range = createHighRndRange(lib.getRndRange());
                int curValue = curMinValue + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(range * (curMaxValue - curMinValue) / 100));
                int pro = CONVERT_DICT.getDist(curKey);
                if (pro > 0) {
                  field.add(new FiedEquip(Integer.valueOf(pro + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(curValue * factor)))));
                } else {
                  logger.error("创建高级装备基础数据属性字典：%s,未找到解析", Integer.valueOf(pro));
                } 
              } 
            } 
          } 
        } 
      } 
      field.add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(bn)));
      field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(maxEndure)));
      return field;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("编号：%d,生成装备基础属性抛出的异常{}", Integer.valueOf(serial), e.getMessage());
      return null;
    } 
  }
  
  public static int createHighRndRange(String rndRange) {
    if (rndRange.isEmpty() || rndRange == null)
      return 1; 
    double rndValue = createRndRange(new double[] { 1.0D, 100.0D });
    String[] split = rndRange.split(";");
    if (split.length <= 0)
      return 1; 
    List<String[]> list = (List)new ArrayList<>();
    for (String str : split) {
      String[] strings = str.split(",");
      if (strings.length != 3)
        return 1; 
      list.add(strings);
    } 
    int minRndValue = 1;
    int maxRndValue = 100;
    int startValue = 0;
    for (int i = 0; i < list.size(); i++) {
      String[] strings = list.get(i);
      int v = Integer.valueOf(strings[2]).intValue();
      if (rndValue >= startValue && rndValue <= (startValue + v)) {
        minRndValue = Integer.valueOf(strings[0]).intValue();
        maxRndValue = Integer.valueOf(strings[1]).intValue();
        break;
      } 
      startValue += v;
    } 
    return minRndValue + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(Math.random() * (maxRndValue - minRndValue)));
  }
  
  public static BagEquip createBaseAcsry(SysDataAcsry acsry, int pos, List<Integer> integers) {
    try {
      double score = 0.0D;
      Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
      List<FiedEquip> field = new ArrayList<>();
      Map<String, SysDataAcsryAttr> acsryAttrMap = EquipDataPool.acsryAttrMap;
      Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
      String[] split = acsry.getBaseAttr().split(",");
      for (String dict : split) {
        SysDataAcsryAttr acsryAttr = acsryAttrMap.get(dict);
        if (acsryAttr != null) {
          List<FieldValue> baseAttrList = acsryAttr.getBaseAttrList();
          int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(baseAttrList.size() - 1));
          FieldValue fieldValue = baseAttrList.get(index);
          List<Lottery> list = (List<Lottery>)EquipDataPool.acsryAttrLotteryMap.get(dict);
          List<Double> rates = new ArrayList<>(list.size());
          for (Lottery lottery1 : list)
            rates.add(lottery1.getBili()); 
          Lottery lottery = list.get(LotteryUtils.lottery(rates));
          Prop prop = acsryAttr.getProp().stream().filter(e -> lottery.getSerial().equals(Integer.valueOf(e.getSerial()))).findFirst().orElse(null);
          if (prop == null) {
            logger.error("配饰数据错误");
          } else {
            double rate = createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers);
            double base = rate / 10000.0D * fieldValue.getV();
            int dist = DataGlobalFunc.GetProDictType(fieldValue.getK()).intValue();
            Double odds = map.get(Integer.valueOf(dist));
            if (odds != null) {
              double value = base;
              score += value * odds.doubleValue();
            } 
            Integer genre = proMap.get(Integer.valueOf(fieldValue.getK() - PRO_DICT_SERIAL.PS.getSerial()));
            if (genre.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
              field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(base))), rate));
            } else {
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
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static List<FiedEquip> createBaseAcsry(SysDataAcsry acsry, List<FiedEquip> original, List<Integer> integers) {
    try {
      int endure = acsry.getMaxEndure();
      double score = 0.0D;
      Map<String, SysDataAcsryAttr> acsryAttrMap = EquipDataPool.acsryAttrMap;
      Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
      List<FiedEquip> field = new ArrayList<>();
      Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
      String[] split = acsry.getBaseAttr().split(",");
      for (String dict : split) {
        SysDataAcsryAttr acsryAttr = acsryAttrMap.get(dict);
        if (acsryAttr != null) {
          List<FieldValue> baseAttrList = acsryAttr.getBaseAttrList();
          int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(baseAttrList.size() - 1));
          FieldValue fieldValue = baseAttrList.get(index);
          List<Lottery> list = (List<Lottery>)EquipDataPool.acsryAttrLotteryMap.get(dict);
          List<Double> rates = new ArrayList<>(list.size());
          for (Lottery lottery1 : list)
            rates.add(lottery1.getBili()); 
          Lottery lottery = list.get(LotteryUtils.lottery(rates));
          Prop prop = acsryAttr.getProp().stream().filter(e -> lottery.getSerial().equals(Integer.valueOf(e.getSerial()))).findFirst().orElse(null);
          if (prop == null) {
            logger.error("配饰数据错误");
          } else {
            int rate = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers)));
            double base = rate / 10000.0D * fieldValue.getV();
            int dist = DataGlobalFunc.GetProDictType(fieldValue.getK()).intValue();
            Double odds = map.get(Integer.valueOf(dist));
            if (odds != null) {
              double value = base;
              score += value * odds.doubleValue();
            } 
            Integer genre = proMap.get(Integer.valueOf(fieldValue.getK() - PRO_DICT_SERIAL.PS.getSerial()));
            if (genre.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
              field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(base))), rate));
            } else {
              field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Double.valueOf(base))), rate));
            } 
          } 
        } 
      } 
      for (int i = 0; i < original.size(); i++) {
        FiedEquip fiedEquip = original.get(i);
        if (fiedEquip.getType().equals(Integer.valueOf(6672))) {
          endure = fiedEquip.getValue().intValue();
          break;
        } 
      } 
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score)) + acsry.getBaseScore())));
      field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(endure)));
      return field;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static Integer calEquipScore(BagEquip equip) {
    double score = 0.0D;
    int gam = DataGlobalFunc.getGemNum(equip);
    List<Integer> gemList = ZB_PRO_BASE_DICT.toList();
    Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
    List<Integer> redoList = EQUIP_PRO_DICT.toList();
    for (FiedEquip fiedEquip : equip.getField()) {
      if (redoList.contains(fiedEquip.getType())) {
        int dist = DataGlobalFunc.GetProDictType(fiedEquip.getType().intValue()).intValue();
        double value = Math.abs(fiedEquip.getValue().intValue());
        double odds = ((Double)map.get(Integer.valueOf(dist))).doubleValue();
        if (dist == PRO_DICT_TYPE.SPEED.getDist() && value <= 0.0D)
          odds = 12.1D; 
        if (gam > 0 && gemList.contains(Integer.valueOf(dist))) {
          score += (value + value * 0.03D * gam) * odds;
          continue;
        } 
        score += value * odds;
      } 
    } 
    List<Integer> artificeList = EQUIP_PRO_LH_DICT.toList();
    for (FiedEquip fiedEquip : equip.getArtifice()) {
      if (artificeList.contains(fiedEquip.getType())) {
        int dist = DataGlobalFunc.GetProDictType(fiedEquip.getType().intValue()).intValue();
        double value = fiedEquip.getValue().intValue();
        double odds = ((Double)map.get(Integer.valueOf(dist))).doubleValue();
        score += value * odds;
      } 
    } 
    return Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score)));
  }
  
  public static PetEquip createPetBaseBieEq(int grade, byte productOrder, List<Integer> integers, int bd) {
    try {
      Map<Integer, SysPetAcsry> petAcsryMap = EquipDataPool.petAcsryMap;
      List<SysPetAcsry> petAcsrieList = (List<SysPetAcsry>)petAcsryMap.values().stream().filter(e -> (e.getGrade() == grade)).collect(Collectors.toList());
      SysPetAcsry petAcsry = petAcsrieList.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(petAcsrieList.size() - 1)));
      int subjoinScore = 0;
      if (ACC_GRADE.PRICELESS.getValue() == productOrder) {
        subjoinScore = 600;
      } else {
        int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(100));
        if (round <= 2) {
          subjoinScore = 600;
          productOrder = ACC_GRADE.PRICELESS.getValue();
        } else if (round <= 18) {
          subjoinScore = 200;
          productOrder = ACC_GRADE.RESERVE.getValue();
        } else {
          productOrder = ACC_GRADE.PLAY_WITH.getValue();
        } 
      } 
      Map<Integer, Integer> proMap = PRO_DICT_TYPE.toMapAll();
      Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
      Map<String, SysPetAcsryAttrLib> petAcsryAttrLibMap = EquipDataPool.petAcsryAttrLibMap;
      List<FiedEquip> field = new ArrayList<>();
      int score = 0;
      for (String dict : petAcsry.getAttrLib()) {
        SysPetAcsryAttrLib attrLib = petAcsryAttrLibMap.get(dict);
        if (attrLib == null) {
          logger.error("宠物配饰生成属性错误，配饰编号 = {}，属性字典 = {}", Integer.valueOf(petAcsry.getSerial()), dict);
          continue;
        } 
        List<Prop> propList = (List<Prop>)attrLib.getPropByRarity().get(Byte.valueOf(productOrder));
        List<Lottery> list = new ArrayList<>();
        for (int i = 0; i < propList.size(); i++) {
          Prop prop1 = propList.get(i);
          list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf(prop1.getProp())));
        } 
        List<Double> rates = new ArrayList<>(list.size());
        for (Lottery lottery1 : list)
          rates.add(lottery1.getBili()); 
        Lottery lottery = list.get(LotteryUtils.lottery(rates));
        Prop prop = propList.get(lottery.getSerial().intValue());
        int rate = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers)));
        FieldValue fieldValue = attrLib.getBaseAttr().get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(attrLib.getBaseAttr().size() - 1)));
        double base = rate / 10000.0D * fieldValue.getV();
        int dist = DataGlobalFunc.GetProDictType(fieldValue.getK()).intValue();
        Double odds = map.get(Integer.valueOf(dist));
        if (odds != null) {
          double value = base;
          score = (int)(score + value * odds.doubleValue());
        } 
        Integer genre = proMap.get(Integer.valueOf(fieldValue.getK() - PRO_DICT_SERIAL.PET_PS.getSerial()));
        if (genre.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
          field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(base))), rate));
          continue;
        } 
        field.add(new FiedEquip(Integer.valueOf(fieldValue.getK()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Double.valueOf(base))), rate));
      } 
      if (ACC_GRADE.PRICELESS.getValue() == productOrder) {
        int dict = ((Integer)petAcsry.getBaseAttr().get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(petAcsry.getBaseAttr().size() - 1)))).intValue();
        SysPetAcsryAdvAttrLib advAttrLib = (SysPetAcsryAdvAttrLib)EquipDataPool.petAcsryAdvAttrLibMap.get(Integer.valueOf(dict));
        if (advAttrLib != null) {
          if (advAttrLib.getType() == 3) {
            List<Prop> propList = advAttrLib.getPropList();
            List<Lottery> list = new ArrayList<>();
            for (int i = 0; i < propList.size(); i++) {
              Prop prop1 = propList.get(i);
              list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf(prop1.getProp())));
            } 
            List<Double> rates = new ArrayList<>(list.size());
            for (Lottery lottery1 : list)
              rates.add(lottery1.getBili()); 
            Lottery lottery = list.get(LotteryUtils.lottery(rates));
            Prop prop = propList.get(lottery.getSerial().intValue());
            int rate = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers)));
            int base = ((Integer)advAttrLib.getValueList().get(0)).intValue() + (((Integer)advAttrLib.getValueList().get(1)).intValue() - ((Integer)advAttrLib.getValueList().get(0)).intValue()) * rate / 10000;
            field.add(new FiedEquip(Integer.valueOf(advAttrLib.getAttrSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Integer.valueOf(base))), rate));
            score += advAttrLib.getScore() * base / ((Integer)advAttrLib.getValueList().get(1)).intValue();
          } else if (advAttrLib.getType() == 2) {
            int base = ((Integer)advAttrLib.getValueList().get(0)).intValue();
            field.add(new FiedEquip(Integer.valueOf(advAttrLib.getAttrSerial()), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(2), Integer.valueOf(base))), 1.0D));
            score += advAttrLib.getScore() * base / ((Integer)advAttrLib.getValueList().get(1)).intValue();
          } else {
            field.add(new FiedEquip(Integer.valueOf(advAttrLib.getAttrSerial()), Integer.valueOf(1), 1.0D));
            score += advAttrLib.getScore() * ((Integer)advAttrLib.getValueList().get(0)).intValue() / ((Integer)advAttrLib.getValueList().get(1)).intValue();
          } 
        } else {
          logger.error("宠物配饰生成加强属性错误，字典：{}", Integer.valueOf(dict));
        } 
      } 
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(score + subjoinScore)))));
      return new PetEquip(petAcsry.getName(), bd, petAcsry.getSerial(), petAcsry.getPos(), productOrder, field);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static List<FiedEquip> calEquipScore(PetEquip petEquip) {
    try {
      int subjoinScore = 0;
      if (ACC_GRADE.PRICELESS.getValue() == petEquip.getProductOrder())
        subjoinScore = 600; 
      Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
      List<FiedEquip> field = new ArrayList<>();
      int score = 0;
      for (FiedEquip fiedEquip : petEquip.getField()) {
        double base = fiedEquip.getValue().intValue();
        SysPetAcsryAdvAttrLib advAttrLib = (SysPetAcsryAdvAttrLib)EquipDataPool.petAcsryAdvAttrLibMap.get(fiedEquip.getType());
        if (advAttrLib != null) {
          score = (int)(score + advAttrLib.getScore() * base / ((Integer)advAttrLib.getValueList().get(1)).intValue());
        } else {
          int dist = DataGlobalFunc.GetProDictType(fiedEquip.getType().intValue()).intValue();
          Double odds = map.get(Integer.valueOf(dist));
          if (odds != null) {
            double value = base;
            score = (int)(score + value * odds.doubleValue());
          } 
        } 
        field.add(fiedEquip);
      } 
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf(score + subjoinScore)))));
      return field;
    } catch (Exception e) {
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
    System.out.println("装备评分为 = " + (integer.intValue() + score));
  }
}
