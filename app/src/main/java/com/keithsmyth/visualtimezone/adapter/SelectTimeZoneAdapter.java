package com.keithsmyth.visualtimezone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author keithsmyth
 */
public class SelectTimeZoneAdapter extends ArrayAdapter<String> {

    private final Set<String> mSelectedTimeZones;

    public SelectTimeZoneAdapter(Context context, int resource, int textViewResourceId,
                                 String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mSelectedTimeZones = new HashSet<String>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView view = (CheckedTextView) super.getView(position, convertView, parent);
        view.setChecked(isSelected(position));
        return view;
    }

    public void toggleTimeZone(int position) {
        if (isSelected(position)) {
            deSelectTimeZone(position);
        }
        else {
            selectTimeZone(position);
        }
    }

    private void selectTimeZone(int position) {
        mSelectedTimeZones.add(getItem(position));
    }

    private void deSelectTimeZone(int position) {
        mSelectedTimeZones.remove(getItem(position));
    }

    public int getSelectedTimeZoneCount() {
        return mSelectedTimeZones.size();
    }

    public ArrayList<String> getSelectedTimeZones() {
        return new ArrayList<String>(mSelectedTimeZones);
    }

    public void clearSelectedTimeZones() {
        mSelectedTimeZones.clear();
        notifyDataSetInvalidated();
    }

    private boolean isSelected(int position) {
        String timeZone = getItem(position);
        return mSelectedTimeZones.contains(timeZone);
    }
}
