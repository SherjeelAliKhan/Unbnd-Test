package com.sherjeel.unbnd.test.Services;

import com.sherjeel.unbnd.test.ParseJson.VideoDetail;
import java.util.ArrayList;

public interface VideoDetailsServiceResponse {
    public void responseOfVideoDetailService(ArrayList<VideoDetail> data);
    public void responseOfVideoDetailService(String error);
}
