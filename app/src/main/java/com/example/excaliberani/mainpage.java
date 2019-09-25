package com.example.excaliberani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class mainpage extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private ImageView b;
    private ImageView bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainpage);

        mSlideViewPager = (ViewPager)findViewById(R.id.slideViewPager);
        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        b = (ImageView) findViewById(R.id.notification);
        bb = (ImageView) findViewById(R.id.search);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainpage.this,notificationpage.class);
                startActivity(intent);
            }
        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainpage.this, searchbar.class);
                startActivity(intent);
            }
        });
    }






}
