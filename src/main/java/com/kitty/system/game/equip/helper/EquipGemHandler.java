package com.kitty.system.game.equip.helper;

import java.util.Arrays;
import com.kitty.system.game.vo.ItemEasyInfo;
import java.util.Iterator;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import java.util.Map;
import java.util.Optional;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.common.utils.text.Convert;
import java.text.MessageFormat;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.LinkedHashMap;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("EquipGem")
public class EquipGemHandler extends UpgradeHandler
{
    private static final String LACK = "缺少%sX%d";
    public static final String NOT_FIND_STUFF = "材料数量不足";
    public static final String SUCCESS_INLAY = "恭喜，镶嵌成功";
    public static final String FIND_GEM_MAX = "宝石镶嵌已达最大数量";
    public static final String FIND_GEM_MIN = "请先镶嵌一颗宝石吧";
    public static final List<Integer> gem_tips;
    
    public EquipGemHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        try {
            Long id = Long.valueOf(param);
            EquipInfo equipInfo = this.getEquip(role, pos, id);
            if (equipInfo.getTips() != null) {
                return equipInfo.getTips();
            }
            BagEquip equip = equipInfo.getEquip();
            int bn = 0;
            Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
            if (first.isPresent()) {
            	FiedEquip fiedEquip = first.get();
                bn = fiedEquip.getValue();
            }
            Map<Integer, Integer> map = new LinkedHashMap<>();
            EquipAlike equipData = DataGlobalFunc.getEquipData(equip.getType());
            if (type == 0) {
                if (equipData != null) {
                    if (bn >= equipData.getMaxEmbedGemCnt()) {
                        return "宝石镶嵌已达最大数量";
                    }
                    Integer serial2 = DataGlobalFunc.getEquipInlayGemSerial(equip.getType(), equip.getPosition(), bn);
                    Integer goods = this.bagService.getBagGoodsNum(role, serial2);
                    boolean flag = false;
                    if (goods >= 1) {
                        flag = true;
                        map.put(serial2, 1);
                    }
                    else {
                        int temp = serial2 % 10000;
                        int level2 = temp % 100;
                        if (level2 > 1) {
                            int subType = temp / 100;
                            int needNum = DataGlobalFunc.gem2lowGemNum(serial2);
                            int typeId = 70000 + subType * 100;
                            int count = 0;
                            for (int j = 1; j < level2; ++j) {
                                int gemSerial = typeId + (level2 - j);
                                Integer gemNum = this.bagService.getBagGoodsNum(role, gemSerial);
                                if (gemNum > 0) {
                                    int num2 = DataGlobalFunc.gem2lowGemNum(gemSerial) * gemNum;
                                    count += num2;
                                    if (num2 > needNum) {
                                        flag = true;
                                        int needed = needNum;
                                        if (level2 - j > 1) {
                                            needed = needNum / DataGlobalFunc.gem2lowGemNum(gemSerial);
                                        }
                                        else {
                                            needed = needNum;
                                        }
                                        map.put(gemSerial, needed);
                                        break;
                                    }
                                    else {
                                        map.put(gemSerial, gemNum);
                                        if (count >= needNum) {
                                            flag = true;
                                            break;
                                        }
                                        needNum -= count;
                                    }
                                }
                            }
                        }
                    }
                    if (flag) {
                        for (Integer key2 : map.keySet()) {
                            Integer bagGoods = this.bagService.consumeBagGoods(role, key2, map.get(key2), false, true, "镶嵌宝石");
                            if (bagGoods != null) {
                                ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(key2);
                                if (easyInfo != null) {
                                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("缺少%sX%d", easyInfo.getName(), bagGoods), true));
                                }
                                else {
                                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("扣除宝石缺少数量：{0}", bagGoods), true));
                                }
                                return null;
                            }
                        }
                        int[] num3 = { 0 };
                        equip.getField().forEach(fiedEquip -> {
                            if (fiedEquip.getType().equals(6670)) {
                                fiedEquip.setValue(fiedEquip.getValue() + 1);
                                num3[0] = fiedEquip.getValue();
                            }
                        });
                        equip = EquipUtils.gemChange(equipData, equip);
                        this.refresh(role, equip, pos, id, true);
                        MessagePusher.pushMessage(role, new RespHint(0, (short)1));
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "恭喜，镶嵌成功", false));
                        if (EquipGemHandler.gem_tips.contains(num3[0])) {
                            MessagePusher.notify2Lb(role, 600, role.getName(), Convert.getChineseFromArabic(num3[0]), String.format("#<MP:0,M:1,MT:%d,CI:%d}>%s#", equip.getType(), equip.getType(), equip.getName()));
                        }
                    }
                    else {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "材料数量不足", false));
                    }
                }
            }
            else {
                if (bn < 1) {
                    return "请先镶嵌一颗宝石吧";
                }
                int level = 1;
                for (int i = 1; i <= bn; ++i) {
                    int num = 1;
                    int serial = 70000 + equip.getPosition() * 100 + level;
                    Integer value = map.get(serial);
                    if (value != null) {
                        num += value;
                    }
                    map.put(serial, num);
                    int cost = i % 3;
                    if (cost == 0) {
                        level = ((++level > 7) ? 7 : level);
                    }
                }
                equip.getField().forEach(fiedEquip -> {
                    if (fiedEquip.getType().equals(6670)) {
                        fiedEquip.setValue(0);
                    }
                });
                for (Integer key : map.keySet()) {
                    this.bagService.addGoods(role, key, map.get(key), 0, true, false, "拆卸宝石");
                }
                equip = EquipUtils.gemChange(equipData, equip);
                this.refresh(role, equip, pos, id, true);
                MessagePusher.pushMessage(role, new RespHint(1, (short)1));
            }
            return null;
        }
        catch (Exception e) {
            this.logger.error("镶嵌宝石出错了：");
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        gem_tips = Arrays.asList(16, 20);
    }
}
