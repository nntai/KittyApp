package thanhloi.finalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import thanhloi.finalproject.Adapter.RequestAdapter;
import thanhloi.finalproject.Firebase.InterfaceReturnRequestList;
import thanhloi.finalproject.Firebase.Request;
import thanhloi.finalproject.Firebase.UserFirebase;

public class RequestListActivity extends Fragment implements InterfaceReturnRequestList {

    private FirebaseUser user;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_request_list,container,false);

        user= FirebaseAuth.getInstance().getCurrentUser();
        UserFirebase.getRequestList(user,this);
        Button send=(Button)view.findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView emailtext=(TextView)view.findViewById(R.id.emailText);
                String email=emailtext.getText().toString();
                UserFirebase.request(user,emailtext.getText().toString());
                Toast.makeText(MainActivity.context,"Request Sent!",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void ActivityResult(ArrayList<Request> param) {
        ListView listView=(ListView)view.findViewById(R.id.requestListView);
        RequestAdapter requestAdapter=new RequestAdapter(MainActivity.context,R.id.requestListView,param);
        listView.setAdapter(requestAdapter);
    }

    public void sendOnClick(View view) {
        TextView emailtext=(TextView)view.findViewById(R.id.emailText);
        String email=emailtext.getText().toString();
        UserFirebase.request(user,emailtext.getText().toString());
        Toast.makeText(MainActivity.context,"Request Sent!",Toast.LENGTH_SHORT).show();
    }
}
