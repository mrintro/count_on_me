package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_activity extends AppCompatActivity {

    TextView username;
    Button friends;
    String userId,userEmail;
    DatabaseReference getName;
    FirebaseUser mCurrentUser;
//    CircleImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        username=findViewById(R.id.excal_my_username);
        friends=findViewById(R.id.excal_my_friendlist);
//        profilePic=findViewById(R.id.excal_my_profile_pic);
        getName= FirebaseDatabase.getInstance().getReference();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        userId=mCurrentUser.getUid();
        userEmail=mCurrentUser.getEmail();
//        getName.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String mail=dataSnapshot.child("users").child(userEmail).child("image").getValue().toString().trim();
////                                Picasso.get().load(mail).into(profilePic);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        Toast.makeText(MyProfile.this,userEmail+" "+userId,Toast.LENGTH_LONG).show();
        //Picasso.get().load().into(profilePic);
        getName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("users").child(convert_mail(userEmail)).child("name").getValue().toString().trim();
                username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                username.setText("Reload to get username");
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(profile_activity.this,FriendList.class);
                startActivity(i);
            }
        });

    }


    public String convert_mail(String orig_mail){
        int n=orig_mail.length();
        for(int i=0;i<n;i++){


            String tmp=orig_mail.substring(i,i+1);
            if(orig_mail.charAt(i)=='.'){
                orig_mail=orig_mail.substring(0,i)+"_dot_"+orig_mail.substring(i+1,n);
                n+=4;
                i+=5;


            }

        }
        return orig_mail;
    }

    public String extract_mail(String orig_mail){
        int n=orig_mail.length();
        for(int i=0;i<n-5;i++){
            String tmp=orig_mail.substring(i,i+5);
            if(tmp.equals("_dot_")){
                String temp1=orig_mail.substring(0,i);
                String temp2=orig_mail.substring(i+5,n);
                orig_mail=temp1+"."+temp2;
                n-=4;
            }
        }
        return orig_mail;
    }


}
