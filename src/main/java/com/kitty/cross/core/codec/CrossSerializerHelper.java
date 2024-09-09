package com.kitty.cross.core.codec;

import com.kitty.mina.codec.reflect.ReflectEncoder;
import com.kitty.mina.codec.reflect.ReflectDecoder;
import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.IMessageDecoder;

public class CrossSerializerHelper
{
    public static volatile CrossSerializerHelper instance;
    private CrossMessageCodecFactory codecFactory;
    private IMessageDecoder decoder;
    private IMessageEncoder encoder;
    
    public CrossSerializerHelper() {
    }
    
    public static CrossSerializerHelper getInstance() {
        if (CrossSerializerHelper.instance != null) {
            return CrossSerializerHelper.instance;
        }
        synchronized (CrossSerializerHelper.class) {
            if (CrossSerializerHelper.instance == null) {
                (CrossSerializerHelper.instance = new CrossSerializerHelper()).initialize();
            }
        }
        return CrossSerializerHelper.instance;
    }
    
    private void initialize() {
        this.decoder = new ReflectDecoder();
        this.encoder = new ReflectEncoder();
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
