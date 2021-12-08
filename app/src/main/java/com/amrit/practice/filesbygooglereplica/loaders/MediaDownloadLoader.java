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

import com.amrit.practice.filesbygooglereplica.utils.DownloadUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;

public class MediaDownloadLoader extends AsyncTaskLoader<ArrayList<DownloadUtils>> {
    
    private final Context context;

    // constructor for downloadLoader
    public MediaDownloadLoader(@NonNull @NotNull Context context) {
        super(context);
        this.context = context;
    }

    // starting the background task
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    // during background task
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public ArrayList<DownloadUtils> loadInBackground() {
        ArrayList<DownloadUtils> list = new ArrayList<>();

        File file = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
                    context.getExternalFilesDir(null) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica/file
                    .getParentFile()) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica
                    .getParentFile()) // /storage/emulated/0/Android/data
                    .getParentFile()) // /storage/emulated/0/Android
                    .getParentFile(); // /storage/emulated/0

        assert file != null;
        File downloadFile = null;
        for(File f: Objects.requireNonNull(file.listFiles())){
            if(f.getAbsolutePath().equals("/storage/emulated/0/Download")){
                downloadFile = f;
                break;
            }
        }

        if(downloadFile != null){
            for(File f: Objects.requireNonNull(downloadFile.listFiles())){
                String path = f.getAbsolutePath();
                String name = f.getName();
                long size = 0;
                try {
                    size = Files.size(Paths.get(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String contentUri = Uri.parse(new File(path).toString()).toString();
                String date = "";
                try {
                    BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
                    date = attr.lastModifiedTime().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DownloadUtils downloadUtils = new DownloadUtils(contentUri, size, name, date, path);
                list.add(downloadUtils);
            }
        }

//        // details required for download files
//        String[] projection = new String[]{
//                MediaStore.Downloads._ID,
//                MediaStore.Downloads.DISPLAY_NAME,
//                MediaStore.Downloads.SIZE,
//                MediaStore.Downloads.DATE_TAKEN,
//                MediaStore.Downloads.RELATIVE_PATH
//        };
//
//        // starting the cursor
//        try (Cursor cursor = context.getContentResolver().query(
//                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
//                projection,
//                null,
//                null,
//                MediaStore.Downloads.DATE_TAKEN + " DESC"
//        )) {
//            assert cursor != null;
//            int idColumn = cursor.getColumnIndex(MediaStore.Downloads._ID);
//            int sizeColumn = cursor.getColumnIndex(MediaStore.Downloads.SIZE);
//            int nameColumn = cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME);
//            int dateColumn = cursor.getColumnIndex(MediaStore.Downloads.DATE_TAKEN);
//            int locationColumn = cursor.getColumnIndex(MediaStore.Downloads.RELATIVE_PATH);
//
//            while (cursor.moveToNext()) {
//                long id = cursor.getLong(idColumn);
//                int size = cursor.getInt(sizeColumn);
//                String name = cursor.getString(nameColumn);
//                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//                long date = cursor.getInt(dateColumn);
//                String location = cursor.getString(locationColumn);
//
//                // adding the details in list
//                DownloadUtils downloadUtils = new DownloadUtils(contentUri.toString(), size, name, date, location);
//                list.add(downloadUtils);
//
//            }
//        }

        return list;
    }
}
