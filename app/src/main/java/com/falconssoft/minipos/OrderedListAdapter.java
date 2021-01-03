package com.falconssoft.minipos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.falconssoft.minipos.Modle.Items;

import java.util.List;

public class OrderedListAdapter extends BaseAdapter {

    private Context context;
    private static List<Items> itemsList;
    private MainActivity obj;
    GlobelFunction globelFunction;

    public OrderedListAdapter(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
        obj = new MainActivity();
        globelFunction=new GlobelFunction();
    }

    public OrderedListAdapter() {

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
        TextView itemNo, itemName, qty,  price, net;
        Button plus, minus;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.ordered_row, null);

        holder.itemNo = (TextView) view.findViewById(R.id.item_no);
        holder.itemName = (TextView) view.findViewById(R.id.item_name);
        holder.qty = (TextView) view.findViewById(R.id.qty);
        holder.price = (TextView) view.findViewById(R.id.price);
        holder.net = (TextView) view.findViewById(R.id.net);

        holder.plus = (Button) view.findViewById(R.id.plus);
        holder.minus = (Button) view.findViewById(R.id.minus);


        String total=globelFunction.DecimalFormat(""+itemsList.get(i).getNet());
        holder.itemNo.setText(itemsList.get(i).getItemNo());
        holder.itemName.setText(itemsList.get(i).getItemName());
        holder.qty.setText("" + itemsList.get(i).getQty());
        holder.price.setText(""+itemsList.get(i).getPrice());
        holder.net.setText(total);

//        DatabaseHandler handler = new DatabaseHandler(context);
//        if(handler.getSettings().getControlQty() == 0){
//            holder.plus.setVisibility(View.INVISIBLE);
//            holder.minus.setVisibility(View.INVISIBLE);
//        }


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String total=globelFunction.DecimalFormat(""+(Double.parseDouble(holder.qty.getText().toString()) + 1) * itemsList.get(i).getPrice());

                obj.orderedItems.get(i).setQty((Double.parseDouble(holder.qty.getText().toString()) + 1));
                obj.orderedItems.get(i).setNet(Double.parseDouble(total));
                holder.qty.setText(""+ (Double.parseDouble(holder.qty.getText().toString()) + 1));
                holder.net.setText(""+ (Double.parseDouble(total)));
                obj.reCalculate(context);

            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(holder.qty.getText().toString()) > 1) {
                    String total=globelFunction.DecimalFormat(""+(Double.parseDouble(holder.qty.getText().toString()) - 1) * itemsList.get(i).getPrice());

                    obj.orderedItems.get(i).setQty((Double.parseDouble(holder.qty.getText().toString()) - 1));
                    obj.orderedItems.get(i).setNet(Double.parseDouble(total));
                    holder.qty.setText("" + (Double.parseDouble(holder.qty.getText().toString()) - 1));
                    holder.net.setText(total);
                    obj.reCalculate(context);
                }
            }
        });
        return view;
    }

}
