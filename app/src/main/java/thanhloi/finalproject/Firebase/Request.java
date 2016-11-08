package thanhloi.finalproject.Firebase;

/**
 * Created by WIN 8 64BIT on 18/6/2016.
 */
public class Request {
    private  String id;
    private String email;
    private String room;
    public Request(String Email, String Room, String ID){
        email=Email;
        room=Room;
        id=ID;
    }

    public String getId() {
        return id;
    }

    public String getRoom() {
        return room;
    }

    public String getEmail() {
        return email;
    }
}
