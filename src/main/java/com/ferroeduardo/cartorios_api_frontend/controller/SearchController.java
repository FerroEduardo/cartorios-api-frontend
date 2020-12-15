package com.ferroeduardo.cartorios_api_frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferroeduardo.cartorios_api_frontend.util.RequestsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {

    private final Logger logger;

    public static final int cartoriosTableRows = 20;

    public SearchController() {
        this.logger = LoggerFactory.getLogger(SearchController.class);
    }

    @GetMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("cartoriosTableRows", cartoriosTableRows);
        return "index";
    }

    @PostMapping(path = "/api/cartorios", produces = "application/json")
    public ResponseEntity<?> getCartorios(@RequestBody Map<String, Object> requestBody, @RequestParam(name = "page", required = true) int page) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String apiUrl = "http://localhost:8080/api/frontend/cartorios/?page="+page+"&cartoriosTableRows="+cartoriosTableRows;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("Frontend requisitou a lista de cartórios");
        ResponseEntity<List> responseEntity = RequestsUtil.makePostRequest(apiUrl, headers, requestBody, List.class);
        List<Map<String, Object>> responseObject = responseEntity.getBody();
        String responseBody = objectMapper.writeValueAsString(responseObject);
        logger.info("Dados requisitados pelo frontend foram processados pelo backend");
        return ResponseEntity.ok().body(responseBody);
    }
}
