package com.sonumangal.tech.filter;

import com.sonumangal.tech.model.Constant;

public class FilterUtils {

    public static boolean shouldNotFilter(String uri) {
        return Constant.skipAuth.stream().anyMatch(uri::contains);
    }

    public static boolean shouldNotFilterMethod(String uri) {
        return Constant.skipAuth_method.stream().anyMatch(uri::contains);
    }

}
