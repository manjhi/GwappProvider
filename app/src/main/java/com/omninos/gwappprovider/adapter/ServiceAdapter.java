package com.omninos.gwappprovider.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.modelClasses.ServiceModel;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    Context context;
    private List<ServiceModel.Detail> details;
    MoveTonext moveTonext;

    public interface MoveTonext{
        void ClickButton(int position);
    }

    public ServiceAdapter(Context context, List<ServiceModel.Detail> details, MoveTonext moveTonext) {
        this.context = context;
        this.details = details;
        this.moveTonext = moveTonext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_service_layout,viewGroup,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.heading.setText(details.get(i).getTitle());
        Glide.with(context).load(details.get(i).getImage()).into(myViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView heading;
        private LinearLayout construction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            heading=itemView.findViewById(R.id.heading);
            construction=itemView.findViewById(R.id.construction);
            construction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.construction:
                    moveTonext.ClickButton(getLayoutPosition());
                    break;
            }
        }
    }
}
