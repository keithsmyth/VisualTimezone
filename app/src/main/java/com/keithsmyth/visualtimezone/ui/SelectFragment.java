package com.keithsmyth.visualtimezone.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.keithsmyth.visualtimezone.R;
import com.keithsmyth.visualtimezone.Utils;
import com.keithsmyth.visualtimezone.adapter.SelectTimeZoneAdapter;

import org.joda.time.DateTimeZone;

import java.util.Set;

/**
 * Created by keithsmyth on 26/07/2014.
 */
public class SelectFragment extends Fragment {

    public static final String ARG_TIME_ZONES = "timeZones";
    private SelectTimeZoneAdapter mSelectTimeZoneAdapter;
    private Button mNextButton;
    private Button mClearButton;
    private ICanStartCompare mCanStartCompare;

    public SelectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateListAdapter();
    }

    private void generateListAdapter() {
        Set<String> timeZoneSet = DateTimeZone.getAvailableIDs();
        String[] timeZoneArray = timeZoneSet.toArray(new String[timeZoneSet.size()]);
        mSelectTimeZoneAdapter =
                new SelectTimeZoneAdapter(getActivity(), R.layout.item_timezone_row,
                        R.id.txt_timezone_name, timeZoneArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select, container, false);

        // list
        final ListView timeZoneList = (ListView) rootView.findViewById(R.id.lst_timezones);
        timeZoneList.setAdapter(mSelectTimeZoneAdapter);
        timeZoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // add clicked item
                mSelectTimeZoneAdapter.toggleTimeZone(position);
                updateButtonViews();
            }
        });

        // filter
        final EditText filterText = (EditText) rootView.findViewById(R.id.txt_filter);
        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onFilterTextUpdated(filterText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // next button
        mNextButton = (Button) rootView.findViewById(R.id.btn_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeKeyboard(getActivity(), filterText.getWindowToken());
                next();
            }
        });

        mClearButton = (Button) rootView.findViewById(R.id.btn_clear);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectTimeZoneAdapter.clearSelectedTimeZones();
                updateButtonViews();
            }
        });

        // restore state
        if (savedInstanceState != null) {
            //mTimeZoneAdapter.restoreTimeZones(savedInstanceState.getStringArrayList
            // (ARG_TIME_ZONES));
            //updateButtonViews();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        mCanStartCompare = (ICanStartCompare) activity;
        super.onAttach(activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putStringArrayList(ARG_TIME_ZONES, mTimeZoneAdapter.getSelectedTimeZones());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonViews();
    }

    private void onFilterTextUpdated(EditText editText) {
        if (editText == null || mSelectTimeZoneAdapter == null) {
            return;
        }
        // perform filtering on list
        String filter = editText.getText().toString();
        mSelectTimeZoneAdapter.getFilter().filter(filter);
        // TODO: filter with contains instead of default startsWith
    }

    private void updateButtonViews() {
        final int selectedTimeZoneCount = mSelectTimeZoneAdapter.getSelectedTimeZoneCount();
        mNextButton.setText(String.valueOf(selectedTimeZoneCount));
        final int visibility = selectedTimeZoneCount == 0 ? View.INVISIBLE : View.VISIBLE;
        mNextButton.setVisibility(visibility);
        mClearButton.setVisibility(visibility);
    }

    private void next() {
        mCanStartCompare.startCompare(mSelectTimeZoneAdapter.getSelectedTimeZones());
    }
}
