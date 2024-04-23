package com.kitty.system.game.equip.pool;

import com.kitty.common.bean.Lottery;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryAttr;
import com.kitty.system.init.domain.SysDataAcsryIdentify;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.init.domain.SysDataAcsrySuitColor;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.init.domain.SysRefine;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.vo.Prop;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EquipDataPool {
  public static Map<Integer, SysNewBieEq> newBieEq = new HashMap<>();
  
  public static Map<Integer, SysSeniorBieEq> seniorBieEq = new HashMap<>();
  
  public static Map<Integer, SysMagicalBieEq> magicalBieEq = new HashMap<>();
  
  public static List<SysMagicalBieEq> lowMagicalBieEq = new ArrayList<>();
  
  public static Map<Integer, SysSinBieEq> sinBieEq = new HashMap<>();
  
  public static List<SysSinBieEq> lowSinBieEq = new ArrayList<>();
  
  public static Map<Integer, SysDataAcsry> acsryMap = new HashMap<>();
  
  public static Map<String, SysDataAcsryIdentify> acsryAIdentifyMap = new HashMap<>();
  
  public static Map<String, SysDataAcsryAttr> acsryAttrMap = new HashMap<>();
  
  public static Map<String, SysDataAcsrySuit> acsrySuitMap = new HashMap<>();
  
  public static Map<String, List<Lottery>> acsryAttrLotteryMap = new HashMap<>();
  
  public static Map<String, Map<Integer, SysDataAcsrySuitColor>> acsrySuitColorMap = new HashMap<>();
  
  public static Map<Integer, SysPetAcsry> petAcsryMap = new HashMap<>();
  
  public static Map<Integer, SysPetAcsryAdvAttrLib> petAcsryAdvAttrLibMap = new HashMap<>();
  
  public static Map<String, SysPetAcsryAttrLib> petAcsryAttrLibMap = new HashMap<>();
  
  public static List<SysRefine> refineList = new ArrayList<>();
  
  public static Map<String, SysAboveAttrLib> above_attr_lib = new HashMap<>();
  
  public static List<SysPurifyAttr> purify = new ArrayList<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    initNewBieEq(initService);
    initSeniorBieEq(initService);
    initMagicalBieEq(initService);
    initSinBieEq(initService);
    initRefine(initService);
    initAboveAttrLib(initService);
    initPurify(initService);
    initAcsry(initService);
    initAcsryAIdentify(initService);
    initAcsrySuit(initService);
    initAcsryAttr(initService);
    initAcsrySuitColor(initService);
    initPetAcsry(initService);
    initPetAcsryAdvAttrLib(initService);
    initPetAcsryAttrLib(initService);
  }
  
  public static void initNewBieEq(InitService initService) {
    newBieEq = initService.initNewBieEq();
  }
  
  public static void initSeniorBieEq(InitService initService) {
    seniorBieEq = initService.initSeniorBieEq();
  }
  
  public static void initMagicalBieEq(InitService initService) {
    magicalBieEq = initService.initMagicalBieEq();
    for (Integer key : magicalBieEq.keySet()) {
      SysMagicalBieEq bieEq = magicalBieEq.get(key);
      if (DataGlobalFunc.getPj(bieEq.getSerial()) == 1)
        lowMagicalBieEq.add(bieEq); 
    } 
  }
  
  public static void initSinBieEq(InitService initService) {
    sinBieEq = initService.initSinBieEq();
    for (Integer key : sinBieEq.keySet()) {
      SysSinBieEq bieEq = sinBieEq.get(key);
      if (DataGlobalFunc.getPj(bieEq.getSerial()) == 1)
        lowSinBieEq.add(bieEq); 
    } 
  }
  
  public static void initRefine(InitService initService) {
    refineList = initService.initRefine();
  }
  
  public static void initAboveAttrLib(InitService initService) {
    above_attr_lib = initService.initAboveAttrLib();
  }
  
  public static void initPurify(InitService initService) {
    purify = initService.initPurify();
  }
  
  public static void initAcsry(InitService initService) {
    acsryMap = initService.initAcsry();
  }
  
  public static void initAcsryAIdentify(InitService initService) {
    acsryAIdentifyMap = initService.initAcsryAIdentify();
  }
  
  public static void initAcsrySuit(InitService initService) {
    acsrySuitMap = initService.initAcsrySuit();
  }
  
  public static void initAcsrySuitColor(InitService initService) {
    acsrySuitColorMap = initService.initAcsrySuitColor();
  }
  
  public static void initAcsryAttr(InitService initService) {
    acsryAttrMap = initService.initAcsryAttr();
    for (Iterator<String> iterator = acsryAttrMap.keySet().iterator(); iterator.hasNext(); ) {
      String dict = iterator.next();
      SysDataAcsryAttr acsryAttr = acsryAttrMap.get(dict);
      List<Lottery> list = new ArrayList<>();
      acsryAttr.getProp().forEach(obj -> list.add(new Lottery(Integer.valueOf(obj.getSerial()), "", Double.valueOf(obj.getProp() * 1.0D))));
      acsryAttrLotteryMap.put(dict, list);
    } 
  }
  
  public static void initPetAcsry(InitService initService) {
    petAcsryMap = initService.initPetAcsry();
  }
  
  public static void initPetAcsryAdvAttrLib(InitService initService) {
    petAcsryAdvAttrLibMap = initService.initPetAcsryAdvAttrLib();
  }
  
  public static void initPetAcsryAttrLib(InitService initService) {
    petAcsryAttrLibMap = initService.initPetAcsryAttrLib();
  }
}
