package com.gfg.gfgsos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText contact1, contact2, message;
    TextView current1, current2;
    Button save, sendSOS;
    SharedPreferences sharedPreferences;

    //Unique Keys for Shared Preference fields & it's name
    private static final String SHARED_PREFERENCE_NAME = "SOS_Info";
    private static final String CONTACT1_KEY = "CONTACT1";
    private static final String CONTACT2_KEY = "CONTACT2";
    private static final String MESSAGE_KEY = "MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hooks
        contact1 = findViewById(R.id.contact1);
        contact2 = findViewById(R.id.contact2);
        message = findViewById(R.id.message);
        current1 = findViewById(R.id.current1);
        current2 = findViewById(R.id.current2);
        save = findViewById(R.id.save);
        sendSOS = findViewById(R.id.send_SOS);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        //Setting TextViews with currently saved data
        String savedContact1 = sharedPreferences.getString(CONTACT1_KEY, null);
        String savedContact2 = sharedPreferences.getString(CONTACT2_KEY, null);
        String savedMessage = sharedPreferences.getString(MESSAGE_KEY, null);
        if(savedContact1!=null || savedContact2!=null || savedMessage!=null){
            current1.setText("Contact 1 : " + savedContact1);
            current2.setText("Contact 2 : " + savedContact2);
            message.setHint(savedMessage);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save data on Shared Preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CONTACT1_KEY, contact1.getText().toString());
                editor.putString(CONTACT2_KEY, contact2.getText().toString());
                editor.putString(MESSAGE_KEY, message.getText().toString());
                editor.apply();

                //Updating TextViews with currently saved data on Click
                String savedContact1 = sharedPreferences.getString(CONTACT1_KEY, null);
                String savedContact2 = sharedPreferences.getString(CONTACT2_KEY, null);
                String savedMessage = sharedPreferences.getString(MESSAGE_KEY, null);
                if(savedContact1!=null || savedContact2!=null || savedMessage!=null){
                    current1.setText("Contact 1 : " + savedContact1);
                    current2.setText("Contact 2 : " + savedContact2);
                    message.setHint(savedMessage);
                }
            }
        });

        sendSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking the permission to Send SMS from the device
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
                    //If permission is granted send message
                    sendFunction();
                }
                //else request permission
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},1);
                }
            }
        });


    }

    //Function to send the message
    private void sendFunction() {
        String savedContact1 = sharedPreferences.getString(CONTACT1_KEY, null);
        String savedContact2 = sharedPreferences.getString(CONTACT2_KEY, null);
        String savedMessage = sharedPreferences.getString(MESSAGE_KEY, null);
        if(!savedContact1.equals("") && !savedContact2.equals("") && !savedMessage.equals("")){
            //Initialize SMS Manager
            SmsManager smsManager = SmsManager.getDefault();
            //Send the messages
            smsManager.sendTextMessage(savedContact1,null, savedMessage,null,null);
            smsManager.sendTextMessage(savedContact2,null, savedMessage,null,null);
            //Confirmation Toast
            Toast.makeText(getApplicationContext(),"SOS Sent Successfully!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Enter The Details", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendFunction();
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_LONG).show();
        }
    }
}