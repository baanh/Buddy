package com.buddy.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.main.R;

public class Dialer extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;

    TextView tv_number;

    Button button_dial_1, button_dial_2, button_dial_3, button_dial_4, button_dial_5, button_dial_6, button_dial_7, button_dial_8, button_dial_9, button_dial_0, button_dial_clear, button_dial_call;

    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_util_dialer);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST);
            }
        }

        tv_number = (TextView) findViewById(R.id.tv_number);

        button_dial_1 = (Button) findViewById(R.id.b_1);
        button_dial_2 = (Button) findViewById(R.id.b_2);
        button_dial_3 = (Button) findViewById(R.id.b_3);
        button_dial_4 = (Button) findViewById(R.id.b_4);
        button_dial_5 = (Button) findViewById(R.id.b_5);
        button_dial_6 = (Button) findViewById(R.id.b_6);
        button_dial_7 = (Button) findViewById(R.id.b_7);
        button_dial_8 = (Button) findViewById(R.id.b_8);
        button_dial_9 = (Button) findViewById(R.id.b_9);
        button_dial_0 = (Button) findViewById(R.id.b_0);
        button_dial_clear = (Button) findViewById(R.id.b_clear);
        button_dial_call = (Button) findViewById(R.id.b_call);

        button_dial_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "1";
                tv_number.setText(number);
            }
        });

        button_dial_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "2";
                tv_number.setText(number);
            }
        });

        button_dial_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "3";
                tv_number.setText(number);
            }
        });

        button_dial_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "4";
                tv_number.setText(number);
            }
        });

        button_dial_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "5";
                tv_number.setText(number);
            }
        });

        button_dial_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "6";
                tv_number.setText(number);
            }
        });

        button_dial_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "7";
                tv_number.setText(number);
            }
        });

        button_dial_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "8";
                tv_number.setText(number);
            }
        });

        button_dial_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "9";
                tv_number.setText(number);
            }
        });

        button_dial_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + "0";
                tv_number.setText(number);
            }
        });

        button_dial_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = "";
                tv_number.setText(number);
            }
        });

        button_dial_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });


    }



    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Dialer.this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Dialer.this, "No permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}
