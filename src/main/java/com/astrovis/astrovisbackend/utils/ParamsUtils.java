package com.astrovis.astrovisbackend.utils;

import java.util.List;

public class ParamsUtils {


    public static boolean checkParamsType(Object params,Class<?> type){
        return type.isInstance(params);
    }

    public static boolean allNotNull(Object... params){

        for(Object param:params){
            if (null == param){return false;}
        }

        return true;

    }

}
