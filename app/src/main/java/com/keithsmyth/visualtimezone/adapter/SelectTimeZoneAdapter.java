package com.keithsmyth.visualtimezone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author keithsmyth
 */
public class SelectTimeZoneAdapter extends ArrayAdapter<String> {

    private final Set<String> mSelectedTimeZones;

    private List<String> mObjects;
    private List<String> mOriginalValues;
    private final Object mLock = new Object();
    private Filter mFilter;

    public SelectTimeZoneAdapter(Context context, int resource, int textViewResourceId,
                                 String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mSelectedTimeZones = new HashSet<String>();
        mObjects = new ArrayList<String>(Arrays.asList(objects));
    }

    @Override
    public String getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getCount() {
        return mObjects.size();
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

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<String>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list;
                synchronized (mLock) {
                    list = new ArrayList<String>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<String> values;
                synchronized (mLock) {
                    values = new ArrayList<String>(mOriginalValues);
                }

                final ArrayList<String> newValues = new ArrayList<String>();

                for (final String value : values) {
                    final String valueText = value.toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.contains(prefixString)) {
                        newValues.add(value);
                    }
                    else {
                        final String[] words = valueText.split(" ");

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.contains(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
