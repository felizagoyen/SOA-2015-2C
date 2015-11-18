package ar.com.unlam.mae;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

import ar.com.unlam.mae.Service.PoiService;
import ar.com.unlam.mae.Utils.Poi;
import ar.com.unlam.mae.Utils.SettingsLocation;

public class OverlayView extends View implements SensorEventListener, LocationListener {

    ArrayList<Poi> poiToDraw;
    ArrayList<Boolean> poiIsInRadius;
    private float[] lastAccel;
    private float[] lastComp;
    private float[] rotation = new float[9];
    private float[] identity = new float[9];
    private LocationManager locationManager;
    private float verticalFOV;
    private float horizontalFOV;
    private SettingsLocation settingsLocation = SettingsLocation.getInstance();
    Bitmap drawIconAirport;
    Bitmap drawIconBank;
    Bitmap drawIconChurch;
    Bitmap drawIconHospital;
    Bitmap drawIconLibrary;
    Bitmap drawIconMall;
    Bitmap drawIconOther;
    Bitmap drawIconParking;
    Bitmap drawIconRestaurant;
    Bitmap drawIconUniversity;

    public OverlayView(Context context, Camera camera) {
        super(context);

        SensorManager sensors = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor compassSensor = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensors.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensors.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);

        poiToDraw = PoiService.getInstance().getPoi(getContext());
        poiIsInRadius = new ArrayList<Boolean>();

        Camera.Parameters params = camera.getParameters();
        verticalFOV = params.getVerticalViewAngle();
        horizontalFOV = params.getHorizontalViewAngle();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        drawIconAirport = BitmapFactory.decodeResource(getResources(), R.drawable.ic_airport);
        drawIconBank = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bank);
        drawIconChurch = BitmapFactory.decodeResource(getResources(), R.drawable.ic_church);
        drawIconHospital = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hospital);
        drawIconLibrary = BitmapFactory.decodeResource(getResources(), R.drawable.ic_library);
        drawIconMall = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mall);
        drawIconOther = BitmapFactory.decodeResource(getResources(), R.drawable.ic_others);
        drawIconParking = BitmapFactory.decodeResource(getResources(), R.drawable.ic_parking);
        drawIconRestaurant = BitmapFactory.decodeResource(getResources(), R.drawable.ic_restaurant);
        drawIconUniversity = BitmapFactory.decodeResource(getResources(), R.drawable.ic_university);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setTextAlign(Paint.Align.CENTER);
        contentPaint.setTextSize(30);
        contentPaint.setColor(Color.CYAN);

        boolean gotRotation = SensorManager.getRotationMatrix(rotation,
                identity, lastAccel, lastComp);
        if (gotRotation) {
            // orientation vector
            float orientation[] = new float[3];
            SensorManager.getOrientation(rotation, orientation);

            if (gotRotation) {
                float cameraRotation[] = new float[9];
                // remap such that the camera is pointing straight down the Y axis
                SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, cameraRotation);

                // orientation vector
                orientation = new float[3];
                SensorManager.getOrientation(cameraRotation, orientation);

                for(Poi poi: poiToDraw) {
                    if (poiIsInRadius.size() > 0 && poiIsInRadius.get(poiToDraw.indexOf(poi)).equals(true)) {
                        // use roll for screen rotation
                        canvas.rotate((float) (0.0f - Math.toDegrees(orientation[2])));
                        // Translate, but normalize for the FOV of the camera -- basically, pixels per degree, times degrees == pixels
                        float dx = (float) ((canvas.getWidth() / horizontalFOV) * (Math.toDegrees(orientation[0]) - poi.getCurBearing()));
                        float dy = (float) ((canvas.getHeight() / verticalFOV) * Math.toDegrees(orientation[1]));

                        canvas.translate(0.0f - dx, 0.0f - dy);

                        // draw our point -- we've rotated and translated this to the right spot already
                        canvas.drawBitmap(getIconToDraw(poi.getCategory()), canvas.getWidth() / 2 - 30, canvas.getHeight() / 2, null);
                        canvas.drawText(poi.getName(), canvas.getWidth() / 2, canvas.getHeight() / 1.7f, contentPaint);
                        canvas.drawText(distanceFormat(poi.getDistance()), canvas.getWidth() / 2, canvas.getHeight() / 1.6f, contentPaint);
                    }
                }
            }
    }}

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                lastAccel = lowPass(event.values.clone(), lastAccel);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                lastComp = lowPass(event.values.clone(), lastComp);
                break;
        }
        this.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onLocationChanged(Location location) {
        for(Poi poi: poiToDraw) {
            Location poiLocation = new Location("manual");
            poiLocation.setLatitude(poi.getLatitude());
            poiLocation.setLongitude(poi.getLongitude());
            poiLocation.setAltitude(poi.getAltitude());
            poi.setDistance(location.distanceTo(poiLocation));
            poi.setCurBearing(location.bearingTo(poiLocation));
            if((poi.getDistance() / 1000) <= settingsLocation.getRadius() || settingsLocation.getRadius() == 0) {
                poiIsInRadius.add(poiToDraw.indexOf(poi), true);
            } else {
                poiIsInRadius.add(poiToDraw.indexOf(poi), false);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    /**
     * Turn off the GPS
     */
    public void pauseGPS() {
        locationManager.removeUpdates(this);
    }

    /**
     * Turn on the GPS
     */
    public void resumeGPS() {
        poiToDraw = PoiService.getInstance().getPoi(getContext());
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String best = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(best, SettingsLocation.getInstance().getRefreshTime() * 1000, 0, this);
    }

    /**
     * Filter low pass
     */
    private float[] lowPass( float[] input, float[] output ) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + 0.1f * (input[i] - output[i]);
        }
        return output;
    }

    /**
     * distance formater
     * @param distance distance to format
     * @return distance in km if distance >= 1000 or in m if < 1000
     */
    private String distanceFormat(double distance) {
        String formatDistance;
        if (distance == 0.0) return "";
        if (distance >= 1000) {
            formatDistance = "%.2f km";
            distance /= 1000;
        } else {
            formatDistance = "%.2f m";
        }
        return String.format(formatDistance, distance);
    }

    /**
     * Return the icon by category
     * @param category Category of the icon to show
     * @return the icon
     */
    private Bitmap getIconToDraw(String category) {
        if ("Airport".equals(category)) {
            return drawIconAirport;
        } else if ("Bank".equals(category)) {
            return drawIconBank;
        } else if ("Church".equals(category)) {
            return drawIconChurch;
        } else if ("Hospital".equals(category)) {
            return drawIconHospital;
        } else if ("Library".equals(category)) {
            return drawIconLibrary;
        } else if ("Mall".equals(category)) {
            return drawIconMall;
        } else if ("Parking".equals(category)) {
            return drawIconParking;
        } else if ("Restaurant".equals(category)) {
            return drawIconRestaurant;
        } else if ("University".equals(category)) {
            return drawIconUniversity;
        } else {
            return drawIconOther;
        }
    }
}