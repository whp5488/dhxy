
package com.kitty.cache;

import java.nio.charset.StandardCharsets;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class KeyExpiredListener implements MessageListener
{
    public void onMessage(final Message message, final byte[] pattern) {
        final String key = new String(message.getBody(), StandardCharsets.UTF_8);
    }
}
