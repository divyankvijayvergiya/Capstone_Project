package application.example.com.notecard;

/**
 * Created by Dell on 20-09-2017.
 */

public class Users {

    private String photoUrl;
    private String title;
    private String name;
    private String email;



    private String noteTime;
    public Users(String photoUrl, String title,String name, String email, String noteTime){
        this.photoUrl=photoUrl;
        this.title=title;
        this.name=name;
        this.email=email;
        this.noteTime=noteTime;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

}
