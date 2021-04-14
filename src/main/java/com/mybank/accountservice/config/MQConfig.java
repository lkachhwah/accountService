package com.mybank.accountservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	@Value("${accountservice.exchange.name}")
	private String excahngeName;
	
	@Value("${accountservice.topic.name}")
	private String topicName;
	
	@Value("${accountservice.routing.key}")
	private String routingKey;
	
	
	
	 @Bean
	  TopicExchange exchange() {
	    return new TopicExchange(excahngeName);
	  }
	 
	@Bean
	public Queue myQueue() {
		//Time being we have it non durable queue.
	    return new Queue(topicName, false);
	}
	
	 @Bean
	  Binding binding(Queue queue, TopicExchange exchange) {
	    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
	  }
	
}
