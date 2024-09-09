package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20645)
public class ReqExtendStoreSize extends Message
{
    public int type;
    public Integer isStore;
    
    public ReqExtendStoreSize() {
    }
}
