
package com.kitty.system.init.pool;

import java.util.ArrayList;
import java.util.HashMap;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysGiftItemOpen;
import java.util.List;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysTaskItem;
import com.kitty.system.init.domain.SysDrug;
import java.util.Map;

public class ItemDataPool
{
    public static Map<Integer, SysDrug> data_drug;
    public static Map<Integer, SysTaskItem> data_task_item;
    public static Map<Integer, SysDataStuff> data_stuff;
    public static Map<Integer, SysGiftItem> data_gift_item;
    public static List<SysGiftItemOpen> data_gift_item_open;
    public static Map<Integer, SysOtherItem> data_other_item;
    public static Map<Integer, SysDataAcsryEgg> acsryEggMap;
    public static Map<Integer, SysEffect> effectMap;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        initDrug(initService);
        initDataStuff(initService);
        initTaskItem(initService);
        initGiftItem(initService);
        initGiftItemOpen(initService);
        initOtherItem(initService);
        initAcsryEgg(initService);
        initEffect(initService);
    }
    
    public static void initDataStuff(final InitService initService) {
        ItemDataPool.data_stuff = initService.initDataStuff();
    }
    
    public static void initTaskItem(final InitService initService) {
        ItemDataPool.data_task_item = initService.initTaskItem();
    }
    
    public static void initGiftItem(final InitService initService) {
        ItemDataPool.data_gift_item = initService.initGiftItem();
    }
    
    public static void initGiftItemOpen(final InitService initService) {
        ItemDataPool.data_gift_item_open = initService.initGiftItemOpen();
    }
    
    public static void initOtherItem(final InitService initService) {
        ItemDataPool.data_other_item = initService.initOtherItem();
    }
    
    public static void initAcsryEgg(final InitService initService) {
        ItemDataPool.acsryEggMap = initService.initAcsryEgg();
    }
    
    public static void initEffect(final InitService initService) {
        ItemDataPool.effectMap = initService.initEffect();
    }
    
    public static void initDrug(final InitService initService) {
        ItemDataPool.data_drug = initService.initDrug();
    }
    
    static {
        ItemDataPool.data_drug = new HashMap<Integer, SysDrug>();
        ItemDataPool.data_task_item = new HashMap<Integer, SysTaskItem>();
        ItemDataPool.data_stuff = new HashMap<Integer, SysDataStuff>();
        ItemDataPool.data_gift_item = new HashMap<Integer, SysGiftItem>();
        ItemDataPool.data_gift_item_open = new ArrayList<SysGiftItemOpen>();
        ItemDataPool.data_other_item = new HashMap<Integer, SysOtherItem>();
        ItemDataPool.acsryEggMap = new HashMap<Integer, SysDataAcsryEgg>();
        ItemDataPool.effectMap = new HashMap<Integer, SysEffect>();
    }
}
