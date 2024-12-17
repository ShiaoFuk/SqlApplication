package com.example.sqlapplication.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sqlapplication.MyApplication;
import com.example.sqlapplication.R;
import com.example.sqlapplication.adapter.ThingViewAdapter;
import com.example.sqlapplication.data.dto.thing.ThingMyPageInfo;
import com.example.sqlapplication.data.model.ThingView;
import com.example.sqlapplication.data.model.User;
import com.example.sqlapplication.data.state.ThingState;
import com.example.sqlapplication.data.state.UserPermission;
import com.example.sqlapplication.databinding.ActivityMainBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.login.GetUserInfoRequest;
import com.example.sqlapplication.net.thing.GetFoundRequest;
import com.example.sqlapplication.net.thing.GetLostRequest;
import com.example.sqlapplication.utils.JsonUtils;
import com.example.sqlapplication.utils.JwtUtil;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding b;

    Context mContext;
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mContext = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 登录跳转
        SharedPreferences sharedPreferences = getSharedPreferences("sp", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token == null || JwtUtil.checkIfTokenExpire(JwtUtil.getExpireTime(token))) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        // 获取个人信息
        GetUserInfoRequest.getInfo(new DefaultRequestListener() {
            @Override
            protected void successHandle(JsonObject data) {
                User user = JsonUtils.getJsonParser().fromJson(data.get("data"), User.class);
                MyApplication.setUser(user);
                ToastUtils.makeShortText(mContext, "更新个人信息成功");
                if (user.getPermission() >= UserPermission.MANAGER.getVal()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(()-> b.managerBtn.setVisibility(View.VISIBLE));
                }
            }

            @Override
            protected void failHandle(String msg) {
                ToastUtils.makeShortText(mContext, msg);
            }

            @Override
            protected void errorHandle(Exception e) {
                ToastUtils.makeShortText(mContext, "当前未联网");
            }
        }, TokenUtils.getToken(mContext));
        init();
    }


    void init() {
        b.recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        initInfoPage();  // 我的
        initShowLost();
        initShowFound();
        initFormBtn();
        initLogout();
        initManager();
    }

    private void initInfoPage() {
        b.infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, InfoPageActivity.class);
            startActivity(intent);
        });
    }

    private void initShowLost() {
        b.showLostBtn.setOnClickListener(v->{
            b.title.setText("丢失物品");
            MyApplication.setMode(MyApplication.Mode.LOST);
            GetLostRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<ThingView> thingViewList = JsonUtils.getJsonParser().fromJson(data.get("data").getAsJsonObject().get("list"), new TypeToken<List<ThingView>>() {}.getType());
                    ThingViewAdapter thingViewAdapter = new ThingViewAdapter(thingViewList);
                    mHandler.post(()-> b.recycleView.setAdapter(thingViewAdapter));
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, "请求失败");
                }
            }, new ThingMyPageInfo(0, 10, ThingState.UNSOLVED));
        });
    }

    private void initShowFound() {
        b.showFoundBtn.setOnClickListener(v->{
            b.title.setText("找回物品");
            MyApplication.setMode(MyApplication.Mode.FOUND);
            GetFoundRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<ThingView> thingViewList = JsonUtils.getJsonParser().fromJson(data.get("data").getAsJsonObject().get("list"), new TypeToken<List<ThingView>>() {}.getType());
                    ThingViewAdapter thingViewAdapter = new ThingViewAdapter(thingViewList);
                    mHandler.post(()-> b.recycleView.setAdapter(thingViewAdapter));
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, "请求失败");
                }
            }, new ThingMyPageInfo(0, 10, ThingState.UNSOLVED));
        });
    }


    private void initFormBtn() {
        b.formBtn.setOnClickListener(v->{
            Intent intent = new Intent(mContext, MyActivity.class);
            startActivity(intent);
        });
    }

    private void initLogout() {
        b.logoutBtn.setOnClickListener(v->{
            SharedPreferences sharedPreferences = getSharedPreferences("sp", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token");
            editor.apply();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            ToastUtils.makeShortText(mContext, "退出登录成功");
            startActivity(intent);
        });
    }

    private void initManager() {
        b.managerBtn.setOnClickListener(v->{
            Intent intent = new Intent(mContext, ManagerActivity.class);
            startActivity(intent);
        });
    }



}