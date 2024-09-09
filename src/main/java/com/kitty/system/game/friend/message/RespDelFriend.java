package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61542)
public class RespDelFriend extends Message
{
    private int id;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespDelFriend)) {
            return false;
        }
        RespDelFriend other = (RespDelFriend)o;
        return other.canEqual(this) && this.getId() == other.getId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespDelFriend;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespDelFriend(id=" + this.getId() + ")";
    }
    
    public RespDelFriend(final int id) {
        this.id = id;
    }
    
    public RespDelFriend() {
    }
}
