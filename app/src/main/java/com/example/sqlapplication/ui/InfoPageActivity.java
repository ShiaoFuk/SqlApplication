package com.example.sqlapplication.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.sqlapplication.MyApplication;
import com.example.sqlapplication.R;
import com.example.sqlapplication.data.dto.user.UserInfo;
import com.example.sqlapplication.data.model.User;
import com.example.sqlapplication.databinding.ActivityInfoPageBinding;
import com.example.sqlapplication.databinding.DialogUserinfoBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.login.GetUserInfoRequest;
import com.example.sqlapplication.net.login.UpdateUserInfoRequest;
import com.example.sqlapplication.utils.JsonUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;

public class InfoPageActivity extends AppCompatActivity {
    ActivityInfoPageBinding d;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        d = DataBindingUtil.setContentView(this, R.layout.activity_info_page);
        mContext = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GetUserInfoRequest.getInfo(new DefaultRequestListener() {
            @Override
            protected void successHandle(JsonObject data) {
                User user = JsonUtils.getJsonParser().fromJson(data.get("data"), User.class);
                MyApplication.setUser(user);
                d.setUser(MyApplication.getUser());
            }

            @Override
            protected void failHandle(String msg) {
                ToastUtils.makeShortText(mContext, msg);
            }

            @Override
            protected void errorHandle(Exception e) {
                ToastUtils.makeShortText(mContext, "获取用户信息失败");
            }
        }, TokenUtils.getToken(mContext));


        d.modifyBtn.setOnClickListener(v->{
            User cuser = MyApplication.getUser();
            UserInfo userInfo = new UserInfo();
            userInfo.setCertificateNumber(cuser.getCertificateNumber());
            userInfo.setEmail(cuser.getEmail());
            DialogUserinfoBinding userBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_userinfo, null, false);
            userBinding.setUserInfo(userInfo);
            // Create the dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(userBinding.getRoot())
                    .setTitle("修改个人信息")
                    .setPositiveButton("确定", (dialog, which) -> onConfirmClicked(userInfo))
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        });

    }
    void onConfirmClicked(UserInfo userInfo) {
        userInfo.setToken(TokenUtils.getToken(mContext));
        UpdateUserInfoRequest.update(new DefaultRequestListener() {
            @Override
            protected void successHandle(JsonObject data) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(()->{
                    User user = MyApplication.getUser();
                    user.setEmail(userInfo.getEmail());
                    user.setCertificateNumber(userInfo.getCertificateNumber());
                    d.invalidateAll();
                });
                ToastUtils.makeShortText(mContext, "修改信息成功");
            }

            @Override
            protected void failHandle(String msg) {
                ToastUtils.makeShortText(mContext, msg);
            }

            @Override
            protected void errorHandle(Exception e) {
                ToastUtils.makeShortText(mContext, e.getMessage());
            }
        }, userInfo);
    }
}