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


    public StoryRemoteViewsFactory(Context appliationContext, Intent intent) {
        mContext = appliationContext;
        storiesArrayList = new ArrayList<Stories>();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mCountDownLatch = new CountDownLatch(1);

        getItems();
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void getItems() {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("nodes").child(firebaseAuth.getCurrentUser().getUid());
        if (mCountDownLatch.getCount() == 0) {
            if (storiesArrayList != null) {
                firebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                String key = child.getKey();
                                Stories stories = child.getValue(Stories.class);
                                storiesArrayList.add(stories);

                            }
                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mContext, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } else {
            if (storiesArrayList != null) {
                firebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            storiesArrayList.clear();

                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                String key = child.getKey();
                                Stories stories = child.getValue(Stories.class);
                                storiesArrayList.add(stories);

                            }
                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mContext, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
            mCountDownLatch.countDown();
        }


    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (storiesArrayList != null && !storiesArrayList.isEmpty()) {
            Log.d(TAG + "items", String.valueOf(storiesArrayList.size()));

            return storiesArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        if (storiesArrayList.size() > 0) {


            remoteViews.setTextViewText(R.id.widget_title_note, storiesArrayList.get(position).getTitle());
            Bundle extras = new Bundle();
            extras.putString(mContext.getString(R.string.stories), storiesArrayList.get(position).getTitle());
            Intent fillIntent = new Intent();
            fillIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.widget_item_linear, fillIntent);
        }


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
