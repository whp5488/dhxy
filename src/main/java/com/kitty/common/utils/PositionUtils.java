
package com.kitty.common.utils;

import java.util.ArrayList;

public class PositionUtils
{
    public static final ArrayList<Short> positions;
    
    static {
        (positions = new ArrayList<>()).add((short) 3);
        PositionUtils.positions.add((short)2);
        PositionUtils.positions.add((short)4);
        PositionUtils.positions.add((short)1);
        PositionUtils.positions.add((short)5);
        PositionUtils.positions.add((short)8);
        PositionUtils.positions.add((short)7);
        PositionUtils.positions.add((short)9);
        PositionUtils.positions.add((short)6);
        PositionUtils.positions.add((short)10);
    }
}
