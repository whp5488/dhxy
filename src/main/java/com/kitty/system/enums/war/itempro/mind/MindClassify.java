package com.kitty.system.enums.war.itempro.mind;

import java.util.Arrays;
import java.util.List;

public interface MindClassify
{
    public static final int CS = 1;
    public static final int SL = 2;
    public static final int DF = 3;
    public static final int ZJ = 4;
    public static final int CS_XIN = 1000;
    public static final int CS_TAN = 1001;
    public static final int CS_CHEN = 1002;
    public static final int CS_CHI = 1003;
    public static final List<Integer> CS_LIST = Arrays.asList(Integer.valueOf(1001), Integer.valueOf(1002), Integer.valueOf(1003));
    public static final int SL_XIN = 2000;
    public static final int SL_SI = 2001;
    public static final int SL_NING = 2002;
    public static final int SL_ZHI = 2003;
    public static final int SL_WANG = 2004;
    public static final int SL_JU = 2005;
    public static final int SL_HEN = 2006;
    public static final int SL_FAN = 2007;
    public static final List<Integer> SL_LIST = Arrays.asList(Integer.valueOf(2001), Integer.valueOf(2002), Integer.valueOf(2003), Integer.valueOf(2004), Integer.valueOf(2005), Integer.valueOf(2006), Integer.valueOf(2007));
    public static final int DF_XIN = 3000;
    public static final int DF_LE = 3001;
    public static final int DF_CHOU = 3002;
    public static final int DF_YUAN = 3003;
    public static final int DF_NAO = 3004;
    public static final int DF_DAI = 3005;
    public static final int DF_SHI = 3006;
    public static final int DF_BEI = 3007;
    public static final List<Integer> DF_LIST = Arrays.asList(Integer.valueOf(3001), Integer.valueOf(3002), Integer.valueOf(3003), Integer.valueOf(3004), Integer.valueOf(3005), Integer.valueOf(3006), Integer.valueOf(3007));
    public static final int ZJ_XIN = 4000;
    public static final int ZJ_NU = 4001;
    public static final int ZJ_AI = 4002;
    public static final int ZJ_XI = 4003;
    public static final int ZJ_E = 4004;
    public static final int ZJ_J = 4005;
    public static final int ZJ_KONG = 4006;
    public static final int ZJ_SI = 4007;
    public static final int ZJ_S = 4008;
    public static final int ZJ_YU = 4009;
    public static final List<Integer> ZJ_LIST = Arrays.asList(Integer.valueOf(4001), Integer.valueOf(4002), Integer.valueOf(4003), Integer.valueOf(4004), Integer.valueOf(4005), Integer.valueOf(4006), Integer.valueOf(4007), Integer.valueOf(4008), Integer.valueOf(4009));
}
