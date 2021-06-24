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

import com.amrit.practice.filesbygooglereplica.adapters.MediaImageAdapter;
import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;
import com.amrit.practice.filesbygooglereplica.adapters.MediaAudioAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaAudioLoader;
import com.amrit.practice.filesbygooglereplica.R;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MediaAudioActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<AudioUtil>> {

    private final String LOG_TAG = MediaAudioActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 15;
    private ArrayList<AudioUtil> audioUtil;
    private static boolean isList = false;
    private MediaAudioAdapter audioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        progressBar = findViewById(R.id.media_progress_bar);
        gridView = findViewById(R.id.media_grid_view);
        listView = findViewById(R.id.media_list_view);

        showAudio();

        gridView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemClickListener(onItemClickListener);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        Intent intent = new Intent(MediaAudioActivity.this, ShowAudioActivity.class);
        ArrayList<String> audioUris = new ArrayList<>();
        ArrayList<String> audioNames = new ArrayList<>();
        ArrayList<Integer> audioSize = new ArrayList<>();
        ArrayList<String> audioLocation = new ArrayList<>();
        ArrayList<Long> audioDate = new ArrayList<>();

        for(AudioUtil util: audioUtil) {
            audioUris.add(util.getUri());
            audioNames.add(util.getName());
            audioSize.add(util.getSize());
            audioLocation.add(util.getLocation());
            audioDate.add(util.getDate());
        }

        long[] audio_date = new long[audioDate.size()];

        for(int j = 0; j < audioDate.size(); j++) audio_date[j] = audioDate.get(j);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("uris", audioUris);
        bundle.putStringArrayList("location", audioLocation);
        bundle.putStringArrayList("names", audioNames);
        bundle.putIntegerArrayList("size", audioSize);
        bundle.putLongArray("dates", audio_date);

        bundle.putInt("position", i);
        intent.putExtra("INFO", bundle);

        startActivity(intent);
    };

    private void showAudio() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LoaderManger_ID, null, this);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<AudioUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG, "started onLoadFinished");
        return new MediaAudioLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<AudioUtil>> loader, ArrayList<AudioUtil> data) {

        progressBar.setVisibility(View.GONE);
        Log.e(LOG_TAG, "Done onLoadFinished");
        audioAdapter = new MediaAudioAdapter(getApplicationContext(), data, isList);
        audioUtil = data;

        if(isList){
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(audioAdapter);
        }else {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(audioAdapter);
        }

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<AudioUtil>> loader) {

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
            audioAdapter = new MediaAudioAdapter(getApplicationContext(), audioUtil, isList);
            if(isList) {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_grid_24));
                listView.setAdapter(audioAdapter);
                listView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            } else {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_list_24));
                gridView.setAdapter(audioAdapter);
                listView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }
            return true;
        }else return super.onOptionsItemSelected(item);
    }
}