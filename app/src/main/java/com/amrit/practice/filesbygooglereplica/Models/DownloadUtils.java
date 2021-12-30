package com.amrit.practice.filesbygooglereplica.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadUtils implements Parcelable {

    //  Required Params for the Download Files
    private String uri;
    private int size;
    private String name;
    private long date;
    private final String location;

    // Constructor for the same
    public DownloadUtils(String uri, int size, String name, long date, String location) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.date = date;
        this.location = location;
    }

    protected DownloadUtils(Parcel in) {
        uri = in.readString();
        size = in.readInt();
        name = in.readString();
        date = in.readLong();
        location = in.readString();
    }

    public static final Creator<DownloadUtils> CREATOR = new Creator<DownloadUtils>() {
        @Override
        public DownloadUtils createFromParcel(Parcel in) {
            return new DownloadUtils(in);
        }

        @Override
        public DownloadUtils[] newArray(int size) {
            return new DownloadUtils[size];
        }
    };

    // Getters to get the values
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeInt(size);
        dest.writeString(name);
        dest.writeLong(date);
        dest.writeString(location);
    }
}
