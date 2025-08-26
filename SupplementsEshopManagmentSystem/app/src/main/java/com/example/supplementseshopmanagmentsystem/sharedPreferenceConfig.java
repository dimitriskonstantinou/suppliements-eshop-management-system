package com.example.supplementseshopmanagmentsystem;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPreferenceConfig {

    SharedPreferences sharedPreferences;

    Context context;

    public sharedPreferenceConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference),Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status), status);
        editor.apply();
    }

    public boolean readLoginStatus(){

        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status), false);
        return status;
    }

    public void writeUsername(String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.username_on_app_destroyed), username);
        editor.apply();
    }

    public String readUsername(){

       String string;
       string = sharedPreferences.getString(context.getResources().getString(R.string.username_on_app_destroyed),"");
       return string;
    }
}

