package com.sherjeel.unbnd.test.ASync;

import android.os.AsyncTask;

public class MyASyncTask extends AsyncTask<Object,Object,Void> {

    private ASyncResponse aSyncResponse;

    public MyASyncTask(Object aSyncResponse) {
        this.aSyncResponse = (ASyncResponse) aSyncResponse;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        publishProgress(objects[0]);
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        aSyncResponse.responseOfASync(values[0]);
    }
}