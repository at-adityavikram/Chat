package com.pd.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Info extends AppCompatActivity {
    TextView cuser;
    TextView cuser1;

    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if (KeyCode == KeyEvent.KEYCODE_BACK)
        {
            this.finish();
            startActivity(new Intent(Info.this, Chat.class));
            return true;
        }
        return super.onKeyDown(KeyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_info);
        Firebase.setAndroidContext(this);
        Firebase referenceee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referenceee.child("Stat").child("stat").setValue("ONLINE");
        cuser = findViewById(R.id.cuser2);
        cuser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cuser.setPadding(10, 5, 10,14);

        cuser1 = findViewById(R.id.cuser3);
        cuser1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cuser1.setPadding(10, 5, 10,14);

        Firebase ry = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
        ry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(UserDetails.ke))
                {
                    Map map = dataSnapshot.getValue(Map.class);
                    String re = "";
                    String de = "";
                    for (int f = 0; f < UserDetails.res.size(); f++)
                    {
                        if (!UserDetails.res.get(f).equals("1Science"))
                        {
                            String hj = map.get(UserDetails.res.get(f)).toString();
                            if (hj.equals("R"))
                            {
                                re += System.getProperty("line.separator") + UserDetails.res.get(f);
                            }
                            else if (hj.equals("D"))
                            {
                                de += System.getProperty("line.separator") + UserDetails.res.get(f);
                            }
                        }
                    }
                    if (re.equals(""))
                    {
                        re += System.getProperty("line.separator") + "NONE";
                    }

                    if (de.equals(""))
                    {
                        de += System.getProperty("line.separator") + "NONE";
                    }

                    cuser1.setText("Read By:" + System.getProperty("line.separator") + re);
                    cuser.setText("Delivered To:" + System.getProperty("line.separator") + de);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(UserDetails.ke))
                {
                    Map map = dataSnapshot.getValue(Map.class);
                    String re = "";
                    String de = "";
                    for (int f = 0; f < UserDetails.res.size(); f++)
                    {
                        if (!UserDetails.res.get(f).equals("1Science"))
                        {
                            String hj = map.get(UserDetails.res.get(f)).toString();
                            if (hj.equals("R"))
                            {
                                re += System.getProperty("line.separator") + UserDetails.res.get(f);
                            }
                            else if (hj.equals("D"))
                            {
                                de += System.getProperty("line.separator") + UserDetails.res.get(f);
                            }
                        }
                    }
                    if (re.equals(""))
                    {
                        re += System.getProperty("line.separator") + "NONE";
                    }

                    if (de.equals(""))
                    {
                        de += System.getProperty("line.separator") + "NONE";
                    }

                    cuser1.setText("Read By:" + System.getProperty("line.separator") + re);
                    cuser.setText("Delivered To:" + System.getProperty("line.separator") + de);
                }
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

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Firebase referencee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referencee.child("Stat").child("stat").setValue("OFFLINE");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        UserDetails.username = pref.getString("user", null);
        Firebase referenceee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referenceee.child("Stat").child("stat").setValue("ONLINE");
    }

}