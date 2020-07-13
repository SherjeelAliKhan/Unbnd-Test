package com.sherjeel.unbnd.test.Services;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.sherjeel.unbnd.test.ASync.ASyncResponse;
import com.sherjeel.unbnd.test.Configuration.Debugging;
import com.sherjeel.unbnd.test.Configuration.APIs;
import com.sherjeel.unbnd.test.HttpHelper.HttpHelper;
import com.sherjeel.unbnd.test.HttpHelper.HttpHelperResponse;
import com.sherjeel.unbnd.test.ASync.MyASyncTask;
import com.sherjeel.unbnd.test.ParseJson.VideoDetail;

public class VideoDetailServices implements HttpHelperResponse, ASyncResponse {

    private VideoDetailsServiceResponse _response;

    private static boolean debugging = Debugging.isOn;

    public VideoDetailServices(VideoDetailsServiceResponse response) {
        this._response = response;
    }

    public void request() {
        String url = APIs.videoDetailAPI;
        if(debugging) { Log.d("VideoDetailServices", "Url:\n" + url); }
        HttpHelper httpHelper = new HttpHelper(this);
        httpHelper.request(url);
    }

    @Override
    public void responseOfHttpHelper(boolean successful, String data) {
        if(successful) {
            if (debugging) { Log.d("VideoDetailServices", "Get Data:\n" + data); }
            Gson gson = new Gson();
            try {
                JSONArray jsonArray = new JSONArray(data);
                ArrayList<VideoDetail> videosDetails = new ArrayList<VideoDetail>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    videosDetails.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), VideoDetail.class));
                    if (debugging) { Log.d("VideoDetailServices", "Each Video Thumbnail:" + videosDetails.get(i).getThumbnail()); }
                }
                MyASyncTask myASyncTask = new MyASyncTask(this);
                myASyncTask.execute(videosDetails);
            } catch (JSONException error) {
                if (debugging) { Log.e("VideoDetailServices", "Error:\n" + error.getMessage()); }
                _response.responseOfVideoDetailService(error.getMessage());
            }
        } else {
            String errorMessage = data;
            if (debugging) { Log.e("VideoDetailServices", "Error:\n" + errorMessage); }
            _response.responseOfVideoDetailService(errorMessage);
        }
    }

    @Override
    public void responseOfASync(Object o) {
        ArrayList<VideoDetail> videoDetail = (ArrayList<VideoDetail>) o;
        _response.responseOfVideoDetailService(videoDetail);
    }
}
