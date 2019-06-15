package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    static String image,desc,origin,name;
    static ArrayList<String> alsoKnownAs, ingr;

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject jsonName = jsonObj.getJSONObject("name");
            name = jsonName.getString("mainName");
            JSONArray also = jsonName.getJSONArray("alsoKnownAs");
            alsoKnownAs = new ArrayList<>();
            for (int i = 0; i < also.length(); i++) {
                alsoKnownAs.add(also.getString(i));
            }
            origin = jsonObj.getString("placeOfOrigin");
            desc = jsonObj.getString("description");
            image = jsonObj.getString("image");
            JSONArray ing = jsonObj.getJSONArray("ingredients");
            ingr = new ArrayList<>();
            for (int i = 0; i < ing.length(); i++) {
                ingr.add(ing.getString(i));
            }
            return new Sandwich(name, alsoKnownAs, origin, desc, image, ingr);
        }catch (JSONException e){
            return null;
        }
    }
}
