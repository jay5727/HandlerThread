package com.jay.myproject.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetStatus {
    //returns boolean true if a network connection is available.
    public static boolean checkConnection(Context ctx) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = mConnectivityManager.getActiveNetworkInfo();
        return ni != null;
    }
}