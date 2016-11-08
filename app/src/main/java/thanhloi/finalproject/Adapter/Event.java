package thanhloi.finalproject.Adapter;

import java.util.Date;

/**
 * Created by ThanhLoi on 21-Jun-16.
 */
public class Event {
    private String nameEvent;
    private String description;
    private Date date;


    public Event(String name, String discription, Date date) {
        this.nameEvent = name;
        this.description = discription;
        this.date=date;
    }

    public String getNameEvent(){
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getDescription() { return description; }

    public void setDescription(String discription) {
        this.description = discription;
    }
}
