package com.credaegis.backend.async;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.ApprovalBlockchainDTO;
import com.credaegis.backend.dto.RevocationBlockchainDTO;
import com.credaegis.backend.entity.*;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.ApprovalRepository;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.repository.NotificationRepository;
import com.credaegis.backend.repository.UserRepository;
import com.credaegis.backend.utility.EmailUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.UlidCreator;
import com.rabbitmq.client.Channel;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;


@Component
@Slf4j
@AllArgsConstructor
public class RabbitMqListeners {


    private final NotificationRepository notificationRepository;
    private final ApprovalRepository approvalRepository;
    private final CertificateRepository certificateRepository;
    private final MinioClient minioClient;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final EmailUtility emailUtility;


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


    @Transactional
    @RabbitListener(queues = Constants.CERTIFICATE_REVOKE_RESPONSE_QUEUE)
    public void receiveRevokeRequest(RevocationBlockchainDTO message, @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                     Channel channel, @Header(AmqpHeaders.CONSUMER_TAG) String consumerTag)
            throws IOException {

        try {

            Certificate certificate = certificateRepository.findByCertificateHash(message.getHash()).orElseThrow(ExceptionFactory::resourceNotFound);
            User user = userRepository.findById(message.getRevokerId()).orElseThrow(ExceptionFactory::resourceNotFound);
            if (!message.getRevoked()) {
                String errorMessage = "Revocation request for certificate with name: " +
                        certificate.getCertificateName() + "of the recipient " +
                        certificate.getRecipientName() + ", " + certificate.getRecipientEmail() +
                        "is already revoked";
                Notification notification = new Notification();
                notification.setId(UlidCreator.getUlid().toString());
                notification.setMessage(errorMessage);
                notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
                notification.setUser(user);
                notification.setType(NotificationType.ERROR);
                notificationRepository.save(notification);
                notificationRepository.save(notification);
            } else {

                certificate.setRevoked(true);
                certificate.setStatus(CertificateStatus.revoked);
                certificateRepository.save(certificate);

            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            //dead letter queue
            log.error("Error in processing revocation request: {}", e);
            channel.basicNack(tag, false, false);
        }

    }


    //on-chain approval
    @Transactional
    @RabbitListener(queues = Constants.APPROVAL_RESPONSE_QUEUE)
    public void receiveApprovalRequest(ApprovalBlockchainDTO message, @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                       Channel channel, @Header(AmqpHeaders.CONSUMER_TAG) String consumerTag
    ) throws IOException {

        try {

            Approval approval = approvalRepository.findById(message.getApprovalId()).orElseThrow(ExceptionFactory::resourceNotFound);
            User user = userRepository.findById(message.getUserId()).orElseThrow(ExceptionFactory::resourceNotFound);
            if (!message.getStored()) {
                String errorMessage = "Approval request for certificate with name: " +
                        approval.getApprovalCertificateName() + " of the recipient: " +
                        approval.getRecipientName() + "," + "" + approval.getRecipientEmail() +
                        " is already issued and checksum found, rejected this certificate.";

                approval.setStatus(ApprovalStatus.rejected);
                Notification notification = new Notification();
                notification.setId(UlidCreator.getUlid().toString());
                notification.setMessage(errorMessage);
                notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
                notification.setUser(user);
                notification.setType(NotificationType.ERROR);
                notificationRepository.save(notification);
                notificationRepository.save(notification);
                approvalRepository.save(approval);

                String approvalPath = approval.getEvent().getCluster().getId() + "/"
                        + approval.getEvent().getId() + "/" + approval.getId() + "/" + approval.getApprovalCertificateName();

                minioClient.removeObject(RemoveObjectArgs.builder()
                        .object(approvalPath)
                        .bucket("approvals")
                        .build());

            } else {

                Certificate certificate = new Certificate();
                certificate.setId(approval.getId());
                certificate.setPersisted(message.getPersist());
                certificate.setCertificateName(approval.getApprovalCertificateName());
                certificate.setCertificateHash(message.getHash());
                certificate.setComments(approval.getComments());
                certificate.setRecipientName(approval.getRecipientName());
                certificate.setRecipientEmail(approval.getRecipientEmail());
                certificate.setIssuedDate(new Date(System.currentTimeMillis()));
                certificate.setExpiryDate(approval.getExpiryDate());
                certificate.setEvent(approval.getEvent());
                certificate.setStatus(CertificateStatus.verified);
                certificate.setIssuedByUser(user);
                approval.setStatus(ApprovalStatus.approved);
                approvalRepository.save(approval);
                certificateRepository.save(certificate);

                String htmlContent =
                        "<html>" +
                                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                                "<table align='center' width='100%' border='0' cellpadding='0' cellspacing='0' style='margin: 0; padding: 20px;'>" +
                                "<tr>" +
                                "<td align='center'>" +
                                "<table width='600px' border='0' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>" +
                                "<tr>" +
                                "<td style='padding: 20px;'>" +
                                "<h2 style='color: #333333;'>Certificate Verification Successful</h2>" +
                                "<p style='color: #555555;'>Hello,</p>" +
                                "<p style='color: #555555;'>We are pleased to inform you that your certificate for the event "+ certificate.getEvent().getName()   + "have been successfully verified.</p>" +
                                "<p style='color: #555555;'>Please find your verified certificate attached below.</p>" +
                                "<table align='center' border='0' cellpadding='0' cellspacing='0' style='margin: 20px auto;'>" +
                                "<tr>" +
                                "<td align='center' bgcolor='#28a745' style='border-radius: 5px;'>" +
                                "</td>" +
                                "</tr>" +
                                "</table>" +
                                "<p style='color: #555555;'>If you have any questions, feel free to reach out to us.</p>" +
                                "<p style='color: #555555;'>Thanks,</p>" +
                                "<p style='color: #555555;'>The Team</p>" +
                                "</td>" +
                                "</tr>" +
                                "</table>" +
                                "</td>" +
                                "</tr>" +
                                "</table>" +
                                "</body>" +
                                "</html>";


                String path = certificate.getEvent().getCluster().getId() + "/" + certificate.getEvent().getId() + "/" + certificate.getId() + "/" + certificate.getCertificateName();
                InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket("approvals").object(path).build());
                emailUtility.sendEmail(certificate.getRecipientEmail(), "Certificate Verification Successful",htmlContent,stream,certificate.getCertificateName());

                if(!message.getPersist()){
                    String certificatePath = certificate.getEvent().getCluster().getId() + "/"
                            + certificate.getEvent().getId() + "/" + certificate.getId() + "/" + certificate.getCertificateName();
                    minioClient.removeObject(RemoveObjectArgs.builder()
                            .object(certificatePath)
                            .bucket("approvals")
                            .build());
                }


            }

            channel.basicAck(tag, false);


        } catch (Exception e) {
            log.error("Error in receiving approval request: {}", e);
            //dead letter queue
            channel.basicNack(tag, false, false);
        }

    }

}
