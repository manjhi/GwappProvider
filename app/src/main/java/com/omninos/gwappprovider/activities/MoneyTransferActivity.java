package com.omninos.gwappprovider.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;

public class MoneyTransferActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView centerTitle;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);


        initView();
        SetUps();
    }

    private void initView() {
        centerTitle=findViewById(R.id.centerTitle);
        backButton=findViewById(R.id.backButton);
    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Monet Transfer");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }
}
