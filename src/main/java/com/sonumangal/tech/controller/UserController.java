package com.sonumangal.tech.controller;

import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public String testApi() {
        return "Hello, Friend...!";
    }

    @PostMapping("/addUser")
    public String createUser(@RequestBody UserEntity request) {
        userService.createUser(request);
        return "Create successfully...!";
    }

    @GetMapping("/getUsers")
    public List<UserEntity> getUser() {
        return userService.getUser();
    }

}
