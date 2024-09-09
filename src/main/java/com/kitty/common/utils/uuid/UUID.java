package com.kitty.common.utils.uuid;

import java.util.concurrent.ThreadLocalRandom;
import cn.hutool.core.exceptions.UtilException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Random;
import java.io.Serializable;

public final class UUID implements Serializable, Comparable<UUID>
{
    private static final long serialVersionUID = -1185015143654744140L;
    private final long mostSigBits;
    private final long leastSigBits;
    
    private UUID(byte[] data) {
        long msb = 0L;
        long lsb = 0L;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; ++i) {
            msb = (msb << 8 | (long)(data[i] & 0xFF));
        }
        for (int i = 8; i < 16; ++i) {
            lsb = (lsb << 8 | (long)(data[i] & 0xFF));
        }
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
        return;
    }
    
    public UUID(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }
    
    public static UUID fastUUID() {
        return randomUUID(false);
    }
    
    public static UUID randomUUID() {
        return randomUUID(true);
    }
    
    public static UUID randomUUID(boolean isSecure) {
        Random ng = isSecure ? UUID.Holder.numberGenerator : getRandom();
        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        byte[] array = randomBytes;
        int n = 6;
        array[n] &= 0xF;
        byte[] array2 = randomBytes;
        int n2 = 6;
        array2[n2] |= 0x40;
        byte[] array3 = randomBytes;
        int n3 = 8;
        array3[n3] &= 0x3F;
        byte[] array4 = randomBytes;
        int n4 = 8;
        array4[n4] |= 0x80;
        return new UUID(randomBytes);
    }
    
    public static UUID nameUUIDFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported");
        }
        byte[] digest;
        byte[] md5Bytes = digest = md.digest(name);
        int n = 6;
        digest[n] &= 0xF;
        byte[] array = md5Bytes;
        int n2 = 6;
        array[n2] |= 0x30;
        byte[] array2 = md5Bytes;
        int n3 = 8;
        array2[n3] &= 0x3F;
        byte[] array3 = md5Bytes;
        int n4 = 8;
        array3[n4] |= 0x80;
        return new UUID(md5Bytes);
    }
    
    public static UUID fromString(String name) {
        String[] components = name.split("-");
        if (components.length != 5) {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        for (int i = 0; i < 5; ++i) {
            components[i] = "0x" + components[i];
        }
        long mostSigBits = (long)Long.decode(components[0]);
        mostSigBits <<= 16;
        mostSigBits |= (long)Long.decode(components[1]);
        mostSigBits <<= 16;
        mostSigBits |= (long)Long.decode(components[2]);
        long leastSigBits = (long)Long.decode(components[3]);
        leastSigBits <<= 48;
        leastSigBits |= (long)Long.decode(components[4]);
        return new UUID(mostSigBits, leastSigBits);
    }
    
    public long getLeastSignificantBits() {
        return this.leastSigBits;
    }
    
    public long getMostSignificantBits() {
        return this.mostSigBits;
    }
    
    public int version() {
        return (int)(this.mostSigBits >> 12 & 0xFL);
    }
    
    public int variant() {
        return (int)(this.leastSigBits >>> (int)(64L - (this.leastSigBits >>> 62)) & this.leastSigBits >> 63);
    }
    
    public long timestamp() throws UnsupportedOperationException {
        this.checkTimeBase();
        return (this.mostSigBits & 0xFFFL) << 48 | (this.mostSigBits >> 16 & 0xFFFFL) << 32 | this.mostSigBits >>> 32;
    }
    
    public int clockSequence() throws UnsupportedOperationException {
        this.checkTimeBase();
        return (int)((this.leastSigBits & 0x3FFF000000000000L) >>> 48);
    }
    
    public long node() throws UnsupportedOperationException {
        this.checkTimeBase();
        return this.leastSigBits & 0xFFFFFFFFFFFFL;
    }
    
    @Override
    public String toString() {
        return this.toString(false);
    }
    
    public String toString(boolean isSimple) {
        StringBuilder builder = new StringBuilder(isSimple ? 32 : 36);
        builder.append(digits(this.mostSigBits >> 32, 8));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.mostSigBits >> 16, 4));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.mostSigBits, 4));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.leastSigBits >> 48, 4));
        if (!isSimple) {
            builder.append('-');
        }
        builder.append(digits(this.leastSigBits, 12));
        return builder.toString();
    }
    
    @Override
    public int hashCode() {
        long hilo = this.mostSigBits ^ this.leastSigBits;
        return (int)(hilo >> 32) ^ (int)hilo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (null == obj || obj.getClass() != UUID.class) {
            return false;
        }
        UUID id = (UUID)obj;
        return this.mostSigBits == id.mostSigBits && this.leastSigBits == id.leastSigBits;
    }
    
    @Override
    public int compareTo(UUID val) {
        return (this.mostSigBits < val.mostSigBits) ? -1 : ((this.mostSigBits > val.mostSigBits) ? 1 : ((this.leastSigBits < val.leastSigBits) ? -1 : ((this.leastSigBits > val.leastSigBits) ? 1 : 0)));
    }
    
    private static String digits(long val, int digits) {
        long hi = 1L << digits * 4;
        return Long.toHexString(hi | (val & hi - 1L)).substring(1);
    }
    
    private void checkTimeBase() {
        if (this.version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        return;
    }
    
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e) {
            throw new UtilException(e);
        }
    }
    
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }
    
    private static class Holder
    {
        static final SecureRandom numberGenerator;
        
        static {
            numberGenerator = UUID.getSecureRandom();
        }
    }
}
