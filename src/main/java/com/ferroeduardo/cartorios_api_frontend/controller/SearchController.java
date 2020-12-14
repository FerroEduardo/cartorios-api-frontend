package com.ferroeduardo.cartorios_api_frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    public static final int cartoriosTableRows = 20;

    @GetMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("cartoriosTableRows", cartoriosTableRows);
        return "index";
    }
}
