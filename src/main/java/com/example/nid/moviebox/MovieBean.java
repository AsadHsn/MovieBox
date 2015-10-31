package com.example.nid.moviebox;

/**
 * Created by Nid on 8/10/2015.
 */


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//This class implements serializable to pass this as an object via Intent to second Activity
public class MovieBean implements Serializable, Parcelable {
    boolean adult ;
    String backdrop_path ;
    String original_title ;
    String overview ;
    String release_date ;
    String poster_path ;
    boolean video ;
    float vote_average ;
    int vote_count ;
    float popularity ;
    String title ;
    boolean isFavourite=false;
    List<Integer> genre_ids;
    int id;
    List<Trailer> trailerList;
    List<Review> reviewlist;



    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }


    public boolean isFavourite() {
        return isFavourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public List<Review> getReviewlist() {
        return reviewlist;
    }

    public void setReviewlist(List<Review> reviewlist) {
        this.reviewlist = reviewlist;
    }

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


    protected MovieBean() {
    }


    protected MovieBean(Parcel in) {
        adult = in.readByte() != 0x00;
        backdrop_path = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        video = in.readByte() != 0x00;
        vote_average = in.readFloat();
        vote_count = in.readInt();
        popularity = in.readFloat();
        title = in.readString();
        isFavourite = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            genre_ids = new ArrayList<Integer>();
            in.readList(genre_ids, Integer.class.getClassLoader());
        } else {
            genre_ids = null;
        }
        id = in.readInt();
        if (in.readByte() == 0x01) {
            trailerList = new ArrayList<Trailer>();
            in.readList(trailerList, Trailer.class.getClassLoader());
        } else {
            trailerList = null;
        }
        if (in.readByte() == 0x01) {
            reviewlist = new ArrayList<Review>();
            in.readList(reviewlist, Review.class.getClassLoader());
        } else {
            reviewlist = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(backdrop_path);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        dest.writeFloat(vote_average);
        dest.writeInt(vote_count);
        dest.writeFloat(popularity);
        dest.writeString(title);
        dest.writeByte((byte) (isFavourite ? 0x01 : 0x00));
        if (genre_ids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre_ids);
        }
        dest.writeInt(id);
        if (trailerList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trailerList);
        }
        if (reviewlist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviewlist);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieBean> CREATOR = new Parcelable.Creator<MovieBean>() {
        @Override
        public MovieBean createFromParcel(Parcel in) {
            return new MovieBean(in);
        }

        @Override
        public MovieBean[] newArray(int size) {
            return new MovieBean[size];
        }
    };
}