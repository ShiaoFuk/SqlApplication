package com.example.sqlapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sqlapplication.R;
import com.example.sqlapplication.adapter.FormAdapter;
import com.example.sqlapplication.data.dto.MyRequestBody;
import com.example.sqlapplication.data.model.Form;
import com.example.sqlapplication.data.state.FormState;
import com.example.sqlapplication.databinding.ActivityMyBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.form.GetAllFormRequest;
import com.example.sqlapplication.net.form.GetStateFormRequest;
import com.example.sqlapplication.utils.JsonUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {
    ActivityMyBinding b;
    Context mContext;
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityMyBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        mContext = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        b.recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        b.recycleView.setAdapter(new FormAdapter(new ArrayList<>()));
        // 请求全部
        GetAllFormRequest.get(new DefaultRequestListener() {
            @Override
            protected void successHandle(JsonObject data) {
                List<Form> formList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<Form>>() {}.getType());
                handler.post(()->{
                    FormAdapter formAdapter = (FormAdapter)b.recycleView.getAdapter();
                    formAdapter.setList(formList);
                });
                ToastUtils.makeShortText(mContext, "获取领回单成功");
            }

            @Override
            protected void failHandle(String msg) {
                ToastUtils.makeShortText(mContext, msg);
            }

            @Override
            protected void errorHandle(Exception e) {
                ToastUtils.makeShortText(mContext, e.getMessage());
            }
        }, TokenUtils.getToken(mContext));
        initUBtn();
        initDBtn();
        initSBtn();
    }



    void initUBtn() {
        b.unsolvedBtn.setOnClickListener(v->{
            GetStateFormRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<Form> formList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<Form>>() {}.getType());
                    handler.post(()->{
                        FormAdapter formAdapter = (FormAdapter)b.recycleView.getAdapter();
                        formAdapter.setList(formList);
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

    void initDBtn() {
        b.deleteBtn.setOnClickListener(v->{
            GetStateFormRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<Form> formList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<Form>>() {}.getType());
                    handler.post(()->{
                        FormAdapter formAdapter = (FormAdapter)b.recycleView.getAdapter();
                        formAdapter.setList(formList);
                    });
                    ToastUtils.makeShortText(mContext, "获取删除领回单成功");
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(mContext), FormState.DELETE));
        });
    }

    void initSBtn() {
        b.solvedBtn.setOnClickListener(v->{
            GetStateFormRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    List<Form> formList = JsonUtils.getJsonParser().fromJson(data.get("data"), new TypeToken<List<Form>>() {}.getType());
                    handler.post(()->{
                        FormAdapter formAdapter = (FormAdapter)b.recycleView.getAdapter();
                        formAdapter.setList(formList);
                    });
                    ToastUtils.makeShortText(mContext, "获取已处理领回单成功");
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(mContext, msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(mContext, e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(mContext), FormState.SOLVED));
        });
    }
}