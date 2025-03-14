package com.credaegis.backend.async;


import com.credaegis.backend.entity.BatchInfo;
import com.credaegis.backend.repository.BatchInfoRepository;
import com.credaegis.backend.utility.EmailUtility;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAsyncService {

//    private final EmailUtility emailUtility;
//    private final BatchInfoRepository batchInfoRepository;
//    private final MinioClient minioClient;
//
//
//    public void sendCertificateVerifiedEmail(String merkleRoot)
//    {
//        try {
//
//
//            BatchInfo batchInfo = batchInfoRepository.findOneByMerkleRoot(merkleRoot).orElse(null);
//            if(batchInfo == null)
//                return;
//
//
//
//        batchInfo.getCertificates().forEach(certificate -> {
//            String htmlContent =
//                    "<html>" +
//                            "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
//                            "<table align='center' width='100%' border='0' cellpadding='0' cellspacing='0' style='margin: 0; padding: 20px;'>" +
//                            "<tr>" +
//                            "<td align='center'>" +
//                            "<table width='600px' border='0' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>" +
//                            "<tr>" +
//                            "<td style='padding: 20px;'>" +
//                            "<h2 style='color: #333333;'>Certificate Verification Successful</h2>" +
//                            "<p style='color: #555555;'>Hello,</p>" +
//                            "<p style='color: #555555;'>We are pleased to inform you that your certificate for the event "+ certificate.getEvent().getName()   + "have been successfully verified.</p>" +
//                            "<p style='color: #555555;'>Please find your verified certificate attached below.</p>" +
//                            "<table align='center' border='0' cellpadding='0' cellspacing='0' style='margin: 20px auto;'>" +
//                            "<tr>" +
//                            "<td align='center' bgcolor='#28a745' style='border-radius: 5px;'>" +
//                            "</td>" +
//                            "</tr>" +
//                            "</table>" +
//                            "<p style='color: #555555;'>If you have any questions, feel free to reach out to us.</p>" +
//                            "<p style='color: #555555;'>Thanks,</p>" +
//                            "<p style='color: #555555;'>The Team</p>" +
//                            "</td>" +
//                            "</tr>" +
//                            "</table>" +
//                            "</td>" +
//                            "</tr>" +
//                            "</table>" +
//                            "</body>" +
//                            "</html>";
//
//            String path = certificate.getEvent().getCluster().getId() + "/" + certificate.getEvent().getId() + "/" + certificate.getId() + "/" + certificate.getCertificateName();
//            InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket("approvals").object(path).build());
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("Error sending email: {}", e.getMessage());
//        }
//    }
}
