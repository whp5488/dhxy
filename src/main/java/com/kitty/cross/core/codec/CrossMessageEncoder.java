
package com.kitty.cross.core.codec;

import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.SerializerHelper;
import org.apache.mina.core.buffer.IoBuffer;
import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.codec.CodecContext;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CrossMessageEncoder implements ProtocolEncoder
{
    public void dispose(final IoSession arg0) throws Exception {
    }
    
    public void encode(final IoSession session, final Object message, final ProtocolEncoderOutput out) throws Exception {
        CodecContext context = (CodecContext)session.getAttribute(SessionProperties.CODEC_CONTEXT);
        if (context == null) {
            context = new CodecContext();
            session.setAttribute(SessionProperties.CODEC_CONTEXT, context);
        }
        final IoBuffer buffer = this.writeMessage((Message)message);
        out.write(buffer);
    }
    
    private IoBuffer writeMessage(final Message message) {
        final IoBuffer buffer = IoBuffer.allocate(1024);
        buffer.setAutoExpand(true);
        final IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
        final byte[] body = msgEncoder.writeMessageBody(message);
        final int metaSize = 3;
        buffer.putInt(body.length + 3);
        final int moduleId = message.getModule();
        buffer.putInt(moduleId);
        buffer.put(body);
        buffer.flip();
        return buffer;
    }
}
