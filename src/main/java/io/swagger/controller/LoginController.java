package io.swagger.controller;

import io.swagger.model.DTO.LoginDTO;
import io.swagger.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO login){
        return userService.login(login.getEmail(), login.getPassword());
    }
}
