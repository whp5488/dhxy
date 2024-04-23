package com.kitty.system.game.player.serializer;

import com.kitty.common.utils.ClassScanner;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerSerializerUtil {
  private static List<IPlayerPropSerializer> propSerializers = new ArrayList<>();
  
  private static final String SCAN_PATH = "com.kitty.system.game";
  
  static {
    Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IPlayerPropSerializer.class);
    for (Class<?> clazz : handler) {
      try {
        propSerializers.add((IPlayerPropSerializer)clazz.newInstance());
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } 
  }
  
  public static void serialize(Role player) {
    for (int i = propSerializers.size() - 1; i >= 0; i--) {
      IPlayerPropSerializer handler = propSerializers.get(i);
      handler.serialize(player);
    } 
  }
  
  public static void deserialize(Role player) {
    for (int i = propSerializers.size() - 1; i >= 0; i--) {
      IPlayerPropSerializer handler = propSerializers.get(i);
      handler.deserialize(player);
    } 
  }
}
