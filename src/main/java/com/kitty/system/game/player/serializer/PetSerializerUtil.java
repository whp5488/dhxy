package com.kitty.system.game.player.serializer;

import com.kitty.common.utils.ClassScanner;
import com.kitty.system.game.pet.domain.Pet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PetSerializerUtil {
  private static List<IPetPropSerializer> propSerializers = new ArrayList<>();
  
  private static final String SCAN_PATH = "com.kitty.system.game";
  
  static {
    Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IPetPropSerializer.class);
    for (Class<?> clazz : handler) {
      try {
        propSerializers.add((IPetPropSerializer)clazz.newInstance());
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } 
  }
  
  public static void serialize(Pet pet) {
    for (IPetPropSerializer handler : propSerializers)
      handler.serialize(pet); 
  }
  
  public static void deserialize(Pet pet) {
    for (IPetPropSerializer handler : propSerializers)
      handler.deserialize(pet); 
  }
}
