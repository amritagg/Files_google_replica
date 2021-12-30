package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.activities.InfoActivity;
import com.amrit.practice.filesbygooglereplica.activities.ShowVideoActivity;
import com.amrit.practice.filesbygooglereplica.Models.VideoUtil;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MediaVideoAdapter extends RecyclerView.Adapter<MediaVideoAdapter.MediaVideoViewHolder>{

    private final ArrayList<VideoUtil> videoUtil;
    private final Context context;
    private final boolean isList;

    public MediaVideoAdapter(Context context, ArrayList<VideoUtil> videoUtil, boolean isList) {
        this.videoUtil = videoUtil;
        this.context = context;
        this.isList = isList;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public MediaVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView;
        if(isList){
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_media, null, false);
        }else{
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_media, null, false);
        }
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MediaVideoViewHolder(layoutView, isList);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MediaVideoViewHolder holder, int position) {
        if(isList){
            Date date = new Date(videoUtil.get(position).getDate()*1000);
            Log.e("MediaVideoAdapter", videoUtil.get(position).getDate() + " " + position);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
            String dateText = df2.format(date);
            String size = getSize(videoUtil.get(position).getSize());
            setupPopup(holder, position);
            holder.linear.setOnClickListener(view -> startVideoActivity(position));
            holder.mediaDate.setText(dateText + ", " + size);
        }else{
            holder.mediaName.setShadowLayer(2, 1, 1, Color.BLACK);
            holder.mediaSize.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = videoUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            holder.mediaSize.setText(sizeString);
            holder.relative.setOnClickListener(view -> startVideoActivity(position));
        }

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri contentUris = Uri.parse(videoUtil.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_image_24))
                .into(holder.imageView);
        holder.mediaName.setText(videoUtil.get(position).getName());

    }

    private void startVideoActivity(int position) {
        Intent intent = new Intent(context, ShowVideoActivity.class);
        ArrayList<String> videoUri = new ArrayList<>();
        ArrayList<String> videoName = new ArrayList<>();
        ArrayList<Integer> videoSize = new ArrayList<>();
        ArrayList<String> videoLocation = new ArrayList<>();
        ArrayList<Long> videoDate = new ArrayList<>();

        for(VideoUtil util: videoUtil) {
            videoUri.add(util.getUri());
            videoName.add(util.getName());
            videoSize.add(util.getSize());
            videoDate.add(util.getDate());
            videoLocation.add(util.getLocation());
        }

        long[] video_date = new long[videoDate.size()];

        for(int j = 0; j < videoDate.size(); j++) video_date[j] = videoDate.get(j);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("video_uris", videoUri);
        bundle.putStringArrayList("video_name", videoName);
        bundle.putIntegerArrayList("video_size", videoSize);
        bundle.putStringArrayList("video_location", videoLocation);
        bundle.putLongArray("video_date", video_date);

        bundle.putInt("current_position", position);
        intent.putExtra("INFO", bundle);
        context.startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    private void setupPopup(MediaVideoViewHolder holder, int position) {
        holder.listMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), holder.listMore);
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
                        deleteToast(videoUtil.get(position).getUri());
                        break;
                    default:
                        return false;
                }
                return true;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return videoUtil.size();
    }

    private void deleteToast(String uri){
        new AlertDialog.Builder(context)
                .setTitle("Delete Video")
                .setMessage("Do You really want to delete the video")
                .setPositiveButton("YES!!",
                        (dialog, which) -> Toast.makeText(context, "The file " + uri + " is deleted", Toast.LENGTH_SHORT).show())
                .setNegativeButton("NO!",
                        (dialog, which) -> Toast.makeText(context, "The file " + uri + " will not be deleted", Toast.LENGTH_SHORT).show())
                .create()
                .show();
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

        Intent intent = new Intent(context, InfoActivity.class);
        Bundle bundle = new Bundle();

        Date date = new Date(videoUtil.get(position).getDate()*1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy, hh:mm:aa");
        String dateText = df2.format(date);
        bundle.putString("uri", videoUtil.get(position).getUri());
        bundle.putString("name", videoUtil.get(position).getName());
        bundle.putString("location", videoUtil.get(position).getLocation());
        bundle.putString("time", dateText);
        bundle.putString("size", getSize(videoUtil.get(position).getSize()));
        bundle.putInt("isMedia", 2);
        intent.putExtra("INFO", bundle);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

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

    public static class MediaVideoViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView listMore;
        TextView mediaName;
        TextView mediaSize;
        TextView mediaDate;
        LinearLayout linear;
        RelativeLayout relative;

        public MediaVideoViewHolder(@NonNull View itemView, boolean isList) {
            super(itemView);
            if(isList){
                imageView = itemView.findViewById(R.id.list_image_view);
                mediaName = itemView.findViewById(R.id.media_name_list);
                linear = itemView.findViewById(R.id.list_linearLayout);
                listMore = itemView.findViewById(R.id.list_more);
                mediaDate = itemView.findViewById(R.id.media_size_date_list);
            }else{
                imageView = itemView.findViewById(R.id.grid_image_view);
                mediaName = itemView.findViewById(R.id.media_name_grid);
                mediaSize = itemView.findViewById(R.id.media_size);
                relative = itemView.findViewById(R.id.grid_layout);
            }
        }
    }

}
