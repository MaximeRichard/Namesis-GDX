package com.gameleonevents.namesis;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maxime on 11/02/2016.
 */
public class ActionResolverAndroid implements ActionResolver {


    public static ArrayList<Beacon> getBeaconList() {
        return beaconList;
    }

    public static void setBeaconList(ArrayList<Beacon> beaconList) {
        ActionResolverAndroid.beaconList = beaconList;
    }

    //paramètre de classe statique pour faire passer les infos du beacon detecté
    public static ArrayList<Beacon> beaconList;
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
                setBeaconList(beacons);
                /*for(Beacon beacon:beacons){
                    setBeaconDistance(beacon.getAccuracy()+"");
                    setBeaconInfo(beacon.getProximity().toString());
                }*/
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
    public HashMap<String, String> SendBeaconId(){
        if(getBeaconList() == null) return null;
        HashMap<String, String> beaconIds = new HashMap<String, String>();
        for (Beacon beacon:getBeaconList()){
            beaconIds.put(beacon.getSerialNumber(), beacon.getProximity().toString());
        }
        return beaconIds;
    }

    /*@Override
    public ArrayList<Double> SendBeaconDistance() {
        if(getBeaconList() == null) return null;
        ArrayList<Double> beaconIds = new ArrayList<Double>();
        for (Beacon beacon:getBeaconList()){
            beaconIds.add(beacon.getAccuracy());
        }
        return beaconIds;
    }

    @Override
    public ArrayList<String> SendBeaconProximity() {
        if(getBeaconList() == null) return null;
        ArrayList<String> beaconIds = new ArrayList<String>();
        for (Beacon beacon:getBeaconList()){
            beaconIds.add(beacon.getProximity().toString());
        }
        return beaconIds;
    }*/

    }
