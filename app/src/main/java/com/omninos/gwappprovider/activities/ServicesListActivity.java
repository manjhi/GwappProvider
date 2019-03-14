package com.omninos.gwappprovider.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.ViewModels.ServiceViewModel;
import com.omninos.gwappprovider.adapter.ServiceAdapter;
import com.omninos.gwappprovider.modelClasses.ServiceModel;

import java.util.ArrayList;
import java.util.List;

public class ServicesListActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next;
    private ServicesListActivity activity;
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private ServiceViewModel viewModel;
    private List<ServiceModel.Detail> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        activity = ServicesListActivity.this;

        viewModel=ViewModelProviders.of(this).get(ServiceViewModel.class);
        initView();
        SetUps();
    }

    //initaite all Id's
    private void initView() {
        next = findViewById(R.id.next);
        recyclerView=findViewById(R.id.recyclerView);
    }

    //setup Actions
    private void SetUps() {
        next.setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //Get List from Server
        viewModel.getServivceList(activity).observe(this, new Observer<ServiceModel>() {
            @Override
            public void onChanged(@Nullable ServiceModel serviceModel) {
                if (serviceModel.getSuccess().equalsIgnoreCase("1")){
                    list=serviceModel.getDetails();

                    adapter=new ServiceAdapter(activity, list, new ServiceAdapter.MoveTonext() {
                        @Override
                        public void ClickButton(int position) {
                            App.getSinltonPojo().setMainServices(list.get(position).getId());
                            App.getSinltonPojo().setSubServicesData(list.get(position).getSubServices());
                            startActivity(new Intent(activity,ChooseSubServicesActivity.class));
                        }
                    });
                    //set data on recyclerView
                    recyclerView.setAdapter(adapter);
                }else {

                }
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
