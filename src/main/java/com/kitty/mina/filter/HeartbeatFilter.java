
package com.kitty.mina.filter;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.filterchain.IoFilter;
import com.kitty.mina.HeartRecord;
import java.util.Map;
import org.slf4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;

public class HeartbeatFilter extends IoFilterAdapter
{
    private static final Logger log;
    private static final int REGULAR_TIME = 5000;
    private static Map<Long, HeartRecord> heartRecords;
    
    public void messageReceived(final IoFilter.NextFilter nextFilter, final IoSession session, final Object packet) {
        final Message message = (Message)packet;
        final Role role = SessionUtils.getRoleBySession(session);
        if (isHeartBeat(message.getModule(), role)) {
            HeartRecord record = getRecord(role.getId());
            if (record == null) {
                record = new HeartRecord();
                record.setLastTime(System.currentTimeMillis());
                record.setCurrTime(System.currentTimeMillis());
                setRecord(role.getId(), record);
            }
            else {
                record.setLastTime(record.getCurrTime());
                record.setCurrTime(System.currentTimeMillis());
                if (isIllegalHeartBeat(record) && record.addCount()) {
                    final RespOtherLogin respOtherLogin = new RespOtherLogin();
                    respOtherLogin.setContent("检测到使用第三方辅助软件，请正常游戏");
                    respOtherLogin.setResult(2);
                    MessagePusher.pushMessage(role, respOtherLogin);
                    role.getHeartCounts().clear();
                    return;
                }
            }
        }
        nextFilter.messageReceived(session, (Object)message);
    }
    
    private static boolean isHeartBeat(final int moduleId, final Role role) {
        return 4274 == moduleId && role != null;
    }
    
    private static HeartRecord getRecord(final long uid) {
        return HeartbeatFilter.heartRecords.get(uid);
    }
    
    private static void setRecord(final long uid, final HeartRecord record) {
        HeartbeatFilter.heartRecords.put(uid, record);
    }
    
    private static boolean isIllegalHeartBeat(final HeartRecord heartRecord) {
        return heartRecord.getCurrTime() - heartRecord.getLastTime() < 5000L;
    }
    
    public static void cleanHeartBeatRecord(final long uid) {
        HeartbeatFilter.heartRecords.remove(uid);
    }
    
    static {
        log = LoggerFactory.getLogger(HeartbeatFilter.class);
        HeartbeatFilter.heartRecords = new ConcurrentHashMap<Long, HeartRecord>();
    }
}
