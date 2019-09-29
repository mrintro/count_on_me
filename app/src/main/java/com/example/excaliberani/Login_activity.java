//LOGIN PAGE USING AUTH FIREBASE BUT NOT USING SHARED PREFERENCES

package com.example.excaliberani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login_activity extends AppCompatActivity {
    float x1,x2,y1,y2;
    EditText emailId, password;
    Button btnSignIn,aniketWalaKaam;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog progressDialog;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog=new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        getSupportActionBar().hide();
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.lemail);
        emailId.setInputType(InputType.TYPE_CLASS_TEXT);
        password = findViewById(R.id.lpass);
        btnSignIn = findViewById(R.id.lbutton);
        db= FirebaseDatabase.getInstance().getReference();
        final RememberUser user=new RememberUser(this);

        //tvSignUp = findViewById(R.id.textView);
        //aniketWalaKaam = findViewById(R.id.excal_final_accpet_request);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
//                    Toast.makeText(Login_activity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    //Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    //startActivity(i);
                }
                else{
                    //Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString().trim();
                final String pwd = password.getText().toString().trim();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login_activity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }

                else  if(!(email.isEmpty() && pwd.isEmpty())) {
                    progressDialog.setMessage("Logging In...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login_activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login_activity.this, "Wrong Credetials", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    if(mFirebaseAuth.getCurrentUser().isEmailVerified()){
                                        user.createLoginSession(email, pwd);
                                        finish();
                                        Intent intToHome = new Intent(Login_activity.this, Main_Menu.class);
                                        intToHome.addFlags(intToHome.FLAG_ACTIVITY_NEW_TASK|intToHome.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intToHome);
                                        finish();
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(Login_activity.this,"Plese Verify Your Email First",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("login",e.getLocalizedMessage());
                            }
                        });
//                    }
//                    else{
//                        Toast.makeText(Login_activity.this, "Incorrect Password, Try again!", Toast.LENGTH_SHORT).show();
//                    }
               }
                else{
                    Toast.makeText(Login_activity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });

//        tvSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intSignUp = new Intent(Login_activity.this, MainActivity.class);
//                startActivity(intSignUp);
//                finish();
//            }
//        });
//
//        aniketWalaKaam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(Login_activity.this,accept_req_page.class);
//                startActivity(i);
//            }
//        });

    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                if(x1 > x2) {
                    Intent i = new Intent(Login_activity.this, Registration.class);
                    startActivity(i);
                }

                break;
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}