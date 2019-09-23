package com.example.excaliberani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class accept_req_page extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_req_page);

        Intent intent = getIntent();
        String abc = intent.getStringExtra("email");
        TextView t1 = (TextView)findViewById(R.id.t1);
        t1.setText(abc);

    }
}
