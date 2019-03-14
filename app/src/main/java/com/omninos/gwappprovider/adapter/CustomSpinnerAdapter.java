package com.omninos.gwappprovider.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.omninos.gwappprovider.R;


public class CustomSpinnerAdapter extends BaseAdapter {

    Activity activity;
    String[] cardList;
    int[] cardImageList;

    public CustomSpinnerAdapter(Activity activity, String[] cardList, int[] cardImageList) {
        this.activity =activity;
        this.cardList = cardList;
        this.cardImageList = cardImageList;
    }

    @Override
    public int getCount() {
        return cardList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.spinner_layout,null);
        TextView tv_spinner_text = view.findViewById(R.id.tv_spinner_text);
        ImageView iv_card_image = view.findViewById(R.id.iv_card_logo);
        if(i>0){
            iv_card_image.setVisibility(View.VISIBLE);
            iv_card_image.setImageResource(cardImageList[i]);
        }

        tv_spinner_text.setText(cardList[i]);

        return view;
    }



}