package com.amrit.practice.filesbygooglereplica.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageUtil implements Parcelable {

    public static final Parcelable.Creator<ImageUtil> CREATOR = new Parcelable.Creator<ImageUtil>() {
        public ImageUtil createFromParcel(Parcel in) {
            return new ImageUtil(in);
        }

        public ImageUtil[] newArray(int size) {
            return new ImageUtil[size];
        }

    };

    //  Required Params for the Image Files
    private final String uri;
    private final int size;
    private final String name;
    private final long date;
    private final String path;

    // Constructor for the same
    public ImageUtil(String uri, int size, String name, long date, String path) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.date = date;
        this.path = path;
    }

    // Getters to get the values
    public String getUri() {
        return uri;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public long getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    protected ImageUtil(Parcel in){
        super();
        uri = in.readString();
        size = in.readInt();
        name = in.readString();
        date = in.readLong();
        path = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeInt(size);
        dest.writeString(name);
        dest.writeLong(date);
        dest.writeString(path);
    }

    public static boolean isImage(String name){
        String[] ext = {".jpg", ".png", ".gif", ".bmp", ".webp", ".heic", ".heif"};
        for (String s : ext) if (name.endsWith(s)) return true;
        return false;
    }

}
