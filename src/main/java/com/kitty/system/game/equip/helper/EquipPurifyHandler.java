package com.kitty.system.game.equip.helper;

import java.util.Iterator;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.List;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import java.text.MessageFormat;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.common.utils.bean.BeanUtils;
import java.util.ArrayList;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipPurify")
public class EquipPurifyHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "需要{0}个八荒遗风";
    public static final int SERIAL = 75031;
    public static final int SILVER = 5000000;
    public static final int ITEM_NUM = 2;
    public static final double MIN = 0.005;
    public static final double MAX = 0.05;
    
    public EquipPurifyHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("五阶仙器炼器：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        int pj = this.getPj(equip.getType());
        if (pj != 5 && pj != 6) {
            return null;
        }
        int count = DataGlobalFunc.expend(type);
        Integer goods = this.bagService.getBagGoods(role, 75031, count * 2);
        if (goods != null) {
            return MessageFormat.format("需要{0}个八荒遗风", goods);
        }
        boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 5000000L * count);
        if (!flag) {
            return null;
        }
        boolean equipPurify = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 5000000L * count, "EquipPurify");
        if (!equipPurify) {
            return null;
        }
        Integer integer = this.bagService.consumeBagGoods(role, 75031, count * 2, true, true, "仙器炼器");
        if (integer != null) {
            return null;
        }
        BagEquip calculate = new BagEquip();
        List<EquipNeo> list = new ArrayList<>();
        BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
        int bn = DataGlobalFunc.getGemNum(equip);
        SysSinBieEq bieEq = EquipDataPool.sinBieEq.get(equip.getType());
        List<List<FiedEquip>> redo = new ArrayList<>();
        double refining = equip.getRefining();
        for (int i = 0; i < count; ++i) {
            refining = this.getRefining(refining);
            double max = (0.005 * refining <= 0.0) ? 0.005 : (0.005 * refining + 1.0);
            double odds = RandomUtil.getRandomDouble(0.005, (max > 0.05) ? 0.05 : max);
            List<FiedEquip> field = MakeAttribute.createBaseBieEq(refining, bieEq.getCoef(), bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, SkillAi.canAffectOnPro(odds));
            calculate.setField(field);
            Integer score = MakeAttribute.calEquipScore(calculate);
            field.add(new FiedEquip(6679, score + bieEq.getBaseScore()));
            list.add(new EquipNeo(field));
            redo.add(field);
        }
        equip.setRefining(refining);
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)2, param, list, (byte)(int)type));
        equip = this.remoldPurifySave(equip, redo, type);
        equip = this.updateRemold(equip, bn);
        this.refresh(role, equip, pos, id, false);
        return null;
    }
    
    public double getRefining(double refining) {
        int round = RandomUtil.round(1, 100);
        if (round > 60) {
            ++refining;
            boolean flag = SkillAi.canAffectOnPro(0.005);
            if (flag) {
                double v = BigDecimalUtil.decimalScaleObjectDown(3, RandomUtil.getRandomDouble(1.5, 2.2));
                refining += v;
            }
        }
        else if (round > 15 && round < 35) {
            --refining;
        }
        refining = ((refining < 0.0) ? 0.0 : refining);
        refining = ((refining > 100.0) ? 100.0 : refining);
        return refining;
    }
    
    public BagEquip updateRemold(BagEquip equip, int bn) {
        if (equip.getRedoGeneralHistory().size() > 0) {
            int serial = this.fieldSerial(equip.getRedoGeneralHistory());
            equip.setRedoGeneralHistory(this.recastField(equip, serial, bn));
        }
        if (equip.getRedoHighHistory().size() > 0) {
            List<List<FiedEquip>> redoHighHistory = new ArrayList<>();
            for (List<FiedEquip> fiedEquips : equip.getRedoHighHistory()) {
                int serial2 = this.fieldSerial(fiedEquips);
                redoHighHistory.add(this.recastField(equip, serial2, bn));
            }
            equip.setRedoHighHistory(redoHighHistory);
        }
        if (equip.getRedoSuperHistory().size() > 0) {
            List<List<FiedEquip>> redoSuperHistory = new ArrayList<>();
            for (List<FiedEquip> fiedEquips : equip.getRedoSuperHistory()) {
                int serial2 = this.fieldSerial(fiedEquips);
                redoSuperHistory.add(this.recastField(equip, serial2, bn));
            }
            equip.setRedoSuperHistory(redoSuperHistory);
            return equip;
        }
        return equip;
    }
    
    public List<FiedEquip> recastField(BagEquip equip, int serial, int bn) {
        SysSinBieEq bieEq = EquipDataPool.sinBieEq.get(serial);
        List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, bieEq.getCoef(), equip.getRefining());
        Integer score = MakeAttribute.calEquipScore(equip);
        field.add(new FiedEquip(6679, score + bieEq.getBaseScore()));
        field.add(new FiedEquip(6680, bieEq.getSerial()));
        return field;
    }
}
