package com.developer.manuelquinteros.clinicadentalx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Notifications;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    List<Notifications> notificationsAdapters;
    Context mContext;

    ItemClickListener mItemClickListener;

    public NotificationsAdapter(List<Notifications> object,ItemClickListener itemClickListener,  Context context) {
        notificationsAdapters = object;
        mItemClickListener = itemClickListener;
        mContext= context;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Notifications newNotifications = notificationsAdapters.get(i);
        viewHolder.title.setText(newNotifications.getmTitle());
        viewHolder.description.setText(newNotifications.getmDescription());
        viewHolder.expiryDate.setText(String.format("Valido hasta el %s",newNotifications.getmExpiryDate()));
        viewHolder.discount.setText(String.format("%d%%", (int) (newNotifications.getmDiscount() * 100)));

        viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.OnItemDeleteClick(i, newNotifications);
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.OnItemClickNotification(i, newNotifications);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsAdapters.size();
    }


    public void setList(List<Notifications> list) {
        notificationsAdapters = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public TextView expiryDate;
        public TextView discount;
        public FloatingActionButton borrar;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            description = (TextView) itemView.findViewById(R.id.text_description);
            expiryDate = (TextView) itemView.findViewById(R.id.text_expiry_date);
            discount = (TextView) itemView.findViewById(R.id.text_discount);
            borrar = (FloatingActionButton) itemView.findViewById(R.id.btnNotification);
            cardView = (CardView)itemView.findViewById(R.id.card_notification);
        }
    }

}
