package thanhloi.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import thanhloi.finalproject.Adapter.Dating;
import thanhloi.finalproject.Adapter.DatingAdapter;
import thanhloi.finalproject.Firebase.InterfaceReturnDatingList;
import thanhloi.finalproject.Firebase.UserFirebase;

public class DatingActivity extends Fragment implements InterfaceReturnDatingList {
    static int NEW_DATING=9002;
    private ProgressDialog progressDialog;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_dating,container,false);

        ImageButton addDating=(ImageButton)view.findViewById(R.id.addDating);
        addDating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.context,NewDatingActivity.class);
                startActivityForResult(intent,NEW_DATING);
                Log.e("dating","addclicked");
            }
        });
        progressDialog = ProgressDialog.show(MainActivity.context, "Please wait.",
                "Loading Dating List..!", true);
        UserFirebase.getDatingList(FirebaseAuth.getInstance().getCurrentUser(), this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==NEW_DATING){
            Toast.makeText(MainActivity.context,"added Success!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ActivityResult(ArrayList<Dating> param) {
        progressDialog.dismiss();
        Log.e("Dating Result:", String.valueOf(param.size()));
        DatingAdapter datingAdapter=new DatingAdapter(MainActivity.context,R.id.listView,param);
        ((ListView)view.findViewById(R.id.listView)).setAdapter(datingAdapter);
    }
}
