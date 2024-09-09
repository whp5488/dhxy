package com.kitty.rabbit;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import java.io.Serializable;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.DataCache;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import com.kitty.system.game.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.cache.RedisService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DelayReceiver
{
    private static final Logger logger;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PlayerService playerService;
    
    public DelayReceiver() {
    }
    
    @RabbitListener(queues = { "delay_queue" })
    public void orderDelayQueue(String string, Message message, Channel channel) {
        try {
            long id = Long.parseLong(string.split("_")[0]);
            Role role = null;
            if (DataCache.OFFLINE_ROLES.containsKey(id)) {
                role = DataCache.OFFLINE_ROLES.get(id);
            }
            else if (DataCache.ONLINE_ROLES.containsKey(id)) {
                role = DataCache.ONLINE_ROLES.get(id);
            }
            Boolean exists = this.redisService.exists(string);
            if (role.getState() == 1) {
                this.redisService.del(string);
                return;
            }
            if (role != null && exists) {
                this.redisService.del(string);
                this.playerService.onPlayerLogout(role);
            }
        }
        catch (Exception e) {
            e.getMessage();
        }
    }
    
    static {
        logger = LoggerFactory.getLogger(DelayReceiver.class);
    }
}
