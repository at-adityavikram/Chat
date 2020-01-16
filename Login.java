package com.pd.chatapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.*;
import android.support.v4.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.evernote.android.job.JobManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import android.provider.Settings.Secure;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button loginButton;
    String user, pass;
    public static final int REQUEST_BOTH_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        MultiDex.install(this);
        Firebase.setAndroidContext(this);
        final ImageView vhh = findViewById(R.id.loader);
        final ImageView loar = findViewById(R.id.loaderx);
        vhh.setVisibility(View.VISIBLE);
        loar.setVisibility(View.VISIBLE);
        int writingp = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (writingp == PackageManager.PERMISSION_GRANTED)
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            user = tm.getSubscriberId();
            String url = "https://scichat-xiscience.firebaseio.com/Tommy.json";
            //final ProgressDialog pd = new ProgressDialog(Login.this);
            //pd.setMessage("Loading...");
            //pd.setCancelable(false);
            //pd.show();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    if(s.equals("null")){
                        Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_LONG).show();
                    }
                    else{
                        try {
                            JSONObject obj = new JSONObject(s);

                            if(!obj.has(user)){
                                Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_LONG).show();
                            }
                            else {
                                UserDetails.username = obj.getJSONObject(user).getString("password");
                                Firebase reference4 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
                                reference4.child("Stat").child("stat").setValue("ONLINE");
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user", UserDetails.username);
                                editor.commit();
                                JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                                ComponentName componentName = new ComponentName(Login.this, DbUpdateJobService.class.getName());
                                JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(1000).build();
                                jobScheduler.schedule(jobInfo);
                                startActivity(new Intent(Login.this, User.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            vhh.setVisibility(View.GONE);
                            loar.setVisibility(View.GONE);
                        }
                    }

                    vhh.setVisibility(View.GONE);
                    loar.setVisibility(View.GONE);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                    vhh.setVisibility(View.GONE);
                    loar.setVisibility(View.GONE);
                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
            rQueue.add(request);
        }
        else
        {
            String[] perm = {Manifest.permission.READ_PHONE_STATE};
            ActivityCompat.requestPermissions(this, perm, REQUEST_BOTH_STORAGE);
        }
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws SecurityException{
                user = username.getText().toString();
                pass = password.getText().toString();
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                final String phone = tm.getSubscriberId();

                if(user.equals("")){
                    username.setError("Can't Be Blank");
                }
                else if(pass.equals("")){
                    password.setError("Can't Be Blank");
                }
                else{
                    String url = "https://scichat-xiscience.firebaseio.com/Tommy.json";
                    //final ProgressDialog pd = new ProgressDialog(Login.this);
                    //pd.setMessage("Loading...");
                    //pd.setCancelable(false);
                    //pd.show();
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        String url = "https://scichat-xiscience.firebaseio.com/Tommy.json";

                                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                            @Override
                                            public void onResponse(String s) {
                                                Firebase reference = new Firebase("https://scichat-xiscience.firebaseio.com/Tommy");

                                                if(s.equals("null")) {
                                                    reference.child(phone).child("password").setValue(user);
                                                    }
                                                else {
                                                    try {
                                                        JSONObject obj = new JSONObject(s);

                                                        if (!obj.has(phone)) {
                                                            reference.child(phone).child("password").setValue(user);
                                                            } else {
                                                            }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }

                                        },new Response.ErrorListener(){
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                System.out.println("" + volleyError );
                                            }
                                        });
                                        Firebase reference4 = new Firebase("https://scichat-xiscience.firebaseio.com/" + user + "_Stat");
                                        reference4.child("Stat").child("stat").setValue("ONLINE");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("user", user);
                                        editor.commit();
                                        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                                        rQueue.add(request);
                                        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                                        ComponentName componentName = new ComponentName(Login.this, DbUpdateJobService.class.getName());
                                        JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(1000).build();
                                        jobScheduler.schedule(jobInfo);
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        startActivity(new Intent(Login.this, User.class));
                                    }
                                    else {
                                        Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            vhh.setVisibility(View.GONE);
                            loar.setVisibility(View.GONE);
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            vhh.setVisibility(View.GONE);
                            loar.setVisibility(View.GONE);
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) throws SecurityException
    {
        switch (requestCode)

        {

            case REQUEST_BOTH_STORAGE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    final ImageView vhh = findViewById(R.id.loader);
                    final ImageView loar = findViewById(R.id.loaderx);
                    vhh.setVisibility(View.VISIBLE);
                    loar.setVisibility(View.VISIBLE);
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    user = tm.getSubscriberId();
                    String url = "https://scichat-xiscience.firebaseio.com/Tommy.json";
                    //final ProgressDialog pd = new ProgressDialog(Login.this);
                    //pd.setMessage("Loading...");
                    //pd.setCancelable(false);
                    //pd.show();
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        UserDetails.username = obj.getJSONObject(user).getString("password");
                                        Firebase reference4 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
                                        reference4.child("Stat").child("stat").setValue("ONLINE");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("user", UserDetails.username);
                                        editor.commit();
                                        JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                                        ComponentName componentName = new ComponentName(Login.this, DbUpdateJobService.class.getName());
                                        JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(1000).build();
                                        jobScheduler.schedule(jobInfo);
                                        startActivity(new Intent(Login.this, User.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    vhh.setVisibility(View.GONE);
                                    loar.setVisibility(View.GONE);
                                }
                            }

                            vhh.setVisibility(View.GONE);
                            loar.setVisibility(View.GONE);
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            vhh.setVisibility(View.GONE);
                            loar.setVisibility(View.GONE);
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }
                else
                {
                    String[] perm = {Manifest.permission.READ_PHONE_STATE};
                    ActivityCompat.requestPermissions(this, perm, REQUEST_BOTH_STORAGE);
                }
                break;
        }
    }

}