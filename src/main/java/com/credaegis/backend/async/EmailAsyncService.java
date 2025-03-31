package com.credaegis.backend.async;


import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.BatchInfo;
import com.credaegis.backend.entity.Certificate;
import com.credaegis.backend.repository.BatchInfoRepository;
import com.credaegis.backend.repository.CertificateRepository;
import com.credaegis.backend.utility.EmailUtility;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAsyncService {

    private final EmailUtility emailUtility;
    private final CertificateRepository certificateRepository;
    private final MinioClient minioClient;



    @Async
    public void sendCertificateVerifiedEmail(Integer batchId) {
        log.info("In Email Sending Async Service");
        log.info("BatchId to send email {}", batchId);
        List<Certificate> certificates = certificateRepository.findAllByBatchInfo_Id(batchId);
        log.info("Certificate count in email async {}", certificates.size());
        certificates.forEach(certificate -> {

            try {
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
                                "<p style='color: #555555;'>We are pleased to inform you that your certificate for the event <strong>" + certificate.getEvent().getName() + "</strong> has been successfully verified.</p>" +
                                "<p style='color: #555555;'>Please find your verified certificate attached below.</p>" +

                                // Transaction Link Section
                                "<p style='color: #555555;'>You can view the transaction details on the public blockchain using the link below:</p>" +
                                "<table align='center' border='0' cellpadding='0' cellspacing='0' style='margin: 20px auto;'>" +
                                "<tr>" +
                                "<td align='center' bgcolor='#007bff' style='border-radius: 5px;'>" +
                                "<a href='" + certificate.getBatchInfo().getTxnUrl() + "' target='_blank' style='display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;'>View Transaction</a>" +
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
                InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                        .bucket("approvals")
                        .object(path)
                        .build());


                emailUtility.sendEmail(certificate.getRecipientEmail(), "Certificate Verification Successful", htmlContent, stream, certificate.getCertificateName());

                if (!certificate.getPersisted()) {
                    String certificatePath = certificate.getEvent().getCluster().getId() + "/"
                            + certificate.getEvent().getId() + "/" + certificate.getId() + "/" + certificate.getCertificateName();
                    minioClient.removeObject(RemoveObjectArgs.builder()
                            .object(certificatePath)
                            .bucket("approvals")
                            .build());
                }

            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("Error while sending email for certificate verification", e);
            }

        });


    }
}
