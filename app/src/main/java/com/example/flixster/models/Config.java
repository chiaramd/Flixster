package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    String imageBaseUrl;
    String posterSize;
    String backdropSize;

    public Config(JSONObject object) throws JSONException {

        JSONObject images = object.getJSONObject("images");

        //get the image base url
        imageBaseUrl = images.getString("secure_base_url");

        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //uses option at index 3 or w342 as fallback

        posterSize = posterSizeOptions.optString(3, "w342");


        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");

        backdropSize = backdropSizeOptions.optString(1, "w780");

    }
    // helper method for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    //getters
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
