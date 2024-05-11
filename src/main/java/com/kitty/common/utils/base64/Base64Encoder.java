
package com.kitty.common.utils.base64;

import org.apache.commons.codec.binary.Base64;

public class Base64Encoder
{
    public static byte[] decode(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }
    
    public static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
}
