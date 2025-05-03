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
                 "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Certificate Verification</title>\n" +
                        "</head>\n" +
                        "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>\n" +
                        "    <table align='center' width='100%' border='0' cellpadding='0' cellspacing='0' style='margin: 0; padding: 20px;'>\n" +
                        "        <tr>\n" +
                        "            <td align='center'>\n" +
                        "                <table width='600px' border='0' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>\n" +
                        "                    <!-- Header with Logo -->\n" +
                        "                    <tr>\n" +
                        "                        <td align=\"center\" style=\"padding: 20px 0; background-color: #f9f9f9; border-top-left-radius: 8px; border-top-right-radius: 8px;\">\n" +
                        "                            <img src=\"cid:logo\" alt=\"CredAegis Logo\" style=\"max-width: 200px; height: auto;\">\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <tr>\n" +
                        "                        <td style='padding: 20px 30px;'>\n" +
                        "                            <h2 style='color: #333333; margin-top: 0;'>Certificate Verification Successful</h2>\n" +
                        "                            <p style='color: #555555;'>Hello,</p>\n" +
                        "                            <p style='color: #555555;'>We are pleased to inform you that your certificate for the event <strong>" + certificate.getEvent().getName() + "</strong> has been successfully verified.</p>\n" +
                        "                            <p style='color: #555555;'>Please find your verified certificate attached below.</p>\n" +
                        "\n" +
                        "                            <!-- Verification Section -->\n" +
                        "                            <div style=\"background-color: #f9f9f9; border-left: 4px solid #007bff; padding: 15px; margin: 20px 0;\">\n" +
                        "                                <p style='color: #555555; margin-top: 0;'>For future verification, you can use :</p>\n" +
                        "                                <ul style=\"color: #555555; padding-left: 20px;\">\n" +
                        "                                    <li>Visit our direct verification portal: <a href='https://www.credaegis.net/verification' style='color: #007bff; text-decoration: none;'>www.credaegis.net/verification</a></li>\n" +

                        "                                </ul>\n" +
                        "                            </div>\n" +
                        "\n" +
                        "                            <!-- Transaction Link Section -->\n" +
                        "                            <p style='color: #555555;'>You can view the transaction details on the public blockchain using the link below:</p>\n" +
                        "                            <table align='center' border='0' cellpadding='0' cellspacing='0' style='margin: 20px auto;'>\n" +
                        "                                <tr>\n" +
                        "                                    <td align='center' bgcolor='#007bff' style='border-radius: 5px;'>\n" +
                        "                                        <a href='" + certificate.getBatchInfo().getTxnUrl() + "' target='_blank' style='display: inline-block; padding: 12px 24px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;'>View Transaction</a>\n" +
                        "                                    </td>\n" +
                        "                                </tr>\n" +
                        "                            </table>\n" +
                        "\n" +
                        "                            <p style='color: #555555;'>If you have any questions, feel free to reach out to us.</p>\n" +
                        "                            <p style='color: #555555;'>Thanks,</p>\n" +
                        "                            <p style='color: #555555;'>The CredAegis Team</p>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                    <!-- Footer -->\n" +
                        "                    <tr>\n" +
                        "                        <td style=\"padding: 15px; background-color: #f9f9f9; text-align: center; font-size: 12px; color: #777; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;\">\n" +
                        "                            <p style=\"margin: 5px 0;\">© 2025 CredAegis. All rights reserved.</p>\n" +
                        "                            <p style=\"margin: 5px 0;\">Secure. Trusted. Verified.</p>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n" +
                        "                </table>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "</body>\n" +
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
