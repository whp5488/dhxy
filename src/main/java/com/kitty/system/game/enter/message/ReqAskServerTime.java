package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41008)
public class ReqAskServerTime extends Message
{
    public ReqAskServerTime() {
    }
}
