package com.ferroeduardo.cartorios_api_frontend.controller;

import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.entity.roles.UserRole;
import com.ferroeduardo.cartorios_api_frontend.exception.UserNotFoundException;
import com.ferroeduardo.cartorios_api_frontend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    private final Logger logger;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public AccountController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.logger = LoggerFactory.getLogger(AccountController.class);
    }

    @GetMapping("/signin")
    public String signin() {
        if (this.isAuthenticated()) {
            return "redirect:/api/access";
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        if (this.isAuthenticated()) {
            return "redirect:/api/access";
        }
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupProcess(@ModelAttribute User user) {
        try {
            userService.findByUsername(user.getUsername());
            userService.findByEmail(user.getEmail());
            logger.trace(String.format("Usuário '%s' de email %s já existe, não foi possivel cadastrar", user.getUsername(), user.getEmail()));
            return "redirect:/signup?exists";
        } catch (UserNotFoundException e) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(UserRole.USER.getRole());
            user.setAccountEnabled(true);
            user.setAccountExpired(false);
            user.setAccountLocked(false);
            user.setCredentialsExpired(false);
            user.setApiAccessible(false);
            try {
                userService.save(user);
            } catch (Exception e1) {
                logger.error(e.getMessage(), e);
                return "redirect:/signup?error";
            }
            logger.trace(String.format("Usuário '%s' cadastrado com sucesso", user.getUsername()));
            return "redirect:/signup?success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "redirect:/signup?error";
        }
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

}
