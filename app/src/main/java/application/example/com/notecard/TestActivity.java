package application.example.com.notecard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import application.example.com.notecard.Model.Stories;

/**
 * Created by Dell on 08-10-2017.
 */

public class TestActivity extends AppCompatActivity {
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private ArrayList<Stories> storiesArrayList;
    private CountDownLatch mCountDownLatch;
    public final String TITLE = "title";
    public final String KEY = "key";

    public final String CONTENT = "content";
    public final String TIMESTAMP = "timeStamp";
    TextView t, n;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_widget_provider);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("nodes").child(firebaseAuth.getCurrentUser().getUid());

        final String noteId = firebaseDatabase.push().getKey();


        firebaseDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, Stories> value = (Map<String, Stories>) dataSnapshot.getValue();
                        for (Map.Entry<String, Stories> entry : value.entrySet()) {
                            String key = entry.getKey();
                            Stories va = entry.getValue();
                            if (!storiesArrayList.contains(va)) {
                                storiesArrayList.add(va);
                                Log.d("jjjjjjj", key + " " + va);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}