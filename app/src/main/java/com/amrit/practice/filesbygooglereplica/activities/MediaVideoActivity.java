package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.amrit.practice.filesbygooglereplica.adapters.MediaAudioAdapter;
import com.amrit.practice.filesbygooglereplica.adapters.MediaVideoAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaVideoLoader;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MediaVideoActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<VideoUtil>> {

    private final String LOG_TAG = MediaVideoActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private static final int LoaderManger_ID = 20;
    private ArrayList<VideoUtil> videoUtils;
    private MediaVideoAdapter videoAdapter;
    private boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        initialiseRecyclerView();
        showVideos();
    }

    private void showVideos(){
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<VideoUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaVideoLoader(getApplicationContext());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<VideoUtil>> loader, ArrayList<VideoUtil> data) {

        Log.e(LOG_TAG, "Done onLoadFinished");
        videoUtils.addAll(data);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<VideoUtil>> loader) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_menu, menu);
        return true;
    }

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sort_date){
            ArrayList<VideoUtil> temp = new ArrayList<>(videoUtils);
            videoUtils.sort(Comparator.comparingLong(VideoUtil::getDate));

            if(videoUtils.equals(temp)) Collections.reverse(videoUtils);
            videoAdapter.notifyDataSetChanged();

            return true;
        }else if (item.getItemId() == R.id.sort_name){
            ArrayList<VideoUtil> temp = new ArrayList<>(videoUtils);
            videoUtils.sort(Comparator.comparing(VideoUtil::getName));

            if(videoUtils.equals(temp)) Collections.reverse(videoUtils);
            videoAdapter.notifyDataSetChanged();

            return true;
        }else if(item.getItemId() == R.id.sort_size){
            ArrayList<VideoUtil> temp = new ArrayList<>(videoUtils);
            videoUtils.sort(Comparator.comparingInt(VideoUtil::getSize));

            if(videoUtils.equals(temp)) Collections.reverse(videoUtils);
            videoAdapter.notifyDataSetChanged();

            return true;
        } else if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }else if(item.getItemId() == R.id.list_grid){
            RecyclerView.LayoutManager layoutManager;

            if(isList){
                layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_list_24));
            }else{
                layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_grid_24));
            }
            recyclerView.setLayoutManager(layoutManager);
            isList = !isList;
            videoAdapter = new MediaVideoAdapter(this, videoUtils, isList);
            recyclerView.setAdapter(videoAdapter);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseRecyclerView() {
        progressBar = findViewById(R.id.media_progress_bar);
        recyclerView = findViewById(R.id.media_recycler_view);
        recyclerView.setVisibility(View.GONE);
        videoUtils = new ArrayList<>();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager;
        if(isList){
            layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        }else{
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        videoAdapter = new MediaVideoAdapter(this, videoUtils, isList);
        recyclerView.setAdapter(videoAdapter);
    }

}