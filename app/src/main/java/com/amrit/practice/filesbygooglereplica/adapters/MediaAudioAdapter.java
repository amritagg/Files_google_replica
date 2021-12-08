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
import com.amrit.practice.filesbygooglereplica.activities.ShowAudioActivity;
import com.amrit.practice.filesbygooglereplica.utils.AudioUtil;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Date;

public class MediaAudioAdapter extends RecyclerView.Adapter<MediaAudioAdapter.MediaAudioViewHolder>{

    public final String LOG_TAG = MediaAudioAdapter.class.getSimpleName();
    private final ArrayList<AudioUtil> audioUtils;
    private final Context context;
    private Toast mToast;
    private final boolean isList;

    public MediaAudioAdapter(Context context, ArrayList<AudioUtil> audioUtils, boolean isList) {
        this.context = context;
        this.audioUtils = audioUtils;
        this.isList = isList;
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public MediaAudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView;
        if(isList) layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_media, null, false);
        else layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_media, null, false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MediaAudioViewHolder(layoutView, isList);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull MediaAudioViewHolder holder, int position) {
        if(isList){
            setUpPop(holder, position);
            holder.linearLayout.setOnClickListener(view -> startAudioActivity(position));
        }else{
            holder.relativeLayout.setOnClickListener(view -> startAudioActivity(position));
            String size_string = getSize(audioUtils.get(position).getSize());
            holder.sizeText.setShadowLayer(2, 1, 1, Color.BLACK);
            holder.sizeText.setText(size_string);
        }
        String name = audioUtils.get(position).getName();
        if(name.length() > 0) holder.nameText.setText(name);
        else holder.nameText.setText("Residue file you must delete it");
        holder.nameText.setShadowLayer(2, 1, 1, Color.BLACK);

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bitmap = audioUtils.get(position).getBitmap();

        if(bitmap != null) holder.imageView.setImageBitmap(bitmap);
        else holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_audiotrack_24));

    }

    @Override
    public int getItemCount() {
        return audioUtils.size();
    }

    private void startAudioActivity(int position){
        Intent intent = new Intent(context, ShowAudioActivity.class);
        ArrayList<String> audioUris = new ArrayList<>();
        ArrayList<String> audioNames = new ArrayList<>();
        ArrayList<Integer> audioSize = new ArrayList<>();
        ArrayList<String> audioLocation = new ArrayList<>();
        ArrayList<Long> audioDate = new ArrayList<>();

        for(AudioUtil util: audioUtils) {
            audioUris.add(util.getUri());
            audioNames.add(util.getName());
            audioSize.add(util.getSize());
            audioLocation.add(util.getLocation());
            audioDate.add(util.getDate());
        }

        long[] audio_date = new long[audioDate.size()];

        for(int j = 0; j < audioDate.size(); j++) audio_date[j] = audioDate.get(j);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("uris", audioUris);
        bundle.putStringArrayList("location", audioLocation);
        bundle.putStringArrayList("names", audioNames);
        bundle.putIntegerArrayList("size", audioSize);
        bundle.putLongArray("dates", audio_date);

        bundle.putInt("position", position);
        intent.putExtra("INFO", bundle);

        context.startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpPop(MediaAudioViewHolder holder, int position){
        holder.list_more.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), holder.list_more);
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

    @NotNull
    private String getSize(int size){
        float sizeFloat = (float) size / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);

        if(sizeFloat < 1024) return sizeFloat + "KB";

        sizeFloat = sizeFloat / 1024;
        sizeFloat = (float) (Math.round(sizeFloat * 100.0) / 100.0);
        return sizeFloat + "MB";
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

        Intent intent = new Intent(context, InfoActivity.class);
        Bundle bundle = new Bundle();

        Date date = new Date(audioUtils.get(position).getDate()*1000);
        bundle.putString("uri", audioUtils.get(position).getUri());
        bundle.putString("name", audioUtils.get(position).getName());
        bundle.putString("location", audioUtils.get(position).getLocation());
        bundle.putString("time", date.toString());
        bundle.putString("size", getSize(audioUtils.get(position).getSize()));
        intent.putExtra("INFO", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static class MediaAudioViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView list_more;
        TextView nameText;
        TextView sizeText;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public MediaAudioViewHolder(@NonNull View itemView, boolean isList) {
            super(itemView);
            if(isList){
                imageView = itemView.findViewById(R.id.list_image_view);
                nameText = itemView.findViewById(R.id.media_name_list);
                list_more = itemView.findViewById(R.id.list_more);
                linearLayout = itemView.findViewById(R.id.list_linearLayout);
            }else{
                imageView = itemView.findViewById(R.id.grid_image_view);
                nameText = itemView.findViewById(R.id.media_name_grid);
                sizeText = itemView.findViewById(R.id.media_size);
                relativeLayout = itemView.findViewById(R.id.grid_layout);
            }
        }
    }

}
