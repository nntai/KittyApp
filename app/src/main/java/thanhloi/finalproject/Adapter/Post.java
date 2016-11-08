package thanhloi.finalproject.Adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

import thanhloi.finalproject.LoginActivity;

/**
 * Created by ThanhLoi on 21-Jun-16.
 */
public class Post {
    private String person;
    private String photo;
    private String content;
    private String name;

    public Date getDate() {
        return date;
    }
    //tra ve chu cua stt
    public String getOwner(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (person.equals(user.getUid().toString())){
            return user.getDisplayName();
        }
        return LoginActivity.getStringResourceByName("ParnerName");
    }

    public Post(String person, String photo, String content, String name, Date date) {

        this.person = person;
        this.photo = photo;
        this.content = content;
        this.name = name;
        this.date = date;
    }

    private Date date;

    public String getName() {
        return name;
    }

    public Post(String person, String photo, String content, String name) {

        this.person = person;
        this.photo = photo;
        this.content = content;
        this.name = name;
    }

    public Post(String person, String photo, String content) {
        this.photo = photo;
        this.person = person;
        this.content = content;
    }

    public String getPerson(){
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPhoto() {

        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

