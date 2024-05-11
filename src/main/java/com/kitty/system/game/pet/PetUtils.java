
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
    public static byte addPetColor(final Pet pet, final List<Byte> colors) {
        final int round = RandomUtil.round(0, colors.size() - 1);
        final Byte color = colors.get(round);
        final Map<String, Byte> map = new HashMap<String, Byte>();
        map.put("color", color);
        map.put("is_use", (byte)1);
        for (final Map<String, Byte> mp : pet.getColorList()) {
            if (mp.get("is_use") == 1) {
                mp.put("is_use", (byte)0);
            }
        }
        pet.getColorList().add(map);
        return color;
    }
    
    public static List<Byte> getAddPetColors(final Pet pet) {
        final Map<Integer, SysPet> petMap = PetDataPool.petMap;
        final SysPet sysPet = petMap.get(pet.getPetType());
        List<Byte> colors = null;
        if (sysPet == null) {
            return null;
        }
        final List<Byte> color = sysPet.getColor();
        if (pet.getColorList().size() >= color.size()) {
            return new ArrayList<Byte>();
        }
        colors = new ArrayList<Byte>();
        for (final Byte b : color) {
            if (b != null) {
                colors.add(b);
            }
        }
        Byte color2 = null;
        for (final Map<String, Byte> map : pet.getColorList()) {
            color2 = map.get("color");
            colors.remove(color2);
        }
        return colors;
    }
}
