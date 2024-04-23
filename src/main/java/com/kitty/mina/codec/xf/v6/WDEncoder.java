package com.kitty.mina.codec.xf.v6;

import com.kitty.mina.codec.CodecContext;
import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.MinaEncoder;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionProperties;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WDEncoder implements ProtocolEncoder {
  private static final Logger log = LoggerFactory.getLogger(WDEncoder.class);
  
  Logger logger = LoggerFactory.getLogger(WDEncoder.class);
  
  private static Set<Integer> FILTER_MAP = new HashSet<>();
  
  static {
    FILTER_MAP.add(Integer.valueOf(45555));
    FILTER_MAP.add(Integer.valueOf(13143));
  }
  
  public void dispose(IoSession arg0) throws Exception {}
  
  public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
    CodecContext context = (CodecContext)session.getAttribute(SessionProperties.CODEC_CONTEXT);
    if (context == null) {
      context = new CodecContext();
      session.setAttribute(SessionProperties.CODEC_CONTEXT, context);
    } 
    Message msg = (Message)message;
    IoBuffer buffer = writeMessage(msg);
    out.write(buffer);
  }
  
  private IoBuffer writeMessage(Message message) {
    if (FILTER_MAP.contains(Integer.valueOf(message.getModule())))
      return MinaEncoder.writeMessage(message); 
    return encryptMsg(message);
  }
  
  public static IoBuffer encryptMsg(Message message) {
    int tableIndex = ThreadLocalRandom.current().nextInt(EncryptTable.TABLE.length) + 1;
    int moduleId = message.getModule();
    IoBuffer buffer = IoBuffer.allocate(32).setAutoExpand(true).setAutoShrink(true);
    buffer.putUnsignedShort(19802);
    buffer.putUnsignedShort(tableIndex);
    buffer.putUnsignedInt(0);
    IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
    byte[] body = msgEncoder.writeMessageBody(message);
    int posi = body.length + 2;
    buffer.putShort((short)posi);
    byte p1 = (byte)EncryptTable.TABLE[tableIndex - 1][moduleId >> 8 & 0xFF];
    byte p2 = (byte)EncryptTable.TABLE[tableIndex - 1][moduleId & 0xFF];
    buffer.put(p1).put(p2);
    for (int i = 0; i < body.length; i++)
      body[i] = (byte)EncryptTable.TABLE[tableIndex - 1][body[i] & 0xFF]; 
    buffer.put(body);
    buffer.flip();
    return buffer;
  }
}
