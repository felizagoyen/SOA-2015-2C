package ar.com.unlam.mae;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ar.com.unlam.mae.Utils.OptionsLocation;

public class OptionsActivity extends Activity implements View.OnClickListener {

    TextView enabled;

    TextView minusRadius;
    TextView plusRadius;
    TextView radiusValue;
    TextView radiusText;

    TextView minusRefreshTime;
    TextView plusRefreshTime;
    TextView refreshTimeValue;
    TextView refreshTimeText;

    OptionsLocation optionsLocation = OptionsLocation.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        enabled = (TextView) findViewById(R.id.txtEnabledValue);
        minusRadius = (TextView) findViewById(R.id.txtMinusRadius);
        plusRadius = (TextView) findViewById(R.id.txtPlusRadius);
        radiusValue = (TextView) findViewById(R.id.txtRadiusValue);
        radiusText = (TextView) findViewById(R.id.txtRadius);

        minusRefreshTime = (TextView) findViewById(R.id.txtMinusRefresh);
        plusRefreshTime = (TextView) findViewById(R.id.txtPlusRefresh);
        refreshTimeValue = (TextView) findViewById(R.id.txtRefreshValue);
        refreshTimeText = (TextView) findViewById(R.id.txtRefresh);

        enabled.setOnClickListener(this);
        minusRadius.setOnClickListener(this);
        plusRadius.setOnClickListener(this);
        minusRefreshTime.setOnClickListener(this);
        plusRefreshTime.setOnClickListener(this);

        radiusValue.setText(String.valueOf(optionsLocation.getRadius()));
        refreshTimeValue.setText(String.valueOf(optionsLocation.getRefreshTime()));
        setEnabled();
    }

    @Override
    public void onClick(View v) {
        if(v == minusRadius) {
            int r = Integer.valueOf(radiusValue.getText().toString()) - 1;
            if(r >= optionsLocation.MIN_RADIUS) {
                optionsLocation.setRadius(r);
                radiusValue.setText(String.valueOf(r));
            }
        } else if(v == plusRadius) {
            int r = Integer.valueOf(radiusValue.getText().toString()) + 1;
            if(r <= optionsLocation.MAX_RADIUS) {
                optionsLocation.setRadius(r);
                radiusValue.setText(String.valueOf(r));
            }
        } else if(v == minusRefreshTime) {
            int r = Integer.valueOf(refreshTimeValue.getText().toString()) - 1;
            if(r >= optionsLocation.MIN_REFRESH_TIME) {
                optionsLocation.setRefreshTime(r);
                refreshTimeValue.setText(String.valueOf(r));
            }
        } else if(v == plusRefreshTime) {
            int r = Integer.valueOf(refreshTimeValue.getText().toString()) + 1;
            if(r <= optionsLocation.MAX_REFRESH_TIME) {
                optionsLocation.setRefreshTime(r);
                refreshTimeValue.setText(String.valueOf(r));
            }
        } else if(v == enabled) {
            if(optionsLocation.getEnabled()) {
                enabled.setText(String.valueOf("OFF"));
                optionsLocation.setEnabled(false);
                setEnabled();
            } else {
                enabled.setText(String.valueOf("ON"));
                optionsLocation.setEnabled(true);
                setEnabled();
            }
        }
    }

    private void setEnabled() {
        int color;
        if (optionsLocation.getEnabled()) {
            color = Color.parseColor(optionsLocation.ENABLED_COLOR);
        } else {
            color = Color.parseColor(optionsLocation.DISABLED_COLLOR);
        }
        minusRefreshTime.setEnabled(optionsLocation.getEnabled());
        plusRefreshTime.setEnabled(optionsLocation.getEnabled());
        minusRadius.setEnabled(optionsLocation.getEnabled());
        plusRadius.setEnabled(optionsLocation.getEnabled());

        minusRefreshTime.setTextColor(color);
        plusRefreshTime.setTextColor(color);
        refreshTimeValue.setTextColor(color);
        refreshTimeText.setTextColor(color);

        minusRadius.setTextColor(color);
        plusRadius.setTextColor(color);
        radiusValue.setTextColor(color);
        radiusText.setTextColor(color);
    }
}
