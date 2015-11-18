package ar.com.unlam.mae;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
    private OverlayView arContent;
    private FrameLayout camViewPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        arContent.pauseGPS();
     }

    @Override
    protected void onResume() {
        super.onResume();
        camViewPane = (FrameLayout) findViewById(R.id.ar_view_pane);
        Camera camera = Camera.open();
        CameraView camView = new CameraView(getApplicationContext(), this, camera);
        camViewPane.addView(camView);

        arContent = new OverlayView(getApplicationContext(), camera);
        camViewPane.addView(arContent);
        arContent.resumeGPS();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                settings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
