package com.pd.chatapp;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.service.notification.StatusBarNotification;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DbUpdateJobService extends JobService
{
    String luser = "";

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

    @Override public boolean onStartJob(final JobParameters jobParameters) throws SecurityException
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Firebase.setAndroidContext(this);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            luser = pref.getString("user", null);
            Firebase ref = new Firebase("https://scichat-xiscience.firebaseio.com/" + luser + "_Refresh");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s)
                {
                    Map map = dataSnapshot.getValue(Map.class);
                    String userName = map.get("user").toString();
                    if (!UserDetails.chatWith.equals(userName)){
                        if (!userName.equals("XI - J"))
                        {
                            String ker = map.get("key").toString();
                            Firebase ry = new Firebase("https://scichat-xiscience.firebaseio.com/" + userName + "_" + luser);
                            ry.child(ker).child("stat").setValue("D");
                        }
                        else {
                            if (!UserDetails.chatWith.equals("1Science"))
                            {
                                String ker = map.get("key").toString();
                                Firebase ry = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                                ry.child(ker).child(luser).setValue("D");
                            }
                        }
                        if (!getCurrent().contains(Integer.toString(giveId(userName))))
                        {
                            if (UserDetails.chatWith.equals("1Science") && userName.equals("XI - J"))
                            {
                                //nothing
                            }
                            else
                            {
                                addNotification(userName, luser);
                            }
                        }
                    }
                    dataSnapshot.getRef().setValue(null);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName componentName = new ComponentName(DbUpdateJobService.this, DbUpdateJobService.class.getName());
            JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(4000).build();
            jobScheduler.schedule(jobInfo);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        return false;
    }

    public void addNotification(String sender, String rcvr)
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannels(sender);
        String channel_id = "";
        Intent notificationIntent = new Intent(this, NotView.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("sender", sender);
        notificationIntent.putExtra("rvr", rcvr);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(DbUpdateJobService.this, giveId(sender), notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            channel_id = notificationManager.getNotificationChannel(sender + "_char").getId();
            pendingIntent = PendingIntent.getActivity(DbUpdateJobService.this, giveId(sender), notificationIntent.putExtra("importance", notificationManager.getNotificationChannel(channel_id).getImportance()).putExtra("channel_id", channel_id), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channel_id)
                        .setContentTitle(sender)
                        .setContentText("New Message From " + sender)
                        .setSound(alarmSound)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.appicon);
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

    private void createNotificationChannels(String s)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(s + "_char", s, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
            {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    public String getCurrent()
    {
        String hd = "";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23)
        {
            try
            {
                StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
                for (StatusBarNotification activeNotification : activeNotifications)
                {
                    if (hd == "")
                    {
                        hd += activeNotification.getId();
                    }
                    else
                    {
                        hd += " " + activeNotification.getId();
                    }
                }
            }
            catch (Throwable e)
            {

            }
        }
        return hd;
    }

}