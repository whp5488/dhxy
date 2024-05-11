
package com.kitty.rabbit;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import java.util.Map;
import java.util.HashMap;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig
{
    public static final String DELAY_QUEUE = "player.delay.queue";
    public static final String DELAY_EXCHANGE = "player.delay.exchange";
    public static final String DELAY_ROUTING_KEY = "player_delay";
    public static final String DELAY_QUEUE_NAME = "delay_queue";
    public static final String EXCHANGE_NAME = "exchange";
    public static final String ROUTING_KEY = "player";
    
    @Bean
    public Queue delayOrderQueue() {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("x-dead-letter-exchange", "exchange");
        params.put("x-dead-letter-routing-key", "player");
        return new Queue("player.delay.queue", true, false, false, (Map)params);
    }
    
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange("player.delay.exchange");
    }
    
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(this.delayOrderQueue()).to(this.orderDelayExchange()).with("player_delay");
    }
    
    @Bean
    public Queue delayQueue() {
        return new Queue("delay_queue", true);
    }
    
    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange("exchange");
    }
    
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(this.delayQueue()).to(this.orderTopicExchange()).with("player");
    }
    
    @Bean
    public RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
