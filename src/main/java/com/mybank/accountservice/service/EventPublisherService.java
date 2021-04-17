/*package com.mybank.accountservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.mybank.accountservice.dto.PublisherDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventPublisherService {

	@Autowired
	RabbitTemplate rabbitTemplate;
	@Async
	public void asyncMethodWithVoidReturnType( PublisherDto object) {
		log.info("Pushing Message");
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.convertAndSend("myTestQueueExchange","myTestQueueKey", object);
	}
}
*/