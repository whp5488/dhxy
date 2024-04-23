package com.kitty.mina.codec;

import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionProperties;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaEncoder implements ProtocolEncoder {
  static Logger logger = LoggerFactory.getLogger(MinaEncoder.class);
  
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
  
  public static IoBuffer writeMessage(Message message) {
    int moduleId = message.getModule();
    IoBuffer buffer = IoBuffer.allocate(32).setAutoExpand(true).setAutoShrink(true);
    buffer.putUnsignedInt(1297743872);
    buffer.putUnsignedInt(0);
    IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
    byte[] body = msgEncoder.writeMessageBody(message);
    int posi = 0;
    if (moduleId == 64977) {
      posi = body.length + 4;
    } else {
      posi = body.length + 2;
    } 
    buffer.putShort((short)posi);
    buffer.putUnsignedShort(moduleId);
    if (moduleId == 64977)
      buffer.putShort((short)((short)body.length + 2)); 
    buffer.put(body);
    buffer.flip();
    buffer.rewind();
    return buffer;
  }
}
