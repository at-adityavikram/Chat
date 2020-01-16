package com.pd.chatapp;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    TextView hgl;
    ImageView sendButton;
    ArrayList<String> alkan = new ArrayList<>();
    ArrayList<String> alc = new ArrayList<>(Arrays.asList("yu", "#915117", "#ffa500", "#00ff00", "#ff0000"));
    EditText messageArea;
    TextView replyArea;
    ScrollView scrollView;
    Firebase reference1, reference2, reference3, reference4;
    TextView cuuser;
    String state;
    int fbn = 0;
    int rep = 0;
    int redao = 0;
    int read = 0;
    int ctr = 0;
    String repm = "";
    String repu = "";
    String repko = "";
    String repkey = "";
    String stry = "";
    int gui = 0;
    int ri = 0;

    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if (KeyCode == KeyEvent.KEYCODE_BACK)
        {
            fbn = 1;
            UserDetails.chatWith = "";
            startActivity(new Intent(Chat.this, User.class));
            return true;
        }
        return super.onKeyDown(KeyCode, event);
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);
        fbn = 0;
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        replyArea = findViewById(R.id.replyArea);
        replyArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        replyArea.setPadding(10, 5, 10,14);
        scrollView = findViewById(R.id.scrollView);
        cuuser = findViewById(R.id.cuuser);
        cuuser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        cuuser.setPadding(10, 5, 10,14);
        cuuser.setText(UserDetails.chatWith);

        Firebase.setAndroidContext(this);
        Firebase referenceee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referenceee.child("Stat").child("stat").setValue("ONLINE");

        replyArea.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                rep = 0;
                repm = "";
                repkey = "";
                repko = "";
                repu = "";
                replyArea.setVisibility(View.GONE);
                return true;
            }
        });

        messageArea.addTextChangedListener(new TextWatcher() {
            Firebase reference6 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                reference6.child("Stat").child("stat").setValue("TYPING");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reference6.child("Stat").child("stat").setValue("TYPING");
            }

            @Override
            public void afterTextChanged(Editable s) {
                new CountDownTimer(1500, 50) {
                    @Override
                    public void onTick(long arg0) {

                    }

                    @Override
                    public void onFinish()
                    {
                        reference6.child("Stat").child("stat").setValue("ONLINE");
                    }
                }.start();
            }
        });

        if (UserDetails.chatWith.equals("1Science")) {
            Firebase.setAndroidContext(this);
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

            RequestQueue rQueue = Volley.newRequestQueue(Chat.this);
            rQueue.add(request);
            reference1 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith);
            reference3 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_Refresh");
        }
        else
        {
            reference1 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
            reference2 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_" + UserDetails.username);
            reference3 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_Refresh");
            reference4 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_Stat");
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if (messageText.replace(" ","").equals(""))
                {
                    messageText = "";
                }
                if(!messageText.equals("")){
                    Timestamp timestampw = new Timestamp(System.currentTimeMillis());
                    String timestamp = timestampw.toString();
                    String zx = timestamp.charAt(8) + "";
                    zx += timestamp.charAt(9) + "";
                    String time = zx + "/" + timestamp.charAt(5) + timestamp.charAt(6) + "/" + timestamp.charAt(2) + timestamp.charAt(3) + "  " + timestamp.charAt(11) + timestamp.charAt(12) + ":" + timestamp.charAt(14) + timestamp.charAt(15);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("time", time);
                    map.put("user", UserDetails.username);
                    map.put("scr", "N");
                    if (UserDetails.chatWith.equals("1Science"))
                    {
                        for (int f = 0; f < alkan.size(); f++)
                        {
                            if (!alkan.get(f).equals("1Science"))
                            {
                                map.put(alkan.get(f), "S");
                            }
                        }
                        Firebase ret1 = reference1.push();
                        String key = ret1.getKey();
                        map.put("key", key);
                        if (rep == 1)
                        {
                            rep = 0;
                            map.put("repm", repm);
                            map.put("repk", repkey);
                            map.put("repko", repko);
                            map.put("rep", "Y");
                            map.put("rto", repu);
                        }
                        else
                        {
                            map.put("rep", "N");
                        }
                        for (int i = 0; i < alkan.size(); i++)
                        {
                            if (!alkan.get(i).equals("1Science"))
                            {
                                Firebase reference9 = new Firebase("https://scichat-xiscience.firebaseio.com/" + alkan.get(i) + "_Refresh");
                                Firebase ret9 = reference9.push();
                                String khy = ret9.getKey();
                                map.put(alkan.get(i) + "r", khy);
                                Map<String, String> map1 = new HashMap<String, String>();
                                map1.put("user", "XI - J");
                                map1.put("key", key);
                                ret9.setValue(map1);
                            }
                        }
                        ret1.setValue(map);

                    }
                    else
                    {
                        map.put("stat", "S");
                        Firebase ref1 = reference1.push();
                        Firebase ref2 = reference2.push();
                        String yk = ref1.getKey();
                        map.put("key", yk);
                        if (rep == 1)
                        {
                            rep = 0;
                            map.put("repm", repm);
                            map.put("repk", repkey);
                            map.put("repko", repko);
                            map.put("rep", "Y");
                            map.put("rto", repu);
                            repm = "";
                            repkey = "";
                            repko = "";
                            repu = "";
                        }
                        else
                        {
                            map.put("rep", "N");
                        }
                        String krey = ref2.getKey();
                        map.put("keyr", krey);
                        ref2.setValue(map);
                        Firebase rtt = reference3.push();
                        String ytk = rtt.getKey();
                        map.put("r", ytk);
                        ref1.setValue(map);
                        Map<String, String> map1 = new HashMap<String, String>();
                        map1.put("user", UserDetails.username);
                        map1.put("key", yk);
                        rtt.setValue(map1);
                    }
                    replyArea.setVisibility(View.GONE);
                    messageArea.setText("");
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                ctr += 1;
                final Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String uy = dataSnapshot.getKey();
                String ku = "";
                String hc = "";
                SpannableStringBuilder toac = new SpannableStringBuilder("");
                String delk = "";
                if (map.get("rep").toString().equals("Y"))
                {
                    String remp = map.get("repm").toString();
                    String userNamex = map.get("rto").toString();
                    final String repk;
                    if (userName.equals(UserDetails.username))
                    {
                        repk = map.get("repk").toString();
                    }
                    else
                    {
                        repk = map.get("repko").toString();
                    }
                    ClickableSpan clickableSpan = new ClickableSpan()
                    {
                        @Override
                        public void onClick(@NonNull View widget)
                        {
                            if (!UserDetails.chatWith.equals("1Science"))
                            {
                                Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                                referencee2.child(repk).child("scr").setValue("Y");
                            }
                            else
                            {
                                Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                                referencee2.child(repk).child("scr").setValue(UserDetails.username);
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds)
                        {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.setColor(Color.WHITE);
                        }
                    };
                    double ran = Math.random();
                    ran = ran * 4 + 1;
                    int rai = (int) ran;
                    toac = new SpannableStringBuilder(userNamex + System.getProperty("line.separator") + remp);
                    toac.setSpan(new RelativeSizeSpan(0.9f), userNamex.length() + 1, userNamex.length() + 1 + remp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toac.setSpan(new RelativeSizeSpan(0.6f), 0, userNamex.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toac.setSpan(new ForegroundColorSpan(Color.WHITE), userNamex.length() + 1, userNamex.length() + 1 + remp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toac.setSpan(new ForegroundColorSpan(Color.parseColor(alc.get(rai))), 0, userNamex.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toac.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, userNamex.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toac.setSpan(clickableSpan, userNamex.length() + 1, userNamex.length() + 1 + remp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (!UserDetails.chatWith.equals("1Science")) {
                    state = map.get("stat").toString();
                    ku = map.get("key").toString();
                    hc = map.get("keyr").toString();
                    if (userName.equals(UserDetails.username))
                    {
                        delk = map.get("r").toString();
                    }
                    stry = "";
                    if (state.equals("S"))
                    {
                        stry = "✓";
                    }
                    else if (state.equals("D"))
                    {
                        stry = "✓✓";
                    }
                    else if (state.equals("R"))
                    {
                        stry = "✓✓";
                    }
                }
                else
                {
                    ku = map.get("key").toString();
                    stry = "";
                    String iz = getStat(map);

                    if (iz.contains("R") && !iz.contains("D") && !iz.contains("S"))
                    {
                        stry = "✓✓";
                    }
                    else if (iz.contains("D") && !iz.contains("S"))
                    {
                        stry = "✓✓";
                    }
                    else
                    {
                        stry = "✓";
                    }
                }
                String time = map.get("time").toString();
                String timex = time + "   " + stry;
                double ran = Math.random();
                ran = ran * 4 + 1;
                int rai = (int) ran;
                if(userName.equals(UserDetails.username)){
                    if (UserDetails.chatWith.equals("1Science")) {
                        SpannableStringBuilder toa = new SpannableStringBuilder(userName + System.getProperty("line.separator") + message + System.getProperty("line.separator") + timex);
                        toa.setSpan(new ForegroundColorSpan(Color.parseColor(alc.get(rai))), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new RelativeSizeSpan(1.5f), userName.length() + 1, userName.length() + 1 + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.WHITE), userName.length() + 1, userName.length() + 1 + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new RelativeSizeSpan(0.7f), userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + timex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + timex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        AlignmentSpan asd = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
                        toa.setSpan(asd, userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + timex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        addMessageBox(toa, toac, userName, 2, uy, ku, delk, map, hc);
                    }
                    else
                    {
                        SpannableStringBuilder toa = new SpannableStringBuilder(message + System.getProperty("line.separator") + timex);
                        toa.setSpan(new RelativeSizeSpan(1.5f), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), message.length() + 1, message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new RelativeSizeSpan(0.7f), message.length() + 1, message.length() + 1 + timex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), message.length() + 1, message.length() + 1 + timex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        AlignmentSpan asd = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
                        toa.setSpan(asd, message.length() + 1, message.length() + 1 + timex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        addMessageBox(toa, toac, userName, 2, uy, ku, delk, map, hc);
                    }
                }
                else{
                    if (UserDetails.chatWith.equals("1Science"))
                    {
                        SpannableStringBuilder toa = new SpannableStringBuilder(userName + System.getProperty("line.separator") + message + System.getProperty("line.separator") + time);
                        toa.setSpan(new ForegroundColorSpan(Color.parseColor(alc.get(rai))), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new RelativeSizeSpan(1.5f), userName.length() + 1, userName.length() + 1 + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.WHITE), userName.length() + 1, userName.length() + 1 + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new RelativeSizeSpan(0.7f), userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), userName.length() + 1 + message.length() + 1, userName.length() + 1 + message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (!map.get(UserDetails.username).toString().equals("R") && read == 0)
                        {
                            addUnread();
                            read = 1;
                            redao = 1;
                        }
                        addMessageBox(toa, toac, userName, 1, uy, ku, delk, map, hc);
                        if (dataSnapshot.getChildrenCount() == ctr)
                        {
                            read = 2;
                        }
                    }
                    else
                    {
                        SpannableStringBuilder toa = new SpannableStringBuilder(message + System.getProperty("line.separator") + time);
                        toa.setSpan(new RelativeSizeSpan(1.5f), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), message.length() + 1, message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new RelativeSizeSpan(0.7f), message.length() + 1, message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toa.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), message.length() + 1, message.length() + 1 + time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        if (!map.get("stat").toString().equals("R") && read == 0)
                        {
                            addUnread();
                            read = 1;
                        }
                        if (read == 1)
                        {
                            reference1.child(dataSnapshot.getKey()).child("stat").setValue("R");
                        }
                        addMessageBox(toa, toac, userName, 1, uy, ku, delk, map, hc);
                        if (dataSnapshot.getChildrenCount() == ctr)
                        {
                            read = 2;
                        }
                    }
                }
                if (read == 0)
                {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                else if (read == 1)
                {
                    scrollToView(scrollView, hgl);
                }
                else if (read == 2)
                {
                    if (redao == 1)
                    {
                        scrollToView(scrollView, hgl);
                        redao = 2;
                    }
                    else {
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                }
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
        if (read == 0)
        {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
        else if (read == 1)
        {
            scrollToView(scrollView, hgl);
        }
        else if (read == 2)
        {
            if (redao == 1)
            {
                scrollToView(scrollView, hgl);
                redao = 2;
            }
            else {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }

        if (!UserDetails.chatWith.equals("1Science"))
        {
            reference4.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map map = dataSnapshot.getValue(Map.class);
                    String colour = null;
                    String yhj = "";
                    if (map.get("stat").toString().equals("ONLINE"))
                    {
                        yhj = "ONLINE";
                        colour = "#00ff00";
                    }
                    else if (map.get("stat").toString().equals("TYPING"))
                    {
                        yhj = "TYPING";
                        colour = "ffff00";
                    }
                    else if (map.get("stat").toString().equals("OFFLINE"))
                    {
                        yhj = "OFFLINE";
                        colour = "#ff0000";
                    }
                    SpannableStringBuilder toax = new SpannableStringBuilder(UserDetails.chatWith + System.getProperty("line.separator") + yhj);
                    toax.setSpan(new ForegroundColorSpan(Color.parseColor(colour)), UserDetails.chatWith.length() + 1, UserDetails.chatWith.length() + 1 + yhj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toax.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), UserDetails.chatWith.length() + 1, UserDetails.chatWith.length() + 1 + yhj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toax.setSpan(new RelativeSizeSpan(0.5f), UserDetails.chatWith.length() + 1, UserDetails.chatWith.length() + 1 + yhj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cuuser = findViewById(R.id.cuuser);
                    cuuser = findViewById(R.id.cuuser);
                    cuuser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    cuuser.setPadding(10, 5, 10,14);
                    cuuser.setText(toax);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Map map = dataSnapshot.getValue(Map.class);
                    String colour = null;
                    String yhj = "";
                    if (map.get("stat").toString().equals("ONLINE"))
                    {
                        yhj = "ONLINE";
                        colour = "#00ff00";
                    }
                    else if (map.get("stat").toString().equals("TYPING"))
                    {
                        yhj = "TYPING";
                        colour = "#ffff00";
                    }
                    else if (map.get("stat").toString().equals("OFFLINE"))
                    {
                        yhj = "OFFLINE";
                        colour = "#ff0000";
                    }
                    SpannableStringBuilder toax = new SpannableStringBuilder(UserDetails.chatWith + System.getProperty("line.separator") + yhj);
                    toax.setSpan(new ForegroundColorSpan(Color.parseColor(colour)), UserDetails.chatWith.length() + 1, UserDetails.chatWith.length() + 1 + yhj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toax.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), UserDetails.chatWith.length() + 1, UserDetails.chatWith.length() + 1 + yhj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toax.setSpan(new RelativeSizeSpan(0.5f), UserDetails.chatWith.length() + 1, UserDetails.chatWith.length() + 1 + yhj.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    cuuser = findViewById(R.id.cuuser);
                    cuuser = findViewById(R.id.cuuser);
                    cuuser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    cuuser.setPadding(10, 5, 10,14);
                    cuuser.setText(toax);
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
    }

    public void addMessageBox(final SpannableStringBuilder msg, final SpannableStringBuilder msgx, final String us, final int type, final String key, final String keyu, final String delid, final Map mapy, final String keyt){
        final TextView textView = new TextView(Chat.this);
        textView.setText(msg);
        textView.setTextIsSelectable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setPadding(10, 5, 10, 5);
        final TextView textViewx = new TextView(Chat.this);
        textViewx.setText(msgx);
        textViewx.setBackgroundColor(Color.BLACK);
        textViewx.setMovementMethod(LinkMovementMethod.getInstance());
        textViewx.setHighlightColor(Color.TRANSPARENT);
        textViewx.setPadding(10, 5, 10, 5);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;
        lp2.setMargins(0,8,0,0);
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //lp3.weight = 7.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            lp2.setMargins(0, 10, 400, 0);
            lp3.gravity = Gravity.LEFT;
            lp3.setMargins(0, 10, 400, 0);
            textView.setBackgroundResource(R.drawable.bubble_in);
            if (!UserDetails.chatWith.equals("1Science")) {
                if (fbn == 0) {
                    if (isAppRunning())
                    {
                        Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_" + UserDetails.username);
                        referencee2.child(keyu).child("stat").setValue("D");
                        referencee2.child(keyu).child("stat").setValue("R");
                    }
                }
                final Firebase referencee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                referencee.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        if (dataSnapshot.getKey().equals(key)) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String scros = map.get("scr").toString();
                            if (scros.equals("Y")) {
                                referencee.child(key).child("scr").setValue("N");
                                scrollToView(scrollView, textView);
                                textView.setBackgroundResource(R.drawable.bubble_sel);
                                new CountDownTimer(1000, 50) {
                                    @Override
                                    public void onTick(long arg0) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        textView.setBackgroundResource(R.drawable.bubble_in);
                                    }
                                }.start();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s)
                    {
                        if (dataSnapshot.getKey().equals(key)) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String scros = map.get("scr").toString();
                            if (scros.equals("Y")) {
                                referencee.child(key).child("scr").setValue("N");
                                scrollToView(scrollView, textView);
                                textView.setBackgroundResource(R.drawable.bubble_sel);
                                new CountDownTimer(1000, 50) {
                                    @Override
                                    public void onTick(long arg0) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        textView.setBackgroundResource(R.drawable.bubble_in);
                                    }
                                }.start();
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey().equals(key))
                        {
                            layout.removeView(textView);
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
            else
            {
                if (fbn == 0)
                {
                    if (isAppRunning())
                    {
                        Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                        referencee2.child(keyu).child(UserDetails.username).setValue("R");
                    }
                }
                final Firebase referencee = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                referencee.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        Map map = dataSnapshot.getValue(Map.class);
                        String scros = map.get("scr").toString();
                        if (scros.equals(UserDetails.username))
                        {
                            referencee.child(key).child("scr").setValue("N");
                            scrollToView(scrollView, textView);
                            textView.setBackgroundResource(R.drawable.bubble_sel);
                            new CountDownTimer(1000, 50)
                            {
                                @Override
                                public void onTick(long arg0)
                                {

                                }

                                @Override
                                public void onFinish()
                                {
                                    textView.setBackgroundResource(R.drawable.bubble_in);
                                }
                            }.start();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s)
                    {
                        Map map = dataSnapshot.getValue(Map.class);
                        String scros = map.get("scr").toString();
                        if (scros.equals(UserDetails.username))
                        {
                            referencee.child(key).child("scr").setValue("N");
                            scrollToView(scrollView, textView);
                            textView.setBackgroundResource(R.drawable.bubble_sel);
                            new CountDownTimer(1000, 50)
                            {
                                @Override
                                public void onTick(long arg0)
                                {

                                }

                                @Override
                                public void onFinish()
                                {
                                    textView.setBackgroundResource(R.drawable.bubble_in);
                                }
                            }.start();
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey().equals(key))
                        {
                            layout.removeView(textView);
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            lp2.setMargins(400, 10, 0, 0);
            lp3.gravity = Gravity.RIGHT;
            lp3.setMargins(400, 10, 0, 0);
            textView.setBackgroundResource(R.drawable.bubble_out);
            if (!UserDetails.chatWith.equals("1Science")) {
                final Firebase referencee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                referencee.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(key)) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String scros = map.get("scr").toString();
                            if (scros.equals("Y"))
                            {
                                referencee.child(key).child("scr").setValue("N");
                                scrollToView(scrollView, textView);
                                textView.setBackgroundResource(R.drawable.bubble_sel);
                                new CountDownTimer(1000, 50)
                                {
                                    @Override
                                    public void onTick(long arg0)
                                    {

                                    }

                                    @Override
                                    public void onFinish()
                                    {
                                        textView.setBackgroundResource(R.drawable.bubble_out);
                                    }
                                }.start();
                            }
                            String zxc = map.get("stat").toString();
                            if (zxc.equals("D")) {
                                msg.replace(msg.length() - stry.length(), msg.length(), "✓✓");
                                textView.setText(msg);
                                stry = "✓✓";
                            } else if (zxc.equals("R")) {
                                stry = "✓✓";
                                msg.setSpan(new ForegroundColorSpan(Color.parseColor("#00ff00")), msg.length() - stry.length(), msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                textView.setText(msg);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(key)) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String zxc = map.get("stat").toString();
                            String scros = map.get("scr").toString();
                            if (scros.equals("Y"))
                            {
                                referencee.child(key).child("scr").setValue("N");
                                scrollToView(scrollView, textView);
                                textView.setBackgroundResource(R.drawable.bubble_sel);
                                new CountDownTimer(1000, 50)
                                {
                                    @Override
                                    public void onTick(long arg0)
                                    {

                                    }

                                    @Override
                                    public void onFinish()
                                    {
                                        textView.setBackgroundResource(R.drawable.bubble_out);
                                    }
                                }.start();
                            }
                            if (zxc.equals("D")) {
                                msg.replace(msg.length() - stry.length(), msg.length(), "✓✓");
                                textView.setText(msg);
                                stry = "✓✓";
                            } else if (zxc.equals("R")) {
                                stry = "✓✓";
                                msg.setSpan(new ForegroundColorSpan(Color.parseColor("#00ff00")), msg.length() - stry.length(), msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                textView.setText(msg);
                            }
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
            else
            {
                final Firebase referencee = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                referencee.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(key)) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String scros = map.get("scr").toString();
                            if (scros.equals(UserDetails.username))
                            {
                                referencee.child(key).child("scr").setValue("N");
                                scrollToView(scrollView, textView);
                                textView.setBackgroundResource(R.drawable.bubble_sel);
                                new CountDownTimer(1000, 50)
                                {
                                    @Override
                                    public void onTick(long arg0)
                                    {

                                    }

                                    @Override
                                    public void onFinish()
                                    {
                                        textView.setBackgroundResource(R.drawable.bubble_out);
                                    }
                                }.start();
                            }
                            String iz = getStat(map);

                            if (iz.contains("R") && !iz.contains("D") && !iz.contains("S"))
                            {
                                stry = "✓✓";
                                msg.setSpan(new ForegroundColorSpan(Color.parseColor("#00ff00")), msg.length() - stry.length(), msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                textView.setText(msg);
                            }
                            else if (iz.contains("D") && !iz.contains("S"))
                            {
                                msg.replace(msg.length() - stry.length(), msg.length(), "✓✓");
                                textView.setText(msg);
                                stry = "✓✓";
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(key)) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String iz = getStat(map);
                            String scros = map.get("scr").toString();
                            if (scros.equals(UserDetails.username))
                            {
                                referencee.child(key).child("scr").setValue("N");
                                scrollToView(scrollView, textView);
                                textView.setBackgroundResource(R.drawable.bubble_sel);
                                new CountDownTimer(1000, 50)
                                {
                                    @Override
                                    public void onTick(long arg0)
                                    {

                                    }

                                    @Override
                                    public void onFinish()
                                    {
                                        textView.setBackgroundResource(R.drawable.bubble_out);
                                    }
                                }.start();
                            }
                            if (iz.contains("R") && !iz.contains("D") && !iz.contains("S"))
                            {
                                stry = "✓✓";
                                msg.setSpan(new ForegroundColorSpan(Color.parseColor("#00ff00")), msg.length() - stry.length(), msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                textView.setText(msg);
                            }
                            else if (iz.contains("D") && !iz.contains("S"))
                            {
                                msg.replace(msg.length() - stry.length(), msg.length(), "✓✓");
                                textView.setText(msg);
                                stry = "✓✓";
                            }
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
        }
        textViewx.setLayoutParams(lp3);
        textView.setLayoutParams(lp2);
        textView.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v)
            {
                PopupMenu popup = new PopupMenu(Chat.this, textView);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String option = (String) item.getTitle();
                        if (option.equals("Forward"))
                        {
                            UserDetails.star = 1;
                            UserDetails.unm = msg.toString();
                            startActivity(new Intent(Chat.this, Users.class));
                        }
                        else if (option.equals("Information"))
                        {
                            if (UserDetails.chatWith.equals("1Science") && type == 2)
                            {
                                UserDetails.ke = key;
                                startActivity(new Intent(Chat.this, Info.class));
                            }
                            else if (UserDetails.chatWith.equals("1Science") && type == 1)
                            {
                                Toast.makeText(Chat.this, "Only Sender Can See It", Toast.LENGTH_LONG).show();
                            }
                            else if (!UserDetails.chatWith.equals("1Science") && type == 1)
                            {
                                Toast.makeText(Chat.this, "Only Sender Can See It", Toast.LENGTH_LONG).show();
                            }

                            else
                            {
                                Toast.makeText(Chat.this, "It is in front of you", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if (option.equals("Delete"))
                        {
                            if (UserDetails.chatWith.equals("1Science") && type == 2)
                            {
                                Firebase referencee3 = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                                referencee3.child(keyu).setValue(null);
                                for (int i = 0; i < alkan.size(); i++)
                                {
                                    if (!alkan.get(i).equals("1Science"))
                                    {
                                        Firebase reference9 = new Firebase("https://scichat-xiscience.firebaseio.com/" + alkan.get(i) + "_Refresh");
                                        reference9.child(mapy.get(alkan.get(i) + "r").toString()).setValue(null);
                                    }
                                }
                                layout.removeView(textView);
                                Toast.makeText(Chat.this, "Message Deleted", Toast.LENGTH_LONG).show();
                            }
                            else if (UserDetails.chatWith.equals("1Science") && type == 1)
                            {
                                Toast.makeText(Chat.this, "Only Sender Can Delete It", Toast.LENGTH_LONG).show();
                            }
                            else if (!UserDetails.chatWith.equals("1Science") && type == 1)
                            {
                                Toast.makeText(Chat.this, "Only Sender Can Delete It", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Firebase referencee4 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_" + UserDetails.username);
                                Firebase referencee5 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                                referencee4.child(keyt).setValue(null);
                                referencee5.child(key).setValue(null);
                                Firebase reference9 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.chatWith + "_Refresh");
                                reference9.child(delid).setValue(null);
                                layout.removeView(textView);
                                Toast.makeText(Chat.this, "Message Deleted", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if (option.equals("Reply"))
                        {
                            if (rep == 0)
                            {
                                rep = 1;
                                String mnb = msg.toString();

                                if (UserDetails.chatWith.equals("1Science")) {
                                    if (mnb.contains("\n")) {
                                        String mes = "";
                                        int pos = mnb.indexOf("\n");
                                        for (int i = pos + 1; i < mnb.length(); i++) {
                                            mes += mnb.charAt(i);
                                        }
                                        mnb = mes;
                                    }
                                }

                                if (mnb.contains("\n")) {
                                    String mes = "";
                                    int pos = mnb.indexOf("\n");
                                    for (int i = 0; i < pos; i++) {
                                        mes += mnb.charAt(i);
                                    }
                                    mnb = mes;
                                }
                                if (mnb.length() > 20) {
                                    for (int h = 0; h < 21; h++) {
                                        repm += mnb.charAt(h);
                                    }
                                    repm += "...";
                                } else {
                                    repm = mnb;
                                }
                                if (us.equals(UserDetails.username)) {
                                    repkey = keyu;
                                    repko = keyt;
                                } else {
                                    repkey = keyt;
                                    repko = keyu;
                                }
                                repu = us;
                                replyArea.setVisibility(View.VISIBLE);
                                replyArea.setText(us + ": " + repm);
                                replyArea.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (!UserDetails.chatWith.equals("1Science"))
                                        {
                                            Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                                            referencee2.child(repkey).child("scr").setValue("Y");
                                        }
                                        else
                                        {
                                            Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                                            referencee2.child(repkey).child("scr").setValue(UserDetails.username);
                                        }
                                    }
                                });
                            }
                            else
                            {
                                repm = "";
                                repkey = "";
                                repko = "";
                                repu = "";
                                rep = 1;
                                String mnb = msg.toString();

                                if (UserDetails.chatWith.equals("1Science")) {
                                    if (mnb.contains("\n")) {
                                        String mes = "";
                                        int pos = mnb.indexOf("\n");
                                        for (int i = pos + 1; i < mnb.length(); i++) {
                                            mes += mnb.charAt(i);
                                        }
                                        mnb = mes;
                                    }
                                }

                                if (mnb.contains("\n")) {
                                    String mes = "";
                                    int pos = mnb.indexOf("\n");
                                    for (int i = 0; i < pos; i++) {
                                        mes += mnb.charAt(i);
                                    }
                                    mnb = mes;
                                }
                                if (mnb.length() > 20) {
                                    for (int h = 0; h < 21; h++) {
                                        repm += mnb.charAt(h);
                                    }
                                    repm += "...";
                                } else {
                                    repm = mnb;
                                }
                                if (us.equals(UserDetails.username)) {
                                    repkey = keyu;
                                    repko = keyt;
                                } else {
                                    repkey = keyt;
                                    repko = keyu;
                                }
                                repu = us;
                                replyArea.setVisibility(View.VISIBLE);
                                replyArea.setText(us + ": " + repm);
                                replyArea.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (!UserDetails.chatWith.equals("1Science"))
                                        {
                                            Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_" + UserDetails.chatWith);
                                            referencee2.child(repkey).child("scr").setValue("Y");
                                        }
                                        else
                                        {
                                            Firebase referencee2 = new Firebase("https://scichat-xiscience.firebaseio.com/1Science");
                                            referencee2.child(repkey).child("scr").setValue(UserDetails.username);
                                        }
                                    }
                                });
                            }
                        }
                        return true;
                    }
                });

                popup.show();
                return true;
            }
        });
        if (!msgx.toString().equals(""))
        {
            layout.addView(textViewx);
        }
        layout.addView(textView);
        if (read == 0)
        {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
        else if (read == 1)
        {
            scrollToView(scrollView, hgl);
        }
        else if (read == 2)
        {
            if (redao == 1)
            {
                scrollToView(scrollView, hgl);
                redao = 2;
            }
            else {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }
    }

    public void addUnread()
    {
        final TextView textView = new TextView(Chat.this);
        String lop = "UNREAD MESSAGES";
        textView.setText(lop);
        textView.setTextIsSelectable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        hgl = textView;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setPadding(10, 5, 10, 5);
        textView.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;
        lp2.setMargins(0,8,0,0);
        lp2.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        new CountDownTimer(6000, 50)
        {
            @Override
            public void onTick(long arg0)
            {

            }

            @Override
            public void onFinish()
            {
                layout.removeView(textView);
            }
        }.start();
    }

    public void doOnSuccess(String s){
        UserDetails.res.clear();
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
                    if (!key.equals(UserDetails.username)) {
                        alkan.add(key);
                        UserDetails.res.add(key);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.print("Error");
            System.out.print("Oops!!!");
            System.out.print("Oops!dwwd!");
        }
    }

    public String getStat(Map maf)
    {
        String izx = "";
        for (int f = 0; f < UserDetails.res.size(); f++)
        {
            if (!UserDetails.res.get(f).equals("1Science"))
            {
                izx += maf.get(UserDetails.res.get(f));
            }
        }
        return izx;
    }

    private void scrollToView(final ScrollView scrollViewParent, final View view)
    {
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset)
    {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent))
        {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    @Override
    protected void onPause()
    {
        read = 0;
        redao = 0;
        fbn = 1;
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
        fbn = 0;
        Firebase referenceee = new Firebase("https://scichat-xiscience.firebaseio.com/" + UserDetails.username + "_Stat");
        referenceee.child("Stat").child("stat").setValue("ONLINE");
    }


}