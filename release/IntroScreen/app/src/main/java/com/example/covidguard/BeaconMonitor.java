package com.example.covidguard;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
//import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
public class BeaconMonitor extends Application implements BootstrapNotifier
{
    //private BackgroundPowerSaver backgroundPowerSaver;
    public void onCreate()
    {
        super.onCreate();
        /*Auto power saver*/
        //backgroundPowerSaver = new BackgroundPowerSaver(this);
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setDebug(true);
        setScanPeriodsToDesiredTimes();
        beaconManager.setAndroidLScanningDisabled(true);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        startForegroundService();
        /* callPotentiallyBrokenMethods after starting the service to avoid duplicates*/
        avoidenterexitduplicateevents();
        /*Specify any name for the region*/
        /* Testing venue is just a name for a 100meter proximity where the scanning is happening*/
        Region region = new Region("Testing Venue", null, null, null);
        RegionBootstrap regionBootstrap = new RegionBootstrap(this, region);

    }
    /*setting the scan periods*/
    protected void setScanPeriodsToDesiredTimes()
    {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundBetweenScanPeriod(40100l);
        beaconManager.setBackgroundScanPeriod(4100l);
        beaconManager.setRegionExitPeriod(90000l);
        beaconManager.setForegroundBetweenScanPeriod(40100l);
        beaconManager.setForegroundScanPeriod(4100l);
    }
    //to avoid the duplication of enter and exit of the regions.
    protected void avoidenterexitduplicateevents()
    {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        Region region = new Region("Testing Venue", null, null, null);
        try {
            beaconManager.updateScanPeriods();
            beaconManager.startRangingBeaconsInRegion(region);
            beaconManager.addRangeNotifier(new RangeNotifier()
            {
                /*List of beaacon/beacons detected when scanning happened*/
                /*This list will update everytime when a new scan happens*/
                /*Used hashset to avoid the duplicates of those UUID's*/
                private HashSet<Beacon> beaconsSeen = new HashSet<Beacon>();
                @Override
                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region)
                {
                    for (Beacon beacon : beacons)
                    {
                        /*printing the uuid to the console for checking*/
                        System.out.println("Beacon Detected" +beacon.getId1());

                    }
                }
         });
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
    /* For starting a foreground service */
    protected void startForegroundService()
    {
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.enableForegroundServiceScanning(getNotificationBuilder().build(), 123);
        beaconManager.setEnableScheduledScanJobs(false);
    }
    protected void getNotificationChannel()
    {
        NotificationChannel channel;
        channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public Notification.Builder getNotificationBuilder()
    {
        getNotificationChannel();
        Notification.Builder builder = new Notification.Builder(this, "notification_channel");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("CovidGuard is watching!");
        return builder;
    }
    /* when entered the region*/
    @Override
    public void didEnterRegion(Region region)
    {
        /*Notification for testing when entered the region*/
        sendNotification("Entered");
         /*start the timer*/
        String START_TIME = timer();
        sendNotification("time entered is " +timer());
        //sendNotification("...." +region.getId1().toString());
    }
   /* when exit the region*/
    @Override
    public void didExitRegion(Region region)
    {
        Log.d("EXITED",region.getUniqueId());
        /*when exited the region*/
        sendNotification("Exited");
        String END_TIME = timer();
        sendNotification("time exited is"+timer());
        //String s1 = region.getBluetoothAddress();
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region)
    {
        Log.d("STATE = "+ state, region.getUniqueId());
    }

    /* To return the start and end time stamps*/
    public String timer()
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            /*Find the current data and time*/
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    private void sendNotification(String direction)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setContentTitle("Covid Guard")
                .setContentText(direction + " Beacons Found")
                .setSmallIcon(R.drawable.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

}
