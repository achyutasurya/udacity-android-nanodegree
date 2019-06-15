package com.surya.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.surya.bakingapp.R;
import com.surya.bakingapp.activity.ItemsActivity;
import com.surya.bakingapp.models.ItemModel;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<ItemModel> itemsList;


    public ItemAdapter(Context context, ArrayList<ItemModel> itemsList) {
        this.context = context;
        this.itemsList = itemsList;

    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item_cardview, viewGroup, false);
        return new MyViewHolder(v);
    }

    private int x = 0;

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.itemTextview.setText(itemsList.get(i).getName());
        x++;
        int y = x % 4;
        int[] z = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four};
        myViewHolder.itemImageview.setImageResource(z[y]);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextview;
        ImageView itemImageview;


        public void clicking() {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, ItemsActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("name", itemTextview.getText().toString());
            context.startActivity(intent);
        }

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemTextview = itemView.findViewById(R.id.item_name);
            itemImageview = itemView.findViewById(R.id.item_imageview);
            itemImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicking();
                }
            });
            itemTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicking();
                }
            });
        }
    }
}
