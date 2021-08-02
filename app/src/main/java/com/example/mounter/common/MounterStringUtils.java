package com.example.mounter.common;

public class MounterStringUtils {
    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }
    public static boolean containsData(String str){
        return !isNullOrEmpty(str);
    }
}
