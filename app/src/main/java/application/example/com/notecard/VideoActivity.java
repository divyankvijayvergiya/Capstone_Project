package application.example.com.notecard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * Created by Dell on 27-09-2017.
 */

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA =1 ;
    private VideoView mVideoView;
    String noteId="key";
    private Camera camera;
    boolean previewing = false;
    public final String CONTENT="content";
    private StorageReference mStorageReference;
    private FirebaseAuth mFireBaseAuth;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView text_camera;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mFireBaseAuth=FirebaseAuth.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView= (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("nodes")
                .child(mFireBaseAuth.getCurrentUser().getUid());
        text_camera= (TextView) findViewById(R.id.text_camera);
        Intent intent=getIntent();
        if(intent!=null) {
            text_camera.setText(intent.getStringExtra(CONTENT));
            noteId = getIntent().getStringExtra("key");
        }




        mStorageReference= FirebaseStorage.getInstance().getReference().child("videos")
                .child(mFireBaseAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (ContextCompat.checkSelfPermission(VideoActivity.this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(VideoActivity.this,
                    android.Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(VideoActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        camera = Camera.open();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA:{
                if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){




                }
                else {

                }
                return;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    }

