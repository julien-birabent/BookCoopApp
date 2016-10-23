package com.julienbirabent.bookcoopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class StudentActivity extends AppCompatActivity {

    public final String SCAN_TARGET = "ONE_D_MODE";
    public final int REQUEST_CODE_SCAN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", SCAN_TARGET);
                intent.setPackage("com.google.zxing.client.android");
                startActivityForResult(intent, REQUEST_CODE_SCAN);

            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                Toast.makeText(getApplicationContext(), " ISBN :" + contents, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

}
