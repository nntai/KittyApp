package thanhloi.finalproject.Settings;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thanhloi.finalproject.R;

public class Donate extends Fragment {

    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_donate,container,false);
        return view;
    }
}
