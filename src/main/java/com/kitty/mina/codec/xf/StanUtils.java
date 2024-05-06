package com.kitty.mina.codec.xf;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.mina.core.buffer.IoBuffer;

public class StanUtils {
  public static final String ENCRYPT = ";function decrypt(hex){var temp=Hex2Bytes(hex);var header=temp.slice(0,10);var buff=temp.slice(10,temp.length);var a1=Bytes2Hex(buff.slice(0,2));var a2=Bytes2Hex(header.slice(2,4))+Bytes2Hex(header.slice(8,10));var m=gfParseM(a1,a2);buff[0]=m>>>8;buff[1]=m&0x00FF;var k=Hex2Bytes(gfParseK(a2));var v6=parseInt(Bytes2Hex(header.slice(8,10)),16);var v9=(v6-2)&0xFFFF;var v11=v9&0xFFFFFFFC;if(v9&0xFFFFFFFC){for(var i=0;i<v11;i++){buff[i+2]=buff[i+2]^k[i%4]}}if(v9>v11){do{buff[v11+2]=buff[v11+2]^v11;v11++}while(v9!=v11)}return Bytes2Hex(header)+Bytes2Hex(buff)}function gfParseM(a1,a2){a1=parseInt(a1,16);a2=parseInt(a2,16);return(((((a2&0x00FF0000)>>>0x10)^(a2&0xFF)^((a1&0x0000FF00)>>>8))<<0x8)|((a1&0x000000FF)^(a2&0xFF)))}function gfParseK(a1){a1=parseInt(a1,16);return((((((((((((a1&0x00FF0000)>>>0x10)|((a1&0x000000FF)))<<0x8)|(a1>>>0x18))|((a1&0x000000FF)))<<0x8)|(a1>>>0x18))|((a1&0x00FF0000)>>>0x10))<<0x8)|((a1>>>0x18)^((a1&0x00FF0000)>>>0x10)))>>>0).toString(16)}function Hex2Bytes(str){var pos=0;var len=str.length;if(len%2!=0){return null}len/=2;var arrBytes=[];for(var i=0;i<len;i++){var s=str.substr(pos,2);var v=parseInt(s,16);arrBytes.push(v);pos+=2}return arrBytes}function Bytes2Hex(arr){var str=\"\";for(var i=0;i<arr.length;i++){var tmp;var num=arr[i];if(num<0){tmp=(255+num+1).toString(16)}else{tmp=num.toString(16)}if(tmp.length==1){tmp=\"0\"+tmp}str+=tmp}return str};";
  
  public static byte[] unsignedShortToByte2(int s) {
    byte[] targets = new byte[2];
    targets[0] = (byte)(s >> 8 & 0xFF);
    targets[1] = (byte)(s & 0xFF);
    return targets;
  }
  
  public static byte[] unsignedIntToByte4(long s) {
    byte[] targets = new byte[4];
    targets[0] = (byte)(int)(s >> 24L & 0xFFL);
    targets[1] = (byte)(int)(s >> 16L & 0xFFL);
    targets[2] = (byte)(int)(s >> 8L & 0xFFL);
    targets[3] = (byte)(int)(s & 0xFFL);
    return targets;
  }
  
  public static byte[] intToBytes(int value) {
    byte[] src = new byte[4];
    src[0] = (byte)(value & 0xFF);
    src[1] = (byte)(value >> 8 & 0xFF);
    src[2] = (byte)(value >> 16 & 0xFF);
    src[3] = (byte)(value >> 24 & 0xFF);
    return src;
  }
  
