package com.paul.debashis.serviceexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private TextView textView;
    private BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DEB","onReceive is called");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString(MyService.FILEPATH);
                int resultCode = bundle.getInt(MyService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this,
                            "Download complete. Download URI: " + string,
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                } else {
                    Toast.makeText(MainActivity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DEB","onCreate is called");
        textView = (TextView) findViewById(R.id.status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEB","onResume is called");
        Log.d("DEB","Register the broadcast receiver");
        registerReceiver(receiver, new IntentFilter(MyService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEB","onPause is called");
        Log.d("DEB","UnRegister the broadcast receiver");
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(View view) {

        Intent intent = new Intent(this, MyService.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(MyService.FILENAME, "index.html");
        intent.putExtra(MyService.URL,
                "http://www.vogella.com/index.html");
        startService(intent);
        textView.setText("Service started");
    }
}
