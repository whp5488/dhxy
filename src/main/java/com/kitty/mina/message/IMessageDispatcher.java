
package com.kitty.mina.message;

import org.apache.mina.core.session.IoSession;

public interface IMessageDispatcher
{
    void dispatch(final IoSession p0, final Message p1);
}
