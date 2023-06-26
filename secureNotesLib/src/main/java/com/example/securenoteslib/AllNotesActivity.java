package com.example.securenoteslib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AllNotesActivity extends AppCompatActivity {

    NoteAdapter noteAdapter;
    private ArrayList<Note> notes = new ArrayList<>();
    private RecyclerView allnotes_RCV_noteslist;
    private ImageButton allnotes_BTN_addnewnote;
    private SecretKey key;
    private byte[] iv;

    CallBack_openNoteProtocol callBack_openNoteProtocol = new CallBack_openNoteProtocol() {
        @Override
        public void openNote(Note note) {
            Intent intent = new Intent(AllNotesActivity.this, NoteActivity.class);
            intent.putExtra(NoteActivity.NOTE_KEY, note);
            startActivity(intent);
        }
    };

    CallBack_noteReadyProtocol callBack_noteReadyProtocol = new CallBack_noteReadyProtocol() {
        @Override
        public void saveNote(Note note) {
            if (note.getIndex() >= 0) {
                notes.set(note.getIndex(), note);
            }
            else {
                note.setIndex(notes.size());
                notes.add(note);
            }
            write_notes_to_DB();
            noteAdapter.notifyDataSetChanged();
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        findViews();
        initViews();

        NoteActivity.setCallBack_noteReadyProtocol(callBack_noteReadyProtocol);
        read_notes_from_DB();
        noteAdapter = new NoteAdapter(AllNotesActivity.this, notes);
        noteAdapter.setCallBack_openNoteProtocol(callBack_openNoteProtocol);
        allnotes_RCV_noteslist.setLayoutManager(new LinearLayoutManager(AllNotesActivity.this));
        allnotes_RCV_noteslist.setAdapter(noteAdapter);
    }

    private void initViews() {
        allnotes_BTN_addnewnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_note_button_clicked();
            }
        });
    }

    private void add_new_note_button_clicked() {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        allnotes_RCV_noteslist = findViewById(R.id.allnotes_RCV_noteslist);
        allnotes_BTN_addnewnote = findViewById(R.id.allnotes_BTN_addnewnote);
    }

    private void read_notes_from_DB() {
        String encryptedGson = SPV.getIntance().getString("List", null);
        if(encryptedGson != null) {
            if(iv == null) { // if there is an iv --> it means key exists and vise versa
                read_key_and_iv();
            }
            String decryptedGson = decryptData(encryptedGson);
            notes = new Gson().fromJson(decryptedGson, new TypeToken<List<Note>>(){}.getType());
        }
    }

    private void read_key_and_iv() {
        try {
            // Read the encodedKey from SharedPreferences
            String keyString = SPV.getIntance().getString2("key", null);
            byte[] keyBytes = Base64.decode(keyString, Base64.DEFAULT);
            key = new SecretKeySpec(keyBytes, "AES");
            String ivString = SPV.getIntance().getString2("iv", null);
            iv = Base64.decode(ivString, Base64.DEFAULT);
        } catch (Exception e) {}
    }

    private void write_notes_to_DB(){
        String gsonString = new Gson().toJson(notes);
        String plainGson = encryptData(gsonString);
        SPV.getIntance().putString("List",plainGson);
    }

    private String encryptData(String gsonString) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            key = keygen.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            iv = cipher.getIV();
            write_key_and_iv();

            byte[] plainlist = cipher.doFinal(gsonString.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(plainlist, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private void write_key_and_iv() {
        try {
            byte[] keyBytes = key.getEncoded();
            String encodedKey = Base64.encodeToString(keyBytes, Base64.DEFAULT);
            SPV.getIntance().putString2("key", encodedKey);
            String encodedIV = Base64.encodeToString(iv, Base64.DEFAULT);
            SPV.getIntance().putString2("iv", encodedIV);
        } catch (Exception e) {}
    }

    private String decryptData(String encryptedGson) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] encryptedBytes = Base64.decode(encryptedGson, Base64.DEFAULT);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException |
                InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
    }
}}