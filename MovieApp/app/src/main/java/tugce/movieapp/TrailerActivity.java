package tugce.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import tugce.movieapp.models.Movie;

public class TrailerActivity extends YouTubeBaseActivity {

    @BindView(R.id.player)YouTubePlayerView trailerVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);
        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        String url="https://api.themoviedb.org/3/movie/"+movie.getId()+"/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client=new AsyncHttpClient();

        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    final String trailerVideoKey = getVideoTrailerKey(response.getJSONArray("youtube"));

                    trailerVideo.initialize("AIzaSyBY-77hI-cvNkiNrjGGrJxW8Sic5uZ187A", new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            final YouTubePlayer youTubePlayer, boolean b) {

                            if (trailerVideoKey.length() > 0) {
                                youTubePlayer.loadVideo(trailerVideoKey);
                            } else {
                                finish();
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private String getVideoTrailerKey(JSONArray results) throws JSONException {
        for (int i = 0; i < results.length(); i++) {
            JSONObject video = results.getJSONObject(i);
            if (video.getString("name").equalsIgnoreCase("Official Trailer")) {
                return video.getString("source");
            }
        }
        return "";
    }

}
