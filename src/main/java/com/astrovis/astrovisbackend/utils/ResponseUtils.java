package com.astrovis.astrovisbackend.utils;

import com.astrovis.astrovisbackend.commons.Response;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {


    public static Response<Map<String,Object>> createIllegalParamsResponse(){
        Map<String,Object> result = new HashMap<>();
        return new Response<>(400,"illegal parameters",result);
    }


}
