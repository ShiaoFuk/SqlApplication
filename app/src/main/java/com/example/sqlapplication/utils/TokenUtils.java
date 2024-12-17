package com.example.sqlapplication.utils;

import android.content.Context;

public class TokenUtils {
    public static String getToken(Context context) {
        return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getString("token", null);
    }
}
