package com.example.covidguard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* UI component */
public class CovidTips extends AppCompatActivity
{
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

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
        listDataHeader.add("Sick? Stay at Home");
        listDataHeader.add("Common Symptoms");
        listDataHeader.add("Wash Your Hands Often");
        listDataHeader.add("Wear a Mask");
        listDataHeader.add("Avoid Touching your nose and mouth");
        listDataHeader.add("Don't Include in Large Public Gatherings!");
        listDataHeader.add("Maintain Social Distancing");
        listDataHeader.add("Self Isolate Yourself");
        listDataHeader.add("Covid-19 vs Flue");
        listDataHeader.add("Who is at the increased risk?");
        listDataHeader.add("Maintain Social Distancing");


        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("If you are sick please stay at home!");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("Fever");
        androidStudio.add("Cough");
        androidStudio.add("Headache");
        androidStudio.add("Nausea");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("Use sanitiser");
        xamarin.add("Wash hands regularly when touched any surfaces");

        List<String> uwp = new ArrayList<>();
        uwp.add("It's bette to avoid the large pubic gatherings.");

        List<String> socialdistance = new ArrayList<>();
        socialdistance.add("Social Distancing is the best practice to avoid the spread");

        List<String> selfIsolate = new ArrayList<>();
        selfIsolate.add("If you have any mild symptoms get tested and self isolate");

        List<String> covidvsflue = new ArrayList<>();
        covidvsflue.add("Check SA Health for the latest updates");

        List<String> mask = new ArrayList<>();
        mask.add("Wear a Mask");
        mask.add("Avoid handshakes");
        mask.add("Avoid touching common surfaces ");


        /*call them accordingly*/
        listHash.put(listDataHeader.get(0),edmtDev);
        listHash.put(listDataHeader.get(1),androidStudio);
        listHash.put(listDataHeader.get(2),xamarin);
        listHash.put(listDataHeader.get(3),mask);
        listHash.put(listDataHeader.get(4),uwp);
        listHash.put(listDataHeader.get(5),socialdistance);
        listHash.put(listDataHeader.get(6),selfIsolate);
        listHash.put(listDataHeader.get(7),covidvsflue);


    }
}
