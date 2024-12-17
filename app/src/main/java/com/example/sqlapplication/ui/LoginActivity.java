package com.example.sqlapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlapplication.R;
import com.example.sqlapplication.data.dto.user.LoginUser;
import com.example.sqlapplication.databinding.ActivityLoginBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.RequestListener;
import com.example.sqlapplication.net.login.LoginRequest;
import com.example.sqlapplication.net.login.RegisterRequest;
import com.example.sqlapplication.utils.ToastUtils;
import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding b;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        mContext = this;
        View view = b.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        b.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    b.loginBtn.setText("注册");
                } else {
                    b.loginBtn.setText("登录");
                }
            }
        });
        b.loginBtn.setOnClickListener(v->{
            String username = b.username.getText().toString();
            String password = b.password.getText().toString();
            LoginUser loginUser = new LoginUser(username, password);
            if (b.checkbox.isChecked()) {
                // 注册
                RegisterRequest.register(new DefaultRequestListener() {
                    @Override
                    protected void successHandle(JsonObject data) {
                        go2Main(data);
                    }

                    @Override
                    protected void failHandle(String msg) {
                        ToastUtils.makeShortText(mContext, msg);
                    }

                    @Override
                    protected void errorHandle(Exception e) {
                        ToastUtils.makeShortText(mContext, "注册出错");
                    }
                }, loginUser);
            } else {
                // 登录
                LoginRequest.login(new DefaultRequestListener() {
                    @Override
                    protected void successHandle(JsonObject data) {
                        go2Main(data);
                    }

                    @Override
                    protected void failHandle(String msg) {
                        ToastUtils.makeShortText(mContext, msg);
                    }

                    @Override
                    protected void errorHandle(Exception e) {
                        ToastUtils.makeShortText(mContext, "登录出错");
                    }
                }, loginUser);
            }
        });
    }

    void go2Main(JsonObject data) {
        String token = data.get("data").getAsString();
        SharedPreferences.Editor editor = getSharedPreferences("sp", MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}