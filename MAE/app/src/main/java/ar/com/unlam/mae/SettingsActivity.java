package ar.com.unlam.mae;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import ar.com.unlam.mae.Utils.SettingsLocation;

public class SettingsActivity extends Activity {

    SettingsLocation settingsLocation = SettingsLocation.getInstance();
    Context context;
    ArrayList<String> listSettings = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        listSettings.add("Radius");
        listSettings.add("Refresh Time");
        listSettings.add("Add new point of interest");

        final ListView settings = (ListView) findViewById(R.id.listSettings);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listSettings);
        settings.setAdapter(adapter);

        context = this;

        settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(view.getContext(), RadiusActivity.class);
                    startActivityForResult(intent, 0);
                } else if (position == 1) {
                    Intent intent = new Intent(view.getContext(), RefreshTimeActivity.class);
                    startActivityForResult(intent, 1);
                } else if (position == 2) {
                    Intent intent = new Intent(view.getContext(), AddPoiActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                settingsLocation.setRadius(data.getIntExtra("Radius", 5));
            } else if (requestCode == 1) {
                settingsLocation.setRadius(data.getIntExtra("Refresh", 5));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                back();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void back() {
        onBackPressed();
    }

}
