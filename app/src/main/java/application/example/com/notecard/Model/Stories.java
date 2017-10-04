package application.example.com.notecard.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 20-09-2017.
 */

public class Stories {

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
        Map<String, String> timestamp=ServerValue.TIMESTAMP;
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sfd.format(new Date(String.valueOf(timestamp)));
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title );
        result.put("content",content );
        result.put("timeStamp", sfd);


        return result;
    }

}
