package application.example.com.notecard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    public final String TITLE="title" ;
    public final String CONTENT="content";
    private String noteId="no";
    private String noteId2="kk";


    public StoryCreateFragment( ) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_create, container, false);
        setHasOptionsMenu(true);

        etNote = (EditText) rootView.findViewById(R.id.script);
        etTitle = (EditText) rootView.findViewById(R.id.title_name);
        btDone = (ImageButton) rootView.findViewById(R.id.button_done);
        btDelete = (ImageButton) rootView.findViewById(R.id.button_delete);
        btDone.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("nodes")
                .child(firebaseAuth.getCurrentUser().getUid());
        if (getArguments() != null) {

            Log.i("This is my title: ", getArguments().getString(TITLE, ""));
            Log.i("This is my content: ", getArguments().getString(CONTENT,""));
            etTitle.setText(getArguments().getString(TITLE,""));
            etNote.setText(getArguments().getString(CONTENT,""));
            try{
                noteId=getArguments().getString(TITLE);
                noteId2=getArguments().getString(CONTENT);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            if(!noteId.equals("no") && !noteId2.equals("kk")){
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
    private void deleteNote(){
        mDatabaseReference.child(noteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Node Deleted",Toast.LENGTH_SHORT).show();
                    noteId="no";
                    noteId2="kk";
                    try {
                        finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }


                }
                else {
                    Log.e("StoryCreateFragment",task.getException().toString());
                    Toast.makeText(getContext(),"ERROR: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

    }
}
