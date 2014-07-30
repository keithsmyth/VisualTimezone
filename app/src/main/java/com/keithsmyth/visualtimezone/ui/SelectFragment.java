package com.keithsmyth.visualtimezone.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.keithsmyth.visualtimezone.R;
import com.keithsmyth.visualtimezone.Utils;

import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by keithsmyth on 26/07/2014.
 */
public class SelectFragment extends Fragment {

    public static final String ARG_TIME_ZONES = "timeZones";
    private final ArrayList<String> mSelectedTimeZones;
    private ArrayAdapter<String> mTimeZoneAdapter;
    private Button mNextButton;
    private Button mClearButton;
    private ICanStartCompare mCanStartCompare;

    public SelectFragment() {
        mSelectedTimeZones = new ArrayList<String>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select, container, false);

        // list
        final ListView timeZoneList = (ListView) rootView.findViewById(R.id.lst_timezones);
        timeZoneList.setAdapter(mTimeZoneAdapter);
        timeZoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // add clicked item
                addTimeZone(timeZoneList.getItemAtPosition(position).toString());
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
                clear();
            }
        });

        // restore state
        if (savedInstanceState != null) {
            mSelectedTimeZones.clear();
            mSelectedTimeZones.addAll(savedInstanceState.getStringArrayList(ARG_TIME_ZONES));
            updateButtonViews();
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
        outState.putStringArrayList(ARG_TIME_ZONES, mSelectedTimeZones);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonViews();
    }

    private void generateListAdapter() {
        Set<String> timeZoneSet = DateTimeZone.getAvailableIDs();
        String[] timeZoneArray = timeZoneSet.toArray(new String[timeZoneSet.size()]);
        mTimeZoneAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_selectable_list_item,
                timeZoneArray);
    }

    private void onFilterTextUpdated(EditText editText) {
        if (editText == null || mTimeZoneAdapter == null) {
            return;
        }
        // perform filtering on list
        String filter = editText.getText().toString();
        mTimeZoneAdapter.getFilter().filter(filter);
        // TODO: filter with contains instead of default startsWith
    }

    private void addTimeZone(String timeZone) {
        if (TextUtils.isEmpty(timeZone)) {
            return;
        }
        mSelectedTimeZones.add(timeZone);
        updateButtonViews();
    }

    private void updateButtonViews() {
        mNextButton.setText(String.valueOf(mSelectedTimeZones.size()));
        final int visibility = mSelectedTimeZones.size() == 0 ? View.INVISIBLE : View.VISIBLE;
        mNextButton.setVisibility(visibility);
        mClearButton.setVisibility(visibility);
    }

    private void next() {
        mCanStartCompare.startCompare(mSelectedTimeZones);
    }

    private void clear() {
        mSelectedTimeZones.clear();
        updateButtonViews();
    }
}
