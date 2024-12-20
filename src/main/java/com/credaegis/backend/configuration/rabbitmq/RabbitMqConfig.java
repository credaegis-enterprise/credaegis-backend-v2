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
