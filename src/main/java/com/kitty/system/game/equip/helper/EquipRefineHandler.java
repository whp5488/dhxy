package com.kitty.system.game.equip.helper;

import java.util.LinkedHashMap;
import com.kitty.common.utils.RateRandomNumber;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.game.vo.Field;
import java.util.Random;
import java.util.Arrays;
import com.kitty.system.init.domain.SysRefine;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.bag.domain.BagEquip;
import java.text.MessageFormat;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipRefine")
public class EquipRefineHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "{0}数量不足";
    public static final int[] SERIAL;
    public static final double[] REBATE;
    public static final String[] NAME;
    static final int DIS = 10;
    
    public EquipRefineHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        int index = type / 1000;
        int genre = type % 1000;
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("装备炼化：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        int bieEqType = equip.getType() / 100000;
        Map<Integer, Integer> consume = this.consume(bieEqType, genre, EquipRefineHandler.SERIAL[index - 1]);
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, amount)) {
                    return null;
                }
            }
            else {
                Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return MessageFormat.format("{0}数量不足", EquipRefineHandler.NAME[index - 1]);
                }
            }
        }
        boolean deduct = this.deduct(role, consume);
        if (!deduct) {
            return "道具或者货币扣除失败";
        }
        int factor = factorData(bieEqType);
        if (factor <= 0) {
            return "该装备不能炼化哦";
        }
        BagEquip calculate = new BagEquip();
        BeanUtils.copyBeanProp(calculate, equip);
        EquipAlike alike = DataGlobalFunc.getEquipData(equip.getType());
        Map<Integer, Integer> map = PRO_DICT_TYPE.toMapAll();
        int position = equip.getPosition();
        List<SysRefine> list = EquipDataPool.refineList;
        List<SysRefine> collect = list.stream().filter(eq -> eq.getPosition() == position).collect(Collectors.toList());
        int count = DataGlobalFunc.expend(genre);
        List<List<FiedEquip>> refine = new ArrayList<>();
        List<EquipNeo> neos = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            List<FiedEquip> fieldEquips = this.create(map, collect, factor, index);
            calculate.setArtifice(fieldEquips);
            Integer score = MakeAttribute.calEquipScore(calculate);
            fieldEquips.add(new FiedEquip(6679, score + alike.getBaseScore()));
            neos.add(new EquipNeo(fieldEquips));
            refine.add(fieldEquips);
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)1, param, neos, (byte)genre));
        equip = this.refineSave(equip, refine, genre);
        this.refresh(role, equip, pos, id, false);
        return null;
    }
    
    public List<FiedEquip> create(Map<Integer, Integer> map, List<SysRefine> collect, int factor, int goodsType) {
        List<Integer> integers = Arrays.asList(85, 14, 1);
        if (goodsType == 2) {
            integers = Arrays.asList(70, 28, 2);
        }
        else if (goodsType == 3) {
            integers = Arrays.asList(60, 32, 8);
        }
        List<FiedEquip> list = new ArrayList<>();
        int row = getRow();
        ArrayList<SysRefine> objects = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < row; ++i) {
            int pos = i + 1;
            List<SysRefine> values = collect.stream().filter(eq -> eq.getPos().contains(pos)).collect(Collectors.toList());
            SysRefine value = values.get(random.nextInt(values.size()));
            objects.add(value);
        }
        for (int i = 0; i < objects.size(); ++i) {
            int pos = i + 1;
            SysRefine refineValue = objects.get(i);
            Field field = refineValue.getFields().stream().filter(eq -> eq.getPos() == pos).findFirst().orElse(null);
            if (field != null) {
                double odds = 100.0;
                Integer dict = CONVERT_DICT.getDist(refineValue.getDict());
                Integer type = map.get(dict);
                if (type == null) {
                    this.logger.error("装备炼化异常字典为{}", dict);
                }
                int min = BigDecimalUtil.decimalScaleIntegerDown(0, (field.getMin() * factor) / odds);
                int max = BigDecimalUtil.decimalScaleIntegerDown(0, (field.getMax() * factor) / odds);
                try {
                    double range = min;
                    if (min != max) {
                        range = MakeAttribute.createRndRange(new double[] { min, max }, integers);
                    }
                    int value2 = BigDecimalUtil.decimalScaleIntegerDown(0, range);
                    list.add(new FiedEquip(CONVERT_DICT.getDist(refineValue.getDict()) + PRO_DICT_SERIAL.EQUIP_PRO_LH.getSerial(), value2));
                }
                catch (Exception e) {
                    this.logger.error("装备炼化属性值范围数据失败字典为：{},最小值：{},最大值：{}", dict, min, max);
                }
            }
            else {
                this.logger.error("装备炼化异常获取数据失败{}", refineValue);
            }
        }
        return list;
    }
    
    public static int factorData(int type) {
        int factor = 0;
        if (type == 30) {
            factor = 55;
        }
        else if (type == 41) {
            factor = 50;
        }
        else if (type == 42) {
            factor = 50;
        }
        else if (type == 43) {
            factor = 65;
        }
        else if (type == 44) {
            factor = 90;
        }
        else if (type == 45) {
            factor = 100;
        }
        else if (type == 46) {
            factor = 110;
        }
        else if (type == 51) {
            factor = 65;
        }
        else if (type == 52) {
            factor = 80;
        }
        else if (type == 53) {
            factor = 90;
        }
        else if (type == 54) {
            factor = 100;
        }
        else if (type == 55) {
            factor = 110;
        }
        else if (type == 56) {
            factor = 150;
        }
        return factor;
    }
    
    public static int getRow() {
        double rndRange = createRndRange(new double[] { 1.0, 3.0, 4.0, 5.0 });
        return BigDecimalUtil.decimalScaleInteger(0, rndRange);
    }
    
    public static double createRndRange(double[] rndRange) {
        List<Double> separates = new ArrayList<>();
        separates.add(rndRange[0] + 0.5);
        separates.add(rndRange[1] - 0.5);
        separates.add(rndRange[2] - 0.6);
        separates.add(rndRange[3] - 1.0);
        List<Integer> percents = new ArrayList<>();
        percents.add(3);
        percents.add(20);
        percents.add(40);
        percents.add(30);
        percents.add(7);
        return BigDecimalUtil.decimalScaleObjectDown(2, RateRandomNumber.produceRateRandomNumber(rndRange[0], rndRange[3], separates, percents));
    }
    
    public Map<Integer, Integer> consume(int grade, int type, int serial) {
        int num = DataGlobalFunc.expend(type);
        double rebate = EquipRefineHandler.REBATE[type - 1];
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (grade == 30) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 3 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (30000 * num) * rebate / 10.0));
        }
        else if (grade == 41) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 3 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (30000 * num) * rebate / 10.0));
        }
        else if (grade == 42) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 3 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (60000 * num) * rebate / 10.0));
        }
        else if (grade == 43) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 5 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (100000 * num) * rebate / 10.0));
        }
        else if (grade == 44) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 8 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (250000 * num) * rebate / 10.0));
        }
        else if (grade == 45) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 10 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (400000 * num) * rebate / 10.0));
        }
        else if (grade == 46) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 13 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (800000 * num) * rebate / 10.0));
        }
        else if (grade == 51) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 3 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (100000 * num) * rebate / 10.0));
        }
        else if (grade == 52) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 6 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (250000 * num) * rebate / 10.0));
        }
        else if (grade == 53) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 9 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (350000 * num) * rebate / 10.0));
        }
        else if (grade == 54) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 12 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (450000 * num) * rebate / 10.0));
        }
        else if (grade == 55) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 15 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (900000 * num) * rebate / 10.0));
        }
        else if (grade == 56) {
            map.put(serial, BigDecimalUtil.decimalScaleIntegerDown(0, 30 * num * rebate));
            map.put(Const.RESTYPE_SILVER, BigDecimalUtil.decimalScaleIntegerDown(0, (1800000 * num) * rebate / 10.0));
        }
        return map;
    }

    static {
        SERIAL = new int[] { 75027, 75028, 75029, 75107, 75108, 75109 };
        REBATE = new double[] { 1.0, 0.95, 0.9, 0.7, 0.6, 0.4 };
        NAME = new String[] { "低级九彩云龙珠", "中级九彩云龙珠", "高级九彩云龙珠", "低级炼化符", "中级炼化符", "高级炼化符" };
    }
}
