
package com.kitty.system.init.pool;

import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import java.util.HashMap;
import java.util.Collection;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import java.util.Map;

public class ConvertDataPool
{
    public static Map<Integer, Map<Integer, Integer>> defPetCorrectionMaxMap;
    public static Map<Integer, Double> defPetCorrectionMap;
    public static List<Integer> PRO_BASE_DICT;
    public static Map<Integer, Integer[]> petSkillReplace;
    public static Map<Integer, Item> bagExtend;
    public static Map<Integer, Item> storeExtend;
    public static Map<Integer, Item> petBagEquipExtend;
    
    public static void load() {
        initProBaseDict();
        initDefPetCorrectionMaxMap();
        initDefPetCorrection();
        initPetSkillReplace();
        initBagExtend();
        initStoreExtend();
        initPetBagEquipExtend();
        System.out.println("属性定义初始化完成");
    }
    
    public static void initProBaseDict() {
        final List<Integer> list = ZB_PRO_BASE_DICT.toList();
        ConvertDataPool.PRO_BASE_DICT.addAll(list);
    }
    
    public static void initDefPetCorrectionMaxMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 3);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 10);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 10);
        ConvertDataPool.defPetCorrectionMaxMap.put(1, map);
        map = new HashMap<>();
        map.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 12);
        map.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 3);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 15);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 13);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 13);
        ConvertDataPool.defPetCorrectionMaxMap.put(2, map);
        map = new HashMap<>();
        map.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 24);
        map.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 24);
        map.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 24);
        map.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 24);
        map.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 14);
        map.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 3);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 20);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 16);
        ConvertDataPool.defPetCorrectionMaxMap.put(3, map);
        map = new HashMap<>();
        map.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 26);
        map.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 26);
        map.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 26);
        map.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 26);
        map.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 16);
        map.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 3);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 25);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 19);
        ConvertDataPool.defPetCorrectionMaxMap.put(4, map);
        map = new HashMap<>();
        map.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 3);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 21);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 21);
        ConvertDataPool.defPetCorrectionMaxMap.put(5, map);
    }
    
    public static void initDefPetCorrection() {
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 0.03);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 0.015);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 0.015);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 1.0);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 0.015);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 0.015);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 0.03);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 0.03);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 0.04);
        ConvertDataPool.defPetCorrectionMap.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 0.04);
    }
    
    public static void initPetSkillReplace() {
        ConvertDataPool.petSkillReplace.put(0, new Integer[] { 88, 12 });
        ConvertDataPool.petSkillReplace.put(1, new Integer[] { 82, 18 });
        ConvertDataPool.petSkillReplace.put(2, new Integer[] { 90, 10 });
        ConvertDataPool.petSkillReplace.put(3, new Integer[] { 92, 8 });
        ConvertDataPool.petSkillReplace.put(4, new Integer[] { 80, 20 });
        ConvertDataPool.petSkillReplace.put(5, new Integer[] { 91, 9 });
        ConvertDataPool.petSkillReplace.put(6, new Integer[] { 89, 11 });
        ConvertDataPool.petSkillReplace.put(7, new Integer[] { 86, 14 });
        ConvertDataPool.petSkillReplace.put(8, new Integer[] { 80, 20 });
        ConvertDataPool.petSkillReplace.put(9, new Integer[] { 87, 13 });
        ConvertDataPool.petSkillReplace.put(10, new Integer[] { 88, 12 });
        ConvertDataPool.petSkillReplace.put(11, new Integer[] { 86, 14 });
        ConvertDataPool.petSkillReplace.put(12, new Integer[] { 95, 5 });
    }
    
    public static void initBagExtend() {
        ConvertDataPool.bagExtend.put(1, new Item(Const.RESTYPE_SILVER, 500000));
        ConvertDataPool.bagExtend.put(2, new Item(Const.RESTYPE_SILVER, 1000000));
        ConvertDataPool.bagExtend.put(3, new Item(Const.RESTYPE_SILVER, 500000));
        ConvertDataPool.bagExtend.put(4, new Item(Const.RESTYPE_SILVER, 1000000));
        ConvertDataPool.bagExtend.put(5, new Item(Const.RESTYPE_SILVER, 2000000));
        ConvertDataPool.bagExtend.put(6, new Item(Const.RESTYPE_SILVER, 3000000));
        ConvertDataPool.bagExtend.put(7, new Item(Const.RESTYPE_SILVER, 3500000));
        ConvertDataPool.bagExtend.put(8, new Item(Const.RESTYPE_SILVER, 4000000));
        ConvertDataPool.bagExtend.put(9, new Item(Const.RESTYPE_GOLD, 100));
        ConvertDataPool.bagExtend.put(10, new Item(Const.RESTYPE_GOLD, 100));
        ConvertDataPool.bagExtend.put(11, new Item(Const.RESTYPE_GOLD, 150));
        ConvertDataPool.bagExtend.put(12, new Item(Const.RESTYPE_GOLD, 150));
        ConvertDataPool.bagExtend.put(13, new Item(Const.RESTYPE_GOLD, 200));
        ConvertDataPool.bagExtend.put(14, new Item(Const.RESTYPE_GOLD, 200));
        ConvertDataPool.bagExtend.put(15, new Item(Const.RESTYPE_GOLD, 250));
    }
    
    public static void initStoreExtend() {
        ConvertDataPool.storeExtend.put(1, new Item(Const.RESTYPE_SILVER, 500000));
        ConvertDataPool.storeExtend.put(2, new Item(Const.RESTYPE_SILVER, 1000000));
        ConvertDataPool.storeExtend.put(3, new Item(Const.RESTYPE_SILVER, 500000));
        ConvertDataPool.storeExtend.put(4, new Item(Const.RESTYPE_SILVER, 1000000));
        ConvertDataPool.storeExtend.put(5, new Item(Const.RESTYPE_SILVER, 2000000));
        ConvertDataPool.storeExtend.put(6, new Item(Const.RESTYPE_SILVER, 3000000));
        ConvertDataPool.storeExtend.put(7, new Item(Const.RESTYPE_SILVER, 3500000));
        ConvertDataPool.storeExtend.put(8, new Item(Const.RESTYPE_SILVER, 4000000));
        ConvertDataPool.storeExtend.put(9, new Item(Const.RESTYPE_GOLD, 100));
        ConvertDataPool.storeExtend.put(10, new Item(Const.RESTYPE_GOLD, 100));
        ConvertDataPool.storeExtend.put(11, new Item(Const.RESTYPE_GOLD, 150));
        ConvertDataPool.storeExtend.put(12, new Item(Const.RESTYPE_GOLD, 150));
        ConvertDataPool.storeExtend.put(13, new Item(Const.RESTYPE_GOLD, 200));
        ConvertDataPool.storeExtend.put(14, new Item(Const.RESTYPE_GOLD, 200));
        ConvertDataPool.storeExtend.put(15, new Item(Const.RESTYPE_GOLD, 250));
        ConvertDataPool.storeExtend.put(16, new Item(Const.RESTYPE_GOLD, 500));
        ConvertDataPool.storeExtend.put(17, new Item(Const.RESTYPE_GOLD, 500));
        ConvertDataPool.storeExtend.put(18, new Item(Const.RESTYPE_GOLD, 500));
        ConvertDataPool.storeExtend.put(19, new Item(Const.RESTYPE_GOLD, 500));
        ConvertDataPool.storeExtend.put(20, new Item(Const.RESTYPE_GOLD, 1000));
        ConvertDataPool.storeExtend.put(21, new Item(Const.RESTYPE_GOLD, 1000));
        ConvertDataPool.storeExtend.put(22, new Item(Const.RESTYPE_GOLD, 1000));
        ConvertDataPool.storeExtend.put(23, new Item(Const.RESTYPE_GOLD, 1500));
        ConvertDataPool.storeExtend.put(24, new Item(Const.RESTYPE_GOLD, 1500));
        ConvertDataPool.storeExtend.put(25, new Item(Const.RESTYPE_GOLD, 1500));
    }
    
    public static void initPetBagEquipExtend() {
        ConvertDataPool.petBagEquipExtend.put(1, new Item(Const.RESTYPE_GOLD, 100));
        ConvertDataPool.petBagEquipExtend.put(2, new Item(Const.RESTYPE_GOLD, 100));
        ConvertDataPool.petBagEquipExtend.put(3, new Item(Const.RESTYPE_GOLD, 150));
        ConvertDataPool.petBagEquipExtend.put(4, new Item(Const.RESTYPE_GOLD, 150));
        ConvertDataPool.petBagEquipExtend.put(5, new Item(Const.RESTYPE_GOLD, 200));
        ConvertDataPool.petBagEquipExtend.put(6, new Item(Const.RESTYPE_GOLD, 200));
        ConvertDataPool.petBagEquipExtend.put(7, new Item(Const.RESTYPE_GOLD, 250));
    }
    
    static {
        ConvertDataPool.defPetCorrectionMaxMap = new HashMap<>();
        ConvertDataPool.defPetCorrectionMap = new HashMap<>();
        ConvertDataPool.PRO_BASE_DICT = new ArrayList<>();
        ConvertDataPool.petSkillReplace = new HashMap<>();
        ConvertDataPool.bagExtend = new HashMap<>();
        ConvertDataPool.storeExtend = new HashMap<>();
        ConvertDataPool.petBagEquipExtend = new HashMap<>();
    }
}
