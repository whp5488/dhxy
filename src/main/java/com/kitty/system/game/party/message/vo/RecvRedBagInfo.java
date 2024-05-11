
package com.kitty.system.game.party.message.vo;

public class RecvRedBagInfo
{
    private String name;
    private int coin;
    private int time;
    
    public String getName() {
        return this.name;
    }
    
    public int getCoin() {
        return this.coin;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setCoin(final int coin) {
        this.coin = coin;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
}
