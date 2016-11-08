package thanhloi.finalproject.Adapter;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import thanhloi.finalproject.Firebase.Store;
import thanhloi.finalproject.LoginActivity;

/**
 * Created by ThanhLoi on 21-Jun-16.
 */
public class Comment {
    private String person;
    private String content;
    private ArrayList<String> like =new ArrayList<String>();

    public String getOwner(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //Log.e("ownerCMT",person);
        if (person.equals(user.getUid().toString())){
            return user.getDisplayName();
        }
        return LoginActivity.getStringResourceByName("ParnerName");
    }

    public String getAvatar(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (person.equals(user.getUid().toString())){
            Log.e("CmtAva:", Store.getUserAva());
            return Store.getUserAva();
        }
        Log.e("CmtAva:", Store.getPartnerAva());
        return Store.getPartnerAva();
    }

    public Comment(String person, String content) {
        this.person = person;
        this.content = content;

    }

    public Comment(String person, String content, ArrayList<String> like) {
        this.person = person;
        this.content = content;
        //this.like = like;
    }

    public Comment(String person, String avatar, String content) {
        this.person = person;
        this.content = content;
    }

    public String getPerson(){
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public void setLike(ArrayList<String> like) {
        this.like = like;
    }
}
