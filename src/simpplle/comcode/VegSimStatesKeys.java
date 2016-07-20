/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class contains methods which define Vegetative Simulation States Keys.  This class has two methods, one which creates a 
 *string 2d array of keys of time [steps] [lifeformId's] and a method to get the keys.
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */
public class VegSimStatesKeys {
  private static final String[][] keys;

//  static {
//    Lifeform[] lives = Lifeform.getAllValues();
//
//    keys = new String[Simulation.MAX_SIMULATIONS][Simulation.MAX_TIME_STEPS][lives.length];
//
//    String str;
//    for (int r=0; r<Simulation.MAX_SIMULATIONS; r++) {
//      for (int ts=0; ts<Simulation.MAX_TIME_STEPS; ts++) {
//        for (int l=0; l<lives.length; l++) {
//          keys[r][ts][lives[l].getId()] =
//              new String(IntToString.get(r) + "-" +
//                         IntToString.get(ts) + "-" +
//                         lives[l].toString());
//        }
//      }
//    }
//  }
  static {
    Lifeform[] lives = Lifeform.getAllValues();

    keys = new String[Simulation.MAX_TIME_STEPS][lives.length];

    String str;
    for (int ts = 0; ts < Simulation.MAX_TIME_STEPS; ts++) {
      for (int l = 0; l < lives.length; l++) {
        keys[ts][lives[l].getId()] =
            new String(IntToString.get(ts) + "-" +
                       lives[l].toString());
      }
    }
  }

  public static String get(int tStep, Lifeform life) {
    return keys[tStep][life.getId()];
  }
}

