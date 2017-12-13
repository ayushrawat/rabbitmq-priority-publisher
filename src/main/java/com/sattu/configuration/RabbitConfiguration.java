package com.sattu.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("rabbitConfig")
public class RabbitConfiguration {
	
	public final static String queueName = "test.queue.with.did.";
	public final static String routingKey = "test.routing.key.";
	
	@Autowired
	ConnectionFactory connectionFactory;
	
	public static String[] queueNames = queues();
	
	@Bean
	List<Queue> queue() {
		Map<String, Object> args = new HashMap<String, Object>();
		List<Queue> queues = new ArrayList<Queue>();
		for(int i=0; i<500; i++) {
			Queue queue = new Queue(queueName+""+i,true,false,false,args);
			queues.add(queue);
		}
		return queues;
	}
	
	@Bean
	TopicExchange exchange() {
		return new TopicExchange("listener.test.exchange");
	}
	
	@Bean
	List<Binding> binding(List<Queue> queues, TopicExchange exchange) {
		List<Binding> bindings = new ArrayList<Binding>();
		for(int i=0; i<500; i++) {
			bindings.add(BindingBuilder.bind(queues.get(i)).to(exchange).with(routingKey+""+i));
		}
		return bindings;
	}
	
	@Bean
	public DirectRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		DirectRabbitListenerContainerFactory srlcf = new DirectRabbitListenerContainerFactory();
		srlcf.setConnectionFactory(connectionFactory);
		srlcf.setPrefetchCount(1);
		srlcf.setConsumersPerQueue(1);
		return srlcf;
	}
	//157532 milliseconds - SMLC
	//15438 milliseconds - DMLC
	
	public static String[] queues() {
		String[] queues = new String[500];
		for(int i=0; i<500; i++) {
			queues[i]=queueName+""+i;
		}
		return queues;
	}

}
