package com.kitty.mina.codec;

import com.kitty.mina.message.Message;

public interface IMessageDecoder {
  Message readMessage(int paramInt1, int paramInt2, byte[] paramArrayOfbyte);
}
