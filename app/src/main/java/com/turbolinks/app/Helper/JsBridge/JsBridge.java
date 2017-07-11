package com.turbolinks.app.Helper.JsBridge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;


public class JsBridge {

    private Context context;
    private JsListener listener;


    public JsBridge(@NonNull Context context, @NonNull JsListener listener) {
        this.context = context;
        this.listener = listener;
    }


    @JavascriptInterface
    public void postMessage(String jsonString)  {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if(jsonObject.has("language")){
                //TODO: Setting the language for the device native parts....

            }
        }catch (JSONException e){

        }


    }

}

