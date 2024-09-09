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
    
    public ItemDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        initDrug(initService);
        initDataStuff(initService);
        initTaskItem(initService);
        initGiftItem(initService);
        initGiftItemOpen(initService);
        initOtherItem(initService);
        initAcsryEgg(initService);
        initEffect(initService);
    }
    
    public static void initDataStuff(InitService initService) {
        ItemDataPool.data_stuff = initService.initDataStuff();
    }
    
    public static void initTaskItem(InitService initService) {
        ItemDataPool.data_task_item = initService.initTaskItem();
    }
    
    public static void initGiftItem(InitService initService) {
        ItemDataPool.data_gift_item = initService.initGiftItem();
    }
    
    public static void initGiftItemOpen(InitService initService) {
        ItemDataPool.data_gift_item_open = initService.initGiftItemOpen();
    }
    
    public static void initOtherItem(InitService initService) {
        ItemDataPool.data_other_item = initService.initOtherItem();
    }
    
    public static void initAcsryEgg(InitService initService) {
        ItemDataPool.acsryEggMap = initService.initAcsryEgg();
    }
    
    public static void initEffect(InitService initService) {
        ItemDataPool.effectMap = initService.initEffect();
    }
    
    public static void initDrug(InitService initService) {
        ItemDataPool.data_drug = initService.initDrug();
    }
    
    static {
        ItemDataPool.data_drug = new HashMap();
        ItemDataPool.data_task_item = new HashMap();
        ItemDataPool.data_stuff = new HashMap();
        ItemDataPool.data_gift_item = new HashMap();
        ItemDataPool.data_gift_item_open = new ArrayList<>();
        ItemDataPool.data_other_item = new HashMap();
        ItemDataPool.acsryEggMap = new HashMap();
        ItemDataPool.effectMap = new HashMap();
    }
}
