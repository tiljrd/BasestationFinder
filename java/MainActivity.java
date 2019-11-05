package com.baselabs.tiljo.basestationfinder;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FloatingActionButton fab;
    ImageView cellularImageView;
    Double longitude, latitude;
    private int cellId;
    private List<Double> longitudeList;
    private List<Double> latitudeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:til.jordan@outlook.de"));

                try {
                    startActivity(Intent.createChooser(emailIntent,"Bei Problemen Email " +
                            "senden mit..."));
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(view, "Kein Email-Programm gefunden", Snackbar.LENGTH_SHORT);
                }
            }
        });



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cellularImageView = findViewById(R.id.cellularImageView);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_privacy){
            Intent intent = new Intent(MainActivity.this, PrivacyActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (checkGooglePlayServices()) {
            if (id == R.id.nav_information) {
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new InformationFragment()).commit();
                fab.setAlpha(0.0f);

            } else if (id == R.id.nav_map_personal) {
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new MapPersonalFragment(cellId)).commit();
                fab.setAlpha(1f);
            } else if (id == R.id.nav_search) {

                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new SearchFragment()).commit();

                cellularImageView.setVisibility(View.INVISIBLE);

            } else if (id == R.id.nav_map_general) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new
                        MapGeneralFragment(longitude, latitude, longitudeList, latitudeList)).commit();
                fab.setAlpha(1f);
            }
        }else {
            Toast.makeText(this, "Google play services sind nicht verfügbar",
                    Toast.LENGTH_SHORT).show();
        }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        }


    // setters
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitudeList(List<Double> longitudeList) {
        this.longitudeList = longitudeList;
    }
    public void setLatitudeList(List<Double> latitudeList) {
        this.latitudeList = latitudeList;
    }
    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    // check google services available
    private boolean checkGooglePlayServices() {
        final int status = GoogleApiAvailability.getInstance().
                isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {

            // ask user to update google play services.
            Dialog dialog = GoogleApiAvailability.getInstance().
                    getErrorDialog(this, status, 1);
            dialog.show();
            return false;
        } else {
            return true;
        }
    }

}

