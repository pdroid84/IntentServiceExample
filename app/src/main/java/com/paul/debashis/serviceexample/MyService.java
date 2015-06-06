package com.paul.debashis.serviceexample;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.paul.debashis.serviceexample";
    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        File output = new File(Environment.getExternalStorageDirectory(),fileName);
        if(output.exists()){
            output.delete();
        }
        InputStream iStream = null;
        FileOutputStream oStream = null;
        try {
            URL url = new URL(urlPath);
            iStream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(iStream);
            oStream = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next=reader.read())!=-1){
                oStream.write(next);
            }
            result = Activity.RESULT_OK;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(iStream!=null){
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oStream!=null){
                try {
                    oStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishResult(output.getAbsolutePath(),result);
    }
    private void publishResult(String path,int res){
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH,path);
        intent.putExtra(RESULT,res);
        sendBroadcast(intent);
    }
}
