package ar.com.unlam.mae.Utils;

public class Poi {

    private String name;
    private String category;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double distance;
    private Double curBearing;

    public Poi(String name, String category, Double latitude, Double longitude, Double altitude) {
        this.name = name;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.distance = 0.0d;
        this.curBearing = 0.0d;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setCurBearing(double curBearing) {
        this.curBearing = curBearing;
    }

    public double getCurBearing() {
        return curBearing;
    }

}
