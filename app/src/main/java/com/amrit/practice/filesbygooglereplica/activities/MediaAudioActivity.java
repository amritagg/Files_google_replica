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
import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;
import com.amrit.practice.filesbygooglereplica.adapters.MediaAudioAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaAudioLoader;
import com.amrit.practice.filesbygooglereplica.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MediaAudioActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<AudioUtil>> {

    private final String LOG_TAG = MediaAudioActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private static final int LoaderManger_ID = 15;
    private ArrayList<AudioUtil> audioUtil;
    private MediaAudioAdapter audioAdapter;
    private boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        initialiseRecyclerView();
        showAudio();
    }

    private void showAudio() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<AudioUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaAudioLoader(getApplicationContext());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<AudioUtil>> loader, ArrayList<AudioUtil> data) {

        Log.e(LOG_TAG, "Done onLoadFinished");
        progressBar.setVisibility(View.GONE);
        audioUtil.addAll(data);
        recyclerView.setVisibility(View.VISIBLE);
        audioAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<AudioUtil>> loader) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_menu, menu);
        return true;
    }

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort_date) {
            ArrayList<AudioUtil> temp = new ArrayList<>(audioUtil);
            audioUtil.sort(Comparator.comparingLong(AudioUtil::getDate));

            if (audioUtil.equals(temp)) Collections.reverse(audioUtil);
            audioAdapter.notifyDataSetChanged();

            return true;
        } else if (item.getItemId() == R.id.sort_name) {
            ArrayList<AudioUtil> temp = new ArrayList<>(audioUtil);
            audioUtil.sort(Comparator.comparing(AudioUtil::getName));

            if (audioUtil.equals(temp)) Collections.reverse(audioUtil);
            audioAdapter.notifyDataSetChanged();

            return true;
        } else if (item.getItemId() == R.id.sort_size) {
            ArrayList<AudioUtil> temp = new ArrayList<>(audioUtil);
            audioUtil.sort(Comparator.comparingInt(AudioUtil::getSize));

            if (audioUtil.equals(temp)) Collections.reverse(audioUtil);
            audioAdapter.notifyDataSetChanged();

            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if(item.getItemId() == R.id.list_grid){
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
            audioAdapter = new MediaAudioAdapter(this, audioUtil, isList);
            recyclerView.setAdapter(audioAdapter);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseRecyclerView(){
        progressBar = findViewById(R.id.media_progress_bar);
        recyclerView = findViewById(R.id.media_recycler_view);
        recyclerView.setVisibility(View.GONE);
        audioUtil = new ArrayList<>();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager;

        if (isList) layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        else layoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        audioAdapter = new MediaAudioAdapter(this, audioUtil, isList);
        recyclerView.setAdapter(audioAdapter);
    }

}