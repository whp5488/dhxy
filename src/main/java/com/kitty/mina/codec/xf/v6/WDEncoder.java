
package com.kitty.mina.codec.xf.v6;

import java.util.HashSet;
import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.SerializerHelper;
import java.util.concurrent.ThreadLocalRandom;
import com.kitty.mina.codec.MinaEncoder;
import org.apache.mina.core.buffer.IoBuffer;
import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.codec.CodecContext;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import java.util.Set;
import org.slf4j.Logger;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class WDEncoder implements ProtocolEncoder
{
    private static final Logger log;
    Logger logger;
    private static Set<Integer> FILTER_MAP;
    
    public WDEncoder() {
        this.logger = LoggerFactory.getLogger(WDEncoder.class);
    }
    
    public void dispose(final IoSession arg0) throws Exception {
    }
    
    public void encode(final IoSession session, final Object message, final ProtocolEncoderOutput out) throws Exception {
        CodecContext context = (CodecContext)session.getAttribute((Object)SessionProperties.CODEC_CONTEXT);
        if (context == null) {
            context = new CodecContext();
            session.setAttribute((Object)SessionProperties.CODEC_CONTEXT, (Object)context);
        }
        final Message msg = (Message)message;
        final IoBuffer buffer = this.writeMessage(msg);
        out.write((Object)buffer);
    }
    
    private IoBuffer writeMessage(final Message message) {
        if (WDEncoder.FILTER_MAP.contains(message.getModule())) {
            return MinaEncoder.writeMessage(message);
        }
        return encryptMsg(message);
    }
    
    public static IoBuffer encryptMsg(final Message message) {
        final int tableIndex = ThreadLocalRandom.current().nextInt(EncryptTable.TABLE.length) + 1;
        final int moduleId = message.getModule();
        final IoBuffer buffer = IoBuffer.allocate(32).setAutoExpand(true).setAutoShrink(true);
        buffer.putUnsignedShort(19802);
        buffer.putUnsignedShort(tableIndex);
        buffer.putUnsignedInt(0);
        final IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
        final byte[] body = msgEncoder.writeMessageBody(message);
        final int posi = body.length + 2;
        buffer.putShort((short)posi);
        final byte p1 = (byte)EncryptTable.TABLE[tableIndex - 1][moduleId >> 8 & 0xFF];
        final byte p2 = (byte)EncryptTable.TABLE[tableIndex - 1][moduleId & 0xFF];
        buffer.put(p1).put(p2);
        for (int i = 0; i < body.length; ++i) {
            body[i] = (byte)EncryptTable.TABLE[tableIndex - 1][body[i] & 0xFF];
        }
        buffer.put(body);
        buffer.flip();
        return buffer;
    }
    
    static {
        log = LoggerFactory.getLogger(WDEncoder.class);
        (WDEncoder.FILTER_MAP = new HashSet<Integer>()).add(45555);
        WDEncoder.FILTER_MAP.add(13143);
    }
}
