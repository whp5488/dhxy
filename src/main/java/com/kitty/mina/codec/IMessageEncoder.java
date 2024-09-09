package com.kitty.mina.codec;

import com.kitty.mina.message.Message;

public interface IMessageEncoder
{
    public abstract byte[] writeMessageBody(Message message);
}
