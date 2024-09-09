package com.kitty.system.game.chat.domain;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class RoleOfflineMessage
{
    private Map<Long, List<OfflineMessage>> messageMap;
    
    public RoleOfflineMessage() {
        this.messageMap = new ConcurrentHashMap();
    }
    
    public void add(long sendUid, int sendTime, String msg) {
        List<OfflineMessage> messageList = (List)this.messageMap.computeIfAbsent(Long.valueOf(sendUid), k -> new ArrayList<>());
        messageList.add(new OfflineMessage(sendTime, msg));
    }
    
    public void setMessageMap(Map<Long, List<OfflineMessage>> messageMap) {
        this.messageMap = messageMap;
    }
    
    public Map<Long, List<OfflineMessage>> getMessageMap() {
        return this.messageMap;
    }
}
