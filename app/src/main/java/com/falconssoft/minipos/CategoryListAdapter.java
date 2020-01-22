package com.falconssoft.minipos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.falconssoft.minipos.Modle.Categories;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private static List<Categories> itemsList;

    public CategoryListAdapter(Context context, List<Categories> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public CategoryListAdapter() {

    }

    public void setItemsList(List<Categories> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView catName;
        ImageView catPic;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.cat_row, null);

        holder.catName = (TextView) view.findViewById(R.id.catName);
        holder.catPic = (ImageView) view.findViewById(R.id.image);


        holder.catName.setText(itemsList.get(i).getCatName());
//        holder.catPic.setImageDrawable(context.getResources().getDrawable(itemsList.get(i).getPic()));

        return view;
    }

}
