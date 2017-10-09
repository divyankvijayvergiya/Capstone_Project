package application.example.com.notecard.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import application.example.com.notecard.Model.Stories;
import application.example.com.notecard.R;

/**
 * Created by Dell on 04-10-2017.
 */

public class StoryRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final String TAG = StoryRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private ArrayList<Stories> storiesArrayList;
    private CountDownLatch mCountDownLatch;
    public final String TITLE = "title";
    public final String KEY = "key";
   public  String noteId;

    public final String CONTENT = "content";
    public final String TIMESTAMP = "timeStamp";


    public StoryRemoteViewsFactory(Context appliationContext, Intent intent) {
        mContext = appliationContext;
        storiesArrayList = new ArrayList<Stories>();
    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {

        getItems();


    }


    private void getItems() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("nodes").child(firebaseAuth.getCurrentUser().getUid());





            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){

                    Iterable<DataSnapshot> children=  dataSnapshot.getChildren();
                    for(DataSnapshot child : children) {
                        String key=dataSnapshot.getKey();
                        Stories stories = child.getValue(Stories.class);

                        storiesArrayList.add(stories);
                        noteId = key;
                    }
                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                }
            });


        }




    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (storiesArrayList != null) {
            Log.d(TAG + "items", String.valueOf(storiesArrayList.size()));

            return storiesArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        Stories stories = storiesArrayList.get(position);
        remoteViews.setTextViewText(R.id.widget_title_note, stories.getTitle());
        remoteViews.setTextViewText(R.id.widget_time_note, stories.getContent());
        Bundle extras = new Bundle();
        extras.putParcelable(mContext.getString(R.string.stories), stories);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_item_linear, fillIntent);


        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
