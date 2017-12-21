package com.callerid.db;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

public class UsersDB extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "UsersDB";
    }

    public UsersDB(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void upload(String options, String passPhrase, Callback callback) {
        try {
//            long startTime = System.nanoTime();
            List<User> newUsers = new ArrayList<>();
            JSONArray arr = new JSONArray(options);
            DataBase database = DataBase.getDatabase(getReactApplicationContext(), passPhrase);
            if(null == database) {
                callback.invoke(new JSONException("Caller-ID database cannot be created"));
                return;
            }
            database.userDao().removeAllUsers();
            for (int i=0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                String name = o.getString("name");
                String number = o.getString("number");
                String i164 = o.getString("i164");
                newUsers.add(new User(name, number, i164));
            }
            database.userDao().addUsers(newUsers);
//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime);
//            Toast.makeText(getReactApplicationContext(), "Users added to DB. Duration: " + (duration / 1000000) + " msec", Toast.LENGTH_LONG).show();
            Toast.makeText(getReactApplicationContext(), "Users added to Caller-ID", Toast.LENGTH_LONG).show();
            callback.invoke();
        } catch (JSONException e) {
            e.printStackTrace();
            callback.invoke(e);
        }
    }
}