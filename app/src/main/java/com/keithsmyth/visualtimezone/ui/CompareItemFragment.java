package com.keithsmyth.visualtimezone.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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

import org.joda.time.DateTime;

import java.util.ArrayList;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompareItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTimeZones = getArguments().getStringArrayList(ARG_TIME_ZONES);
        }

        generateListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compareitem, container, false);

        // current times layout
        LinearLayout currentTimeLayout = (LinearLayout) view.findViewById(R.id.layout_current_time);
        for (String timeZone : mTimeZones) {
            createTextClock(currentTimeLayout, timeZone);
        }

        // times list view
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        mAdapter.addHeaders(listView);

        return view;
    }

    private void createTextClock(LinearLayout currentTimeLayout, String timeZone) {
        // creates a TextClock widget for viewing the current time
        TextClock textClock = new TextClock(getActivity());
        textClock.setTimeZone(timeZone);
        textClock.setGravity(Gravity.CENTER);
        currentTimeLayout.addView(textClock,
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
    }

    private void generateListAdapter() {
        // creates a controller and adapter for the listview
        CompareTimeController mController = new CompareTimeController(mTimeZones);
        mAdapter = new CompareTimeAdapter(mController);
    }
}
