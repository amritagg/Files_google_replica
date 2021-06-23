package com.amrit.practice.filesbygooglereplica.loaders;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.amrit.practice.filesbygooglereplica.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaImageLoader extends AsyncTaskLoader<ArrayList<ImageUtil>>{

    private final Context context;
    private final String LOG_TAG = MediaImageLoader.class.getSimpleName();

    public MediaImageLoader(@NonNull @NotNull Context context) {
        super(context);
        this.context = context;
        Log.e(LOG_TAG, "background called");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.e(LOG_TAG, "Force loading");
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<ImageUtil> loadInBackground() {

        Log.e(LOG_TAG, "background started");

        ArrayList<ImageUtil> list = new ArrayList<>();

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.RELATIVE_PATH
        };

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC"
        )) {
            assert cursor != null;
            int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int locationColumn = cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                int size = cursor.getInt(sizeColumn);
                String name = cursor.getString(nameColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                long date = cursor.getLong(dateColumn);
                String location = cursor.getString(locationColumn);

               ImageUtil imageUtil = new ImageUtil(contentUri.toString(), size, name, date, location);
               list.add(imageUtil);

            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Can't Load Files");
        }

        Log.e(LOG_TAG, "Back process Done");

        return list;
    }
}
