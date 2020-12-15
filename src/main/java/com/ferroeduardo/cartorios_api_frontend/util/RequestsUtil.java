package com.ferroeduardo.cartorios_api_frontend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RequestsUtil {

    public static <T> ResponseEntity<T> makePostRequest(String requestUrl, HttpHeaders headers, Map<?, ?> requestBodyMap, Class<T> responseType) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(requestUrl, request, responseType);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> makeGetRequest(String requestUrl, HttpHeaders headers, Map<?, ?> requestBodyMap, Class<T> responseType) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, request, responseType);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> makeGetRequest(String requestUrl, HttpHeaders headers, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, request, responseType);
        return responseEntity;
    }

}
