package com.kitty.cross;

public interface CrossCommands
{
    public static final int CCMD_HEART_BEAT = -1;
    public static final int CMSG_HEART_BEAT = -2;
    public static final int CCMD_CALL_BACK = -3;
    public static final int CMSG_CALL_BACK = -4;
    public static final int G2C_LOGIN_TO_SERVER = -5;
    public static final int C2G_LOGIN_TO_SERVER = -6;
    public static final int G2C_LEAVE_CROSS = -7;
}
