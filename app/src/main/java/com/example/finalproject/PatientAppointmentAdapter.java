package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> appointmentsList;

    public PatientAppointmentAdapter(Context context, List<Appointment> appointmentsList) {
        this.context = context;
        this.appointmentsList = appointmentsList;
    }

    public void setAppointments(List<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);
        holder.txtPatientName.setText(appointment.getTherapist_name());
        holder.txtDate.setText(appointment.getDate());
        holder.txtTime.setText(appointment.getTime_slot());
    }

    @Override
    public int getItemCount() {
        return appointmentsList != null ? appointmentsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientName, txtDate, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientName = itemView.findViewById(R.id.txtPatientName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
