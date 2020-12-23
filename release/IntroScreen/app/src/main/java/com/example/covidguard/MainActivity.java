package com.example.covidguard;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.covidguard.backend.DBCipher;

import java.util.Date;


public class MainActivity extends AppCompatActivity
{
    Handler handler= new Handler();
    Runnable runnable;
    ImageView img;
    private Context myContext;
    private DownloadInfected di;
    private DBCipher dbCipher;
    public RequestQueue queue;
    final static String TAG = "INTRO_SCREEN";

    int numbofrecordmatch=0;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(getApplicationContext());
        dbCipher = new DBCipher(this);
        //Shared Preferences to show the intropages only once
        SharedPreferences s1 = getSharedPreferences("prefs", MODE_PRIVATE);

        boolean firstStart = s1.getBoolean("firststart", true);
        if (firstStart)
        {
            startshow();
            dbCipher.createTable();
            handler.post(periodicUpdate);
        }
        /* When user visits application next time skip intro pages */
        else
        {
            Intent next = new Intent(MainActivity.this,Connect_to_beacon.class);
            handler = new Handler();
            //handler.post(periodicUpdate);
            startActivity(next);

        }
      }
        /* When user enters the first time */
        private void startshow()
        {
            //checkPermissions();
            try {
                this.getSupportActionBar().hide();
            } catch (NullPointerException e) {

            }
            img = findViewById(R.id.splash);
            img.animate().alpha(4000).setDuration(0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent next_page = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(next_page);
                    finish();
                }
            }, 4000);
            SharedPreferences s1 = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = s1.edit();
            editor.putBoolean("firststart",false);
            editor.apply();
        }

    private Runnable periodicUpdate = new Runnable () {
        public void run() {
            // scheduled another events to be in 10 seconds later

            handler.postDelayed(periodicUpdate, 80*1000); //milliseconds, we can change this to 24 hours.
            //Log.d(BeaconHandler.TAG,"Timer Start");
            // SQLiteDatabase.loadLibs(myContext);
            Log.d(TAG,"Calling download");
            open();
        }
    };
    private boolean getInfectedUserPrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isUserInfected = pref.getBoolean("IsUserInfected",false);
        return  isUserInfected;
    }
    //make this method should be dynamic
    private void open()
    {
        if (!getInfectedUserPrefData())
        {
                di = new DownloadInfected(dbCipher);
                // DownloadInfected downloadInfected = new DownloadInfected();
                di.downloadLatestInfectedData(new Date(), queue);
                Log.d(TAG, "Downloaded");
                // Checks UUID and time, giving high alert notification
                numbofrecordmatch = dbCipher.checkHighAlert();
                if(numbofrecordmatch>=1)
                {
                    sendHighalertNotification("Dear User");
                    Log.d(TAG, "HighAlertFound");
                    System.out.println("count columns" +numbofrecordmatch);
                }
                else if(numbofrecordmatch<=0)
                {
                    int exposmatch=dbCipher.checkExposure();
                    if(exposmatch>=1)
                    {
                        sendExposureNotification("Dear User");
                        Log.d(TAG, "Exposure Found but not high alert");
                    }
                }
        }
    }
     /* To generate a notification */
    private void sendHighalertNotification(String direction) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setContentTitle("High Alert: Exposed to Infected Location")
                .setContentText(direction + "Please Follow Health Authorities guidelines")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
    /* To generate a Exposure Notification */
    private void sendExposureNotification(String direction) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setContentTitle("Exposure Alert: Exposed to Infected Location")
                .setContentText(direction + "Please Follow Health Authorities guidelines")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
