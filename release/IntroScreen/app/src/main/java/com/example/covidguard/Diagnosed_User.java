package com.example.covidguard;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.covidguard.backend.DBCipher;
import com.example.covidguard.beacon.BeaconHandler;

import java.util.Date;

public class Diagnosed_User extends AppCompatActivity{

    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosed_user);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);



    }
    private void setToggleEvent(GridLayout mainGrid)
    {
        /*Loop all child items to the  Main Grid*/
        for (int i = 0; i < mainGrid.getChildCount(); i++)
        {
            /* all the child items are cardview */
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1)
                    {
                        /*Change background color*/
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(Diagnosed_User.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(Diagnosed_User.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid)
    {
        /* Looping all child items of the gridlayout for setting the onclick event */
        for (int i = 0; i < mainGrid.getChildCount(); i++)
        {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(finalI ==0) {
                        /* Intent when onclick infected user page will */
                        Intent intent = new Intent(Diagnosed_User.this, InfectedUsers.class);
                        startActivity(intent);
                    }
                    else if(finalI == 1)
                    {
                        Intent intent = new Intent(Diagnosed_User.this, GeneralUsers.class);
                        startActivity(intent);
                    }
                    else if(finalI == 3)
                    {
                        Intent SAhelpLine_page = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.covid-19.sa.gov.au/health-advice/call-centres-and-information-lines#:~:text=SA%20COVID%2D19%20Information%20Line,19%20information%20for%20South%20Australians.&text=SA%20COVID%2D19%20Mental%20Health,for%20people%20surrounding%20COVID%2D19."));
                        startActivity(SAhelpLine_page);
                    }
                    else if(finalI ==5)
                    {
                        Intent HealthAuthorities_page = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sahealth.sa.gov.au/wps/wcm/connect/public+content/sa+health+internet/home/home"));
                        startActivity(HealthAuthorities_page);
                    }
                    else if(finalI ==2)
                    {
                        Intent covidtips = new Intent(Diagnosed_User.this, CovidTips.class);
                        startActivity(covidtips);
                    }
                    else if(finalI ==4)
                    {
                        Intent FAQ = new Intent(Diagnosed_User.this, com.example.covidguard.FAQ.class);
                        startActivity(FAQ);
                    }
                }
            });
        }
    }



}