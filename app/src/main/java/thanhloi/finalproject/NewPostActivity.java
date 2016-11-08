package thanhloi.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import thanhloi.finalproject.Adapter.Post;
import thanhloi.finalproject.Firebase.ImgFirebase;
import thanhloi.finalproject.Firebase.InterfaceSendPost;
import thanhloi.finalproject.Firebase.InterfaceUploadImage;
import thanhloi.finalproject.Firebase.UserFirebase;
import thanhloi.finalproject.Tool.DownloadImg;

public class NewPostActivity extends AppCompatActivity implements InterfaceUploadImage, InterfaceSendPost {

    int NEWPOST=9002;
    static int RESULT_LOAD_IMAGE=9001;
    String url="null";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    protected void uploadImage(){
        Intent i = new Intent(Intent.ACTION_PICK);
        File pictureDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String pictureDirectoryPath=pictureDirectory.getPath();
        Uri data=Uri.parse(pictureDirectoryPath);
        i.setDataAndType(data,"image/*");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Uploading Image..!", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            ImgFirebase.upload(selectedImage,getBaseContext(),this);
        }
    }

    @Override
    public void afterUpload(String url) {
        progressDialog.dismiss();
        this.url=url;
        ImageView imageView=(ImageView)findViewById(R.id.image);
        new DownloadImg(url,imageView);
    }

    public void uploadclick(View view) {
        uploadImage();
    }

    public void postclick(View view) {
        String content=((EditText)findViewById(R.id.editText)).getText().toString();
        Post post=new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(),url,content);
        UserFirebase.sendPos(FirebaseAuth.getInstance().getCurrentUser(),post,this);
    }

    @Override
    public void afterSendPost() {
        Toast.makeText(getBaseContext(),"Post sent!",Toast.LENGTH_SHORT).show();
        finishActivity(NEWPOST);
    }
}
