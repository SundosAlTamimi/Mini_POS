package com.falconssoft.minipos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.falconssoft.minipos.Modle.Categories;
import com.falconssoft.minipos.Modle.Items;
import com.falconssoft.minipos.R;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private static List<Items> itemsList;
    private int lastPosition = -1;

    public ItemListAdapter(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public ItemListAdapter() {

    }

    public void setItemsList(List<Items> itemsList) {
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
        TextView itemName , price;
        ImageView itemPic;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.item_row, null);


        if ( i%3 == 0) {
            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .8f, ScaleAnimation.RELATIVE_TO_SELF, .8f);
//            scale.setStartOffset(900);
            scale.setDuration(400);
            scale.setInterpolator(new OvershootInterpolator());
            view.startAnimation(scale);

        } else if ( i%2 == 0) {
            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .5f, ScaleAnimation.RELATIVE_TO_SELF, .8f);
//            scale.setStartOffset(900);
            scale.setDuration(700);
            scale.setInterpolator(new OvershootInterpolator());
            view.startAnimation(scale);

        } else {
            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .2f, ScaleAnimation.RELATIVE_TO_SELF, .8f);
//            scale.setStartOffset(900);
            scale.setDuration(200);
            scale.setInterpolator(new OvershootInterpolator());
            view.startAnimation(scale);
        }






        holder.itemName = (TextView) view.findViewById(R.id.itemName);
        holder.price = (TextView) view.findViewById(R.id.price);
        holder.itemPic = (ImageView) view.findViewById(R.id.item_pic);


        holder.itemName.setText(itemsList.get(i).getItemName());
//        holder.itemPic.setImageDrawable(context.getResources().getDrawable(itemsList.get(i).getPic()));
        holder.price.setText(""+itemsList.get(i).getPrice());

        return view;
    }

}
