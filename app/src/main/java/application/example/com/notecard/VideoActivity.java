package application.example.com.notecard;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * Created by Dell on 27-09-2017.
 */

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private VideoView mVideoView;
    private Camera camera;
    boolean previewing = false;
    private StorageReference mStorageReference;
    private FirebaseAuth mFireBaseAuth;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;


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


        mStorageReference= FirebaseStorage.getInstance().getReference().child("videos")
                .child(mFireBaseAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();


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

