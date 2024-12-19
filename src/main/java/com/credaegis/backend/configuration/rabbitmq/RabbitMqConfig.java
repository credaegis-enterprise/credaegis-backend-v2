package com.credaegis.backend.configuration.rabbitmq;


import com.credaegis.backend.exception.custom.CustomException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig {


    @Value("${rabbitmq.url}")
    private String rabbitMqHost;

    @Bean
    Connection connection(){
            ConnectionFactory factory = new ConnectionFactory();
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setHost(rabbitMqHost);
            try{
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare("ERROR_QUEUE", true, false, false, null);
                channel.queueDeclare("APPROVAL_REQUEST_QUEUE", true, false, false, null);
                channel.close();

                log.info("Rabbit MQ connection established successfully");
                return connection;
            }
            catch (Exception e)
            {
               log.error("Rabbit MQ connection could not be established");
               log.error(e.getMessage());
               e.printStackTrace();
               throw new RuntimeException("Rabbit mq connection error");
            }
    }
}
