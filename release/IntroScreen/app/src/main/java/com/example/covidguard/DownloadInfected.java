package com.example.covidguard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.covidguard.backend.DBCipher;
import com.example.covidguard.dto.VenueRecordDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/* To download once in  a day call this activity every 24 hrs */
public class DownloadInfected extends AppCompatActivity
{
    public DBCipher dbCipher;
  //  public boolean checkDownload;
    private Context myContext;
    public static final String TAG = "DOWNLOAD_DATA";
    public static final String bearer = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2MzM0MDE4MDUsImlhdCI6MTYwMTg2NTgwNX0.B_mOFUIRe11nQC_qIFOSWqbSF_vkDRMNPKfloVNVe8qwgxsZatZMyxzmd_JT90hwuAdfjjcr3EGfc4OZk1UTIA";
    public static final String ENDPOINT2 = "https://quiet-meadow-42471.herokuapp.com/report/all";
    public RequestQueue queue;
    public Context context;

    public DownloadInfected() {
    }

    public DownloadInfected(DBCipher dbCipher)
    {
        this.dbCipher = dbCipher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(myContext);
        setContentView(R.layout.activity_download_infecteduserdata);

    }
    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public void downloadLatestInfectedData(final Date lastUpdated, RequestQueue queue1) {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT2, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + bearer);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String requestBody = "";
                Log.d(TAG, "getBody: " + requestBody);
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d(TAG, "Error while posting rest request: " + requestBody);
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        Log.d(TAG,"Adding to queue");
        queue1.add(request);
    }
    public final Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response)
        {
            Gson gson = new Gson();
            List<VenueRecordDto> reports = gson.fromJson(response, new TypeToken<List<VenueRecordDto>>() {
            }.getType());
            //Toast.makeText(context,"Data successfully downloaded ", Toast.LENGTH_LONG).show();
            Log.d(TAG, "------Downloaded Data-------" + "\n" + reports.toString() + "------X------");
            for (VenueRecordDto report : reports)
            {
                dbCipher.insert_infect(report.getUUID(),report.getStartTime(),report.getEndTime());

            }
            //call open method here.
        }
    };

    public final Response.ErrorListener errorListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
            String errorResponse = parseVolleyError(error);
            //Toast.makeText(context,"Data download Failed: " + errorResponse, Toast.LENGTH_LONG).show();
        }
    };
    public String parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            return responseBody;
        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG, uee.getLocalizedMessage());
            return "";
        }
    }
}