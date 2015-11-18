package ar.com.unlam.mae.Utils;

public class Poi {

    private String name;
    private String category;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double distance;
    private Double curBearing;

    public Poi(String name, String cagegory, Double latitude, Double longitude, Double altitude) {
        this.name = name;
        this.category = cagegory;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.distance = null;
        this.curBearing = null;
    }

    public Poi() {

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
