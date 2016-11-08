package thanhloi.finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thanhloi.finalproject.R;

/**
 * Created by ThanhLoi on 21-Jun-16.
 */
public class EventAdapter extends BaseAdapter{
    private List<Event> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public EventAdapter(Context aContext,  List<Event> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.event_item, null);
            holder = new ViewHolder();
            holder.Photo = (ImageView) convertView.findViewById(R.id.EventPhoto);
            holder.NameEvent = (TextView) convertView.findViewById(R.id.textNameEvent);
            holder.Description = (TextView) convertView.findViewById(R.id.textDescription);
            holder.Location = (TextView) convertView.findViewById(R.id.textLocation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event event = this.listData.get(position);
        holder.NameEvent.setText(event.getNameEvent());
        holder.Description.setText(event.getDescription());
        //holder.Location.setText(event.getLocation());

        //int imageId  = context.getResources().getIdentifier(event.getPhoto() , "drawable", context.getPackageName());

       // holder.Photo.setImageResource(imageId);
        return convertView;
    }

    static class ViewHolder {
        ImageView Photo;
        TextView NameEvent;
        TextView Description;
        TextView Location;
    }
}
