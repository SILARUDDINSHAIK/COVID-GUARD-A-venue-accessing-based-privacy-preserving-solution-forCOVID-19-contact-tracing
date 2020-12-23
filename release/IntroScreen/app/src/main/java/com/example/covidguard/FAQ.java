package com.example.covidguard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*Frequently asked Questions*/
public class FAQ extends AppCompatActivity
{
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);
    }
    private void initData()
    {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        /*List Header upon clicking them message will be displayed*/
        listDataHeader.add("when to Turn on Bluetooth?");
        listDataHeader.add("What data will be stored in local storage?");
        listDataHeader.add("Is my data secure?");
        listDataHeader.add("When to upload my local data?");
        listDataHeader.add("What are permission codes?");
        listDataHeader.add("Where shall I get the permission codes?");
        listDataHeader.add("Do I need to enter permission codes for uploading the data?");
        listDataHeader.add("What is High Alert Notification?");
        listDataHeader.add("What is Exposure Notification?");
        listDataHeader.add("What is Contaminated Location?");
        listDataHeader.add("How my phones battery life is effected?");
        listDataHeader.add("Who are Health Authorities?");


        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("If you are sick please stay at home!");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("Fever");
        androidStudio.add("Cough");
        androidStudio.add("Headache");
        androidStudio.add("Nausea");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("While installing the application please turn on bluetooth");


        List<String> uwp = new ArrayList<>();
        uwp.add("Our application is highly secure we don't take any personal details");
        uwp.add("We only record the UUID of nearby beacons in your location and duration at venue");
        uwp.add("Still we don't require your location details");


        List<String> socialdistance = new ArrayList<>();

        socialdistance.add("The data collected by the application will only be stored in the local storage");
        socialdistance.add("With your own consent when diagnosed positive you can upload the data to the server");
        socialdistance.add("Still we don't require your location details");


        List<String> selfIsolate = new ArrayList<>();
        selfIsolate.add("Upload your data only when your diagnosed positive for covid-19");
        selfIsolate.add("Anyhow you require the permission code from health authorities");

        List<String> health = new ArrayList<>();
        health.add("Permission codes are like one time passwords given to each infected patients by health authorities");

        List<String> covidvsflue = new ArrayList<>();
        covidvsflue.add("From Health Authorities along with diagnostic report!");
        covidvsflue.add("Any infected person who is already a application can upload using Permission codes");


        List<String> why = new ArrayList<>();
        why.add("Yes! To avoid false positives user need to verify.");

        List<String> mask = new ArrayList<>();
        mask.add("High Alert Notification:: Requires immediate attention");
        mask.add("You might have visited a location at same time where the infected user present");
        mask.add("Test for Covid-19 and Self Isolate");

        List<String> contaminated = new ArrayList<>();
        contaminated.add("User might have visited the contaminated location");

        List<String> covidvsflue2 = new ArrayList<>();
        covidvsflue2.add("Location visted by the infected user may be contaminated");
        covidvsflue2.add("Covid-19 virus may survive on surfaces present on the location for hours");
        covidvsflue2.add("The surfaces touched by the user may leads to further spread");

        List<String> covidvsflue3 = new ArrayList<>();
        covidvsflue3.add("Health Authorities department from government who are admin of this application");

        /*call them accordingly*/
        listHash.put(listDataHeader.get(0),xamarin);
        listHash.put(listDataHeader.get(1),uwp);
        listHash.put(listDataHeader.get(2),socialdistance);
        listHash.put(listDataHeader.get(3),selfIsolate);
        listHash.put(listDataHeader.get(4),health);
        listHash.put(listDataHeader.get(5),covidvsflue);
        listHash.put(listDataHeader.get(6),why);
        listHash.put(listDataHeader.get(7),mask);
        listHash.put(listDataHeader.get(8),contaminated);
        listHash.put(listDataHeader.get(9),covidvsflue2);
        listHash.put(listDataHeader.get(11),covidvsflue3);


    }
}
