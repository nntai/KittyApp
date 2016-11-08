package thanhloi.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thanhloi.finalproject.Modules.GuideActivity;
import thanhloi.finalproject.R;

/**
 * Created by WIN 8 64BIT on 21/6/2016.
 */
public class DatingAdapter extends ArrayAdapter<Dating> {
    private List<Dating> datingList = new ArrayList<Dating>();
    private Context context;

    public DatingAdapter(Context context, int textViewResourceId, ArrayList<Dating> list) {
        super(context, textViewResourceId);
        this.context = context;
        this.datingList=list;
    }

    @Override
    public void add(Dating object) {
        datingList.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.datingList.size();
    }

    public Dating getItem(int index) {
        return this.datingList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Dating datingObj = getItem(position);
        View row = convertView;
        final LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.dating_item_layout, parent, false);

        String month=datingObj.getMoth();
        int day=datingObj.getDay();
        int hour=datingObj.getHour();
        int minute=datingObj.getMinute();

        ((TextView)row.findViewById(R.id.month)).setText(month);
        ((TextView)row.findViewById(R.id.day)).setText(String.valueOf(day));
        ((TextView)row.findViewById(R.id.time)).setText(String.valueOf(hour)+":"+String.valueOf(minute));
        ((TextView)row.findViewById(R.id.name)).setText(datingObj.getName());
        ((TextView)row.findViewById(R.id.place)).setText(datingObj.getPlace());
        ((TextView)row.findViewById(R.id.address)).setText(datingObj.getAddress());
        ((ImageView)row.findViewById(R.id.find)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GuideActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle args = new Bundle();
                args.putParcelable("dest", datingObj.getLocation());
                intent.putExtra("bundle",args);
                context.startActivity(intent);
            }
        });
        Log.e("DatingListAt",String.valueOf(position)+":"+datingObj.getName());
        return row;
    }
}
