package com.example.nid.moviebox;

/**
 * Created by Nid on 8/10/2015.
 */


import java.io.Serializable;
import java.util.List;

//This class implements serializable to pass this as an object via Intent to second Activity
public class MovieBean implements Serializable {
    boolean adult ;
    String backdrop_path ;
    String original_title ;
    String overview ;
    String release_date ;
    String poster_path ;
    float popularity ;
    String title ;
    boolean video ;
    float vote_average ;
    int vote_count ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    List<Integer> genre_ids;
    int id;

    List<Trailer> trailerList;





    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }
    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }



    public List<Integer> getGenre_ids() {
        return genre_ids;
    }
    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
    public String getOriginal_title() {
        return original_title;
    }
    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public float getPopularity() {
        return popularity;
    }
    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAdult() {
        return adult;
    }
    public void setAdult(boolean adult) {
        this.adult = adult;
    }
    public boolean isVideo() {
        return video;
    }
    public void setVideo(boolean video) {
        this.video = video;
    }
    public float getVote_average() {
        return vote_average;
    }
    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
    public int getVote_count() {
        return vote_count;
    }
    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

}
