package com.d3if3150.catatin.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.d3if3150.catatin.R

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

        /*Function to check the network connection.
        Based on the result, decides whether to refresh the display or keep the current display */
        if (networkInfo != null) {
            refreshDisplay = true
            Toast.makeText(context, R.string.connected, Toast.LENGTH_SHORT).show()

            //Otherwise, the app can't download content also, because there is no network
            //connection (mobile or wifi)
            //set refreshDisplay to false
        } else {
            refreshDisplay = false
            Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val WIFI = "Wi-Fi"

        //check for wifi connection
        var wifiConnected = false

        //check for mobile connection
        var mobileConnected = false

        //check if the display should be refreshed
        var refreshDisplay = true
    }

}