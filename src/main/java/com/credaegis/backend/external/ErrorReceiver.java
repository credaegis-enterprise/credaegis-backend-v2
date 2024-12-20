package com.credaegis.backend.external;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ErrorReceiver {


   @RabbitListener(queues = "ERROR_QUEUE")
    public void receiveError(String message) {
        System.out.println("Received error message: " + message);
    }

    @RabbitListener(queues = "APPROVAL_REQUEST_QUEUE")
    public void receiveApprovalRequest(String message) {
        System.out.println("Received approval request: " + message);
    }

}
