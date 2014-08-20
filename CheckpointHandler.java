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
public class CheckpointHandler {
    
    protected ArrayList<Checkpoint> checkpoints;
    
    public CheckpointHandler()  {
//        File file = new File("src/assets/LoopTrackCheckpoints.json");
//        Scanner scanner = new Scanner(file);;
//        String json = "";
//        while(scanner.hasNext()) {
//            json += scanner.nextLine();
//        }
//        System.out.println(""+json);
//        JsonParser jsonParser = new JsonParser();
//        jsonParser.parse(json);
        
        // Quick set up, read in from JSON or similar later.
        quickAndRoughSetup();
      
    }

    private void quickAndRoughSetup() {
        checkpoints.add(new Checkpoint(856, 2400, 1456, 2400, "StartLine", 0));
        checkpoints.add(new Checkpoint(3500, 470, 3500, 998, "QuarterLap", 1));
        checkpoints.add(new Checkpoint(5576, 2400, 6200, 2400, "Halfway", 2));
        checkpoints.add(new Checkpoint(3500, 3978, 3500, 4553, "ThreeQuarterLap", 3));
    }
    
    
    
    
}
