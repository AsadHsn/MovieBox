package com.example.nid.moviebox;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    MovieBean objMovieBean;

    MovieBean objIntentMovieBean;

    TrailerList TrailerListAdapterObject;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

//        ListView listview = (ListView) rootView.findViewById(R.id.trailer_listview);
//
//        MovieBean objMovieBean= new MovieBean();
//
//        ArrayList<MovieBean> tempData = populateTrailerDummyData();
//
//        TrailerListAdapterObject = new TrailerList(getActivity(), tempData,1);
//
//        listview.setAdapter(TrailerListAdapterObject);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent=getActivity().getIntent();

        objIntentMovieBean=(MovieBean) intent.getSerializableExtra("MOVIEBEAN");
        Log.v(MovieDetailActivityFragment.class.getSimpleName(), objIntentMovieBean.getOverview());

        //To fetch the data again specially the Rating of the Movie
        fetchMovieDetails();
/*
        ImageView objImageView =(ImageView) getView().findViewById(R.id.image_in_detailFragment);

        String PosterPathHref="http://image.tmdb.org/t/p/w154/"+objIntentMovieBean.getPoster_path();
        Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);

        TextView objTitleText = (TextView) getView().findViewById(R.id.movie_title);
        objTitleText.setText("Title: "+objIntentMovieBean.getTitle());

        TextView objTextRelease = (TextView) getView().findViewById(R.id.movie_release_date);
        objTextRelease.setText("Release Date: "+objIntentMovieBean.getRelease_date());

        TextView objTextRating = (TextView) getView().findViewById(R.id.movie_rating);
        objTextRating.setText("Rating: "+objIntentMovieBean.getTitle());

        TextView objTextView = (TextView) getView().findViewById(R.id.description);
        objTextView.setText("Synopsis: \n"+objIntentMovieBean.getOverview());
*/
    }

    public void fetchMovieDetails()
    {

        FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask(new FragmentCallback() {

            @Override
            public void onTaskDone() {
                methodThatDoesSomethingWhenTaskIsDone();
            }
        });


        fetchMovieDetailTask.execute();


    }

    private void methodThatDoesSomethingWhenTaskIsDone() {

        Log.v(FetchMovieDetailTask.class.getName(), "IN methodThatDoesSomethingWhenTaskIsDone-> " );
/*
        ImageView objImageView =(ImageView) getView().findViewById(R.id.image_in_detailFragment);

        String PosterPathHref="http://image.tmdb.org/t/p/w185/"+objMovieBean.getPoster_path();
        Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);

        TextView objTextView = (TextView) getView().findViewById(R.id.description);
        objTextView.setText(objMovieBean.getOverview());

*/



        ImageView objImageView =(ImageView) getView().findViewById(R.id.image_in_detailFragment);

        String PosterPathHref="http://image.tmdb.org/t/p/w185"+objMovieBean.getPoster_path();
    //    PosterPathHref="http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg";
        Log.v("PosterPath", PosterPathHref);
      //  Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);
        Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);

        TextView objTitleText = (TextView) getView().findViewById(R.id.movie_title_Highlight);
        objTitleText.setText(objMovieBean.getTitle());

        TextView objTextRelease = (TextView) getView().findViewById(R.id.movie_release_date);
        objTextRelease.setText(objMovieBean.getRelease_date().substring(0, 4));

        TextView objTextRating = (TextView) getView().findViewById(R.id.movie_rating);
        objTextRating.setText( Float.toString(objMovieBean.getVote_average())+"/10");

        TextView objTextView = (TextView) getView().findViewById(R.id.description_detail);
        objTextView.setText(objMovieBean.getOverview());


        LinearLayout layout = (LinearLayout)getView().findViewById(R.id.trailer_images_filler);


            int counter=0;

            for (final Trailer valueTrailer : objMovieBean.getTrailerList()) {

                Log.v("getView====>",valueTrailer.getTrailername());
                Log.v("getView====>", valueTrailer.getSource());

                ImageView image = new ImageView(getActivity());
                image.setBackgroundResource(R.drawable.martianposter);
                image.setId(counter);
                layout.addView(image);

                counter++;

            }

        layout.setOnClickListener(new LinearLayout.OnClickListener() {

            public void onClick(View v) {


                //to be added

            }


        });




    }

    public interface FragmentCallback {
        public void onTaskDone();
    }





    public class FetchMovieDetailTask extends AsyncTask<String, Void, MovieBean> {

        private FragmentCallback mFragmentCallback;

        public FetchMovieDetailTask(FragmentCallback fragmentCallback) {
            mFragmentCallback = fragmentCallback;
        }

        @Override
        protected MovieBean doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.

            //http://api.themoviedb.org/3/movie/550?api_key=###
            String forecastJsonStr = null;


            MovieBean MovieBeanForId= new MovieBean();
            //Fetch Movie Id
            MovieBeanForId.setId(objIntentMovieBean.getId());


            String apikey = getActivity().getString(R.string.api_key );

            //https://api.themoviedb.org/3/movie/550?api_key=###&append_to_response=releases,trailers
            //https://api.themoviedb.org/3/movie/550?api_key=###&append_to_response=trailers
            //http://api.themoviedb.org/3/movie/550/videos

            //actual URL
           // https://www.youtube.com/watch?v=8hP9D6kZseM

            try {

                final String MOVIE_DETAILDB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";
                final String APPENDTRAILER="append_to_response";
                final String appendTrailerVaule="trailers";

               int Movieid= MovieBeanForId.getId();
                Log.v(MovieDetailActivityFragment.class.getSimpleName(), Integer.toString(MovieBeanForId.getId()));


                Uri builtUri = Uri.parse(MOVIE_DETAILDB_BASE_URL + Integer.toString(Movieid) + "?").buildUpon()
                        .appendQueryParameter(API_KEY, apikey)
                        .appendQueryParameter(APPENDTRAILER,appendTrailerVaule)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(FetchMovieDetailTask.class.getSimpleName(), "Tag 333 ->" + builtUri.toString());



                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.v(FetchMovieDetailTask.class.getSimpleName(),"Inside NULL return buffer");
                    return null;
                }
                else
                {
                    Log.v(FetchMovieDetailTask.class.getSimpleName(),"Inside NULL ELSE buffer");
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                    System.out.println("This is the result >>>>>>>>>>>>>>>>>>>\n" + buffer);
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Log.v(FetchMovieDetailTask.class.getSimpleName(),"Inside NULL buffer buffer");
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(FetchMovieDetailTask.class.getSimpleName() ,forecastJsonStr);
                Log.v(FetchMovieDetailTask.class.getName() ,forecastJsonStr);
            } catch (IOException e) {
                Log.e("FetchWeatherTask 1", "Error ", e);
                // Log.v("Fetch IOException", "Error ", e);

                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FetchWeatherTask 22", "Error closing stream", e);
                    }
                }
            }

            try
            {
                return MovieDetailParser(forecastJsonStr);
                //   return "test";

            }
            catch (Exception e)
            {
                Log.e("exception in JSON","sdf",e);
            }
            return null;

        }

        public MovieBean MovieDetailParser(String JsonStr)
        {
             new MovieBean();

            try {

                //Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

                JSONObject jsonBaseObject = new JSONObject(JsonStr);
                String title = jsonBaseObject.getString("title");
                String relasedate = jsonBaseObject.getString("release_date");
                String movieposter = jsonBaseObject.getString("poster_path");
                int VoteAvg = jsonBaseObject.getInt("vote_average");
                String plotSynopsis = jsonBaseObject.getString("overview");

                JSONObject jsonObjectTrailer = jsonBaseObject.getJSONObject("trailers");
                JSONArray jsonArrayTrailerYoutube=jsonObjectTrailer.getJSONArray("youtube");


                List<Trailer> ArrListTrailers= new ArrayList<Trailer>();



                for(int j=0; j < jsonArrayTrailerYoutube.length(); j++){

                    JSONObject jsonObjectTrailerData = jsonArrayTrailerYoutube.getJSONObject(j);

                    String trailerOrigin = "youtube";
                    String trailername = jsonObjectTrailerData.getString("name");
                    String size = jsonObjectTrailerData.getString("size");
                    String source = jsonObjectTrailerData.getString("source");
                    String type = jsonObjectTrailerData.getString("type");

                    Trailer TrailerObject=new Trailer();
                    TrailerObject.setTrailerOrigin(trailerOrigin);
                    TrailerObject.setTrailername(trailername);
                    TrailerObject.setSize(size);
                    TrailerObject.setSource(source);
                    TrailerObject.setType(type);

                    Log.v(FetchMovieDetailTask.class.getName(), "Trailer-> " + TrailerObject.getSource()+TrailerObject.getTrailername());

                    ArrListTrailers.add(j, TrailerObject);

                }


                objMovieBean=new MovieBean();

                objMovieBean.setTitle(title);
                objMovieBean.setRelease_date(relasedate);
                objMovieBean.setPoster_path(movieposter);
                objMovieBean.setVote_average(VoteAvg);
                objMovieBean.setOverview(plotSynopsis);
                objMovieBean.setTrailerList(ArrListTrailers);

                Log.v(FetchMovieDetailTask.class.getName(), "IN PARSER-> "+objMovieBean.getRelease_date());




            } catch (JSONException e) {
            // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return objMovieBean;
        }

        @Override
        protected void onPostExecute(MovieBean result) {
            if (result != null) {

                //Load Image and other details
                mFragmentCallback.onTaskDone();

                Log.v(FetchMovieDetailTask.class.getName(), "ON POST EXECUTE-> " );


            }
        }


    }

    //Adapter for  Listview
    public class TrailerList extends ArrayAdapter<MovieBean> {

        private final Activity context;
        public ArrayList<MovieBean> adapterData;
     //   private final Integer[] imageId;

        public TrailerList(Activity context,ArrayList<MovieBean> web, int resource) {
            super(context, R.layout.detail_fragment_trailer_listview, web);


            this.adapterData = web;
            this.context = context;
         //   this.imageId = imageId;


        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Integer it=position;
            Log.v("getView Called",it.toString() );
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.detail_fragment_trailer_listview, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.list_item_trailer_name);

            for (final MovieBean value : adapterData) {
             //   Log.v("getView====>",value.getArtistName());
                Integer iy=adapterData.size();
                Log.v("Shouldbe1=>", iy.toString());

                for (final Trailer valueTrailer : value.getTrailerList()) {
                   // Log.v("getView====>",valueTrailer.getTrailerOrigin());
                    Log.v("getView====>",valueTrailer.getTrailername());

                }
            }



                //Data is withing Moviebean Arraylist and then in Trailer arraylist, however moviebean is of size 1
            txtTitle.setText(adapterData.get(0).getTrailerList().get(position).getTrailername());

           ImageView imageView = (ImageView) rowView.findViewById(R.id.list_item_image);

            //this has to be updated to play button image later on
            Picasso.with(context).load("http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg").into(imageView);



            return rowView;
        }

        @Override
        public int getCount() {
              Log.v("getCount Called",Integer.toString( adapterData.get(0).getTrailerList().size()));
            return adapterData.get(0).getTrailerList().size();

            //  return super.getCount();
        }



    }

    private ArrayList<MovieBean> populateTrailerDummyData() {

        MovieBean objMovieBean= new MovieBean();

        ArrayList<MovieBean> tempMovie = new ArrayList<MovieBean>();

        ArrayList<Trailer> tempTrailer = new ArrayList<Trailer>();


        Trailer objTrailer = new Trailer();
        objTrailer.setTrailername("Test Trailer 1");
        tempTrailer.add(0,objTrailer);
        objTrailer.setTrailername("Test Trailer 2");
        tempTrailer.add(1,objTrailer);
        objTrailer.setTrailername("Test Trailer 3");
        tempTrailer.add(2,objTrailer);

        objMovieBean.setTrailerList(tempTrailer);



        tempMovie.add(objMovieBean);

        return tempMovie;

    }


}
