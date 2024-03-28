package com.example.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView wifi_state;
    private TextView bt_state;
    private TextView bt_id;
    private TextView cell_state;
    private ConnectivityManager cm;
    private TextView rate;
    private TextView operatorName;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WifiBluetoothReceiver wifiBluetoothReceiver;
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
    public void onStart() {
        super.onStart();
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        wifiBluetoothReceiver = new WifiBluetoothReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(wifiBluetoothReceiver, filter);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_network, container, false);     //necéssite un inflation avant de cibler les id
        wifi_state = v.findViewById(R.id.wifi_status);
        bt_state = v.findViewById(R.id.bt_status);
        cell_state = v.findViewById(R.id.cell_status);
        rate = v.findViewById(R.id.data_rate);
        bt_id = v.findViewById(R.id.bt_id);
        operatorName = v.findViewById(R.id.operator_name);
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    Log.d("mess", "gprs");
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    Log.d("mess", "GSM");
                    break;
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operatorName.setText(telephonyManager.getSimOperatorName());
            }
        });
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
            }
            if (!bluetoothAdapter.isEnabled()) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bt_id.setText(bluetoothAdapter.getName());
                    }
                });
            }
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        return v;
    }
    public void updateFragmentBtUI(String status) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_state.setText(status);
            }
        });
    }
    public void updateFragmentWiUI(String status){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wifi_state.setText(status);
            }
        });
    }
    public void updateFragmentCellUI(String status){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cell_state.setText(status);
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        requireActivity().unregisterReceiver(wifiBluetoothReceiver);
    }

}