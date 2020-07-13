package com.sherjeel.unbnd.test.HttpHelper;

import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.sherjeel.unbnd.test.Configuration.Debugging;

public class HttpHelper {

    private HttpHelperResponse _response;
    private OkHttpClient client = new OkHttpClient();

    private static boolean debugging = Debugging.isOn;

    public HttpHelper(HttpHelperResponse _response) {
        this._response = _response;
    }

    public void request(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            if (debugging) { Log.d("HttpHelper", "URL:\n" + url); }
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (debugging) { Log.e("HttpHelper", "Error:\n" + e.toString()); }
                    //socket failed: EPERM (Operation not permitted) || SOLUTION: REINSTALL APPLICATION
                    _response.responseOfHttpHelper(false, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        if (debugging) { Log.e("HttpHelper", "Error:\n" + response); }
                        _response.responseOfHttpHelper(false, "Unexpected code " + response);
                    } else {
                        String res = response.body().string();
                        if (debugging) { Log.d("HttpHelper", "Response:\n" + res); }
                        _response.responseOfHttpHelper(true, res);
                    }
                }
            });
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            //client.cache().close();
        } catch (Exception error){
            if (debugging) { Log.e("HttpHelper", "Error:\n" + error.getMessage()); }
            _response.responseOfHttpHelper(false, error.getMessage());
        }
    }
}