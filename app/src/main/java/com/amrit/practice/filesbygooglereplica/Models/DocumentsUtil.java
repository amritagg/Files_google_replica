package com.amrit.practice.filesbygooglereplica.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class DocumentsUtil implements Parcelable {

    //  Required Params for the Document Files
    private String uri;
    private int size;
    private long date;
    private String name;
    private final Bitmap bitmap;
    private String location;

    // Constructor for the same
    public DocumentsUtil(String uri, int size, String name, Bitmap bitmap, long date, String location) {
        this.uri = uri;
        this.size = size;
        this.name = name;
        this.bitmap = bitmap;
        this.date = date;
        this.location = location;
    }

    protected DocumentsUtil(Parcel in) {
        uri = in.readString();
        size = in.readInt();
        date = in.readLong();
        name = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        location = in.readString();
    }

    public static final Creator<DocumentsUtil> CREATOR = new Creator<DocumentsUtil>() {
        @Override
        public DocumentsUtil createFromParcel(Parcel in) {
            return new DocumentsUtil(in);
        }

        @Override
        public DocumentsUtil[] newArray(int size) {
            return new DocumentsUtil[size];
        }
    };

    // Getters to get the values
    public Bitmap getBitmap(){
        return bitmap;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeInt(size);
        dest.writeLong(date);
        dest.writeString(name);
        dest.writeParcelable(bitmap, flags);
        dest.writeString(location);
    }

}
