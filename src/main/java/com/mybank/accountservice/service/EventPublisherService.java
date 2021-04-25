package com.mybank.accountservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.dto.PublisherDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventPublisherService<T> {

	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	@Async
	public void asyncMethodWithVoidReturnType( PublisherDto<T> object) {
		log.info("Pushing Message : {}",object);
	    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
		rabbitTemplate.convertAndSend("myTestQueueExchange","myTestQueueKey", object);
	}
}