  public static String bytesToHex(byte[] bytes) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(bytes[i] & 0xFF);
      if (hex.length() < 2)
        sb.append(0); 
      sb.append(hex);
    } 
    return sb.toString().toUpperCase();
  }
  
  public static String byteToHex(byte b) {
    String hex = Integer.toHexString(b & 0xFF);
    if (hex.length() < 2)
      hex = "0" + hex; 
    return hex;
  }
  
  public static byte[] hexToByteArray(String inHex) {
    byte[] result;
    int hexlen = inHex.length();
    if (hexlen % 2 == 1) {
      hexlen++;
      result = new byte[hexlen / 2];
      inHex = "0" + inHex;
    } else {
      result = new byte[hexlen / 2];
    } 
    int j = 0;
    for (int i = 0; i < hexlen; i += 2) {
      result[j] = hexToByte(inHex.substring(i, i + 2));
      j++;
    } 
    return result;
  }
  
  public static byte hexToByte(String inHex) {
    return (byte)Integer.parseInt(inHex, 16);
  }
  
  public static byte[] ioBufferToByte(Object message) {
    if (!(message instanceof IoBuffer))
      return null; 
    IoBuffer ioBuffer = (IoBuffer)message;
    byte[] b = new byte[ioBuffer.limit()];
    ioBuffer.get(b);
    return b;
  }
  
  public static int calcAuthKey(int originAuth) {
    return (originAuth & 0xFF00) >> 8 ^ originAuth;
  }
  
  public static int calcXorKey(int authKey) {
    return authKey & 0xFFFF;
  }
  
  public static int calcXorKeyByOriginAuthKey(int originAuth) {
    return calcXorKey(calcAuthKey(originAuth));
  }
  
  public static int calcVal(int xorKey, int val) {
    return xorKey ^ val;
  }
  
  public static int calcX(int x) {
    return x ^ 0x3333;
  }
  
  public static int calcY(int x, int y) {
    return x ^ y;
  }
  
  public static int calcMapId(int x, int y, int mapId) {
    return mapId ^ (x << 8) + y;
  }
  
  public static void main(String[] args) {
    int authKey = 13050799;
    System.out.printf("origin key: 0x%X\n", authKey);
    System.out.printf("auth key: 0x%X\n", calcAuthKey(authKey));
    System.out.printf("xor key: 0x%X\n", calcXorKeyByOriginAuthKey(authKey));
    int xorKey = calcXorKeyByOriginAuthKey(authKey);
    int en = calcVal(xorKey, 8201);
    System.out.printf("val en: 0x%X\n", en);
    System.out.printf("val de: %d\n", calcVal(xorKey, en));
    int x = 84;
    int y = 80;
    int mapId = 1000;
    int xx = calcX(x);
    System.out.printf("val x: 0x%X\n", xx);
    int yy = x ^ y;
    System.out.printf("val x, y, y: 0x%X, 0x%X, 0x%X\n", x, y, yy);
    mapId = calcMapId(x, y, mapId);
    System.out.printf("val mapId: 0x%X\n", mapId);
  }
  
  public static String decrypt(String hex) throws ScriptException, NoSuchMethodException {
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
    engine.eval(";function decrypt(hex){var temp=Hex2Bytes(hex);var header=temp.slice(0,10);var buff=temp.slice(10,temp.length);var a1=Bytes2Hex(buff.slice(0,2));var a2=Bytes2Hex(header.slice(2,4))+Bytes2Hex(header.slice(8,10));var m=gfParseM(a1,a2);buff[0]=m>>>8;buff[1]=m&0x00FF;var k=Hex2Bytes(gfParseK(a2));var v6=parseInt(Bytes2Hex(header.slice(8,10)),16);var v9=(v6-2)&0xFFFF;var v11=v9&0xFFFFFFFC;if(v9&0xFFFFFFFC){for(var i=0;i<v11;i++){buff[i+2]=buff[i+2]^k[i%4]}}if(v9>v11){do{buff[v11+2]=buff[v11+2]^v11;v11++}while(v9!=v11)}return Bytes2Hex(header)+Bytes2Hex(buff)}function gfParseM(a1,a2){a1=parseInt(a1,16);a2=parseInt(a2,16);return(((((a2&0x00FF0000)>>>0x10)^(a2&0xFF)^((a1&0x0000FF00)>>>8))<<0x8)|((a1&0x000000FF)^(a2&0xFF)))}function gfParseK(a1){a1=parseInt(a1,16);return((((((((((((a1&0x00FF0000)>>>0x10)|((a1&0x000000FF)))<<0x8)|(a1>>>0x18))|((a1&0x000000FF)))<<0x8)|(a1>>>0x18))|((a1&0x00FF0000)>>>0x10))<<0x8)|((a1>>>0x18)^((a1&0x00FF0000)>>>0x10)))>>>0).toString(16)}function Hex2Bytes(str){var pos=0;var len=str.length;if(len%2!=0){return null}len/=2;var arrBytes=[];for(var i=0;i<len;i++){var s=str.substr(pos,2);var v=parseInt(s,16);arrBytes.push(v);pos+=2}return arrBytes}function Bytes2Hex(arr){var str=\"\";for(var i=0;i<arr.length;i++){var tmp;var num=arr[i];if(num<0){tmp=(255+num+1).toString(16)}else{tmp=num.toString(16)}if(tmp.length==1){tmp=\"0\"+tmp}str+=tmp}return str};");
    Invocable invocable = (Invocable)engine;
    Object res = invocable.invokeFunction("decrypt", new Object[] { hex });
    return (String)res;
  }
}
