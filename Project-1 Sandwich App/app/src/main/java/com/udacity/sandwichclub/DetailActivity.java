package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    ImageView imgV;
    TextView nameTV, ingTV, desTV, alsoTV, placeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgV = findViewById(R.id.img);
        nameTV = findViewById(R.id.name);
        ingTV = findViewById(R.id.ing);
        desTV = findViewById(R.id.desc);
        alsoTV = findViewById(R.id.also_known_as);
        placeTV = findViewById(R.id.place_of_origin);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        if(json!=null){
            sandwich = JsonUtils.parseSandwichJson(json);
        }
        if (sandwich == null) {
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .error(R.drawable.imagenotfound)
                .into(imgV);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        List<String> also, ing;
        String temp = "";
        nameTV.setText(sandwich.getMainName());
        also = sandwich.getAlsoKnownAs();
        for (int i = 0; i < also.size(); i++) {
            temp = temp + also.get(i) + ",";
        }
        if (temp == "") {
            alsoTV.setTextColor(Color.parseColor("#FC0B27"));
            alsoTV.setText("!!!Name is not found!!!");
        } else {
            alsoTV.setText(temp);
        }
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            placeTV.setTextColor(Color.parseColor("#FC0B27"));
            placeTV.setText("!!!No Place of Origin!!!");
        } else {
            placeTV.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getDescription().isEmpty()) {
            desTV.setTextColor(Color.parseColor("#FC0B27"));
            desTV.setText("!!!No description found!!!");
        } else {
            desTV.setText(sandwich.getDescription());
        }

        temp = "";
        ing = sandwich.getIngredients();
        for (int i = 0; i < ing.size(); i++) {
            temp = temp + "->" + ing.get(i) + "\n";
        }
        if (temp == "") {
            ingTV.setTextColor(Color.parseColor("#FC0B27"));
            ingTV.setText("!!!No Ingredients found!!!");
        } else {
            ingTV.setText(temp);
        }
    }
}
