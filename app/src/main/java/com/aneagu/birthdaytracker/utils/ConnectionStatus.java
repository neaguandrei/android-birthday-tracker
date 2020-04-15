package com.aneagu.birthdaytracker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionStatus {

    private static ConnectionStatus instance = null;

    private Context context;

    private ConnectionStatus(Context context) {
        this.context = context;
    }

    private static ConnectionStatus getInstance(Context ctx) {
        if (instance == null) {
            instance = new ConnectionStatus(ctx);
        }

        return instance;
    }

    private boolean isOnline() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isConnected();
        } catch (Exception e) {
            Log.e("Connection error:", e.toString());
        }
        return connected;
    }

    public static boolean verifyConnection(Context context) {
        ConnectionStatus connection = ConnectionStatus.getInstance(context);
        return connection.isOnline();
    }
}
