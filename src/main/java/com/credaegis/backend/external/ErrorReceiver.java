package com.credaegis.backend.external;


import com.credaegis.backend.dto.NotificationMessageDTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ErrorReceiver {


   @RabbitListener(queues = "ERROR_QUEUE")
    public void receiveError(String message) {
        System.out.println("Received error message: " + message);
    }

    @RabbitListener(queues = "APPROVAL_REQUEST_QUEUE")
    public void receiveApprovalRequest(NotificationMessageDTO message,@Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                       Channel channel) throws IOException {
        System.out.println("Received approval request: " + message.getMessage());
        System.out.println("User ID: " + message.getUserId());
        channel.basicNack(tag,false,true);

    }

}
