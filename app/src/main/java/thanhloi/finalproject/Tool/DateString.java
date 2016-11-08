package thanhloi.finalproject.Tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by WIN 8 64BIT on 23/6/2016.
 */
public class DateString {
    public static String getTimeString(){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtTime=dateFormatGmt.format(new Date());
        return gmtTime;
    }
    public static String getTimeString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        //dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time=dateFormat.format(date);
        return time;
    }
}
