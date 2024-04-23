package com.kitty.mina.codec;

import com.kitty.mina.codec.reflect.ReflectDecoder;
import com.kitty.mina.codec.reflect.ReflectEncoder;

public class SerializerHelper {
  public static volatile SerializerHelper instance;
  
  private MessageCodecFactory codecFactory;
  
  private IMessageDecoder decoder;
  
  private IMessageEncoder encoder;
  
  public static SerializerHelper getInstance() {
    if (instance != null)
      return instance; 
    synchronized (SerializerHelper.class) {
      if (instance == null) {
        instance = new SerializerHelper();
        instance.initialize();
      } 
    } 
    return instance;
  }
  
  private void initialize() {
    this.decoder = (IMessageDecoder)new ReflectDecoder();
    this.encoder = (IMessageEncoder)new ReflectEncoder();
    this.codecFactory = new MessageCodecFactory();
  }
  
  public MessageCodecFactory getCodecFactory() {
    return this.codecFactory;
  }
  
  public IMessageDecoder getDecoder() {
    return this.decoder;
  }
  
  public IMessageEncoder getEncoder() {
    return this.encoder;
  }
}
