
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
    @Override
    public String acceptFeed(final Partner partner, final Role role, final Integer param, final String type, final Object... args) {
        final SysDataStuff item = ItemDataPool.data_stuff.get(param);
        if (item == null) {
            return null;
        }
        final int roleGrade = role.getRelive() * 1000 + role.getLevel();
        final int partnerGrade = partner.getRebirth() * 1000 + partner.getPartnerLv();
        if (partnerGrade > roleGrade) {
            return "伙伴等级不能超过人物等级";
        }
        if (roleGrade == partnerGrade && partner.getExp() >= 1500000000L) {
            return null;
        }
        if (partnerGrade >= 3250 && partner.getExp() >= 1500000000L) {
            return String.format("%s伙伴已到达%d转的上限%d级", partner.getPartnerName(), partner.getRebirth(), partner.getPartnerLv());
        }
        final Integer goods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, item.getSerial(), 1, true, true, "使用伙伴修炼手册");
        if (goods != null) {
            return null;
        }
        final boolean flag = this.partnerService.addExp(role, partner, item.getValue(), true);
        if (!flag) {
            return null;
        }
        return null;
    }
}
