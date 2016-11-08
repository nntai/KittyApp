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
import thanhloi.finalproject.Tool.DownloadImg;

/**
 * Created by ThanhLoi on 21-Jun-16.
 */
public class CommentAdapter extends BaseAdapter{
    private List<Comment> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CommentAdapter(Context aContext,  List<Comment> listData) {
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
            convertView = layoutInflater.inflate(R.layout.comment_item, null);
            holder = new ViewHolder();
            holder.Avatar = (ImageView)convertView.findViewById(R.id.avatar);
            holder.Person = (TextView)convertView.findViewById(R.id.textPerson);
            holder.Content = (TextView)convertView.findViewById(R.id.textContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = this.listData.get(position);
        holder.Person.setText(comment.getOwner());
        holder.Content.setText(comment.getContent());

        new DownloadImg(comment.getAvatar(),holder.Avatar).execute();

        //int imageId  = context.getResources().getIdentifier(comment.getAvatar() , "drawable", context.getPackageName());

        //holder.Avatar.setImageResource(imageId);
        return convertView;
    }

    static class ViewHolder {
        ImageView Avatar;
        TextView Person;
        TextView Content;
    }
}
