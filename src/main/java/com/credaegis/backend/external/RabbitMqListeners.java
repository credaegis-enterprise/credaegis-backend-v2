package com.credaegis.backend.external;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ApprovalBlockchainDTO;
import com.credaegis.backend.dto.NotificationMessageDTO;
import com.credaegis.backend.entity.*;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ApprovalRepository;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.repository.NotificationRepository;
import com.credaegis.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;


@Component
@Slf4j
@AllArgsConstructor
public class RabbitMqListeners {


    private final NotificationRepository notificationRepository;
    private final ApprovalRepository approvalRepository;
    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;


//    @RabbitListener(queues = Constants.NOTIFICATION_QUEUE)
//    public void receiveError(NotificationMessageDTO message, @Header(AmqpHeaders.DELIVERY_TAG) long tag,
//
//                             Channel channel) throws IOException {
//
//        try {
//
//            User user = userRepository.findById(message.getUserId()).orElseThrow(ExceptionFactory::resourceNotFound);
//            log.info("Notification received for user: " + user.getEmail() + " with message: " + message.getMessage());
//            Notification notification = new Notification();
//            notification.setId(UlidCreator.getUlid().toString());
//            notification.setMessage(message.getMessage());
//            notification.setUser(user);
//            notification.setType(message.getType());
//            notification.setTimestamp(message.getTimestamp());
//            notificationRepository.save(notification);
//            channel.basicAck(tag, false);
//        } catch (Exception e) {
//            log.error("Error in receiving notification: {}", e.getMessage());
//            channel.basicNack(tag, false, false);
//        }
//
//
//    }


    //on-chain approval
    @RabbitListener(queues = Constants.APPROVAL_RESPONSE_QUEUE)
    public void receiveApprovalRequest(ApprovalBlockchainDTO message, @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                       Channel channel,@Header(AmqpHeaders.CONSUMER_TAG) String consumerTag
                                       ) throws IOException {

        try {

            Approval approval = approvalRepository.findById(message.getApprovalId()).orElseThrow(ExceptionFactory::resourceNotFound);
            User user = userRepository.findById(message.getUserId()).orElseThrow(ExceptionFactory::resourceNotFound);
            if (!message.getStored()) {
                String errorMessage = "Approval request for certificate with name: " +
                        approval.getApprovalCertificateName() + " of the recipient: " +
                        approval.getRecipientName() + "," + "" + approval.getRecipientEmail() +
                        " is already issued and checksum found, rejected this certificate.";

                approval.setStatus(Status.rejected);
                Notification notification = new Notification();
                notification.setId(UlidCreator.getUlid().toString());
                notification.setMessage(errorMessage);
                notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
                notification.setUser(user);
                notification.setType("duplicate_certificate");
                notificationRepository.save(notification);
                notificationRepository.save(notification);
                approvalRepository.save(approval);

            }

            else{

            Certificate certificate = new Certificate();
            certificate.setId(UlidCreator.getUlid().toString());
            certificate.setCertificateName(approval.getApprovalCertificateName());
            certificate.setCertificateHash(message.getHash());
            certificate.setComments(approval.getComments());
            certificate.setRecipientName(approval.getRecipientName());
            certificate.setRecipientEmail(approval.getRecipientEmail());
            certificate.setIssuedDate(new Date(System.currentTimeMillis()));
            certificate.setEvent(approval.getEvent());
            certificate.setIssuedByUser(user);
            approval.setStatus(Status.approved);

            approvalRepository.save(approval);
            certificateRepository.save(certificate);

            }

            channel.basicAck(tag, false);


        } catch (Exception e) {
            log.error("Error in receiving approval request: {}", e.getMessage());
            e.printStackTrace();
            channel.basicNack(tag, false, false);
        }

    }

}
