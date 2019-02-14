package com.developer.manuelquinteros.clinicadentalx.Gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Gallery;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PortfolioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Gallery> data = new ArrayList<>();


    public PortfolioAdapter(Context context, List<Gallery> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);
        viewHolder = new MyItemHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RequestOptions myOptions = new RequestOptions()
                .override(200,200)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(data.get(i).getUrl())
                .transition(withCrossFade())
                .apply(myOptions)
                .thumbnail(0.5f)
                .into(((MyItemHolder) viewHolder).mImg);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class MyItemHolder extends RecyclerView.ViewHolder {

        ImageView mImg;

        public MyItemHolder(@NonNull View itemView) {
            super(itemView);

            mImg = (ImageView) itemView.findViewById(R.id.item_gallery);

        }
    }
}
