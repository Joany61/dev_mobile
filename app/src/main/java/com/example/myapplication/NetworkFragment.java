package com.example.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView wifi_state;
    private TextView bt_state;
    private TextView cell_state;
    private ConnectivityManager cm;
    private BluetoothManager btmanager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NetworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NetworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetworkFragment newInstance(String param1, String param2) {
        NetworkFragment fragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(wifiReceiver, intentFilter);
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(btReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_network, container, false);     //necéssite un inflation avant de cibler les id
        wifi_state = v.findViewById(R.id.wifi_status);
        bt_state = v.findViewById(R.id.bt_status);
        cell_state = v.findViewById(R.id.cell_status);
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            int networkType = telephonyManager.getNetworkType();

            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    Log.d("mess", "gprs");
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    Log.d("mess","GSM");
                    break;
            }
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        Log.d("tel_ope_name", telephonyManager.getNetworkOperatorName());
        Log.d("tel_ope", telephonyManager.getNetworkOperator());
        return v;
    }
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) cell_state.setText("ON");
                }
            });
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cell_state.setText("OFF");
                    wifi_state.setText("OFF");
                }
            });
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        cm = requireActivity().getSystemService(ConnectivityManager.class);
        cm.requestNetwork(networkRequest, networkCallback);
    }

    public BroadcastReceiver wifiReceiver;
    {
        wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    // wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    int wifiExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                    switch (wifiExtra){
                        case WifiManager.WIFI_STATE_ENABLED:
                        requireActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                wifi_state.setText("ON");
                            }
                        });
                        String ss = WifiManager.EXTRA_NEW_RSSI;
                        Log.d("strength", ss);

                        case WifiManager.WIFI_STATE_DISABLED:
                        requireActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                wifi_state.setText("OFF");
                            }
                        });
                        Log.d("stat", "disabled");
                    }
                }
        };
    }
    public BroadcastReceiver btReceiver;
    {
        btReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // btmanager = (BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE);
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
                    int state_bt = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    handleBluetoothStateChange(context, state_bt);
                }
            }

            public void handleBluetoothStateChange(Context context, int state){
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        requireActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                bt_state.setText("OFF");
                            }
                        });break;
                    case BluetoothAdapter.STATE_ON:
                        requireActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                bt_state.setText("ON");
                            }
                        });break;
                }
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(wifiReceiver);
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(btReceiver);
    }
}