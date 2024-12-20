package com.credaegis.backend.external;


import org.springframework.stereotype.Component;

@Component
public class ErrorReceiver {


    public void receiveMessage(String message){
        System.out.println("hey working errror" + message);
    }


}
