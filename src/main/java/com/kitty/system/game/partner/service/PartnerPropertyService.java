package com.kitty.system.game.partner.service;

import com.kitty.system.init.domain.SysPartnerProperty;
import java.util.List;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerPropertyService {
  @Autowired
  private Dao dao;
  
  public SysPartnerProperty getPartnerPropertyBySerialLevel(int partnerSerial, int partnerLevel) {
    List<SysPartnerProperty> sysPartnerPropertys = this.dao.query(SysPartnerProperty.class, 
        (Condition)Cnd.where("partner_serial", "=", Integer.valueOf(partnerSerial)).and("level", "=", Integer.valueOf(partnerLevel)));
    if (sysPartnerPropertys.size() > 0)
      return sysPartnerPropertys.get(0); 
    return null;
  }
}
