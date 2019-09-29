package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendList extends AppCompatActivity {

    private ArrayList<Emailndname> friendlist;
    FirebaseUser mCurrentUser;
    DatabaseReference db;
    String user_mail,key_email;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        list=findViewById(R.id.friends);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        friendlist=new ArrayList<Emailndname>();
        db= FirebaseDatabase.getInstance().getReference();
//        Intent i=getIntent();
//        Bundle extra=i.getExtras();
//        if(extra!=null){
//            user_mail= (String) extra.get("current_user_mail");
//            Toast.makeText(FriendList.this, user_mail, Toast.LENGTH_SHORT).show();
//        }
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
//        userId=mCurrentUser.getUid();
        user_mail=mCurrentUser.getEmail();
        if(user_mail!=null){
            Email orig_mail= new Email(user_mail);
            key_email=orig_mail.convert_mail();
        }
        else{
            Toast.makeText(this, "no current user info", Toast.LENGTH_SHORT).show();
        }
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("friend_list").child(key_email).exists()) {
                    DataSnapshot ds=dataSnapshot.child("friend_list").child(key_email);
                    for (DataSnapshot data : ds.getChildren()) {
                        Emailndname em = data.getValue(Emailndname.class);
                        friendlist.add(em);
                    }
   //                 Toast.makeText(FriendList.this, friendlist.get(0).getEmail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("oncancelled",databaseError.toException());
            }
        });

        //ArrayAdapter<Emailndname> adapter=new ArrayAdapter<Emailndname>(this,R.layout.activity_item_view,friendlist);
        Adaptebanauga adapter=new Adaptebanauga(this,friendlist);
    //    Toast.makeText(this, Toast.LENGTH_SHORT).show();
        list.setAdapter(adapter);
    }
}
