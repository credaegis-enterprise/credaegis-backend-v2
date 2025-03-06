package com.credaegis.backend.utility;

import com.credaegis.backend.constant.Constants;
import org.springframework.http.HttpHeaders;

public class HttpUtility {


    public static HttpHeaders getApiKeyHeader(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.API_KEY_HEADER, apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}
