package com.credaegis.backend.configuration.rabbitmq;


import com.credaegis.backend.constant.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {


    //not using listener container factory instead using annotation based listener
    //if needed different config for each listener then use multiple listener container factor

    @Bean
    Queue notificationQueue(){
        return new Queue(Constants.NOTIFICATION_QUEUE,true);
    }

    @Bean
    Queue approvalRequestQueue(){
        return new Queue(Constants.APPROVAL_REQUEST_QUEUE,true);
    }

    @Bean
    Queue approvalResponseQueue(){
        return new Queue(Constants.APPROVAL_RESPONSE_QUEUE,true);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(Constants.DIRECT_EXCHANGE);
    }


    @Bean
    Binding approvalResponseBinding(Queue approvalResponseQueue,DirectExchange exchange){
        return BindingBuilder.bind(approvalResponseQueue).to(exchange).with(Constants.APPROVAL_RESPONSE_QUEUE_KEY);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue,DirectExchange exchange){
        return BindingBuilder.bind(notificationQueue).to(exchange).with(Constants.NOTIFICATION_QUEUE_KEY);
    }

    @Bean
    Binding approvalRequestBinding(Queue approvalRequestQueue,DirectExchange exchange){
        return BindingBuilder.bind(approvalRequestQueue).to(exchange).with(Constants.APPROVAL_REQUEST_QUEUE_KEY);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }



    //sender configuration
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


}
