
package com.kitty.system.game.horse.handler;

import com.kitty.system.game.vo.ItemEasyInfo;
import java.text.MessageFormat;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import org.springframework.stereotype.Component;

@Component("HorseVitUpHandler")
public class HorseVitUpHandler extends FeedHorseHandler
{
    private static final int SERIAL = 75106;
    private static final String SUCCESS = "你的坐骑#<LG1>{0}#补充了#<LG1>{1}#点体力";
    
    @Override
    public String acceptFeed(final Horse horse, final Role role, final short type, final byte operation, final Object... args) {
        boolean flag = false;
        int vit = horse.getVit();
        if (vit <= 0) {
            flag = true;
        }
        if (this.bagService.getBagGoods(role, 75106, 1) != null) {
            return null;
        }
        final ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(75106);
        if (easyInfo == null) {
            return "没要找到该物品配置信息";
        }
        final Integer value = easyInfo.getValue();
        final Integer integer = this.bagService.consumeBagGoods(role, 75106, 1, true, true, "使用坐骑口粮");
        if (integer != null) {
            this.logger.error("扣除道具坐骑口粮失败，玩家编号：{}", (Object)role.getRoleId());
            return null;
        }
        role.save();
        vit += value;
        horse.setVit((vit > HorseDataPool.horseVitMax) ? HorseDataPool.horseVitMax : vit);
        horse.save();
        this.horseService.refresh(role, horse);
        if (flag) {
            this.horseService.calculateControlPet(role, horse.getId());
        }
        return MessageFormat.format("你的坐骑#<LG1>{0}#补充了#<LG1>{1}#点体力", horse.getName(), value);
    }
}
