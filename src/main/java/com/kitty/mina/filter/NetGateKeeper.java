
package com.kitty.mina.filter;

import com.kitty.common.utils.ConcurrentHashSet;

public class NetGateKeeper
{
    private static NetGateKeeper instance;
    private ConcurrentHashSet<String> forbidProcotols;
    
    public NetGateKeeper() {
        this.forbidProcotols = new ConcurrentHashSet<String>();
    }
    
    public static NetGateKeeper getInstance() {
        return NetGateKeeper.instance;
    }
    
    public void openProcotol(final String messageId) {
        this.forbidProcotols.add(messageId);
    }
    
    public void closeProcotol(final String messageId) {
        this.forbidProcotols.add(messageId);
    }
    
    public boolean canVisit(final String messageId) {
        return !this.forbidProcotols.contains(messageId);
    }
    
    static {
        NetGateKeeper.instance = new NetGateKeeper();
    }
}
