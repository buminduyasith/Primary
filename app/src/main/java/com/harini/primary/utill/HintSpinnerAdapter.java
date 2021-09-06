package com.harini.primary.utill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.harini.primary.R;

import java.util.List;

public class HintSpinnerAdapter extends ArrayAdapter<String> {

    private final List<String> list;
    private final Context context;
    private final int viewResourceId;

    public HintSpinnerAdapter(Context context, int viewResourceId, List<String> list) {
        super(context, viewResourceId, list);
        this.context = context;
        this.viewResourceId = viewResourceId;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(viewResourceId, parent, false);
        }

        TextView text = convertView.findViewById(R.id.text);
        text.setText(list.get(position));

        if (list.size() - 1 == position) {
            text.setTextColor(ContextCompat.getColor(context, R.color.hint_color));
        } else {
            text.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        return convertView;
    }

    @Override
    public int getCount() {
        int count = list.size();
        return count > 0 ? count - 1 : count;// returns size-1 to hide the hint, which is added to the end of the list, from drop down list,
    }
}
