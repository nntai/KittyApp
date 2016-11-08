package thanhloi.finalproject;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import thanhloi.finalproject.Firebase.UserFirebase;
import thanhloi.finalproject.HintPlace.Place;
import thanhloi.finalproject.HintPlace.PlaceFinder;
import thanhloi.finalproject.HintPlace.PlaceFinderListener;
import thanhloi.finalproject.Modules.DirectionFinder;
import thanhloi.finalproject.Modules.DirectionFinderListener;
import thanhloi.finalproject.Modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener,PlaceFinderListener {

    private GoogleMap mMap;
    GoogleMap googleMap;
    LatLng myPosition=null;
    LatLng partnerPosition=null;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Getting GoogleMap object from the fragment
        googleMap = mapFragment.getMap();

        // Enabling MyLocation Layer of Google Map
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
        setupMyLocation();
    }

    private void setupMyLocation() {

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            myPosition = new LatLng(latitude, longitude);

            //googleMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        }
        else Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        /*
        LocationManager service =(LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        if (location!=null)        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
        */
    }


    private boolean checkPos(){
        if(myPosition==null){
            Toast.makeText(this,"My Position haven't updated",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (partnerPosition==null){
            Toast.makeText(this,"Partner's Position haven't updated",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }





    public float distFrom(LatLng origin, LatLng destination) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(destination.latitude-origin.latitude);
        double dLng = Math.toRadians(destination.longitude-origin.longitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(origin.latitude)) * Math.cos(Math.toRadians(destination.latitude)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


    //vi tri moi cua partner
    public void updatePartnerPosition(LatLng pos){
        partnerPosition=pos;


        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.heart_pin))
                .position(partnerPosition));
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }
    //tra ve ket qua duong di tim dc:
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        mMap.clear();
        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);//hien thi thoi gian
            //((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);//hien thi quang duong

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.heartpin))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onPlaceFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding Places..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }

    }

    //tra ve ket qua cac dia diem tim duoc trong khu vuc:
    @Override
    public void onPlaceFinderSuccess(List<Place> places) {
        progressDialog.dismiss();
        mMap.clear();
        //draw circle
        LatLng center=new LatLng((myPosition.latitude+partnerPosition.latitude)/2,(myPosition.longitude+partnerPosition.longitude)/2);
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(distFrom(myPosition,partnerPosition)/2)
                .strokeColor(Color.RED)
                .strokeWidth(2)
                .fillColor(0x55000099));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 16));
        if (places!=null && places.size()>0){
            Place place;
            Toast.makeText(this,"Found:"+String.valueOf(places.size()),Toast.LENGTH_SHORT).show();
            for(int i=0;i<places.size();++i){
                place=places.get(i);
                mMap.addMarker(new MarkerOptions()
                        .title(place.Name)
                        .position(place.Location));
            }
        }

    }

    public void getWayClick(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserFirebase.getPartnerPosition(user, this);

            if (checkPos()) {
                try {
                    new DirectionFinder(this, myPosition, partnerPosition).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void hintPlaceClick(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserFirebase.getPartnerPosition(user, this);

            if (checkPos()) {
                try {
                    LatLng center=new LatLng((myPosition.latitude+partnerPosition.latitude)/2,(myPosition.longitude+partnerPosition.longitude)/2);
                    ArrayList<String> types=new ArrayList<String>();
                    types.add("cafe");
                    types.add("food");
                    new PlaceFinder(this, center, distFrom(myPosition,partnerPosition)/2, types).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendPositionLick(View view) {
        googleMap.clear();
        setupMyLocation();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserFirebase.sendPosition(user, myPosition);
    }
}
