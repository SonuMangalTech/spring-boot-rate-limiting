package com.sonumangal.tech.config;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLevelLimit {

    String apiName();

}