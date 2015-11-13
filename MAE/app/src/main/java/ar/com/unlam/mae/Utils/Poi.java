package ar.com.unlam.mae.Utils;

public class Poi {

    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double distance;

    public Poi(String name, String description, Double latitude, Double longitude, Double altitude) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

}
