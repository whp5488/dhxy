package com.kitty.cross.core.codec;

import com.kitty.mina.codec.CodecContext;
import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionProperties;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class CrossMessageEncoder implements ProtocolEncoder {
  public void dispose(IoSession arg0) throws Exception {}
  
  public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
    CodecContext context = (CodecContext)session.getAttribute(SessionProperties.CODEC_CONTEXT);
    if (context == null) {
      context = new CodecContext();
      session.setAttribute(SessionProperties.CODEC_CONTEXT, context);
    } 
    IoBuffer buffer = writeMessage((Message)message);
    out.write(buffer);
  }
  
  private IoBuffer writeMessage(Message message) {
    IoBuffer buffer = IoBuffer.allocate(1024);
    buffer.setAutoExpand(true);
    IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
    byte[] body = msgEncoder.writeMessageBody(message);
    int metaSize = 3;
    buffer.putInt(body.length + 3);
    int moduleId = message.getModule();
    buffer.putInt(moduleId);
    buffer.put(body);
    buffer.flip();
    return buffer;
  }
}
