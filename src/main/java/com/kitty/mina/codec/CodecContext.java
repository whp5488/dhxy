package com.kitty.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;

public class CodecContext
{
    private IoBuffer buffer;
    
    public CodecContext() {
        this.buffer = IoBuffer.allocate(32).setAutoExpand(true).setAutoShrink(true);
    }
    
    public IoBuffer append(IoBuffer in) {
        this.buffer.put(in);
        return this.buffer;
    }
    
    public IoBuffer getBuffer() {
        return this.buffer;
    }
}
