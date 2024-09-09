package com.kitty.system.game.pet.handler;

import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("ExpHandler")
public class ExpHandler extends FeedPetHandler
{
    private static final String SUCCESS = "消耗了{0}x1，你的#<G>{1}#获得了#<IR12>##<G>{2}#";
    private static final String ERROR = "背包没有{0}";
    private static final String TIPS = "#<MP:0,M:1,MT:%d,CI:%d}>[%s]#";
    private static final int GOODS_SERIAL_L = 75021;
    private static final int GOODS_SERIAL_S = 75022;
    private static final int GOODS_SERIAL_D = 75023;
    
    public ExpHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        int roleGrade = (int)role.getRelive() * 1000 + (int)role.getLevel();
        int petGrade = (int)pet.getRebirth() * 1000 + (int)pet.getPetLv();
        SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)pet.getRebirth() + 1));
        if (sysRbattr != null) {
            Integer lvLimit = sysRbattr.getLvLimit();
            if (petGrade - roleGrade >= 10) {
                return String.format("%s超过你的%d级%d级", pet.getPetName(), role.getLevel(), Integer.valueOf(10));
            }
            if ((int)pet.getPetLv() >= (int)lvLimit && pet.getExp() >= 1500000000L) {
                return String.format("%s召唤兽已到达%d转的上限%d级", pet.getPetName(), pet.getRebirth(), lvLimit);
            }
            int serial = 0;
            if ((int)param == 1) {
                serial = 75021;
            }
            else if ((int)param == 2) {
                serial = 75022;
            }
            else if ((int)param == 3) {
                serial = 75023;
            }
            SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(Integer.valueOf(serial));
            if (serial > 0 && item != null) {
                Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, serial, 1, true, true, "宠物经验丹");
                if (goods != null) {
                    return MessageFormat.format("背包没有{0}", item.getName());
                }
                Integer exp = item.getValue();
                boolean flag = this.petService.addExp(role, pet, (long)Long.valueOf((long)(int)exp), false);
                if (flag) {
                    String format = String.format("#<MP:0,M:1,MT:%d,CI:%d}>[%s]#", item.getSerial(), item.getSerial(), item.getName());
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("消耗了{0}x1，你的#<G>{1}#获得了#<IR12>##<G>{2}#", format, pet.getPetName(), exp), true));
                }
                return null;
            }
            else {
                return "未找到该道具";
            }
        }
        else {
            return "经验值已上限";
        }
    }
}
