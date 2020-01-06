package com.example.job;

import android.app.job.JobParameters; 
import android.app.job.JobService; 
import java.util.*;
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

private boolean isAppRunning() 
{ 
ActivityManager m = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE ); 
List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(10); 
Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator(); 
int n=0; 
while(itr.hasNext())
{ 
n++; 
itr.next(); 
} 
if(n==1)
{ 
return false; 
} 
return true; 
}

   @Override 
public boolean onStartJob(final JobParameters jobParameters) 
{  
if (isAppRunning())
{
Toast.makeText(this, "Running", Toast.LENGTH_LONG).show();
}
else
{
Toast.makeText(this, "Not Running", Toast.LENGTH_LONG).show();
}
JobScheduler jobScheduler = (JobScheduler)getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE); 
ComponentName componentName = new ComponentName(DbUpdateJobService.this, DbUpdateJobService.class.getName()); 
JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(10000).build(); 
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