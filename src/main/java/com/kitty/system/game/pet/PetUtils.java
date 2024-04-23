package com.kitty.system.game.pet;

import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetUtils implements Serializable {
  public static byte addPetColor(Pet pet, List<Byte> colors) {
    int round = RandomUtil.round(0, colors.size() - 1);
    Byte color = colors.get(round);
    Map<String, Byte> map = new HashMap<>();
    map.put("color", color);
    map.put("is_use", (byte) 1);
    for (Map<String, Byte> mp : pet.getColorList()) {
      if (mp.get("is_use") == 1)
        mp.put("is_use", (byte) 0);
    } 
    pet.getColorList().add(map);
    return color;
  }
  
  public static List<Byte> getAddPetColors(Pet pet) {
    Map<Integer, SysPet> petMap = PetDataPool.petMap;
    SysPet sysPet = petMap.get(pet.getPetType());
    List<Byte> colors = null;
    if (sysPet == null)
      return null; 
    List<Byte> color = sysPet.getColor();
    if (pet.getColorList().size() >= color.size())
      return new ArrayList<>(); 
    colors = new ArrayList<>();
    for (Byte b : color) {
      if (b != null)
        colors.add(b); 
    } 
    Byte color1 = null;
    for (Map<String, Byte> map : pet.getColorList()) {
      color1 = map.get("color");
      colors.remove(color1);
    } 
    return colors;
  }
}
