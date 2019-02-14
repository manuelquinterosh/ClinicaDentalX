package com.developer.manuelquinteros.clinicadentalx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Notifications;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    //https://github.com/rickydam/RickyBooks-Android/blob/master/app/src/main/java/com/rickybooks/rickybooks/FirebaseService.java
    //https://github.com/DhvanilP/Live-video-streaming/blob/master/Android-App/app/src/main/java/net/majorkernelpanic/example3/MainActivity.java


    ArrayList<Notifications> notificationsAdapters = new ArrayList<>();

    public NotificationsAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public TextView expiryDate;
        public TextView discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            description = (TextView) itemView.findViewById(R.id.text_description);
            expiryDate = (TextView) itemView.findViewById(R.id.text_expiry_date);
            discount = (TextView) itemView.findViewById(R.id.text_discount);
        }
    }
}
