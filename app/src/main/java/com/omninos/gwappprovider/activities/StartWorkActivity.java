package com.omninos.gwappprovider.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omninos.gwappprovider.R;

public class StartWorkActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView centerTitle;
    private ImageView backButton;
    private StartWorkActivity activity;
    private Button startWorkButton,doneWorkButton;
    private LinearLayout startWork,doneWork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);
        activity=StartWorkActivity.this;

        initView();
        SetUps();

    }

    private void initView() {
        centerTitle=findViewById(R.id.centerTitle);
        backButton=findViewById(R.id.backButton);
        startWork=findViewById(R.id.startWork);
        startWorkButton=findViewById(R.id.startWorkButton);
        doneWork=findViewById(R.id.doneWork);
        doneWorkButton=findViewById(R.id.doneWorkButton);

    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Start Work");
        startWorkButton.setOnClickListener(this);
        doneWorkButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startWorkButton:
                startWork.setVisibility(View.GONE);
                doneWork.setVisibility(View.VISIBLE);
                break;
            case R.id.doneWorkButton:
                startActivity(new Intent(activity,CompleteWorkActivity.class));
                break;

            case R.id.backButton:
                onBackPressed();
                break;
        }
    }
}
