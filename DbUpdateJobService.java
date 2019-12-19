package com.example.job;

import android.app.job.JobParameters; 
import android.app.job.JobService; 
import android.widget.*;
import android.app.Activity;
import android.app.*;
import android.os.*;
import java.lang.*;
import android.app.Service.*;
import android.os.Bundle;
import android.app.job.JobInfo; 
import android.app.job.JobScheduler; 
import android.widget.*;
import android.content.*; 
import android.content.SharedPreferences.*;
import android.preference.PreferenceManager;

public class DbUpdateJobService extends JobService
{ 
static String con = "";
int f = 1;
SharedPreferences pref;

   @Override public boolean onStartJob(final JobParameters jobParameters) 
{  
pref = getApplicationContext().getSharedPreferences("MyPref", 0);
Toast.makeText(this, pref.getString("user", null), Toast.LENGTH_LONG).show();
JobScheduler jobScheduler = (JobScheduler)getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE); 
ComponentName componentName = new ComponentName(DbUpdateJobService.this, DbUpdateJobService.class.getName()); 
JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(1000).build(); 
jobScheduler.schedule(jobInfo);
jobFinished(jobParameters, false);
return true;
 } 

@Override 
public boolean onStopJob(JobParameters jobParameters) 
{ 

return false;
 }

 
}