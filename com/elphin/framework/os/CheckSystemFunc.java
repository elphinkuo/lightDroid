package com.elphin.framework.os;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.Iterator;
import java.util.List;

public class CheckSystemFunc {
    /**
     * 是否有后置摄像头
     */
    public static boolean hasBackCamera = false;

    /**
     * 是否有GPS
     */
    public static boolean hasGps = false;

    /**
     * 是否有方向传感器
     */
    public static boolean hasOrientationSensor = false;

    /**
     * 是否有电话功能
     */
    public static boolean telephoneEnable = false;
    
    /**
     * 是否有语音话筒
     */
    public static boolean voiceEnable = false;
    
    //pre-install
    /**
     * 是否支持GPRS/3G等mobile网络
     */
    public static boolean hasMobileNetwork = false;
    //pre-install - end

    public static void determineSystemFunc(Context context) {
        checkGps(context);
        checkBackCamera();
        checkSensor(context);
        checkTelePhoneEnable(context);
        checkVoiceEnable(context);
        
      //pre-install
        checkMobileNetwork(context);
        //pre-install - end
    }
    
    private static void checkTelePhoneEnable(Context context){
        telephoneEnable = context.getResources().getBoolean(R.bool.enableCallTelephone);
    }
    
    private static void checkVoiceEnable(Context context){
        voiceEnable = context.getResources().getBoolean(R.bool.enableVoiceSearch);
    }

    private static void checkGps(Context context) {
        try {
            final LocationManager mgr = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            if (mgr == null){
                hasGps = false;
                return;
            }
            final List<String> providers = mgr.getAllProviders();
            if (providers == null){
                hasGps = false;
                return;
            }
            hasGps = providers.contains(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            hasGps = false;
        }
    }

    @SuppressLint("NewApi")
    private static void checkBackCamera() {
        if (Build.VERSION.SDK_INT > 9) {
            int numberOfCameras = Camera.getNumberOfCameras();
            CameraInfo cameraInfo = new CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    hasBackCamera = true;
                }
            }
        } else {
            hasBackCamera = true;
        }
    }

    private static void checkSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> all_sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);// 取得所有传感器
        Iterator<Sensor> it = all_sensors.iterator();
        while (it.hasNext()) {
            Sensor sensor = it.next();
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                hasOrientationSensor = true;
            }
        }
    }
    
  //pre-install
    private static void checkMobileNetwork(Context context) {
        NetworkInfo[] infos = NetworkUtil.getAllNetworkInfo(context);
        if (infos != null) {
            for (int i = 0; i < infos.length; i++) {
                if (ConnectivityManager.TYPE_MOBILE == infos[i].getType()) {
                    hasMobileNetwork = true;
                }
            }
        }
    }
    //pre-install - end
}

