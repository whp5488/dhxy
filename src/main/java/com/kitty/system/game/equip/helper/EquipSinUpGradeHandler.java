
package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.utils.Const;
import java.util.List;
import java.util.Optional;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.vo.FiedEquip;
import java.util.Map;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.logs.LoggerUtils;
import java.text.MessageFormat;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipSinUpGrade")
public class EquipSinUpGradeHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "请添加足够的仙器";
    public static final String SUCCESS = "升级成功，你得到了一件{0}级{1}";
    public static final String NOT_STUFF = "请放入同等品阶的仙器";
    public static final String NOT_LEVEL = "等级升到{0}级才能升级该装备";
    public static final int SERIAL = 75031;
    public static final int DEDUCT_NUM = 30;
    public static final int[] SILVER = new int[] { 10000000, 20000000, 30000000, 100000000, 1000000000 };
    
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final String[] split = param.split("-");
        if (split.length < 3) {
            return "请放入同等品阶的仙器";
        }
        final Long id = Long.valueOf(split[0]);
        final Long aux_id = Long.valueOf(split[1]);
        final Long aux_id_two = Long.valueOf(split[2]);
        final EquipInfo equipInfo = this.getEquip(role, 1, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("角色编号：{}，仙器升阶：获取物品所在位置为 = {}，获取物品ID = {}", new Object[] { role.getRoleId(), pos, id });
            return equipInfo.getTips();
        }
        final BagEquip equip = equipInfo.getEquip();
        final Map<Integer, SysSinBieEq> sinBieEq = EquipDataPool.sinBieEq;
        final SysSinBieEq sysSinBieEq = sinBieEq.get(equip.getType());
        if (sysSinBieEq == null) {
            return "该装备不可以升级";
        }
        final int needGrade = sysSinBieEq.getNeedGrade();
        final int needRei = sysSinBieEq.getNeedRei();
        final String msg = needRei + "转";
        if (role.getRelive() < needRei) {
            return MessageFormat.format("等级升到{0}级才能升级该装备", ((needRei > 0) ? msg : "") + needGrade);
        }
        if (role.getLevel() < needGrade) {
            return MessageFormat.format("等级升到{0}级才能升级该装备", ((needRei > 0) ? msg : "") + needGrade);
        }
        if (sysSinBieEq.getNextSerial() <= 0) {
            return equip.getName() + "已达最大等级";
        }
        final int pj = this.getPj(equip.getType());
        final boolean deduct = this.deduct(role, pj, aux_id, aux_id_two);
        if (deduct) {
            this.succeed(role, sinBieEq.get(sysSinBieEq.getNextSerial()), equip);
        }
        else {
            LoggerUtils.info("玩家[{}]升阶仙器错误：参数 = {}，位置 = {}" + role.getRoleId(), param, pos);
        }
        return null;
    }
    
    public void succeed(final Role role, final SysSinBieEq sinBieEq, final BagEquip equip) {
        int bn = 0;
        final Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
        if (first.isPresent()) {
            final FiedEquip fiedEquip = first.get();
            bn = fiedEquip.getValue();
        }
        BagEquip newEq = new BagEquip();
        BeanUtils.copyBeanProp(newEq, equip);
        final List<FiedEquip> field = MakeAttribute.createBaseBieEq(sinBieEq.getBaseAttr(), sinBieEq.getMaxEndure(), sinBieEq.getSerial(), bn, 0, 0.0);
        newEq = this.clearHistory(newEq);
        newEq.setField(field);
        newEq.setType(sinBieEq.getSerial());
        newEq.setName(sinBieEq.getName());
        newEq.setField(field);
        final Integer score = MakeAttribute.calEquipScore(newEq);
        field.add(new FiedEquip(6679, score + sinBieEq.getBaseScore()));
        newEq.setField(field);
        this.refresh(role, newEq, equip.getPos(), equip.getId(), true);
        MessagePusher.pushMessage(role, new RespHint(2, (short)1));
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级成功，你得到了一件{0}级{1}", this.getPj(sinBieEq.getSerial()), sinBieEq.getName()), false));
        this.animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue());
        final int pj = this.getPj(newEq.getType());
        if (pj > 2) {
            final String msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", newEq.getType(), equip.getId(), role.getRoleId(), newEq.getType(), newEq.getType(), pj + "阶仙器");
            MessagePusher.notify2Lb(role, 800, role.getName(), "升阶", msg);
        }
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)1, "升阶仙器获得装备 - " + JsonUtils.object2String(newEq), role.getMarking());
    }
    
    public boolean deduct(final Role role, final int grade, final long aux_id, final long aux_id_two) {
        final int silver = EquipSinUpGradeHandler.SILVER[grade - 1];
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, silver)) {
            return false;
        }
        if (grade == 4) {
            final Integer goods = this.bagService.consumeBagGoods(role, 75031, 30, true, true, "4阶升5阶仙器");
            if (goods != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("升级材料数量不足"));
                return false;
            }
            return true;
        } else if (grade == 5) {
            final Integer goods = this.bagService.consumeBagGoods(role, 76001, 30, true, true, "5阶升6阶仙器");
            if (goods != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("升级材料数量不足"));
                return false;
            }
            return true;
        }
        else {
            final BagEquip auxEquip = this.getEquip(role, 1, aux_id).getEquip();
            if (auxEquip == null) {
                return false;
            }
            if (aux_id_two > 0L) {
                final BagEquip auxEquipTwo = this.getEquip(role, 1, aux_id_two).getEquip();
                if (auxEquip == null || auxEquipTwo == null) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("请添加足够的仙器"));
                    return false;
                }
                if (!this.check(grade, this.getPj(auxEquip.getType()), this.getPj(auxEquipTwo.getType()))) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("请放入同等品阶的仙器"));
                    return false;
                }
                final boolean b = this.bagService.deleteRoleEquip(role, auxEquipTwo, "升阶扣除材料装备");
                if (!b) {
                    this.logger.error("删除材料二时，玩家编号 = {}，玩家名称：{}，升阶扣除道具失败，可能使用非法操作", (Object)role.getRoleId(), (Object)role.getName());
                    return false;
                }
            }
            else if (aux_id > 0L && !this.check(grade, this.getPj(auxEquip.getType()), 0)) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("请放入同等品阶的仙器"));
                return false;
            }
            boolean b2 = this.bagService.deleteRoleEquip(role, auxEquip, "升阶扣除材料装备-");
            if (!b2) {
                this.logger.error("删除材料一， 时玩家编号 = {}，玩家名称：{}，升阶扣除道具失败，可能使用非法操作", (Object)role.getRoleId(), (Object)role.getName());
                return false;
            }
            b2 = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipSinUpGrade");
            if (!b2) {
                this.logger.error("扣除银币时，玩家编号 = {}，玩家名称：{}，升阶扣除道具失败，可能使用非法操作", (Object)role.getRoleId(), (Object)role.getName());
                return false;
            }
            return true;
        }
    }
    
    public boolean check(final int grade, final int auxPj, final int auxPjTwo) {
        if (auxPjTwo > 0) {
            if (auxPj != grade || auxPjTwo != grade) {
                return false;
            }
        }
        else if (auxPj != grade) {
            return false;
        }
        return true;
    }
}
