package thanhloi.finalproject.Firebase;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

import thanhloi.finalproject.R;
import thanhloi.finalproject.Tool.DateString;

/**
 * Created by WIN 8 64BIT on 16/6/2016.
 */
public class ImgFirebase extends Activity {

    static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    static protected String getNewName(){
        DatabaseReference mainref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference img=mainref.child("img");
        String key=img.push().getKey();
        img.child(key).setValue("true");
        return key;
    }

    static public void upload(Uri selectedImage, final Context context, final InterfaceUploadImage intf){

        ContentResolver contentResolver=context.getContentResolver();
        String path=getNewName();
        //String name=path.substring(path.lastIndexOf('/'));
        //Toast.makeText(getBaseContext(), path,Toast.LENGTH_SHORT).show();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(context.getString(R.string.firebase_storage_url));

        InputStream inputStream;
        try {
            inputStream=contentResolver.openInputStream(selectedImage);
            String name= DateString.getTimeString();
            StorageReference tempref = storageRef.child(name);
            UploadTask uploadTask=tempref.putStream(inputStream);
            // Register observers to listen for when the download is done or if it fails

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(context, "Failed " + exception.toString(),Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    intf.afterUpload(downloadUrl.toString());
                    //Toast.makeText(context, "Success Upload to "+downloadUrl.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
