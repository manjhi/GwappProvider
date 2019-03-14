package com.omninos.gwappprovider.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;

public class CompleteWorkActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView centerTitle;
    private ImageView backButton;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_work);

        initView();
        SetUps();
    }

    private void initView() {
        centerTitle=findViewById(R.id.centerTitle);
        backButton=findViewById(R.id.backButton);
        submit=findViewById(R.id.submit);
    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Work Completed");
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.submit:
                startActivity(new Intent(CompleteWorkActivity.this,HomeActivity.class));
                finishAffinity();
                break;
        }
    }
}
