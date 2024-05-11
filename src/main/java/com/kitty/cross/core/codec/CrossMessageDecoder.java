
package com.kitty.cross.core.codec;

import com.kitty.mina.message.Message;
import com.kitty.mina.codec.IMessageDecoder;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.codec.CodecContext;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.apache.mina.core.session.AttributeKey;
import org.slf4j.Logger;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;

public class CrossMessageDecoder extends CumulativeProtocolDecoder
{
    Logger logger;
    AttributeKey CODEC_CONTEXT;
    
    public CrossMessageDecoder() {
        this.logger = LoggerFactory.getLogger(CrossMessageDecoder.class);
        this.CODEC_CONTEXT = new AttributeKey(CrossMessageDecoder.class, "CONTEXT_KEY");
    }
    
    protected boolean doDecode(final IoSession session, final IoBuffer in, final ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        CodecContext context = SessionManager.INSTANCE.getSessionAttr(session, this.CODEC_CONTEXT, CodecContext.class);
        if (context == null) {
            context = new CodecContext();
            session.setAttribute(this.CODEC_CONTEXT, context);
        }
        final IoBuffer ioBuffer = context.getBuffer();
        ioBuffer.put(in);
        final IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();
        while (true) {
            ioBuffer.flip();
            final int metaSize = 3;
            if (ioBuffer.remaining() < 3) {
                ioBuffer.compact();
                return false;
            }
            final int length = ioBuffer.getInt();
            if (ioBuffer.remaining() < length) {
                ioBuffer.rewind();
                ioBuffer.compact();
                break;
            }
            final int moduleId = ioBuffer.getInt();
            final int msgbodyLen = length - 3;
            final byte[] body = new byte[msgbodyLen];
            ioBuffer.get(body);
            final Message msg = msgDecoder.readMessage(moduleId, 0, body);
            protocolDecoderOutput.write(msg);
            if (ioBuffer.remaining() == 0) {
                ioBuffer.clear();
                break;
            }
            ioBuffer.compact();
        }
        return true;
    }
    
    public void dispose(final IoSession arg0) throws Exception {
    }
    
    public void finishDecode(final IoSession arg0, final ProtocolDecoderOutput arg1) throws Exception {
    }
}
