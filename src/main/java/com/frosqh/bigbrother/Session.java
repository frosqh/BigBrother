package com.frosqh.bigbrother;

import java.util.HashMap;

public class Session {
    private static HashMap<String,Object> map;

    public static void init(){
        map = new HashMap<String,Object>();
    }

    public static void set(String key, Object value){
        map.put(key,value);
    }

    public static Object get(String key){
        return map.get(key);
    }

}
