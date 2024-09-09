package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 106)
public class ReqMobile extends Message
{
    private String verificationCode;
    
    public ReqMobile() {
    }
    
    public String getVerificationCode() {
        return this.verificationCode;
    }
    
    public void setVerificationCode(final String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
