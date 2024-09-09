package com.kitty.cross.login.cmessage;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -5)
public class G2CLoginCross extends Message
{
    private byte type;
    private String playerJson;
    
    public G2CLoginCross() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(byte type) {
        this.type = type;
    }
    
    public String getPlayerJson() {
        return this.playerJson;
    }
    
    public void setPlayerJson(String playerJson) {
        this.playerJson = playerJson;
    }
}
