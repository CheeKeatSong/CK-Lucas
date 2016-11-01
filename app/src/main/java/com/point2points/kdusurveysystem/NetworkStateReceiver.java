package com.point2points.kdusurveysystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d(TAG, "Network connectivity change");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(TAG, "There's no network connectivity");
                Toast.makeText(context, "Connection Lost\nPlease reconnect before making changes to data.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
