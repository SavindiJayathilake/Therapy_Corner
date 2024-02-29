package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ApprovedAdapter extends RecyclerView.Adapter<ApprovedAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> approvedAppointmentsList;

    public ApprovedAdapter(Context context) {
        this.context = context;
    }

    public void setAppointments(List<Appointment> approvedAppointmentsList) {
        this.approvedAppointmentsList = approvedAppointmentsList;
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
        Appointment appointment = approvedAppointmentsList.get(position);
        holder.txtPatientName.setText(appointment.getPatient_first_name() + " " + appointment.getPatient_last_name());
        holder.txtDate.setText(appointment.getDate());
        holder.txtTime.setText(appointment.getTime_slot());
        holder.txtpatientemail.setText(appointment.getPatient_email());
    }

    @Override
    public int getItemCount() {
        return approvedAppointmentsList != null ? approvedAppointmentsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientName, txtDate, txtTime, txtpatientemail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientName = itemView.findViewById(R.id.txtPatientName);
            txtpatientemail = itemView.findViewById(R.id.txtpatientemail);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
