package application.example.com.notecard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Dell on 27-09-2017.
 */

public class VideoActivity extends AppCompatActivity {
    private VideoView mVideoView;

    private StorageReference mStorageReference;
    private FirebaseAuth mFireBaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoView= (VideoView) findViewById(R.id.videoView);
        mFireBaseAuth=FirebaseAuth.getInstance();

        mStorageReference= FirebaseStorage.getInstance().getReference().child("videos")
                .child(mFireBaseAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
