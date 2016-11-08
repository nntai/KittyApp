package thanhloi.finalproject.Tool;

/**
 * Created by WIN 8 64BIT on 22/6/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImg extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView bmp;
    private boolean isFit;
    private Context context;

    public DownloadImg(String url, ImageView bmp) {
        this.url = url;
        this.bmp = bmp;
        isFit=false;
    }

    public DownloadImg(String url, ImageView bitmap, boolean isFit, Context context) {
        this.url = url;
        this.bmp=bitmap;
        this.isFit=isFit;
        this.context=context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.context,"OutOfMemory",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    LinearLayout.LayoutParams calculateSize(int w,int h,int androidw,int androidh){
        if (1.0*w/androidw>1.0*h/androidh){
            double c=1.0*androidw/w;
            w=androidw;
            h=(int)(c*h);
            Log.e("sizechanged",String.valueOf(h)+"x"+String.valueOf(w)+" c="+String.valueOf(c));
        }
        else{
            double c=1.0*androidh/h;
            h=androidh;
            w=(int)(c*w);
            Log.e("sizechanged",String.valueOf(h)+"x"+String.valueOf(w)+" c="+String.valueOf(c));
        }
        return new LinearLayout.LayoutParams(w, h);

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result == null)
        {
            int imageId  = context.getResources().getIdentifier("defaultimage" , "drawable", context.getPackageName());
            bmp.setImageResource(imageId);
            return;
        }
        Log.e("DownloadImgDone:",url);
        super.onPostExecute(result);
        if(isFit){
            int h=result.getHeight();
            int w=result.getWidth();
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            int androidw = metrics.widthPixels;
            int androidh = (int)(metrics.heightPixels*0.5);

            Log.e("size",String.valueOf(androidh)+"x"+String.valueOf(androidw));
            Log.e("sizeimage",String.valueOf(h)+"x"+String.valueOf(w));

            //result.setHeight(h);
            //result.setWidth(w);

            LinearLayout.LayoutParams layoutParams = calculateSize(w,h,androidw,androidh);

            Log.e("size",String.valueOf(h)+"x"+String.valueOf(w));
            bmp.setLayoutParams(layoutParams);

        }
        bmp.setImageBitmap(result);
    }

}
