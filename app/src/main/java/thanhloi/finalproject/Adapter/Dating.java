package thanhloi.finalproject.Adapter;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WIN 8 64BIT on 21/6/2016.
 */
public class Dating {
    private String name;
    private String address;
    private String place;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {

        this.place = place;
    }

    private Date date;
    private LatLng location;

    public Dating(String name, String place, String address, Date date, LatLng location) {
        this.name = name;
        this.place = place;
        this.address = address;
        this.date = date;
        this.location = location;
    }

    public String getDateString(){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        String time=dateFormatGmt.format(date);
        return time;
    }

    public String getMoth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (cal.get(Calendar.MONTH)+1){
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            default: return "DEC";
        }
    }

    public int getDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {

        return address;
    }

    public Date getDate() {
        return date;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
