package com.example.nid.moviebox;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    MovieBean objMovieBean;

    MovieBean objIntentMovieBean;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
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

        String PosterPathHref="http://image.tmdb.org/t/p/w185/"+objIntentMovieBean.getPoster_path();
        Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);

        TextView objTitleText = (TextView) getView().findViewById(R.id.movie_title_Highlight);
        objTitleText.setText(objIntentMovieBean.getTitle());

        TextView objTextRelease = (TextView) getView().findViewById(R.id.movie_release_date);
        objTextRelease.setText(objIntentMovieBean.getRelease_date());

        TextView objTextRating = (TextView) getView().findViewById(R.id.movie_rating);
        objTextRating.setText( Float.toString(objIntentMovieBean.getVote_average())+"/10");

        TextView objTextView = (TextView) getView().findViewById(R.id.description);
        objTextView.setText("Synopsis: \n"+objIntentMovieBean.getOverview());



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


            try {

                final String MOVIE_DETAILDB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";

               int Movieid= MovieBeanForId.getId();
                Log.v(MovieDetailActivityFragment.class.getSimpleName(),Integer.toString(MovieBeanForId.getId()));


                Uri builtUri = Uri.parse(MOVIE_DETAILDB_BASE_URL + Integer.toString(Movieid) + "?").buildUpon()
                        .appendQueryParameter(API_KEY, apikey)
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

                objMovieBean=new MovieBean();

                objMovieBean.setTitle(title);
                objMovieBean.setRelease_date(relasedate);
                objMovieBean.setPoster_path(movieposter);
                objMovieBean.setVote_average(VoteAvg);
                objMovieBean.setOverview(plotSynopsis);

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




}
