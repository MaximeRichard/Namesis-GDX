package com.gameleonevents.namesis;

import com.sensoro.beacon.kit.Beacon;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maxime on 11/02/2016.
 */
public interface ActionResolver {
    public HashMap<String, String> SendBeaconId();
    //public ArrayList<Double> SendBeaconDistance();
    //public ArrayList<String> SendBeaconProximity();
}
