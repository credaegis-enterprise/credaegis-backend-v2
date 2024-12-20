package com.credaegis.backend.configuration;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.external.ErrorReceiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {


    @Bean
    Queue errorQueue(){
        return new Queue(Constants.ERROR_QUEUE,true);
    }

    @Bean
    Queue approvalRequestQueue(){
        return new Queue(Constants.APPROVAL_REQUEST_QUEUE,true);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(Constants.DIRECT_EXCHANGE);
    }


    @Bean
    Binding errorBinding(Queue errorQueue,DirectExchange exchange){
        return BindingBuilder.bind(errorQueue).to(exchange).with(Constants.ERROR_QUEUE_KEY);
    }

    @Bean
    Binding approvalRequestBinding(Queue approvalRequestQueue,DirectExchange exchange){
        return BindingBuilder.bind(approvalRequestQueue).to(exchange).with(Constants.APPROVAL_REQUEST_QUEUE_KEY);
    }


}
