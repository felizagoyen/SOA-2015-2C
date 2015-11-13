package ar.com.unlam.mae.Service;

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

    public List<Poi> getPoi() {
        List<Poi> poi = new ArrayList<>();

        poi.add(new Poi("Plaza Oeste", "Shopping", -34.6344413, -58.6318342, 1916.5));

        return poi;
    }

}
