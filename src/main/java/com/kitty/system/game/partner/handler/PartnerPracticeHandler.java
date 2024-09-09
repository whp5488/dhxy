package com.kitty.system.game.partner.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.partner.domain.Partner;
import org.springframework.stereotype.Component;

@Component("PartnerPracticeHandler")
public class PartnerPracticeHandler extends FeedPartnerHandler
{
    public PartnerPracticeHandler() {
    }
    
    @Override
    public String acceptFeed(Partner partner, Role role, Integer param, String type, Object... args) {
        SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(param);
        if (item == null) {
            return null;
        }
        int roleGrade = (int)role.getRelive() * 1000 + (int)role.getLevel();
        int partnerGrade = partner.getRebirth() * 1000 + partner.getPartnerLv();
        if (partnerGrade > roleGrade) {
            return "伙伴等级不能超过人物等级";
        }
        if (roleGrade == partnerGrade && partner.getExp() >= 1500000000L) {
            return null;
        }
        if (partnerGrade >= 3250 && partner.getExp() >= 1500000000L) {
            return String.format("%s伙伴已到达%d转的上限%d级", partner.getPartnerName(), Byte.valueOf(partner.getRebirth()), Short.valueOf(partner.getPartnerLv()));
        }
        Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)item.getSerial(), 1, true, true, "使用伙伴修炼手册");
        if (goods != null) {
            return null;
        }
        boolean flag = this.partnerService.addExp(role, partner, (long)(int)item.getValue(), true);
        if (!flag) {
            return null;
        }
        return null;
    }
}
