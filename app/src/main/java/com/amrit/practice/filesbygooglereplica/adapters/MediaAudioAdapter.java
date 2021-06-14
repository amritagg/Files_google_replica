package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaAudioAdapter extends BaseAdapter {

    public final String LOG_TAG = MediaAudioAdapter.class.getSimpleName();

    private final boolean isList;
    private final ArrayList<AudioUtil> audioUtils;
    private final Context context;
    private Toast mToast;

    public MediaAudioAdapter(Context context, ArrayList<AudioUtil> audioUtils, boolean isList) {
        this.context = context;
        this.audioUtils = audioUtils;
        this.isList = isList;
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
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_media, null);
            else convertView = layoutInflater.inflate(R.layout.list_media, null);
        }

        ImageView imageView;
        TextView fileName;

        if(isList){
            imageView = convertView.findViewById(R.id.list_image_view);
            fileName = convertView.findViewById(R.id.media_name_list);
            setupPopUp(convertView, position);
        }else {
            imageView = convertView.findViewById(R.id.grid_image_view);
            fileName = convertView.findViewById(R.id.media_name_grid);
            TextView size = convertView.findViewById(R.id.media_size);

            String size_string = getSize(audioUtils.get(position).getSize());
            size.setShadowLayer(2, 1, 1, Color.BLACK);
            size.setText(size_string);
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

    @NotNull
    private String getSize(int size){
        float sizeFloat = (float) size / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);

        if(sizeFloat < 1024) return sizeFloat + "KB";

        sizeFloat = sizeFloat / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);
        return sizeFloat + "MB";
    }

    @SuppressLint("NonConstantResourceId")
    private void setupPopUp(View convertView, int position) {

        ImageView imageView = convertView.findViewById(R.id.list_more);
        imageView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), imageView);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.share:
                        shareAudio(audioUtils.get(position).getUri());
                        break;
                    case R.id.open_with:
                        audioOpenWith(position);
                        break;
                    case R.id.file_info:
                        infoAudio(position);
                        break;
                    case R.id.delete_permanent:
                        deleteToast();
                        break;
                    case R.id.yes:
                        deleteYesToast(audioUtils.get(position).getUri());
                        break;
                    case R.id.no:
                        deleteNoToast(audioUtils.get(position).getUri());
                        break;
                    default:
                        return false;
                }
                return true;
            });
            popupMenu.show();
        });

    }

    private void deleteToast(){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,
                "Do you really want to delete audio", Toast.LENGTH_LONG);
        mToast.show();
    }

    private void deleteYesToast(String uri){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,
                "The file " + uri + " is deleted", Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void deleteNoToast(String uri){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,
                "The file " + uri + " will not be deleted", Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void audioOpenWith(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(audioUtils.get(position).getUri());
        intent.setDataAndType(uri, "audio/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void shareAudio(String uri_string) {

        Uri uri = Uri.parse(uri_string);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("audio/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(shareIntent);

    }

    private void infoAudio(int position) {
//        Intent intent = new Intent(context, audioInfoActivity.class);
//        Bundle bundle = new Bundle();
//
//        Date date = new Date(audioUtils.get(position).getDate() * 1000);
//        bundle.putString("uri", audioUtil.get(position).getUri());
//        bundle.putString("name", audioUtil.get(position).getName());
//        bundle.putString("location", audioUtil.get(position).getLocation());
//        bundle.putString("time", date.toString());
//        bundle.putString("size", getSize(audioUtil.get(position).getSize()));
//        intent.putExtra("INFO", bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

}
