package com.keithsmyth.visualtimezone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.keithsmyth.visualtimezone.R;
import com.keithsmyth.visualtimezone.controller.CompareTimeController;

import java.util.List;

/**
 * Created by keithsmyth on 31/07/2014.
 */
public class CompareTimeAdapter extends BaseAdapter {

    private final CompareTimeController mController;

    public CompareTimeAdapter(CompareTimeController controller) {
        mController = controller;
    }

    @Override
    public int getCount() {
        return mController.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get view
        Context context = parent.getContext();
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_compare_time_row, parent, false);
        }

        // bind view
        List<String> values = mController.getItem(position);
        bindView(view, values);

        return view;
    }

    /**
     * Binds a standard view for comparison to the data
     *
     * @param view
     * @param values
     */
    private void bindView(View view, List<String> values) {
        LinearLayout container = (LinearLayout) view.findViewById(R.id.layout_compare_time);
        container.removeAllViews();

        for (String value : values) {
            LayoutInflater inflater = LayoutInflater.from(view.getContext());
            TextView textView =
                    (TextView) inflater.inflate(R.layout.item_compare_time_value, container, false);
            textView.setText(value);
            container
                    .addView(textView, new LinearLayout.LayoutParams(0,
                            LayoutParams.MATCH_PARENT, 1));
        }
    }
}
