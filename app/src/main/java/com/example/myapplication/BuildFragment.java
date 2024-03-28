package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuildFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView brand_text;
    TextView product_text;
    TextView device_text;
    TextView hardware_text;
    TextView manufacturer_text;
    TextView release_text;
    TextView security_text;
    TextView sdk_text;
    TextView model_text;
    int sdk_version = Build.VERSION.SDK_INT;

    public BuildFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuildFragment newInstance(String param1, String param2) {
        BuildFragment fragment = new BuildFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_build, container, false);
        brand_text = v.findViewById(R.id.brand_info);
        product_text = v.findViewById(R.id.product_info);
        device_text = v.findViewById(R.id.device_info);
        hardware_text = v.findViewById(R.id.hardware_info);
        manufacturer_text = v.findViewById(R.id.manufacturer_info);
        release_text = v.findViewById(R.id.release_info);
        security_text = v.findViewById(R.id.security_info);
        sdk_text = v.findViewById(R.id.sdk_info);
        model_text = v.findViewById(R.id.model_info);

        setInformation();
        return v;
    }

    public void setInformation(){
        brand_text.setText(Build.BRAND);
        product_text.setText(Build.PRODUCT);
        hardware_text.setText(Build.HARDWARE);
        device_text.setText(Build.DEVICE);
        manufacturer_text.setText(Build.MANUFACTURER);
        model_text.setText(Build.MODEL);
        security_text.setText(Build.VERSION.SECURITY_PATCH);
        release_text.setText(Build.VERSION.RELEASE);
        sdk_text.setText(Integer.toString(sdk_version));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}