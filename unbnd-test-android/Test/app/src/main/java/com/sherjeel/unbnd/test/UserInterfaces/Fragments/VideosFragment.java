package com.sherjeel.unbnd.test.UserInterfaces.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.sherjeel.unbnd.test.Configuration.Debugging;
import com.sherjeel.unbnd.test.Configuration.ImageThumbnails;
import com.sherjeel.unbnd.test.Configuration.IntentExtras;
import com.sherjeel.unbnd.test.UserInterfaces.Adapters.VideoListAdapter;
import com.sherjeel.unbnd.test.UserInterfaces.Activities.InteractiveVideoActivity;
import com.sherjeel.unbnd.test.Utilities.CustomAlertBox;
import com.sherjeel.unbnd.test.Utilities.ImageUtilities;
import com.sherjeel.unbnd.test.ParseJson.VideoDetail;
import com.sherjeel.unbnd.test.Services.VideoDetailServices;
import com.sherjeel.unbnd.test.Services.VideoDetailsServiceResponse;
import com.sherjeel.unbnd.test.Utilities.Loader;
import com.sherjeel.unbnd.test.R;
import com.sherjeel.unbnd.test.Utilities.Utils;
import java.io.File;
import java.util.ArrayList;

public class VideosFragment extends Fragment implements VideoDetailsServiceResponse {

    private String videoURL = Debugging.testURl;
    private boolean debugging = Debugging.isOn;
    private boolean playSavedVideo = Debugging.playSavedVideo;
    private boolean enabledInteractiveView;
    private boolean isVideoPlayerReady, isVideoThumbnailsReady;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; //milliseconds
    private long lastClickTime = 0;
    private Loader loader;
    private VideoView videoView;
    private Activity _activity;
    private View rootView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_videos2, container, false);
        _activity = getActivity();
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        loader = new Loader(_activity);

        if (Utils.internetCheck(_activity)) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                requestVideos();
            }
            loader.on();
            initializePlayer();
            playVideo(videoURL);
            changePlayedTime(0);
        } else {
            rootView = inflater.inflate(R.layout.fragment_internet, container, false);
        }
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        videoView.stopPlayback();
        Debugging.playSavedVideo = false;
        loader.off();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView.isPlaying()) { videoView.pause(); }
    }

    //======================================================= List View ================================================//
    private void requestVideos() {
        try {
            VideoDetailServices videoDetailServices = new VideoDetailServices(this);
            videoDetailServices.request();
        } catch (Exception e) {
            Log.e("VideosFragment","Error\n" +  e.getMessage());
            CustomAlertBox.alertMessage(_activity,"Error", "There is an some problem from server. Please try again later.", new CustomAlertBox.AlertCallbackInterface() {
                @Override
                public void onFinished() {
                    loader.on();
                    requestVideos();
                }
            });
        }
    }

    private void loadListView(final ArrayList<VideoDetail> data) {
        if (debugging) { Log.d("VideosFragment", "Data received! Size of data:" + data.size()); }
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        VideoListAdapter myAdapter = new VideoListAdapter(_activity, R.layout.videolist_item, data);
        listView.setAdapter(myAdapter);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        listView.setLayoutParams(params);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (debugging) { Log.d("VideosFragment", "Click on item No:" + i + "\nUrl:" + data.get(i).getUrl()); }
                playVideo(data.get(i).getUrl());
            }
        });
        isVideoThumbnailsReady = true;
        if(isVideoPlayerReady){
            loader.off();
            videoView.start();
        }
    }

    @Override
    public void responseOfVideoDetailService(ArrayList<VideoDetail> data) {
        loadListView(data);
    }

    @Override
    public void responseOfVideoDetailService(String error) {
        Log.e("VideosFragment","Error\n" +  error);
        loader.off();
        CustomAlertBox.alertMessage(_activity,"Error", "There is an some problem from server. Please try again later.", new CustomAlertBox.AlertCallbackInterface() {
            @Override
            public void onFinished() {
                loader.on();
                requestVideos();
            }
        });
    }
    //======================================================= List View ================================================//

    //=======================================================Video Player===============================================//
    private void initializePlayer() {
        MediaController mediaController = new MediaController(_activity);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.setBackground(null);
                //videoView.start();
                enabledInteractiveView = true;
                isVideoPlayerReady = true;
                if(isVideoThumbnailsReady){
                    loader.off();
                    mediaPlayer.start();
                }
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                isVideoPlayerReady = true;
                if(isVideoThumbnailsReady){
                    loader.off();
                }
                enabledInteractiveView = false;
                return false;
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onVideoClick();
                return false;
            }
        });
    }

    private void playVideo(String videoURL) {
        if (playSavedVideo) {
            Uri videoUri = getMedia("vid");
            videoView.setVideoURI(videoUri);
        } else {
            videoView.setVideoURI(Uri.parse(videoURL));
        }
    }

    private void onVideoClick() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick();
            lastClickTime = 0;
        } else {
            onSingleClick();
        }
        lastClickTime = clickTime;
    }

    private void onSingleClick() {
        /*if (videoView.isPlaying()) {
            videoView.pause();
        } else {
            videoView.start();
        }*/
    }

    private void onDoubleClick() {
        if(enabledInteractiveView) {
            transitionTo3DView();
        }
    }

    private void transitionTo3DView() {
        int playedTime = videoView.getCurrentPosition();
        videoView.pause();
        videoView.stopPlayback();
        loader.on();
        Intent intent = new Intent(getActivity(), InteractiveVideoActivity.class);
        intent.putExtra(IntentExtras.playSavedVideo, playSavedVideo);
        if(isVideoLiveStreaming(videoURL)) {
            intent.putExtra(IntentExtras.videoPlayedTime, 0);
            intent.putExtra(IntentExtras.isLiveStreamingVideo, true);
        } else {
            intent.putExtra(IntentExtras.videoPlayedTime,playedTime);
            intent.putExtra(IntentExtras.isLiveStreamingVideo, false);
        }
        intent.putExtra(IntentExtras.videoURL, videoURL);
        startActivityForResult(intent, ImageThumbnails.transitionRequestCode);
    }

    private void changePlayedTime(int playedTime) {
        videoView.pause();
        loader.on();
        ImageUtilities imageUtilities = new ImageUtilities();
        File file = imageUtilities.loadImage(_activity, ImageThumbnails.fileName);
        if (file != null && file.exists()) {
            Bitmap bitmap = imageUtilities.getBitmap(_activity, ImageThumbnails.fileName);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            videoView.setBackground(bitmapDrawable);
        }
        if(!isVideoLiveStreaming(videoURL)) {
            videoView.seekTo(playedTime);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageThumbnails.transitionRequestCode && resultCode == Activity.RESULT_OK) {
            int videoPlayedTime = data.getIntExtra(IntentExtras.videoPlayedTime, 0);
            changePlayedTime(videoPlayedTime);
        } else {
            if(debugging){ Log.e("VideoFragment", "Error:\nResult Code:"  + requestCode); }
        }
    }
    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + _activity.getPackageName() + "/raw/" + mediaName);
    }

    private boolean isVideoLiveStreaming(String url) {
        if (url.contains(".m3u8")) {
            return true;
        }
        return false;
    }
    /*
    private void playVideo(String url){
        webView.clearFormData();
        webView.clearCache(true);
        webView.setWebViewClient(new BrowserClient());
        webView.setWebChromeClient(new ChromeClient(getActivity()));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webView.loadUrl(url);
    }*/
    //=======================================================Video Player===============================================//
}
