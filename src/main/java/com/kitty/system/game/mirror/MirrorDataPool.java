package com.kitty.system.game.mirror;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MirrorDataPool {
  public static Map<Byte, Map<Short, List<Integer>>> skillDataMap = new HashMap<>();
  
  static {
    Map<Short, List<Integer>> tempMap = new HashMap<>();
    skillDataMap.put(Byte.valueOf((byte)1), tempMap);
    tempMap.put(Short.valueOf((short)30), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(21), Integer.valueOf(32) }));
    tempMap.put(Short.valueOf((short)40), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(32), Integer.valueOf(33) }));
    tempMap.put(Short.valueOf((short)50), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(32), Integer.valueOf(33), 
            Integer.valueOf(34) }));
    tempMap.put(Short.valueOf((short)60), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(21), 
            Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34) }));
    tempMap.put(Short.valueOf((short)70), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(21), 
            Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34) }));
    tempMap.put(Short.valueOf((short)80), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(21), 
            Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34) }));
    tempMap.put(Short.valueOf((short)90), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(21), 
            Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34) }));
    tempMap.put(Short.valueOf((short)100), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), 
            Integer.valueOf(25), Integer.valueOf(21), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) }));
    tempMap.put(Short.valueOf((short)110), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), 
            Integer.valueOf(25), Integer.valueOf(21), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) }));
    tempMap.put(Short.valueOf((short)120), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), 
            Integer.valueOf(25), Integer.valueOf(21), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) }));
    tempMap.put(Short.valueOf((short)130), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), 
            Integer.valueOf(25), Integer.valueOf(21), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) }));
    tempMap = new HashMap<>();
    skillDataMap.put(Byte.valueOf((byte)2), tempMap);
    tempMap.put(Short.valueOf((short)30), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(71), Integer.valueOf(81) }));
    tempMap.put(Short.valueOf((short)40), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(81), Integer.valueOf(82) }));
    tempMap.put(Short.valueOf((short)50), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), Integer.valueOf(81), Integer.valueOf(82), 
            Integer.valueOf(83) }));
    tempMap.put(Short.valueOf((short)60), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), Integer.valueOf(74), 
            Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84) }));
    tempMap.put(Short.valueOf((short)70), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), Integer.valueOf(74), 
            Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84) }));
    tempMap.put(Short.valueOf((short)80), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), Integer.valueOf(74), 
            Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84) }));
    tempMap.put(Short.valueOf((short)90), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), Integer.valueOf(74), 
            Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84) }));
    tempMap.put(Short.valueOf((short)100), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(65), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), 
            Integer.valueOf(74), Integer.valueOf(75), Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84), Integer.valueOf(85) }));
    tempMap.put(Short.valueOf((short)110), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(65), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), 
            Integer.valueOf(74), Integer.valueOf(75), Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84), Integer.valueOf(85) }));
    tempMap.put(Short.valueOf((short)120), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(65), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), 
            Integer.valueOf(74), Integer.valueOf(75), Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84), Integer.valueOf(85) }));
    tempMap.put(Short.valueOf((short)130), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(65), Integer.valueOf(71), Integer.valueOf(72), Integer.valueOf(73), 
            Integer.valueOf(74), Integer.valueOf(75), Integer.valueOf(81), Integer.valueOf(82), Integer.valueOf(83), Integer.valueOf(84), Integer.valueOf(85) }));
    tempMap = new HashMap<>();
    skillDataMap.put(Byte.valueOf((byte)3), tempMap);
    tempMap.put(Short.valueOf((short)30), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(121), Integer.valueOf(131) }));
    tempMap.put(Short.valueOf((short)40), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(121), Integer.valueOf(122), Integer.valueOf(131), Integer.valueOf(132) }));
    tempMap.put(Short.valueOf((short)50), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(121), Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(131), Integer.valueOf(132), 
            Integer.valueOf(133) }));
    tempMap.put(Short.valueOf((short)60), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(121), Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), 
            Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)70), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(121), Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), 
            Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)80), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(121), Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), 
            Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)90), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(121), Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), 
            Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)100), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(114), Integer.valueOf(125), Integer.valueOf(135), Integer.valueOf(121), 
            Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)110), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(114), Integer.valueOf(125), Integer.valueOf(135), Integer.valueOf(121), 
            Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)120), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(114), Integer.valueOf(125), Integer.valueOf(135), Integer.valueOf(121), 
            Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap.put(Short.valueOf((short)130), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(110), Integer.valueOf(111), Integer.valueOf(112), Integer.valueOf(113), Integer.valueOf(114), Integer.valueOf(125), Integer.valueOf(135), Integer.valueOf(121), 
            Integer.valueOf(122), Integer.valueOf(123), Integer.valueOf(124), Integer.valueOf(131), Integer.valueOf(132), Integer.valueOf(133), Integer.valueOf(134) }));
    tempMap = new HashMap<>();
    skillDataMap.put(Byte.valueOf((byte)4), tempMap);
    tempMap.put(Short.valueOf((short)30), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(171), Integer.valueOf(181) }));
    tempMap.put(Short.valueOf((short)40), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(162), Integer.valueOf(171), Integer.valueOf(172), Integer.valueOf(181), Integer.valueOf(182) }));
    tempMap.put(Short.valueOf((short)50), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(171), Integer.valueOf(172), Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), 
            Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)60), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), 
            Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)70), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), 
            Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)80), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), 
            Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)90), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), 
            Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)100), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(165), Integer.valueOf(175), Integer.valueOf(185), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), 
            Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)110), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(165), Integer.valueOf(175), Integer.valueOf(185), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), 
            Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)120), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(165), Integer.valueOf(175), Integer.valueOf(185), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), 
            Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap.put(Short.valueOf((short)130), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(161), Integer.valueOf(165), Integer.valueOf(175), Integer.valueOf(185), Integer.valueOf(162), Integer.valueOf(163), Integer.valueOf(164), Integer.valueOf(174), 
            Integer.valueOf(184), Integer.valueOf(171), Integer.valueOf(172), Integer.valueOf(173), Integer.valueOf(181), Integer.valueOf(182), Integer.valueOf(183) }));
    tempMap = new HashMap<>();
    skillDataMap.put(Byte.valueOf((byte)5), tempMap);
    tempMap.put(Short.valueOf((short)30), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(221), Integer.valueOf(231) }));
    tempMap.put(Short.valueOf((short)40), Arrays.asList(new Integer[] { Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(211), Integer.valueOf(221), Integer.valueOf(222), Integer.valueOf(231), Integer.valueOf(232) }));
    tempMap.put(Short.valueOf((short)50), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(211), Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), 
            Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)60), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), 
            Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)70), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), 
            Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)80), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), 
            Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)90), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), 
            Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)100), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(214), Integer.valueOf(235), Integer.valueOf(225), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), 
            Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)110), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(214), Integer.valueOf(235), Integer.valueOf(225), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), 
            Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)120), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(214), Integer.valueOf(235), Integer.valueOf(225), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), 
            Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
    tempMap.put(Short.valueOf((short)130), Arrays.asList(new Integer[] { 
            Integer.valueOf(2), Integer.valueOf(501), Integer.valueOf(210), Integer.valueOf(213), Integer.valueOf(214), Integer.valueOf(235), Integer.valueOf(225), Integer.valueOf(224), Integer.valueOf(234), Integer.valueOf(211), 
            Integer.valueOf(212), Integer.valueOf(221), Integer.valueOf(222), Integer.valueOf(223), Integer.valueOf(231), Integer.valueOf(232), Integer.valueOf(233) }));
  }
  
  public static List<Integer> getSkillIds(byte polar, short level) {
    Map<Short, List<Integer>> listMap = skillDataMap.get(Byte.valueOf(polar));
    if (listMap != null) {
      short floorLevel = (short)(level - level % 10);
      return listMap.get(Short.valueOf(floorLevel));
    } 
    return null;
  }
}
