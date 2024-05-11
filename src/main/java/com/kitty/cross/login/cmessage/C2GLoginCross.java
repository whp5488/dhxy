
package com.kitty.cross.login.cmessage;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -6)
public class C2GLoginCross extends Message
{
    int resultCode;
    
    public int getResultCode() {
        return this.resultCode;
    }
    
    public void setResultCode(final int resultCode) {
        this.resultCode = resultCode;
    }
}
