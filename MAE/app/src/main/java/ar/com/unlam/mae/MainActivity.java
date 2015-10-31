package ar.com.unlam.mae;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout camViewPane = (FrameLayout) findViewById(R.id.ar_view_pane);

        CameraView camView = new CameraView(getApplicationContext(), this);
        camViewPane.addView(camView);

        OverlayView arContent = new OverlayView(getApplicationContext());
        camViewPane.addView(arContent);
    }
}
