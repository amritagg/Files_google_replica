package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.activities.InfoActivity;
import com.amrit.practice.filesbygooglereplica.activities.ShowVideoActivity;
import com.amrit.practice.filesbygooglereplica.utils.VideoUtil;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Date;

public class MediaVideoAdapter extends RecyclerView.Adapter<MediaVideoAdapter.MediaVideoViewHolder>{

    private final ArrayList<VideoUtil> videoUtil;
    private final Context context;
    private Toast mToast;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MediaVideoViewHolder holder, int position) {
        holder.nameText.setText(videoUtil.get(position).getName());
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Uri contentUris = Uri.parse(videoUtil.get(position).getUri());
        Glide.with(context)
                .load(contentUris)
                .placeholder(context.getDrawable(R.drawable.ic_baseline_videocam_24))
                .into(holder.imageView);

        if(isList){
            holder.linearLayout.setOnClickListener(view -> startVideoActivity(position));
            setupPopup(holder, position);
        }else{
            holder.relativeLayout.setOnClickListener(view -> startVideoActivity(position));
            holder.sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            int sizeInt = videoUtil.get(position).getSize();
            String sizeString = getSize(sizeInt);
            holder.sizeText.setText(sizeString);
        }

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
        holder.list_more.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), holder.list_more);
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

    @Override
    public int getItemCount() {
        return videoUtil.size();
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

        Intent intent = new Intent(context, InfoActivity.class);
        Bundle bundle = new Bundle();

        Date date = new Date(videoUtil.get(position).getDate()*1000);
        bundle.putString("uri", videoUtil.get(position).getUri());
        bundle.putString("name", videoUtil.get(position).getName());
        bundle.putString("location", videoUtil.get(position).getLocation());
        bundle.putString("time", date.toString());
        bundle.putString("size", getSize(videoUtil.get(position).getSize()));
        intent.putExtra("INFO", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

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

    public static class MediaVideoViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView list_more;
        TextView nameText;
        TextView sizeText;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public MediaVideoViewHolder(@NonNull View itemView, boolean isList) {
            super(itemView);
            if(isList){
                imageView = itemView.findViewById(R.id.list_image_view);
                nameText = itemView.findViewById(R.id.media_name_list);
                linearLayout = itemView.findViewById(R.id.list_linearLayout);
                list_more = itemView.findViewById(R.id.list_more);
            }else {
                imageView = itemView.findViewById(R.id.grid_image_view);
                nameText = itemView.findViewById(R.id.media_name_grid);
                relativeLayout = itemView.findViewById(R.id.grid_layout);
                sizeText = itemView.findViewById(R.id.media_size);
            }
        }
    }

}
