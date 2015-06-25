package com.comvigo.imlockandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comvigo.imlockandroid.Models.SettingItem;

import java.util.List;

/**
 * Created by Dmitry on 15.06.2015.
 */
public class SettingsAdapter extends BaseAdapter {

    Context context;
    List<SettingItem> itemList;
    LayoutInflater lInflater;

    public SettingsAdapter(Context context, List<SettingItem> itemList){
        this.context = context;
        this.itemList = itemList;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.settings_item_layout, parent, false);
        }
        TextView item = (TextView) view.findViewById(R.id.item);
        item.setText(itemList.get(position).getSettingName());
        return view;
    }


}
