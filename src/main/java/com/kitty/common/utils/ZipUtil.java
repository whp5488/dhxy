
package com.kitty.common.utils;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.kitty.common.utils.base64.Base64Encoder;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;

public class ZipUtil
{
    public static String compress(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = null;
        GZIPOutputStream gzip = null;
        String compress = "";
        try {
            out = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            final byte[] compressed = out.toByteArray();
            compress = Base64Encoder.encode(compressed);
        }
        catch (final IOException e) {
            e.printStackTrace();
            if (null != out) {
                try {
                    out.close();
                }
                catch (final IOException e2) {
                    e.printStackTrace();
                }
            }
        }
        finally {
            if (null != out) {
                try {
                    out.close();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return compress;
    }
    
    public static String uncompress(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        GZIPInputStream gzip = null;
        String uncompress = "";
        try {
            out = new ByteArrayOutputStream();
            final byte[] compressed = Base64Encoder.decode(str.getBytes());
            in = new ByteArrayInputStream(compressed);
            gzip = new GZIPInputStream(in);
            final byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = gzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            uncompress = out.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (null != gzip) {
                try {
                    gzip.close();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return uncompress;
    }
}
