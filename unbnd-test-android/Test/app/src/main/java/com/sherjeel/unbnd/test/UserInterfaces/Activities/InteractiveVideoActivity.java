package com.sherjeel.unbnd.test.UserInterfaces.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import com.sherjeel.unbnd.test.Configuration.Debugging;
import com.sherjeel.unbnd.test.Configuration.IntentExtras;
import com.sherjeel.unbnd.test.R;
import com.sherjeel.unbnd.test.UserInterfaces.InteractiveVideo.VideoSurfaceView;

public class InteractiveVideoActivity extends Activity {

    private VideoSurfaceView videoSurfaceView;
    private boolean debugging = Debugging.isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity _activity = this;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (hasGLES20()) {
            try {
                final MediaPlayer mediaPlayer;
                final int videoPlayedTime = getIntent().getIntExtra(IntentExtras.videoPlayedTime, 0);
                final String videoURL = getIntent().getStringExtra(IntentExtras.videoURL);
                final boolean isLiveStreamingVideo = getIntent().getBooleanExtra(IntentExtras.isLiveStreamingVideo, false);
                final boolean playSavedVideo = getIntent().getBooleanExtra(IntentExtras.playSavedVideo, false);

                if(debugging) {
                    Log.d("InteractiveVideoAct", "Video Played Time:\n" + videoPlayedTime);
                    Log.d("InteractiveVideoAct", "Video URL:\n" + videoURL);
                }
                if(playSavedVideo){
                    mediaPlayer = MediaPlayer.create(_activity, R.raw.vid);
                    mediaPlayer.seekTo(videoPlayedTime);
                    mediaPlayer.setLooping(true);
                } else {
                    mediaPlayer = new MediaPlayer();
                    //mediaPlayer = MediaPlayer.create(_activity, Uri.parse(videoURL));
                    mediaPlayer.setDataSource(videoURL);
                    mediaPlayer.prepare();
                    if(!isLiveStreamingVideo) {
                        mediaPlayer.seekTo(videoPlayedTime);
                    }
                }
                videoSurfaceView = new VideoSurfaceView(_activity, mediaPlayer);
                videoSurfaceView.setPreserveEGLContextOnPause(true);
                _activity.setContentView(videoSurfaceView);
                setContentView(videoSurfaceView);
            } catch (Exception error) {
                if(debugging) { Log.e("InteractiveVideoAct", "Error:\n" + error.toString()); }
            }
        } else {
            if(debugging) { Log.e("InteractiveVideoAct", "There is no GLES20"); }
        }
    }

    private boolean hasGLES20() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }

    @Override
    public void onBackPressed() {
        videoSurfaceView.endActivity();
    }

    @Override
    protected void onResume() {
        //mGLView.resumeVideo();
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoSurfaceView.pauseVideo();
        super.onPause();
    }
}
