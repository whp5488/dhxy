package com.kitty.system.init.pool;

import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertDataPool {
  public static Map<Integer, Map<Integer, Integer>> defPetCorrectionMaxMap = new HashMap<>();
  
  public static Map<Integer, Double> defPetCorrectionMap = new HashMap<>();
  
  public static List<Integer> PRO_BASE_DICT = new ArrayList<>();
  
  public static Map<Integer, Integer[]> petSkillReplace = (Map)new HashMap<>();
  
  public static Map<Integer, Item> bagExtend = new HashMap<>();
  
  public static Map<Integer, Item> storeExtend = new HashMap<>();
  
  public static Map<Integer, Item> petBagEquipExtend = new HashMap<>();
  
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
    List<Integer> list = ZB_PRO_BASE_DICT.toList();
    PRO_BASE_DICT.addAll(list);
  }
  
  public static void initDefPetCorrectionMaxMap() {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(10));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(10));
    defPetCorrectionMaxMap.put(Integer.valueOf(1), map);
    map = new HashMap<>();
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(12));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(15));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(13));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(13));
    defPetCorrectionMaxMap.put(Integer.valueOf(2), map);
    map = new HashMap<>();
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(24));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(24));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(24));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(24));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(14));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(20));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(16));
    defPetCorrectionMaxMap.put(Integer.valueOf(3), map);
    map = new HashMap<>();
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(26));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(26));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(26));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(26));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(25));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(16));
    map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(19));
    defPetCorrectionMaxMap.put(Integer.valueOf(4), map);
  }
  
  public static void initDefPetCorrection() {
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Double.valueOf(0.03D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Double.valueOf(0.015D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Double.valueOf(0.015D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Double.valueOf(1.0D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Double.valueOf(0.015D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Double.valueOf(0.015D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Double.valueOf(0.03D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Double.valueOf(0.03D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Double.valueOf(0.04D));
    defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Double.valueOf(0.04D));
  }
  
  public static void initPetSkillReplace() {
    petSkillReplace.put(Integer.valueOf(0), new Integer[] { Integer.valueOf(88), Integer.valueOf(12) });
    petSkillReplace.put(Integer.valueOf(1), new Integer[] { Integer.valueOf(82), Integer.valueOf(18) });
    petSkillReplace.put(Integer.valueOf(2), new Integer[] { Integer.valueOf(90), Integer.valueOf(10) });
    petSkillReplace.put(Integer.valueOf(3), new Integer[] { Integer.valueOf(92), Integer.valueOf(8) });
    petSkillReplace.put(Integer.valueOf(4), new Integer[] { Integer.valueOf(80), Integer.valueOf(20) });
    petSkillReplace.put(Integer.valueOf(5), new Integer[] { Integer.valueOf(91), Integer.valueOf(9) });
    petSkillReplace.put(Integer.valueOf(6), new Integer[] { Integer.valueOf(89), Integer.valueOf(11) });
    petSkillReplace.put(Integer.valueOf(7), new Integer[] { Integer.valueOf(86), Integer.valueOf(14) });
    petSkillReplace.put(Integer.valueOf(8), new Integer[] { Integer.valueOf(80), Integer.valueOf(20) });
    petSkillReplace.put(Integer.valueOf(9), new Integer[] { Integer.valueOf(87), Integer.valueOf(13) });
    petSkillReplace.put(Integer.valueOf(10), new Integer[] { Integer.valueOf(88), Integer.valueOf(12) });
    petSkillReplace.put(Integer.valueOf(11), new Integer[] { Integer.valueOf(86), Integer.valueOf(14) });
    petSkillReplace.put(Integer.valueOf(12), new Integer[] { Integer.valueOf(95), Integer.valueOf(5) });
  }
  
  public static void initBagExtend() {
    bagExtend.put(Integer.valueOf(1), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
    bagExtend.put(Integer.valueOf(2), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
    bagExtend.put(Integer.valueOf(3), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
    bagExtend.put(Integer.valueOf(4), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
    bagExtend.put(Integer.valueOf(5), new Item(Const.RESTYPE_SILVER, Integer.valueOf(2000000)));
    bagExtend.put(Integer.valueOf(6), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3000000)));
    bagExtend.put(Integer.valueOf(7), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3500000)));
    bagExtend.put(Integer.valueOf(8), new Item(Const.RESTYPE_SILVER, Integer.valueOf(4000000)));
    bagExtend.put(Integer.valueOf(9), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
    bagExtend.put(Integer.valueOf(10), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
    bagExtend.put(Integer.valueOf(11), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
    bagExtend.put(Integer.valueOf(12), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
    bagExtend.put(Integer.valueOf(13), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
    bagExtend.put(Integer.valueOf(14), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
    bagExtend.put(Integer.valueOf(15), new Item(Const.RESTYPE_GOLD, Integer.valueOf(250)));
  }
  
  public static void initStoreExtend() {
    storeExtend.put(Integer.valueOf(1), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
    storeExtend.put(Integer.valueOf(2), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
    storeExtend.put(Integer.valueOf(3), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
    storeExtend.put(Integer.valueOf(4), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
    storeExtend.put(Integer.valueOf(5), new Item(Const.RESTYPE_SILVER, Integer.valueOf(2000000)));
    storeExtend.put(Integer.valueOf(6), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3000000)));
    storeExtend.put(Integer.valueOf(7), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3500000)));
    storeExtend.put(Integer.valueOf(8), new Item(Const.RESTYPE_SILVER, Integer.valueOf(4000000)));
    storeExtend.put(Integer.valueOf(9), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
    storeExtend.put(Integer.valueOf(10), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
    storeExtend.put(Integer.valueOf(11), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
    storeExtend.put(Integer.valueOf(12), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
    storeExtend.put(Integer.valueOf(13), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
    storeExtend.put(Integer.valueOf(14), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
    storeExtend.put(Integer.valueOf(15), new Item(Const.RESTYPE_GOLD, Integer.valueOf(250)));
    storeExtend.put(Integer.valueOf(16), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
    storeExtend.put(Integer.valueOf(17), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
    storeExtend.put(Integer.valueOf(18), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
    storeExtend.put(Integer.valueOf(19), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
    storeExtend.put(Integer.valueOf(20), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1000)));
    storeExtend.put(Integer.valueOf(21), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1000)));
    storeExtend.put(Integer.valueOf(22), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1000)));
    storeExtend.put(Integer.valueOf(23), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1500)));
    storeExtend.put(Integer.valueOf(24), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1500)));
    storeExtend.put(Integer.valueOf(25), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1500)));
  }
  
  public static void initPetBagEquipExtend() {
    petBagEquipExtend.put(Integer.valueOf(1), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
    petBagEquipExtend.put(Integer.valueOf(2), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
    petBagEquipExtend.put(Integer.valueOf(3), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
    petBagEquipExtend.put(Integer.valueOf(4), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
    petBagEquipExtend.put(Integer.valueOf(5), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
    petBagEquipExtend.put(Integer.valueOf(6), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
    petBagEquipExtend.put(Integer.valueOf(7), new Item(Const.RESTYPE_GOLD, Integer.valueOf(250)));
  }
}
