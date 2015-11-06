package org.cyanogenmod.focal.Utils;

public class GeoUtils {
    private static int EARTH_RADIO = 6371;
    private static GeoUtils ourInstance = new GeoUtils();

    public static GeoUtils getInstance() {
        return ourInstance;
    }

    private GeoUtils() {
    }

    public Double getDistanceBetweenCoordinates(final Coordinate coordinate1, final Coordinate coordinate2) {
        Double dLat = Math.toRadians((coordinate2.getLatitude().subtract(coordinate1.getLatitude()).doubleValue()));
        Double dLon = Math.toRadians((coordinate2.getLongitude().subtract(coordinate1.getLongitude()).doubleValue()));
        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(coordinate1.getLatitude().doubleValue())) * Math.cos(Math.toRadians(coordinate2.getLatitude().doubleValue())) * Math.sin(dLon/2) * Math.sin(dLon/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIO * c;
    }

}
