package com.example.covidguard.beacon;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import com.example.covidguard.R;
import com.example.covidguard.backend.DBCipher;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class BeaconHandler extends Service implements BootstrapNotifier {

    public static final String TAG = "BEACON-TEST";
    private BeaconManager beaconManager;
    private DBCipher dbCipher;
    private String START_TIME;
    private String END_TIME;
    public RequestQueue queue;

    private HashSet<String> nearByBeacons = new HashSet<>(); //includes list of all the UUID's around


    @Override
    //private BackgroundPowerSaver backgroundPowerSaver;
    public void onCreate() {
        Log.d(TAG, "Starting Beacon Test");
        super.onCreate();
        //backgroundPowerSaver = new BackgroundPowerSaver(this);
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        BeaconManager.setDebug(true);
        setScanPeriodsToDesiredTimes();
        BeaconManager.setAndroidLScanningDisabled(true);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setEnableScheduledScanJobs(false);
        startForegroundService();
        //sendNotification();
        Region region = new Region("backgroundRegion", null, null, null);
        RegionBootstrap regionBootstrap = new RegionBootstrap(this, region);
        //create table here for user_information
        dbCipher = new DBCipher(this);
        dbCipher.createTable();
        //DownloadInfected u1 = new DownloadInfected();
        //u1.downloadLatestInfectedData(new Date(), queue);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    protected void setScanPeriodsToDesiredTimes() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundBetweenScanPeriod(40100l);
        beaconManager.setBackgroundScanPeriod(4100l);
        BeaconManager.setRegionExitPeriod(90000l);
        beaconManager.setForegroundBetweenScanPeriod(40100l);
        beaconManager.setForegroundScanPeriod(4100l);
    }
    //to avoid the duplication of enter and exit of the regions.
    private void startScanningBeaconsInRange() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region("backgroundRegion", null, null, null);
        try {
            beaconManager.updateScanPeriods();
            beaconManager.startRangingBeaconsInRegion(region);
            beaconManager.addRangeNotifier(new RangeNotifier() {
                // This will be called repeatedly
                public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                    Log.d(TAG, "Scanning nearby beacons");
                    for (Beacon beacon : beacons) {
                        Log.d(TAG, "Found beacon:" + beacon.toString());
                        String uuid = beacon.getId1().toString();
                        nearByBeacons.add(uuid);
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected void startForegroundService() {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.enableForegroundServiceScanning(getNotificationBuilder().build(), 123);
        beaconManager.setEnableScheduledScanJobs(false);
    }

    protected void getNotificationChannel() {
        NotificationChannel channel;
        channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public Notification.Builder getNotificationBuilder() {
        getNotificationChannel();
        Notification.Builder builder = new Notification.Builder(this, "notification_channel");
        builder.setSmallIcon(R.drawable.spring_dot_background);
        builder.setContentTitle("Started Foreground Service!");
        return builder;
    }
    /* when entered the region*/
    @Override
    public void didEnterRegion(Region region) {
        // Log.d(TAG, "Entered Region" + region.getUniqueId());
        Log.d(TAG, "Entered Region" + region.getId1());
        START_TIME = timer();
        startScanningBeaconsInRange();
    }
    /* when exit the region*/
    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "Did exit region called");
        END_TIME = timer();
        saveBeaconMonitorData();
    }
    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG, "Determine region state = " + state + " " + region.getUniqueId());
        //When already in a region start scanning near by beacons
        //if the did enter region is not called start time will be null, so set start time here
        if(START_TIME == null){
            START_TIME = timer();
        }
        startScanningBeaconsInRange();
    }
    /* To return the start and end time stamps*/
    public String timer() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            /*Find the current data and time*/
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void sendNotification(String direction) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setContentTitle("Venue Trace Application")
                .setContentText(direction + " a Beacon region");
        // .setSmallIcon(R.drawable.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
    /**
     * Use this method to save data to SQLite
     */
    private void saveBeaconMonitorData() {
        for (String scannedBeacon : nearByBeacons) {
            dbCipher.insert(scannedBeacon, START_TIME, END_TIME);
        }
//        try {
//          dbCipher.getUsers();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
    }
}
