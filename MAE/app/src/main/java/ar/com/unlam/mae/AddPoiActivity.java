package ar.com.unlam.mae;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddPoiActivity extends Activity {

    private Spinner spinnerCategory;
    private EditText eTxtName;
    private Button buttonAdd;
    private ArrayList<String> listCategory = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_poi);

        listCategory.add("Category");

        eTxtName = (EditText) findViewById(R.id.eTxtName);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategory);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(v.getContext(), AddPoiListActivity.class);
                    startActivityForResult(intent, 3);
                }
                return true;
            }
        });

        buttonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(eTxtName.getText().toString()) && !"Category".equals(listCategory.get(0))) {
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                listCategory.remove(0);
                listCategory.add(data.getStringExtra("Category"));
                adapter.notifyDataSetChanged();
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
