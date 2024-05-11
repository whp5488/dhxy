
package com.kitty.system.game.role.message;

import com.kitty.common.utils.CodeUtils;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 105)
public class ReqVerificationCode extends Message
{
    private String mobile;
    
    public ReqVerificationCode() {
    }
    
    public ReqVerificationCode(final String mobile) {
        this.mobile = CodeUtils.phoneMask(mobile);
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
}
