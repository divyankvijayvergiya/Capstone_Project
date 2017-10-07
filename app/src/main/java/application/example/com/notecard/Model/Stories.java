package application.example.com.notecard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 20-09-2017.
 */

public class Stories implements Parcelable {

    private String title;
    private String content;



    private Long timeStamp;

    public Stories(){

    }
    public Stories(String title,String content){
        this.title=title;
        this.content=content;


    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title );
        result.put("content",content );
        result.put("timeStamp", ServerValue.TIMESTAMP);


        return result;
    }


    protected Stories(Parcel in) {
        title = in.readString();
        content = in.readString();
        timeStamp = in.readByte() == 0x00 ? null : in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        if (timeStamp == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(timeStamp);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Stories> CREATOR = new Parcelable.Creator<Stories>() {
        @Override
        public Stories createFromParcel(Parcel in) {
            return new Stories(in);
        }

        @Override
        public Stories[] newArray(int size) {
            return new Stories[size];
        }
    };
}