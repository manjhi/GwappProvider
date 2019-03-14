package com.omninos.gwappprovider.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.modelClasses.WorkListModel;

import java.util.List;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.MyViewHolder> {

    Context context;
    String Type;
    private List<WorkListModel> listModels;
    private CheckDetail checkDetail;


    public interface CheckDetail {
        void Movenext(int position);
    }


    public WorkListAdapter(Context context, List<WorkListModel> listModels, String type, CheckDetail checkDetail) {
        this.context = context;
        this.listModels = listModels;
        Type = type;
        this.checkDetail = checkDetail;
    }

    public WorkListAdapter(Context context, String type) {
        this.context = context;
        Type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_worklist_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (Type.equalsIgnoreCase("Load")) {
            myViewHolder.notification.setVisibility(View.GONE);
        } else {
            myViewHolder.notification.setVisibility(View.VISIBLE);
            for (int j = 0; j < listModels.get(i).getDetails().size(); j++) {
                myViewHolder.dateData.setText(listModels.get(i).getDetails().get(j).getCreatedDate());
                myViewHolder.address.setText(listModels.get(i).getDetails().get(j).getAddress());
                myViewHolder.category.setText(listModels.get(i).getDetails().get(j).getTitle());
                myViewHolder.timeData.setText(listModels.get(i).getDetails().get(j).getCreatedTime());



            }
        }

    }

    @Override
    public int getItemCount() {
        if (Type.equalsIgnoreCase("Load")) {
            return 20;
        } else {
            return listModels.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView notification;
        private TextView dateData, timeData, category, address;
        private RelativeLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notification);
            dateData = itemView.findViewById(R.id.dateData);
            timeData = itemView.findViewById(R.id.timeData);
            category = itemView.findViewById(R.id.category);
            address = itemView.findViewById(R.id.address);
            mainLayout=itemView.findViewById(R.id.mainLayout);
            mainLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mainLayout:
                    checkDetail.Movenext(getLayoutPosition());
                    break;
            }
        }
    }
}
