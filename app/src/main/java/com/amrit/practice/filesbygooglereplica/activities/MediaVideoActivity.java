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

import com.amrit.practice.filesbygooglereplica.adapters.MediaVideoAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaVideoLoader;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaVideoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<VideoUtil>> {

    private final String LOG_TAG = MediaVideoActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 20;
    private ArrayList<VideoUtil> videoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_video);

        progressBar = findViewById(R.id.media_video_progress_bar);
        gridView = findViewById(R.id.grid_view_video);
        listView = findViewById(R.id.list_view_video);

        showVideos();

        gridView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemClickListener(onItemClickListener);

    }

    private final AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        Intent intent = new Intent(MediaVideoActivity.this, ShowVideoActivity.class);
        ArrayList<String> videoUri = new ArrayList<>();
        ArrayList<String> videoName = new ArrayList<>();
        ArrayList<Integer> videoSize = new ArrayList<>();

        for(VideoUtil util: videoUtils) {
            videoUri.add(util.getUri());
            videoName.add(util.getName());
            videoSize.add(util.getSize());
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("video_uris", videoUri);
        bundle.putStringArrayList("video_name", videoName);
        bundle.putIntegerArrayList("video_size", videoSize);

        bundle.putInt("current_position", i);
        intent.putExtra("INFO", bundle);
        startActivity(intent);
    };

    private void showVideos(){
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<VideoUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaVideoLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<VideoUtil>> loader, ArrayList<VideoUtil> data) {
//        gridView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Log.e(LOG_TAG, "Done onLoadFinished");
        MediaVideoAdapter videoAdapter = new MediaVideoAdapter(data, getApplicationContext());
//        gridView.setAdapter(videoAdapter);
        videoUtils = data;
        listView.setAdapter(videoAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<VideoUtil>> loader) {

    }
}