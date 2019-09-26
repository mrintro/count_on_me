package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Main_Menu extends AppCompatActivity {


    private ViewPager mSlideViewPager;
    private ImageView b;
    private ImageView bb;
    private ImageView bbb;
    private RememberUser userx;
    private String user_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main__menu);
//        setvalues();

        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);

        mSlideViewPager = (ViewPager)findViewById(R.id.slideViewPager);
        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        b = (ImageView) findViewById(R.id.notification);
        bb = (ImageView) findViewById(R.id.search);
        bbb = (ImageView) findViewById(R.id.log_out);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this,notificationpage.class);
                startActivity(intent);
            }
        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this, searchbar.class);
                startActivity(intent);
            }
        });

        bbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(Main_Menu.this);
                builder.setMessage("DO YOU REALLY WANT TO LOG OUT")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                userx.logOutUser();
                                Intent intent = new Intent(Main_Menu.this, Login_activity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No",null);
                AlertDialog alert=builder.create();
                alert.show();



            }
        });

    }

}