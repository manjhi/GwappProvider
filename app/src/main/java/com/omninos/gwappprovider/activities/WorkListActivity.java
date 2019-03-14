package com.omninos.gwappprovider.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.ConstantData;
import com.omninos.gwappprovider.ViewModels.WorkListViewModel;
import com.omninos.gwappprovider.adapter.WorkListAdapter;
import com.omninos.gwappprovider.modelClasses.WorkListModel;

import java.util.ArrayList;
import java.util.List;

public class WorkListActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView centerTitle;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private WorkListActivity activity;
    private WorkListAdapter adapter;
    private WorkListViewModel viewModel;
    private List<WorkListModel> listModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);
        activity = WorkListActivity.this;

        viewModel = ViewModelProviders.of(activity).get(WorkListViewModel.class);

        initView();
        SetUps();
        getWorkList();
    }

    private void getWorkList() {
        viewModel.workListModelLiveData(activity, App.getAppPreference().GetString(activity, ConstantData.USERID)).observe(activity, new Observer<WorkListModel>() {
            @Override
            public void onChanged(@Nullable WorkListModel workListModel) {
                if (workListModel.getSuccess().equalsIgnoreCase("1")) {
                    listModels.add(workListModel);

                    adapter = new WorkListAdapter(activity, listModels, "List", new WorkListAdapter.CheckDetail() {
                        @Override
                        public void Movenext(int position) {
//                            if (listModels.get(position).getDetails().get(position).getProviderStatus().equalsIgnoreCase("0")){
                            Intent intent = new Intent(activity, RequestActivity.class);
                            intent.putExtra("Status", "Work");
                            intent.putExtra("paymentType", listModels.get(position).getDetails().get(position).getPaymentType());
                            intent.putExtra("userImage", listModels.get(position).getDetails().get(position).getUserimage());
                            intent.putExtra("lAtitude", listModels.get(position).getDetails().get(position).getLocationLatitude());
                            intent.putExtra("longitude", listModels.get(position).getDetails().get(position).getLocationLongitude());
                            intent.putExtra("userName", listModels.get(position).getDetails().get(position).getUsername());
                            intent.putExtra("userServices", listModels.get(position).getDetails().get(position).getTitle());
                            intent.putExtra("problemDetail", listModels.get(position).getDetails().get(position).getComments());
                            intent.putExtra("address", listModels.get(position).getDetails().get(position).getAddress());
                            intent.putExtra("id", listModels.get(position).getDetails().get(position).getId());
                            startActivity(intent);
//                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(activity, workListModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        centerTitle = findViewById(R.id.centerTitle);
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerView);

    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Work List");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }
}
