package ar.com.unlam.mae.Utils;

public class OptionsLocation {

    private static OptionsLocation instance = new OptionsLocation();

    public final int MIN_REFRESH_TIME = 1;
    public final int MAX_REFRESH_TIME = 10;
    public final int MIN_RADIUS = 1;
    public final int MAX_RADIUS = 10;
    public final String DISABLED_COLLOR = "#666666";
    public final String ENABLED_COLOR = "#FFFFFF";

    private int radius;
    private int refreshTime;
    private boolean enabled;

    private OptionsLocation() {
        radius = 5;
        refreshTime = 5;
        enabled = false;
    }

    public static OptionsLocation getInstance() {
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }
}
