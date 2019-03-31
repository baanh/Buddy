package com.buddy.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.buddy.main.R;
import com.buddy.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, View.OnKeyListener {

    private GoogleMap mMap;
    private AutoCompleteTextView txtAddress;
    private Geocoder gc;
    private List<Address> possibleAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtAddress = findViewById(R.id.txt_address);
        txtAddress.setThreshold(Constants.AUTOCOMPLETE_THRESHOLD);
        Intent intent = getIntent();
        txtAddress.setText(intent.getStringExtra(Constants.EXTRA_ADDRESS));
        txtAddress.setOnKeyListener(this);
        txtAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address address = possibleAddresses.get(position);
                showAddressOnMap(address);
            }
        });
        gc = new Geocoder(this, Locale.getDefault());
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
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            // If user specified location, show possible locations found
            String addressText = txtAddress.getText().toString();
            if (!TextUtils.isEmpty(addressText)) {
                showPossibleAddresses(addressText);
                return;
            }
            // If user did not specify location, show user's current location on the map
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Address address = gc.getFromLocation(latitude, longitude, 1).get(0);
            txtAddress.setText(address.getAddressLine(0));

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            LatLng currentLocation = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(currentLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method show the input address on the map using marker
     */
    private void showAddressOnMap(Address address) {
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();
        LatLng location = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
    }

    /**
     * Method checks if previous activity sends a text address. If so, show list of possible addresses
     */
    private void showPossibleAddresses(String addressText) {
        try {
            possibleAddresses = gc.getFromLocationName(addressText, 3);
            if (possibleAddresses.size() == 0) {
                Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
                return;
            }
            // Make a dropdown below the text field
            String[] addresses = new String[possibleAddresses.size()];
            for (int i = 0; i < addresses.length; i++) {
                addresses[i] = possibleAddresses.get(i).getAddressLine(0);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_singlechoice, addresses);
            txtAddress.setAdapter(adapter);
            txtAddress.showDropDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                finish();
                return true;
            case R.id.action_done:
                // Send the address back to previous activity
                Intent response = new Intent();
                response.putExtra(Constants.EXTRA_REPLY_ADDRESS, txtAddress.getText().toString());
                setResult(RESULT_OK, response);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            showPossibleAddresses(txtAddress.getText().toString());
            return true;
        }
        return false;
    }
}
