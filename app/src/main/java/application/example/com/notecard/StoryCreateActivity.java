package application.example.com.notecard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import application.example.com.notecard.Model.Stories;

/**
 * Created by Dell on 23-09-2017.
 */

public class StoryCreateActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_VIDEO_CAPTURE = 1;

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
    private ImageButton btEdit;
    private TextView tvSave;
    private String note;

    private StorageReference mStorageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_story_create);
        Toolbar toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etNote = (EditText)findViewById(R.id.script);
        etTitle = (EditText)findViewById(R.id.title_name);
        btDone = (ImageButton)findViewById(R.id.button_done);
        btDelete = (ImageButton)findViewById(R.id.button_delete);
        btEdit= (ImageButton) findViewById(R.id.button_update);
        tvSave= (TextView) findViewById(R.id.text_save);
        btEdit.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        btDone.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("nodes")
                .child(firebaseAuth.getCurrentUser().getUid());
        mStorageReference= FirebaseStorage.getInstance().getReference().child("videos")
                .child(firebaseAuth.getCurrentUser().getUid());
        progressDialog=new ProgressDialog(this);
        Intent intent=getIntent();
        if(intent!=null){
            etTitle.setText(intent.getStringExtra(TITLE));
            etNote.setText(intent.getStringExtra(CONTENT));
            noteId=getIntent().getStringExtra("key");
            if (noteId!=null){
                btDone.setVisibility(View.GONE);
            }
            else {
                btEdit.setVisibility(View.GONE);
                tvSave.setVisibility(View.GONE);
            }





        }

    }

    @Override
    public void onClick(View v) {
        String title = etTitle.getText().toString().trim();
        String content = etNote.getText().toString().trim();
        if (v == btDone) {

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                createNote(title, content);






            }
            else {
                Snackbar.make(v, "Please fill empty fields", Snackbar.LENGTH_LONG).show();
            }
        } else if (v == btDelete) {
            if(!noteId.equals("key")){
                deleteNote();
            }

        }
        else if(v==btEdit){
            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                updateData(title,content);
                Snackbar.make(v, "Data Updated", Snackbar.LENGTH_LONG).show();

            }
            else{
                Snackbar.make(v, "Please fill empty fields", Snackbar.LENGTH_LONG).show();

            }
        }
        else if(v==tvSave){
            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                updateData(title,content);
                Snackbar.make(v, "Data Updated", Snackbar.LENGTH_LONG).show();


            }
            else{
                Snackbar.make(v, "Please fill empty fields", Snackbar.LENGTH_LONG).show();

            }

        }

    }

    private void createNote(String title, String content) {
        if (firebaseAuth.getCurrentUser() != null) {
            final DatabaseReference newDatabaseReference = mDatabaseReference.push();

            noteId=newDatabaseReference.getKey();
            Log.d("Note key",noteId);

            final Map noteMap = new HashMap();
            noteMap.put(TITLE, title);
            noteMap.put(CONTENT, content);
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
    private void updateData(String title, String content){
        if(firebaseAuth.getCurrentUser()!=null){

            Stories stories=new Stories(title,content);

            Map<String, Object> postValues = stories.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(noteId, postValues);

            mDatabaseReference.updateChildren(childUpdates);



        }
    }
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading Video...");
            progressDialog.show();
            Uri videoUri = intent.getData();
            StorageReference videoRef=mStorageReference.child(videoUri.getLastPathSegment());
            UploadTask uploadTask = videoRef.putFile(videoUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(StoryCreateActivity.this,"Video Uploaded",Toast.LENGTH_SHORT).show();


                }
            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.story_create_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(id==R.id.action_video){
            Intent intent =getIntent();
            String  content=intent.getStringExtra(CONTENT);
            Intent newIntent=new Intent(StoryCreateActivity.this,VideoActivity.class);
            newIntent.putExtra(CONTENT,content);
            newIntent.putExtra("key",noteId);
            startActivity(newIntent);











            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}