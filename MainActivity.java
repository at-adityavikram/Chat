package com.example.job;

import android.app.Activity;
import android.os.Bundle;
import android.app.job.JobInfo; 
import android.app.job.JobScheduler; 
import android.widget.*;
import android.content.ComponentName; 
import android.content.SharedPreferences.*;
import android.preference.PreferenceManager;
import android.os.*;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import java.net.*;
import android.widget.*;
import java.util.*;
import android.speech.tts.*;
import android.speech.tts.TextToSpeech.*;
import android.speech.*;
import android.content.*;
import android.content.Context.*;
import android.content.pm.*;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import java.util.concurrent.*;
import java.io.*; 

public class MainActivity extends Activity {
Context df = this;
SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);
final EditText fg = findViewById(R.id.sta);
Button start = findViewById(R.id.start);
start.setOnClickListener(new OnClickListener()
{
public void onClick(View v)
{
 
pref = getApplicationContext().getSharedPreferences("MyPref", 0);
Editor editor = pref.edit();
editor.putString("user", fg.getText().toString());
editor.commit();
JobScheduler jobScheduler = (JobScheduler)getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE); 
ComponentName componentName = new ComponentName(MainActivity.this, DbUpdateJobService.class.getName()); 
JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(1000).build(); 
jobScheduler.schedule(jobInfo);
}
});
Button stop = findViewById(R.id.stop);
stop.setOnClickListener(new OnClickListener()
{
public void onClick(View v)
{
JobScheduler jobScheduler = (JobScheduler)getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
jobScheduler.cancelAll();
}
});
    }
}