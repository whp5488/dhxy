
package com.kitty.system.game.partner.service;

import java.util.List;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.init.domain.SysPartnerProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class PartnerPropertyService
{
    @Autowired
    private Dao dao;
    
    public SysPartnerProperty getPartnerPropertyBySerialLevel(final int partnerSerial, final int partnerLevel) {
        final List<SysPartnerProperty> sysPartnerPropertys = this.dao.query(SysPartnerProperty.class, Cnd.where("partner_serial", "=", (Object)partnerSerial).and("level", "=", (Object)partnerLevel));
        if (sysPartnerPropertys.size() > 0) {
            return sysPartnerPropertys.get(0);
        }
        return null;
    }
}
