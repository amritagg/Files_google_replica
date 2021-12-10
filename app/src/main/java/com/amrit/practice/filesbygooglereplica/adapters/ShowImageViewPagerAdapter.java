package com.amrit.practice.filesbygooglereplica.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amrit.practice.filesbygooglereplica.R;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class ShowImageViewPagerAdapter extends RecyclerView.Adapter<ShowImageViewPagerAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<String> imageUris;
    private final boolean isMedia;

    public ShowImageViewPagerAdapter(Context mContext, ArrayList<String> imageUris, boolean isMedia) {
        this.mContext = mContext;
        this.imageUris = imageUris;
        this.isMedia = isMedia;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_image_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.imageView.setOnClickListener(view -> {
            Intent intent = new Intent("show_hide");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        });

        if(isMedia){
            Uri uri = Uri.parse(imageUris.get(position));
            Glide.with(mContext)
                    .load(uri)
                    .into(holder.imageView);
        }else {
            String uri_string = imageUris.get(position);
            Uri uri = Uri.parse(uri_string);
            holder.imageView.setImageURI(uri);
        }
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        PhotoView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.show_image_view_pager_image);
        }
    }

}
