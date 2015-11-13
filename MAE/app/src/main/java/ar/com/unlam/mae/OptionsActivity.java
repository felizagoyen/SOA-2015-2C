package ar.com.unlam.mae;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.com.unlam.mae.Service.PoiService;
import ar.com.unlam.mae.Utils.OptionsLocation;
import ar.com.unlam.mae.Utils.Poi;

public class OptionsActivity extends Activity implements View.OnClickListener, LocationListener {

    TextView enabledValue;

    TextView minusRadius;
    TextView plusRadius;
    TextView radiusValue;
    TextView radiusText;

    TextView minusRefreshTime;
    TextView plusRefreshTime;
    TextView refreshTimeValue;
    TextView refreshTimeText;

    Button searchButton;
    TextView distance;

    OptionsLocation optionsLocation = OptionsLocation.getInstance();
    LocationManager locationManager;

    Poi poi;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        enabledValue = (TextView) findViewById(R.id.txtEnabledValue);
        minusRadius = (TextView) findViewById(R.id.txtMinusRadius);
        plusRadius = (TextView) findViewById(R.id.txtPlusRadius);
        radiusValue = (TextView) findViewById(R.id.txtRadiusValue);
        radiusText = (TextView) findViewById(R.id.txtRadius);

        minusRefreshTime = (TextView) findViewById(R.id.txtMinusRefresh);
        plusRefreshTime = (TextView) findViewById(R.id.txtPlusRefresh);
        refreshTimeValue = (TextView) findViewById(R.id.txtRefreshValue);
        refreshTimeText = (TextView) findViewById(R.id.txtRefresh);

        searchButton = (Button) findViewById(R.id.btnSearch);
        distance = (TextView) findViewById(R.id.txtDistance);

        enabledValue.setOnClickListener(this);
        minusRadius.setOnClickListener(this);
        plusRadius.setOnClickListener(this);
        minusRefreshTime.setOnClickListener(this);
        plusRefreshTime.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        radiusValue.setText(String.valueOf(optionsLocation.getRadius()));
        refreshTimeValue.setText(String.valueOf(optionsLocation.getRefreshTime()));
        setEnabled();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(bestProvider, 500, 0, this);

        context = this;
    }

    @Override
    public void onClick(View v) {
        if(v == minusRadius) {
            int r = Integer.valueOf(radiusValue.getText().toString()) - 1;
            if(r >= optionsLocation.MIN_RADIUS) {
                optionsLocation.setRadius(r);
                radiusValue.setText(String.valueOf(r));
            }
        } else if(v == plusRadius) {
            int r = Integer.valueOf(radiusValue.getText().toString()) + 1;
            if(r <= optionsLocation.MAX_RADIUS) {
                optionsLocation.setRadius(r);
                radiusValue.setText(String.valueOf(r));
            }
        } else if(v == minusRefreshTime) {
            int r = Integer.valueOf(refreshTimeValue.getText().toString()) - 1;
            if(r >= optionsLocation.MIN_REFRESH_TIME) {
                optionsLocation.setRefreshTime(r);
                refreshTimeValue.setText(String.valueOf(r));
            }
        } else if(v == plusRefreshTime) {
            int r = Integer.valueOf(refreshTimeValue.getText().toString()) + 1;
            if(r <= optionsLocation.MAX_REFRESH_TIME) {
                optionsLocation.setRefreshTime(r);
                refreshTimeValue.setText(String.valueOf(r));
            }
        } else if(v == enabledValue) {
            if(optionsLocation.getEnabled()) {
                enabledValue.setText(String.valueOf("OFF"));
                optionsLocation.setEnabled(false);
                setEnabled();
            } else {
                enabledValue.setText(String.valueOf("ON"));
                optionsLocation.setEnabled(true);
                setEnabled();
            }
        } else if(v == searchButton) {
            String formatDistance;
            double distanceNumber = poi.getDistance();
            if(distanceNumber > 1000) {
                formatDistance = "%.2f km";
                distanceNumber /= 1000;
            } else {
                formatDistance = "%.2f m";
            }
            distance.setText(String.format(formatDistance, distanceNumber));
        }
    }

    private void setEnabled() {
        int color;
        if (optionsLocation.getEnabled()) {
            color = Color.parseColor(optionsLocation.ENABLED_COLOR);
        } else {
            color = Color.parseColor(optionsLocation.DISABLED_COLLOR);
        }
        minusRefreshTime.setEnabled(optionsLocation.getEnabled());
        plusRefreshTime.setEnabled(optionsLocation.getEnabled());
        minusRadius.setEnabled(optionsLocation.getEnabled());
        plusRadius.setEnabled(optionsLocation.getEnabled());
        searchButton.setEnabled(optionsLocation.getEnabled());

        minusRefreshTime.setTextColor(color);
        plusRefreshTime.setTextColor(color);
        refreshTimeValue.setTextColor(color);
        refreshTimeText.setTextColor(color);

        minusRadius.setTextColor(color);
        plusRadius.setTextColor(color);
        radiusValue.setTextColor(color);
        radiusText.setTextColor(color);
    }

    private void setPoi(Location location) {
        Location poiLocation = new Location("manual");
        poi = PoiService.getInstance().getPoi().get(0);
        poiLocation.setAltitude(poi.getAltitude());
        poiLocation.setLatitude(poi.getLatitude());
        poiLocation.setLongitude(poi.getLongitude());
        poi.setDistance(location.distanceTo(poiLocation));
    }

    @Override
    public void onLocationChanged(Location location) {
        setPoi(location);
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
}
