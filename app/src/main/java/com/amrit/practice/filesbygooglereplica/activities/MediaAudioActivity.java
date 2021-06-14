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

import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;
import com.amrit.practice.filesbygooglereplica.adapters.MediaAudioAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.MediaAudioLoader;
import com.amrit.practice.filesbygooglereplica.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaAudioActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<AudioUtil>> {

    private final String LOG_TAG = MediaAudioActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private GridView gridView;
    private ListView listView;
    private static final int LoaderManger_ID = 15;
    private ArrayList<AudioUtil> audioUtil;
    private final static boolean isList = false;

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
        ArrayList<String> imageUris = new ArrayList<>();

        for(AudioUtil util: audioUtil) {
            imageUris.add(util.getUri());
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("uris", imageUris);

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
        MediaAudioAdapter audioAdapter = new MediaAudioAdapter(getApplicationContext(), data, isList);
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
}