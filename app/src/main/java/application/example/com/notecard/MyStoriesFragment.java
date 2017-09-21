package application.example.com.notecard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Dell on 20-09-2017.
 */

public class MyStoriesFragment extends Fragment implements StoryAdapter.ListItemClickListener {
    private RecyclerView mRecyclerView;
    private ArrayList<Users> mUserArrayList;
    private StoryAdapter mAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;

    public MyStoriesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.story_fragment, container, false);
        mRecyclerView= (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new StoryAdapter(this, mUserArrayList);
        mRecyclerView.setAdapter(mAdapter);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            databaseReference= FirebaseDatabase.getInstance().getReference().child("nodes")
                    .child(firebaseAuth.getCurrentUser().getUid());
        }

        databaseReference.child("nodes").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title=dataSnapshot.child("title").getValue().toString();
                String timeStamp=dataSnapshot.child("timeStamp").getValue().toString();
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(int clickItemIndex) {

    }


}
