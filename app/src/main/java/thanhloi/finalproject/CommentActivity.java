package thanhloi.finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import thanhloi.finalproject.Adapter.Comment;
import thanhloi.finalproject.Adapter.CommentAdapter;

public class CommentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        //getSupportActionBar().setIcon(R.drawable.icon);

        List<Comment> comments = getListData();
        final ListView listView = (ListView) findViewById(R.id.listComment);
        listView.setAdapter(new CommentAdapter(this, comments));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Comment comment = (Comment) o;
                chooseItem(comment);
            }
        });
    }

    //Lấy data từ server: // Fake data để test
    private List<Comment> getListData() {
        List<Comment> list = new ArrayList<Comment>();
        Comment comment1 = new Comment("Robert", "defaultavatar", "This is the first comment");
        Comment comment2 = new Comment("Nathan", "defaultavatar", "This is the second comment");
        Comment comment3 = new Comment("Robert", "defaultavatar", "This is the third comment");
        list.add(comment1);
        list.add(comment2);
        list.add(comment3);
        return list;
    }

    //hàm gọi khi item p được chọn
    void chooseItem(Comment comment)
    {

    }
}
