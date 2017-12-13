package com.sattu;

import java.io.IOException;
import java.net.URI;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.sattu.configuration.RabbitConfiguration;

@Service("sendMessage")
public class SendMessage {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private TopicExchange exchange;
	
	public void hello(String map) {
		System.out.println("Sending Messages to RabbitMQ...");
		rabbitTemplate.setExchange(exchange.getName());
		for (int j = 0; j < 200; j++) {
			for (int i = 0; i < 500; i++) {
				rabbitTemplate.convertAndSend(RabbitConfiguration.routingKey+i, "messageNumber="+j+"---ToQueue="+i);
			} 
		}
		System.out.println("DONE");
	}
	
	@RabbitListener(queues = "#{T(com.sattu.configuration.RabbitConfiguration).queueNames}" , containerFactory = "rabbitListenerContainerFactory")
	public void hello2(String data, @Header(org.springframework.amqp.support.AmqpHeaders.CONSUMER_QUEUE) String queue) {
		try {
			data=new Date().getTime()+"..."+data+"---FromQueue="+queue.substring(20)+"\n";
			java.nio.file.Files.write(Paths.get(URI.create("file:///Volumes/WorkAndStuff/data/RMQ-withDMLC.log")), data.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
