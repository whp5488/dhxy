package com.kitty.common.utils;

import java.util.ArrayList;

public class PositionUtils
{
    public static final ArrayList<Short> positions;
    
    public PositionUtils() {
    }
    
    static {
        (positions = new ArrayList<>()).add(Short.valueOf((short)3));
        PositionUtils.positions.add(Short.valueOf((short)2));
        PositionUtils.positions.add(Short.valueOf((short)4));
        PositionUtils.positions.add(Short.valueOf((short)1));
        PositionUtils.positions.add(Short.valueOf((short)5));
        PositionUtils.positions.add(Short.valueOf((short)8));
        PositionUtils.positions.add(Short.valueOf((short)7));
        PositionUtils.positions.add(Short.valueOf((short)9));
        PositionUtils.positions.add(Short.valueOf((short)6));
        PositionUtils.positions.add(Short.valueOf((short)10));
    }
}
