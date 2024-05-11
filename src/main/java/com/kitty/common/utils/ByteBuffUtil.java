
package com.kitty.common.utils;

import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;

public final class ByteBuffUtil
{
    private static final Logger logger;
    
    public static boolean readBoolean(final IoBuffer buf) {
        return buf.get() > 0;
    }
    
    public static void writeBoolean(final IoBuffer buf, final boolean value) {
        buf.put((byte)(byte)(value ? 1 : 0));
    }
    
    public static byte readByte(final IoBuffer buf) {
        return buf.get();
    }
    
    public static void writeByte(final IoBuffer buf, final byte value) {
        buf.put(value);
    }
    
    public static char readChar(final IoBuffer buf) {
        return buf.getChar();
    }
    
    public static void writeChar(final IoBuffer buf, final char value) {
        buf.putChar(value);
    }
    
    public static short readShort(final IoBuffer buf) {
        return buf.getShort();
    }
    
    public static int readUnsignedShort(final IoBuffer buf) {
        return buf.getUnsignedShort();
    }
    
    public static void writeShort(final IoBuffer buf, final short value) {
        buf.putShort(value);
    }
    
    public static int readInt(final IoBuffer buf) {
        return buf.getInt();
    }
    
    public static long readUnsignedInt(final IoBuffer buf) {
        return buf.getUnsignedInt();
    }
    
    public static void writeInt(final IoBuffer buf, final int value) {
        buf.putUnsignedInt(value);
    }
    
    public static long readLong(final IoBuffer buf) {
        return buf.getLong();
    }
    
    public static void writeLong(final IoBuffer buf, final long value) {
        buf.putUnsignedInt(value);
    }
    
    public static float readFloat(final IoBuffer buf) {
        return buf.getFloat();
    }
    
    public static void writeFloat(final IoBuffer buf, final float value) {
        buf.putFloat(value);
    }
    
    public static double readDouble(final IoBuffer buf) {
        return buf.getDouble();
    }
    
    public static void writeDouble(final IoBuffer buf, final double value) {
        buf.putDouble(value);
    }
    
    public static String readShortString(final IoBuffer buf) {
        final int strSize = buf.getUnsignedShort();
        final byte[] content = new byte[strSize];
        buf.get(content);
        try {
            return new String(content, "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            ByteBuffUtil.logger.error("", (Throwable)e);
            return "";
        }
    }
    
    public static String readByteString(final IoBuffer buf) {
        final int strSize = buf.getUnsigned();
        final byte[] content = new byte[strSize];
        buf.get(content);
        try {
            return new String(content, "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            ByteBuffUtil.logger.error("", (Throwable)e);
            return "";
        }
    }
    
    public static String readFullString(final IoBuffer buf) {
        final int strSize = buf.getInt();
        final byte[] content = new byte[strSize];
        buf.get(content);
        try {
            return new String(content, "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            ByteBuffUtil.logger.error("", (Throwable)e);
            return "";
        }
    }
    
    public static void writeByteString(final IoBuffer buf, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }
            final byte[] content = msg.getBytes("UTF-8");
            buf.putUnsigned(content.length);
            buf.put(content);
        }
        catch (final UnsupportedEncodingException e) {
            ByteBuffUtil.logger.error("", (Throwable)e);
        }
    }
    
    public static void writeShortString(final IoBuffer buf, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }
            final byte[] content = msg.getBytes("UTF-8");
            buf.putUnsignedShort(content.length);
            buf.put(content);
        }
        catch (final UnsupportedEncodingException e) {
            ByteBuffUtil.logger.error("", (Throwable)e);
        }
    }
    
    public static void writeFullString(final IoBuffer buf, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }
            final byte[] content = msg.getBytes("UTF-8");
            buf.putInt(content.length);
            buf.put(content);
        }
        catch (final UnsupportedEncodingException e) {
            ByteBuffUtil.logger.error("", (Throwable)e);
        }
    }
    
    public static void writeUnsignedByte(final IoBuffer buf, final short value) {
        buf.putUnsigned(value);
    }
    
    public static void writeUnsignedInt(final IoBuffer buf, final long value) {
        buf.putUnsignedInt(value);
    }
    
    public static void writeUnsignedShort(final IoBuffer buf, final int value) {
        buf.putUnsignedShort(value);
    }
    
    static {
        logger = LoggerFactory.getLogger(ByteBuffUtil.class);
    }
}
