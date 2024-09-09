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
    
    public HeartbeatFilter() {
    }
    
    public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object packet) {
        Message message = (Message)packet;
        Role role = SessionUtils.getRoleBySession(session);
        if (isHeartBeat(message.getModule(), role)) {
            HeartRecord record = getRecord((long)role.getId());
            if (record == null) {
                record = new HeartRecord();
                record.setLastTime(System.currentTimeMillis());
                record.setCurrTime(System.currentTimeMillis());
                setRecord((long)role.getId(), record);
            }
            else {
                record.setLastTime(record.getCurrTime());
                record.setCurrTime(System.currentTimeMillis());
                if (isIllegalHeartBeat(record) && record.addCount()) {
                    RespOtherLogin respOtherLogin = new RespOtherLogin();
                    respOtherLogin.setContent("检测到使用第三方辅助软件，请正常游戏");
                    respOtherLogin.setResult(Integer.valueOf(2));
                    MessagePusher.pushMessage(role, respOtherLogin);
                    role.getHeartCounts().clear();
                    return;
                }
            }
        }
        nextFilter.messageReceived(session, message);
    }
    
    private static boolean isHeartBeat(int moduleId, Role role) {
        return 4274 == moduleId && role != null;
    }
    
    private static HeartRecord getRecord(long uid) {
        return (HeartRecord)HeartbeatFilter.heartRecords.get(Long.valueOf(uid));
    }
    
    private static void setRecord(long uid, HeartRecord record) {
        HeartbeatFilter.heartRecords.put(Long.valueOf(uid), record);
    }
    
    private static boolean isIllegalHeartBeat(HeartRecord heartRecord) {
        return heartRecord.getCurrTime() - heartRecord.getLastTime() < 5000L;
    }
    
    public static void cleanHeartBeatRecord(long uid) {
        HeartbeatFilter.heartRecords.remove(Long.valueOf(uid));
    }
    
    static {
        log = LoggerFactory.getLogger(HeartbeatFilter.class);
        HeartbeatFilter.heartRecords = new ConcurrentHashMap();
    }
}
