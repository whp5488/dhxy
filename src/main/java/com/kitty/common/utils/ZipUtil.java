package com.kitty.common.utils;

import com.kitty.common.utils.base64.Base64Encoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtil {
  public static String compress(String str) {
    if (str == null || str.length() == 0)
      return str; 
    ByteArrayOutputStream out = null;
    GZIPOutputStream gzip = null;
    String compress = "";
    try {
      out = new ByteArrayOutputStream();
      gzip = new GZIPOutputStream(out);
      gzip.write(str.getBytes());
      gzip.close();
      byte[] compressed = out.toByteArray();
      compress = Base64Encoder.encode(compressed);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != out)
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }  
    } 
    return compress;
  }
  
  public static String uncompress(String str) {
    if (str == null || str.length() == 0)
      return str; 
    ByteArrayOutputStream out = null;
    ByteArrayInputStream in = null;
    GZIPInputStream gzip = null;
    String uncompress = "";
    try {
      out = new ByteArrayOutputStream();
      byte[] compressed = Base64Encoder.decode(str.getBytes());
      in = new ByteArrayInputStream(compressed);
      gzip = new GZIPInputStream(in);
      byte[] buffer = new byte[1024];
      int offset = -1;
      while ((offset = gzip.read(buffer)) != -1)
        out.write(buffer, 0, offset); 
      uncompress = out.toString();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != gzip)
        try {
          gzip.close();
        } catch (IOException e) {
          e.printStackTrace();
        }  
      if (null != in)
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }  
      if (null != out)
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }  
    } 
    return uncompress;
  }
}
