package cn.com.forthedream.imhere;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {
    private View perference;
    private View main;
    private ArrayList<Item> items;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private static TextView locationTextView;
    private TextView username;
    private IntentFilter locationIntentFilter;
    private LocationReceiver locationReceiver;
    NavigationView navigationView;
    private final static String usernameString="用户名:";
    private final static String locationString="MAC:";
    private static long beforetime=0;
    private final static int UPDATE_LOCATION = 1;
    private static String nowMac;
    private Handler hander;
    private String name = "sht";
    private String password = "pass";
    @SuppressLint("SetTextI18n")
    private void init(){
        perference = findViewById(R.id.perference);
        main = findViewById(R.id.main);
        assert main != null;
        main.setVisibility(View.VISIBLE);
        perference.setVisibility(View.GONE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        View header = navigationView.getHeaderView(0);
        username = (TextView) header.findViewById(R.id.left_bar_username);
        username.setText(usernameString+"tmp");
        locationTextView = (TextView) header.findViewById(R.id.left_bar_location);
        Intent FromLoginintent = getIntent();
        username.setText(FromLoginintent.getStringExtra("username"));
        locationTextView.setText(locationString+"none");
        locationIntentFilter = new IntentFilter();
        locationIntentFilter.addAction("cn.com.forthedream.locationreceiver");
        locationReceiver = new LocationReceiver();
        registerReceiver(locationReceiver,locationIntentFilter);
        hander=new Handler() {
            public void handleMessage(Message msg){
                switch (msg.what){
                    case UPDATE_LOCATION:
                        Log.d("main","1:"+System.currentTimeMillis());
                        locationTextView.setText(locationString+"none");
                        break;
                }
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    Log.d("main", "2:" + System.currentTimeMillis()+"\n is:"+beforetime+"\n less:"+(System.currentTimeMillis()-beforetime));
                    if (System.currentTimeMillis() - beforetime > 2 * 1000) {
                        Message message = new Message();
                        message.what = UPDATE_LOCATION;
                        hander.sendMessage(message);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        init();
        Intent intent = new Intent(this,LocationService.class);
        intent.putExtra("test","cccc");
        startService(intent);
        intent = new Intent(this,NotificationService.class);
        intent.putExtra("name","tmp");
        startService(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //getTotNote("zhranklin,com/imh");
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();
                if (id == R.id.bt_homepage) {
                    // Handle the camera action
                    perference.setVisibility(View.GONE);
                    main.setVisibility(View.VISIBLE);
                }
                else if (id == R.id.bt_perference){
                    Intent intent = new Intent(MainActivity.this,LocationService.class);
                    intent.putExtra("test","bbbb");
                    startService(intent);
                    Log.d("per","aaaaaaaaaa");
                    main.setVisibility(View.GONE);
                    Log.d("per","bbbbb");
                    perference.setVisibility(View.VISIBLE);
                }
                else if (id == R.id.bt_richeng){
                    Intent intent = new Intent(MainActivity.this,WebActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if(items != null) {
            listViewAdapter = new ListViewAdapter(MainActivity.this, R.layout.list_main, items);
            listView.setAdapter(listViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Item item = items.get(position);
                    //    Intent intent = new Intent()
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        moveTaskToBack(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private items parseJson(String jsonData) {
        Gson gson = new Gson();
        items item = gson.fromJson(jsonData,items.class);
        return item;
    }

    public static class LocationReceiver extends BroadcastReceiver{

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            nowMac = intent.getStringExtra("MAC");
            beforetime = intent.getLongExtra("time",0);
            Log.d("main","is"+beforetime);
            locationTextView.setText(locationString+nowMac);
        }
    }
    private class items{
        public String title,type,content,place,owner,id;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationReceiver);
    }
    private void startRemind(int mouth,int day,int hour,int min,int id){
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.MONTH,mouth);
        mCalendar.set(Calendar.DAY_OF_MONTH,day);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, min);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        Intent intent = new Intent(MainActivity.this, TimeReceiver.class);
        intent.putExtra("id",id);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);

    }
    private void stopRemind() {
        Intent intent = new Intent(MainActivity.this, TimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);
    }

}
