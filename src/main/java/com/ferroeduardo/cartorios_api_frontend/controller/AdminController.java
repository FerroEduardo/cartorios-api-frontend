package com.ferroeduardo.cartorios_api_frontend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.entity.UserDTO;
import com.ferroeduardo.cartorios_api_frontend.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/admin")
public class AdminController {

    private static final int usersPageSize = 4;

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/panel")
    public String apiAdminPanelControl() {
        return "/admin/panel";
    }

    @GetMapping(path = "/panel/authorize")
    public String authorizeUser(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Pageable firstPage = PageRequest.of(0, usersPageSize);
        model.addAttribute("apiAccessible", user.isApiAccessible());
        model.addAttribute("usersWithoutAccessToApi", userService.findUsersWithoutAccessToApi(firstPage));
        model.addAttribute("queryPath", "userswithoutaccesstoapi");
        model.addAttribute("usersPageSize", usersPageSize);
        return "/admin/authorize";
    }

    @PostMapping(path = "/panel/authorize")
    public String authorizeUserAccess(@RequestBody(required = true) Map<String, String> requestMap) throws JsonProcessingException {
        Long userId = Long.parseLong(requestMap.get("userId"));
        userService.authorizeUserAccess(userId);
        return "redirect:/api/admin/panel/authorize";
    }

    @GetMapping(path = "/panel/revoke")
    public String revokeUser(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Pageable firstPage = PageRequest.of(0, usersPageSize);
        model.addAttribute("apiAccessible", user.isApiAccessible());
        model.addAttribute("usersWithAccessToApi", userService.findUsersWithAccessToApi(firstPage));
        model.addAttribute("queryPath", "userswithaccesstoapi");
        model.addAttribute("usersPageSize", usersPageSize);
        return "/admin/revoke";
    }

    @PostMapping(path = "/panel/revoke")
    public String revokeUserAccess(@RequestBody(required = true) Map<String, String> requestMap) throws JsonProcessingException {
        Long userId = Long.parseLong(requestMap.get("userId"));
        userService.revokeUserAccess(userId);
        return "redirect:/api/admin/panel/revoke";
    }

    @PostMapping(path = "/userswithoutaccesstoapi", produces = "application/json")
    public ResponseEntity<?> usersWithoutAccessToApi(@RequestBody(required = true) Map<String, String> requestMap) {
        int page = Integer.parseInt(requestMap.get("page"));
        Pageable pageRequest = PageRequest.of(page, usersPageSize);
        List<UserDTO> users = userService.findUsersWithoutAccessToApi(pageRequest);
        return ResponseEntity.ok().body(users);
    }

    @PostMapping(path = "/userswithaccesstoapi", produces = "application/json")
    public ResponseEntity<?> usersWithAccessToApi(@RequestBody(required = true) Map<String, String> requestMap) {
        int page = Integer.parseInt(requestMap.get("page"));
        Pageable pageRequest = PageRequest.of(page, usersPageSize);
        List<UserDTO> users = userService.findUsersWithAccessToApi(pageRequest);
        return ResponseEntity.ok().body(users);
    }
}
