package thanhloi.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class LoginSuccess extends FragmentActivity{

    static int RESULT_LOAD_IMAGE=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        setupView();
    }

    private void setupView(){

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null) {
            //UserFirebase.request(user,"tainnt85@gmail.com");
            //UserFirebase.acceptReq(user,"55APyuiXlRcff5x69R1rokn3t3U2");

            TextView textv = (TextView) findViewById(R.id.textv);
            textv.setText("Username: " + user.getDisplayName().toString());
            if (user.getEmail() != null) {
                textv.append("\nEmail: " + user.getEmail().toString());
            }
            if (user.getPhotoUrl() != null) {
                WebView webView = (WebView) findViewById(R.id.webView);
                webView.loadUrl(user.getPhotoUrl().toString());
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            //ImgFirebase.upload(selectedImage,MainActivity.context);
        }
    }

    public void uploadImageClick(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        File pictureDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String pictureDirectoryPath=pictureDirectory.getPath();
        Uri data=Uri.parse(pictureDirectoryPath);
        i.setDataAndType(data,"image/*");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void startChatClick(View view) {
        Intent i=new Intent(getBaseContext(),ChatActivity.class);
        startActivity(i);
    }

    public void logOutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void requestListClick(View view) {
        Intent intent=new Intent(getBaseContext(),RequestListActivity.class);
        startActivity(intent);
    }

    public void MapOnClick(View view) {
        Intent intent=new Intent(getBaseContext(),MapsActivity.class);
        startActivity(intent);
    }

    public void DatingClick(View view) {
        Intent intent=new Intent(getBaseContext(),DatingActivity.class);
        startActivity(intent);
    }

    public void diaryClick(View view) {
        Intent intent=new Intent(getBaseContext(),DiaryActivity.class);
        startActivity(intent);
    }

    public void memoriesClick(View view) {
        Intent intent=new Intent(getBaseContext(),MemoriesActivity.class);
        startActivity(intent);
    }
}
