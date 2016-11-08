package thanhloi.finalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thanhloi.finalproject.Adapter.Event;
import thanhloi.finalproject.Adapter.EventAdapter;


public class MemoriesActivity extends Fragment {
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_memories,container,false);

        List<Event> posts = getListData();
        final ListView listView = (ListView) view.findViewById(R.id.listMemories);
        listView.setAdapter(new EventAdapter(MainActivity.context, posts));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Event event = (Event) o;
                chooseItem(event);
            }
        });
        return view;
    }

    //Lấy data từ server: // Fake data để test
    private List<Event> getListData() {
        List<Event> list = new ArrayList<Event>();
        Date date=new Date();//for tìm ngày
        Event event1 = new Event("First meeting", "Face to face",date);
        Event event2 = new Event("First fist", "Hand in hand", date);
        Event event3 = new Event("First kiss","Slips touch slips", date);
        list.add(event1);
        list.add(event2);
        list.add(event3);
        return list;
    }

    //hàm gọi khi item p được chọn
    void chooseItem(Event event)
    {

    }
}
