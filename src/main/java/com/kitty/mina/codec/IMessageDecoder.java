package com.kitty.mina.codec;

import com.kitty.mina.message.Message;

public interface IMessageDecoder
{
    public abstract Message readMessage(int module, int cmd, byte[] body);
}
