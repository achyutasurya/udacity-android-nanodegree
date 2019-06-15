package com.surya.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.surya.bakingapp.R;
import com.surya.bakingapp.models.IngredientsModel;
import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private Context context;
    private ArrayList<IngredientsModel> ingredients;
    private int x = 1;

    public IngredientsAdapter(Context context, ArrayList<IngredientsModel> ingredients) {
        this.context = context;
        this.ingredients = ingredients;

    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.ingredient_view, viewGroup, false);
        return new IngredientsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int i) {
        String ing, serialNo;
        ing = ingredients.get(i).getQuantity() + " " + ingredients.get(i).getMesure() + " of " + ingredients.get(i).getName();
        ingredientsViewHolder.ingredientTextview.setText(ing);
        serialNo = x + "). ";
        ingredientsViewHolder.seriesCountTextview.setText(serialNo);
        x++;


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTextview, seriesCountTextview;

        IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTextview = itemView.findViewById(R.id.ingredients_name);
            seriesCountTextview = itemView.findViewById(R.id.ingredients_serial_number);
        }
    }
}