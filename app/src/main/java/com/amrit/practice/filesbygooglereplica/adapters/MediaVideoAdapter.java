package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MediaVideoAdapter extends BaseAdapter {

    private final ArrayList<VideoUtil> videoUtil;
    private final Context context;
    private Toast mToast;
    private final boolean isList;

    public MediaVideoAdapter(ArrayList<VideoUtil> videoUtil, Context context, boolean isList) {
        this.videoUtil = videoUtil;
        this.context = context;
        this.isList = isList;
    }

    @Override
    public int getCount() {
        return videoUtil.size();
    }

    @Override
    public Object getItem(int i) {
        return getItemId(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if(!isList) convertView = layoutInflater.inflate(R.layout.grid_media, null);
            else convertView = layoutInflater.inflate(R.layout.list_view, null);
        }

        ImageView imageView;

        if(isList) {
            imageView = convertView.findViewById(R.id.list_image_view);
            TextView name = convertView.findViewById(R.id.media_name_list);
            name.setText(videoUtil.get(position).getName());
            setupPopup(convertView, position);
        }else{
            imageView = convertView.findViewById(R.id.grid_image_view);
            TextView sizeText = convertView.findViewById(R.id.media_size);
            sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = videoUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            sizeText.setText(sizeString);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(videoUtil.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_videocam_24))
                .into(imageView);

        return convertView;
    }

    @SuppressLint("NonConstantResourceId")
    private void setupPopup(View convertView, int position) {
        ImageView imageMore = convertView.findViewById(R.id.list_more);
        imageMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), imageMore);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                switch (menuItem.getItemId()){
                    case R.id.share:
                        shareVideo(videoUtil.get(position).getUri());
                        break;
                    case R.id.open_with:
                        videoOpenWith(position);
                        break;
                    case R.id.file_info:
                        infoVideo(position);
                        break;
                    case R.id.delete_permanent:
                        deleteToast();
                        break;
                    case R.id.yes:
                        deleteYesToast(videoUtil.get(position).getUri());
                        break;
                    case R.id.no:
                        deleteNoToast(videoUtil.get(position).getUri());
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
                "Do you really want to delete video", Toast.LENGTH_LONG);
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

    private void videoOpenWith(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(videoUtil.get(position).getUri());
        intent.setDataAndType(uri, "video/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void shareVideo(String uri_string) {

        Uri uri = Uri.parse(uri_string);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("video/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(shareIntent);

    }

    private void infoVideo(int position) {
//        Intent intent = new Intent(context, videoInfoActivity.class);
//        Bundle bundle = new Bundle();
//
//        Date date = new Date(videoUtil.get(position).getDate() * 1000);
//        bundle.putString("uri", videoUtil.get(position).getUri());
//        bundle.putString("name", videoUtil.get(position).getName());
//        bundle.putString("location", videoUtil.get(position).getLocation());
//        bundle.putString("time", date.toString());
//        bundle.putString("size", getSize(videoUtil.get(position).getSize()));
//        intent.putExtra("INFO", bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
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

}
