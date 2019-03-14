package com.omninos.gwappprovider.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.modelClasses.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    Context context;
    List<ChatModel> models;

    public ChatAdapter(Context context, List<ChatModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       if (ChatModel.MSG_TYPE_RECEIVED.equalsIgnoreCase(models.get(position).getMsgType())){
           holder.right.setVisibility(View.GONE);
           holder.left.setVisibility(View.VISIBLE);
           holder.leftText.setText(models.get(position).getMsgContent());
       }else if (ChatModel.MSG_TYPE_SENT.equalsIgnoreCase(models.get(position).getMsgType())){
           holder.right.setVisibility(View.VISIBLE);
           holder.left.setVisibility(View.GONE);
           holder.RightText.setText(models.get(position).getMsgContent());
       }
    }

    @Override
    public int getItemCount() {
        if (models==null){
            models=new ArrayList<ChatModel>();
        }
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView leftText,RightText;
        LinearLayout left,right;
        public MyViewHolder(View itemView) {
            super(itemView);
            left=itemView.findViewById(R.id.left);
            right=itemView.findViewById(R.id.right);
            leftText=itemView.findViewById(R.id.leftChat);
            RightText=itemView.findViewById(R.id.rightChat);
        }
    }
}
