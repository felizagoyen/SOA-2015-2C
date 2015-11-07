package org.cyanogenmod.focal.widgets;

import android.content.Context;
import android.hardware.Camera;
import android.location.LocationManager;
import android.view.View;

import org.cyanogenmod.focal.CameraActivity;
import org.cyanogenmod.focal.ui.CenteredSeekBar;

import fr.xplod.focal.R;

public class GpsWidget extends WidgetBase implements
        CenteredSeekBar.OnCenteredSeekBarChangeListener, View.OnClickListener {

    private static final int MIN_RADIUS = 1;
    private static final int MAX_RADIUS = 10;
    private static final int MIN_REFRESH_TIME = 1;
    private static final int MAX_REFRESH_TIME = 5;

    private LocationManager locationManager;

    private WidgetOptionGPSSeekBar mSeekBarRadius;
    private WidgetOptionGPSSeekBar mSeekBarRefreshTime;
    private WidgetOptionGPSLabel mLabelRefreshTime;
    private WidgetOptionGPSLabel mLabelRadius;

    private int refreshTimeValue = 3;
    private int radiusValue = 5;
    public GpsWidget(CameraActivity activity) {
        super(activity.getCamManager(), activity, R.drawable.ic_widget_videofr);
        getToggleButton().setHintText(R.string.widget_geolocalization);

        mSeekBarRadius = new WidgetOptionGPSSeekBar(MIN_RADIUS, MAX_RADIUS, activity);
        mSeekBarRadius.setNotifyWhileDragging(true);
        mSeekBarRefreshTime = new WidgetOptionGPSSeekBar(MIN_REFRESH_TIME, MAX_REFRESH_TIME, activity);
        mSeekBarRefreshTime.setNotifyWhileDragging(true);
        mLabelRadius = new WidgetOptionGPSLabel(activity);
        mLabelRefreshTime = new WidgetOptionGPSLabel(activity);

        addViewToContainerForGps(mSeekBarRadius, 1, 1);
        addViewToContainerForGps(mLabelRadius, 1, 2);
        addViewToContainerForGps(mSeekBarRefreshTime, 2, 1);
        addViewToContainerForGps(mLabelRefreshTime, 2, 2);

        mSeekBarRadius.setOnCenteredSeekBarChangeListener(this);
        mSeekBarRefreshTime.setOnCenteredSeekBarChangeListener(this);

        mSeekBarRadius.setSelectedMinValue(radiusValue);
        mSeekBarRefreshTime.setSelectedMinValue(refreshTimeValue);

        mLabelRadius.setText(String.format("Radius: %d Km", mSeekBarRadius.getSelectedValue()));
        mLabelRefreshTime.setText(String.format("Refresh: %d Sec", refreshTimeValue));

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * @return True if the GPS provider is Enabled
     */
    @Override
    public boolean isSupported(Camera.Parameters params) {
        return locationManager.isProviderEnabled("gps");
    }

    /**
     * Set label radius when the seekbar change
     */
    @Override
    public void OnCenteredSeekBarValueChanged(CenteredSeekBar bar, Integer value) {
        if(bar == mSeekBarRadius) {
            mLabelRadius.setText(String.format("Radius: %d Km", value));
        } else if (bar == mSeekBarRefreshTime) {
            mLabelRefreshTime.setText(String.format("Refresh: %d Sec", value));
        }
    }

    @Override
    public void onClick(View view) {

    }
}
