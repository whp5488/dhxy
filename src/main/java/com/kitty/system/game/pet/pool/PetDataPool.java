
package com.kitty.system.game.pet.pool;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.enums.war.warskill.petskilldef.ULTIMATE_PETSKILL;
import com.kitty.system.enums.war.warskill.petskilldef.HIGH_PETSKILL;
import com.kitty.system.enums.war.warskill.petskilldef.GENERAL_PETSKILL;
import com.kitty.system.enums.war.warskill.petskilldef.DOGZ_PETSKILL;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.vo.PetAccColor;
import com.kitty.system.init.domain.SysPet;
import java.util.Map;
import java.util.List;

public class PetDataPool
{
    public static List<Integer> ACTIVE_PETSKILLLIST;
    public static Map<Integer, SysPet> petMap;
    public static Map<String, Map<Byte, PetAccColor>> petAccColorMap;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        initActivePetSkill();
        initPet(initService);
        initPetAccColorMap(initService);
    }
    
    public static void initActivePetSkill() {
        final List<Map<String, Integer>> dogz_maps = DOGZ_PETSKILL.toList();
        for (final Map<String, Integer> map : dogz_maps) {
            final Integer type = map.get("type");
            if (type == 1) {
                PetDataPool.ACTIVE_PETSKILLLIST.add(map.get("value"));
            }
        }
        final List<Map<String, Integer>> generl_maps = GENERAL_PETSKILL.toList();
        for (final Map<String, Integer> map2 : generl_maps) {
            final Integer type2 = map2.get("type");
            if (type2 == 1) {
                PetDataPool.ACTIVE_PETSKILLLIST.add(map2.get("value"));
            }
        }
        final List<Map<String, Integer>> high_maps = HIGH_PETSKILL.toList();
        for (final Map<String, Integer> map3 : high_maps) {
            final Integer type3 = map3.get("type");
            if (type3 == 1) {
                PetDataPool.ACTIVE_PETSKILLLIST.add(map3.get("value"));
            }
        }
        final List<Map<String, Integer>> ultimate_maps = ULTIMATE_PETSKILL.toList();
        for (final Map<String, Integer> map4 : ultimate_maps) {
            final Integer type4 = map4.get("type");
            if (type4 == 1) {
                PetDataPool.ACTIVE_PETSKILLLIST.add(map4.get("value"));
            }
        }
    }
    
    public static void initPet(final InitService initService) {
        PetDataPool.petMap = initService.initPet();
    }
    
    public static void initPetAccColorMap(final InitService initService) {
        PetDataPool.petAccColorMap = initService.initPetAccColor();
    }
    
    static {
        PetDataPool.ACTIVE_PETSKILLLIST = new ArrayList<Integer>();
        PetDataPool.petMap = new HashMap<Integer, SysPet>();
        PetDataPool.petAccColorMap = new HashMap<String, Map<Byte, PetAccColor>>();
    }
}
