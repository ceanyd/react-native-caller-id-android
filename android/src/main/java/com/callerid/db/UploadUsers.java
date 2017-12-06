package com.callerid.db;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
//import android.widget.Toast;


public class UploadUsers extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "UploadUsers";
    }

    public UploadUsers(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void upload(ReadableMap options, Callback complete) {

//        final Callback completeCallback = complete;
//        Toast.makeText(getReactApplicationContext(), "LOLOOOOLLOL", Toast.LENGTH_SHORT).show();
//        try {

//            String uri = options.getString("uri");
//            Uri file_uri = Uri.parse(uri);
//            File file = new File(file_uri.getPath());
//
//            String url = options.getString("uploadUrl");
//            String mimeType = options.getString("mimeType");
//            String fileName = options.getString("fileName");
//            ReadableMap headers = options.getMap("headers");
//            ReadableMap data = options.getMap("data");


//            if (!response.isSuccessful()) {
//                Log.d(TAG, "Unexpected code" + response);
//                completeCallback.invoke(response, null);
//                return;
//            }

//            completeCallback.invoke(null, response.body().string());
//        } catch(Exception e) {
//            Log.d(TAG, e.toString());
//        }
    }
}