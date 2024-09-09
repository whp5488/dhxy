package com.kitty.cxf.pool;

public class CxfDataPool
{
    public static int ID;
    
    public CxfDataPool() {
    }
    
    public static void load(int id) {
        CxfDataPool.ID = id;
    }
    
    static {
        CxfDataPool.ID = 0;
    }
}
