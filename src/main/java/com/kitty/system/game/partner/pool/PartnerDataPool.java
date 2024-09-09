package com.kitty.system.game.partner.pool;

import java.util.Iterator;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.vo.Approximate;
import java.util.List;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.domain.SysPartner;
import java.util.Map;

public class PartnerDataPool
{
    public static Map<Integer, SysPartner> partnerMap;
    public static Map<Integer, Map<Short, SysPartnerProperty>> partnerPropertyMap;
    public static Map<Integer, List<Approximate>> partnerPropertyLevelMap;
    
    public PartnerDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        PartnerDataPool.partnerMap = initService.initPartner();
        PartnerDataPool.partnerPropertyMap = initService.initPartnerProperty();
        Map<Integer, List<Approximate>> objMap = new HashMap();
        for (Integer key : PartnerDataPool.partnerPropertyMap.keySet()) {
            List<Approximate> list = new ArrayList<>();
            Map<Short, SysPartnerProperty> map = (Map)PartnerDataPool.partnerPropertyMap.get(key);
            for (Short level : map.keySet()) {
                list.add(new Approximate((int)key, (int)(short)level));
            }
            objMap.put(key, list);
        }
        PartnerDataPool.partnerPropertyLevelMap = objMap;
    }
    
    static {
        PartnerDataPool.partnerMap = new HashMap();
        PartnerDataPool.partnerPropertyMap = new HashMap();
        PartnerDataPool.partnerPropertyLevelMap = new HashMap();
    }
}
