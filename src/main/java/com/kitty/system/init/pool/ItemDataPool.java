package com.kitty.system.init.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysTaskItem;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDataPool {
  public static Map<Integer, SysDrug> data_drug = new HashMap<>();
  
  public static Map<Integer, SysTaskItem> data_task_item = new HashMap<>();
  
  public static Map<Integer, SysDataStuff> data_stuff = new HashMap<>();
  
  public static Map<Integer, SysGiftItem> data_gift_item = new HashMap<>();
  
  public static List<SysGiftItemOpen> data_gift_item_open = new ArrayList<>();
  
  public static Map<Integer, SysOtherItem> data_other_item = new HashMap<>();
  
  public static Map<Integer, SysDataAcsryEgg> acsryEggMap = new HashMap<>();
  
  public static Map<Integer, SysEffect> effectMap = new HashMap<>();
  
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
    data_stuff = initService.initDataStuff();
  }
  
  public static void initTaskItem(InitService initService) {
    data_task_item = initService.initTaskItem();
  }
  
  public static void initGiftItem(InitService initService) {
    data_gift_item = initService.initGiftItem();
  }
  
  public static void initGiftItemOpen(InitService initService) {
    data_gift_item_open = initService.initGiftItemOpen();
  }
  
  public static void initOtherItem(InitService initService) {
    data_other_item = initService.initOtherItem();
  }
  
  public static void initAcsryEgg(InitService initService) {
    acsryEggMap = initService.initAcsryEgg();
  }
  
  public static void initEffect(InitService initService) {
    effectMap = initService.initEffect();
  }
  
  public static void initDrug(InitService initService) {
    data_drug = initService.initDrug();
  }
}
