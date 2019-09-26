package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class notificationpage extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayList<Emailndname> details = new ArrayList<Emailndname>();
    private RememberUser userx;
    private String user_mail;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notificationpage);

        listView = (ListView)findViewById(R.id.list);
        userx = new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);
        final Email email = new Email(user_mail);
        final String str = email.convert_mail();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child("friend_req").child(str).exists()){
                   DataSnapshot ds = dataSnapshot.child("friend_req").child(str);
                   Emailndname y = ds.child("shashank2409@gmail_dot_com").getValue(Emailndname.class);
                   Toast.makeText(notificationpage.this,y.getEmail(),Toast.LENGTH_SHORT).show();
                   for(DataSnapshot x : ds.getChildren()){
                        Emailndname xy;

                        xy=x.getValue(Emailndname.class);
                        details.add(xy);
                   }

               }
               runadap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void runadap() {

        Adaptebanauga adap =new Adaptebanauga(notificationpage.this,details);
        listView.setAdapter(adap);
    }
}
