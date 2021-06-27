package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.amrit.practice.filesbygooglereplica.adapters.MediaAudioAdapter;
import com.amrit.practice.filesbygooglereplica.adapters.MediaVideoAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaVideoLoader;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;

public class MediaVideoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<VideoUtil>> {

    private final String LOG_TAG = MediaVideoActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 20;
    private ArrayList<VideoUtil> videoUtils;
    private static boolean isList = false;
    private MediaVideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        progressBar = findViewById(R.id.media_progress_bar);
        gridView = findViewById(R.id.media_grid_view);
        listView = findViewById(R.id.media_list_view);

        showVideos();

        gridView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemClickListener(onItemClickListener);

    }

    private final AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        Intent intent = new Intent(MediaVideoActivity.this, ShowVideoActivity.class);
        ArrayList<String> videoUri = new ArrayList<>();
        ArrayList<String> videoName = new ArrayList<>();
        ArrayList<Integer> videoSize = new ArrayList<>();
        ArrayList<String> videoLocation = new ArrayList<>();
        ArrayList<Long> videoDate = new ArrayList<>();

        for(VideoUtil util: videoUtils) {
            videoUri.add(util.getUri());
            videoName.add(util.getName());
            videoSize.add(util.getSize());
            videoDate.add(util.getDate());
            videoLocation.add(util.getLocation());
        }

        long[] video_date = new long[videoDate.size()];

        for(int j = 0; j < videoDate.size(); j++) video_date[j] = videoDate.get(j);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("video_uris", videoUri);
        bundle.putStringArrayList("video_name", videoName);
        bundle.putIntegerArrayList("video_size", videoSize);
        bundle.putStringArrayList("video_location", videoLocation);
        bundle.putLongArray("video_date", video_date);

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

        videoAdapter = new MediaVideoAdapter(data, getApplicationContext(), isList);
        videoUtils = data;
        Log.e(LOG_TAG, "Done onLoadFinished");
        progressBar.setVisibility(View.GONE);

        if(isList){
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(videoAdapter);
        }else {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(videoAdapter);
        }
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<VideoUtil>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_menu, menu);
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.list_grid){
            isList = !isList;
            videoAdapter = new MediaVideoAdapter(videoUtils, getApplicationContext(), isList);
            if(isList) {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_grid_24));
                listView.setAdapter(videoAdapter);
                listView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            } else {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_list_24));
                gridView.setAdapter(videoAdapter);
                listView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }
            return true;
        } else if (item.getItemId() == R.id.sort_date){
            ArrayList<VideoUtil> temp = new ArrayList<>(videoUtils);

            videoUtils.sort((videoUtil, t1) -> Long.compare(videoUtil.getDate(), t1.getDate()));

            if(videoUtils.equals(temp)) Collections.reverse(videoUtils);

            videoAdapter = new MediaVideoAdapter(videoUtils, getApplicationContext(), isList);

            if(isList) listView.setAdapter(videoAdapter);
            else gridView.setAdapter(videoAdapter);

            return true;
        } else if (item.getItemId() == R.id.sort_name){
            ArrayList<VideoUtil> temp = new ArrayList<>(videoUtils);
            videoUtils.sort((videoUtils, t1) -> videoUtils.getName().compareTo(t1.getName()));

            if(videoUtils.equals(temp)) Collections.reverse(videoUtils);

            videoAdapter = new MediaVideoAdapter(videoUtils, getApplicationContext(), isList);

            if(isList) listView.setAdapter(videoAdapter);
            else gridView.setAdapter(videoAdapter);

            return true;
        } else if(item.getItemId() == R.id.sort_size){
            ArrayList<VideoUtil> temp = new ArrayList<>(videoUtils);

            videoUtils.sort((videoUtils, t1) -> videoUtils.getSize() - t1.getSize());
            if(videoUtils.equals(temp)) Collections.reverse(videoUtils);

            videoAdapter = new MediaVideoAdapter(videoUtils, getApplicationContext(), isList);

            if(isList) listView.setAdapter(videoAdapter);
            else gridView.setAdapter(videoAdapter);

            return true;
        } else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}