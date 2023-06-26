package com.example.securenoteslib;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private ArrayList<Note> notes;
    private CallBack_openNoteProtocol callBack_openNoteProtocol;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    public void setCallBack_openNoteProtocol(CallBack_openNoteProtocol callBack_openNoteProtocol){
        this.callBack_openNoteProtocol = callBack_openNoteProtocol;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.notelist_LBL_notename.setText(note.getNote_name());

        holder.notelist_LAY_onenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack_openNoteProtocol.openNote(note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes == null || notes.size() ==0? 0: notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat notelist_LAY_onenote;
        private MaterialTextView notelist_LBL_notename;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View itemView) {
            notelist_LAY_onenote = itemView.findViewById(R.id.notelist_LAY_onenote);
            notelist_LBL_notename = itemView.findViewById(R.id.notelist_LBL_notename);
        }
    }
}
