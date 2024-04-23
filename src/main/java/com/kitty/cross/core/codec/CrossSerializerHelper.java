package com.kitty.cross.core.codec;

import com.kitty.mina.codec.IMessageDecoder;
import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.reflect.ReflectDecoder;
import com.kitty.mina.codec.reflect.ReflectEncoder;

public class CrossSerializerHelper {
  public static volatile CrossSerializerHelper instance;
  
  private CrossMessageCodecFactory codecFactory;
  
  private IMessageDecoder decoder;
  
  private IMessageEncoder encoder;
  
  public static CrossSerializerHelper getInstance() {
    if (instance != null)
      return instance; 
    synchronized (CrossSerializerHelper.class) {
      if (instance == null) {
        instance = new CrossSerializerHelper();
        instance.initialize();
      } 
    } 
    return instance;
  }
  
  private void initialize() {
    this.decoder = (IMessageDecoder)new ReflectDecoder();
    this.encoder = (IMessageEncoder)new ReflectEncoder();
    this.codecFactory = new CrossMessageCodecFactory();
  }
  
  public CrossMessageCodecFactory getCodecFactory() {
    return this.codecFactory;
  }
  
  public IMessageDecoder getDecoder() {
    return this.decoder;
  }
  
  public IMessageEncoder getEncoder() {
    return this.encoder;
  }
}
