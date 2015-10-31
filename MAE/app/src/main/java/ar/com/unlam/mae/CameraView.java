package ar.com.unlam.mae;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by gon on 10/31/2015.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    String DEBUG_TAG = "CameraView";
    Camera camara;
    SurfaceHolder holder;
    Activity myActivity;

    public CameraView(Context context, Activity activity){
        super(context);
        myActivity = activity;
        holder=getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback((SurfaceHolder.Callback) this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camara = Camera.open();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = myActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch(rotation){
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        camara.setDisplayOrientation((info.orientation - degrees + 360)%360);

        try{
            camara.setPreviewDisplay(holder);
        } catch(IOException e){
            Log.e(DEBUG_TAG, "surfaceCreated exception: ", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Camera.Parameters params = camara.getParameters();
        List<Camera.Size> prevSizes = params.getSupportedPreviewSizes();
        for (Camera.Size s : prevSizes){
            if((s.height <= height) && (s.width <= width))
            {
                params.setPreviewSize(s.width, s.height);
                break;
            }
        }
        camara.setParameters(params);
        camara.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camara.stopPreview();
        camara.release();
    }
}
