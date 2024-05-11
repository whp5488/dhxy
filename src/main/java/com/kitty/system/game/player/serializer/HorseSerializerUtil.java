
package com.kitty.system.game.player.serializer;

import java.util.Set;
import com.kitty.common.utils.ClassScanner;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.horse.domain.Horse;
import java.util.List;

public class HorseSerializerUtil
{
    private static List<IHorsePropSerializer> propSerializers;
    private static final String SCAN_PATH = "com.kitty.system.game";
    
    public static void serialize(final Horse obj) {
        for (final IHorsePropSerializer handler : HorseSerializerUtil.propSerializers) {
            handler.serialize(obj);
        }
    }
    
    public static void deserialize(final Horse obj) {
        for (final IHorsePropSerializer handler : HorseSerializerUtil.propSerializers) {
            handler.deserialize(obj);
        }
    }
    
    static {
        HorseSerializerUtil.propSerializers = new ArrayList<IHorsePropSerializer>();
        final Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IHorsePropSerializer.class);
        for (final Class<?> clazz : handler) {
            try {
                HorseSerializerUtil.propSerializers.add((IHorsePropSerializer)clazz.newInstance());
            }
            catch (final Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
