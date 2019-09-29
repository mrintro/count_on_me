package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Make_Requests_Activity extends AppCompatActivity {

    private RememberUser userx;
    private String user_mail,str;
    private ProgressDialog progressDialog;
    private DatabaseReference db;
    private EditText req,pickup,dropdown;
    private Button makereq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_make__requests_);

        userx=new RememberUser(this);
        HashMap<String, String> reg_user = userx.getUserDetails();
        user_mail = reg_user.get(RememberUser.EMAIL);

        final Email email = new Email(user_mail);
        str = email.convert_mail();

        fetch();
        makereq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String reqs,pickups,dropdowns;
                reqs = req.getText().toString().trim();
                pickups = pickup.getText().toString().trim();
                dropdowns = dropdown.getText().toString().trim();
                if(validte(reqs,pickups,dropdowns)){

                    final FeedData feedData = new FeedData();
                    feedData.setEmail(user_mail);
                    feedData.setDropdown(dropdowns);
                    feedData.setPickup(pickups);
                    feedData.setReq(reqs);
                    db = FirebaseDatabase.getInstance().getReference();
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            int size= (int)dataSnapshot.child("Requests").child(str).getChildrenCount();

                            Task<Void> task = db.child("Requests").child(str).child(str+reqs+dropdowns).setValue(feedData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Make_Requests_Activity.this,"SUCCESSFULLY PLACED REQUEST",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Make_Requests_Activity.this,Main_Menu.class);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                };

            }
        });

    }

    private boolean validte(String reqs, String pickups, String dropdowns) {
        if(reqs.length()==0 || pickups.length()==0 || dropdowns.length()==0){
            Toast.makeText(Make_Requests_Activity.this,"PLEASE FILL CREDETIALS", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void fetch() {
        req= (EditText)findViewById(R.id.req_state);
        pickup = (EditText)findViewById(R.id.pick_up);
        dropdown = (EditText)findViewById(R.id.drop_down);
        makereq = (Button)findViewById(R.id.btn_makereq);
    }
}