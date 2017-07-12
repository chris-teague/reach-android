package com.turbolinks.app;

/**
 * Created by markbiegel on 23/1/17.
 */

import android.Manifest;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;


public class BaseActivity extends TabActivity{

    TabHost tabHost;
    private String path = "";
    private String token = "";

    private static final String[] INITIAL_PERMS={
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=225;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            this.finish();
            return;
        }

        startLocationTracking();

        final TabHost tabHost = getTabHost();

        Intent intentHome = new Intent().setClass(this, MainActivity.class);
        intentHome.putExtra("path","");
        TabSpec tabSpecHome = tabHost
                .newTabSpec("")
                .setIndicator("",getResources().getDrawable(R.drawable.home_active))
                .setContent(intentHome);

        Intent intentGoogle = new Intent().setClass(this, MainActivity.class);
        intentGoogle.putExtra("path","google");
        TabSpec tabSpecGoogle = tabHost
                .newTabSpec("Google")
                .setIndicator("", getResources().getDrawable(R.drawable.google))
                .setContent(intentGoogle);

        Intent intentFirefox = new Intent().setClass(this, MainActivity.class);
        intentFirefox.putExtra("path","firefox");
        TabSpec tabSpecFirefox = tabHost
                .newTabSpec("Firefox")
                .setIndicator("",getResources().getDrawable(R.drawable.firefox))
                .setContent(intentFirefox);

        Intent intentSafari = new Intent().setClass(this, MainActivity.class);
        intentSafari.putExtra("path","safari");
        TabSpec tabSpecSafari = tabHost
                .newTabSpec("Safari")
                .setIndicator("",getResources().getDrawable(R.drawable.safari))
                .setContent(intentSafari);

        Intent intentIceCat = new Intent().setClass(this, MainActivity.class);
        intentIceCat.putExtra("path","icecat");
        TabSpec tabSpecIceCat = tabHost
                .newTabSpec("IcaCat")
                .setIndicator("",getResources().getDrawable(R.drawable.icecat))
                .setContent(intentIceCat);

        // add all tabs
        tabHost.addTab(tabSpecHome);
        tabHost.addTab(tabSpecGoogle);
        tabHost.addTab(tabSpecFirefox);
        tabHost.addTab(tabSpecSafari);
        tabHost.addTab(tabSpecIceCat);

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(0);



        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                Activity currentActivity = getCurrentActivity();
                resetTabIcons(tabHost.getCurrentTab());
                if (currentActivity instanceof MainActivity) {
                    ((MainActivity) currentActivity).reload();
                }
            }});

    }

    private void resetTabIcons(Integer tabId){
        TabWidget t = getTabWidget();

        ViewGroup tabViewGroup;
        ImageView tabImage;


        tabViewGroup = (ViewGroup) getTabWidget().getChildAt(0);
        tabImage  = (ImageView) tabViewGroup.getChildAt(0);
        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.home));

        tabViewGroup = (ViewGroup) getTabWidget().getChildAt(1);
        tabImage  = (ImageView) tabViewGroup.getChildAt(0);
        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.google));

        tabViewGroup = (ViewGroup) getTabWidget().getChildAt(2);
        tabImage  = (ImageView) tabViewGroup.getChildAt(0);
        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.firefox));

        tabViewGroup = (ViewGroup) getTabWidget().getChildAt(3);
        tabImage  = (ImageView) tabViewGroup.getChildAt(0);
        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.safari));

        tabViewGroup = (ViewGroup) getTabWidget().getChildAt(4);
        tabImage  = (ImageView) tabViewGroup.getChildAt(0);
        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.icecat));




        tabViewGroup = (ViewGroup) getTabWidget().getChildAt(tabId);
        tabImage = (ImageView) tabViewGroup.getChildAt(0);
        switch(tabId){
            case 0:
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.home_active));
                break;
            case 1:
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.google_active));
                break;
            case 2:
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.firefox_active));
                break;
            case 3:
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.safari_active));
                break;
            case 4:
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.icecat_active));
                break;

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Activity currentActivity = getCurrentActivity();
        if (currentActivity instanceof MainActivity) {
            ((MainActivity) currentActivity).reload();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void startLocationTracking() {
        if (!canAccessLocation()) {
            Log.e("WAT", "REQUEST PERMS");
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        } else {


            Log.e("WAT", "LocationListener ");
            startService(new Intent(this, BGLocationService.class));
            Log.e("WAT", "START SERVICE ");
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {

        if (requestCode == INITIAL_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationTracking();
        }
    }


}