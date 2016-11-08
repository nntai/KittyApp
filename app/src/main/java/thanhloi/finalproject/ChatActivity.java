package thanhloi.finalproject;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import thanhloi.finalproject.Adapter.ChatArrayAdapter;
import thanhloi.finalproject.Adapter.ChatMessage;
import thanhloi.finalproject.Firebase.UserFirebase;

public class ChatActivity extends Fragment {

    FirebaseUser user;
    String msgFolder="msg";
    DatabaseReference myRef;
    DatabaseReference msgRef;
    ChatArrayAdapter chatArrayAdapter;
    ListView listView;
    EditText chatText;
    private boolean side = true;
    String room;
    ChildEventListener childEventListener;
    private static DatabaseReference mainref = FirebaseDatabase.getInstance().getReference();

    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_chat,container,false);

        Firebase.setAndroidContext(MainActivity.context);

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        UserFirebase.setStatus(user,true);

        //set up view:
        ImageView buttonSend = (ImageView) view.findViewById(R.id.send);

        listView= (ListView) view.findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(MainActivity.context, R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) view.findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        receiveChatMessage();
        return view;
    }

    public void onStop(){
        UserFirebase.setStatus(user,false);
        mainref.child("data").child(room).child("msg").removeEventListener(childEventListener);
    }

    private boolean sendChatMessage() {
        //update firebase
        String msg=chatText.getText().toString();
        UserFirebase.sendMessage(user,msg);
        //importChatMessage(true,msg);

        chatText.setText("");
        return true;
    }

    private  void importChatMessage(boolean side, String msg){
        chatArrayAdapter.add(new ChatMessage(side,msg ));
    }

    private void receiveChatMessage() {
        DatabaseReference roomRef=mainref.child("user").child(user.getUid()).child("room");
        //Toast.makeText(getBaseContext(),"start receiving",Toast.LENGTH_SHORT).show();               //toast
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getBaseContext(),"listen to room",Toast.LENGTH_SHORT).show();        //toast
                room=dataSnapshot.getValue().toString();
                DatabaseReference msgRef=mainref.child("data").child(room).child("msg");
                Query queryRef = msgRef.orderByKey().limitToLast(1);
                childEventListener= new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                            String uid=dataSnapshot.child("user").getValue().toString();
                            String msg=dataSnapshot.child("msg").getValue().toString();
                            //Toast.makeText(getBaseContext(),uid+":"+msg,Toast.LENGTH_SHORT).show(); //toast
                            if (uid.equals(user.getUid())){
                                importChatMessage(true,msg);
                            }
                            else{
                                importChatMessage(false,msg);
                            }


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                msgRef.addChildEventListener(childEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
