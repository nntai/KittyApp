package thanhloi.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thanhloi.finalproject.CommentActivity;
import thanhloi.finalproject.R;
import thanhloi.finalproject.Tool.DownloadImg;

/**
 * Created by ThanhLoi on 21-Jun-16.
 */
public class PostAdapter extends BaseAdapter{
    private List<Post> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public PostAdapter(Context aContext,  List<Post> listData) {
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
            convertView = layoutInflater.inflate(R.layout.post_item, null);
            holder = new ViewHolder();
            holder.Photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.Person = (TextView) convertView.findViewById(R.id.textPerson);
            holder.Content = (TextView) convertView.findViewById(R.id.textContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Post p = this.listData.get(position);
        holder.Person.setText(p.getOwner());
        holder.Content.setText(p.getContent());

        String photoUrl=p.getPhoto();
        if (photoUrl.equals("null")){
            holder.Photo.setVisibility(View.GONE);
        }
        else{
            new DownloadImg(photoUrl,holder.Photo,true,context).execute();
        }

        final ImageView likeView = (ImageView) convertView.findViewById(R.id.like);
        (likeView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                likeView.setImageResource(R.drawable.liked);
            }
        });
        convertView.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle=new Bundle();
                bundle.putString("post", p.getName());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView Photo;
        TextView Person;
        TextView Content;
    }
}
