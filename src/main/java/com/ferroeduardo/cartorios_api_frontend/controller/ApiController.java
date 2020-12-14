package com.ferroeduardo.cartorios_api_frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ferroeduardo.cartorios_api_frontend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api")
public class ApiController {

    private final Logger logger;

    private UserService userService;

    public ApiController(UserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(ApiController.class);
    }

    @GetMapping(path = "/access")
    public String apiPanelControl() throws JsonProcessingException {
        return "/api/access";
    }

}
