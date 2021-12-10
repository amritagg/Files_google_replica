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
import android.widget.TextView;
import android.widget.Toast;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.adapters.InternalStorageAdapter;
import com.amrit.practice.filesbygooglereplica.adapters.MediaAudioAdapter;
import com.amrit.practice.filesbygooglereplica.loaders.InternalStorageLoader;
import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;
import com.amrit.practice.filesbygooglereplica.utils.ImageUtil;
import com.amrit.practice.filesbygooglereplica.utils.InternalStorageUtil;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class InternalStorageActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<InternalStorageUtil>> {

    private static final String LOG_TAG = InternalStorageActivity.class.getSimpleName();
    private ListView listView;
    private GridView gridView;
    private ProgressBar progressBar;
    private TextView nothing;
    private static final int LOADER_ID = 35;
    private InternalStorageAdapter internalStorageAdapter;
    private ArrayList<InternalStorageUtil> utils;
    private File visibleFileListParent;
    private LoaderManager loaderManager;
    private String HEAD_FILE;
    private static boolean isList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);

        listView = findViewById(R.id.list_view_internal_Storage);
        gridView = findViewById(R.id.grid_view_internal_storage);
        progressBar = findViewById(R.id.internal_storage_progress_bar);
        nothing = findViewById(R.id.nothing_internal_storage);

        progressBar.setVisibility(View.VISIBLE);
        visibleFileListParent = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(
                        getExternalFilesDir(null) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica/file
                        .getParentFile()) // /storage/emulated/0/Android/data/com.amrit.practice.filesByGoogleReplica
                        .getParentFile()) // /storage/emulated/0/Android/data
                        .getParentFile()) // /storage/emulated/0/Android
                        .getParentFile(); // /storage/emulated/0

        assert visibleFileListParent != null;
        HEAD_FILE = visibleFileListParent.getAbsolutePath();

        loaderManager = LoaderManager.getInstance(this);
        showFiles(0);

        listView.setOnItemClickListener(onItemClickListener);
        gridView.setOnItemClickListener(onItemClickListener);
    }

    private void showFiles(int i){
        if(i == 0) loaderManager.initLoader(LOADER_ID, null, this);
        else loaderManager.restartLoader(LOADER_ID, null, this);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
            if(utils.get(pos).isFolder()){
                visibleFileListParent = new File(utils.get(pos).getUri());
                showFiles(1);
            }else {
                String name = utils.get(pos).getName();

                if(AudioUtil.isAudio(name)){
                    startAudioPlayer(pos);
                }else if(VideoUtil.isVideo(name)){
                    startVideoPlayer(pos);
                }else if(ImageUtil.isImage(name)){
                    startImageShower(pos);
                }else{
                    Toast.makeText(getApplicationContext(),"Can't find app to open the app", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void startImageShower(int pos) {

        String name = utils.get(pos).getName();

        int total = -1;
        int current = -1;

        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < utils.size(); i++){
            String n = utils.get(i).getName();
            if(ImageUtil.isImage(n)){
                total++;
                String uri = utils.get(i).getUri();
                list.add(uri);

                if(n.equals(name)) current = total;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("uris", list);
        bundle.putInt("current_position", current);

//        Intent intent = new Intent(InternalStorageActivity.this, ShowImageActivity.class);
//        intent.putExtra("from_internal", bundle);
//        startActivity(intent);
    }

    private void startAudioPlayer(int pos){

        String name = utils.get(pos).getName();

        int total = -1;
        int current = -1;

        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < utils.size(); i++){
            String n = utils.get(i).getName();
            if(AudioUtil.isAudio(n)){
                total++;
                String uri = utils.get(i).getUri();
                list.add(uri);

                if(n.equals(name)) current = total;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putInt("position", current);
        bundle.putStringArrayList("uris", list);

        Intent intent = new Intent(InternalStorageActivity.this, ShowAudioActivity.class);
        intent.putExtra("INFO", bundle);
        startActivity(intent);
    }

    private void startVideoPlayer(int pos){

        String name = utils.get(pos).getName();

        int total = -1;
        int current = -1;

        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i < utils.size(); i++){
            String n = utils.get(i).getName();
            if(VideoUtil.isVideo(n)){
                total++;
                String uri = utils.get(i).getUri();
                list.add(uri);

                if(n.equals(name)) current = total;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("video_uris", list);
        bundle.putInt("current_position", current);

        Intent intent = new Intent(InternalStorageActivity.this, ShowVideoActivity.class);
        intent.putExtra("INFO", bundle);
        startActivity(intent);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<ArrayList<InternalStorageUtil>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        gridView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        return new InternalStorageLoader(getApplicationContext(), visibleFileListParent);
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<ArrayList<InternalStorageUtil>> loader, ArrayList<InternalStorageUtil> data) {

        internalStorageAdapter = new InternalStorageAdapter(this, data, isList);
        progressBar.setVisibility(View.GONE);
        Log.e(LOG_TAG, "Done onLoadFinished");
        utils = data;

        if(utils.size() == 0){
            nothing.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        }else {
            nothing.setVisibility(View.GONE);
            if (isList) {
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(internalStorageAdapter);
            } else {
                gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(internalStorageAdapter);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<ArrayList<InternalStorageUtil>> loader) {
        internalStorageAdapter = null;
    }

    @Override
    public void onBackPressed() {
        if(visibleFileListParent.getAbsolutePath().equals(HEAD_FILE)){
            super.onBackPressed();
        }else {
            visibleFileListParent = visibleFileListParent.getParentFile();
            showFiles(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_menu, menu);
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(visibleFileListParent.getAbsolutePath().equals(HEAD_FILE)){
                finish();
                return true;
            }else {
                visibleFileListParent = visibleFileListParent.getParentFile();
                showFiles(1);
                return true;
            }
        }else if(item.getItemId() == R.id.list_grid){
            isList = !isList;
            internalStorageAdapter = new InternalStorageAdapter(this, utils, isList);

            if(isList) {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_grid_24));
                listView.setAdapter(internalStorageAdapter);
                listView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            } else {
                item.setIcon(getDrawable(R.drawable.ic_baseline_view_list_24));
                gridView.setAdapter(internalStorageAdapter);
                listView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
