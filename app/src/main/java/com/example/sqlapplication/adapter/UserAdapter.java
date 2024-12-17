package com.example.sqlapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlapplication.MyApplication;
import com.example.sqlapplication.R;
import com.example.sqlapplication.data.dto.MyRequestBody;
import com.example.sqlapplication.data.model.User;
import com.example.sqlapplication.data.state.UserPermission;
import com.example.sqlapplication.databinding.UserLayoutBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.manager.DelManagerRequest;
import com.example.sqlapplication.net.manager.SetManagerRequest;
import com.example.sqlapplication.utils.DialogUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    List<User> userList;
    boolean ifManager;
    Handler handler = new Handler(Looper.getMainLooper());
    public UserAdapter(List<User> userList, UserPermission userPermission) {
        this.userList = userList;
        if (userPermission == UserPermission.MANAGER) {
            ifManager = true;
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserLayoutBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_layout, null, false);
        return new UserViewHolder(b);
    }


    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = userList.get(position);
        Context context = holder.b.getRoot().getContext();
        holder.b.setUser(user);
        if (ifManager) {
            // 删除管理员
            holder.b.userItem.setOnLongClickListener(v->{
                DialogUtils.showEnsureDialog(context, "撤销管理员权限", "是否确认撤销该管理员的权限", "确定", new DialogUtils.DialogCallback() {
                    @Override
                    public void doSth(View view) {
                        DelManagerRequest.del(new DefaultRequestListener() {
                            @Override
                            protected void successHandle(JsonObject data) {
                                int index = holder.getLayoutPosition();
                                handler.post(()->{
                                    userList.remove(index);
                                    notifyItemRemoved(index);
                                });
                                ToastUtils.makeShortText(context, "删除管理员权限成功");
                            }

                            @Override
                            protected void failHandle(String msg) {
                                ToastUtils.makeShortText(context, msg);
                            }

                            @Override
                            protected void errorHandle(Exception e) {
                                ToastUtils.makeShortText(context, e.getMessage());
                            }
                        }, new MyRequestBody<>(TokenUtils.getToken(context), user.getId()));
                    }
                }, null);
                return false;
            });
        } else {
            holder.b.userItem.setOnLongClickListener(v->{
                DialogUtils.showEnsureDialog(context, "设置管理员权限", "是否确认设置该管理员的权限", "确定", new DialogUtils.DialogCallback() {
                    @Override
                    public void doSth(View view) {
                        SetManagerRequest.set(new DefaultRequestListener() {
                            @Override
                            protected void successHandle(JsonObject data) {
                                int index = holder.getLayoutPosition();
                                handler.post(()->{
                                    userList.remove(index);
                                    notifyItemRemoved(index);
                                });
                                ToastUtils.makeShortText(context, "设置管理员权限成功");
                            }

                            @Override
                            protected void failHandle(String msg) {
                                ToastUtils.makeShortText(context, msg);
                            }

                            @Override
                            protected void errorHandle(Exception e) {
                                ToastUtils.makeShortText(context, e.getMessage());
                            }
                        }, new MyRequestBody<>(TokenUtils.getToken(context), user.getId()));
                    }
                }, null);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder {
    UserLayoutBinding b;

    public UserViewHolder(@NonNull UserLayoutBinding b) {
        super(b.getRoot());
        this.b = b;
    }
}
