package com.developer.manuelquinteros.clinicadentalx.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.manuelquinteros.clinicadentalx.R;
import com.developer.manuelquinteros.clinicadentalx.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {

    private List<Appointment> mItems;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(Appointment clickedAppointment);

        void onCancelAppointment(Appointment canceledAppointment);

        void onPrintReportAppointment(Appointment editAppointment);

    }

    public AppointmentAdapter(Context context, List<Appointment> items, OnItemClickListener listener) {
        this.mContext = context;
        this.mItems = items;
        this.mOnItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void swapItems(List<Appointment> appointments){

        if (appointments == null){
            mItems = new ArrayList<>(0);
        } else {
            mItems = appointments;
        }
        notifyDataSetChanged();

    }

    public void removeItem(int position) {
        mItems.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }

    @Override
    public AppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_appointment, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);



        return new AppointmentHolder(vista);
    }



    @Override
    public void onBindViewHolder(final AppointmentHolder holder, int position) {

        Appointment appointment = mItems.get(position);

        View stat = holder.statusIndicator;

        if (appointment.getStatus().equalsIgnoreCase("Activa")) {
            holder.imgSSt.setImageResource(R.drawable.activeappointment);
        } else if (appointment.getStatus().equalsIgnoreCase("Cumplida")) {
            stat.setBackgroundResource(R.color.completedStatus);
            holder.imgSSt.setImageResource(R.drawable.confirmappointment);
        } else if (appointment.getStatus().equalsIgnoreCase("Cancelada")) {
            stat.setBackgroundResource(R.color.cancelledStatus);
            holder.imgSSt.setImageResource(R.drawable.cancelledappointment);
        }

        holder.txCode.setText( mItems.get(position).getIdcitas().toString());
        holder.txDate.setText(mItems.get(position).getFecha().toString());
        holder.txSchedule.setText(mItems.get(position).getHora().toString());
        holder.txDoctors.setText(mItems.get(position).getNdoctor().toString());
        holder.txSpecialty.setText(mItems.get(position).getEspecialidad().toString());


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class AppointmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txCode, txDate, txSchedule, txDoctors, txSpecialty, txtStatus;
        public View statusIndicator;
        public Button btCancel, btPrint;
        public CardView cardStatus;
        public ImageView imgSSt;
       /** public TextView mListaVacia;
        public ImageView mImageEmpty;
        public RecyclerView recyclerAppointment;**/


        public AppointmentHolder(View itemView) {
            super(itemView);

           /** mListaVacia= (TextView)itemView.findViewById(R.id.sms_lista_vacia);
            mImageEmpty = (ImageView) itemView.findViewById(R.id.imgEmpty);
            recyclerAppointment = (RecyclerView) itemView.findViewById(R.id.list_appointment);**/

            statusIndicator = (View) itemView.findViewById(R.id.indicatorStatus);
            txCode = (TextView) itemView.findViewById(R.id.tvCode);
            txDate = (TextView) itemView.findViewById(R.id.tvDate);
            txSchedule = (TextView) itemView.findViewById(R.id.tvSchedule);
            txDoctors = (TextView) itemView.findViewById(R.id.tvNameDoctors);
            txSpecialty = (TextView) itemView.findViewById(R.id.tvSpecialty);
            btCancel = (Button) itemView.findViewById(R.id.btnCancel);
            btPrint = (Button) itemView.findViewById(R.id.btnPrintReport);

            cardStatus = (CardView) itemView.findViewById(R.id.recyclerCita);
            imgSSt = (ImageView) itemView.findViewById(R.id.imgStatus);

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mOnItemClickListener.onCancelAppointment(mItems.get(position));
                        removeItem(position);
                    }

                }
            });

            btPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mOnItemClickListener.onPrintReportAppointment(mItems.get(position));
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }
    }


}
