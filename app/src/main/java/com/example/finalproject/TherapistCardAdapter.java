package com.example.finalproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;





public class TherapistCardAdapter extends RecyclerView.Adapter<TherapistCardAdapter.TherapistCardViewHolder> {


    private List<TheraDataClass> therapists;

    public TherapistCardAdapter(List<TheraDataClass> therapists) {
        this.therapists = therapists;
    }



    @NonNull
    @Override
    public TherapistCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.therapist_item, parent, false);
        return new TherapistCardViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull TherapistCardViewHolder holder, int position) {
        TheraDataClass therapist = therapists.get(position);

        Picasso.get().load(therapist.getTheradataImage()).into(holder.profileImage);
        holder.therapistName.setText("Dr. " + therapist.getTheradataFirstName() + " " + therapist.getTheradataLastName());
        holder.therapyService.setText(therapist.getTheradataTherapyServices());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TheraDataClass selectedTherapist = therapists.get(position);

                Intent intent = new Intent(view.getContext(), ReadonlyProfileActivity.class);
                intent.putExtra("therapistData", selectedTherapist);
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return therapists.size();
    }

    public static class TherapistCardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView profileImage;
        TextView therapistName;
        TextView therapyService;

        public TherapistCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            profileImage = itemView.findViewById(R.id.profile_image);
            therapistName = itemView.findViewById(R.id.therapist_name);
            therapyService = itemView.findViewById(R.id.therapy_service);
        }
    }
}

