package com.example.securenoteslib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class NoteActivity extends AppCompatActivity {

    public final static String NOTE_KEY = "NOTE_KEY";
    private TextInputEditText note_TXT_notename;

    private TextInputEditText note_TXT_notecontent;

    private Button note_BTN_done;
    private Note note;

    private static CallBack_noteReadyProtocol callBack_noteReadyProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        findViews();
        initViews();

        Intent prevIntent = getIntent();
        if(prevIntent.getExtras() != null) {
            note = (Note)prevIntent.getExtras().getSerializable(NOTE_KEY);
            note_TXT_notename.setText(note.getNote_name());
            note_TXT_notecontent.setText(note.getNote_content());
        }
        else {
            note = new Note();
        }

    }

    public static void setCallBack_noteReadyProtocol(CallBack_noteReadyProtocol callBack){
        callBack_noteReadyProtocol = callBack;
    }

    private void initViews() {
        note_BTN_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_done_clicked();
            }
        });
    }

    private void button_done_clicked() {
        note.setNote_name(note_TXT_notename.getText().toString())
                .setNote_content(note_TXT_notecontent.getText().toString());
        callBack_noteReadyProtocol.saveNote(note);
        finish();
    }

    private void findViews() {
        note_TXT_notename = findViewById(R.id.note_TXT_notename);
        note_TXT_notecontent = findViewById(R.id.note_TXT_notecontent);
        note_BTN_done = findViewById(R.id.note_BTN_done);
    }
}