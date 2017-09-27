package application.example.com.notecard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    View mView;
    TextView textTitle,textTime;
    ImageView noteImage;


    public NoteViewHolder( View itemView) {
        super(itemView);

        mView=itemView;
        textTitle= (TextView) mView.findViewById(R.id.title_note);
        textTime= (TextView) mView.findViewById(R.id.time_note);
        noteImage= (ImageView) mView.findViewById(R.id.note_pic);
    }
    public void setNodeTitle(String title){
        textTitle.setText(title);

    }
    public void setTime(String time){
        textTime.setText(time);


    }

    public void setImage(int image){
        noteImage.setImageResource(image);

    }


}