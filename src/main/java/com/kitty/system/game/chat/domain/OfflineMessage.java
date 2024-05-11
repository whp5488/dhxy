
package com.kitty.system.game.chat.domain;

public class OfflineMessage
{
    private int time;
    private String msg;
    
    public int getTime() {
        return this.time;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public OfflineMessage() {
    }
    
    public OfflineMessage(final int time, final String msg) {
        this.time = time;
        this.msg = msg;
    }
}
