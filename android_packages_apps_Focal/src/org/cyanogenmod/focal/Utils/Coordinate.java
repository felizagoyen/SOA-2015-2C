package org.cyanogenmod.focal.Utils;

import java.math.BigDecimal;

public class Coordinate {

    private BigDecimal latitude;
    private BigDecimal longitude;

    /**
     * Constructor.
     * @param latitude Latitud
     * @param longitude Longitud
     */
    public Coordinate(final BigDecimal latitude, final BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Asegura que tanto la longitud como la latitud se encuentran seteadas, considerando entonces como valida a la coordenada, devolviendo true.
     * @return True si ambos campos estan seteados.
     */
    public boolean isValid() {
        return this.latitude != null && this.longitude != null;
    }


    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
