package com.omninos.gwappprovider.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.omninos.gwappprovider.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BidCounterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;
    private TextView centerTitle;
    private EditText price, pickDate, serviceTime;
    private String format = "";
    private Button Negotiate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_counter);

        initView();
        SetUps();
    }

    private void initView() {
        backButton=findViewById(R.id.backButton);
        centerTitle=findViewById(R.id.centerTitle);
        price=findViewById(R.id.negotiate_price);
        pickDate=findViewById(R.id.pickDate);
        serviceTime=findViewById(R.id.serviceTime);
        Negotiate=findViewById(R.id.Negotiate);

    }

    private void SetUps() {
        pickDate.setOnClickListener(this);
        pickDate.setFocusable(false);
        serviceTime.setOnClickListener(this);
        serviceTime.setFocusable(false);
        backButton.setOnClickListener(this);
        Negotiate.setOnClickListener(this);
        centerTitle.setText("Bid Counter");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.serviceTime:
                PickTime();
                break;

            case R.id.pickDate:
                PickDate();
                break;

            case R.id.Negotiate:
                NegotiateData();
                break;
        }
    }

    private void NegotiateData() {

    }


    private void PickTime() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        serviceTime.setText(hourOfDay + ":" + minute+" "+format);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void PickDate() {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(
                BidCounterActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker,
                                  int selectedyear, int selectedmonth,
                                  int selectedday) {

                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH,
                        selectedday);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                pickDate.setText(sdf.format(mcurrentDate
                        .getTime()));
            }
        }, mYear, mMonth, mDay);

        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePicker.setTitle("Date of Birth");
        mDatePicker.show();
    }
}
