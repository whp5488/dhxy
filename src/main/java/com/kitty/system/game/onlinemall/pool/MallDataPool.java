
package com.kitty.system.game.onlinemall.pool;

import java.util.HashMap;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopNpcGrocery;
import com.kitty.system.init.domain.SysShopNpcEquip;
import com.kitty.system.init.domain.SysShopNpcDrug;
import com.kitty.system.init.domain.SysShopNpcPet;
import com.kitty.system.init.domain.SysShopNpcNice;
import com.kitty.system.init.domain.SysShopNpcNao;
import com.kitty.system.init.domain.SysShopNpcLong;
import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.init.domain.SysShopNpcGuo;
import com.kitty.system.init.domain.SysShopNpcFeats;
import com.kitty.system.init.domain.SysShopNpcDi;
import com.kitty.system.init.domain.SysShopNpcBrac;
import com.kitty.system.init.domain.SysShopNpcArena;
import java.util.Map;

public class MallDataPool
{
    public static Map<Integer, SysShopNpcArena> shop_npc_arena;
    public static Map<Integer, SysShopNpcBrac> shop_npc_brac;
    public static Map<Integer, SysShopNpcDi> shop_npc_di;
    public static Map<Integer, SysShopNpcFeats> shop_npc_feats;
    public static Map<Integer, SysShopNpcGuo> shop_npc_guo;
    public static Map<Integer, SysShopNpcGuoRecycle> shop_npc_guo_recycle;
    public static Map<Integer, SysShopNpcLong> shop_npc_long;
    public static Map<Integer, SysShopNpcNao> shop_npc_nao;
    public static Map<Integer, SysShopNpcNice> shop_npc_nice;
    public static Map<Integer, SysShopNpcPet> shop_npc_pet;
    public static Map<Integer, SysShopNpcDrug> shop_npc_drug;
    public static Map<Integer, SysShopNpcEquip> shop_npc_equip;
    public static Map<Integer, SysShopNpcGrocery> shop_npc_grocery;
    public static Map<Integer, SysShopOther> data_shopother;
    public static Map<Integer, SysShopStrengthenMaterial> data_shopStrengthenMaterial;
    public static Map<Integer, SysDataLimitShop> t_data_limit_shop;
    public static Map<Integer, SysDataRecommendShop> data_recommend_shop;
    public static Map<Integer, SysBoundShop> boundShopMap;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
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
    
    public static void initShopNpcArena(final InitService initService) {
        MallDataPool.shop_npc_arena = initService.initShopNpcArena();
    }
    
    public static void initShopNpcBrac(final InitService initService) {
        MallDataPool.shop_npc_brac = initService.initShopNpcBrac();
    }
    
    public static void initShopNpcDi(final InitService initService) {
        MallDataPool.shop_npc_di = initService.initShopNpcDi();
    }
    
    public static void initShopNpcFeats(final InitService initService) {
        MallDataPool.shop_npc_feats = initService.initShopNpcFeats();
    }
    
    public static void initShopNpcGuo(final InitService initService) {
        MallDataPool.shop_npc_guo = initService.initShopNpcGuo();
    }
    
    public static void initShopNpcGuoRecycle(final InitService initService) {
        MallDataPool.shop_npc_guo_recycle = initService.initShopNpcGuoRecycle();
    }
    
    public static void initShopNpcLong(final InitService initService) {
        MallDataPool.shop_npc_long = initService.initShopNpcLong();
    }
    
    public static void initShopNpcNao(final InitService initService) {
        MallDataPool.shop_npc_nao = initService.initShopNpcNao();
    }
    
    public static void initShopNpcNice(final InitService initService) {
        MallDataPool.shop_npc_nice = initService.initShopNpcNice();
    }
    
    public static void initShopNpcPet(final InitService initService) {
        MallDataPool.shop_npc_pet = initService.initShopNpcPet();
    }
    
    public static void initShopNpcDrug(final InitService initService) {
        MallDataPool.shop_npc_drug = initService.initShopNpcDrug();
    }
    
    public static void initShopNpcEquip(final InitService initService) {
        MallDataPool.shop_npc_equip = initService.initShopNpcEquip();
    }
    
    public static void initShopNpcGrocery(final InitService initService) {
        MallDataPool.shop_npc_grocery = initService.initShopNpcGrocery();
    }
    
    public static void initShopOther(final InitService initService) {
        MallDataPool.data_shopother = initService.initShopOther();
    }
    
    public static void initShopStrengthenMaterial(final InitService initService) {
        MallDataPool.data_shopStrengthenMaterial = initService.initShopStrengthenMaterial();
    }
    
    public static void initLimitShop(final InitService initService) {
        MallDataPool.t_data_limit_shop = initService.initLimitShop();
    }
    
    public static void initRecommendShop(final InitService initService) {
        MallDataPool.data_recommend_shop = initService.initRecommendShop();
    }
    
    public static void initBoundShop(final InitService initService) {
        MallDataPool.boundShopMap = initService.initBoundShop();
    }
    
    static {
        MallDataPool.shop_npc_arena = new HashMap<Integer, SysShopNpcArena>();
        MallDataPool.shop_npc_brac = new HashMap<Integer, SysShopNpcBrac>();
        MallDataPool.shop_npc_di = new HashMap<Integer, SysShopNpcDi>();
        MallDataPool.shop_npc_feats = new HashMap<Integer, SysShopNpcFeats>();
        MallDataPool.shop_npc_guo = new HashMap<Integer, SysShopNpcGuo>();
        MallDataPool.shop_npc_guo_recycle = new HashMap<Integer, SysShopNpcGuoRecycle>();
        MallDataPool.shop_npc_long = new HashMap<Integer, SysShopNpcLong>();
        MallDataPool.shop_npc_nao = new HashMap<Integer, SysShopNpcNao>();
        MallDataPool.shop_npc_nice = new HashMap<Integer, SysShopNpcNice>();
        MallDataPool.shop_npc_pet = new HashMap<Integer, SysShopNpcPet>();
        MallDataPool.shop_npc_drug = new HashMap<Integer, SysShopNpcDrug>();
        MallDataPool.shop_npc_equip = new HashMap<Integer, SysShopNpcEquip>();
        MallDataPool.shop_npc_grocery = new HashMap<Integer, SysShopNpcGrocery>();
        MallDataPool.data_shopother = new HashMap<Integer, SysShopOther>();
        MallDataPool.data_shopStrengthenMaterial = new HashMap<Integer, SysShopStrengthenMaterial>();
        MallDataPool.t_data_limit_shop = new HashMap<Integer, SysDataLimitShop>();
        MallDataPool.data_recommend_shop = new HashMap<Integer, SysDataRecommendShop>();
        MallDataPool.boundShopMap = new HashMap<Integer, SysBoundShop>();
    }
}
