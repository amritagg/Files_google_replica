package com.amrit.practice.filesbygooglereplica.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amrit.practice.filesbygooglereplica.R;
import com.bumptech.glide.Glide;

public class ImageInfoActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);

        ImageView image = findViewById(R.id.info_activity_image);
        TextView name = findViewById(R.id.image_name);
        TextView location = findViewById(R.id.image_location);
        TextView time = findViewById(R.id.image_date);
        TextView size = findViewById(R.id.info_image_size);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("INFO");

        String uri_string = bundle.getString("uri");
        String name_string = bundle.getString("name");
        String location_string = bundle.getString("location");
        String time_string = bundle.getString("time");
        String size_string = bundle.getString("size");

        Glide.with(this)
                .load(Uri.parse(uri_string))
                .into(image);

        name.setText(name_string);
        location.setText(location_string + name_string);
        time.setText(time_string);
        size.setText(size_string);
    }
}