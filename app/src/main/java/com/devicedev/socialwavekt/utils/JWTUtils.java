package com.devicedev.socialwavekt.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static String get(String token,int part){
        try {
            return getJson(token.split("\\.")[part]);

        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;

    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}