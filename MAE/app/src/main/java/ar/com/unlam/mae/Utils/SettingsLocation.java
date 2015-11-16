package ar.com.unlam.mae.Utils;

public class SettingsLocation {

    private static SettingsLocation instance = new SettingsLocation();

    public final int MIN_REFRESH_TIME = 0;
    public final int MAX_REFRESH_TIME = 10;
    public final int MIN_RADIUS = 0;
    public final int MAX_RADIUS = 10;

    private int radius;
    private int refreshTime;

    private SettingsLocation() {
        radius = 5;
        refreshTime = 5;
    }

    public static SettingsLocation getInstance() {
        return instance;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

}
