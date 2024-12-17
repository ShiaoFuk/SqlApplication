package com.example.sqlapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlapplication.R;
import com.example.sqlapplication.data.dto.MyRequestBody;
import com.example.sqlapplication.data.model.Form;
import com.example.sqlapplication.data.state.FormState;
import com.example.sqlapplication.databinding.FormLayoutBinding;
import com.example.sqlapplication.net.DefaultRequestListener;
import com.example.sqlapplication.net.form.DelFormRequest;
import com.example.sqlapplication.net.manager.HandleBadFormRequest;
import com.example.sqlapplication.net.manager.HandleFormRequest;
import com.example.sqlapplication.utils.DialogUtils;
import com.example.sqlapplication.utils.ToastUtils;
import com.example.sqlapplication.utils.TokenUtils;
import com.google.gson.JsonObject;

import java.util.List;

public class ManagerFormAdapter extends RecyclerView.Adapter<MFormViewHolder> {
    List<Form> formList;
    Handler handler = new Handler(Looper.getMainLooper());
    public ManagerFormAdapter(List<Form> formList) {
        this.formList = formList;
    }

    

    @NonNull
    @Override
    public MFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FormLayoutBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.form_layout, null, false);
        return new MFormViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull MFormViewHolder holder, int position) {
        FormLayoutBinding b = holder.b;
        Context context = b.getRoot().getContext();
        b.setForm(formList.get(position));
        b.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (formList.get(holder.getLayoutPosition()).getState() != FormState.UNSOLVED.getVal()) {
                    ToastUtils.makeShortText(context, "只能操作未处理的领回单");
                    return false;
                }
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.manager_form_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.handle) {
                            DialogUtils.showEnsureDialog(context, "处理领回单", "确定要完成该领回单吗?", "确定", new DialogUtils.DialogCallback() {
                                @Override
                                public void doSth(View view) {
                                    HandleFormRequest.set(new DefaultRequestListener() {
                                        @Override
                                        protected void successHandle(JsonObject data) {
                                            int index = holder.getLayoutPosition();
                                            formList.remove(index);
                                            handler.post(()->{
                                                notifyItemRemoved(index);
                                            });
                                            ToastUtils.makeShortText(context, "成功把领回单设为完成状态");
                                        }

                                        @Override
                                        protected void failHandle(String msg) {
                                            ToastUtils.makeShortText(context, msg);

                                        }

                                        @Override
                                        protected void errorHandle(Exception e) {
                                            ToastUtils.makeShortText(context, e.getMessage());
                                        }
                                    }, new MyRequestBody<>(TokenUtils.getToken(context), formList.get(holder.getLayoutPosition()).getId()));

                                }
                            }, null);
                        } else if (item.getItemId() == R.id.handle_bad) {
                            DialogUtils.showEnsureDialog(context, "处理误领", "确定要设置该领回单为误领吗?", "确定", new DialogUtils.DialogCallback() {
                                @Override
                                public void doSth(View view) {
                                    HandleBadFormRequest.set(new DefaultRequestListener() {
                                        @Override
                                        protected void successHandle(JsonObject data) {
                                            int index = holder.getLayoutPosition();
                                            formList.remove(index);
                                            handler.post(()->{
                                                notifyItemRemoved(index);
                                            });
                                            ToastUtils.makeShortText(context, "成功把领回单设为误领");
                                        }

                                        @Override
                                        protected void failHandle(String msg) {
                                            ToastUtils.makeShortText(context, msg);

                                        }

                                        @Override
                                        protected void errorHandle(Exception e) {
                                            ToastUtils.makeShortText(context, e.getMessage());
                                        }
                                    }, new MyRequestBody<>(TokenUtils.getToken(context), formList.get(holder.getLayoutPosition()).getId()));

                                }
                            }, null);
                        }
                        return false;
                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }
}


class MFormViewHolder extends RecyclerView.ViewHolder {
    FormLayoutBinding b;
    public MFormViewHolder(@NonNull FormLayoutBinding b) {
        super(b.getRoot());
        this.b = b;
    }
}
