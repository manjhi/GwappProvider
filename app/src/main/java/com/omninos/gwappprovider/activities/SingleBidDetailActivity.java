package com.omninos.gwappprovider.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.adapter.SkillAdapter;

import java.util.ArrayList;
import java.util.List;

public class SingleBidDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bid_on_this_project;

    private RecyclerView recycler;
    List<String> list=new ArrayList<>();

    private TextView centerTitle;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bid_detail);

        initView();
        SetUps();
    }

    private void SetUps() {
        bid_on_this_project.setOnClickListener(this);

        recycler.setLayoutManager(new GridLayoutManager(SingleBidDetailActivity.this, 3));

        list.add("Graphics designer");
        list.add("UI & UX designer");
        list.add("Android");
        list.add("iOS");

        SkillAdapter adapter=new SkillAdapter(list,SingleBidDetailActivity.this);
        recycler.setAdapter(adapter);


        backButton.setOnClickListener(this);
        centerTitle.setText("View Details");
    }

    private void initView() {
        bid_on_this_project=findViewById(R.id.bid_on_this_project);
        recycler=findViewById(R.id.recycler);

        centerTitle=findViewById(R.id.centerTitle);
        backButton=findViewById(R.id.backButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bid_on_this_project:
                startActivity(new Intent(SingleBidDetailActivity.this,BidProjectActivity.class));
                break;

            case R.id.backButton:
                onBackPressed();
                break;
        }
    }
}
