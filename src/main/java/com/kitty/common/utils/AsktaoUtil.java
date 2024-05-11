
package com.kitty.common.utils;

import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.message.Message;
import java.text.DecimalFormat;

public class AsktaoUtil
{
    public static long serverStartTime;
    
    public static String getMoneyFormat(final int needMoney) {
        final DecimalFormat decimalFormat = new DecimalFormat("#,###");
        final String msg = decimalFormat.format(needMoney);
        String color;
        if (msg.length() >= 12) {
            color = "#c009ACD";
        }
        else if (msg.length() >= 11) {
            color = "#cEE2C2C";
        }
        else if (msg.length() >= 10) {
            color = "#cFFFF00";
        }
        else if (msg.length() >= 9) {
            color = "#cBA55DC";
        }
        else if (msg.length() >= 7) {
            color = "#c32CD32";
        }
        else {
            color = "#c6D76B2";
        }
        return color + msg + "#";
    }
    
    public static byte[] getMessageBody(final Message message) {
        final IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
        return msgEncoder.writeMessageBody(message);
    }
    
    static {
        AsktaoUtil.serverStartTime = 8658970L;
    }
}
