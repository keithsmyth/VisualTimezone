package com.keithsmyth.visualtimezone.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.keithsmyth.visualtimezone.R;
import com.keithsmyth.visualtimezone.adapter.CompareTimeAdapter;
import com.keithsmyth.visualtimezone.controller.CompareTimeController;

import java.util.ArrayList;

public class CompareItemFragment extends Fragment {

    private static final String ARG_TIME_ZONES = "timeZones";

    private ArrayList<String> mTimeZones;
    private ListView mListView;
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

        // list view
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mAdapter.addHeaders(mListView);

        return view;
    }

    private void generateListAdapter() {
        CompareTimeController controller = new CompareTimeController(mTimeZones);
        mAdapter = new CompareTimeAdapter(controller);
    }
}
