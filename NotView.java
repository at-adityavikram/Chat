package com.pd.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class NotView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_noti_view);
        String sender = getIntent().getStringExtra("sender");
        UserDetails.username = getIntent().getStringExtra("rvr");
        if (!sender.equals("XI - J"))
        {
            UserDetails.chatWith = sender;
        }
        else
        {
            UserDetails.chatWith = "1Science";
        }
        startActivity(new Intent(NotView.this, Chat.class));
    }


}