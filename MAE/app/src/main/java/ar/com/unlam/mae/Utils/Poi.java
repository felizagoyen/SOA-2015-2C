package ar.com.unlam.mae.Utils;

import java.math.BigDecimal;

public class Poi {

    private String name;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal altitude;
    private Double distance;

    public Poi(String name, String description, BigDecimal latitude, BigDecimal longitude, BigDecimal altitude) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latitude = altitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

}
