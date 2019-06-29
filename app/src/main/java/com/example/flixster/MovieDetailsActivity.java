package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;
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
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.flixster.MainActivity.API_BASE_URL;
import static com.example.flixster.MainActivity.API_KEY_PARAM;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    Movie movie;

    /*
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.tvVoteCount) TextView tvVoteCount;
    */
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView tvVoteCount;
    ImageView imageView;
    YouTubePlayerView playerView;

    AsyncHttpClient client;

    String videoId;
    String youtubeId;

    String backdropUrl;

    TextView tvRuntime;

    Integer pageNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);



        Intent intent = getIntent();
        backdropUrl = intent.getStringExtra("backdropUrl");

        //ButterKnife.bind(this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvVoteCount = (TextView) findViewById(R.id.tvVoteCount);
        tvRuntime = (TextView) findViewById(R.id.tvRuntime);


        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        int voteCount = movie.getVoteCount().intValue();
        tvVoteCount.setText(Integer.toString(voteCount) + " votes");

        client = new AsyncHttpClient();
//        Intent intent = getIntent();
//        videoId = intent.getStringExtra("id");




        videoId = Integer.toString(movie.getId());

        getRuntime();


//        imageView = findViewById(R.id.ivDetailPoster);
//
//        Glide.with(this)
//                .load(backdropUrl)
//                .bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
//                .placeholder(R.drawable.flicks_backdrop_placeholder)
//                .error(R.drawable.flicks_backdrop_placeholder)
//                .into(imageView);

    }

    private void getRuntime() {
        String url = API_BASE_URL + "/movie/" + videoId;
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {

                    Integer runtime = response.getInt("runtime");
                    String runtimeText = Integer.toString(runtime) + " minutes";
                    tvRuntime.setText(runtimeText);

                    Log.d("MovieDetailsActivity", "Received runtime results from API");
                } catch (JSONException except) {

                    Log.d("MovieDetailsActivity", "Failed to parse movie videos");
                    finish();
                }
            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//            }
        });
        getVideo();
    }


    private void loadVideo() {

        playerView = (YouTubePlayerView) findViewById(R.id.player);
        playerView.setVisibility(View.VISIBLE);
//        imageView.setVisibility(View.GONE);

        Log.d("MovieDetailsActivity", "About to initialize player view");
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (youtubeId != null) {
                    Log.d("MovieDetailsActivity", youtubeId);
                    youTubePlayer.cueVideo(youtubeId);
                } else {
                    Log.e("MovieDetailsActivity", "Error loading Youtube video");
                    playerView.setVisibility(View.GONE);
                    imageView = findViewById(R.id.ivDetailPoster);

                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(MovieDetailsActivity.this)
                            .load(backdropUrl)
                            .bitmapTransform(new RoundedCornersTransformation(MovieDetailsActivity.this, 25, 0))
                            .placeholder(R.drawable.flicks_backdrop_placeholder)
                            .error(R.drawable.flicks_backdrop_placeholder)
                            .into(imageView);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("MovieDetailsActivity", "Error initializing YouTube player");
                playerView.setVisibility(View.GONE);
                imageView = findViewById(R.id.ivDetailPoster);

                imageView.setVisibility(View.VISIBLE);
                Glide.with(MovieDetailsActivity.this)
                .load(backdropUrl)
                .bitmapTransform(new RoundedCornersTransformation(MovieDetailsActivity.this, 25, 0))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(imageView);

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
                    Log.d("MovieDetailsActivity", "Received results from API");
                    youtubeId = results.getJSONObject(0).getString("key");
                    loadVideo();
                } catch (JSONException except) {

                    Log.d("MovieDetailsActivity", "Failed to parse movie videos");
                    finish();
                }
            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//            }
        });

    }
}
