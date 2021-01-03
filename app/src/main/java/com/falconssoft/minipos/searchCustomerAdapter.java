package com.falconssoft.minipos;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.minipos.Modle.Items;


import java.util.ArrayList;
import java.util.List;

import static com.falconssoft.minipos.ItemCard.isShow;
import static com.falconssoft.minipos.ItemCard.recyclerk;


public class searchCustomerAdapter extends  RecyclerView.Adapter<searchCustomerAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    ItemCard context;
    List<Items> companey;
    List<Items> companeyInfos=new ArrayList<>();
     int row_index=-1;

     EditText editText;
Items items=new Items() ;
     DatabaseHandler databaseHandler;


    public searchCustomerAdapter(ItemCard context, List<Items> companeyInfo) {
        this.context = context;
        this.companey = companeyInfo;
        databaseHandler=new DatabaseHandler(context);
//        this.editText=editText;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_row, viewGroup, false);
        return new searchCustomerAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

//        viewHolder.cancelButton.setVisibility(View.GONE);
            viewHolder.itemName.setText(companey.get(i).getItemName());
            viewHolder.ItemCategory.setText(companey.get(i).getItemNameE());
            viewHolder.itemPrice.setText(""+companey.get(i).getPrice());
            viewHolder.linearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   isShow=1;
                    recyclerk.setVisibility(View.GONE);
                    context.fillEditeText(companey.get(i));

                }

            });
            if(row_index==i)
            {
                viewHolder.linearItem.setBackgroundColor(Color.parseColor("#e5e4e2"));

            }
            else {
                viewHolder.linearItem.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }


    }


    @Override
    public int getItemCount() {
        return companey.size();
    }
    public List<Items> getCheckedItems() {
        return companeyInfos;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView green, itemName,itemPrice,ItemCategory;
        LinearLayout linearItem;

        public ViewHolder(View itemView) {
            super(itemView);

            itemPrice=itemView.findViewById(R.id.itemPriceSearch);
            ItemCategory=itemView.findViewById(R.id.itemCategorySearch);
            itemName=itemView.findViewById(R.id.itemNameSearch);
            linearItem=itemView.findViewById(R.id.linearItem);


        }
    }



}