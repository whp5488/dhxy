
package com.kitty.system.game.mirror;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MirrorDataPool
{
    public static Map<Byte, Map<Short, List<Integer>>> skillDataMap;
    
    public static List<Integer> getSkillIds(final byte polar, final short level) {
        final Map<Short, List<Integer>> listMap = MirrorDataPool.skillDataMap.get(polar);
        if (listMap != null) {
            final short floorLevel = (short)(level - level % 10);
            return listMap.get(floorLevel);
        }
        return null;
    }
    
    static {
        MirrorDataPool.skillDataMap = new HashMap<Byte, Map<Short, List<Integer>>>();
        Map<Short, List<Integer>> tempMap = new HashMap<Short, List<Integer>>();
        MirrorDataPool.skillDataMap.put((byte)1, tempMap);
        tempMap.put((short)30, Arrays.asList(2, 501, 11, 21, 32));
        tempMap.put((short)40, Arrays.asList(2, 501, 11, 12, 21, 22, 32, 33));
        tempMap.put((short)50, Arrays.asList(2, 501, 11, 12, 13, 21, 22, 23, 32, 33, 34));
        tempMap.put((short)60, Arrays.asList(2, 501, 11, 12, 13, 14, 22, 23, 24, 21, 32, 33, 34));
        tempMap.put((short)70, Arrays.asList(2, 501, 11, 12, 13, 14, 22, 23, 24, 21, 32, 33, 34));
        tempMap.put((short)80, Arrays.asList(2, 501, 11, 12, 13, 14, 22, 23, 24, 21, 32, 33, 34));
        tempMap.put((short)90, Arrays.asList(2, 501, 11, 12, 13, 14, 22, 23, 24, 21, 32, 33, 34));
        tempMap.put((short)100, Arrays.asList(2, 501, 11, 12, 13, 14, 15, 22, 23, 24, 25, 21, 32, 33, 34, 35));
        tempMap.put((short)110, Arrays.asList(2, 501, 11, 12, 13, 14, 15, 22, 23, 24, 25, 21, 32, 33, 34, 35));
        tempMap.put((short)120, Arrays.asList(2, 501, 11, 12, 13, 14, 15, 22, 23, 24, 25, 21, 32, 33, 34, 35));
        tempMap.put((short)130, Arrays.asList(2, 501, 11, 12, 13, 14, 15, 22, 23, 24, 25, 21, 32, 33, 34, 35));
        tempMap = new HashMap<Short, List<Integer>>();
        MirrorDataPool.skillDataMap.put((byte)2, tempMap);
        tempMap.put((short)30, Arrays.asList(2, 501, 61, 71, 81));
        tempMap.put((short)40, Arrays.asList(2, 501, 61, 62, 71, 72, 81, 82));
        tempMap.put((short)50, Arrays.asList(2, 501, 61, 62, 63, 71, 72, 73, 81, 82, 83));
        tempMap.put((short)60, Arrays.asList(2, 501, 61, 62, 63, 64, 71, 72, 73, 74, 81, 82, 83, 84));
        tempMap.put((short)70, Arrays.asList(2, 501, 61, 62, 63, 64, 71, 72, 73, 74, 81, 82, 83, 84));
        tempMap.put((short)80, Arrays.asList(2, 501, 61, 62, 63, 64, 71, 72, 73, 74, 81, 82, 83, 84));
        tempMap.put((short)90, Arrays.asList(2, 501, 61, 62, 63, 64, 71, 72, 73, 74, 81, 82, 83, 84));
        tempMap.put((short)100, Arrays.asList(2, 501, 61, 62, 63, 64, 65, 71, 72, 73, 74, 75, 81, 82, 83, 84, 85));
        tempMap.put((short)110, Arrays.asList(2, 501, 61, 62, 63, 64, 65, 71, 72, 73, 74, 75, 81, 82, 83, 84, 85));
        tempMap.put((short)120, Arrays.asList(2, 501, 61, 62, 63, 64, 65, 71, 72, 73, 74, 75, 81, 82, 83, 84, 85));
        tempMap.put((short)130, Arrays.asList(2, 501, 61, 62, 63, 64, 65, 71, 72, 73, 74, 75, 81, 82, 83, 84, 85));
        tempMap = new HashMap<Short, List<Integer>>();
        MirrorDataPool.skillDataMap.put((byte)3, tempMap);
        tempMap.put((short)30, Arrays.asList(2, 501, 110, 121, 131));
        tempMap.put((short)40, Arrays.asList(2, 501, 110, 111, 121, 122, 131, 132));
        tempMap.put((short)50, Arrays.asList(2, 501, 110, 111, 112, 121, 122, 123, 131, 132, 133));
        tempMap.put((short)60, Arrays.asList(2, 501, 110, 111, 112, 113, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)70, Arrays.asList(2, 501, 110, 111, 112, 113, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)80, Arrays.asList(2, 501, 110, 111, 112, 113, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)90, Arrays.asList(2, 501, 110, 111, 112, 113, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)100, Arrays.asList(2, 501, 110, 111, 112, 113, 114, 125, 135, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)110, Arrays.asList(2, 501, 110, 111, 112, 113, 114, 125, 135, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)120, Arrays.asList(2, 501, 110, 111, 112, 113, 114, 125, 135, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap.put((short)130, Arrays.asList(2, 501, 110, 111, 112, 113, 114, 125, 135, 121, 122, 123, 124, 131, 132, 133, 134));
        tempMap = new HashMap<Short, List<Integer>>();
        MirrorDataPool.skillDataMap.put((byte)4, tempMap);
        tempMap.put((short)30, Arrays.asList(2, 501, 161, 171, 181));
        tempMap.put((short)40, Arrays.asList(2, 501, 161, 162, 171, 172, 181, 182));
        tempMap.put((short)50, Arrays.asList(2, 501, 161, 162, 163, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)60, Arrays.asList(2, 501, 161, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)70, Arrays.asList(2, 501, 161, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)80, Arrays.asList(2, 501, 161, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)90, Arrays.asList(2, 501, 161, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)100, Arrays.asList(2, 501, 161, 165, 175, 185, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)110, Arrays.asList(2, 501, 161, 165, 175, 185, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)120, Arrays.asList(2, 501, 161, 165, 175, 185, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap.put((short)130, Arrays.asList(2, 501, 161, 165, 175, 185, 162, 163, 164, 174, 184, 171, 172, 173, 181, 182, 183));
        tempMap = new HashMap<Short, List<Integer>>();
        MirrorDataPool.skillDataMap.put((byte)5, tempMap);
        tempMap.put((short)30, Arrays.asList(2, 501, 210, 221, 231));
        tempMap.put((short)40, Arrays.asList(2, 501, 210, 211, 221, 222, 231, 232));
        tempMap.put((short)50, Arrays.asList(2, 501, 210, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)60, Arrays.asList(2, 501, 210, 213, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)70, Arrays.asList(2, 501, 210, 213, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)80, Arrays.asList(2, 501, 210, 213, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)90, Arrays.asList(2, 501, 210, 213, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)100, Arrays.asList(2, 501, 210, 213, 214, 235, 225, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)110, Arrays.asList(2, 501, 210, 213, 214, 235, 225, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)120, Arrays.asList(2, 501, 210, 213, 214, 235, 225, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
        tempMap.put((short)130, Arrays.asList(2, 501, 210, 213, 214, 235, 225, 224, 234, 211, 212, 221, 222, 223, 231, 232, 233));
    }
}
