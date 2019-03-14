package com.omninos.gwappprovider.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.adapter.CustomSpinnerAdapter;

public class MyWallet extends AppCompatActivity implements View.OnClickListener {


    private TextView centerTitle;
    private ImageView backButton;

    Spinner spCard;
    String[] cardList = {"Select Card", "Debit Card", "Credit Card", "Master Card"};
    int[] cardImageList = {R.drawable.ic_visa,R.drawable.ic_visa,R.drawable.ic_credit_card,R.drawable.ic_mastercard};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        initView();
        SetUps();

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MyWallet.this,cardList,cardImageList);
        spCard.setAdapter(customSpinnerAdapter);
        spCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    String card = cardList[i];
                    Toast.makeText(MyWallet.this, card, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Monet Transfer");
    }

    private void initView() {
        spCard=findViewById(R.id.sp_card);
        centerTitle=findViewById(R.id.centerTitle);
        backButton=findViewById(R.id.backButton);
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
