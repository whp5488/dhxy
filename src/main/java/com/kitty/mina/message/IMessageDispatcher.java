package com.kitty.mina.message;

import org.apache.mina.core.session.IoSession;

public interface IMessageDispatcher
{
    public abstract void dispatch(IoSession session, Message message);
}
