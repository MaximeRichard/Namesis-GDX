package com.gameleonevents.namesis;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;

/**
 * Created by Maxime on 11/02/2016.
 */
public class ActionResolverAndroid implements ActionResolver {



    //paramètre de classe statique pour faire passer les infos du beacon detecté
    public static String beaconInfo;

    public static String getBeaconInfo() {
        return beaconInfo;
    }

    public static void setBeaconInfo(String beaconInfo) {
        ActionResolverAndroid.beaconInfo = beaconInfo;
    }

    BluetoothAdapter bluetoothAdapter;
    SensoroManager sensoroManager;
    Context context;
    BeaconManagerListener beaconManagerListener;

    public ActionResolverAndroid(Context context) {
        this.context = context;
        InitializeSensoro();
        initSensoroListener();
    }

    //Initialisation du service Sensoro
    public void InitializeSensoro() {
        sensoroManager = SensoroManager.getInstance(context);
/**
 * Check whether the Bluetooth is on
 **/
        if (isBlueEnable()) {
            /**
             * Enable cloud service (upload sensor data, including battery status, UMM, etc.)。Without setup, it keeps in closed status as default.
             **/
            sensoroManager.setCloudServiceEnable(true);
            /**
             * Enable SDK service
             **/
            try {
                sensoroManager.startService();
            } catch (Exception e) {
                e.printStackTrace(); // Fetch abnormal info
            }
        }
    }

    private boolean isBlueEnable() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean status = bluetoothAdapter.isEnabled();

        return status;
    }

    //Initialisation du listener de beacons
    public void initSensoroListener() {
        beaconManagerListener = new BeaconManagerListener() {

            @Override
            public void onUpdateBeacon(ArrayList<Beacon> beacons) {
                for(Beacon beacon:beacons){
                    setBeaconInfo(beacon.getProximity().toString());
                }
            }

            @Override
            public void onNewBeacon(Beacon beacon) {
                // New sensor found
            }

            @Override
            public void onGoneBeacon(Beacon beacon) {
                // A sensor disappears from the range
            }
        };
        sensoroManager.setBeaconManagerListener(beaconManagerListener);
    }


    @Override
    public String SendBeaconInfo(){
        if(getBeaconInfo() == null) return "No Beacon";
        else return getBeaconInfo();
    }



    }
