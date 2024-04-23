package com.kitty.rabbit;

import com.kitty.cache.RedisService;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.player.PlayerService;
import com.kitty.system.game.role.domain.Role;
import com.rabbitmq.client.Channel;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelayReceiver {
  private static final Logger logger = LoggerFactory.getLogger(DelayReceiver.class);
  
  @Autowired
  private RedisService redisService;
  
  @Autowired
  private PlayerService playerService;
  
  @RabbitListener(queues = {"delay_queue"})
  public void orderDelayQueue(String string, Message message, Channel channel) {
    try {
      long id = Long.valueOf(string.split("_")[0]).longValue();
      Role role = null;
      if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id))) {
        role = (Role)DataCache.OFFLINE_ROLES.get(Long.valueOf(id));
      } else if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id))) {
        role = (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id));
      } 
      Boolean exists = this.redisService.exists(string);
      if (role.getState() == 1) {
        this.redisService.del(new Serializable[] { string });
        return;
      } 
      if (role != null && exists.booleanValue()) {
        this.redisService.del(new Serializable[] { string });
        this.playerService.onPlayerLogout(role);
      } 
    } catch (Exception e) {
      e.getMessage();
    } 
  }
}
