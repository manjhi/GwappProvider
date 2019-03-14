package com.omninos.gwappprovider.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.adapter.WorkListAdapter;

public class WorkLoadActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView centerTitle;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private WorkLoadActivity activity;
    private WorkListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_load);

        activity = WorkLoadActivity.this;


        initView();
        SetUps();
    }

    private void initView() {
        centerTitle = findViewById(R.id.centerTitle);
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerView);

    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Work Load");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new WorkListAdapter(activity,"Load");
        recyclerView.setAdapter(adapter);
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
