package com.pd.chatapp;

import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class User extends AppCompatActivity {
    ListView usersList;
    TextView cuser;
    private ArrayAdapter mAdapter;
    ArrayList<String> alz = new ArrayList<>();
    Firebase reference1;

    public String create(String str)
    {
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Chat");
        if (!f.exists())
        {
            f.mkdirs();
        }
        String str2 = Environment.getExternalStorageDirectory() + File.separator + "Chat" + File.separator + str;
        return str2;
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if (KeyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(KeyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#010728")));
        setContentView(R.layout.activity_user);
        ProgressDialog pdx = new ProgressDialog(User.this);
        pdx.setMessage("Loading...");
        pdx.setCancelable(false);
        pdx.show();
        UserDetails.al.clear();
        Firebase.setAndroidContext(this);
        Firebase referenceee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referenceee.child("Stat").child("stat").setValue("ONLINE");
        usersList = (ListView)findViewById(R.id.userList);
        cuser = findViewById(R.id.cusers);
        cuser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cuser.setPadding(10, 5, 10,14);
        cuser.setText("Welcome, " + UserDetails.username);
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alz){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView item = (TextView) super.getView(position,convertView,parent);
                item.setTextColor(Color.parseColor("#ffffff"));
                item.setTypeface(item.getTypeface(), Typeface.BOLD);
                item.setBackgroundColor(Color.parseColor("#525252"));
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                return item;
            }
        };
        usersList.setAdapter(mAdapter);
        reference1 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_User");
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String userName = map.get("user").toString();
                UserDetails.al.add(userName);
                mAdapter.add(userName);
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
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = (String) usersList.getItemAtPosition(position);
                startActivity(new Intent(User.this, Chat.class));
            }
        });

        EditText se = findViewById(R.id.search);
        se.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pdx.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton1)
        {
            startActivity(new Intent(User.this, Users.class));
        }
        return super.onOptionsItemSelected(item);
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