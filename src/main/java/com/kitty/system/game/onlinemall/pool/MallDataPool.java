package com.kitty.system.game.onlinemall.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysShopNpcArena;
import com.kitty.system.init.domain.SysShopNpcBrac;
import com.kitty.system.init.domain.SysShopNpcDi;
import com.kitty.system.init.domain.SysShopNpcDrug;
import com.kitty.system.init.domain.SysShopNpcEquip;
import com.kitty.system.init.domain.SysShopNpcFeats;
import com.kitty.system.init.domain.SysShopNpcGrocery;
import com.kitty.system.init.domain.SysShopNpcGuo;
import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.init.domain.SysShopNpcLong;
import com.kitty.system.init.domain.SysShopNpcNao;
import com.kitty.system.init.domain.SysShopNpcNice;
import com.kitty.system.init.domain.SysShopNpcPet;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.service.InitService;
import java.util.HashMap;
import java.util.Map;

public class MallDataPool {
  public static Map<Integer, SysShopNpcArena> shop_npc_arena = new HashMap<>();
  
  public static Map<Integer, SysShopNpcBrac> shop_npc_brac = new HashMap<>();
  
  public static Map<Integer, SysShopNpcDi> shop_npc_di = new HashMap<>();
  
  public static Map<Integer, SysShopNpcFeats> shop_npc_feats = new HashMap<>();
  
  public static Map<Integer, SysShopNpcGuo> shop_npc_guo = new HashMap<>();
  
  public static Map<Integer, SysShopNpcGuoRecycle> shop_npc_guo_recycle = new HashMap<>();
  
  public static Map<Integer, SysShopNpcLong> shop_npc_long = new HashMap<>();
  
  public static Map<Integer, SysShopNpcNao> shop_npc_nao = new HashMap<>();
  
  public static Map<Integer, SysShopNpcNice> shop_npc_nice = new HashMap<>();
  
  public static Map<Integer, SysShopNpcPet> shop_npc_pet = new HashMap<>();
  
  public static Map<Integer, SysShopNpcDrug> shop_npc_drug = new HashMap<>();
  
  public static Map<Integer, SysShopNpcEquip> shop_npc_equip = new HashMap<>();
  
  public static Map<Integer, SysShopNpcGrocery> shop_npc_grocery = new HashMap<>();
  
  public static Map<Integer, SysShopOther> data_shopother = new HashMap<>();
  
  public static Map<Integer, SysShopStrengthenMaterial> data_shopStrengthenMaterial = new HashMap<>();
  
  public static Map<Integer, SysDataLimitShop> t_data_limit_shop = new HashMap<>();
  
  public static Map<Integer, SysDataRecommendShop> data_recommend_shop = new HashMap<>();
  
  public static Map<Integer, SysBoundShop> boundShopMap = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    initShopNpcArena(initService);
    initShopNpcBrac(initService);
    initShopNpcDi(initService);
    initShopNpcFeats(initService);
    initShopNpcGuo(initService);
    initShopNpcGuoRecycle(initService);
    initShopNpcLong(initService);
    initShopNpcNao(initService);
    initShopNpcNice(initService);
    initShopNpcPet(initService);
    initShopNpcDrug(initService);
    initShopNpcEquip(initService);
    initShopNpcGrocery(initService);
    initShopOther(initService);
    initShopStrengthenMaterial(initService);
    initLimitShop(initService);
    initRecommendShop(initService);
    initBoundShop(initService);
  }
  
  public static void initShopNpcArena(InitService initService) {
    shop_npc_arena = initService.initShopNpcArena();
  }
  
  public static void initShopNpcBrac(InitService initService) {
    shop_npc_brac = initService.initShopNpcBrac();
  }
  
  public static void initShopNpcDi(InitService initService) {
    shop_npc_di = initService.initShopNpcDi();
  }
  
  public static void initShopNpcFeats(InitService initService) {
    shop_npc_feats = initService.initShopNpcFeats();
  }
  
  public static void initShopNpcGuo(InitService initService) {
    shop_npc_guo = initService.initShopNpcGuo();
  }
  
  public static void initShopNpcGuoRecycle(InitService initService) {
    shop_npc_guo_recycle = initService.initShopNpcGuoRecycle();
  }
  
  public static void initShopNpcLong(InitService initService) {
    shop_npc_long = initService.initShopNpcLong();
  }
  
  public static void initShopNpcNao(InitService initService) {
    shop_npc_nao = initService.initShopNpcNao();
  }
  
  public static void initShopNpcNice(InitService initService) {
    shop_npc_nice = initService.initShopNpcNice();
  }
  
  public static void initShopNpcPet(InitService initService) {
    shop_npc_pet = initService.initShopNpcPet();
  }
  
  public static void initShopNpcDrug(InitService initService) {
    shop_npc_drug = initService.initShopNpcDrug();
  }
  
  public static void initShopNpcEquip(InitService initService) {
    shop_npc_equip = initService.initShopNpcEquip();
  }
  
  public static void initShopNpcGrocery(InitService initService) {
    shop_npc_grocery = initService.initShopNpcGrocery();
  }
  
  public static void initShopOther(InitService initService) {
    data_shopother = initService.initShopOther();
  }
  
  public static void initShopStrengthenMaterial(InitService initService) {
    data_shopStrengthenMaterial = initService.initShopStrengthenMaterial();
  }
  
  public static void initLimitShop(InitService initService) {
    t_data_limit_shop = initService.initLimitShop();
  }
  
  public static void initRecommendShop(InitService initService) {
    data_recommend_shop = initService.initRecommendShop();
  }
  
  public static void initBoundShop(InitService initService) {
    boundShopMap = initService.initBoundShop();
  }
}
