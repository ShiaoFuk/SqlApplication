package com.example.sqlapplication.net;

import com.example.sqlapplication.utils.JsonUtils;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.Response;

public class ResponseUtils {
    public static JsonElement doWithResponse(Response response) throws IOException {
        String res = response.body().string();
        return JsonUtils.getJsonParser().fromJson(res, JsonElement.class);
    }
}
