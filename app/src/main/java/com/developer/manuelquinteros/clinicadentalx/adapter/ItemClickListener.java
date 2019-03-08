package com.developer.manuelquinteros.clinicadentalx.adapter;

import com.developer.manuelquinteros.clinicadentalx.model.Notifications;

public interface ItemClickListener {
    void OnItemDeleteClick(int position, Notifications delete);
    void OnItemClickNotification(int position, Notifications id);
}