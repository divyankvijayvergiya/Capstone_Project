package application.example.com.notecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 23-09-2017.
 */

public class StoryCreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etTitle;
    private EditText etNote;
    private ImageButton btDone;
    private ImageButton btDelete;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;
    public int index=0;
    public final String TITLE="title" ;
    public final String CONTENT="content";
    private String noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_story_create);
        etNote = (EditText)findViewById(R.id.script);
        etTitle = (EditText)findViewById(R.id.title_name);
        btDone = (ImageButton)findViewById(R.id.button_done);
        btDelete = (ImageButton)findViewById(R.id.button_delete);
        btDone.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("nodes")
                .child(firebaseAuth.getCurrentUser().getUid());
        Intent intent=getIntent();
        if(intent!=null){
            etTitle.setText(intent.getStringExtra(TITLE));
            etNote.setText(intent.getStringExtra(CONTENT));
            noteId=getIntent().getStringExtra("key");
        }

    }

    @Override
    public void onClick(View v) {
        String title = etTitle.getText().toString().trim();
        String content = etNote.getText().toString().trim();
        if (v == btDone) {

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                createNote(title, content);
            } else {
                Snackbar.make(v, "Please fill empty fields", Snackbar.LENGTH_LONG).show();
            }
        } else if (v == btDelete) {
            if(!noteId.equals("key")){
                deleteNote();
            }

        }

    }

    private void createNote(String title, String content) {
        if (firebaseAuth.getCurrentUser() != null) {
            final DatabaseReference newDatabaseReference = mDatabaseReference.push();
            final Map noteMap = new HashMap();
            noteMap.put("title", title);
            noteMap.put("content", content);
            noteMap.put("timeStamp", ServerValue.TIMESTAMP);
            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newDatabaseReference.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(StoryCreateActivity.this, "Note added to database", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(StoryCreateActivity.this, "ERROR: ", Toast.LENGTH_SHORT).show();


                            }

                        }
                    });

                }
            });
            mainThread.start();


        } else {
            Toast.makeText(StoryCreateActivity.this, "User is not Signed in", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteNote(){
        mDatabaseReference.child(noteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(StoryCreateActivity.this,"Notes Deleted",Toast.LENGTH_SHORT).show();
                    noteId="key";
                    finish();




                }
                else {
                    Log.e("StoryCreateFragment",task.getException().toString());
                    Toast.makeText(StoryCreateActivity.this,"ERROR: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}