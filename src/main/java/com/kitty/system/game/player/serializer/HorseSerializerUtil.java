package com.kitty.system.game.player.serializer;

import com.kitty.common.utils.ClassScanner;
import com.kitty.system.game.horse.domain.Horse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HorseSerializerUtil {
  private static List<IHorsePropSerializer> propSerializers = new ArrayList<>();
  
  private static final String SCAN_PATH = "com.kitty.system.game";
  
  static {
    Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IHorsePropSerializer.class);
    for (Class<?> clazz : handler) {
      try {
        propSerializers.add((IHorsePropSerializer)clazz.newInstance());
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } 
  }
  
  public static void serialize(Horse obj) {
    for (IHorsePropSerializer handler : propSerializers)
      handler.serialize(obj); 
  }
  
  public static void deserialize(Horse obj) {
    for (IHorsePropSerializer handler : propSerializers)
      handler.deserialize(obj); 
  }
}
