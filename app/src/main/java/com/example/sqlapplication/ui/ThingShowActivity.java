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
import androidx.databinding.DataBindingUtil;

import com.example.sqlapplication.R;
import com.example.sqlapplication.data.model.Thing;
import com.example.sqlapplication.databinding.ActivityThingShowBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.thing.GetThingDetailRequest;
import com.example.sqlapplication.utils.JsonUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.google.gson.JsonObject;

public class ThingShowActivity extends AppCompatActivity {
    ActivityThingShowBinding b;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = DataBindingUtil.setContentView(this, R.layout.activity_thing_show);
        mContext = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int id = getIntent().getIntExtra("id", -1);
        GetThingDetailRequest.get(new DefaultRequestListener() {
            @Override
            protected void successHandle(JsonObject data) {
                Thing thing = JsonUtils.getJsonParser().fromJson(data.get("data"), Thing.class);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(()->{
                    b.setThing(thing);
                });
                ToastUtils.makeShortText(mContext, "获取物品信息成功");
            }

            @Override
            protected void failHandle(String msg) {
                ToastUtils.makeShortText(mContext, msg);
            }

            @Override
            protected void errorHandle(Exception e) {
                ToastUtils.makeShortText(mContext, e.getMessage());
            }
        }, id);

    }
}