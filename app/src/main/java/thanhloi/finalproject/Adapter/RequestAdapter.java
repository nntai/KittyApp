package thanhloi.finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import thanhloi.finalproject.Firebase.Request;
import thanhloi.finalproject.Firebase.UserFirebase;
import thanhloi.finalproject.R;

/**
 * Created by WIN 8 64BIT on 18/6/2016.
 */
public class RequestAdapter extends ArrayAdapter<Request> {
    private List<Request> requestList = new ArrayList<Request>();
    private Context context;

    @Override
    public void add(Request object) {
        requestList.add(object);
        super.add(object);
    }

    public RequestAdapter(Context context, int textViewResourceId,List<Request> list ) {
        super(context, textViewResourceId, list);
        this.context = context;
        requestList=list;
    }

    public int getCount() {
        return this.requestList.size();
    }

    public Request getItem(int index) {
        return this.requestList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Request requestObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.request_layout, parent, false);
        TextView email, room;
        email=(TextView)row.findViewById(R.id.email);
        email.setText(requestObj.getEmail());
        room=(TextView)row.findViewById(R.id.room);
        room.setText(requestObj.getRoom());

        Button accept=(Button)row.findViewById(R.id.acceptButton);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),requestObj.getId(), Toast.LENGTH_SHORT).show();
                UserFirebase.acceptReq(FirebaseAuth.getInstance().getCurrentUser(),requestObj.getId());
                Toast.makeText(getContext(),"Accepted!",Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    }

}
