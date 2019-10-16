package com.piyush.pictattendance.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyush.pictattendance.R;
import com.piyush.pictattendance.model.MenuItem;
import com.piyush.pictattendance.utils.Utils;

import java.util.List;

public class NavMenuAdapter extends ArrayAdapter<MenuItem>{

    public NavMenuAdapter(@NonNull Context context, List<MenuItem> items) {
        super(context, R.layout.menu_item, items);
        this.items = items;
    }

    private List<MenuItem> items;


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(parent.getContext());

            convertView = vi.inflate(R.layout.menu_item, parent, false);
        }
        MenuItem item = items.get(position);
        ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(parent.getContext().getDrawable(item.getIconId()));
        ((TextView) convertView.findViewById(R.id.name)).setText(item.getName());
        return convertView;
    }


    @Override
    public int getCount() {
        return items.size();
    }

}
