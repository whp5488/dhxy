
package com.kitty.cxf.pool;

public class CxfDataPool
{
    public static int ID;
    
    public static void load(final int id) {
        CxfDataPool.ID = id;
    }
    
    static {
        CxfDataPool.ID = 0;
    }
}
