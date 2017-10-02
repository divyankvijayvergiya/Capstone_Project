package application.example.com.notecard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dell on 27-09-2017.
 */

public class VideoActivity  extends AppCompatActivity {
    public final String CONTENT="content";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (null == savedInstanceState) {
            Intent intent=getIntent();
            String content=intent.getStringExtra(CONTENT);
            String not=intent.getStringExtra("k");
            String noteId=intent.getStringExtra("key");
            String con=intent.getStringExtra("con");
            Bundle b=new Bundle();
            b.putString("k",not);
            b.putString("con",con);
            b.putString(CONTENT,content);
            b.putString("key",noteId);
            VideoFragment videoFragment=new VideoFragment();
            videoFragment.setArguments(b);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, videoFragment)
                    .commit();
        }
    }

}