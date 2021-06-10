package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;

import java.util.ArrayList;

public class MediaAudioAdapter extends BaseAdapter {

    public final String LOG_TAG = MediaAudioAdapter.class.getSimpleName();

    boolean isList = false;
    private final ArrayList<AudioUtil> audioUtils;
    private final Context context;

    public MediaAudioAdapter(Context context, ArrayList<AudioUtil> audioUtils) {
        this.context = context;
        this.audioUtils = audioUtils;
    }

    @Override
    public int getCount() {
        return audioUtils.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_audio, null);
            else convertView = layoutInflater.inflate(R.layout.list_audio, null);
        }

        ImageView imageView;
        TextView fileName;

        if(isList){
            imageView = convertView.findViewById(R.id.audio_list_imaeg_view);
            fileName = convertView.findViewById(R.id.audio_name_list);
        }else {
            imageView = convertView.findViewById(R.id.audio_grid_image_view);
            fileName = convertView.findViewById(R.id.audio_name_grid);
            TextView size = convertView.findViewById(R.id.audio_size);
            size.setShadowLayer(2, 1, 1, Color.BLACK);
            size.setText(audioUtils.get(position).getSize() + "");
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bitmap = audioUtils.get(position).getBitmap();

        if(bitmap != null) imageView.setImageBitmap(bitmap);
        else imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_audiotrack_24));

        String name = audioUtils.get(position).getName();

        if(name.length() > 0) fileName.setText(name);
        else fileName.setText("Residue file you must delete it");

        fileName.setShadowLayer(2, 1, 1, Color.BLACK);

        return convertView;
    }
}
