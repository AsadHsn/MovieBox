package com.example.nid.moviebox;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nid on 7/10/2015.
 */


public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mHrefIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

     //   imageView.setImageResource(mThumbIds[position]);
      //  Picasso.with(context).load(webadap.get(position).getImgHref()).into(imageView);
        Log.v(ImageAdapter.class.getName(),mHrefIds[position]);
        Log.v(ImageAdapter.class.getName(), parent.toString());
        Log.v(ImageAdapter.class.getName(), parent.getContext().toString());


        Picasso.with(parent.getContext()).load(mHrefIds[position]).into(imageView);
        return imageView;
    }

    // references to our images
    private String[] mHrefIds = {

            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            "http://image.tmdb.org/t/p/w154/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg"

//            R.drawable.sample_0, R.drawable.sample_3,
//            R.drawable.sample_1, R.drawable.sample_4,
//            R.drawable.sample_2, R.drawable.sample_5,
//            R.drawable.sample_3, R.drawable.sample_6,
//            R.drawable.sample_4, R.drawable.sample_7,
//            R.drawable.sample_5, R.drawable.sample_0,
//            R.drawable.sample_6, R.drawable.sample_1,
//            R.drawable.sample_7, R.drawable.sample_2,
//            R.drawable.sample_0, R.drawable.sample_3,
//            R.drawable.sample_1, R.drawable.sample_4,
//            R.drawable.sample_2, R.drawable.sample_5
    };
}
