package com.turbolinks.app.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.TabHost;
import android.widget.TextView;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;

import com.turbolinks.app.BaseActivity;
import com.turbolinks.app.Helper.JsBridge.JsBridge;
import com.turbolinks.app.Helper.JsBridge.JsListener;
import com.turbolinks.app.MainActivity;
import com.turbolinks.app.R;

import static com.turbolinks.app.Constants.MODAL_WINDOW;


public class TurbolinksHelper implements JsListener, TurbolinksAdapter {

    private final String INTENT_URL = "intentUrl";
    private final String TOOLBAR_TITLE = "TOOLBAR_TITLE";

    private Context context;
    private Activity activity;
    private TurbolinksView turbolinksView;
    private String fromUrl = "";
    private Boolean isModal = false;

    public TurbolinksHelper(Context context, Activity activity, TurbolinksView turbolinksView, Boolean isModal) {
        this.context = context;
        this.activity = activity;
        this.turbolinksView = turbolinksView;
        this.isModal = isModal;

        setupTurbolinks();
    }

    public void visit(String url) {
        visit(url, false);
    }



    public void visit(String url, boolean withCachedSnapshot) {


            if (withCachedSnapshot) {
                TurbolinksSession.getDefault(context).activity(activity).adapter(this).restoreWithCachedSnapshot(true).view(turbolinksView).visit(url);
            } else {
                TurbolinksSession.getDefault(context).activity(activity).adapter(this).view(turbolinksView).visit(url);
            }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupTurbolinks() {
        TurbolinksSession.getDefault(context).setDebugLoggingEnabled(true);
        TurbolinksSession.getDefault(context).getWebView().getSettings().setUserAgentString("TurbolinksApp");
        TurbolinksSession.getDefault(context).getWebView().getSettings().setJavaScriptEnabled(true);
        TurbolinksSession.getDefault(context).getWebView().addJavascriptInterface(new JsBridge(context, this), "android");
        TurbolinksSession.getDefault(context).getWebView().getSettings().setAllowFileAccess(true);
        TurbolinksSession.getDefault(context).getWebView().getSettings().setAllowContentAccess(true);
        TurbolinksSession.getDefault(context).getWebView().getSettings().setAllowFileAccessFromFileURLs(true);
        TurbolinksSession.getDefault(context).getWebView().getSettings().setAllowUniversalAccessFromFileURLs(true);
        TurbolinksSession.getDefault(context).setPullToRefreshEnabled(false);
        TurbolinksSession.getDefault(context).setScreenshotsEnabled(true);
    }


    public void dismissView(){
        activity.finish();
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode) {

    }

    @Override
    public void pageInvalidated() {

    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {

    }

    @Override
    public void visitCompleted() {

        final Toolbar toolbar = (Toolbar) this.activity.findViewById(R.id.nodrawer_toolbar);
        final TextView titleText = (TextView) this.activity.findViewById(R.id.toolbar_title);

        WebView webview  = TurbolinksSession.getDefault(context).activity(activity).getWebView();
        if(toolbar!=null) {
            String title = webview.getTitle();
            if(title.startsWith("http")){
                title = "";
            }
            titleText.setText(title);
            if(title.length() > 60){
                titleText.setTextSize(16.0f);
            }
        }

    }

    private boolean shouldBeDismissedFromFormReturn(String toUrl){
        Uri toURI = Uri.parse(toUrl);
        fromUrl = TurbolinksSession.getDefault(context).activity(activity).adapter(this).view(turbolinksView).getWebView().getUrl();
        if(fromUrl != null) {
            Uri fromURI = Uri.parse(fromUrl);

            if(fromURI.getPath().endsWith("/splash") && toURI.getPath().endsWith("/home")){
                return true;
            }
        }
        return false;
    }


    @Override
    public void visitProposedToLocationWithAction(String location, String action) {

        if(shouldBeDismissedFromFormReturn(location)){
            activity.finish();
            return;
        }

        if(this.activity.getParent() instanceof BaseActivity){
            TabHost th = ((BaseActivity) this.activity.getParent()).getTabHost();
            if(location.endsWith("/rooms")){
                th.setCurrentTab(1);
                return;
            }else if(location.endsWith("/wines")){
                th.setCurrentTab(2);
                return;
            }else if(location.endsWith("/clubs")){
                th.setCurrentTab(3);
                return;
            }else if(location.endsWith("/contact")){
                th.setCurrentTab(4);
                return;
            }

        }

        if (action.equals("replace")) {
            visit(location, true);
        } else {

            visitToNewActivity(location);
        }
    }

    public void redirectToSelf(String url, int idResourceToolbarTitle, boolean isFullUrl) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        if (!isFullUrl) {
            intent.putExtra(INTENT_URL, context.getString(R.string.base_url) + url);
        }  else {
            intent.putExtra(INTENT_URL, url);
        }

        intent.putExtra(TOOLBAR_TITLE, context.getString(idResourceToolbarTitle));

        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

    public void redirectToSelf(String url, int idResourceToolbarTitle) {
        redirectToSelf(url, idResourceToolbarTitle, false);
    }

    private void visitNoDrawerActivity(int idResourceToolbarTitle, String location) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra(INTENT_URL, location);
        i.putExtra(TOOLBAR_TITLE, context.getString(idResourceToolbarTitle));
        i.putExtra(MODAL_WINDOW,true);
        activity.startActivity(i);
    }

    private void visitToNewActivity(@NonNull final String url) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(INTENT_URL, url);
        intent.putExtra(MODAL_WINDOW,true);
        activity.startActivity(intent);
    }
}
