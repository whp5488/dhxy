package com.kitty.system.game.vo.utils;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StatusConst {
  public static final int STATUS_CONFUSION = 1002;
  
  public static final int STATUS_FROZEN = 1003;
  
  public static final int STATUS_SLEEP = 1004;
  
  public static final int STATUS_POISON = 1005;
  
  public static final int STATUS_FORGOTTEN = 1006;
  
  public static final int STATUS_CHARM = 1007;
  
  public static final int STATUS_PHY_POWER_UP = 1008;
  
  public static final int STATUS_DEF_UP = 1009;
  
  public static final int STATUS_SPEED_UP = 1010;
  
  public static final int STATUS_CLOAKING = 1011;
  
  public static final int STATUS_1012 = 1012;
  
  public static final int STATUS_1013 = 1013;
  
  public static final int STATUS_1014 = 1014;
  
  public static final int STATUS_1015 = 1015;
  
  public static final int STATUS_SPEED_CUT = 1016;
  
  public static final int STATUS_1018 = 1018;
  
  public static final int STATUS_1019 = 1019;
  
  public static final int STATUS_1020 = 1020;
  
  public static final int STATUS_1021 = 1021;
  
  public static final int STATUS_RECOVER_LIFE = 1022;
  
  public static final int STATUS_1023 = 1023;
  
  public static final int STATUS_1024 = 1024;
  
  public static final int STATUS_1048 = 1048;
  
  public static final int STATUS_1052 = 1052;
  
  public static final int STATUS_1053 = 1053;
  
  public static final int STATUS_1054 = 1054;
  
  public static final int STATUS_1055 = 1055;
  
  public static final int STATUS_1059 = 1059;
  
  public static final int STATUS_1060 = 1060;
  
  public static final int STATUS_1061 = 1061;
  
  public static final int STATUS_1062 = 1062;
  
  public static final int STATUS_1064 = 1064;
  
  public static final int STATUS_1065 = 1065;
  
  public static final int STATUS_2009 = 2009;
  
  public static final int STATUS_2010 = 2010;
  
  public static final int STATUS_2011 = 2011;
  
  public static final int STATUS_2012 = 2012;
  
  public static final int STATUS_2031 = 2031;
  
  public static final int STATUS_2032 = 2032;
  
  public static final int STATUS_2035 = 2035;
  
  public static final int STATUS_2038 = 2038;
  
  public static final int STATUS_2041 = 2041;
  
  public static final int STATUS_2042 = 2042;
  
  public static final int STATUS_2043 = 2043;
  
  public static final int STATUS_2045 = 2045;
  
  public static final int STATUS_2046 = 2046;
  
  public static final int STATUS_2050 = 2050;
  
  public static final int STATUS_2051 = 2051;
  
  public static final int STATUS_2057 = 2057;
  
  public static final int STATUS_2058 = 2058;
  
  public static final int STATUS_2059 = 2059;
  
  public static final int STATUS_2060 = 2060;
  
  public static final int STATUS_2062 = 2062;
  
  public static final int STATUS_2063 = 2063;
  
  public static final int STATUS_2064 = 2064;
  
  public static final int STATUS_2065 = 2065;
  
  public static final int STATUS_2066 = 2066;
  
  public static final int STATUS_2067 = 2067;
  
  public static final int STATUS_2070 = 2070;
  
  public static final int STATUS_2077 = 2077;
  
  public static final int STATUS_2078 = 2078;
  
  public static final int STATUS_2079 = 2079;
  
  public static final int STATUS_2080 = 2080;
  
  public static final int STATUS_2081 = 2081;
  
  public static final int STATUS_2082 = 2082;
  
  public static final int STATUS_2083 = 2083;
  
  public static final int STATUS_2084 = 2084;
  
  public static final int STATUS_2085 = 2085;
  
  public static final int STATUS_2086 = 2086;
  
  public static final int STATUS_2087 = 2087;
  
  public static final int STATUS_2088 = 2088;
  
  public static final int STATUS_2089 = 2089;
  
  public static final int STATUS_2090 = 2090;
  
  public static final int STATUS_2103 = 2103;
  
  public static final int STATUS_2104 = 2104;
  
  public static final int STATUS_2169 = 2169;
  
  public static final int STATUS_2170 = 2170;
  
  public static final int STATUS_2171 = 2171;
  
  public static final int STATUS_2172 = 2172;
  
  public static final int STATUS_2173 = 2173;
  
  public static final int STATUS_2174 = 2174;
  
  public static final int STATUS_2175 = 2175;
  
  public static final int STATUS_2177 = 2177;
  
  public static final int STATUS_2189 = 2189;
  
  public static final int STATUS_3001 = 3001;
  
  public static final int STATUS_3002 = 3002;
  
  public static final int STATUS_3003 = 3003;
  
  public static final int STATUS_3004 = 3004;
  
  public static final int STATUS_3005 = 3005;
  
  public static final int STATUS_3006 = 3006;
  
  public static final int STATUS_3007 = 3007;
  
  public static final int STATUS_3008 = 3008;
  
  public static final int STATUS_5002 = 5002;
  
  public static final int STATUS_6001 = 6001;
  
  public static final int STATUS_6002 = 6002;
  
  public static final int STATUS_6004 = 6004;
  
  public static final int STATUS_6006 = 6006;
  
  public static final int STATUS_6007 = 6007;
  
  public static final int STATUS_6008 = 6008;
  
  public static final int STATUS_6010 = 6010;
  
  public static final int STATUS_6011 = 6011;
  
  public static final int STATUS_6012 = 6012;
  
  public static final int STATUS_6015 = 6015;
  
  public static final int STATUS_6016 = 6016;
  
  public static final int STATUS_6017 = 6017;
  
  public static final int STATUS_6019 = 6019;
  
  public static final int STATUS_6020 = 6020;
  
  public static final int STATUS_6021 = 6021;
  
  public static final int STATUS_6024 = 6024;
  
  public static final int STATUS_6026 = 6026;
  
  public static final int STATUS_6027 = 6027;
  
  public static final int STATUS_6028 = 6028;
  
  public static final int STATUS_6029 = 6029;
  
  public static final int STATUS_6100 = 6100;
  
  public static final int STATUS_8005 = 8005;
  
  public static final int STATUS_8006 = 8006;
  
  public static final int STATUS_8007 = 8007;
  
  public static final int STATUS_8008 = 8008;
  
  public static final int STATUS_8009 = 8009;
  
  public static final int STATUS_8010 = 8010;
  
  public static final int STATUS_8011 = 8011;
  
  public static final int STATUS_20011 = 20011;
  
  public static final int STATUS_50068 = 50068;
  
  public static final int STATUS_67317 = 67317;
  
  public static final int STATUS_81412 = 81412;
  
  public static final int STATUS_85005 = 85005;
  
  public static final int STATUS_85006 = 85006;
  
  public static final int STATUS_85007 = 85007;
  
  public static final int STATUS_85010 = 85010;
  
  public static final int STATUS_91001 = 91001;
  
  public static final int STATUS_91002 = 91002;
  
  public static final int STATUS_91003 = 91003;
  
  public static final int STATUS_940003 = 940003;
  
  public static final int STATUS_950200 = 950200;
  
  public static final int STATUS_961129 = 961129;
  
  public static final int CLEAR_ALL = 19999;
  
  public static final int STATUS_DODGE_UP = 17;
  
  public static final int FURY = 100;
  
  public static final int PENETRATE = 101;
  
  public static final int DOUBLEHIT = 102;
  
  public static final int STATUS_80011 = 105;
  
  public static final int STATUS_80020 = 115;
  
  public static final int STATUS_80023 = 116;
  
  public static final int STATUS_81002 = 117;
  
  public static final int STATUS_324501 = 118;
  
  public static final int STATUS_FRIEND_SIDE = 119;
  
  public static final int STATUS_83039 = 120;
  
  public static final int PROTECT_83029 = 106;
  
  public static final int BACK_HURT = 107;
  
  public static final int RECOVER_SHAPESHIFT = 200;
  
  public static final int MAGIC = 100000;
  
  public static final int UNUSUAL = 1999;
  
  public static final int LIFE_VISIBLE = 113;
  
  public static final int MANA_VISIBLE = 114;
  
  static List<Integer> getAnomalousStatus() {
    return Arrays.asList(new Integer[] { Integer.valueOf(1002), Integer.valueOf(1003), Integer.valueOf(1004), 
          Integer.valueOf(1006), Integer.valueOf(1019) });
  }
  
  static List<Integer> getControlStatus() {
    return Arrays.asList(new Integer[] { Integer.valueOf(1002), Integer.valueOf(1003), Integer.valueOf(1004), 
          Integer.valueOf(1006) });
  }
  
  static List<Integer> getWuxingStatus() {
    return Arrays.asList(new Integer[] { Integer.valueOf(2062), Integer.valueOf(2063), Integer.valueOf(2064), 
          Integer.valueOf(2065), Integer.valueOf(2066), 
          Integer.valueOf(2077), Integer.valueOf(2078), Integer.valueOf(2079), 
          Integer.valueOf(2080), Integer.valueOf(2081) });
  }
  
  static List<Integer> getPositiveStatus() {
    return Arrays.asList(new Integer[] { Integer.valueOf(1008), Integer.valueOf(1009), Integer.valueOf(1010), Integer.valueOf(1011), 
          Integer.valueOf(1018), Integer.valueOf(1022), Integer.valueOf(1023), Integer.valueOf(2010), Integer.valueOf(2031) });
  }
  
  static List<Integer> getMiniPositiveStatus() {
    return Arrays.asList(new Integer[] { Integer.valueOf(1008), Integer.valueOf(1009), Integer.valueOf(1010) });
  }
  
  static Map<Integer, Integer> getWuxinPro() {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(Integer.valueOf(2062), Integer.valueOf(PRO_DICT_TYPE.WX_J.getDist()));
    map.put(Integer.valueOf(2063), Integer.valueOf(PRO_DICT_TYPE.WX_M.getDist()));
    map.put(Integer.valueOf(2064), Integer.valueOf(PRO_DICT_TYPE.WX_S.getDist()));
    map.put(Integer.valueOf(2065), Integer.valueOf(PRO_DICT_TYPE.WX_H.getDist()));
    map.put(Integer.valueOf(2066), Integer.valueOf(PRO_DICT_TYPE.WX_T.getDist()));
    map.put(Integer.valueOf(2077), Integer.valueOf(PRO_DICT_TYPE.WX_J.getDist()));
    map.put(Integer.valueOf(2078), Integer.valueOf(PRO_DICT_TYPE.WX_M.getDist()));
    map.put(Integer.valueOf(2079), Integer.valueOf(PRO_DICT_TYPE.WX_S.getDist()));
    map.put(Integer.valueOf(2080), Integer.valueOf(PRO_DICT_TYPE.WX_H.getDist()));
    map.put(Integer.valueOf(2081), Integer.valueOf(PRO_DICT_TYPE.WX_T.getDist()));
    return map;
  }
}
