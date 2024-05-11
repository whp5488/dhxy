
package com.kitty.system.game.player.serializer;

import java.util.Set;
import com.kitty.common.utils.ClassScanner;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;

public class PetSerializerUtil
{
    private static List<IPetPropSerializer> propSerializers;
    private static final String SCAN_PATH = "com.kitty.system.game";
    
    public static void serialize(final Pet pet) {
        for (final IPetPropSerializer handler : PetSerializerUtil.propSerializers) {
            handler.serialize(pet);
        }
    }
    
    public static void deserialize(final Pet pet) {
        for (final IPetPropSerializer handler : PetSerializerUtil.propSerializers) {
            handler.deserialize(pet);
        }
    }
    
    static {
        PetSerializerUtil.propSerializers = new ArrayList<IPetPropSerializer>();
        final Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IPetPropSerializer.class);
        for (final Class<?> clazz : handler) {
            try {
                PetSerializerUtil.propSerializers.add((IPetPropSerializer)clazz.newInstance());
            }
            catch (final Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
