package com.turbolinks.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.basecamp.turbolinks.TurbolinksView;
import com.turbolinks.app.Helper.TurbolinksHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.turbolinks.app.Constants.INTENT_URL;
import static com.turbolinks.app.Constants.MODAL_WINDOW;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.nodrawer_turbolinks_view)
    TurbolinksView turbolinksView;

    private TurbolinksHelper turbolinksHelper;
    private String location;
    private String path = "";
    private Boolean isModal = false;



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

        String pathFromIntent = getIntent().getStringExtra("path");
        if(pathFromIntent!=null) {
            path = getIntent().getStringExtra("path");
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
        turbolinksHelper.visit(location + path, true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        turbolinksHelper.visit(location + path, true);
    }


}
