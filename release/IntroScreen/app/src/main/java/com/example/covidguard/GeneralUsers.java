package com.example.covidguard;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.covidguard.DownloadInfected;
import com.example.covidguard.backend.DBCipher;
import java.util.Date;

/*General User UI page*/
public class GeneralUsers extends AppCompatActivity {

//    private DownloadInfected di;
//    private DBCipher dbCipher;
//
//    public RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
//        queue = Volley.newRequestQueue(getApplicationContext());
//        dbCipher = new DBCipher(this);
    }
//    public void open(View view)//make this method should be dynamic
//    {
//        //put this in if-block, savePreferences and getPreferences check, if user is infected
//        di = new DownloadInfected(dbCipher);
//        /*call download methods here*/
//        di.downloadLatestInfectedData(new Date(), queue);
//        int res = dbCipher.match1();
//        int res1= dbCipher.match2();
//        if(res>=1) {
//            if(res1<=1) {
//                sendNotification(" Dear User!");
//            }
//        }
//        else if(res1>=1)
//        {
//            sendNotification(" Dear User!");
//        }
//    }
//    /*To generate a notification*/
//    private void sendNotification(String direction) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
//                .setContentTitle("High Alert: Exposed to Infected Location")
//                .setContentText(direction + "Please Follow Health Authorities guidelines")
//                .setSmallIcon(R.drawable.ic_baseline_add_alert_24);
//        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
//    }
}