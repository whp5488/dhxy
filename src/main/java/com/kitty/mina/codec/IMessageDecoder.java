
package com.kitty.mina.codec;

import com.kitty.mina.message.Message;

public interface IMessageDecoder
{
    Message readMessage(final int p0, final int p1, final byte[] p2);
}
