package com.credaegis.backend.configuration;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.external.ErrorReceiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sound.midi.Receiver;

@Configuration
public class RabbitMqConfig {


    @Bean
    Queue queue(){
        return new Queue(Constants.ERROR_QUEUE,true);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(Constants.DIRECT_EXCHANGE);
    }


    @Bean
    Binding binding(Queue queue,DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("errors");
    }



    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(Constants.ERROR_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(ErrorReceiver errorReceiver){
        return new MessageListenerAdapter(errorReceiver,"receiveMessage");
    }


}
