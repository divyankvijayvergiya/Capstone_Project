package application.example.com.notecard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class MyStoriesFragment extends Fragment  {
    private RecyclerView mRecyclerView;
    private ArrayList<Users> mUserArrayList;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    public final String TITLE="title" ;
    public final String CONTENT="content";
    public final String TIMESTAMP="timeStamp";

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



        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            databaseReference= FirebaseDatabase.getInstance().getReference().child("nodes")
                    .child(firebaseAuth.getCurrentUser().getUid());
        }



        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users, NoteViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, NoteViewHolder>(
               Users.class, R.layout.list_notes,NoteViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, Users model, int position) {
                final String noteId=getRef(position).getKey();
                databaseReference.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String title=dataSnapshot.child(TITLE).getValue().toString();
                        final String time=dataSnapshot.child(TIMESTAMP).getValue().toString();
                        final String content=dataSnapshot.child(CONTENT).getValue().toString();
                        viewHolder.setNodeTitle(title);
                        viewHolder.setTime(time);
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                StoryCreateFragment storyCreateFragment=new StoryCreateFragment();
                                Bundle b=new Bundle();
                                b.putString(TITLE,title);
                                b.putString(CONTENT,content);
                                storyCreateFragment.setArguments(b);
                                fragmentManager.beginTransaction()
                                        .add(R.id.frame_stories,storyCreateFragment)
                                        .commit();

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


}



