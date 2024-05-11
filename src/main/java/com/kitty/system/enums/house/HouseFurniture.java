
package com.kitty.system.enums.house;

public enum HouseFurniture
{
    MOTIF(0), 
    FURNISHINGS(1), 
    DRESS_UP(2), 
    CRAFT(3), 
    STOOL(4), 
    WALL(5), 
    LANE(6), 
    BOTANY(7);
    
    private int type;
    
    public int getType() {
        return this.type;
    }
    
    private HouseFurniture(final int type) {
        this.type = type;
    }
}
