package com.example.excaliberani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class Main_Menu extends AppCompatActivity {

    private Button news, profile, search, notification, make_req, logout;
    RememberUser userx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        String user_mail = reg_user.get(RememberUser.EMAIL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        setvalues();

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
                Intent intent = new Intent(Main_Menu.this,SearchActivity.class);
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
                Intent intent = new Intent(Main_Menu.this,Make_Requests_Activity.class);
                startActivity(intent);
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
