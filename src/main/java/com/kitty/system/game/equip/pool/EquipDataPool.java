
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
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
    
    public static void initNewBieEq(final InitService initService) {
        EquipDataPool.newBieEq = initService.initNewBieEq();
    }
    
    public static void initSeniorBieEq(final InitService initService) {
        EquipDataPool.seniorBieEq = initService.initSeniorBieEq();
    }
    
    public static void initMagicalBieEq(final InitService initService) {
        EquipDataPool.magicalBieEq = initService.initMagicalBieEq();
        for (final Integer key : EquipDataPool.magicalBieEq.keySet()) {
            final SysMagicalBieEq bieEq = EquipDataPool.magicalBieEq.get(key);
            if (DataGlobalFunc.getPj(bieEq.getSerial()) == 1) {
                EquipDataPool.lowMagicalBieEq.add(bieEq);
            }
        }
    }
    
    public static void initSinBieEq(final InitService initService) {
        EquipDataPool.sinBieEq = initService.initSinBieEq();
        for (final Integer key : EquipDataPool.sinBieEq.keySet()) {
            final SysSinBieEq bieEq = EquipDataPool.sinBieEq.get(key);
            if (DataGlobalFunc.getPj(bieEq.getSerial()) == 1) {
                EquipDataPool.lowSinBieEq.add(bieEq);
            }
        }
    }
    
    public static void initRefine(final InitService initService) {
        EquipDataPool.refineList = initService.initRefine();
    }
    
    public static void initAboveAttrLib(final InitService initService) {
        EquipDataPool.above_attr_lib = initService.initAboveAttrLib();
    }
    
    public static void initPurify(final InitService initService) {
        EquipDataPool.purify = initService.initPurify();
    }
    
    public static void initAcsry(final InitService initService) {
        EquipDataPool.acsryMap = initService.initAcsry();
    }
    
    public static void initAcsryAIdentify(final InitService initService) {
        EquipDataPool.acsryAIdentifyMap = initService.initAcsryAIdentify();
    }
    
    public static void initAcsrySuit(final InitService initService) {
        EquipDataPool.acsrySuitMap = initService.initAcsrySuit();
    }
    
    public static void initAcsrySuitColor(final InitService initService) {
        EquipDataPool.acsrySuitColorMap = initService.initAcsrySuitColor();
    }
    
    public static void initAcsryAttr(final InitService initService) {
        EquipDataPool.acsryAttrMap = initService.initAcsryAttr();
        for (final String dict : EquipDataPool.acsryAttrMap.keySet()) {
            final SysDataAcsryAttr acsryAttr = EquipDataPool.acsryAttrMap.get(dict);
            final List<Lottery> list = new ArrayList<Lottery>();
            acsryAttr.getProp().forEach(obj -> list.add(new Lottery(obj.getSerial(), "", obj.getProp() * 1.0)));
            EquipDataPool.acsryAttrLotteryMap.put(dict, list);
        }
    }
    
    public static void initPetAcsry(final InitService initService) {
        EquipDataPool.petAcsryMap = initService.initPetAcsry();
    }
    
    public static void initPetAcsryAdvAttrLib(final InitService initService) {
        EquipDataPool.petAcsryAdvAttrLibMap = initService.initPetAcsryAdvAttrLib();
    }
    
    public static void initPetAcsryAttrLib(final InitService initService) {
        EquipDataPool.petAcsryAttrLibMap = initService.initPetAcsryAttrLib();
    }
    
    static {
        EquipDataPool.newBieEq = new HashMap<Integer, SysNewBieEq>();
        EquipDataPool.seniorBieEq = new HashMap<Integer, SysSeniorBieEq>();
        EquipDataPool.magicalBieEq = new HashMap<Integer, SysMagicalBieEq>();
        EquipDataPool.lowMagicalBieEq = new ArrayList<SysMagicalBieEq>();
        EquipDataPool.sinBieEq = new HashMap<Integer, SysSinBieEq>();
        EquipDataPool.lowSinBieEq = new ArrayList<SysSinBieEq>();
        EquipDataPool.acsryMap = new HashMap<Integer, SysDataAcsry>();
        EquipDataPool.acsryAIdentifyMap = new HashMap<String, SysDataAcsryIdentify>();
        EquipDataPool.acsryAttrMap = new HashMap<String, SysDataAcsryAttr>();
        EquipDataPool.acsrySuitMap = new HashMap<String, SysDataAcsrySuit>();
        EquipDataPool.acsryAttrLotteryMap = new HashMap<String, List<Lottery>>();
        EquipDataPool.acsrySuitColorMap = new HashMap<String, Map<Integer, SysDataAcsrySuitColor>>();
        EquipDataPool.petAcsryMap = new HashMap<Integer, SysPetAcsry>();
        EquipDataPool.petAcsryAdvAttrLibMap = new HashMap<Integer, SysPetAcsryAdvAttrLib>();
        EquipDataPool.petAcsryAttrLibMap = new HashMap<String, SysPetAcsryAttrLib>();
        EquipDataPool.refineList = new ArrayList<SysRefine>();
        EquipDataPool.above_attr_lib = new HashMap<String, SysAboveAttrLib>();
        EquipDataPool.purify = new ArrayList<SysPurifyAttr>();
    }
}
