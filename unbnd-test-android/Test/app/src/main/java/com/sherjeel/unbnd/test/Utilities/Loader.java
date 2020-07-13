package com.sherjeel.unbnd.test.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;

public class Loader {

    private ProgressDialog progressDialog;

    public Loader(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    public void on(){
        progressDialog.show();
    }

    public void off(){
        progressDialog.cancel();
    }
}
