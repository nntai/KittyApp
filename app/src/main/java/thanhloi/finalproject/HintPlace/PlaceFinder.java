package thanhloi.finalproject.HintPlace;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import thanhloi.finalproject.LoginActivity;

public class PlaceFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private PlaceFinderListener listener;
    private LatLng origin;
    private  double radius;
    private ArrayList<String> types;
    List<Place> Places = new ArrayList<Place>();

    public PlaceFinder(PlaceFinderListener listener, LatLng origin, double radius, ArrayList<String> typeslist){
        this.types=typeslist;
        this.listener=listener;
        this.origin=origin;
        this.radius=radius;
        Places = new ArrayList<Place>();
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onPlaceFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String url=DIRECTION_URL_API;
        url=url +"location="+ String.valueOf(origin.latitude)+","+String.valueOf(origin.longitude);
        url=url + "&radius="+String.valueOf(radius);
        url=url+ "&types="+types.get(0);
        for(int i=1; i<types.size(); ++i){
            url=url+"|"+types.get(i);
        }
        url=url+"&pagetoken&key="+ LoginActivity.getStringResourceByName("google_map_api");
        return url;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            String token=null;
            do{
                try {
                    Log.e("placeurl:",link);
                    URL url = new URL(link);
                    InputStream is = url.openConnection().getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    token=null;
                    token=parseJSon(buffer.toString());
                    is.close();
                    reader.close();
                    if (token!=null){
                        Thread.sleep(3000);
                        link=createUrlToken(token);
                    }
                    else break;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (token!=null);
            return "done";
        }

        @Override
        protected void onPostExecute(String res) {
            listener.onPlaceFinderSuccess(Places);
        }
    }

    private String createUrlToken(String token){
        String url=DIRECTION_URL_API;
        url=url+"pagetoken="+token;
        url=url+"&key="+LoginActivity.getStringResourceByName("google_map_api");
        //Log.e("urlwithtoken:",url);
        return url;
    }

    private String parseJSon(String data) throws JSONException {
        String token=null;
        if (data == null) {
            Log.e("data buffer:","null"+Places.size());
            return null;
        }
        else Log.e("data buffer",data);
        JSONObject jsonData = new JSONObject(data);

        if (jsonData.has("next_page_token")) token=jsonData.getString("next_page_token");

        JSONArray jsonPlaces = jsonData.getJSONArray("results");
        Log.e("testjson",String.valueOf(jsonPlaces.length()));
        for (int i = 0; i < jsonPlaces.length(); i++) {
            JSONObject jsonPlace = jsonPlaces.getJSONObject(i);
            Place place = new Place();

            JSONObject geometryObject = jsonPlace.getJSONObject("geometry");
            JSONObject locationObject = geometryObject.getJSONObject("location");
            LatLng position = new LatLng(Double.valueOf(locationObject.getString("lat")), Double.valueOf(locationObject.getString("lng")));
            place.Location = position;
            place.Name = jsonPlace.getString("name");
            Log.e("place:", Places.size() + ":" + place.Name);
            Places.add(place);
        }
        if (token==null) Log.e("token","null at"+Places.size());
        return token;
    }

}
