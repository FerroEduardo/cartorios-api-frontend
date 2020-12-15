package com.ferroeduardo.cartorios_api_frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.service.UserService;
import com.ferroeduardo.cartorios_api_frontend.util.RequestsUtil;
import com.ferroeduardo.cartorios_api_frontend.util.ServicesCommunicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(path = "/api")
public class ApiController {

    private final Logger logger;

    private UserService userService;

    private ServicesCommunicationUtil servicesCommunicationUtil;

    public ApiController(UserService userService, ServicesCommunicationUtil servicesCommunicationUtil) {
        this.userService = userService;
        this.servicesCommunicationUtil = servicesCommunicationUtil;
        this.logger = LoggerFactory.getLogger(ApiController.class);
    }

    @GetMapping(path = "/access")
    public String apiPanelControl(Model model, Principal principal) throws JsonProcessingException {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("apiAccessible",user.isApiAccessible());
        if (user.isApiAccessible()) {
            String userApiKey = getUserApiKey(user.getId()).get("api_key");
            if (userApiKey == null || userApiKey.length() == 0) {
                userApiKey = "REQUISITAR NOVA CHAVE";
            }
            model.addAttribute("apiKey", userApiKey);
        }
        return "/api/access";
    }

    @GetMapping(path = "/how")
    public String howAccessApi(Model model, Principal principal) {
        return "/api/how";
    }

    @GetMapping(path = "/key/get", produces = "application/json")
    private Map<String, String> getUserApiKey(Principal principal) throws JsonProcessingException {
        User user = userService.findByUsername(principal.getName());
        Long userId = user.getId();
        Map<String, String> responseObject = getUserApiKey(userId);
        return responseObject;
    }

    private Map<String, String> getUserApiKey(Long userId) throws JsonProcessingException {
        String requestUrl = "http://localhost:8080/api/key/get/";
        Map<String, Object> requestBodyMap = new TreeMap<>();
        requestBodyMap.put("userId", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set(servicesCommunicationUtil.usernameHeaderName, servicesCommunicationUtil.serviceUsername);
        headers.set(servicesCommunicationUtil.passwordHeaderName, servicesCommunicationUtil.servicePassword);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> responseEntity = RequestsUtil.makePostRequest(requestUrl, headers, requestBodyMap, Map.class);
        Map<String, String> responseObject = responseEntity.getBody();
        return responseObject;
    }

    @PostMapping(path = "/key/generate", produces = "application/json")
    private ResponseEntity<Map<String, String>> generateNewUserApiKey(Principal principal) throws JsonProcessingException {
        User user = userService.findByUsername(principal.getName());
        if (!user.isApiAccessible()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = user.getId();
        String requestUrl = "http://localhost:8080/api/key/generate/";
        Map<String, Object> requestBodyMap = new TreeMap<>();
        requestBodyMap.put("userId", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set(servicesCommunicationUtil.usernameHeaderName, servicesCommunicationUtil.serviceUsername);
        headers.set(servicesCommunicationUtil.passwordHeaderName, servicesCommunicationUtil.servicePassword);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> responseEntity = RequestsUtil.makePostRequest(requestUrl, headers, requestBodyMap, Map.class);
        Map<String, String> responseObject = responseEntity.getBody();
        logger.info(String.format("Usuário ID:'%d' requisitou uma nova API KEY", userId));
        return ResponseEntity.ok().body(responseObject);
    }

    @PostMapping(path = "/key/revoke", produces = "application/json")
    private ResponseEntity<?> revokeNewUserApiKey(Principal principal) throws JsonProcessingException {
        User user = userService.findByUsername(principal.getName());
        if (!user.isApiAccessible()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = user.getId();
        String requestUrl = "http://localhost:8080/api/key/revoke/";
        Map<String, Object> requestBodyMap = new TreeMap<>();
        requestBodyMap.put("userId", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set(servicesCommunicationUtil.usernameHeaderName, servicesCommunicationUtil.serviceUsername);
        headers.set(servicesCommunicationUtil.passwordHeaderName, servicesCommunicationUtil.servicePassword);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> responseEntity = RequestsUtil.makePostRequest(requestUrl, headers, requestBodyMap, Map.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.info(String.format("Usuário ID:'%d' revogou a API KEY com sucesso", userId));
            return ResponseEntity.ok().build();
        } else {
            logger.warn(String.format("Ocorreu um erro quando o usuário ID:'%d' tentou revogar a API key", userId));
            return ResponseEntity.unprocessableEntity().build();
        }
    }

}
