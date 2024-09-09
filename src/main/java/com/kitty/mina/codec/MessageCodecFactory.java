package com.kitty.mina.codec;

import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.codec.xf.v6.WDEncoder;
import com.kitty.mina.codec.xf.WDDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

public class MessageCodecFactory implements ProtocolCodecFactory
{
    private WDDecoder decoder;
    private WDEncoder encoder;
    
    public MessageCodecFactory() {
        this.decoder = new WDDecoder();
        this.encoder = new WDEncoder();
    }
    
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return this.encoder;
    }
    
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return this.decoder;
    }
}
