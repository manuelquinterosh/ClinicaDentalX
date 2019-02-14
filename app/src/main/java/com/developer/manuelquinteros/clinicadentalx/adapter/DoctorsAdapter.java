package com.developer.manuelquinteros.clinicadentalx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Doctors;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder>{

    private List<Doctors> mItems;
    private Context mContext;
    private  OnItemListenerDoctor mOnItemClickListener;

    public interface OnItemListenerDoctor {
        void onInfoDoctorClicked(Doctors doctorClicked);
    }

    public DoctorsAdapter(List<Doctors> items, Context context, OnItemListenerDoctor listenerDoctor) {
        this.mItems = items;
        this.mContext = context;
        this.mOnItemClickListener = listenerDoctor;
    }

    public void setDoctors(List<Doctors> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(parent);

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position) {


        // Spinner Drop down elements
        holder.bind(mItems.get(position));

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class DoctorsViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView specialtyView;
        private final TextView descriptionView;
        private final Button solicitarButton;

        public DoctorsViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctors_item_list, parent, false));

            nameView = (TextView) itemView.findViewById(R.id.name_text);
            specialtyView = (TextView) itemView.findViewById(R.id.specialty_text);
            descriptionView = (TextView) itemView.findViewById(R.id.description_text);


            solicitarButton = (Button) itemView.findViewById(R.id.solicit_button);

            solicitarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        //String selectedItem = (String) scheduleView.getSelectedItem();
                        mOnItemClickListener.onInfoDoctorClicked(mItems.get(position));

                    }
                }
            });

        }

        public void bind(Doctors doctor) {

            nameView.setText(doctor.getNombre());
            specialtyView.setText(doctor.getEspecialidad());
            descriptionView.setText(doctor.getDescripcion());


        }
    }

}
