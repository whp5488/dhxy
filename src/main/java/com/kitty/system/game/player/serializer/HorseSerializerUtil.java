package com.kitty.system.game.player.serializer;

import java.util.ArrayList;
import com.kitty.system.game.horse.domain.Horse;
import java.util.Iterator;
import java.util.Set;
import com.kitty.common.utils.ClassScanner;
import java.util.List;

public class HorseSerializerUtil
{
    private static List<IHorsePropSerializer> propSerializers;
    private static final String SCAN_PATH = "com.kitty.system.game";
    
    public HorseSerializerUtil() {
    }
    
    public static void init() {
        Set<Class<?>> handler = ClassScanner.listAllSubclasses("com.kitty.system.game", IHorsePropSerializer.class);
        for (Class<?> clazz : handler) {
            try {
                HorseSerializerUtil.propSerializers.add((IHorsePropSerializer)clazz.newInstance());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void serialize(Horse obj) {
        for (IHorsePropSerializer handler : HorseSerializerUtil.propSerializers) {
            handler.serialize(obj);
        }
    }
    
    public static void deserialize(Horse obj) {
        for (IHorsePropSerializer handler : HorseSerializerUtil.propSerializers) {
            handler.deserialize(obj);
        }
    }
    
    static {
        HorseSerializerUtil.propSerializers = new ArrayList<>();
    }
}
