package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StorageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StorageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private long totalBytes = 0;
    //
    TextView total_t;
    TextView used_t;
    TextView free_t;

    // Chart
    ArrayList<PieEntry> entries = new ArrayList<>();
    private PieChart pieChart;
    public StorageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StorageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StorageFragment newInstance(String param1, String param2) {
        StorageFragment fragment = new StorageFragment();
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
        View v = inflater.inflate(R.layout.fragment_storage, container, false);
        pieChart = v.findViewById(R.id.pieChart);
        total_t = v.findViewById(R.id.total_state);
        free_t = v.findViewById(R.id.free_state);
        used_t = v.findViewById(R.id.used_state);
        checkStorage();
        return v;
    }
    public void checkStorage(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBytes = stat.getTotalBytes() / (1024 * 1024);
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        }
        else {
            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        }
        long megAvailable = bytesAvailable / (1024 * 1024);

        total_t.setText(Long.toString(totalBytes) + " MB");
        free_t.setText(Long.toString(megAvailable) + " MB");
        used_t.setText(Long.toString(totalBytes-megAvailable) + " MB");

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        // pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry((totalBytes-megAvailable), "% Used"));
        values.add(new PieEntry(megAvailable, "% Free"));

        PieDataSet dataSet = new PieDataSet(values, "Space");
        dataSet.setSliceSpace(1f);
        dataSet.setSelectionShift(5f);
        int[] colors = {Color.YELLOW, Color.CYAN};
        dataSet.setColors(colors);

        pieChart.animateY(1000, Easing.EaseInCubic);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setData(data);

        ViewGroup.LayoutParams params = pieChart.getLayoutParams();
        params.width = DpToPx(250);
        params.height = DpToPx(250);
        pieChart.setLayoutParams(params);
    }
    public int DpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}