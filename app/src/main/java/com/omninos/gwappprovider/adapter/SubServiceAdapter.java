package com.omninos.gwappprovider.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.modelClasses.ServiceModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SubServiceAdapter extends RecyclerView.Adapter<SubServiceAdapter.MyViewHolder> {

    Context context;
    List<ServiceModel.SubService> subServices;
    Choose choose;

    List<String> ServiceIds = new ArrayList<>();

    public interface Choose {
        void ChooseSubService(int position);
    }

    public SubServiceAdapter(Context context, List<ServiceModel.SubService> subServices, Choose choose) {
        this.context = context;
        this.subServices = subServices;
        this.choose = choose;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_subservce_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.headingData.setText(subServices.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return subServices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView headingData;
        private CardView cardLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headingData = itemView.findViewById(R.id.headingData);
            cardLayout = itemView.findViewById(R.id.cardLayout);
            cardLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardLayout:
                    if (!ServiceIds.contains(App.getSinltonPojo().getSubServicesData().get(getLayoutPosition()).getId())) {
                        ServiceIds.add(App.getSinltonPojo().getSubServicesData().get(getLayoutPosition()).getId());
                        headingData.setTextColor(Color.RED);
                        choose.ChooseSubService(getLayoutPosition());
                    } else {
                        ServiceIds.remove(App.getSinltonPojo().getSubServicesData().get(getLayoutPosition()).getId());
                        headingData.setTextColor(Color.BLACK);
                        choose.ChooseSubService(getLayoutPosition());
                    }
                    break;
            }
        }
    }
}
