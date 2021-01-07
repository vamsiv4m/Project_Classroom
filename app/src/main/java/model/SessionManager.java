package model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Display;

import com.example.projectclassroom.LoginActivity;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String is_Login="IsLoggedIn";
    public static final String Username="username";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("userloginSession", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

    }

    public void storeData(String username){
        editor.putBoolean(is_Login,true);
        editor.putString(Username,username);
        editor.commit();

    }
    public HashMap<String,String> getUserData() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(Username,sharedPreferences.getString(Username,null));
        return userData;
    }
}

