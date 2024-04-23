package com.kitty.mina.codec;

import com.kitty.mina.codec.xf.WDDecoder;
import com.kitty.mina.codec.xf.v6.WDEncoder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageCodecFactory implements ProtocolCodecFactory {
  private WDDecoder decoder = new WDDecoder();
  
  private WDEncoder encoder = new WDEncoder();
  
  public ProtocolEncoder getEncoder(IoSession session) throws Exception {
    return (ProtocolEncoder)this.encoder;
  }
  
  public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    return (ProtocolDecoder)this.decoder;
  }
}
