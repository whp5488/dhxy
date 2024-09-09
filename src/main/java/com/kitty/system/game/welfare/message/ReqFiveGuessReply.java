package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40996)
public class ReqFiveGuessReply extends Message
{
    private String token;
    
    public ReqFiveGuessReply() {
    }
    
    public String getToken() {
        return this.token;
    }
}
