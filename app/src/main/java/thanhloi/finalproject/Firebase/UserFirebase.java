package thanhloi.finalproject.Firebase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import thanhloi.finalproject.Adapter.Comment;
import thanhloi.finalproject.Adapter.Dating;
import thanhloi.finalproject.Adapter.Post;
import thanhloi.finalproject.MapsActivity;
import thanhloi.finalproject.Tool.DateString;

/**
 * Created by WIN 8 64BIT on 17/6/2016.
 */
public class UserFirebase {
    private static DatabaseReference mainref = FirebaseDatabase.getInstance().getReference();

    static public void setupAcc(final FirebaseUser user, final InterfaceAfterSetupAcc intf){

        final DatabaseReference userRef=mainref.child("user").child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("room")){
                    DatabaseReference roomRef=mainref.child("data");
                    String room=roomRef.push().getKey();
                    userRef.child("room").setValue(room);
                    userRef.child("partner").setValue("open");
                }
                intf.AfterSetup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static public void sendMessage(final FirebaseUser user, final String msg){
        DatabaseReference roomRef=mainref.child("user").child(user.getUid()).child("room");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    String RoomName = dataSnapshot.getValue().toString();
                    DatabaseReference msgRef = mainref.child("data").child(RoomName).child("msg");

                    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String gmtTime = dateFormatGmt.format(new Date());
                    DatabaseReference tempref = msgRef.child(gmtTime);

                    Map<String, String> obj = new HashMap<String, String>();
                    obj.put("user", user.getUid());
                    obj.put("msg", msg);
                    tempref.setValue(obj);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void setAvatar(final FirebaseUser user, final String url){
        DatabaseReference roomRef=mainref.child("user").child(user.getUid()).child("room");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    String RoomName = dataSnapshot.getValue().toString();
                    DatabaseReference avaRef = mainref.child("data").child(RoomName).child("pic").child(user.getUid());
                    avaRef.setValue(user.getPhotoUrl().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void getAvatar(final FirebaseUser user, final boolean me, final InterfaceReturnAvatar intf){
        Log.e("getAva","start get ava");
        DatabaseReference roomRef=mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null) {
                    intf.ActivityResult("null",me);
                    return;
                }
                String RoomName=dataSnapshot.child("room").getValue().toString();
                String Partner=dataSnapshot.child("partner").getValue().toString();
                DatabaseReference partnerPosRef=mainref.child("data").child(RoomName).child("pic");
                partnerPosRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ava=null;
                        if (me){
                            ava= dataSnapshot.child(user.getUid().toString()).getValue().toString();
                        }
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            if (me && data.getKey().toString().equals(user.getUid().toString())){
                                ava=data.getValue().toString();
                                break;
                            }
                            else{
                                ava=data.getValue().toString();
                            }
                        }
                        intf.ActivityResult(ava,me);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static public void setStatus(FirebaseUser user, boolean stt){
        if (user!=null){
            DatabaseReference ref = mainref.child("user");
            DatabaseReference myRef=ref.child(user.getUid()).child("stt");
            if (stt){
                myRef.setValue("online");
            }
            else myRef.setValue("offline");
        }
    }

    static public void request(final FirebaseUser user, final String email){
        if (user!=null){
            //xoa request cu:
            DatabaseReference ref = mainref.child("user");
            DatabaseReference saveRequestRef=ref.child(user.getUid());
            ref.child(user.getUid()).child("room").removeValue();//xoa room
            saveRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String oldEmail=(String)dataSnapshot.child("partner").getValue();
                    if (oldEmail!=null && oldEmail!=email){
                        DatabaseReference requestRef=mainref.child("request").child(oldEmail.replace('.','_')).child(user.getUid());
                        if (requestRef!=null) requestRef.removeValue();
                        //set new email:
                    }
                    mainref.child("user").child(user.getUid()).child("partner").setValue(email);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                //tao room moi:
                Map<String,String> RoomData=new HashMap<String,String>();
                RoomData.put(user.getUid(),"true");
                RoomData.put("email",user.getEmail());
                RoomData.put("open","true");
                String key=mainref.child("data").push().getKey();
                mainref.child("data").child(key).setValue(RoomData);
                ref.child(user.getUid()).child("room").setValue(key);

            //gui request
            DatabaseReference requestRef=mainref.child("request").child(email.replace('.','_'));
            Map<String,String> data=new HashMap<String,String>();
            data.put("uid",user.getUid());
            data.put("email",user.getEmail());
            data.put("room",key);
            requestRef.child(user.getUid()).setValue(data);

        }
    }
    static public void acceptReq(final FirebaseUser user, final String uid){
        if (user!=null){
            DatabaseReference requestRef=mainref.child("request").child(user.getEmail().replace('.','_'));

            if(requestRef.child(uid)!=null){
                //xoa trang thai trong room cu:
                DatabaseReference roomRef=mainref.child("user").child(user.getUid()).child("room");
                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue()!=null) {
                            Log.e("oldroom", dataSnapshot.getValue().toString());
                            mainref.child("data").child(dataSnapshot.getValue().toString()).child("open").setValue("true");
                            mainref.child("data").child(dataSnapshot.getValue().toString()).child(user.getUid()).removeValue();
                        }
                        else Log.e("oldroom", "oldroom=null");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //xoa request
                requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //cap nhat room trong stt:
                        DataSnapshot request=dataSnapshot.child(uid);
                        String room=request.child("room").getValue().toString();
                        mainref.child("user").child(user.getUid()).child("room").setValue(room);

                        //thay doi partner cua userid:
                        DatabaseReference emailorigin=mainref.child("data").child(room).child("email");
                        emailorigin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String email=dataSnapshot.getValue().toString();
                                mainref.child("user").child(user.getUid()).child("partner").setValue(email);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //them gia tri nao room:
                        mainref.child("data").child(room).child(user.getUid()).setValue("true");
                        mainref.child("data").child(room).child("open").setValue("false");
                        //xoa toan bo request
                        mainref.child("request").child(user.getEmail().replace('.','_')).removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
    static public void getRequestList(FirebaseUser user, final InterfaceReturnRequestList intf ){
        DatabaseReference reqRef=mainref.child("request").child(user.getEmail().replace('.','_'));
        reqRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email, room,uid;
                ArrayList<Request> res=new ArrayList<Request>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    email=data.child("email").getValue().toString();
                    uid=data.child("uid").getValue().toString();
                    room=data.child("room").getValue().toString();
                    Request tempReq=new Request(email, room,uid);
                    Log.e("room",tempReq.getEmail()+tempReq.getRoom());
                    res.add(tempReq);
                };
                intf.ActivityResult(res);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void sendPosition(final FirebaseUser user, final LatLng position){
        Log.e("Pos","start sending pos");
        DatabaseReference roomRef=mainref.child("user").child(user.getUid()).child("room");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    String RoomName = dataSnapshot.getValue().toString();
                    DatabaseReference posRef = mainref.child("data").child(RoomName).child("pos").child(user.getEmail().replace('.', '_'));
                    Log.e("Pos", "send done!!" + position.toString());
                    posRef.setValue(position);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void getPartnerPosition(final FirebaseUser user, final Activity activity){
        Log.e("GetPos","start sending pos");
        DatabaseReference roomRef=mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue()==null) return;
                String RoomName=dataSnapshot.child("room").getValue().toString();
                String Partner=dataSnapshot.child("partner").getValue().toString();
                DatabaseReference partnerPosRef=mainref.child("data").child(RoomName).child("pos").child(Partner.replace('.','_'));
                partnerPosRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("latitude").getValue()!=null) {
                            double latitude = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                            double longitude = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                            LatLng pos = new LatLng(latitude, longitude);
                            Toast.makeText(activity.getBaseContext(), "New Partner's Position", Toast.LENGTH_SHORT).show();
                            ((MapsActivity) activity).updatePartnerPosition(pos);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static public void getDatingList(FirebaseUser user, final InterfaceReturnDatingList intf) {
        Log.e("DatingList", "getDatingList");
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue()==null){
                    intf.ActivityResult(new ArrayList<Dating>());
                }
                String RoomName = dataSnapshot.child("room").getValue().toString();
                DatabaseReference datingRef = mainref.child("data").child(RoomName).child("dating");
                datingRef.orderByKey();
                datingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Dating> list = new ArrayList<Dating>();
                        Log.e("GetDatingList", "Size:" + String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String name = data.child("name").getValue().toString();
                            Log.e("DatingData",name);
                            //String note=data.child("note").getValue().toString();
                            String place = data.child("place").child("name").getValue().toString();
                            String address = data.child("place").child("address").getValue().toString();
                            double lat = Double.parseDouble(data.child("place").child("lat").getValue().toString());
                            double lng = Double.parseDouble(data.child("place").child("lng").getValue().toString());
                            LatLng location = new LatLng(lat, lng);
                            String dateStr = data.getKey().toString();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                            Date date = new Date();
                            try {
                                date = dateFormat.parse(dateStr);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Dating dating = new Dating(name, place, address, date, location);
                            list.add(dating);
                        }
                        intf.ActivityResult(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static public void sendDating(FirebaseUser user, final Dating dating) {
        Log.e("sendDating", "getDatingList");
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue()==null) return;
                String RoomName = dataSnapshot.child("room").getValue().toString();
                DatabaseReference datingRef = mainref.child("data").child(RoomName).child("dating");

                DatabaseReference newDatingRef=datingRef.child(dating.getDateString());
                newDatingRef.child("name").setValue(dating.getName());
                newDatingRef.child("note").setValue("note");
                DatabaseReference placeRef=newDatingRef.child("place");
                placeRef.child("name").setValue(dating.getPlace());
                placeRef.child("address").setValue(dating.getAddress());
                placeRef.child("lat").setValue(dating.getLocation().latitude);
                placeRef.child("lng").setValue(dating.getLocation().longitude);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static public void getPos(FirebaseUser user, final InterfaceReturnPostList intf){
        Log.e("PostList", "getPostList");
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue() == null){
                    intf.ActivityResult(new ArrayList<Post>());
                    return;
                }
                String RoomName = dataSnapshot.child("room").getValue().toString();
                Log.e("PostListRoom", RoomName);
                DatabaseReference datingRef = mainref.child("data").child(RoomName).child("diary");
                datingRef.orderByKey();
                datingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Post> list = new ArrayList<Post>();
                        Log.e("getPostList", "Size:" + String.valueOf(dataSnapshot.getChildrenCount()));
                        Log.e("Path", dataSnapshot.getKey().toString());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String name = data.getKey().toString();
                            Log.e("DatingData",name);
                            //String note=data.child("note").getValue().toString();
                            String content = data.child("content").getValue().toString();
                            String photo = data.child("photo").getValue().toString();
                            String person = data.child("own").getValue().toString();
                            String dateStr=name.substring(0,19);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                            Date date = new Date();
                            try {
                                date = dateFormat.parse(dateStr);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Post post = new Post(person,photo,content,name,date);
                            list.add(post);
                        }
                        intf.ActivityResult(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void getCmt(FirebaseUser user, final String sttName, final InterfaceReturnCmt intf){

        Log.e("CmtList", "getCmtList");
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue()==null){
                    intf.ActivityResult(new ArrayList<Comment>());
                    return;
                }
                String RoomName = dataSnapshot.child("room").getValue().toString();
                DatabaseReference SttRef = mainref.child("data").child(RoomName).child("diary").child(sttName);
                SttRef.orderByKey();
                SttRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Comment> list = new ArrayList<Comment>();
                        Log.e("getCmtList", "Size:" + String.valueOf(dataSnapshot.getChildrenCount()));
                        DataSnapshot cmtdata=dataSnapshot.child("cmt");
                        Log.e("CmtPath", dataSnapshot.getKey().toString());
                        for (DataSnapshot data : cmtdata.getChildren()) {
                            String content=data.child("content").getValue().toString();
                            String name=data.getKey();
                            String owner=name.substring(20);
                            /*
                            ArrayList<String> likes=new ArrayList<String>();
                            for(DataSnapshot likesdata: data.child("like").getChildren()) {
                                String like=likesdata.getKey();
                                likes.add(like);
                            }
                            */
                            Comment cmt=new Comment(owner,content);
                            list.add(cmt);
                        }
                        intf.ActivityResult(list);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static public void sendCmt(FirebaseUser user, final Comment cmt, final String stt, final InterfaceSendCmt intf){
        Log.e("Cmt", "sendCmt "+stt);
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue()!=null) {
                    String RoomName = dataSnapshot.child("room").getValue().toString();
                    String name = DateString.getTimeString() + " " + cmt.getPerson();
                    DatabaseReference CmtRef = mainref.child("data").child(RoomName).child("diary").child(stt).child("cmt").child(name);
                    CmtRef.child("content").setValue(cmt.getContent());
                }
                intf.AfterSendCmt();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void sendPos(FirebaseUser user, final Post stt, final InterfaceSendPost intf){
        Log.e("Cmt", "sendCmt "+stt);
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.child("room").getValue().toString();
                String name= DateString.getTimeString()+" " +stt.getPerson();
                DatabaseReference SttRef = mainref.child("data").child(RoomName).child("diary").child(name);
                SttRef.child("content").setValue(stt.getContent());
                SttRef.child("photo").setValue(stt.getPhoto());
                SttRef.child("own").setValue(stt.getPerson());
                intf.afterSendPost();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    static public void setDateBegin(FirebaseUser user, final Date date){
        DatabaseReference roomRef=mainref.child("user").child(user.getUid()).child("room");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue().toString();
                DatabaseReference dateRef = mainref.child("data").child(RoomName).child("begin");
                String dateStr = DateString.getTimeString(date);
                dateRef.setValue(dateStr);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    static public void getDateBegin(FirebaseUser user, final InterfaceReturnDate intf){
        Log.e("Date", "getDate");
        DatabaseReference roomRef = mainref.child("user").child(user.getUid());
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("room").getValue()==null){
                    Log.e("Date","return");
                    intf.ActivityResult(new Date());
                    return;
                }
                String RoomName = dataSnapshot.child("room").getValue().toString();
                DatabaseReference dateRef = mainref.child("data").child(RoomName).child("begin");
                dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Date date = new Date();
                        if (dataSnapshot.getValue()!=null) {
                            String dateStr = dataSnapshot.getValue().toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
                            try {
                                date = dateFormat.parse(dateStr);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        Log.e("Date","return");
                        intf.ActivityResult(date);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
