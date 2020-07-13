package com.sherjeel.unbnd.test.UserInterfaces.VideoPlayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

public class ChromeClient extends WebChromeClient {

    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private int originalOrientation;
    private int originalSystemUiVisibility;
    private Activity _activity;

    public ChromeClient(Activity activity) {
        _activity = activity;
    }

    public Bitmap getDefaultVideoPoster() {
        if (customView == null) {
            return null;
        }
        return BitmapFactory.decodeResource(_activity.getApplicationContext().getResources(), 2130837573);
    }

    public void onHideCustomView() {
        ((FrameLayout) _activity.getWindow().getDecorView()).removeView(customView);
        customView = null;
        _activity.getWindow().getDecorView().setSystemUiVisibility(originalSystemUiVisibility);
        _activity.setRequestedOrientation(originalOrientation);
        customViewCallback.onCustomViewHidden();
        customViewCallback = null;
    }

    public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
        if (customView != null) {
            onHideCustomView();
            return;
        }
        customView = paramView;
        originalSystemUiVisibility = _activity.getWindow().getDecorView().getSystemUiVisibility();
        originalOrientation = _activity.getRequestedOrientation();
        customViewCallback = paramCustomViewCallback;
        ((FrameLayout) _activity.getWindow().getDecorView()).addView(this.customView, new FrameLayout.LayoutParams(-1, -1));
        _activity.getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
