package thanhloi.finalproject.Tool;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class display_image extends AppCompatActivity {

    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_image);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        bitmap = bundle.getParcelable("dest");

    }
}
