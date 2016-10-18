package tugce.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import tugce.movieapp.helpers.DeviceDimensionsHelper;
import tugce.movieapp.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie;
    @BindView (R.id.ivBackdropImage)ImageView ivBackdropImage;
    @BindView (R.id.ivMoviePoster)ImageView ivPoster;
    //@BindView (R.id.tvMainMovieTitle)TextView tvMainMovieTitle;
    @BindView (R.id.tvMovieTitle)TextView tvMovieTitle;
    @BindView (R.id.tvScore)TextView tvScore;
    @BindView (R.id.tvOverview)TextView tvOverview;
    @BindView (R.id.tvReleaseDate)TextView tvReleaseDate;
    @BindView (R.id.rbPopularity)RatingBar rbPopularity;
    @BindView (R.id.tvLanguage )TextView tvLanguage;
    @BindView (R.id.btnPlay )ImageButton btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
       // toolbar.setTitle(movie.getOriginalTitle());

        Intent intent=getIntent();
        movie = (Movie) intent.getSerializableExtra("Movie");

        Picasso.with(ivBackdropImage.getContext()).load(movie.getBackdropPath())
                .resize(DeviceDimensionsHelper.getDisplayWidth(getBaseContext()), 0)
                .placeholder(R.mipmap.popcorn).into(ivBackdropImage);
        Picasso.with(this).load(movie.getPosterPath()).placeholder(R.mipmap.popcorn).into(ivPoster);
        //tvMainMovieTitle.setText(movie.getOriginalTitle());
        tvMovieTitle.setText(movie.getOriginalTitle());
        tvScore.setText("Score: "+movie.getVoteAverage()+"/10");
        tvOverview.setText(movie.getOverview());
        tvReleaseDate.setText(parseDateToMMddyyyy(movie.getReleaseDate()));
        tvLanguage.setText(getLanguage(movie.getOriginalLanguage()));
        rbPopularity.setRating((float) (movie.getVoteAverage() / 2));

        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MovieDetailActivity.this,TrailerActivity.class).putExtra("Movie", movie);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String parseDateToMMddyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MM/dd/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getLanguage(String language){
        if(language.equals("en")){
            return "English";
        }
        return "";
    }
}
