package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RejectedAdapter extends RecyclerView.Adapter<RejectedAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> rejectedAppointmentsList;

    public RejectedAdapter(Context context) {
        this.context = context;
        this.rejectedAppointmentsList = new ArrayList<>();
    }

    public void setAppointments(List<Appointment> rejectedAppointmentsList) {
        this.rejectedAppointmentsList = rejectedAppointmentsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (rejectedAppointmentsList != null && position < rejectedAppointmentsList.size()) {
            Appointment appointment = rejectedAppointmentsList.get(position);
            holder.txtPatientName.setText(appointment.getPatient_first_name() + " " + appointment.getPatient_last_name());
            holder.txtDate.setText(appointment.getDate());
            holder.txtTime.setText(appointment.getTime_slot());
            holder.txtpatientemail.setText(appointment.getPatient_email());
        }
    }

    @Override
    public int getItemCount() {
        return rejectedAppointmentsList != null ? rejectedAppointmentsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientName, txtDate, txtTime, txtpatientemail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientName = itemView.findViewById(R.id.txtPatientName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtpatientemail = itemView.findViewById(R.id.txtpatientemail);
        }
    }
}
