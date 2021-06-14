package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.adapters.MediaImageAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaImageLoader;
import com.amrit.practice.filesbygooglereplica.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaImageActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<ImageUtil>> {

    private final String LOG_TAG = MediaImageActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 10;
    private ArrayList<ImageUtil> imageUtils;
    private static final boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        progressBar = findViewById(R.id.media_progress_bar);
        gridView = findViewById(R.id.media_grid_view);
        listView = findViewById(R.id.media_list_view);

        startGallery();

        gridView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemClickListener(onItemClickListener);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        Intent intent = new Intent(MediaImageActivity.this, ShowImageActivity.class);
        ArrayList<String> imageUris = new ArrayList<>();
        ArrayList<String> imageName = new ArrayList<>();
        ArrayList<Integer> imageSize = new ArrayList<>();
        ArrayList<Long> imageDate = new ArrayList<>();
        ArrayList<String> imageLocation = new ArrayList<>();

        for(ImageUtil util: imageUtils) {
            imageUris.add(util.getUri());
            imageName.add(util.getName());
            imageSize.add(util.getSize());
            imageDate.add(util.getDate());
            imageLocation.add(util.getLocation());
        }

        long[] longList = new long[imageDate.size()];

        for(int j = 0; j < imageDate.size(); j++) longList[j] = imageDate.get(j);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("image_uris", imageUris);
        bundle.putStringArrayList("image_name", imageName);
        bundle.putIntegerArrayList("image_size", imageSize);
        bundle.putLongArray("image_date", longList);
        bundle.putStringArrayList("image_location", imageLocation);

        bundle.putInt("current_position", i);
        intent.putExtra("bundle", bundle);

        startActivity(intent);
    };

    public void startGallery() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<ImageUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaImageLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<ImageUtil>> loader, ArrayList<ImageUtil> data) {

        MediaImageAdapter imageAdapter = new MediaImageAdapter(getApplicationContext(), data, isList);
        Log.e(LOG_TAG, "Done onLoadFinished");
        progressBar.setVisibility(View.GONE);
        imageUtils = data;

        if(isList){
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(imageAdapter);
        }else {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(imageAdapter);
        }

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<ImageUtil>> loader) { }
}