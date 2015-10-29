package org.cyanogenmod.focal.widgets;

import android.content.Context;
import android.hardware.Camera;
import android.location.LocationManager;
import org.cyanogenmod.focal.CameraActivity;
import fr.xplod.focal.R;

public class GpsWidget extends WidgetBase {

    private LocationManager locationManager;

    public GpsWidget(CameraActivity activity) {
        super(activity.getCamManager(), activity, R.drawable.ic_widget_videofr);
        getToggleButton().setHintText(R.string.widget_geolocalization);
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isSupported(Camera.Parameters params) {
        // Is supported if has GPS component
        // return locationManager.isProviderEnabled("gps");
        return true;
    }

}
