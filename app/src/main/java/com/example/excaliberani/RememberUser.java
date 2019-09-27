package com.example.excaliberani;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

//remembers previously logged in user

public class RememberUser {
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME="UserDetails";
    private static final String PREF_USER_NAME="username";
    int PRIVATE_MODE=0;
    private static final String IS_USER_LOGIN="IsUserLoggedIn";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";

    public RememberUser(Context context){
        this.context=context;
        preferences=context.getSharedPreferences(PREF_USER_NAME,PRIVATE_MODE);
        editor=preferences.edit();
    }

    public void createLoginSession(String email, String password){

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(PASSWORD, password);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_USER_LOGIN,false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent=new Intent(context,Login_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
//            context.finish();
            //        return true;
        }
        else{
            Intent intent=new Intent(context,Main_Menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
//           context.finish();
//
//            return;
        }
        //   return false;
    }

    public void logOutUser(){
        editor=preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context,Login_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<>();
        user.put(EMAIL,preferences.getString(EMAIL, null));
        user.put(PASSWORD,preferences.getString(PASSWORD, null));

        return user;
    }


}
