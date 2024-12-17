package com.credaegis.backend.utility;

import com.credaegis.backend.exception.custom.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class CheckSumUtility {


    private static final Logger log = LogManager.getLogger(CheckSumUtility.class);

    public Boolean isHashValid(byte[] data, String hash) throws NoSuchAlgorithmException {
        String hashedData = hashCertificate(data);
        return hashedData.equals(hash);
    }

    public String hashCertificate(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(data);
            StringBuilder stringBuilder = new StringBuilder();
            for (byte value : hashedBytes) {

                stringBuilder.append(String.format("%02x", value));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            throw new CustomException("Error in hashing", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
