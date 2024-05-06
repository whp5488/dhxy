package com.kitty.system.game.chat.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public class RoleOfflineMessage {
  private Map<Long, List<OfflineMessage>> messageMap = new ConcurrentHashMap<>();
  
  public void add(long sendUid, int sendTime, String msg) {
    List<OfflineMessage> messageList = this.messageMap.computeIfAbsent(sendUid, k -> new ArrayList<>());
    messageList.add(new OfflineMessage(sendTime, msg));
  }

}
