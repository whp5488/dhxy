package com.kitty.system.game.pet;

import java.util.ArrayList;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.pet.pool.PetDataPool;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import com.kitty.common.utils.RandomUtil;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import java.io.Serializable;

public class PetUtils implements Serializable
{
    public PetUtils() {
    }
    
    public static byte addPetColor(Pet pet, List<Byte> colors) {
        int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(colors.size() - 1));
        Byte color = (Byte)colors.get(round);
        Map<String, Byte> map = new HashMap();
        map.put("color", color);
        map.put("is_use", Byte.valueOf((byte)1));
        for (Map<String, Byte> mp : pet.getColorList()) {
            if ((byte)mp.get("is_use") == 1) {
                mp.put("is_use", Byte.valueOf((byte)0));
            }
            else {
                continue;
            }
        }
        pet.getColorList().add(map);
        return (byte)color;
    }
    
    public static List<Byte> getAddPetColors(Pet pet) {
        Map<Integer, SysPet> petMap = PetDataPool.petMap;
        SysPet sysPet = (SysPet)petMap.get(pet.getPetType());
        List<Byte> colors = null;
        if (sysPet == null) {
            return null;
        }
        List<Byte> color = sysPet.getColor();
        if (pet.getColorList().size() >= color.size()) {
            return new ArrayList<>();
        }
        colors = new ArrayList<>();
        for (Byte b : color) {
            if (b != null) {
                colors.add(b);
            }
            else {
                continue;
            }
        }
        Byte color2 = null;
        for (Map<String, Byte> map : pet.getColorList()) {
            color2 = (Byte)map.get("color");
            colors.remove(color2);
        }
        return colors;
    }
}
