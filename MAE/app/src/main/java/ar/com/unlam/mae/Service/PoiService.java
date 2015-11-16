package ar.com.unlam.mae.Service;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
        List<Poi> poi = new ArrayList<Poi>();
        String json = readFromFile(context);
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray pois = reader.getJSONArray("pois");
            for(int i = 0; i < pois.length(); i++) {
                JSONObject p = pois.getJSONObject(i);
                Poi singlePoi = new Poi(p.getString("name"),
                                        p.getString("category"),
                                        p.getDouble("latitude"),
                                        p.getDouble("longitude"),
                                        p.getDouble("altitude"));
                poi.add(singlePoi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poi;
    }

    /*
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("name", person.getName()); // Set the first name/pair
        jsonObj.put("surname", person.getSurname());
     */

   private String readFromFile(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().getAssets().open("poi.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
