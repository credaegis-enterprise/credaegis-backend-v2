package com.credaegis.backend.utility;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public  class CheckSumUtility  {



    public Boolean isHashValid(byte [] data,String hash) throws NoSuchAlgorithmException{
        String hashedData = hashCertificate(data);
        return hashedData.equals(hash);
    }

    public  String hashCertificate(byte [] data) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte [] hashedBytes = md.digest(data);
        StringBuilder stringBuilder = new StringBuilder();
        for(byte value:hashedBytes){

            stringBuilder.append(String.format("%02x",value));
        }

        return stringBuilder.toString();
    }
}
