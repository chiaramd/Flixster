package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

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

    AsyncHttpClient client;


    //Config config;

    //public void setConfig(Config config) {this.config = config;}


    String backdropUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

       // config = new Config(movie);

//Intent intent = getIntent();
//
//        videoId = intent.getStringExtra("id");
//
        Intent intent = getIntent();

        //ButterKnife.bind(this);
        backdropUrl = intent.getStringExtra("backdropUrl");

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvVoteCount = (TextView) findViewById(R.id.tvVoteCount);


        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        int voteCount = movie.getVoteCount().intValue();
        tvVoteCount.setText(Integer.toString(voteCount) + " votes");



        ImageView imageView = findViewById(R.id.ivDetailPoster);

        Glide.with(this)
                .load(backdropUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(imageView);


    }





    public void onClick(View v) {


        //getVideoId();





        String videoId = Integer.toString(movie.getId());

        //video id as a string extra
        Intent intent = new Intent(this, MovieTrailerActivity.class);
        intent.putExtra("id", videoId);
        this.startActivity(intent);
    }
    /*
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        //delete @NonNull????
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //lookup view objects by id
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }
        }
    }*/

}
