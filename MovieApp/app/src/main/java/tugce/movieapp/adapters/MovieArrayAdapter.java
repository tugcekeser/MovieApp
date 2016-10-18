package tugce.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import tugce.movieapp.MovieDetailActivity;
import tugce.movieapp.R;
import tugce.movieapp.TrailerActivity;
import tugce.movieapp.models.Movie;

/**
 * Created by Tugce on 10/13/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter <Movie> {
    private int orientation;

    private static class ViewHolderMovie{
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
    }

    private static class ViewHolderTrailer{
        ImageView ivImage;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_expandable_list_item_1,movies);
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).getVoteAverage()>=5)
            return 0;
        else
            return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // return super.getView(position, convertView, parent);
        final Movie movie=getItem(position);

    //    if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(getContext());
            int type = getItemViewType(position);
            if(type==0) {
                convertView = inflater.inflate(R.layout.item_trailer, parent, false);
                ViewHolderTrailer holder=new ViewHolderTrailer();
                holder.ivImage=(ImageView)convertView.findViewById(R.id.ivTrailer);
                holder.ivImage.setImageResource(0);
                Picasso.with(getContext()).load(movie.getBackdropPath())
                        .transform(new RoundedCornersTransformation(10, 10)).into(holder.ivImage);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), TrailerActivity.class).putExtra("Movie", movie);
                        getContext().startActivity(intent);
                    }
                });
            }
            else {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                ViewHolderMovie holder=new ViewHolderMovie();
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                holder.ivImage.setImageResource(0);

                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                holder.tvTitle.setText(movie.getOriginalTitle());

                holder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                holder.tvOverview.setText(movie.getOverview().substring(0,100)+"...");
                Picasso.with(holder.ivImage.getContext())
                        .load(orientation == Configuration.ORIENTATION_PORTRAIT ? movie.getPosterPath() : movie.getBackdropPath())
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder( R.mipmap.popcorn)
                        .into(holder.ivImage);
               // holder.tvTitle.setVisibility(orientation==Configuration.ORIENTATION_PORTRAIT ? View.GONE :View.VISIBLE );
                //Picasso.with(getContext()).load(movie.getPosterPath()).into(holder.ivImage);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MovieDetailActivity.class).putExtra("Movie", movie);
                        getContext().startActivity(intent);
                    }
                });
            }

        return convertView;
    }


}
