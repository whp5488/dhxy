package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RateRandomNumber;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.Field;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysRefine;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("EquipRefine")
public class EquipRefineHandler extends UpgradeHandler {
  public static final String NOT_FIND_STUFF = "{0}数量不足";
  
  public static final int[] SERIAL = new int[] { 75027, 75028, 75029 };
  
  public static final double[] REBATE = new double[] { 1.0D, 0.95D, 0.9D };
  
  public static final String[] NAME = new String[] { "低级九彩云龙珠", "中级九彩云龙珠", "高级九彩云龙珠" };
  
  static final int DIS = 10;
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    int index = type.intValue() / 1000;
    int genre = type.intValue() % 1000;
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("装备炼化：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    int bieEqType = equip.getType() / 100000;
    Map<Integer, Integer> consume = consume(bieEqType, genre, SERIAL[index - 1]);
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (key.equals(Const.RESTYPE_SILVER)) {
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, amount.intValue()))
          return null; 
        continue;
      } 
      Integer num = this.bagService.getBagGoods(role, key.intValue(), amount.intValue());
      if (num != null)
        return MessageFormat.format("{0}数量不足", new Object[] { NAME[index - 1] }); 
    } 
    boolean deduct = deduct(role, consume);
    if (!deduct)
      return "道具或者货币扣除失败"; 
    int factor = factorData(bieEqType);
    if (factor <= 0)
      return "该装备不能炼化哦"; 
    BagEquip calculate = new BagEquip();
    BeanUtils.copyBeanProp(calculate, equip);
    EquipAlike alike = DataGlobalFunc.getEquipData(Integer.valueOf(equip.getType()));
    Map<Integer, Integer> map = PRO_DICT_TYPE.toMapAll();
    int position = equip.getPosition();
    List<SysRefine> list = EquipDataPool.refineList;
    List<SysRefine> collect = (List<SysRefine>)list.stream().filter(eq -> (eq.getPosition() == position)).collect(Collectors.toList());
    int count = expend(genre);
    List<List<FiedEquip>> refine = new ArrayList<>();
    List<EquipNeo> neos = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      List<FiedEquip> fieldEquips = create(map, collect, factor, index);
      calculate.setArtifice(fieldEquips);
      Integer score = MakeAttribute.calEquipScore(calculate);
      fieldEquips.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + alike.getBaseScore())));
      neos.add(new EquipNeo(fieldEquips));
      refine.add(fieldEquips);
    } 
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)1, param, neos, (byte)genre));
    equip = refineSave(equip, refine, genre);
    refresh(role, equip, pos.intValue(), id, false);
    return null;
  }
  
  public List<FiedEquip> create(Map<Integer, Integer> map, List<SysRefine> collect, int factor, int goodsType) {
    List<Integer> integers = Arrays.asList(new Integer[] { Integer.valueOf(85), Integer.valueOf(14), Integer.valueOf(1) });
    if (goodsType == 2) {
      integers = Arrays.asList(new Integer[] { Integer.valueOf(70), Integer.valueOf(28), Integer.valueOf(2) });
    } else if (goodsType == 3) {
      integers = Arrays.asList(new Integer[] { Integer.valueOf(60), Integer.valueOf(32), Integer.valueOf(8) });
    } 
    List<FiedEquip> list = new ArrayList<>();
    int row = getRow();
    ArrayList<SysRefine> objects = new ArrayList<>();
    Random random = new Random();
    int i;
    for (i = 0; i < row; i++) {
      int pos = i + 1;
      List<SysRefine> values = (List<SysRefine>)collect.stream().filter(eq -> eq.getPos().contains(Integer.valueOf(pos))).collect(Collectors.toList());
      SysRefine value = values.get(random.nextInt(values.size()));
      objects.add(value);
    } 
    for (i = 0; i < objects.size(); i++) {
      int pos = i + 1;
      SysRefine refineValue = objects.get(i);
      Field field = refineValue.getFields().stream().filter(eq -> (eq.getPos().intValue() == pos)).findFirst().orElse(null);
      if (field != null) {
        double odds = 100.0D;
        Integer dict = Integer.valueOf(CONVERT_DICT.getDist(refineValue.getDict()));
        Integer type = map.get(dict);
        if (type == null)
          this.logger.error("装备炼化异常字典为{}", dict); 
        int min = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((field.getMin().intValue() * factor) / odds));
        int max = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((field.getMax().intValue() * factor) / odds));
        try {
          double range = min;
          if (min != max)
            range = MakeAttribute.createRndRange(new double[] { min, max }, integers); 
          int value = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(range));
          list.add(new FiedEquip(Integer.valueOf(CONVERT_DICT.getDist(refineValue.getDict()) + PRO_DICT_SERIAL.EQUIP_PRO_LH.getSerial()), Integer.valueOf(value)));
        } catch (Exception e) {
          this.logger.error("装备炼化属性值范围数据失败字典为：{},最小值：{},最大值：{}", new Object[] { dict, Integer.valueOf(min), Integer.valueOf(max) });
        } 
      } else {
        this.logger.error("装备炼化异常获取数据失败{}", refineValue);
      } 
    } 
    return list;
  }
  
  public static int factorData(int type) {
    int factor = 0;
    if (type == 30) {
      factor = 55;
    } else if (type == 41) {
      factor = 50;
    } else if (type == 42) {
      factor = 50;
    } else if (type == 43) {
      factor = 65;
    } else if (type == 44) {
      factor = 90;
    } else if (type == 45) {
      factor = 100;
    } else if (type == 46) {
      factor = 110;
    } else if (type == 51) {
      factor = 65;
    } else if (type == 52) {
      factor = 80;
    } else if (type == 53) {
      factor = 90;
    } else if (type == 54) {
      factor = 100;
    } else if (type == 55) {
      factor = 110;
    } 
    return factor;
  }
  
  public static int getRow() {
    double rndRange = createRndRange(new double[] { 1.0D, 3.0D, 4.0D, 5.0D });
    return BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(rndRange));
  }
  
  public static double createRndRange(double[] rndRange) {
    List<Double> separates = new ArrayList<>();
    separates.add(Double.valueOf(rndRange[0] + 0.5D));
    separates.add(Double.valueOf(rndRange[1] - 0.5D));
    separates.add(Double.valueOf(rndRange[2] - 0.6D));
    separates.add(Double.valueOf(rndRange[3] - 1.0D));
    List<Integer> percents = new ArrayList<>();
    percents.add(Integer.valueOf(3));
    percents.add(Integer.valueOf(20));
    percents.add(Integer.valueOf(40));
    percents.add(Integer.valueOf(30));
    percents.add(Integer.valueOf(7));
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[3], separates, percents)));
  }
  
  public Map<Integer, Integer> consume(int grade, int type, int serial) {
    int num = expend(type);
    double rebate = REBATE[type - 1];
    Map<Integer, Integer> map = new LinkedHashMap<>();
    if (grade == 30) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((3 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((30000 * num) * rebate / 10.0D))));
    } else if (grade == 41) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((3 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((30000 * num) * rebate / 10.0D))));
    } else if (grade == 42) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((3 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((60000 * num) * rebate / 10.0D))));
    } else if (grade == 43) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((5 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((100000 * num) * rebate / 10.0D))));
    } else if (grade == 44) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((8 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((250000 * num) * rebate / 10.0D))));
    } else if (grade == 45) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((10 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((400000 * num) * rebate / 10.0D))));
    } else if (grade == 46) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((13 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((800000 * num) * rebate / 10.0D))));
    } else if (grade == 51) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((3 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((100000 * num) * rebate / 10.0D))));
    } else if (grade == 52) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((6 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((250000 * num) * rebate / 10.0D))));
    } else if (grade == 53) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((9 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((350000 * num) * rebate / 10.0D))));
    } else if (grade == 54) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((12 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((450000 * num) * rebate / 10.0D))));
    } else if (grade == 55) {
      map.put(Integer.valueOf(serial), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((15 * num) * rebate))));
      map.put(Const.RESTYPE_SILVER, Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((900000 * num) * rebate / 10.0D))));
    } 
    return map;
  }
}
