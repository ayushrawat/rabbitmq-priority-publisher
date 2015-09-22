package com.sattu.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.support.DefaultAmqpHeaderMapper;

@Configuration
public class RabbitConfiguration {
	
	final static String queueName = "priority-test";
	
	
	@Bean
	Queue queue() {
		Map<String, Object> args = new HashMap<String, Object>();
	    args.put("x-max-priority", 10);
		Queue queue = new Queue(queueName,true,false,false,args);
		return queue;
	}
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange("priority-exchange");
	}
	
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}
	@Bean
	DefaultAmqpHeaderMapper headerMapper() {
		DefaultAmqpHeaderMapper mapper = new DefaultAmqpHeaderMapper();
		mapper.setRequestHeaderNames("priority");
		return mapper;
	}
	

}
