package com.kitty.system.game.equip.pool;

import java.util.HashMap;
import com.kitty.system.init.domain.vo.Prop;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.init.domain.SysRefine;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.init.domain.SysDataAcsrySuitColor;
import com.kitty.common.bean.Lottery;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.init.domain.SysDataAcsryAttr;
import com.kitty.system.init.domain.SysDataAcsryIdentify;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysSinBieEq;
import java.util.List;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysNewBieEq;
import java.util.Map;

public class EquipDataPool
{
    public static Map<Integer, SysNewBieEq> newBieEq;
    public static Map<Integer, SysSeniorBieEq> seniorBieEq;
    public static Map<Integer, SysMagicalBieEq> magicalBieEq;
    public static List<SysMagicalBieEq> lowMagicalBieEq;
    public static Map<Integer, SysSinBieEq> sinBieEq;
    public static List<SysSinBieEq> lowSinBieEq;
    public static Map<Integer, SysDataAcsry> acsryMap;
    public static Map<String, SysDataAcsryIdentify> acsryAIdentifyMap;
    public static Map<String, SysDataAcsryAttr> acsryAttrMap;
    public static Map<String, SysDataAcsrySuit> acsrySuitMap;
    public static Map<String, List<Lottery>> acsryAttrLotteryMap;
    public static Map<String, Map<Integer, SysDataAcsrySuitColor>> acsrySuitColorMap;
    public static Map<Integer, SysPetAcsry> petAcsryMap;
    public static Map<Integer, SysPetAcsryAdvAttrLib> petAcsryAdvAttrLibMap;
    public static Map<String, SysPetAcsryAttrLib> petAcsryAttrLibMap;
    public static List<SysRefine> refineList;
    public static Map<String, SysAboveAttrLib> above_attr_lib;
    public static List<SysPurifyAttr> purify;
    
    public EquipDataPool() {
    }
    
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
        EquipDataPool.newBieEq = initService.initNewBieEq();
    }
    
    public static void initSeniorBieEq(InitService initService) {
        EquipDataPool.seniorBieEq = initService.initSeniorBieEq();
    }
    
    public static void initMagicalBieEq(InitService initService) {
        EquipDataPool.magicalBieEq = initService.initMagicalBieEq();
        for (Integer key : EquipDataPool.magicalBieEq.keySet()) {
            SysMagicalBieEq bieEq = (SysMagicalBieEq)EquipDataPool.magicalBieEq.get(key);
            if (DataGlobalFunc.getPj(bieEq.getSerial()) == 1) {
                EquipDataPool.lowMagicalBieEq.add(bieEq);
            }
            else {
                continue;
            }
        }
    }
    
    public static void initSinBieEq(InitService initService) {
        EquipDataPool.sinBieEq = initService.initSinBieEq();
        for (Integer key : EquipDataPool.sinBieEq.keySet()) {
            SysSinBieEq bieEq = (SysSinBieEq)EquipDataPool.sinBieEq.get(key);
            if (DataGlobalFunc.getPj(bieEq.getSerial()) == 1) {
                EquipDataPool.lowSinBieEq.add(bieEq);
            }
            else {
                continue;
            }
        }
    }
    
    public static void initRefine(InitService initService) {
        EquipDataPool.refineList = initService.initRefine();
    }
    
    public static void initAboveAttrLib(InitService initService) {
        EquipDataPool.above_attr_lib = initService.initAboveAttrLib();
    }
    
    public static void initPurify(InitService initService) {
        EquipDataPool.purify = initService.initPurify();
    }
    
    public static void initAcsry(InitService initService) {
        EquipDataPool.acsryMap = initService.initAcsry();
    }
    
    public static void initAcsryAIdentify(InitService initService) {
        EquipDataPool.acsryAIdentifyMap = initService.initAcsryAIdentify();
    }
    
    public static void initAcsrySuit(InitService initService) {
        EquipDataPool.acsrySuitMap = initService.initAcsrySuit();
    }
    
    public static void initAcsrySuitColor(InitService initService) {
        EquipDataPool.acsrySuitColorMap = initService.initAcsrySuitColor();
    }
    
    public static void initAcsryAttr(InitService initService) {
        EquipDataPool.acsryAttrMap = initService.initAcsryAttr();
        for (String dict : EquipDataPool.acsryAttrMap.keySet()) {
            SysDataAcsryAttr acsryAttr = (SysDataAcsryAttr)EquipDataPool.acsryAttrMap.get(dict);
            List<Lottery> list = new ArrayList<>();
            acsryAttr.getProp().forEach(obj -> list.add(new Lottery(Integer.valueOf(obj.getSerial()), "", Double.valueOf(obj.getProp() * 1.0))));
            EquipDataPool.acsryAttrLotteryMap.put(dict, list);
        }
    }
    
    public static void initPetAcsry(InitService initService) {
        EquipDataPool.petAcsryMap = initService.initPetAcsry();
    }
    
    public static void initPetAcsryAdvAttrLib(InitService initService) {
        EquipDataPool.petAcsryAdvAttrLibMap = initService.initPetAcsryAdvAttrLib();
    }
    
    public static void initPetAcsryAttrLib(InitService initService) {
        EquipDataPool.petAcsryAttrLibMap = initService.initPetAcsryAttrLib();
    }
    
    static {
        EquipDataPool.newBieEq = new HashMap();
        EquipDataPool.seniorBieEq = new HashMap();
        EquipDataPool.magicalBieEq = new HashMap();
        EquipDataPool.lowMagicalBieEq = new ArrayList<>();
        EquipDataPool.sinBieEq = new HashMap();
        EquipDataPool.lowSinBieEq = new ArrayList<>();
        EquipDataPool.acsryMap = new HashMap();
        EquipDataPool.acsryAIdentifyMap = new HashMap();
        EquipDataPool.acsryAttrMap = new HashMap();
        EquipDataPool.acsrySuitMap = new HashMap();
        EquipDataPool.acsryAttrLotteryMap = new HashMap();
        EquipDataPool.acsrySuitColorMap = new HashMap();
        EquipDataPool.petAcsryMap = new HashMap();
        EquipDataPool.petAcsryAdvAttrLibMap = new HashMap();
        EquipDataPool.petAcsryAttrLibMap = new HashMap();
        EquipDataPool.refineList = new ArrayList<>();
        EquipDataPool.above_attr_lib = new HashMap();
        EquipDataPool.purify = new ArrayList<>();
    }
}
