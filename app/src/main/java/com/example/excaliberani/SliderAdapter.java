package com.example.excaliberani;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SliderAdapter extends PagerAdapter {

    Context context;
    private LayoutInflater layoutInflater;
    ProgressDialog progressDialog;
    DatabaseReference db;
    RememberUser userx;
    private String user_mail;

    public SliderAdapter(Context context)
    {
        this.context = context;

    }

    //Arrays
    public int[] slide_images = {
            R.drawable.news,
            R.drawable.profile,
            R.drawable.makereq,
            R.drawable.makereq
    };

    public String[] slide_headings = {
            "News",
            "Profile",
            "Make Request",
            "Cancel Request"
    };




    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.slide_layout, container,false);

        ImageView slideImageView = (ImageView)view.findViewById(R.id.slide_image);
        final TextView slideHeading = (TextView)view.findViewById(R.id.slide_heading);
        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);

        container.addView(view);

        final int x = position;
        slideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(x){
                    case 0:
                        Intent intent = new Intent(context,ew_feed_page.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context,profile_activity.class);
                        context.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context,Make_Requests_Activity.class);
                        context.startActivity(intent2);

                        break;
                    case 3:
                        Intent intent3 = new Intent(context, Cancel_req.class);
                        context.startActivity(intent3);
                        break;


                }

            }
        });

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}





