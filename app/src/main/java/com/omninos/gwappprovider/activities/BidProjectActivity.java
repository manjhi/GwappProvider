package com.omninos.gwappprovider.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;

public class BidProjectActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView centerTitle;
    private ImageView backButton;
    private Button place_bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_project);

        initView();
        SetUps();

    }

    private void SetUps() {

        backButton.setOnClickListener(this);
        centerTitle.setText("Bid Project");
        place_bid.setOnClickListener(this);
    }

    private void initView() {

        centerTitle = findViewById(R.id.centerTitle);
        backButton = findViewById(R.id.backButton);
        place_bid = findViewById(R.id.place_bid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.place_bid:
                startActivity(new Intent(BidProjectActivity.this, HomeActivity.class));
                finishAffinity();
                break;
        }
    }
}
