package com.kitty.cache;

import java.nio.charset.StandardCharsets;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class KeyExpiredListener implements MessageListener {
  public void onMessage(Message message, byte[] pattern) {
    String key = new String(message.getBody(), StandardCharsets.UTF_8);
  }
}
