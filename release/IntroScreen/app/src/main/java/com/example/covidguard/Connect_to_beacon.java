package com.example.covidguard;
import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.annotation.Nullable;
import com.example.covidguard.beacon.BeaconHandler;

import java.util.Calendar;

public class Connect_to_beacon extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final int BLUETOOTH_REQ_CODE = 1;
    Button bluetooth,cont;
    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bluetooth = findViewById(R.id.bluetooth);
        cont = findViewById(R.id.cont);
        checkPermissions();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(), "This device won't support", Toast.LENGTH_LONG).show();
        }
        else {
            /* Check if connection exists set text to the button accordingly*/
            if (!bluetoothAdapter.isEnabled())
            {
                bluetooth.setText("Turn Bluetooth ON");//Button visible only if the bluetooth is not currently on
            }
            else {
                bluetooth.setVisibility(Button.INVISIBLE);//If the bluetooth of the device is already on then this button will be disabled.

            }

        }
        /* Check bluetooth */
        bluetooth.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bluetoothIntent,BLUETOOTH_REQ_CODE);

            }
        });
        /* on click continue go to next page */
        cont.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Start timer here. to match the data every 24 hours/10 mins/whatever time.
               // setAlarm();

                startActivity(new Intent(Connect_to_beacon.this, Diagnosed_User.class));

            }
        });
    }

//    private void setAlarm() {
//        // Retrieve a PendingIntent that will perform a broadcast
//        Intent alarmIntent = new Intent(Connect_to_beacon.this,
//                AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                Connect_to_beacon.this, 0, alarmIntent, 0);
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        // Set the alarm to start at 10:00 AM
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 10);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//
//        manager.setRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(), 86400000,
//                pendingIntent);  // for repeating in every 24 hours
//    }

    /* Need Location Permission only to Andriod 10 devices */
    private void checkPermissions() {
        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Access");
            builder.setMessage("Please grant location access to detect beacons");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }
        else{
            startBeaconService();
        }
    }


    /* Need Location Permission only to Andriod 10 devices for detecting the beacons */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startBeaconService();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void startBeaconService() {
        Log.d("PERMISSIONS", "coarse location permission granted");
        try {
            Intent beaconService = new Intent(Connect_to_beacon.this, BeaconHandler.class);
            startService(beaconService);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.d(BeaconHandler.TAG, "ENTERING BEACON HANDLER");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Toast.makeText(Connect_to_beacon.this, "Bluetooth is ON", Toast.LENGTH_SHORT).show();
            bluetooth.setVisibility(Button.INVISIBLE);
        }
        else if (resultCode == RESULT_CANCELED)
        {
            Toast.makeText(Connect_to_beacon.this, "Bluetooth operation is cancelled",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
