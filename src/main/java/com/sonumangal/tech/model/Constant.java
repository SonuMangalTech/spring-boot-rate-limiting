package com.sonumangal.tech.model;

import java.util.Arrays;
import java.util.List;

public interface Constant {
    String X_USER_KEY = "X-USER-KEY";
    List<String> skipAuth_method = Arrays.asList("/user", "/h2-console");
    List<String> skipAuth = Arrays.asList("/user", "/h2-console","/getUsers");

    // BUCKET
    int TOKEN_CONSUME = 1;
    int REFILL_CAPACITY = 6;
    int REFILL_TIME = 600;
}
