package thanhloi.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import thanhloi.finalproject.Settings.About;
import thanhloi.finalproject.Settings.Contact;
import thanhloi.finalproject.Settings.Donate;
import thanhloi.finalproject.Settings.Profile;
import thanhloi.finalproject.Settings.Share;
import thanhloi.finalproject.Settings.Wallpaper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Khởi tạo 1 toolbar mới thay cho ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Khởi tạo Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DiaryActivity diaryActivity = new DiaryActivity();
        diaryActivity.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, diaryActivity).addToBackStack(null).commit();
        context = this;
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null && user.getDisplayName()!=null){
            Toast.makeText(context,"Welcome "+user.getDisplayName(),Toast.LENGTH_SHORT).show();
        }
    }


    //Sự kiện khi nhấn vào nút back trên thiết bị
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Intent intent;
        switch (id)
        {
            case R.id.nav_profile:
                Profile profile = new Profile();
                profile.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profile).addToBackStack(null).commit();
                break;
            case R.id.nav_wallpaper:
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, wallpaper).addToBackStack(null).commit();
                break;
            case R.id.nav_contact:
                Contact contact = new Contact();
                contact.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, contact).addToBackStack(null).commit();
                break;
            case R.id.nav_share:
                Share share = new Share();
                share.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, share).addToBackStack(null).commit();
                break;
            case R.id.nav_about:
                About about = new About();
                about.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, about).addToBackStack(null).commit();
                break;
            case R.id.nav_donate:
                Donate donate = new Donate();
                donate.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, donate).addToBackStack(null).commit();
                break;

            //Features:
            case R.id.diary:
                DiaryActivity diaryActivity = new DiaryActivity();
                diaryActivity.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, diaryActivity).addToBackStack(null).commit();
                break;
            case R.id.chat:
                ChatActivity chatActivity = new ChatActivity();
                chatActivity.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, chatActivity).addToBackStack(null).commit();
                break;
            case R.id.dating:
                DatingActivity datingActivity = new DatingActivity();
                datingActivity.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, datingActivity).addToBackStack(null).commit();
                break;
            case R.id.memories:
                MemoriesActivity maps = new MemoriesActivity();
                maps.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, maps).addToBackStack(null).commit();
                break;
            case R.id.maps:
                Intent i = new Intent(getBaseContext(),MapsActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.friend:
                RequestListActivity requestListActivity = new RequestListActivity();
                requestListActivity.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, requestListActivity).addToBackStack(null).commit();
                break;
        }
        return true;
    }
}