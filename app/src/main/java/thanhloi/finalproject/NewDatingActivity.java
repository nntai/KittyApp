package thanhloi.finalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import thanhloi.finalproject.Adapter.Dating;
import thanhloi.finalproject.Firebase.UserFirebase;

public class NewDatingActivity extends FragmentActivity {

    String TAG="GAutocomplete";
    int NEW_DATING=9002;
    static EditText DateEdit;
    static EditText TimeEdit;
    LatLng location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dating);
        Log.e("newdating","setcontentdone");

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        location=new LatLng(0,0);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e(TAG, "Place: " + place.getName());
                //Toast.makeText(getBaseContext(),place.getName()+" is choosen!",Toast.LENGTH_SHORT).show();
                ((EditText)findViewById(R.id.address)).setText(place.getAddress());
                TextView placename = (TextView)findViewById(R.id.place);
                placename.setText(place.getName());
                location=place.getLatLng();
                Intent output = new Intent();
              //output.putExtra(, num1);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });

        DateEdit = (EditText) findViewById(R.id.date);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });
        TimeEdit = (EditText) findViewById(R.id.time);
        TimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonTimePickerDialog(v);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addNewDatingClick(View view) {
        String name= ((EditText)findViewById(R.id.name)).getText().toString();
        String note= ((EditText)findViewById(R.id.note)).getText().toString();
        String address= ((EditText)findViewById(R.id.address)).getText().toString();
        String nameplace= ((TextView)findViewById(R.id.place)).getText().toString();
        String datestr=((TextView)findViewById(R.id.date)).getText().toString() + " " + ((TextView)findViewById(R.id.time)).getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy hh:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Dating dating=new Dating(name,nameplace,address,date,location);
        UserFirebase.sendDating(FirebaseAuth.getInstance().getCurrentUser(),dating);
        try {
            Thread.sleep(2000);
            finishActivity(NEW_DATING);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            TimeEdit.setText(hourOfDay + ":" + minute+":00");
        }
    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            DateEdit.setText(day + ":" + (month + 1) + ":" + year);
        }
    }
}
