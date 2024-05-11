
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        PartnerDataPool.partnerMap = initService.initPartner();
        PartnerDataPool.partnerPropertyMap = initService.initPartnerProperty();
        final Map<Integer, List<Approximate>> objMap = new HashMap<Integer, List<Approximate>>();
        for (final Integer key : PartnerDataPool.partnerPropertyMap.keySet()) {
            final List<Approximate> list = new ArrayList<Approximate>();
            final Map<Short, SysPartnerProperty> map = PartnerDataPool.partnerPropertyMap.get(key);
            for (final Short level : map.keySet()) {
                list.add(new Approximate(key, level));
            }
            objMap.put(key, list);
        }
        PartnerDataPool.partnerPropertyLevelMap = objMap;
    }
    
    static {
        PartnerDataPool.partnerMap = new HashMap<Integer, SysPartner>();
        PartnerDataPool.partnerPropertyMap = new HashMap<Integer, Map<Short, SysPartnerProperty>>();
        PartnerDataPool.partnerPropertyLevelMap = new HashMap<Integer, List<Approximate>>();
    }
}
