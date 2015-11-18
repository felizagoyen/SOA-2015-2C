package ar.com.unlam.mae;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ar.com.unlam.mae.Service.PoiService;
import ar.com.unlam.mae.Utils.Poi;
import ar.com.unlam.mae.Utils.SettingsLocation;

public class AddPoiActivity extends Activity implements LocationListener {

    private Spinner spinnerCategory;
    private EditText eTxtName;
    private Button buttonAdd;
    private ArrayList<String> listCategory = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private LocationManager locationManager;
    private LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_poi);

        locationListener = this;
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        listCategory.add("Category");

        eTxtName = (EditText) findViewById(R.id.eTxtName);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategory);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(v.getContext(), AddPoiListActivity.class);
                    startActivityForResult(intent, 3);
                }
                return true;
            }
        });

        buttonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(eTxtName.getText().toString()) && !"Category".equals(listCategory.get(0))) {
                    buttonAdd.setEnabled(false);
                    buttonAdd.setText(String.valueOf("Adding Poi..."));
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                    String best = locationManager.getBestProvider(criteria, true);
                    locationManager.requestLocationUpdates(best, 1000, 0, locationListener);
                }
            }
        });

    }


    @Override
    public void onLocationChanged(Location location) {
        Poi poi = new Poi(eTxtName.getText().toString(),
                          listCategory.get(0),
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude());
        PoiService.getInstance().setPoi(poi, getApplicationContext());
        locationManager.removeUpdates(locationListener);
        finish();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                listCategory.remove(0);
                listCategory.add(data.getStringExtra("Category"));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                back();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void back() {
        onBackPressed();
    }
}
