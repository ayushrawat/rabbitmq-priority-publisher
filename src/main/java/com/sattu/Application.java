package com.sattu;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
@ImportResource("integration-context2.xml")
public class Application
{	
    public static void main( String[] args ) throws Exception
    {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        //DirectChannel channel = (DirectChannel) ctx.getBean("testChannel");
        //channel.send(MessageBuilder.withPayload("Spring").build());
    }
}
