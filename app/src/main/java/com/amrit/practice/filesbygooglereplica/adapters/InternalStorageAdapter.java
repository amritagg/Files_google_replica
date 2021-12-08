package com.amrit.practice.filesbygooglereplica.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amrit.practice.filesbygooglereplica.R;
import com.amrit.practice.filesbygooglereplica.utils.InternalStorageUtil;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class InternalStorageAdapter extends BaseAdapter {

    private final boolean isList;
    private final Context context;
    private final ArrayList<InternalStorageUtil> utils;

    public InternalStorageAdapter(Context context, ArrayList<InternalStorageUtil> utils, boolean isList) {
        this.context = context;
        this.utils = utils;
        this.isList = isList;
    }

    @Override
    public int getCount() {
        return utils.size();
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
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            if (!isList) convertView = layoutInflater.inflate(R.layout.grid_internal, null);
            else convertView = layoutInflater.inflate(R.layout.list_media, null);
        }

        ImageView imageView;

        if (isList) {
            imageView = convertView.findViewById(R.id.list_image_view);
            TextView name = convertView.findViewById(R.id.media_name_list);
            name.setText(utils.get(position).getName());
            if (!utils.get(position).isFolder()) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(utils.get(position).getUri()).error(R.drawable.ic_baseline_document_24).into(imageView);
            }else{
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_folder_open_24));
            }
        } else {

            LinearLayout linearLayout = convertView.findViewById(R.id.layout_for_folder);
            RelativeLayout relativeLayout = convertView.findViewById(R.id.layout_for_media);

            if (utils.get(position).isFolder()) {
                linearLayout.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                TextView name = convertView.findViewById(R.id.folder_name);
                name.setText(utils.get(position).getName());
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                imageView = convertView.findViewById(R.id.internal_grid_image_view);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(utils.get(position).getUri()).error(R.drawable.ic_baseline_document_24).into(imageView);

                TextView size = convertView.findViewById(R.id.internal_size);
                size.setText(utils.get(position).getSize() + "");
                size.setShadowLayer(2, 1, 1, Color.BLACK);
            }
        }

        return convertView;
    }
}
