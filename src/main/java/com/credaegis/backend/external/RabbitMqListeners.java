package com.credaegis.backend.external;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.NotificationMessageDTO;
import com.credaegis.backend.repository.NotificationRepository;
import com.credaegis.backend.repository.UserRepository;
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


    @RabbitListener(queues = Constants.ERROR_QUEUE)
    public void receiveError(NotificationMessageDTO message,@Header(AmqpHeaders.DELIVERY_TAG) long tag,
                             Channel channel) throws IOException {



    }

    @RabbitListener(queues = Constants.APPROVAL_REQUEST_QUEUE)
    public void receiveApprovalRequest(NotificationMessageDTO message,@Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                       Channel channel) throws IOException {


    }

}
