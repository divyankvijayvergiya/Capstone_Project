package application.example.com.notecard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Dell on 20-09-2017.
 */

public class StoryCreateFragment extends Fragment implements View.OnClickListener {
    private EditText etTitle;
    private EditText etNote;
    private ImageButton btDone;
    private ImageButton btDelete;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;
    public int index=0;
   public String not;


    public StoryCreateFragment( ) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_create, container, false);
        etNote = (EditText) rootView.findViewById(R.id.script);
        etTitle = (EditText) rootView.findViewById(R.id.title_name);
        btDone = (ImageButton) rootView.findViewById(R.id.button_done);
        btDelete = (ImageButton) rootView.findViewById(R.id.button_delete);
        btDone.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("nodes")
                .child(firebaseAuth.getCurrentUser().getUid());


        return rootView;
    }

    @Override
    public void onClick(View v) {
        String title = etTitle.getText().toString().trim();
        String content = etNote.getText().toString().trim();
        if (v == btDone) {

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                createNote(title, content);
            } else {
                Snackbar.make(getView(), "Please fill empty fields", Snackbar.LENGTH_LONG).show();
            }
        } else if (v == btDelete) {

        }

    }

    private void createNote(String title, String content) {
        if (firebaseAuth.getCurrentUser() != null) {
            final DatabaseReference newDatabaseReference = mDatabaseReference.push();
            Users users=new Users();
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
                                Toast.makeText(getActivity(), "Note added to database", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getActivity(), "ERROR: ", Toast.LENGTH_SHORT).show();


                            }

                        }
                    });

                }
            });
            mainThread.start();


        } else {
            Toast.makeText(getActivity(), "User is not Signed in", Toast.LENGTH_SHORT).show();
        }
    }
}
