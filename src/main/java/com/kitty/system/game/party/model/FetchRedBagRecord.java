
package com.kitty.system.game.party.model;

public class FetchRedBagRecord
{
    private long roleUid;
    private short coin;
    private int time;
    
    public FetchRedBagRecord() {
    }
    
    public FetchRedBagRecord(final long roleUid, final short coin) {
        this.roleUid = roleUid;
        this.coin = coin;
        this.time = (int)(System.currentTimeMillis() / 1000L);
    }
    
    public long getRoleUid() {
        return this.roleUid;
    }
    
    public short getCoin() {
        return this.coin;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setRoleUid(final long roleUid) {
        this.roleUid = roleUid;
    }
    
    public void setCoin(final short coin) {
        this.coin = coin;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
}
