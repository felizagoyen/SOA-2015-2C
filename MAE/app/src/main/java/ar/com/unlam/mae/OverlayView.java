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
import android.util.Log;
import android.view.View;

import ar.com.unlam.mae.Service.PoiService;
import ar.com.unlam.mae.Utils.Poi;
import ar.com.unlam.mae.Utils.SettingsLocation;

public class OverlayView extends View implements SensorEventListener, LocationListener {

    public static final String DEBUG_TAG = "OverlayView Log";
    String accelData = "Accelerometer Data";
    String compassData = "Compass Data";
    String gyroData = "Gyro Data";
    private float[] lastAccel;
    private float[] lastComp;
    private float[] rotation = new float[9];
    private float[] identity = new float[9];
    private float[] orientation =  new float[3];
    private float[] cameraRotation = new float[9];
    private Location lastLocation;
    private LocationManager locationManager;
    private float verticalFOV;
    private float horizontalFOV;
    float curBearing;
    String distanceToPoi = null;
    private final static Location plazaOeste = new Location("manual");
    static {
        plazaOeste.setLatitude(-34.6344413);
        plazaOeste.setLongitude(-58.6318342);
        plazaOeste.setAltitude(1916.5d);
    };
    Bitmap drawIcon;

    public OverlayView(Context context, Camera camera) {
        super(context);

        SensorManager sensors = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor compassSensor = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor gyroSensor = sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        boolean isAccelAvailable = sensors.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        boolean isCompassAvailable = sensors.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);
        boolean isGyroAvailable = sensors.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Camera.Parameters params = camera.getParameters();
        verticalFOV = params.getVerticalViewAngle();
        horizontalFOV = params.getHorizontalViewAngle();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        drawIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_poi);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setTextAlign(Paint.Align.CENTER);
        contentPaint.setTextSize(80);
        contentPaint.setColor(Color.CYAN);
//      canvas.drawText(accelData, canvas.getWidth() / 2, canvas.getHeight() / 4, contentPaint);
//      canvas.drawText(compassData, canvas.getWidth()/2, canvas.getHeight()/2, contentPaint);
//      canvas.drawText(gyroData, canvas.getWidth()/2, (canvas.getHeight()*3)/4, contentPaint);
//      canvas.drawText(""+ curBearing, canvas.getWidth()/2, canvas.getHeight()/6, contentPaint);

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

                // use roll for screen rotation
                canvas.rotate((float) (0.0f - Math.toDegrees(orientation[2])));
                // Translate, but normalize for the FOV of the camera -- basically, pixels per degree, times degrees == pixels
                float dx = (float) ( (canvas.getWidth() / horizontalFOV) * (Math.toDegrees(orientation[0]) - curBearing));
                float dy = (float) ( (canvas.getHeight() / verticalFOV) * Math.toDegrees(orientation[1])) ;

                canvas.translate(0.0f - dx, 0.0f - dy);

                // draw our point -- we've rotated and translated this to the right spot already
                //canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 40.0f, contentPaint);

                if(distanceToPoi != null) {
                    canvas.drawText(distanceToPoi, canvas.getWidth() / 2, canvas.getHeight() / 6, contentPaint);
                    canvas.drawBitmap(drawIcon, canvas.getWidth() / 2, canvas.getHeight() / 5, null);
                }
           }

    }}

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder msg = new StringBuilder(event.sensor.getName()).append(" ");
        for(float value: event.values) {
            msg.append("[").append(value).append("]");
        }

        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelData = msg.toString();
                lastAccel = lowPass(event.values.clone(), lastAccel);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroData = msg.toString();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                compassData = msg.toString();
                lastComp = lowPass(event.values.clone(), lastComp);
                break;
        }

        this.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        Poi poi = PoiService.getInstance().getPoi(getContext()).get(0);
        Location poiLocation = new Location("manual");
        poiLocation.setLatitude(poi.getLatitude());
        poiLocation.setLongitude(poi.getLongitude());
        poiLocation.setAltitude(poi.getAltitude());

        curBearing = lastLocation.bearingTo(poiLocation);
        String formatDistance;
        double distanceNumber = lastLocation.distanceTo(poiLocation);
        if(distanceNumber > 1000) {
            formatDistance = "%.2f km";
            distanceNumber /= 1000;
        } else {
            formatDistance = "%.2f m";
        }

        distanceToPoi = String.format(formatDistance, distanceNumber);
        Log.v("distance: ", distanceToPoi);
        Log.v("curBearing: ", String.valueOf(curBearing));
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

    public void pauseGPS() {
        locationManager.removeUpdates(this);
    }

    public void resumeGPS() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String best = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(best, SettingsLocation.getInstance().getRefreshTime() * 1000, 0, this);
    }


    private float[] lowPass( float[] input, float[] output ) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + 0.1f * (input[i] - output[i]);
        }
        return output;
    }
}