package com.example.sqlapplication.net.login;

import android.util.Log;

import com.example.sqlapplication.data.dto.MyRequestBody;
import com.example.sqlapplication.data.dto.user.LoginUser;
import com.example.sqlapplication.data.dto.user.UserInfo;
import com.example.sqlapplication.net.RequestListener;
import com.example.sqlapplication.net.ResponseUtils;
import com.example.sqlapplication.utils.JsonUtils;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateUserInfoRequest {
    private final static String TAG = UpdateUserInfoRequest.class.getName();

    public static void update(RequestListener listener, UserInfo userInfo) {
        listener.onRequest();
        Thread thread = new Thread(()->{
            try {
                JsonElement element = request(userInfo);
                listener.onReceive();
                listener.onSuccess(element);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onError(e);
            } finally {
                listener.onFinish();
            }
        });
        thread.start();
    }


    private static JsonElement request(UserInfo userInfo) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JsonUtils.toJson(userInfo));
        Request request = new Request.Builder()
                .url("http://suansun.top/user/updateInfo")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return ResponseUtils.doWithResponse(response);
    }
}
