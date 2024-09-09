package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 403)
public class Resp403 extends Message
{
    public Resp403() {
    }
}
