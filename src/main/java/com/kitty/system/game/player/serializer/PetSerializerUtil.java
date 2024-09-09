package com.kitty.system.game.player.serializer;

import java.util.ArrayList;
import com.kitty.system.game.pet.domain.Pet;
import java.util.Iterator;
import java.util.Set;
import com.kitty.common.utils.ClassScanner;
import java.util.List;

public class PetSerializerUtil
{
    private static List<IPetPropSerializer> propSerializers;
    private static final String SCAN_PATH = "com.kitty.system.game";
    
    public PetSerializerUtil() {
    }
    
    public static void init() {
        Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IPetPropSerializer.class);
        for (Class<?> clazz : handler) {
            try {
                PetSerializerUtil.propSerializers.add((IPetPropSerializer)clazz.newInstance());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void serialize(Pet pet) {
        for (IPetPropSerializer handler : PetSerializerUtil.propSerializers) {
            handler.serialize(pet);
        }
    }
    
    public static void deserialize(Pet pet) {
        for (IPetPropSerializer handler : PetSerializerUtil.propSerializers) {
            handler.deserialize(pet);
        }
    }
    
    static {
        PetSerializerUtil.propSerializers = new ArrayList<>();
    }
}
