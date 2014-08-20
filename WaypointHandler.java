/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.util.ArrayList;

/**
 *
 * @author markburton
 */
public class WaypointHandler {
    
    private ArrayList<Waypoint> goodAIWaypointList;
    private ArrayList<Waypoint> badAIWaypointList;
    private static WaypointHandler instance;
    
    public WaypointHandler() {
        goodAIWaypointList = new ArrayList<>();
        badAIWaypointList = new ArrayList<>();
        // Implement setup from JSON or similar
        quickAndRoughSetup();
    }

    private void quickAndRoughSetup() {
        // Set up Good(ish) Waypoints
        goodAIWaypointList.add(new Waypoint(1277, 1499));
        goodAIWaypointList.add(new Waypoint(1427, 1109));
        goodAIWaypointList.add(new Waypoint(1882, 839));
        goodAIWaypointList.add(new Waypoint(5103, 809));
        goodAIWaypointList.add(new Waypoint(5516, 959));
        goodAIWaypointList.add(new Waypoint(5768, 1445));
        goodAIWaypointList.add(new Waypoint(5780, 3527));
        goodAIWaypointList.add(new Waypoint(5624, 3941));
        goodAIWaypointList.add(new Waypoint(5241, 4181));
        goodAIWaypointList.add(new Waypoint(1976, 4202));
        goodAIWaypointList.add(new Waypoint(1496, 3954));
        goodAIWaypointList.add(new Waypoint(1272, 3419));
        
        // Set up Bad Waypoints
        badAIWaypointList.add(new Waypoint(1151, 1391));
        badAIWaypointList.add(new Waypoint(1409, 821));
        badAIWaypointList.add(new Waypoint(2296, 629));
        badAIWaypointList.add(new Waypoint(5055, 647));
        badAIWaypointList.add(new Waypoint(5792, 953));
        badAIWaypointList.add(new Waypoint(5996, 1553));
        badAIWaypointList.add(new Waypoint(5936, 3593));
        badAIWaypointList.add(new Waypoint(5654, 4061));
        badAIWaypointList.add(new Waypoint(5202, 4343));
        badAIWaypointList.add(new Waypoint(2160, 4329));
        badAIWaypointList.add(new Waypoint(1488, 4146));
        badAIWaypointList.add(new Waypoint(1080, 3467));
    }
        
    public Waypoint getGoodWaypoint(int i) {
        return goodAIWaypointList.get(i);
    }
    
    
        public static WaypointHandler getInstance()
    {
        if (instance == null)
        {
            instance = new WaypointHandler();
        }
        return instance;
    }
    
    
}
