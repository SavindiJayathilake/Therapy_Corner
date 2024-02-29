package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> pendingAppointmentsList;
    private OnButtonClickListener buttonClickListener;

    public PendingAdapter(Context context, List<Appointment> pendingAppointmentsList, OnButtonClickListener buttonClickListener) {
        this.context = context;
        this.pendingAppointmentsList = pendingAppointmentsList;
        this.buttonClickListener = buttonClickListener;
    }

    public void setAppointments(List<Appointment> pendingAppointmentsList) {
        this.pendingAppointmentsList = pendingAppointmentsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = pendingAppointmentsList.get(position);
        holder.txtPatientName.setText(appointment.getPatient_first_name() + " " + appointment.getPatient_last_name());
        holder.txtDate.setText(appointment.getDate());
        holder.txtTime.setText(appointment.getTime_slot());
        holder.txtpatientemail.setText(appointment.getPatient_email());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onApproveButtonClick(appointment);
                }
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onRejectButtonClick(appointment);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingAppointmentsList != null ? pendingAppointmentsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientName, txtDate, txtTime, txtpatientemail;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientName = itemView.findViewById(R.id.txtPatientName);
            txtpatientemail = itemView.findViewById(R.id.txtpatientemail);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    public interface OnButtonClickListener {
        void onApproveButtonClick(Appointment appointment);
        void onRejectButtonClick(Appointment appointment);
    }
}
