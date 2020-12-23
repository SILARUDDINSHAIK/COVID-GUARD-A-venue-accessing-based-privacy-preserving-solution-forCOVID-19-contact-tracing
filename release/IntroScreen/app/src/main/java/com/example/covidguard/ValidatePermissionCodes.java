package com.example.covidguard;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidguard.backend.DBCipher;
import com.example.covidguard.dto.InfectedUserDto;
import com.example.covidguard.dto.VenueRecordDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class ValidatePermissionCodes extends AppCompatDialogFragment {
    public static final String TAG = "UPLOAD_DATA";
    public static final String bearer = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2MzM0MDE4MDUsImlhdCI6MTYwMTg2NTgwNX0.B_mOFUIRe11nQC_qIFOSWqbSF_vkDRMNPKfloVNVe8qwgxsZatZMyxzmd_JT90hwuAdfjjcr3EGfc4OZk1UTIA";
    public static final String ENDPOINT2 = "https://quiet-meadow-42471.herokuapp.com/report/new_incident";


    public EditText permission_code;
    public ExampleDialogListener listener;
    public RequestQueue queue;
    public Context context;
    public DBCipher dbCipher;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        dbCipher = new DBCipher(getActivity().getApplicationContext());
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialogue, null);
        builder.setView(view)
                .setTitle("Validate Permission Code")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                /* On confirm submit and verify the permission code with the codes of the backend*/
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String permissionCode = permission_code.getText().toString();
                        InfectedUserDto infectedUserDto = retrieveUserVenueData(permissionCode);

                        postRequest(infectedUserDto);
                    }
                });
        permission_code = view.findViewById(R.id.permission_code);
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    public interface ExampleDialogListener {
        void applyTexts(String username, String password);
    }


    public void postRequest(final InfectedUserDto infectedUserDto) {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT2, responseListener, errorListener) {
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + bearer);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gsonBuilder = new GsonBuilder().create();
                String requestBody = gsonBuilder.toJson(infectedUserDto);
                Log.d(TAG, "getBody: " + requestBody);

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d(TAG, "Error while posting rest request: " + requestBody);
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(request);
    }


    private final Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(context,"Data successfully uploaded ", Toast.LENGTH_LONG).show();
            saveInfectedPrefsData();
            //Truncate tables
//            DBCipher dbCipher = new DBCipher(context);
//            dbCipher.truncate();
      Log.i(TAG, response);
        }
    };

    //This method will be called when user is verified as infected. A callback is needed from Server.
    private void saveInfectedPrefsData() {

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("IsUserInfected",true);

        editor.commit();
    }

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            Log.e(TAG, error.toString());
            String errorResponse = parseVolleyError(error);

            Toast.makeText(context,"Data upload Failed: " + errorResponse, Toast.LENGTH_LONG).show();

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

    public InfectedUserDto retrieveUserVenueData(String permissionCode) {
        System.out.println("Uploading data");
        List<VenueRecordDto> venueList = dbCipher.getAll();
        System.out.println("Uploaded    data");


//        VenueRecordDto venue1 = new VenueRecordDto("123445e56", "2020-10-27 10:10:10", "2020-10-27 16:10:10");
//        VenueRecordDto venue2 = new VenueRecordDto("12324343d", "2020-10-26 16:10:10", "2020-10-26 20:10:10");
//
//        List<VenueRecordDto> venueList = new ArrayList<>();
//        venueList.add(venue1);
//        venueList.add(venue2);

        return new InfectedUserDto(permissionCode, venueList);
    }
}