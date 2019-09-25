package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Main_Menu extends AppCompatActivity {

    private Button news, profile, search, notification, make_req, logout;
    RememberUser userx;
    private String user_mail;
    ProgressDialog progressDialog;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        setvalues();


        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);

        Email email = new Email(user_mail);
        final String str = email.convert_mail();

        db = FirebaseDatabase.getInstance().getReference();

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this,ew_feed_page.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this,profile_activity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this,searchbar.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this,notificationpage.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userx.logOutUser();
            }
        });
        make_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(Main_Menu.this);
                progressDialog.setMessage("Registering...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Toast.makeText(Main_Menu.this,"here",Toast.LENGTH_SHORT).show();

                        if(dataSnapshot.child("Requests").child(str).exists()){
                            progressDialog.dismiss();
                            Intent intent = new Intent(Main_Menu.this,cancel_request_Activity.class);
                            startActivity(intent);
                        }
                        else{
                            progressDialog.dismiss();
                            Intent intent = new Intent(Main_Menu.this,Make_Requests_Activity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(Main_Menu.this,"not good",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }

    private void setvalues() {
        news = (Button)findViewById(R.id.menu_news_feed);
        profile = (Button)findViewById(R.id.menu_PROFILE);
        search = (Button)findViewById(R.id.menu_SEARCH_FRIENDS);
        notification = (Button)findViewById(R.id.menu_NOTIFICATIONS);
        make_req = (Button)findViewById(R.id.menu_MAKE_REQUEST);
        logout = (Button)findViewById(R.id.menu_LOG_OUT);
    }
}
