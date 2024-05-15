
package com.kitty.system.enums.item;

import java.util.ArrayList;
import java.util.List;

public enum ItemClassify
{
    RESTS(1), 
    PLUG(1024), 
    RESTYPE_SILVER(90021), 
    HERO_EXP(90022), 
    PET_EXP(90023), 
    RESTYPE_COIN(90024), 
    RESTYPE_TRIBUTE(90026), 
    RESTYPE_BRAC(90034), 
    RES_TYPE_BOUND_GOLD(90035), 
    RES_TYPE_GOLD(90036), 
    RES_TYPE_BRAC(6), 
    RES_TYPE_CHARGE_SCORE(15), 
    RES_TYPE_ACHIEVEMENT(17), 
    STATUS(65);
    
    private int id;
    
    private ItemClassify(final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static List<Integer> getValue() {
        final List<Integer> objects = new ArrayList<>();
        for (final ItemClassify itemClassify : values()) {
            objects.add(itemClassify.id);
        }
        return objects;
    }
}
