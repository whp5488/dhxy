package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45696)
public class ReqGetCommunityAddress extends Message
{
    private String accountId;
    
    public ReqGetCommunityAddress() {
    }
    
    public String getAccountId() {
        return this.accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
