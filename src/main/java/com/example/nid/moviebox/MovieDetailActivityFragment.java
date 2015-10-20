package com.example.nid.moviebox;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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
    String SHARED_PREFS_FILE2="SHARED_PREFS_FILE3";
    String MOVIE_ID = "MOVIE_ID";


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

        Intent intent = getActivity().getIntent();

        objIntentMovieBean = (MovieBean) intent.getSerializableExtra("MOVIEBEAN");
        Log.e("onStart","onStart");
        Log.e(MovieDetailActivityFragment.class.getSimpleName(), objIntentMovieBean.getOverview());

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("onActivityC frag2", "haha");

        if (savedInstanceState != null) {
            objIntentMovieBean=new MovieBean();
            objIntentMovieBean.setId(savedInstanceState.getInt(MOVIE_ID));
            Log.e("onActivityCreated", Integer.toString(savedInstanceState.getInt(MOVIE_ID)));


        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //Save the fragment's state here
        Log.e("onSaveIns DetailFrag->", "in onSaveInstanceState");

        if (objIntentMovieBean != null) {
            savedInstanceState.putInt(MOVIE_ID, objIntentMovieBean.getId());
        }


    }


    public void fetchMovieDetails() {

        FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask(new FragmentCallback() {

            @Override
            public void onTaskDone() {
                methodThatDoesSomethingWhenTaskIsDone();
            }
        });


        fetchMovieDetailTask.execute();


    }

    private void methodThatDoesSomethingWhenTaskIsDone() {

        Log.v(FetchMovieDetailTask.class.getName(), "IN methodThatDoesSomethingWhenTaskIsDone-> ");
/*
        ImageView objImageView =(ImageView) getView().findViewById(R.id.image_in_detailFragment);

        String PosterPathHref="http://image.tmdb.org/t/p/w185/"+objMovieBean.getPoster_path();
        Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);

        TextView objTextView = (TextView) getView().findViewById(R.id.description);
        objTextView.setText(objMovieBean.getOverview());

*/


        ImageView objImageView = (ImageView) getView().findViewById(R.id.image_in_detailFragment);

        String PosterPathHref = "http://image.tmdb.org/t/p/w185" + objMovieBean.getPoster_path();
        //    PosterPathHref="http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg";
        Log.v("PosterPath", PosterPathHref);
        //  Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);
        Picasso.with(getActivity()).load(PosterPathHref).into(objImageView);

        TextView objTitleText = (TextView) getView().findViewById(R.id.movie_title_Highlight);
        objTitleText.setText(objMovieBean.getTitle());

        TextView objTextRelease = (TextView) getView().findViewById(R.id.movie_release_date);
        objTextRelease.setText(objMovieBean.getRelease_date().substring(0, 4));

        TextView objTextRating = (TextView) getView().findViewById(R.id.movie_rating);
        objTextRating.setText(Float.toString(objMovieBean.getVote_average()) + "/10");

        TextView objTextView = (TextView) getView().findViewById(R.id.description_detail);
        objTextView.setText(objMovieBean.getOverview());


        Button objButton = (Button) getView().findViewById(R.id.faourite_button);
        if (checkInSharedPrefrence(objMovieBean)) {
            objButton.setBackgroundColor(Color.parseColor("#FF5722"));
        }

        objButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                //to be added
                Log.e("getView====>", "Oclicklistener called Id->" + v.getId());
                Log.e("ButtonOnclick->", Integer.toString(objMovieBean.getId()));

                if (!checkInSharedPrefrence(objMovieBean)) {
                    Log.e("addInSharedPrefrence->", Integer.toString(objMovieBean.getId()));
                    addInSharedPrefrence(objMovieBean);
                    v.setBackgroundColor(Color.parseColor("#FF5722"));
                } else {
                    Log.e("removefromSharedPrefr->", Integer.toString(objMovieBean.getId()));
                    removefromSharedPrefrence(objMovieBean);
                    v.setBackgroundColor(Color.parseColor("#607D8B"));
                }


            }


        });

        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.trailer_images_filler);
        layout.removeAllViewsInLayout();

        int counter = 0;

        for (final Trailer valueTrailer : objMovieBean.getTrailerList()) {

            Log.v("getView====>", valueTrailer.getTrailername());
            Log.v("getView====>", valueTrailer.getSource());

            ImageView image = new ImageView(getActivity());
            //     String BackdropPathHref="http://image.tmdb.org/t/p/w185"+objMovieBean.getBackdrop_path();
            Picasso.with(getActivity()).load(PosterPathHref).into(image);
            //  image.setBackgroundResource(R.drawable.martianposter);
            image.setId(counter);
            image.setTag(valueTrailer.getSource());
            layout.addView(image);
            counter++;


            image.setOnClickListener(new ImageView.OnClickListener() {

                public void onClick(View v) {

                    //to be added
                    Log.v("getView====>", "Oclicklistener called Id->" + v.getId());
                    startVideo((String) v.getTag());

                }


            });

        }

        TextView objTextviewReview = (TextView) getView().findViewById(R.id.description_Review);
        objTextviewReview.setLines(9);
        try {
            objTextviewReview.setText(objMovieBean.getReviewlist().get(0).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView objTextReviewAuthor = (TextView) getView().findViewById(R.id.atuhor_review);
        try {
            objTextReviewAuthor.setText("Review by " + objMovieBean.getReviewlist().get(0).getAuthor());
        } catch (Exception e) {
            e.printStackTrace();
        }

        objTextviewReview.setOnClickListener(new TextView.OnClickListener() {

            public void onClick(View v) {

                //to be added
                Log.v("getView====>", "Review Onclicklistener called Id->" + v.getId());

                String textData = "";
                for (final Review valueReview : objMovieBean.getReviewlist()) {
                    // Log.v("getView====>",valueTrailer.getTrailerOrigin());
                    Log.v("getView====>", valueReview.getAuthor());

                    textData = textData + valueReview.getContent() + "\n";
                    textData = textData + "By " + valueReview.getAuthor() + "\n\n\n";

                }

                Intent IntentReviewDetailActivity = new Intent(getActivity(), ReviewDetail.class);

                IntentReviewDetailActivity.putExtra("REVIEW", textData);

                startActivity(IntentReviewDetailActivity);


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


            MovieBean MovieBeanForId = new MovieBean();
            //Fetch Movie Id
            MovieBeanForId.setId(objIntentMovieBean.getId());


            String apikey = getActivity().getString(R.string.api_key);

            //https://api.themoviedb.org/3/movie/550?api_key=###&append_to_response=releases,trailers
            //https://api.themoviedb.org/3/movie/550?api_key=###&append_to_response=trailers
            //http://api.themoviedb.org/3/movie/550/videos

            //actual URL
            // https://www.youtube.com/watch?v=8hP9D6kZseM

            try {

                final String MOVIE_DETAILDB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "api_key";
                final String APPENDTRAILER = "append_to_response";
                final String appendTrailerVaule = "trailers,reviews";

                int Movieid = MovieBeanForId.getId();
                Log.v(MovieDetailActivityFragment.class.getSimpleName(), Integer.toString(MovieBeanForId.getId()));


                Uri builtUri = Uri.parse(MOVIE_DETAILDB_BASE_URL + Integer.toString(Movieid) + "?").buildUpon()
                        .appendQueryParameter(API_KEY, apikey)
                        .appendQueryParameter(APPENDTRAILER, appendTrailerVaule)
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
                    Log.v(FetchMovieDetailTask.class.getSimpleName(), "Inside NULL return buffer");
                    return null;
                } else {
                    Log.v(FetchMovieDetailTask.class.getSimpleName(), "Inside NULL ELSE buffer");
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
                    Log.v(FetchMovieDetailTask.class.getSimpleName(), "Inside NULL buffer buffer");
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(FetchMovieDetailTask.class.getSimpleName(), forecastJsonStr);
                Log.v(FetchMovieDetailTask.class.getName(), forecastJsonStr);
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

            try {
                return MovieDetailParser(forecastJsonStr);
                //   return "test";

            } catch (Exception e) {
                Log.e("exception in JSON", "sdf", e);
            }
            return null;

        }

        public MovieBean MovieDetailParser(String JsonStr) {
            new MovieBean();

            try {

                //Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

                JSONObject jsonBaseObject = new JSONObject(JsonStr);
                String title = jsonBaseObject.getString("title");
                int Mid = Integer.parseInt(jsonBaseObject.optString("id"));
                String relasedate = jsonBaseObject.getString("release_date");
                String movieposter = jsonBaseObject.getString("poster_path");
                String backdrop = jsonBaseObject.getString("backdrop_path");
                int VoteAvg = jsonBaseObject.getInt("vote_average");
                String plotSynopsis = jsonBaseObject.getString("overview");

                JSONObject jsonObjectTrailer = jsonBaseObject.getJSONObject("trailers");
                JSONArray jsonArrayTrailerYoutube = jsonObjectTrailer.getJSONArray("youtube");


                List<Trailer> ArrListTrailers = new ArrayList<Trailer>();


                for (int j = 0; j < jsonArrayTrailerYoutube.length(); j++) {

                    JSONObject jsonObjectTrailerData = jsonArrayTrailerYoutube.getJSONObject(j);

                    String trailerOrigin = "youtube";
                    String trailername = jsonObjectTrailerData.getString("name");
                    String size = jsonObjectTrailerData.getString("size");
                    String source = jsonObjectTrailerData.getString("source");
                    String type = jsonObjectTrailerData.getString("type");

                    Trailer TrailerObject = new Trailer();
                    TrailerObject.setTrailerOrigin(trailerOrigin);
                    TrailerObject.setTrailername(trailername);
                    TrailerObject.setSize(size);
                    TrailerObject.setSource(source);
                    TrailerObject.setType(type);

                    Log.v(FetchMovieDetailTask.class.getName(), "Trailer-> " + TrailerObject.getSource() + TrailerObject.getTrailername());

                    ArrListTrailers.add(j, TrailerObject);

                }

                JSONObject jsonObjectReview = jsonBaseObject.getJSONObject("reviews");
                JSONArray jsonArrayReviews = jsonObjectReview.getJSONArray("results");

                List<Review> ArrListReviews = new ArrayList<>();
                for (int j = 0; j < jsonArrayReviews.length(); j++) {

                    JSONObject jsonObjectReviewData = jsonArrayReviews.getJSONObject(j);

                    String id = jsonObjectReviewData.getString("id");
                    String author = jsonObjectReviewData.getString("author");
                    String content = jsonObjectReviewData.getString("content");
                    String url = jsonObjectReviewData.getString("url");

                    Review objReview = new Review();
                    objReview.setId(id);
                    objReview.setAuthor(author);
                    objReview.setContent(content);
                    objReview.setUrl(url);

                    Log.v(FetchMovieDetailTask.class.getName(), "Review-> " + objReview.getAuthor() + objReview.getContent());


                    ArrListReviews.add(j, objReview);

                }


                objMovieBean = new MovieBean();

                objMovieBean.setTitle(title);
                objMovieBean.setId(Mid);
                objMovieBean.setRelease_date(relasedate);
                objMovieBean.setPoster_path(movieposter);
                objMovieBean.setBackdrop_path(backdrop);
                objMovieBean.setVote_average(VoteAvg);
                objMovieBean.setOverview(plotSynopsis);
                objMovieBean.setTrailerList(ArrListTrailers);
                objMovieBean.setReviewlist(ArrListReviews);

                Log.v(FetchMovieDetailTask.class.getName(), "IN PARSER-> " + objMovieBean.getRelease_date());


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

                Log.v(FetchMovieDetailTask.class.getName(), "ON POST EXECUTE-> ");


            }
        }


    }

    //Adapter for  Listview
    public class TrailerList extends ArrayAdapter<MovieBean> {

        private final Activity context;
        public ArrayList<MovieBean> adapterData;
        //   private final Integer[] imageId;

        public TrailerList(Activity context, ArrayList<MovieBean> web, int resource) {
            super(context, R.layout.detail_fragment_trailer_listview, web);


            this.adapterData = web;
            this.context = context;
            //   this.imageId = imageId;


        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Integer it = position;
            Log.v("getView Called", it.toString());
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.detail_fragment_trailer_listview, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.list_item_trailer_name);

            for (final MovieBean value : adapterData) {
                //   Log.v("getView====>",value.getArtistName());
                Integer iy = adapterData.size();
                Log.v("Shouldbe1=>", iy.toString());

                for (final Trailer valueTrailer : value.getTrailerList()) {
                    // Log.v("getView====>",valueTrailer.getTrailerOrigin());
                    Log.v("getView====>", valueTrailer.getTrailername());

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
            Log.v("getCount Called", Integer.toString(adapterData.get(0).getTrailerList().size()));
            return adapterData.get(0).getTrailerList().size();

            //  return super.getCount();
        }


    }

    private ArrayList<MovieBean> populateTrailerDummyData() {

        MovieBean objMovieBean = new MovieBean();

        ArrayList<MovieBean> tempMovie = new ArrayList<MovieBean>();

        ArrayList<Trailer> tempTrailer = new ArrayList<Trailer>();


        Trailer objTrailer = new Trailer();
        objTrailer.setTrailername("Test Trailer 1");
        tempTrailer.add(0, objTrailer);
        objTrailer.setTrailername("Test Trailer 2");
        tempTrailer.add(1, objTrailer);
        objTrailer.setTrailername("Test Trailer 3");
        tempTrailer.add(2, objTrailer);

        objMovieBean.setTrailerList(tempTrailer);


        tempMovie.add(objMovieBean);

        return tempMovie;

    }

    public void startVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }


    public void removefromSharedPrefrence(MovieBean objMovie) {
        ArrayList<MovieBean> objSharedPref = new ArrayList<MovieBean>();
        ArrayList<MovieBean> objBlank = new ArrayList<MovieBean>();
        objBlank.add(new MovieBean());


        Log.e("removefromSharedPre->", objMovie.getTitle());

        Gson gson = new Gson();


        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS_FILE2, Context.MODE_PRIVATE);


        String jsonMovieArrayList;
        jsonMovieArrayList = sharedPref.getString(SHARED_PREFS_FILE2, gson.toJson(objBlank));
        Type type = new TypeToken<ArrayList<MovieBean>>() {
        }.getType();
        objSharedPref = gson.fromJson(jsonMovieArrayList, type);

        int counter = 0;
        for (MovieBean objMovieBean : objSharedPref) {
            if (Integer.toString(objMovieBean.getId()).equalsIgnoreCase(Integer.toString(objMovie.getId()))) {
                Log.e("to removd", objMovieBean.getTitle());
                Log.e("counter->", Integer.toString(counter));
                Log.e("value removed->", objSharedPref.get(counter).getTitle().toString());
                objSharedPref.remove(counter);
                break;
            }
            counter++;
        }


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREFS_FILE2, gson.toJson(objSharedPref));

        editor.commit();

        for (MovieBean objMovieBean : objSharedPref) {
            Log.e("Gson data", objMovieBean.getTitle());
        }


    }


    public void addInSharedPrefrence(MovieBean objMovie) {
        ArrayList<MovieBean> objSharedPref = new ArrayList<MovieBean>();
        ArrayList<MovieBean> objBlank = new ArrayList<MovieBean>();
        MovieBean dummyData = new MovieBean();
        dummyData.setTitle("Discard");
        objBlank.add(0, dummyData);


        Log.e("addInSharedPre->", objMovie.getTitle());

        Gson gson = new Gson();


        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS_FILE2, Context.MODE_PRIVATE);
        String jsonMovieArrayList = sharedPref.getString(SHARED_PREFS_FILE2, gson.toJson(objBlank));
        //  String jsonMovieArrayList=sharedPref.getString(SHARED_PREFS_FILE_NEW, "Default value picked");
        Log.e("json set->", gson.toJson(objBlank));
        Log.e("json ret->", jsonMovieArrayList);

        Type type = new TypeToken<ArrayList<MovieBean>>() {
        }.getType();
        objSharedPref = gson.fromJson(jsonMovieArrayList, type);

        for (MovieBean objMovieBean : objSharedPref) {
            Log.e("Title data->", objMovieBean.toString());
            Log.e("Title data->", objMovieBean.getTitle());
        }

        if (objSharedPref.get(0).getTitle().equalsIgnoreCase("Discard")) {
            objSharedPref.remove(objSharedPref.get(0));
        }
        objSharedPref.add(objMovie);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREFS_FILE2, gson.toJson(objSharedPref));

        editor.commit();

        for (MovieBean objMovieBean : objSharedPref) {
            Log.e("Gson data", objMovieBean.getTitle());
        }


    }


    public boolean checkInSharedPrefrence(MovieBean objMovie) {

        ArrayList<MovieBean> objSharedPref = new ArrayList<MovieBean>();
        ArrayList<MovieBean> objBlank = new ArrayList<MovieBean>();
        objBlank.add(new MovieBean());

        Log.e("checkInSharedPre->", objMovie.getTitle());

        Gson gson = new Gson();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS_FILE2, Context.MODE_PRIVATE);


        String jsonMovieArrayList;
        jsonMovieArrayList = sharedPref.getString(SHARED_PREFS_FILE2, gson.toJson(objBlank));
        Type type = new TypeToken<ArrayList<MovieBean>>() {
        }.getType();
        objSharedPref = gson.fromJson(jsonMovieArrayList, type);

        boolean InSharedPrefrence = false;
        for (MovieBean objMovieBean : objSharedPref) {
            if (Integer.toString(objMovieBean.getId()).equalsIgnoreCase(Integer.toString(objMovie.getId()))) {
                Log.e("Checking array", Boolean.toString(InSharedPrefrence));
                InSharedPrefrence = true;
                break;
            }
        }
        Log.e("checkInSharedPre->", "FoundORnot" + Boolean.toString(InSharedPrefrence));

        return InSharedPrefrence;

    }




}
