package com.kitty.mina.codec.xf;

import com.kitty.common.utils.ByteBuffUtil;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.codec.IMessageDecoder;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessageFactory;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class WDDecoder extends CumulativeProtocolDecoder {
  Logger logger = LoggerFactory.getLogger(WDDecoder.class);
  
  protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    if (ioBuffer.remaining() < 12) {
      return false;
    } else {
      ioBuffer.mark();
      int i;
      do {
        if (ioBuffer.remaining() < 12) {
          ioBuffer.reset();
          return false;
        }
        i = ByteBuffUtil.readUnsignedShort(ioBuffer);
      } while(i != 19802);

      ByteBuffUtil.readUnsignedShort(ioBuffer);
      if (ioBuffer.remaining() < 8) {
        ioBuffer.reset();
        return false;
      } else {
        long time = ByteBuffUtil.readUnsignedInt(ioBuffer);
        int length = ByteBuffUtil.readUnsignedShort(ioBuffer);
        if (ioBuffer.remaining() < length) {
          ioBuffer.reset();
          return false;
        } else {
          int moduleId = ioBuffer.getUnsignedShort();
          Class<?> msgClazz = MessageFactory.INSTANCE.getMessage(moduleId, 0);
          if (msgClazz == null) {
            this.logger.warn("未处理封包==协议号={}={}", moduleId, ioBuffer.getHexDump());
            length -= 2;

            for(int j = 0; j < length; ++j) {
              ioBuffer.getUnsigned();
            }

            return true;
          } else {
            HashMap heartbeatMap = SessionManager.INSTANCE.getSessionAttr(ioSession, SessionProperties.HEARTBEAT_MAP, HashMap.class);
            if (heartbeatMap == null) {
              heartbeatMap = new HashMap();
            }

            if (moduleId == 4274) {
              heartbeatMap.put("last_heart_time", time);
            }

            heartbeatMap.put("req_current_time", time);
            ioSession.setAttribute(SessionProperties.HEARTBEAT_MAP, heartbeatMap);
            byte[] body = new byte[length - 2];
            ioBuffer.get(body);
            IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();

            try {
              Message msg = msgDecoder.readMessage(moduleId, 0, body);
              protocolDecoderOutput.write(msg);
              if (4274 != moduleId && 61634 != moduleId) {
                this.logger.info("解析协议:==={}=={}", moduleId, Json.toJson(msg));
              }
            } catch (Exception var14) {
              Role role = SessionUtils.getRoleBySession(ioSession);
              if (role != null) {
                this.logger.error("解包出错: 玩家编号 = {}，协议号 = {}，包体内容 = {}", role.getRoleId(), moduleId, convert16HexStr(body));
              } else {
                this.logger.error("解包出错: 协议号 = {}，包体内容 = {}", moduleId, convert16HexStr(body));
              }
            }

            return true;
          }
        }
      }
    }
  }
  
  public static String convert16HexStr(byte[] b) {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
      if ((b[i] & 0xFF) < 16)
        result.append("0"); 
      result.append(Long.toString((b[i] & 0xFF), 16) + " ");
    } 
    return result.toString().toUpperCase();
  }
}
