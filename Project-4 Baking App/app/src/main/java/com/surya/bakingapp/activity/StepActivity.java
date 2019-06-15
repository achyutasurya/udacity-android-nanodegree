package com.surya.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.surya.bakingapp.R;

public class StepActivity extends AppCompatActivity {

    Context context;
    Button nextButton, previousButton;
    int position;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();
        final String shortDes = intent.getStringExtra("shortdec");
        setTitle(shortDes);
        context = this;
        nextButton = findViewById(R.id.frag_next);
        previousButton = findViewById(R.id.frag_previous);

        final String des = intent.getStringExtra("description");
        final String videoLink = intent.getStringExtra("videourl");
        final String thumbnailLink = intent.getStringExtra("thumbnnail");
        final int size = intent.getIntExtra("size", -1);

        position = intent.getIntExtra(StepFragment.ARG_POS, -1);

        if (position == 0) {
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        } else if (position == size - 1) {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putString(StepFragment.ARG_POS, String.valueOf(position));
            bundle.putString("description", des);
            bundle.putString("videourl", videoLink);
            bundle.putString("shortdec", shortDes);
            bundle.putString("thumbnnail", thumbnailLink);
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, stepFragment).commit();
        }


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position + 1;
                if (position >= size - 1) {
                    nextButton.setVisibility(View.INVISIBLE);
                    position--;
                    Toast.makeText(StepActivity.this, "Preperation Completed", Toast.LENGTH_SHORT).show();
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                    previousButton.setVisibility(View.VISIBLE);
                    setTitle(ItemsActivity.stepsList.get(position).getShortDesc());
                    Bundle bundle = new Bundle();
                    bundle.putString(StepFragment.ARG_POS, String.valueOf(position));
                    bundle.putString("description", ItemsActivity.stepsList.get(position).getDesc());
                    bundle.putString("videourl", ItemsActivity.stepsList.get(position).getVideoLink());
                    bundle.putString("shortdec", ItemsActivity.stepsList.get(position).getShortDesc());
                    bundle.putString("thumbnnail", ItemsActivity.stepsList.get(position).getThumbnailLink());
                    StepFragment stepFragment = new StepFragment();
                    stepFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, stepFragment).commit();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position - 1;
                if (position < 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                    position++;
                } else {
                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                    setTitle(ItemsActivity.stepsList.get(position).getShortDesc());
                    Bundle bundle = new Bundle();
                    bundle.putString(StepFragment.ARG_POS, String.valueOf(position));
                    bundle.putString("description", ItemsActivity.stepsList.get(position).getDesc());
                    bundle.putString("videourl", ItemsActivity.stepsList.get(position).getVideoLink());
                    bundle.putString("shortdec", ItemsActivity.stepsList.get(position).getShortDesc());
                    bundle.putString("thumbnnail", ItemsActivity.stepsList.get(position).getThumbnailLink());
                    StepFragment stepFragment = new StepFragment();
                    stepFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, stepFragment).commit();
                }
            }
        });


    }
}
