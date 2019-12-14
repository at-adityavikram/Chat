package com.journaldev.notificationchannels;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    
    Button btnNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        
        

        
        

        





        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
addNotification("Abhay", "Aditya");
addNotification("Aditya", "Aditya");
                
                   
                
            }
        });
    }

    private void initViews() {
        
        btnNotification = findViewById(R.id.btnNotification);
    }

    public void addNotification(String sender, String rcvr) {
NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
createNotificationChannels(sender);
     String channel_id = "";  


        
        
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, MainActivity.class), 0);
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        
                        
                        channel_id = notificationManager.getNotificationChannel(sender + "_char").getId();
                        pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, MainActivity.class).putExtra("importance", notificationManager.getNotificationChannel(channel_id).getImportance()).putExtra("channel_id", channel_id), PendingIntent.FLAG_UPDATE_CURRENT);
                    }      
        
NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channel_id)                       
                        .setContentTitle(sender)
                        
                        .setContentText("New Message From " + sender)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)

                        .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(giveId(sender), builder.build());
    }

    public int giveId(String s)
    {
        int asc = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char j = s.charAt(i);
            int ascii = j;
            asc += ascii;
        }
        return asc;
    }


    private void createNotificationChannels(String s) {
        
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel(s + "_char", s, NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                
                

                
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                    
                }
            }
        
    }


    


}



