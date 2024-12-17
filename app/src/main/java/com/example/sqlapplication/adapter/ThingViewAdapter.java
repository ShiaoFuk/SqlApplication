package com.example.sqlapplication.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlapplication.MyApplication;
import com.example.sqlapplication.R;
import com.example.sqlapplication.data.dto.MyRequestBody;
import com.example.sqlapplication.data.model.Complaint;
import com.example.sqlapplication.data.model.Thing;
import com.example.sqlapplication.data.model.ThingView;
import com.example.sqlapplication.databinding.DialogThingBinding;
import com.example.sqlapplication.databinding.ThingviewLayoutBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.form.AddFormRequest;
import com.example.sqlapplication.net.thing.DelFoundRequest;
import com.example.sqlapplication.net.thing.DelLostRequest;
import com.example.sqlapplication.net.thing.UpdateFoundRequest;
import com.example.sqlapplication.net.thing.UpdateLostRequest;
import com.example.sqlapplication.utils.DialogUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThingViewAdapter extends RecyclerView.Adapter<ThingViewViewHolder> {
    List<ThingView> list;
    Handler handler = new Handler(Looper.getMainLooper());
    public ThingViewAdapter(List<ThingView> thingViews) {
        this.list = thingViews;
    }

    @NonNull
    @Override
    public ThingViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThingviewLayoutBinding d = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.thingview_layout, parent, false);
        return new ThingViewViewHolder(d);
    }

    private void onConfirmClicked(Thing thing, EditText time, int position) {
        String timestr = time.getText().toString();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(timestr);
        } catch (ParseException e) {
            ToastUtils.makeShortText(time.getContext(), "日期格式输入错误");
            return;
        }
        thing.setTime(date);
        if (MyApplication.getMode() == MyApplication.Mode.FOUND) {
            UpdateFoundRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    ThingView view = list.get(position);
                    view.setName(thing.getName());
                    view.setPlace(thing.getPlace());
                    view.setTime(thing.getTime());
                    handler.post(()->{
                        notifyItemChanged(position);
                    });
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(time.getContext(), msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(time.getContext(), e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(time.getContext()), thing));
        }
        else if (MyApplication.getMode() == MyApplication.Mode.LOST) {
            UpdateLostRequest.get(new DefaultRequestListener() {
                @Override
                protected void successHandle(JsonObject data) {
                    ThingView view = list.get(position);
                    view.setName(thing.getName());
                    view.setPlace(thing.getPlace());
                    view.setTime(thing.getTime());
                    handler.post(()->{
                        notifyItemChanged(position);
                    });
                }

                @Override
                protected void failHandle(String msg) {
                    ToastUtils.makeShortText(time.getContext(), msg);
                }

                @Override
                protected void errorHandle(Exception e) {
                    ToastUtils.makeShortText(time.getContext(), e.getMessage());
                }
            }, new MyRequestBody<>(TokenUtils.getToken(time.getContext()), thing));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ThingViewViewHolder holder, int position) {
        holder.d.setThing(list.get(position));
        Context context = holder.d.getRoot().getContext();
        holder.d.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.thing_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.update) {
                            // 更新
                            Thing thing = new Thing();
                            thing.setId(list.get(holder.getLayoutPosition()).getId());
                            DialogThingBinding thingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_thing, null, false);
                            thingBinding.setThing(thing);
                            // Create the dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setView(thingBinding.getRoot())
                                    .setTitle("修改物品")
                                    .setPositiveButton("确定", (dialog, which) -> onConfirmClicked(thing, thingBinding.editTextTime, holder.getLayoutPosition()))
                                    .setNegativeButton("取消", null)
                                    .create()
                                    .show();
                        }
                        if (item.getItemId() == R.id.delete) {
                            DialogUtils.showEnsureDialog(context, "删除", "确定要删除吗？", "确定", new DialogUtils.DialogCallback() {
                                @Override
                                public void doSth(View view) {
                                    // 发起请求
                                    if (MyApplication.getMode() == MyApplication.Mode.FOUND) {
                                        DelFoundRequest.get(new DefaultRequestListener() {
                                            @Override
                                            protected void successHandle(JsonObject data) {
                                                handler.post(()->{
                                                    int index = holder.getLayoutPosition();
                                                    list.remove(index);
                                                    notifyItemRemoved(index);
                                                });
                                            }
                                            @Override
                                            protected void failHandle(String msg) {
                                                ToastUtils.makeShortText(context, msg);
                                            }

                                            @Override
                                            protected void errorHandle(Exception e) {
                                                ToastUtils.makeShortText(context, e.getMessage());
                                            }
                                        }, new MyRequestBody<>(TokenUtils.getToken(context), list.get(holder.getAdapterPosition()).getId()));
                                    } else if (MyApplication.getMode() == MyApplication.Mode.LOST) {
                                        DelLostRequest.get(new DefaultRequestListener() {
                                            @Override
                                            protected void successHandle(JsonObject data) {
                                                handler.post(()->{
                                                    int index = holder.getLayoutPosition();
                                                    list.remove(index);
                                                    notifyItemRemoved(index);
                                                });
                                            }

                                            @Override
                                            protected void failHandle(String msg) {
                                                ToastUtils.makeShortText(context, msg);
                                            }

                                            @Override
                                            protected void errorHandle(Exception e) {
                                                ToastUtils.makeShortText(context, e.getMessage());
                                            }
                                        }, new MyRequestBody<>(TokenUtils.getToken(context), list.get(holder.getAdapterPosition()).getId()));
                                    }
                                }
                            }, null);
                        }
                        if (item.getItemId() == R.id.get) {
                            if (MyApplication.getMode() == MyApplication.Mode.FOUND) {
                                DialogUtils.showEnsureDialog(context, "领回单", "确定要认领这个物品吗", "确定", new DialogUtils.DialogCallback() {
                                    @Override
                                    public void doSth(View view) {
                                        AddFormRequest.add(new DefaultRequestListener() {
                                            @Override
                                            protected void successHandle(JsonObject data) {
                                                ToastUtils.makeShortText(context, "成功，您可以先领回或等待审批");
                                            }

                                            @Override
                                            protected void failHandle(String msg) {
                                                ToastUtils.makeShortText(context, "您已经领回过这个物品了");
                                            }

                                            @Override
                                            protected void errorHandle(Exception e) {
                                                ToastUtils.makeShortText(context, e.getMessage());
                                            }
                                        }, new MyRequestBody<>(TokenUtils.getToken(context), list.get(holder.getLayoutPosition()).getId()));

                                    }
                                }, null);
                            } else {
                                ToastUtils.makeShortText(context, "只能对找到的物品发起领回单");
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}




class ThingViewViewHolder extends RecyclerView.ViewHolder {

    ThingviewLayoutBinding d;

    public ThingViewViewHolder(@NonNull ThingviewLayoutBinding d) {
        super(d.getRoot());
        this.d = d;
    }
}
