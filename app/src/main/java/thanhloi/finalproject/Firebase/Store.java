package thanhloi.finalproject.Firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by WIN 8 64BIT on 22/6/2016.
 */
public class Store implements InterfaceReturnAvatar {
    static String DefaultAva="https://firebasestorage.googleapis.com/v0/b/project-7404440764790796635.appspot.com/o/defaultavatar.jpg?alt=media&token=3d3618c6-d42b-4b44-91db-7c3a541448b2";
    static String UserAva="https://firebasestorage.googleapis.com/v0/b/project-7404440764790796635.appspot.com/o/defaultavatar.jpg?alt=media&token=3d3618c6-d42b-4b44-91db-7c3a541448b2";
    static String PartnerAva="https://firebasestorage.googleapis.com/v0/b/project-7404440764790796635.appspot.com/o/defaultavatar.jpg?alt=media&token=3d3618c6-d42b-4b44-91db-7c3a541448b2";;
    static InterfaceLoginReturn intf;
    public Store(InterfaceLoginReturn intf) {
        UserAva=DefaultAva;
        PartnerAva= DefaultAva;
        this.intf=intf;
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserFirebase.getAvatar(user,true,this);
        UserFirebase.getAvatar(user,false,this);
    }

    @Override
    public void ActivityResult(String param, boolean me) {
            if (me){
                if (!param.equals("null")) UserAva = param;
                Log.e("storecheck","me");
                intf.afterLogin(1);
            }
            else {
                if (!param.equals("null")) PartnerAva = param;
                Log.e("storecheck","partner");
                intf.afterLogin(2);
            }

    }

    public static String getUserAva() {
        return UserAva;
    }

    public static String getPartnerAva() {
        return PartnerAva;
    }

    public static String getDefaultAva(){return DefaultAva;}
}
