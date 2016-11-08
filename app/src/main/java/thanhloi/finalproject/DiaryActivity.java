package thanhloi.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import thanhloi.finalproject.Adapter.Post;
import thanhloi.finalproject.Adapter.PostAdapter;
import thanhloi.finalproject.Firebase.InterfaceReturnPostList;
import thanhloi.finalproject.Firebase.Store;
import thanhloi.finalproject.Firebase.UserFirebase;
import thanhloi.finalproject.Tool.DownloadImg;
import thanhloi.finalproject.Tool.NotiDate;

public class DiaryActivity extends Fragment implements InterfaceReturnPostList{

    int NEWPOST=9002;
    private ProgressDialog progressDialog;
    //Lấy data từ server: // Fake data để test
    private List<Post> getListData() {
        List<Post> list = new ArrayList<Post>();
        Post p1 = new Post("Robert", "defaultavatar", "This is the first post!");
        Post p2 = new Post("Nathan", "defaultavatar", "This is the second post!");
        Post p3 = new Post("Robert", "defaultavatar", "This is the third post!");
        list.add(p1);
        list.add(p2);
        list.add(p3);
        return list;
    }

    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_diary,container,false);

        List<Post> posts = getListData();
        final ListView listView = (ListView) view.findViewById(R.id.listPost);
        PostAdapter padapter = new PostAdapter(MainActivity.context, posts);
        listView.setAdapter(padapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Post p = (Post) o;
                chooseItem(p);
            }
        });

        //

        progressDialog = ProgressDialog.show(MainActivity.context, "Please wait.",
                "Loading Post List..!", true);
        UserFirebase.getPos(FirebaseAuth.getInstance().getCurrentUser(), this);

        ImageView ava1=(ImageView)view.findViewById(R.id.avatar1);
        ImageView ava2=(ImageView)view.findViewById(R.id.avatar2);

        Log.e("ava1", Store.getUserAva());
        new DownloadImg(Store.getUserAva(),ava1).execute();
        Log.e("ava2",Store.getPartnerAva());
        new DownloadImg(Store.getPartnerAva(),ava2).execute();
        //List<Post> posts = getListData();

        ImageButton addPost=(ImageButton)view.findViewById(R.id.addPos);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.context,NewPostActivity.class);
                startActivityForResult(intent,NEWPOST);
            }
        });
        TextView dayTv=(TextView)view.findViewById(R.id.day);
        String dayString= String.valueOf(NotiDate.getCount()) + " days";
        Log.e("day:",dayString);
        dayTv.setText(dayString);

        if(NotiDate.getNoti()!=null){
            TextView notiTv=(TextView)view.findViewById(R.id.notiday);
            String notiString= String.valueOf(NotiDate.getNoti());
            Log.e("day:",notiString);
            notiTv.setText(notiString);
        }
        else {
            TextView notiTv=(TextView)view.findViewById(R.id.notiday);
            notiTv.setVisibility(View.GONE);
        }

        //
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==NEWPOST){
            /*
            Fragment frg = null;
            frg = getFragmentManager().findFragmentByTag("DiaryActivity");
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();*/
        }
    }

    //hàm gọi khi item p được chọn
    void chooseItem(Post p)
    {
        Toast.makeText(MainActivity.context, "Hehe", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ActivityResult(ArrayList<Post> posts) {
        progressDialog.dismiss();
        final ListView listView = (ListView) view.findViewById(R.id.listPost);
        listView.setAdapter(new PostAdapter(MainActivity.context, posts));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Post p = (Post) o;
                chooseItem(p);
            }
        });
    }
}
