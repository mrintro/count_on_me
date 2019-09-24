package com.example.excaliberani;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class notificationpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notificationpage);
    }
}
