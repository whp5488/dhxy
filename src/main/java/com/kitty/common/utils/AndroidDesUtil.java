package com.kitty.common.utils;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.Key;
import java.io.IOException;

public class AndroidDesUtil
{
    public static final String SDK_STR = "{\"loginUrl\":\"http://%s:85\",\"payUrl\":\"http://%s:85\",\"accountUrl\":\"http://%s:85\",\"bbsUrl\":\"http://bbs.leiting.com\",\"helpUrl\":\"http://helper.leiting.com\",\"kernelUrl\":\"http://www.leiting.com\",\"logUrl\":\"http://tplog.leiting.com\",\"urlApis\":[{\"name\":\"login\",\"value\":\"/login/login.php\"},{\"name\":\"checkLogin\",\"value\":\"/login/mobile!mobileCheckLoginV3.php\"},{\"name\":\"fastRegister\",\"value\":\"/login/mobile!fastRegisterV2.action\"},{\"name\":\"payOrder\",\"value\":\"/terrace/phone_charge!createLeitingNo.action\"},{\"name\":\"getAdultInfo\",\"value\":\"/terrace/game_api/getIdCardBindInfo.htm\"}],\"channels\":[{\"name\":\"leiting\",\"value\":\"{\\\"guestLogin\\\":\\\"0\\\",\\\"payLevel\\\":\\\"1\\\",\\\"gmPhoneNum\\\":\\\"0592-3011618\\\"}\"}],\"plugs\":[{\"name\":\"qiyukf\",\"value\":\"{\\\"appKey\\\":\\\"b8c33a8308b536dc4fde7be133fc9835\\\",\\\"groupId\\\":\\\"354781\\\",\\\"robotId\\\":\\\"83373\\\"}\"},{\"name\":\"toutiao\",\"value\":\"{\\\"appId\\\":\\\"151304\\\",\\\"appName\\\":\\\"wendao\\\"}\"},{\"name\":\"wechath5\",\"value\":\"{\\\"payResult\\\":\\\"/terrace/phone_charge!searchResultByLeitingNo.action\\\",\\\"payResultChannel\\\":\\\"/terrace/notify_back!searchResultByLeitingNo.action\\\",\\\"payLoginResult\\\":\\\"/terrace/drm_charge!queryWechatPayResult.action\\\"}\"}],\"resValues\":[{\"name\":\"lt_realname_auth_toast_msg1\",\"value\":\"根据国家法规要求，您的账号信息尚未完善，请尽快完成实名认证以保障账号安全。\"}]}";
    
    public AndroidDesUtil() {
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println(getSdkWd("dx00.hhhwd.com"));
    }
    
    public static String byteArr2HexStr(byte[] paramArrayOfByte) throws Exception {
        int i = 0;
        int k = paramArrayOfByte.length;
        StringBuffer localStringBuffer = new StringBuffer(k * 2);
        while (i < k) {
            int j;
            for (j = paramArrayOfByte[i]; j < 0; j += 256) {}
            if (j < 16) {
                localStringBuffer.append("0");
            }
            localStringBuffer.append(Integer.toString(j, 16));
            ++i;
        }
        return localStringBuffer.toString();
    }
    
    public static String decrypt(String paramString1, String paramString2) {
        Key key = getKey(paramString2.getBytes());
        try {
            paramString1 = new String(decrypt(hexStr2ByteArr(paramString1), key));
            return paramString1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] decrypt(byte[] paramArrayOfByte, Key paramKey) throws Exception {
        Cipher localCipher = Cipher.getInstance("DES");
        localCipher.init(2, paramKey);
        return localCipher.doFinal(paramArrayOfByte);
    }
    
    public static String encrypt(String paramString1, String paramString2) {
        Key key = getKey(paramString2.getBytes());
        try {
            paramString1 = byteArr2HexStr(encrypt(paramString1.getBytes(), key));
            return paramString1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] encrypt(byte[] paramArrayOfByte, Key paramKey) throws Exception {
        Cipher localCipher = Cipher.getInstance("DES");
        localCipher.init(1, paramKey);
        return localCipher.doFinal(paramArrayOfByte);
    }
    
    private static Key getKey(byte[] paramArrayOfByte) {
        byte[] arrayOfByte = new byte[8];
        for (int i = 0; i < paramArrayOfByte.length && i < arrayOfByte.length; ++i) {
            arrayOfByte[i] = paramArrayOfByte[i];
        }
        return new SecretKeySpec(arrayOfByte, "DES");
    }
    
    public static byte[] hexStr2ByteArr(String paramString) throws Exception {
        byte[] bytes = paramString.getBytes();
        int i = 0;
        int j = bytes.length;
        byte[] arrayOfByte = new byte[j / 2];
        while (i < j) {
            String str = new String(bytes, i, 2);
            arrayOfByte[i / 2] = (byte)Integer.parseInt(str, 16);
            i += 2;
        }
        return arrayOfByte;
    }
    
    public static String getSdkWd(String host) {
        return encrypt(String.format("{\"loginUrl\":\"http://%s:85\",\"payUrl\":\"http://%s:85\",\"accountUrl\":\"http://%s:85\",\"bbsUrl\":\"http://bbs.leiting.com\",\"helpUrl\":\"http://helper.leiting.com\",\"kernelUrl\":\"http://www.leiting.com\",\"logUrl\":\"http://tplog.leiting.com\",\"urlApis\":[{\"name\":\"login\",\"value\":\"/login/login.php\"},{\"name\":\"checkLogin\",\"value\":\"/login/mobile!mobileCheckLoginV3.php\"},{\"name\":\"fastRegister\",\"value\":\"/login/mobile!fastRegisterV2.action\"},{\"name\":\"payOrder\",\"value\":\"/terrace/phone_charge!createLeitingNo.action\"},{\"name\":\"getAdultInfo\",\"value\":\"/terrace/game_api/getIdCardBindInfo.htm\"}],\"channels\":[{\"name\":\"leiting\",\"value\":\"{\\\"guestLogin\\\":\\\"0\\\",\\\"payLevel\\\":\\\"1\\\",\\\"gmPhoneNum\\\":\\\"0592-3011618\\\"}\"}],\"plugs\":[{\"name\":\"qiyukf\",\"value\":\"{\\\"appKey\\\":\\\"b8c33a8308b536dc4fde7be133fc9835\\\",\\\"groupId\\\":\\\"354781\\\",\\\"robotId\\\":\\\"83373\\\"}\"},{\"name\":\"toutiao\",\"value\":\"{\\\"appId\\\":\\\"151304\\\",\\\"appName\\\":\\\"wendao\\\"}\"},{\"name\":\"wechath5\",\"value\":\"{\\\"payResult\\\":\\\"/terrace/phone_charge!searchResultByLeitingNo.action\\\",\\\"payResultChannel\\\":\\\"/terrace/notify_back!searchResultByLeitingNo.action\\\",\\\"payLoginResult\\\":\\\"/terrace/drm_charge!queryWechatPayResult.action\\\"}\"}],\"resValues\":[{\"name\":\"lt_realname_auth_toast_msg1\",\"value\":\"根据国家法规要求，您的账号信息尚未完善，请尽快完成实名认证以保障账号安全。\"}]}", host, host, host), "548711fdc20a2129");
    }
}
