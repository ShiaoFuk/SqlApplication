package com.example.sqlapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sqlapplication.MyApplication;
import com.example.sqlapplication.R;
import com.example.sqlapplication.adapter.FormAdapter;
import com.example.sqlapplication.adapter.ManagerFormAdapter;
import com.example.sqlapplication.adapter.UserAdapter;
import com.example.sqlapplication.data.dto.MyRequestBody;
import com.example.sqlapplication.data.model.Form;
import com.example.sqlapplication.data.model.User;
import com.example.sqlapplication.data.state.FormState;
import com.example.sqlapplication.data.state.UserPermission;
import com.example.sqlapplication.databinding.ActivityManagerBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.form.GetStateFormRequest;
import com.example.sqlapplication.net.manager.GetUsersRequest;
import com.example.sqlapplication.utils.JsonUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {
    ActivityManagerBinding b;
    Context mContext;
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mContext = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        b.recycView.setLayoutManager(new LinearLayoutManager(mContext));
        initUserBtn();
        initManagerBtn();
        initFormBtn();
        if (MyApplication.getUser().getPermission() < UserPermission.ROOT.getVal()) {
            b.showManager.setVisibility(View.GONE);
            b.showUser.setVisibility(View.GONE);
        } else {
            b.showManager.setVisibility(View.VISIBLE);
            b.showUser.setVisibility(View.VISIBLE);
        }
        b.showForm.performClick();
    }



    private void initManagerBtn() {
        b.showManager.setOnClickListener(v->{
            // 发起请求
            b.title.setText("管理员");
            GetUsersRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<User> userList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<User>>(){}.getType());
                    handler.post(()->{
                        b.recycView.setAdapter(new UserAdapter(userList, UserPermission.MANAGER));
                    });
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(mContext), UserPermission.MANAGER));
        });
    }

    private void initUserBtn() {
        b.showUser.setOnClickListener(v->{
            // 发起请求
            b.title.setText("用户");
            GetUsersRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<User> userList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<User>>(){}.getType());
                    handler.post(()->{
                        b.recycView.setAdapter(new UserAdapter(userList, UserPermission.NORMAL));
                    });
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(mContext), UserPermission.NORMAL));
        });
    }

    private void initFormBtn() {
        b.showForm.setOnClickListener(v->{
            b.title.setText("表单");
            GetStateFormRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<Form> formList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<Form>>() {}.getType());
                    handler.post(()->{
                        b.recycView.setAdapter(new ManagerFormAdapter(formList));
                    });
                    ToastUtils.makeShortText(mContext, "获取未处理领回单成功");
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(mContext), FormState.UNSOLVED));
        });
    }
}