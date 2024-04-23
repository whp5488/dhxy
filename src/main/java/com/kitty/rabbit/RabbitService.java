package com.kitty.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitService {
  private static final Logger logger = LoggerFactory.getLogger(RabbitService.class);
  
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private RabbitAdmin rabbitAdmin;
  
  public void sendMessage(String msg, Integer time) {
    this.rabbitTemplate.convertAndSend("player.delay.exchange", "player_delay", msg, message -> {
          message.getMessageProperties().setExpiration(time + "");
          return message;
        });
  }
  
  public void purgeDelay() {
    this.rabbitAdmin.purgeQueue("player.delay.queue", false);
    this.rabbitAdmin.purgeQueue("delay_queue", false);
  }
}
