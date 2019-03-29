package com.developer.manuelquinteros.clinicadentalx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Schedule;

import java.util.List;

public class GridCustomAdapterSchedule extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Schedule> scheduleList;
    private Context context;
    RequestOptions options;

    public GridCustomAdapterSchedule(Context context, List<Schedule> customListview) {
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        scheduleList = customListview;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.scheduleicon);

    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.grid_item_schedule, parent, false);
            listViewHolder.txSchedule = (TextView) convertView.findViewById(R.id.textSchedule);
            listViewHolder.ImgPicture = (ImageView) convertView.findViewById(R.id.picture);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.txSchedule.setText(scheduleList.get(position).getHora_inicio() + " a " + scheduleList.get(position).getHora_final());

        //Load Image from the internet set it into Imageviw Glide
        Glide.with(context).load(scheduleList.get(position).getImageResource()).apply(options).into(listViewHolder.ImgPicture);


        return convertView;
    }

    static class ViewHolder{
        TextView txSchedule;
        ImageView ImgPicture;
    }
}
