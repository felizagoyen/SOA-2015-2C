package ar.com.unlam.mae.Service;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.com.unlam.mae.Utils.Poi;

public class PoiService {

    private static final PoiService instance = new PoiService();

    private PoiService() {
    }

    public static PoiService getInstance() {
        return instance;
    }

    public List<Poi> getPoi(Context context) {
        List<Poi> poi = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("pointsOfInterest.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String currentJSONString;

            while( (currentJSONString = br.readLine()) != null ) {
                JSONObject json = new JSONObject(currentJSONString);
                poi.add(convertPoi(json));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return poi;
    }

    private Poi convertPoi(JSONObject json) throws Exception {
        String name = json.getString("name");
        String description = json.getString("description");
        BigDecimal latitude = BigDecimal.valueOf(json.getDouble("latitude"));
        BigDecimal longitude = BigDecimal.valueOf(json.getDouble("longitude"));
        BigDecimal altitude = BigDecimal.valueOf(json.getDouble("altitude"));
        return new Poi(name, description, latitude, longitude, altitude);
    }
}
