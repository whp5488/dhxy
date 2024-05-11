
package com.kitty.system.game.player.serializer;

import java.util.Iterator;
import java.util.Set;
import com.kitty.common.utils.ClassScanner;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import java.util.List;

public class PlayerSerializerUtil
{
    private static List<IPlayerPropSerializer> propSerializers;
    private static final String SCAN_PATH = "com.kitty.system.game";
    
    public static void serialize(final Role player) {
        for (int i = PlayerSerializerUtil.propSerializers.size() - 1; i >= 0; --i) {
            final IPlayerPropSerializer handler = PlayerSerializerUtil.propSerializers.get(i);
            handler.serialize(player);
        }
    }
    
    public static void deserialize(final Role player) {
        for (int i = PlayerSerializerUtil.propSerializers.size() - 1; i >= 0; --i) {
            final IPlayerPropSerializer handler = PlayerSerializerUtil.propSerializers.get(i);
            handler.deserialize(player);
        }
    }
    
    static {
        PlayerSerializerUtil.propSerializers = new ArrayList<IPlayerPropSerializer>();
        final Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IPlayerPropSerializer.class);
        for (final Class<?> clazz : handler) {
            try {
                PlayerSerializerUtil.propSerializers.add((IPlayerPropSerializer)clazz.newInstance());
            }
            catch (final Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
