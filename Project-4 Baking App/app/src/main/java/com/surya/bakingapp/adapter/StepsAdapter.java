package com.surya.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.surya.bakingapp.R;
import com.surya.bakingapp.activity.StepActivity;
import com.surya.bakingapp.activity.StepFragment;
import com.surya.bakingapp.models.StepsModel;
import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private Context context;
    private ArrayList<StepsModel> stepsList;
    private boolean isTab;

    public StepsAdapter(Context context, ArrayList<StepsModel> stepsList, boolean isTab) {
        this.context = context;
        this.stepsList = stepsList;
        this.isTab =isTab;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.steps_view,viewGroup,false);
        return new StepsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder stepsViewHolder, int i) {
        String text= stepsList.get(i).getShortDesc();
        stepsViewHolder.stepsTextview.setText(text);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {
        TextView stepsTextview;
        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            stepsTextview =itemView.findViewById(R.id.steps_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(isTab) {
                        Bundle bundle = new Bundle();
                        bundle.putString(StepFragment.ARG_POS, String.valueOf(position));
                        bundle.putString("description", stepsList.get(position).getDesc());
                        bundle.putString("videourl", stepsList.get(position).getVideoLink());
                        bundle.putString("shortdec", stepsList.get(position).getShortDesc());
                        bundle.putString("thumbnnail", stepsList.get(position).getThumbnailLink());
                        StepFragment fragment = new StepFragment();
                        fragment.setArguments(bundle);
                        ((AppCompatActivity)context).getSupportFragmentManager().
                                beginTransaction().replace(R.id.item_detail_container, fragment).commit();
                    }else {
                        Intent intent = new Intent(context, StepActivity.class);
                        intent.putExtra(StepFragment.ARG_POS, position);
                        intent.putExtra("description", stepsList.get(position).getDesc());
                        intent.putExtra("videourl", stepsList.get(position).getVideoLink());
                        intent.putExtra("shortdec", stepsList.get(position).getShortDesc());
                        intent.putExtra("thumbnnail", stepsList.get(position).getThumbnailLink());
                        intent.putExtra("size", stepsList.size());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

