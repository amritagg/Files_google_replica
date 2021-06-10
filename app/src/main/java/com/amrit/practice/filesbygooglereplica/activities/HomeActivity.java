package com.amrit.practice.filesbygooglereplica.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amrit.practice.filesbygooglereplica.R;

public class HomeActivity extends AppCompatActivity {

    // request code for permission
    private final int REQUEST_PERMISSION_CODE = 5;

    LinearLayout download, images, videos, music, documents, internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionStatus();
    }

    private void show(){
        download = findViewById(R.id.download_layout);
        images = findViewById(R.id.image_layout);
        videos = findViewById(R.id.video_layout);
        music = findViewById(R.id.audio_layout);
        documents = findViewById(R.id.document_layout);
        internalStorage = findViewById(R.id.internal_storage_layout);

        internalStorage.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, InternalStorageActivity.class);
            startActivity(intent);
        });

        download.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MediaDownloadActivity.class);
            startActivity(intent);
        });

        images.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MediaImageActivity.class);
            startActivity(intent);
        });

        videos.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MediaVideoActivity.class);
            startActivity(intent);
        });

        music.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MediaAudioActivity.class);
            startActivity(intent);
        });

        documents.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MediaDocumentsActivity.class);
            startActivity(intent);
        });

    }

    //    list of permissions required
    String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //  Method for permission handling
    private void checkPermissionStatus() {

//      check weather the permission is given or not
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            show();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                if not permission is denied than show the message to user showing him the benefits of the permission.
                Toast.makeText(this, "Permission is needed to show the Media..", Toast.LENGTH_SHORT).show();
                finish();
            }
//            request permission
            requestPermissions(PERMISSION_LIST, REQUEST_PERMISSION_CODE);
        }
    }

    //    handling the permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                show();
            } else{
                Toast.makeText(this,"Permissions not granted!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}