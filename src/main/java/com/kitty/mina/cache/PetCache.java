package com.kitty.mina.cache;

import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.pet.domain.Pet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface PetCache {
  public static final ConcurrentHashMap<Long, Map<Long, PetEquip>> PET_EQUIP_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Map<Integer, Pet>> PET_MAP = new ConcurrentHashMap<>();
}
