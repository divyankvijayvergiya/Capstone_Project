package application.example.com.notecard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dell on 20-09-2017.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private final ListItemClickListener mListItemClickListener;

    private final ArrayList<Users> usersArrayList;
    public interface ListItemClickListener {
        void onListItemClick(int clickItemIndex);

    }

    public StoryAdapter(ListItemClickListener mListItemClickListener, ArrayList<Users> usersArrayList) {
        this.mListItemClickListener = mListItemClickListener;
        this.usersArrayList = usersArrayList;
    }

    @Override
    public StoryAdapter.StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_notes;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToBooleanImmediately = false;
        View view = layoutInflater.inflate(layoutIdForListItem, parent, shouldAttachToBooleanImmediately);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryAdapter.StoryViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        if(usersArrayList!=null){
           return usersArrayList.size();
        }else {
            return 0;
        }
    }
    class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView notePic;
        public final TextView title;
        public final TextView mUserName;
        public final TextView time;

        public StoryViewHolder(View itemView) {

            super(itemView);
            notePic=(ImageView)itemView.findViewById(R.id.note_pic);
            title= (TextView) itemView.findViewById(R.id.title_note);
            mUserName= (TextView) itemView.findViewById(R.id.name_user);
            time=(TextView)itemView.findViewById(R.id.time_note);
            itemView.setOnClickListener(this);


        }
        void onBind(int position){
            if(!usersArrayList.isEmpty()){
                if(!usersArrayList.get(position).getPhotoUrl().isEmpty()){
                    Picasso.with(itemView.getContext()).load(usersArrayList.get(position).getPhotoUrl())
                            .placeholder(R.drawable.ic_speaker_notes_black_24px).into(notePic);
                }
                title.setText(usersArrayList.get(position).getTitle());
                mUserName.setText(usersArrayList.get(position).getName());
                time.setText(usersArrayList.get(position).getNoteTime());

            }

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);

        }
    }
}
