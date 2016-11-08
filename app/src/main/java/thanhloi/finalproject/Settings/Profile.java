package thanhloi.finalproject.Settings;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thanhloi.finalproject.R;

public class Profile extends Fragment {

    View view;
    public Profile(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_profile,container,false);
        return view;
    }
}
