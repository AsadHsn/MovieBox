package com.example.nid.moviebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

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
public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    List<MovieBean> objArrListMovieBean;
    InnerImageArrayAdapter  mCustomMovieAdapter;
    String sortCriteriaSelected="Most Popular";
    String SORT_SELECTION="SORT";
    String GRID_INDEX="GRID";
    int indexValue;
    GridView gridview;
    String SHARED_PREFS_FILE="MovieF";
    String SHARED_PREFS_FILE2="SHARED_PREFS_FILE2";






    public MainActivityFragment() {
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Log.v(MainActivityFragment.class.getSimpleName() + "Yipee", parent.getItemAtPosition(pos).toString());

        fetchCorrectDataForSpinner(parent.getItemAtPosition(pos).toString());

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        Log.v(MainActivityFragment.class.getSimpleName(), "NOTHING Selected");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridview = (GridView) rootView.findViewById(R.id.gridview_movie);

        MovieBean objMovieBean= new MovieBean();
     //  objMovieBean.setPoster_path("/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg");
      //  objMovieBean.setBackdrop_path("/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg");


        ArrayList<MovieBean> tempData = populateDummyData();


        mCustomMovieAdapter = new InnerImageArrayAdapter(getActivity(), 1 ,tempData);

        if(indexValue>0)
        gridview.setSelection(indexValue);
        gridview.setAdapter(mCustomMovieAdapter);


        //Setting spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.sort_criteria_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.sort_options_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //Making grid view clickable

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View listView, int position, long id) {


                Intent IntentMovieDetailActivity = new Intent(getActivity(), MovieDetailActivity.class);

                if(mCustomMovieAdapter.getItem(position).getTrailerList()!=null || mCustomMovieAdapter.getItem(position).getReviewlist()!=null)
                {
                    MovieBean objMbean= mCustomMovieAdapter.getItem(position);
                    objMbean.setTrailerList(null);
                    objMbean.setReviewlist(null);

                    IntentMovieDetailActivity.putExtra("MOVIEBEAN", objMbean);

                    startActivity(IntentMovieDetailActivity);

                }
                else {

                    IntentMovieDetailActivity.putExtra("MOVIEBEAN", (MovieBean) mCustomMovieAdapter.getItem(position));

                    startActivity(IntentMovieDetailActivity);
                }

            }

        });


        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("onStart called", "onstart");

        fetchCorrectDataForSpinner(sortCriteriaSelected);


    }


    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieBean>> {

        @Override
        protected ArrayList<MovieBean> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            String sortby = "popularity.desc";
            String voteCount="1000";

            if(params.length!=0 ) {
                String SortSelection = params[0];
                Log.v(FetchMovieTask.class.getSimpleName(),params[0]);


                if (SortSelection.equalsIgnoreCase("Most Popular")) {
                    sortby = "popularity.desc";
                } else if (SortSelection.equalsIgnoreCase("Highest Rated")) {
                    sortby = "vote_average.desc";
                }

            }


            String apikey = getActivity().getString(R.string.api_key );

            int numDays = 7;

            try {

                final String MOVIEDB_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";
                final String VOTE_COUNT="vote_count.gte";


                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, sortby)
                        .appendQueryParameter(API_KEY, apikey)
                        .appendQueryParameter(VOTE_COUNT,voteCount)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(FetchMovieTask.class.getSimpleName(), "Tag 333 ->" + builtUri.toString());



                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.v(FetchMovieTask.class.getSimpleName(),"Inside NULL return buffer");
                    return null;
                }
                else
                {
                    Log.v(FetchMovieTask.class.getSimpleName(),"Inside NULL ELSE buffer");
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
                    Log.v(FetchMovieTask.class.getSimpleName(),"Inside NULL buffer buffer");
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(FetchMovieTask.class.getSimpleName() ,forecastJsonStr);
                Log.v(FetchMovieTask.class.getName() ,forecastJsonStr);
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
                return MdbParser(forecastJsonStr);
             //   return "test";

            }
            catch (Exception e)
            {
                Log.e("exception in JSON","sdf",e);
            }
            return null;

        }

        public ArrayList<MovieBean> MdbParser(String JsonStr)
        {
            String strJson="{\"page\":1,\"results\":[{\"adult\":false,\"backdrop_path\":\"/t5KONotASgVKq4N19RyhIthWOPG.jpg\",\"genre_ids\":[28,12,878,53],\"id\":135397,\"original_language\":\"en\",\"original_title\":\"Jurassic World\",\"overview\":\"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",\"release_date\":\"2015-06-12\",\"poster_path\":\"/hTKME3PUzdS3ezqK5BZcytXLCUl.jpg\",\"popularity\":52.593315,\"title\":\"Jurassic World\",\"video\":false,\"vote_average\":7.0,\"vote_count\":2503},{\"adult\":false,\"backdrop_path\":\"/bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg\",\"genre_ids\":[878,28,53,12],\"id\":87101,\"original_language\":\"en\",\"original_title\":\"Terminator Genisys\",\"overview\":\"The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.\",\"release_date\":\"2015-07-01\",\"poster_path\":\"/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg\",\"popularity\":41.391217,\"title\":\"Terminator Genisys\",\"video\":false,\"vote_average\":6.3,\"vote_count\":1085}] ,\"total_pages\":12297,\"total_results\":245922}";

            try {
               // JSONObject jsonBaseObject = new JSONObject(strJson);
                JSONObject jsonBaseObject = new JSONObject(JsonStr);
                //get Array corresponding to result set
                JSONArray jsonArray = jsonBaseObject.getJSONArray("results");
                objArrListMovieBean = new ArrayList<MovieBean>();
                //Iterate Array and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    String backdrop_path = jsonObject.getString("backdrop_path");
                    boolean adult = jsonObject.getBoolean("adult");

                    JSONArray genre_ids = jsonObject.getJSONArray("genre_ids");
                    List<Integer> ArrListGenreIds= new ArrayList<Integer>();
                    for(int j=0; j < genre_ids.length(); j++){
                        ArrListGenreIds.add(j, genre_ids.getInt(j));

                    }

                    String original_title = jsonObject.getString("original_title");
                    String overview = jsonObject.getString("overview");
                    String release_date = jsonObject.getString("release_date");
                    String poster_path = jsonObject.getString("poster_path");
                    float popularity = Float.parseFloat(jsonObject.optString("popularity"));
                    String title = jsonObject.getString("title");
                    boolean video = jsonObject.getBoolean("video");
                    float vote_average = Float.parseFloat(jsonObject.optString("vote_average"));
                    int vote_count = Integer.parseInt(jsonObject.optString("vote_count"));
                    int id=Integer.parseInt(jsonObject.optString("id"));

                    MovieBean objMovieBean= new MovieBean();
                    objMovieBean.setAdult(adult);
                    objMovieBean.setBackdrop_path(backdrop_path);
                    objMovieBean.setGenre_ids(ArrListGenreIds);
                    objMovieBean.setOriginal_title(original_title);
                    objMovieBean.setOverview(overview);
                    objMovieBean.setRelease_date(release_date);
                    objMovieBean.setPoster_path(poster_path);
                    objMovieBean.setPopularity(popularity);
                    objMovieBean.setTitle(title);
                    objMovieBean.setVideo(video);
                    objMovieBean.setVote_average(vote_average);
                    objMovieBean.setVote_count(vote_count);
                    objMovieBean.setId(id);

                    //   System.out.println("Overview ->> "+overview);


                    objArrListMovieBean.add(objMovieBean);


                }

                for (final MovieBean objMovieBean : objArrListMovieBean) {


                    System.out.println("Movie : =====:" );

                    Log.v(FetchMovieTask.class.getName(), objMovieBean.getOriginal_title());

                    Log.v(FetchMovieTask.class.getName(), String.valueOf(  objMovieBean.isAdult()) );

                    Log.v(FetchMovieTask.class.getName(), objMovieBean.getBackdrop_path());

                    Log.v(FetchMovieTask.class.getName(), objMovieBean.getOverview());

                    Log.v(FetchMovieTask.class.getName(), objMovieBean.getRelease_date());

                    Log.v(FetchMovieTask.class.getName(), objMovieBean.getPoster_path());

                    Log.v(FetchMovieTask.class.getName(), String.valueOf( objMovieBean.getPopularity()));

                    Log.v(FetchMovieTask.class.getName(), objMovieBean.getTitle());

                    Log.v(FetchMovieTask.class.getName(), String.valueOf( objMovieBean.isVideo() ));

                    Log.v(FetchMovieTask.class.getName(), String.valueOf(objMovieBean.getVote_average()));

                    Log.v(FetchMovieTask.class.getName(), String.valueOf(objMovieBean.getVote_count()));

//http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg

                    List<Integer> ArrListGenreIds2= new ArrayList<Integer>();

                    ArrListGenreIds2=objMovieBean.getGenre_ids();
                    for(int j=0; j < ArrListGenreIds2.size(); j++)
                    {

                        System.out.println("Genre Values is ->> "+ArrListGenreIds2.get(j) + " for position ="+ j +" and movie " + objMovieBean.getTitle() );

                    }



                }


            } catch (JSONException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

         //   ArrayList<MovieBean> objARRAYLISTMovieBean = (ArrayList<MovieBean>)objArrListMovieBean;

            return (ArrayList<MovieBean>)objArrListMovieBean;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieBean> result) {
            if (result != null) {



                for (MovieBean s : result) {
                    Log.v("onPostExecute->",  "s.getArtistName() " +s.getPoster_path());

                }



                Log.v("onPostExecute", "Before clear");

                mCustomMovieAdapter.clear();
                mCustomMovieAdapter.addAll(result);
                mCustomMovieAdapter.notifyDataSetChanged();


            }
        }


    }


    public void updateMovie()
    {

        FetchMovieTask fetchMovierTask = new FetchMovieTask();

        Log.v(FetchMovieTask.class.getName(), "khb");

        fetchMovierTask.execute();


    }

    public class InnerImageArrayAdapter extends ArrayAdapter<MovieBean>
    {
        List<MovieBean> ObjMovieBean;
        private final Activity context;


        public InnerImageArrayAdapter(Activity context, int resource, List<MovieBean> objects) {
            super(context, resource, objects);

            this.ObjMovieBean=objects;
            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
         //    super.getView(position, convertView, parent);

            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_custom_layout, null, true);
            }

            String hrefFull="http://image.tmdb.org/t/p/w185"+ObjMovieBean.get(position).getPoster_path();
            Log.v(FetchMovieTask.class.getName(), hrefFull);

            ImageView objImageView=(ImageView)convertView.findViewById(R.id.image_in_layout);
            boolean objisNetworkAvailable= isNetworkAvailable();

            if(objisNetworkAvailable)
            {
                Picasso.with(parent.getContext()).load(hrefFull).into(objImageView);
            }
            else
            {

                String uri = "@drawable/reel_launcher";

                int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                objImageView.setImageDrawable(res);
            }





            return convertView;
        }

        @Override
        public int getCount() {

            return ObjMovieBean.size();

        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private ArrayList<MovieBean> populateDummyData() {

        MovieBean objMovieBean= new MovieBean();

        ArrayList<MovieBean> tempData = new ArrayList<MovieBean>();
        for(int i=0;i<20;i++)
        {
            tempData.add(objMovieBean);

        }

        return tempData;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //Save the fragment's state here
        Log.e("onSaveInstanceS->","in onSaveInstanceState");
        indexValue=gridview.getFirstVisiblePosition();

        savedInstanceState.putString(SORT_SELECTION, sortCriteriaSelected);
        savedInstanceState.putInt(GRID_INDEX, indexValue);


    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("onActivityCreated", "haha");

        if (savedInstanceState != null) {
            sortCriteriaSelected=savedInstanceState.getString(SORT_SELECTION);
            indexValue=savedInstanceState.getInt(GRID_INDEX);
            Log.v("onActivityCreated",sortCriteriaSelected);
            Log.v("onActivityCreated",Integer.toString(indexValue));

        }




    }



    public void fetchCorrectDataForSpinner(String SortCriteria)
    {

        sortCriteriaSelected=SortCriteria;

        if(SortCriteria.equalsIgnoreCase("Favourite list"))
        {
            //fetch from shared preference
            Gson gson = new Gson();
            ArrayList<MovieBean> objBlank= new ArrayList<MovieBean>();
            objBlank.add(new MovieBean());

            SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS_FILE2, Context.MODE_PRIVATE);
            String jsonMovieArrayList=sharedPref.getString(SHARED_PREFS_FILE2, gson.toJson(objBlank));
            Type type = new TypeToken<ArrayList<MovieBean>>(){}.getType();
            ArrayList<MovieBean>  objSharedPref =gson.fromJson(jsonMovieArrayList, type);

            mCustomMovieAdapter.clear();
            mCustomMovieAdapter.addAll(objSharedPref);
            mCustomMovieAdapter.notifyDataSetChanged();

        }
        else
        {

            //fetch by API call
            FetchMovieTask fetchMovierTaskForSpinner = new FetchMovieTask();
            fetchMovierTaskForSpinner.execute(SortCriteria);

        }


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("onViewStateRestored", "onViewStateRestored");
        if(savedInstanceState!=null)
        Log.e("savedInstanceState",savedInstanceState.toString());
    }



}
