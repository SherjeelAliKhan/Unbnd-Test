package com.sherjeel.unbnd.test.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class CustomAlertBox {

    public interface AlertCallbackInterface {
        void onFinished();
    }

    public static void alertMessage (Activity activity, String title, String message, final AlertCallbackInterface alertCallbackInterface){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        AlertDialog alertDialog = builder1.create();
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertCallbackInterface.onFinished();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
