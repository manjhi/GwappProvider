package com.omninos.gwappprovider.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.adapter.ChatAdapter;
import com.omninos.gwappprovider.adapter.ChatListAdapter;
import com.omninos.gwappprovider.modelClasses.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private RelativeLayout content;
    float slideX;
    private RecyclerView drawer_recycler,chatItems;
    private ChatActivity activity;
    private ChatListAdapter chatListAdapter;


    private TextView centerTitle,chat_send_msg;
    private ImageView backButton;
    private EditText chat_input_msg;
    private ChatAdapter adapter;
    List<ChatModel> list=new ArrayList<>();




    @Override
    public void onBackPressed() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
//            finish();
            startActivity(new Intent(activity,HomeActivity.class));
            finishAffinity();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        activity=ChatActivity.this;


        initView();
        SetUpRecyclerView();
        SetUps();
    }

    private void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        content=findViewById(R.id.content);

        drawer_recycler=findViewById(R.id.drawer_recycler);
        centerTitle=findViewById(R.id.centerTitle);
        backButton=findViewById(R.id.backButton);
        chatItems=findViewById(R.id.chatItems);

        chat_send_msg=findViewById(R.id.sendButton);
        chat_input_msg=findViewById(R.id.chat_input_msg);
    }


    private void SetUpRecyclerView() {
        chatItems=findViewById(R.id.chatItems);
        chatItems.setLayoutManager(new LinearLayoutManager(this));

        ChatModel model=new ChatModel("Hi, How are You?",ChatModel.MSG_TYPE_RECEIVED);
        list.add(model);
        adapter=new ChatAdapter(ChatActivity.this,list);
        adapter.notifyDataSetChanged();
        chatItems.setAdapter(adapter);
    }



    private void SetUps() {


        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        drawer_recycler.setLayoutManager(linearLayoutManager);

        chatListAdapter=new ChatListAdapter(activity);
        drawer_recycler.setAdapter(chatListAdapter);


        backButton.setOnClickListener(this);
        centerTitle.setText("Chat");

        chat_send_msg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                onBackPressed();
                break;

            case R.id.sendButton:
                SendMessage();
                break;
        }
    }

    private void SendMessage() {
        String data = chat_input_msg.getText().toString();

        if (!data.isEmpty()) {
            ChatModel model = new ChatModel(data, ChatModel.MSG_TYPE_SENT);
            list.add(model);
            int newMsgPosition = list.size() - 1;
            adapter.notifyItemInserted(newMsgPosition);
            chatItems.scrollToPosition(newMsgPosition);
            chat_input_msg.setText("");
        }else {
            Toast.makeText(this, "write something", Toast.LENGTH_SHORT).show();
        }
    }
}
