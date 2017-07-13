package com.turbolinks.app;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.content.pm.PackageManager;
import android.Manifest;
import android.webkit.WebSettings;
import android.content.ClipboardManager;
import android.content.Context;

import com.basecamp.turbolinks.TurbolinksView;
import com.turbolinks.app.Helper.TurbolinksHelper;
import com.basecamp.turbolinks.TurbolinksAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.turbolinks.app.Constants.INTENT_URL;
import static com.turbolinks.app.Constants.MODAL_WINDOW;

import com.basecamp.turbolinks.TurbolinksSession;


public class MainActivity extends AppCompatActivity implements TurbolinksAdapter {
    private static final String BASE_URL = "https://pdd-reach.herokuapp.com";

    @BindView(R.id.nodrawer_turbolinks_view)
    TurbolinksView turbolinksView;

    private TurbolinksHelper turbolinksHelper;
    private String location;
    private String path = "";
    private Boolean isModal = false;
    private String userToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.nodrawer_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        isModal = getIntent().hasExtra(MODAL_WINDOW);

        if (getSupportActionBar() != null && isModal){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        WebSettings settings;
        settings = TurbolinksSession.getDefault(getApplicationContext()).getWebView().getSettings();


        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        String pathFromIntent = getIntent().getStringExtra("path");
        if(pathFromIntent!=null) {
            path = getIntent().getStringExtra("path");
        }

        Log.d("PATHC", path);
        if(path.indexOf("locations") != -1) {
            Log.d("CLIPPYC", path);
            copyLink(location + path);
        }

        location = getIntent().hasExtra(INTENT_URL) ? getIntent().getStringExtra(INTENT_URL) : getString(R.string.base_url);
        turbolinksHelper = new TurbolinksHelper(this, this, turbolinksView, isModal);
        turbolinksHelper.visit(location + path);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    public void reload(){
        String newPath = path;
        if(!userToken.isEmpty()) {
            newPath = path + "?userToken="+userToken;
        }

        Log.d("PATH", path);
        if(path.indexOf("locations") != -1) {
            Log.d("CLIPPY", path);

            copyLink(location + path);
        }

        turbolinksHelper.visit(location + newPath, true);
    }

    public void copyLink(String url) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", url);
        clipboard.setPrimaryClip(clip);
    }

    public void setToken(String token){
        userToken = token;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("RESTART", "MEOW");

        turbolinksHelper.visit(location + path, true);
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode) {
        handleError(errorCode);
    }
    // The starting point for any href clicked inside a Turbolinks enabled site. In a simple case
    // you can just open another activity, or in more complex cases, this would be a good spot for
    // routing logic to take you to the right place within your app.
    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(INTENT_URL, location);

        this.startActivity(intent);

        Log.d("LOC", location);
        Log.d("LOC", action);
    }

    @Override
    public void visitCompleted() {
        Log.d("LOC", "CMPLETED");
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        handleError(statusCode);
    }

    @Override
    public void pageInvalidated() {

    }

    private void handleError(int code) {
        if (code == 404) {
            TurbolinksSession.getDefault(this)
                    .activity(this)
                    .adapter(this)
                    .restoreWithCachedSnapshot(false)
                    .view(turbolinksView)
                    .visit(BASE_URL + "/error");
        }
    }
}
