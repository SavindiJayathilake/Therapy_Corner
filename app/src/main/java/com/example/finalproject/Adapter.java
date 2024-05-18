package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {



    LayoutInflater inflater;
    List<NoteModel> noteModels;

    Adapter(Context context, List<NoteModel> noteModels){
        this.inflater = LayoutInflater.from(context);
        this.noteModels = noteModels;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.note_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        String title = noteModels.get(position).getNoteTitle();
        String date = noteModels.get(position).getNoteDate();
        String time = noteModels.get(position).getNoteTime();

        holder.nTitle.setText(title);
        holder.nDate.setText(date);
        holder.nTime.setText(time);

    }

    @Override
    public int getItemCount(){
        return noteModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nTitle, nDate, nTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nTitle = itemView.findViewById(R.id.nTitle);
            nTime = itemView.findViewById(R.id.nTime);
            nDate = itemView.findViewById(R.id.nDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NoteDetailActivity.class);
                    intent.putExtra("ID", noteModels.get(getAbsoluteAdapterPosition()).getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public void setNoteList(List<NoteModel> noteModels) {
        this.noteModels = noteModels;
    }



}

//
//package com.example.finalproject;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//
//    private LayoutInflater inflater;
//    private List<NoteModel> noteModels;
//    private Context context;
//
//    Adapter(Context context, List<NoteModel> noteModels) {
//        this.inflater = LayoutInflater.from(context);
//        this.noteModels = noteModels;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.note_view, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
//        String title = noteModels.get(position).getNoteTitle();
//        String date = noteModels.get(position).getNoteDate();
//        String time = noteModels.get(position).getNoteTime();
//
//        holder.nTitle.setText(title);
//        holder.nDate.setText(date);
//        holder.nTime.setText(time);
//    }
//
//    @Override
//    public int getItemCount() {
//        return noteModels.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView nTitle, nDate, nTime;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            nTitle = itemView.findViewById(R.id.nTitle);
//            nTime = itemView.findViewById(R.id.nTime);
//            nDate = itemView.findViewById(R.id.nDate);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, NoteDetailActivity.class);
//                    intent.putExtra("ID", noteModels.get(getAbsoluteAdapterPosition()).getId());
//                    ((EJournalActivity) context).startActivityForResult(intent, EJournalActivity.REQUEST_CODE_VIEW_NOTE);
//                }
//            });
//        }
//    }
//
//    public void updateList(List<NoteModel> newNoteList) {
//        noteModels = newNoteList;
//        notifyDataSetChanged();
//    }
//}
