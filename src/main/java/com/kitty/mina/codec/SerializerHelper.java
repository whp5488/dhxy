
package com.kitty.mina.codec;

import com.kitty.mina.codec.reflect.ReflectEncoder;
import com.kitty.mina.codec.reflect.ReflectDecoder;

public class SerializerHelper
{
    public static volatile SerializerHelper instance;
    private MessageCodecFactory codecFactory;
    private IMessageDecoder decoder;
    private IMessageEncoder encoder;
    
    public static SerializerHelper getInstance() {
        if (SerializerHelper.instance != null) {
            return SerializerHelper.instance;
        }
        synchronized (SerializerHelper.class) {
            if (SerializerHelper.instance == null) {
                (SerializerHelper.instance = new SerializerHelper()).initialize();
            }
        }
        return SerializerHelper.instance;
    }
    
    private void initialize() {
        this.decoder = new ReflectDecoder();
        this.encoder = new ReflectEncoder();
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
