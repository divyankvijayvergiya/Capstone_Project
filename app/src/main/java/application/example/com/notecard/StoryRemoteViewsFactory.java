package application.example.com.notecard;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import application.example.com.notecard.Model.Stories;

/**
 * Created by Dell on 04-10-2017.
 */

public class StoryRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final String TAG=StoryRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private ArrayList<Stories> storiesArrayList;

    public StoryRemoteViewsFactory(Context appliationContext, Intent intent){
        mContext=appliationContext;
    }
    @Override
    public void onCreate() {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("nodes").child(firebaseAuth.getCurrentUser().getUid());
        final String noteId=firebaseDatabase.push().getKey();
        firebaseDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Stories stories = userSnapshot.getValue(Stories.class);

                    storiesArrayList.add(stories);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onDataSetChanged() {
        final String noteId=firebaseDatabase.push().getKey();
        firebaseDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Stories stories = userSnapshot.getValue(Stories.class);

                        storiesArrayList.add(stories);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                                new ComponentName(mContext, getClass()));
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);



                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(storiesArrayList!=null) {
            Log.d(TAG, String.valueOf(storiesArrayList.size()));

            return storiesArrayList.size();
        }else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        Stories stories=storiesArrayList.get(position);
        remoteViews.setTextViewText(R.id.widget_title_note,stories.getTitle());
        remoteViews.setTextViewText(R.id.widget_time_note,stories.getContent());





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
