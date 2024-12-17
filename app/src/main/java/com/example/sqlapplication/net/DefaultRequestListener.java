package com.example.sqlapplication.net;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class DefaultRequestListener implements RequestListener {
    @Override
    public Object onPrepare(Object object) {
        return null;
    }

    @Override
    public void onRequest() {

    }

    @Override
    public void onReceive() {

    }

    @Override
    public void onSuccess(JsonElement element) throws Exception {
        new CheckSuccess() {
            @Override
            protected void doWithSuccess(JsonObject data) throws Exception {
                successHandle(data);
            }

            @Override
            protected void doWithFailure(String message) throws Exception {
                failHandle(message);
            }
        }.checkIfSuccess(element);
    }

    @Override
    public void onError(Exception e) {
        errorHandle(e);
    }

    @Override
    public void onFinish() {

    }

    /**
     * 请求成功
     * @param data 返回的整个result json格式
     */
    protected abstract void successHandle(JsonObject data);

    /**
     * 请求返回错误码处理
     */
    protected abstract void failHandle(String msg);

    /**
     * 本地抛出异常
     * @param e 抛出的异常
     */
    protected abstract void errorHandle(Exception e);
}
