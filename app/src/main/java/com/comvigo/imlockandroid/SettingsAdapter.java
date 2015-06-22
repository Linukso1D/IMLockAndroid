package com.comvigo.imlockandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comvigo.imlockandroid.Models.SettingItem;

import java.util.List;

/**
 * Created by Dmitry on 15.06.2015.
 */
public class SettingsAdapter extends ArrayAdapter<SettingItem> {

    Context context;
    List<SettingItem> itemList;

    public SettingsAdapter(Context context, int resource, List<SettingItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.itemList = objects;
    }

    @Override
    public SettingItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.settings_item_layout, parent, false);
        TextView item = (TextView) view.findViewById(R.id.item);
        item.setText(itemList.get(position).getSettingName());
        return super.getView(position, convertView, parent);
    }
}
