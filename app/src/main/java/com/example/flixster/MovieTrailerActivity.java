package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.flixster.MainActivity.API_BASE_URL;
import static com.example.flixster.MainActivity.API_KEY_PARAM;

public class MovieTrailerActivity extends YouTubeBaseActivity {


    String videoId = "cjdkljfdls";
    String youtubeId = "SUXWAEX2jlg";

    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        client = new AsyncHttpClient();

        //videoId = getIntent().getStringExtra();

        Intent intent = getIntent();

        videoId = intent.getStringExtra("id");


        getVideo();


    }

    private void loadVideo() {
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);
        Log.d("MovieTrailerActivity", "About to initialize player view");
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("MovieTrailerActivity", youtubeId);
                youTubePlayer.cueVideo(youtubeId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }


    private void getVideo() {
        String url = API_BASE_URL + "/movie/" + videoId + "/videos";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray results = response.getJSONArray("results");
                    Log.d("MovieTrailerActivity", "Received results from API");
                    youtubeId = results.getJSONObject(0).getString("key");
                    loadVideo();
                } catch (JSONException except) {

                    Log.d("MovieTrailerActivity", "Failed to parse movie videos");
                    finish();
                }
            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//                Log.e("MovieTrailerActivity", "Failed getting video");
//            }
        });


    }

}
