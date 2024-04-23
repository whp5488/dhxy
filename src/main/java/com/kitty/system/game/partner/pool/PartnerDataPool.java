package com.kitty.system.game.partner.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.vo.Approximate;
import com.kitty.system.init.domain.SysPartner;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartnerDataPool {
  public static Map<Integer, SysPartner> partnerMap = new HashMap<>();
  
  public static Map<Integer, Map<Short, SysPartnerProperty>> partnerPropertyMap = new HashMap<>();
  
  public static Map<Integer, List<Approximate>> partnerPropertyLevelMap = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    partnerMap = initService.initPartner();
    partnerPropertyMap = initService.initPartnerProperty();
    Map<Integer, List<Approximate>> objMap = new HashMap<>();
    for (Integer key : partnerPropertyMap.keySet()) {
      List<Approximate> list = new ArrayList<>();
      Map<Short, SysPartnerProperty> map = partnerPropertyMap.get(key);
      for (Short level : map.keySet())
        list.add(new Approximate(key.intValue(), level.shortValue())); 
      objMap.put(key, list);
    } 
    partnerPropertyLevelMap = objMap;
  }
}
