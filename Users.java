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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    TextView cuser;
    private ArrayAdapter mAdapter;
    Firebase reference3;
    Firebase reference5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_users);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        cuser = findViewById(R.id.cuser);
        cuser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cuser.setPadding(10, 5, 10,14);
        cuser.setText("Select User");
        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();
        Firebase.setAndroidContext(this);
        Firebase referenceee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referenceee.child("Stat").child("stat").setValue("ONLINE");
        String url = "https://scichat-xiscience.firebaseio.com/Tommy.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                System.out.println("Oops!!!");
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (UserDetails.star == 0) {
                    UserDetails.chatWith = (String) usersList.getItemAtPosition(position);
                    reference3 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_User");
                    Map<String, String> map1 = new HashMap<String, String>();
                    map1.put("user", UserDetails.chatWith);
                    reference3.push().setValue(map1);
                    reference5 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_User");
                    Map<String, String> map2 = new HashMap<String, String>();
                    map2.put("user", UserDetails.username);
                    reference5.push().setValue(map2);
                    startActivity(new Intent(Users.this, User.class));
                }
                else if (UserDetails.star == 1)
                {
                    String to = UserDetails.chatWith;
                    UserDetails.chatWith = (String) usersList.getItemAtPosition(position);
                    UserDetails.star = 0;
                    if (!UserDetails.al.contains(UserDetails.chatWith))
                    {
                        reference3 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_User");
                        Map<String, String> map1 = new HashMap<String, String>();
                        map1.put("user", UserDetails.chatWith);
                        reference3.push().setValue(map1);
                        reference5 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_User");
                        Map<String, String> map2 = new HashMap<String, String>();
                        map2.put("user", UserDetails.username);
                        reference5.push().setValue(map2);
                    }
                    Firebase reference1 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith);
                    Firebase reference7 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_Refresh");
                    Firebase reference2 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_" + UserDetails.username);
                    Firebase reference8 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                    String messageText = UserDetails.unm;
                    if(!messageText.equals("")) {
                        Timestamp timestampw = new Timestamp(System.currentTimeMillis());
                        String timestamp = timestampw.toString();
                        String zx = timestamp.charAt(8) + "";
                        zx += timestamp.charAt(9) + "";
                        String time = zx + "/" + timestamp.charAt(5) + timestamp.charAt(6) + "/" + timestamp.charAt(2) + timestamp.charAt(3) + "  " + timestamp.charAt(11) + timestamp.charAt(12) + ":" + timestamp.charAt(14) + timestamp.charAt(15);
                        if (UserDetails.chatWith.equals("1Science")) {
                            if (to.equals("1Science"))
                            {
                                if (messageText.contains("\n")) {
                                    String mes = "";
                                    int pos = messageText.indexOf("\n");
                                    for (int i = pos + 1; i < messageText.length(); i++) {
                                        mes += messageText.charAt(i);
                                    }
                                    messageText = mes;
                                }
                            }
                            if (messageText.contains("\n"))
                            {
                                String mes = "";
                                int pos = messageText.indexOf("\n");
                                for (int i = 0; i < pos; i++)
                                {
                                    mes += messageText.charAt(i);
                                }
                                messageText = mes;
                            }

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("message", messageText);
                            map.put("time", time);
                            map.put("rep", "N");
                            map.put("scr", "N");
                            map.put("user", UserDetails.username);

                            for (int f = 0; f < al.size(); f++)
                            {
                                if (!al.get(f).equals("1Science"))
                                {
                                    map.put(al.get(f), "S");
                                }
                            }
                            Firebase ret1 = reference1.push();
                            String key = ret1.getKey();
                            map.put("key", key);
                            for (int i = 0; i < al.size(); i++)
                            {
                                if (!al.get(i).equals("1Science"))
                                {
                                    Firebase reference9 = new Firebase("https://scichat-xiscience.firebaseio.com/" + al.get(i) + "_Refresh");
                                    Firebase ret9 = reference9.push();
                                    String khy = ret9.getKey();
                                    map.put(al.get(i) + "r", khy);
                                    Map<String, String> map1 = new HashMap<String, String>();
                                    map1.put("user", "XI - J");
                                    map1.put("key", key);
                                    ret9.setValue(map1);
                                }
                            }
                            ret1.setValue(map);
                        } else {
                            if (to.equals("1Science"))
                            {
                                if (messageText.contains("\n")) {
                                    String mes = "";
                                    int pos = messageText.indexOf("\n");
                                    for (int i = pos + 1; i < messageText.length(); i++) {
                                        mes += messageText.charAt(i);
                                    }
                                    messageText = mes;
                                }
                            }
                            if (messageText.contains("\n"))
                            {
                                String mes = "";
                                int pos = messageText.indexOf("\n");
                                for (int i = 0; i < pos; i++)
                                {
                                    mes += messageText.charAt(i);
                                }
                                messageText = mes;
                            }

                            Map<String, String> map2 = new HashMap<String, String>();
                            map2.put("message", messageText);
                            map2.put("time", time);
                            map2.put("rep", "N");
                            map2.put("scr", "N");
                            map2.put("stat", "S");
                            map2.put("user", UserDetails.username);
                            Firebase refe8 = reference8.push();
                            String hk = refe8.getKey();
                            map2.put("key", hk);
                            Firebase ref3 = reference2.push();
                            String jh = ref3.getKey();
                            map2.put("keyr", jh);
                            ref3.setValue(map2);
                            Firebase retr7 = reference7.push();
                            String rtk = retr7.getKey();
                            map2.put("r", rtk);
                            refe8.setValue(map2);
                            Map<String, String> map1 = new HashMap<String, String>();
                            map1.put("user", UserDetails.username);
                            map1.put("key", hk);
                            retr7.setValue(map1);
                        }
                    }
                    startActivity(new Intent(Users.this, Chat.class));
                }
            }
        });
        EditText see = findViewById(R.id.searchs);
        see.addTextChangedListener(new TextWatcher() {
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

    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                try
                {
                    long numb = Long.parseLong(key);
                }
                catch (NumberFormatException e) {
                    if (UserDetails.star == 0) {
                        if (!key.equals(UserDetails.username) && !UserDetails.al.contains(key)) {
                            al.add(key);
                        }
                    }
                    else
                    {
                        if (!key.equals(UserDetails.username)) {
                            al.add(key);
                        }
                    }
                }
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.print("Error");
            System.out.print("Oops!!!");
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,al){
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
        }

        pd.dismiss();
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