package thanhloi.finalproject.Tool;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

import thanhloi.finalproject.Firebase.InterfaceLoginReturn;
import thanhloi.finalproject.Firebase.InterfaceReturnDate;
import thanhloi.finalproject.Firebase.UserFirebase;

/**
 * Created by WIN 8 64BIT on 25/6/2016.
 */
public class NotiDate implements InterfaceReturnDate {
    static private Date begin;
    static private InterfaceLoginReturn intf;

    public NotiDate(InterfaceLoginReturn intf){
        this.intf=intf;
        UserFirebase.getDateBegin(FirebaseAuth.getInstance().getCurrentUser(),this);
    }

    static public int getCount(){
        if (begin!=null) {
            Date now = new Date();
            Calendar cBegin = Calendar.getInstance();
            cBegin.setTime(begin);
            Calendar cNow = Calendar.getInstance();
            cNow.setTime(now);
            long count =( cNow.getTimeInMillis() - cBegin.getTimeInMillis() )/ 1000 / 60 / 60 / 24;
            return (int) count;
        }
        return -1;
    }

    static public String getNoti(){
        if (begin!=null) {
            Date now = new Date();
            Calendar cBegin= Calendar.getInstance();
            cBegin.setTime(begin);
            Calendar cNow= Calendar.getInstance();
            cNow.setTime(now);
            Log.e("memorybeginst",cBegin.toString());
            Log.e("memorynowst",cNow.toString());
            Log.e("memorybegin",String.valueOf(cBegin.get(Calendar.DAY_OF_MONTH)) + "/"+ String.valueOf(cBegin.get(Calendar.MONTH))+"/"+String.valueOf(cBegin.get(Calendar.YEAR)));
            Log.e("memorynow",String.valueOf(cNow.get(Calendar.DAY_OF_MONTH)) + "/"+ String.valueOf(cNow.get(Calendar.MONTH))+"/"+String.valueOf(cNow.get(Calendar.YEAR)));
            long count=(cNow.getTimeInMillis()-cBegin.getTimeInMillis())/1000/60/60/24;

            //long count = (now.getTime() - begin.getTime()) / 1000 / 60 / 60 / 24;
            String res = "Kỷ niệm";
            if (cBegin.get(Calendar.DAY_OF_MONTH)==cNow.get(Calendar.DAY_OF_MONTH) && cBegin.get(Calendar.MONTH)==cNow.get(Calendar.MONTH)) {
                res = res+" " + String.valueOf(cNow.get(Calendar.YEAR) - cBegin.get(Calendar.YEAR)) + " năm";
            } else if (cBegin.get(Calendar.DAY_OF_MONTH)==cNow.get(Calendar.DAY_OF_MONTH)) {
                res = res +" " + String.valueOf((cNow.get(Calendar.YEAR) - cBegin.get(Calendar.YEAR))*12 +cNow.get(Calendar.MONTH)-cBegin.get(Calendar.MONTH)) + " tháng";
            } else
            if (count % 100 == 0) {
                res = res +" "+ String.valueOf(count) + " ngày";
            } else if (count % 7 == 0) {
                res = res +" " + String.valueOf(count / 30) + " tuần";
            }
            else return null;
            Log.e("Memory",res);
            return res;
        }
        return "Data haven't updated";
    }

    @Override
    public void ActivityResult(Date param) {
        Log.e("Date","returned");
        begin=param;
        intf.afterLogin(3);
        //Log.e("getnoti",getNoti());
    }
}
