package com.omninos.gwappprovider.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omninos.gwappprovider.R;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.MyViewHolder> {

    Context context;
    ClickItems clickItems;


    public interface ClickItems{
        public void OnSelectItem(int position);
    }

    public JobAdapter(Context context, ClickItems clickItems) {
        this.context = context;
        this.clickItems = clickItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_jobs_layout,viewGroup,false);
      return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView clickcard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            clickcard=itemView.findViewById(R.id.clickCard);
            clickcard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.clickCard:
                    clickItems.OnSelectItem(getLayoutPosition());
                    break;
            }
        }
    }
}
