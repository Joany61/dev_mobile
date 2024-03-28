package com.example.myapplication;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class WifiBluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            String stateStr = "OFF";
            switch (state){
                case BluetoothAdapter.STATE_OFF:
                    stateStr = "OFF";break;
                case BluetoothAdapter.STATE_ON:
                    stateStr = "ON";break;
            }
            updateFragmentBtfUI(context, stateStr);
        }
        else if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            String stateStr = "OFF";
            switch (state){
                case WifiManager.WIFI_STATE_ENABLED:
                    stateStr = "ON";break;
                case WifiManager.WIFI_STATE_DISABLED:
                    stateStr = "OFF";break;
            }
            updateFragmentWifUI(context, stateStr);
        }
        else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
            int state = intent.getIntExtra(TelephonyManager.EXTRA_STATE, TelephonyManager.DATA_DISCONNECTED);
            String stateStr = "OFF";
            switch (state){
                case TelephonyManager.DATA_CONNECTED:
                    stateStr = "ON";break;
                case TelephonyManager.DATA_DISCONNECTED:
                    stateStr = "OFF";break;
            }
            updateFragmentCellUI(context, stateStr);
        }
    }
    private void updateFragmentWifUI(Context context, String status) {
        // Find the Fragment and update its UI with the status
        Fragment fragment = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("frag_tag");
        if (fragment instanceof NetworkFragment) {
            ((NetworkFragment) fragment).updateFragmentWiUI(status);
        }
    }
    private void updateFragmentBtfUI(Context context, String status) {
        // Find the Fragment and update its UI with the status
        Fragment fragment = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("frag_tag");
        if (fragment instanceof NetworkFragment) {
            ((NetworkFragment) fragment).updateFragmentBtUI(status);
        }
    }
    private void updateFragmentCellUI(Context context, String status){
        Fragment fragment = ((MainActivity) context).getSupportFragmentManager().findFragmentByTag("frag_tag");
        if (fragment instanceof NetworkFragment){
            ((NetworkFragment) fragment).updateFragmentCellUI(status);
        }
    }
}