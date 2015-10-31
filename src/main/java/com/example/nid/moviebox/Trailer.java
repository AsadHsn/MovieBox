package com.example.nid.moviebox;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nid on 15/10/2015.
 */
public class Trailer implements Parcelable {

    String trailerOrigin;
    String trailername;
    String size;
    String source;
    String type;

    protected Trailer() {
    }

    public String getTrailerOrigin() {
        return trailerOrigin;
    }

    public void setTrailerOrigin(String trailerOrigin) {
        this.trailerOrigin = trailerOrigin;
    }

    public String getTrailername() {
        return trailername;
    }

    public void setTrailername(String trailername) {
        this.trailername = trailername;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected Trailer(Parcel in) {
        trailerOrigin = in.readString();
        trailername = in.readString();
        size = in.readString();
        source = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerOrigin);
        dest.writeString(trailername);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}