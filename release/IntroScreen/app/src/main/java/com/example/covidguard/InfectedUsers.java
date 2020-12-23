package com.example.covidguard;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


public class InfectedUsers extends AppCompatActivity implements ValidatePermissionCodes.ExampleDialogListener{

    CheckBox checkBox;
    Button b1;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        checkBox =findViewById(R.id.infected_user);
        TextView txtInfo = (TextView)findViewById(R.id.txtinfo);
        b1 = findViewById(R.id.upload);

         /*On click verifyPermission code button dialog will open*/
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openDialog();
            }
        });
        /* On click Infected display the upload button*/
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    b1.setVisibility(View.VISIBLE);
                }
                /* Normal User view*/
                else
                {
                    b1.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void openDialog() {
        ValidatePermissionCodes exampleDialog = new ValidatePermissionCodes();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void applyTexts(String username, String password) {

    }
}
