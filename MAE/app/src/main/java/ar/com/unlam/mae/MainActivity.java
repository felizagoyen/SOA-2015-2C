package ar.com.unlam.mae;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout camViewPane = (FrameLayout) findViewById(R.id.ar_view_pane);
        Camera camera = Camera.open();
        CameraView camView = new CameraView(getApplicationContext(), this, camera);
        camViewPane.addView(camView);

        OverlayView arContent = new OverlayView(getApplicationContext(), camera);
        camViewPane.addView(arContent);
    }
}
