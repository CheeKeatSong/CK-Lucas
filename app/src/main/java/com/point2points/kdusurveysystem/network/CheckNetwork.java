package com.point2points.kdusurveysystem.network;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class CheckNetwork {
    private static final String TAG = CheckNetwork.class.getSimpleName();

    private static final int SPLASH_DISPLAY_LENGTH = 500;

    public static boolean isInternetAvailable(final Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Connection error");
                    alertDialogBuilder.setMessage("Unable to connect with the server.\nCheck your internet connection and try again");
                    alertDialogBuilder.setNeutralButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = ((Activity)context).getIntent();
                            ((Activity)context).finish();
                            context.startActivity(intent);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }, SPLASH_DISPLAY_LENGTH);

            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }

}
