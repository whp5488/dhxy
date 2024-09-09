package com.kitty.mina.codec.xf;

import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.Message;
import com.kitty.mina.codec.IMessageDecoder;
import com.kitty.mina.cache.SessionUtils;
import org.nutz.json.Json;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.session.SessionManager;
import java.util.HashMap;
import com.kitty.mina.message.MessageFactory;
import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;

public class WDDecoder extends CumulativeProtocolDecoder
{
    Logger logger;
    
    public WDDecoder() {
        this.logger = LoggerFactory.getLogger(WDDecoder.class);
    }
    
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        if (ioBuffer.remaining() >= 12) {
            ioBuffer.mark();
            while (ioBuffer.remaining() >= 12) {
                int i = ByteBuffUtil.readUnsignedShort(ioBuffer);
                if (i == 19802) {
                    ByteBuffUtil.readUnsignedShort(ioBuffer);
                    if (ioBuffer.remaining() < 8) {
                        ioBuffer.reset();
                        return false;
                    }
                    long time = ByteBuffUtil.readUnsignedInt(ioBuffer);
                    int length = ByteBuffUtil.readUnsignedShort(ioBuffer);
                    if (ioBuffer.remaining() < length) {
                        ioBuffer.reset();
                        return false;
                    }
                    int moduleId = ioBuffer.getUnsignedShort();
                    Class<?> msgClazz = MessageFactory.INSTANCE.getMessage(moduleId, 0);
                    if (msgClazz == null) {
                        this.logger.warn("未处理封包==协议号={}={}", Integer.valueOf(moduleId), ioBuffer.getHexDump());
                        length -= 2;
                        for (int j = 0; j < length; ++j) {
                            ioBuffer.getUnsigned();
                        }
                        return true;
                    }
                    HashMap<String, Long> heartbeatMap = (HashMap)SessionManager.INSTANCE.getSessionAttr(ioSession, SessionProperties.HEARTBEAT_MAP, HashMap.class);
                    if (heartbeatMap == null) {
                        heartbeatMap = new HashMap();
                    }
                    if (moduleId == 4274) {
                        heartbeatMap.put("last_heart_time", Long.valueOf(time));
                    }
                    heartbeatMap.put("req_current_time", Long.valueOf(time));
                    ioSession.setAttribute(SessionProperties.HEARTBEAT_MAP, heartbeatMap);
                    byte[] body = new byte[length - 2];
                    ioBuffer.get(body);
                    IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();
                    try {
                        Message msg = msgDecoder.readMessage(moduleId, 0, body);
                        protocolDecoderOutput.write(msg);
                        if (4274 != moduleId && 61634 != moduleId) {
                            this.logger.info("解析协议:==={}=={}", Integer.valueOf(moduleId), Json.toJson(msg));
                        }
                    }
                    catch (Exception e) {
                        Role role = SessionUtils.getRoleBySession(ioSession);
                        if (role != null) {
                            this.logger.error("解包出错: 玩家编号 = {}，协议号 = {}，包体内容 = {}", new Object[] { role.getRoleId(), Integer.valueOf(moduleId), convert16HexStr(body) });
                        }
                        else {
                            this.logger.error("解包出错: 协议号 = {}，包体内容 = {}", Integer.valueOf(moduleId), convert16HexStr(body));
                        }
                    }
                    return true;
                }
                else {
                    continue;
                }
            }
            ioBuffer.reset();
            return false;
        }
        return false;
    }
    
    public static String convert16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            if ((b[i] & 0xFF) < 16) {
                result.append("0");
            }
            result.append(Long.toString((long)(b[i] & 0xFF), 16) + " ");
        }
        return result.toString().toUpperCase();
    }
}
