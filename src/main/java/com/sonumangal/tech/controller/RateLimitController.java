package com.sonumangal.tech.controller;

import com.sonumangal.tech.config.MethodLevelLimit;
import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.model.LoginUser;
import com.sonumangal.tech.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {
    private final UserService userService;

    public RateLimitController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUser loginUser) {
        return "Login API called";
    }

    @GetMapping("/history")
    public String history() {
        return "History API called";
    }

    @GetMapping("/getUsers")
    @MethodLevelLimit(apiName = "getUsers")  // Using Spring AOP
    public List<UserEntity> getUser() {
        return userService.getUser();
    }
}
