package com.amrit.practice.filesbygooglereplica.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Size;
import android.widget.ImageView;
import android.widget.TextView;
import com.amrit.practice.filesbygooglereplica.R;
import java.io.IOException;

public class AudioInfoActivity extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_info);

        ImageView image = findViewById(R.id.info_activity_audio);
        TextView name = findViewById(R.id.audio_name);
        TextView location = findViewById(R.id.audio_location);
        TextView time = findViewById(R.id.audio_date);
        TextView size = findViewById(R.id.info_audio_size);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("INFO");

        String uri_string = bundle.getString("uri");
        String name_string = bundle.getString("name");
        String location_string = bundle.getString("location");
        String time_string = bundle.getString("time");
        String size_string = bundle.getString("size");

        try {
            Bitmap bitmap = getContentResolver().loadThumbnail(
                    Uri.parse(uri_string),
                    new Size(200, 200),
                    null
            );
            image.setImageBitmap(bitmap);
        } catch (IOException e) {
            image.setImageDrawable(getDrawable(R.drawable.ic_baseline_audiotrack_24));
        }

        name.setText(name_string);
        location.setText(location_string + name_string);
        time.setText(time_string);
        size.setText(size_string);

    }
}