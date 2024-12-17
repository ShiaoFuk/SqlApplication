package com.example.sqlapplication.net;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class CheckSuccess {
    final static String TAG = "CheckSuccess";

    /**
     * 请求成功要做的事情，解析data
     * @param data 得到的result响应的data字段内容，可能是对象、列表或字符串
     * @throws Exception
     */
    protected abstract void doWithSuccess(JsonObject data) throws Exception;  //

    /**
     * 请求失败码不为200要做的事情（不包含exception）,一般不实现
     */
    protected abstract void doWithFailure(String message) throws Exception;


    /**
     * 判断响应是否成功，data可能为空
     * @param element
     * @throws Exception
     */
    public void checkIfSuccess(JsonElement element) throws Exception {
        JsonObject jsonObject = element.getAsJsonObject();
        Log.e("fsdklasl", jsonObject.toString());
        if (jsonObject.get("code") != null && jsonObject.get("code").getAsInt() == 200) {
            doWithSuccess(jsonObject);
        } else {
            doWithFailure(jsonObject.get("message").getAsString());
        }
    }

}
