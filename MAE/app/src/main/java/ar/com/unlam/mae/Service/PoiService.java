package ar.com.unlam.mae.Service;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ar.com.unlam.mae.Utils.Poi;

public class PoiService {

    private static final PoiService instance = new PoiService();

    private PoiService() {
    }

    /**
     * Return the instances
     * @return instance
     */
    public static PoiService getInstance() {
        return instance;
    }

    /**
     * Get the pois from the Internal Storage. If don't exist it, take the pois from assets
     * @param context
     * @return return list of Poi
     */
    public ArrayList<Poi> getPoi(Context context) {
        ArrayList<Poi> poi = new ArrayList<Poi>();
        try {
            JSONObject reader = readJsonFromFile(context);
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

    /**
     * Write new poi to the Internal Storage
     * @param poi New Poi
     * @param context
     * @return Operation was success
     * */
    public Boolean setPoi(Poi poi, Context context) {
        try {
            JSONObject reader = readJsonFromFile(context);
            JSONArray pois = reader.getJSONArray("pois");
            JSONObject newPoi = getNewJsonObjectPoi(poi);
            pois.put(newPoi);
            reader.put("pois", pois);
            writeJsonInFile(reader, context);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Read file from Internal Storage or assets if the file on Internal Storage don't exist
     * @param context
     * @return
     * @throws Exception
     */
    private JSONObject readJsonFromFile(Context context) throws Exception {
        String json = null;
        try {
            try {
                FileInputStream is = context.openFileInput("poi.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch(Exception e) {
                new File(context.getFilesDir(), "poi.json");
                InputStream is = context.getResources().getAssets().open("poi.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new JSONObject(json);
    }

    /**
     * Create a new Poi as JsonObject
     * @param poi
     * @return
     * @throws Exception
     */
    private JSONObject getNewJsonObjectPoi(Poi poi) throws Exception {
        JSONObject newPoi = new JSONObject();
        newPoi.put("name", poi.getName());
        newPoi.put("category", poi.getCategory());
        newPoi.put("latitude", poi.getLatitude());
        newPoi.put("longitude", poi.getLongitude());
        newPoi.put("altitude", poi.getAltitude());
        return newPoi;
    }

    /**
     * Write all poi in Internal Storage
     * @param json
     * @param context
     * @throws Exception
     */
    private void writeJsonInFile(JSONObject json, Context context) throws Exception {
        FileOutputStream fos = context.openFileOutput("poi.json", Context.MODE_PRIVATE);
        fos.write(json.toString().getBytes());
        fos.close();
    }


}
