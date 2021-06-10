package com.amrit.practice.filesbygooglereplica.loaders;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.amrit.practice.filesbygooglereplica.utils.DownloadUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaDownloadLoader extends AsyncTaskLoader<ArrayList<DownloadUtils>> {
    
    private final Context context;
    
    public MediaDownloadLoader(@NonNull @NotNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public ArrayList<DownloadUtils> loadInBackground() {
        ArrayList<DownloadUtils> list = new ArrayList<>();
        
        String[] projection = new String[]{
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DISPLAY_NAME,
                MediaStore.Downloads.SIZE,
                MediaStore.Downloads.DATE_TAKEN,
                MediaStore.Downloads.RELATIVE_PATH
        };

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Downloads.DATE_TAKEN + " DESC"
        )) {
            assert cursor != null;
            int idColumn = cursor.getColumnIndex(MediaStore.Downloads._ID);
            int sizeColumn = cursor.getColumnIndex(MediaStore.Downloads.SIZE);
            int nameColumn = cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME);
            int dateColumn = cursor.getColumnIndex(MediaStore.Downloads.DATE_TAKEN);
            int locationColumn = cursor.getColumnIndex(MediaStore.Downloads.RELATIVE_PATH);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                int size = cursor.getInt(sizeColumn);
                String name = cursor.getString(nameColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                long date = cursor.getInt(dateColumn);
                String location = cursor.getString(locationColumn);

                DownloadUtils downloadUtils = new DownloadUtils(contentUri.toString(), size, name, date, location);
                list.add(downloadUtils);

            }
        }
        
        return list;
    }
}
