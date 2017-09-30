package application.example.com.notecard.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 20-09-2017.
 */

public class Stories {

    private String title;
    private String content;

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


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title );
        result.put("content",content );
        result.put("timeStamp", ServerValue.TIMESTAMP);


        return result;
    }

}
