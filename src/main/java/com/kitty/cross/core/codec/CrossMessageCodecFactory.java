package com.kitty.cross.core.codec;

import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;

public class CrossMessageCodecFactory implements ProtocolCodecFactory
{
    private CrossMessageDecoder decoder;
    private CrossMessageEncoder encoder;
    
    public CrossMessageCodecFactory() {
        this.decoder = new CrossMessageDecoder();
        this.encoder = new CrossMessageEncoder();
    }
    
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return this.encoder;
    }
    
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return this.decoder;
    }
}
