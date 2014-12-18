package com.keithsmyth.visualtimezone.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.keithsmyth.visualtimezone.R;
import com.keithsmyth.visualtimezone.adapter.CompareTimeAdapter;
import com.keithsmyth.visualtimezone.controller.CompareTimeController;

import java.util.ArrayList;

/**
 * @author keithsmyth
 */
public class CompareItemFragment extends Fragment {

    private static final String ARG_TIME_ZONES = "timeZones";

    private ArrayList<String> mTimeZones;
    private CompareTimeAdapter mAdapter;

    public static CompareItemFragment newInstance(ArrayList<String> timeZones) {
        CompareItemFragment fragment = new CompareItemFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_TIME_ZONES, timeZones);
        fragment.setArguments(args);
        return fragment;
    }

    public CompareItemFragment() {
        // Mandatory empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTimeZones = getArguments().getStringArrayList(ARG_TIME_ZONES);
        }

        generateListAdapter();
    }

    private void generateListAdapter() {
        // creates a controller and adapter for the listview
        CompareTimeController mController = new CompareTimeController(mTimeZones);
        mAdapter = new CompareTimeAdapter(mController);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compareitem, container, false);

        // current times layout
        LinearLayout headerLayout = (LinearLayout) view.findViewById(R.id.layout_header);
        for (String timeZone : mTimeZones) {
            addHeaderItem(inflater, headerLayout, timeZone);
        }

        // times list view
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        return view;
    }

    private void addHeaderItem(LayoutInflater inflater, LinearLayout headerLayout,
                               String timeZone) {
        // populates current time and time zone name

        View view = inflater.inflate(R.layout.item_compare_time_header_value, headerLayout, false);

        TextClock textClock = (TextClock) view.findViewById(R.id.txc_current_time);
        textClock.setTimeZone(timeZone);

        TextView textView = (TextView) view.findViewById(R.id.txt_timezone_name);
        textView.setText(timeZone);

        headerLayout
                .addView(view, new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
    }
}
