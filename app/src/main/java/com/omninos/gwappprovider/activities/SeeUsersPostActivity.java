package com.omninos.gwappprovider.activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.adapter.JobAdapter;

public class SeeUsersPostActivity extends AppCompatActivity implements View.OnClickListener {

    private SeeUsersPostActivity activity;
    private RecyclerView recyclerView;
    private JobAdapter adapter;
    private ImageView backButton;
    private TextView centerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_users_post);
        activity = SeeUsersPostActivity.this;

        initView();
        SetUps();
    }

    private void SetUps() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new JobAdapter(activity, new JobAdapter.ClickItems() {
            @Override
            public void OnSelectItem(int position) {
                startActivity(new Intent(activity, SingleBidDetailActivity.class));
            }
        });

        recyclerView.setAdapter(adapter);
        backButton.setOnClickListener(this);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
        centerTitle = findViewById(R.id.centerTitle);

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
