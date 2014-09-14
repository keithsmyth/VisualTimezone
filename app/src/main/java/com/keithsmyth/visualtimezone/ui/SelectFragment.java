package com.keithsmyth.visualtimezone.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

    private SelectTimeZoneAdapter mSelectTimeZoneAdapter;
    private Button mNextButton;
    private Button mClearButton;
    private ICanStartCompare mCanStartCompare;

    public SelectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                // refresh views
                mSelectTimeZoneAdapter.getView(position, view, timeZoneList);
                updateButtonViews();
            }
        });

        // next button
        mNextButton = (Button) rootView.findViewById(R.id.btn_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeKeyboard(getActivity(), v.getWindowToken());
                next();
            }
        });

        mClearButton = (Button) rootView.findViewById(R.id.btn_clear);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectTimeZoneAdapter.clearSelectedTimeZones();
                mSelectTimeZoneAdapter.notifyDataSetChanged();
                updateButtonViews();
            }
        });

        return rootView;
    }

    private SearchView mSearchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
        // Store reference to search view
        mSearchView = (SearchView) menu.findItem(R.id.action_filter).getActionView();
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    mSelectTimeZoneAdapter.getFilter().filter(s);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mSearchView != null && item.getItemId() == R.id.action_filter) {
            mSearchView.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        mCanStartCompare = (ICanStartCompare) activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonViews();
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
