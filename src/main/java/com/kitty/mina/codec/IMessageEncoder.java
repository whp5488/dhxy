
package com.kitty.mina.codec;

import com.kitty.mina.message.Message;

public interface IMessageEncoder
{
    byte[] writeMessageBody(final Message p0);
}
