package com.credaegis.backend.external;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.NotificationMessageDTO;
import com.credaegis.backend.entity.Notification;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.NotificationRepository;
import com.credaegis.backend.repository.UserRepository;
import com.github.f4b6a3.ulid.UlidCreator;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
@AllArgsConstructor
public class RabbitMqListeners{


    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    @RabbitListener(queues = Constants.NOTIFICATION_QUEUE)
    public void receiveError(NotificationMessageDTO message,@Header(AmqpHeaders.DELIVERY_TAG) long tag,
                             Channel channel) throws IOException {

       try {

           User user = userRepository.findById(message.getUserId()).orElseThrow(ExceptionFactory::resourceNotFound);
           log.info("Notification received for user: " + user.getEmail() + " with message: " + message.getMessage());
           Notification notification = new Notification();
           notification.setId(UlidCreator.getUlid().toString());
           notification.setMessage(message.getMessage());
           notification.setUser(user);
           notification.setType(message.getType());
           notificationRepository.save(notification);
           channel.basicAck(tag,false);
       }
         catch (Exception e){
              log.error("Error in receiving notification: {}",e.getMessage());
                channel.basicNack(tag,false,false);
         }


    }

    @RabbitListener(queues = Constants.APPROVAL_REQUEST_QUEUE)
    public void receiveApprovalRequest(String message,@Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                       Channel channel) throws IOException {


    }

}
