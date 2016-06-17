
package simpplle.comcode;

import java.awt.*;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> StandReplacingFire is a spreading process that does not occur yearly.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class StandReplacingFire extends Process {

  private static final String printName = "STAND-REPLACING-FIRE";

  /**
   * Creates a stand replacing fire with yearlyProcess = false.
   */
  public StandReplacingFire () {

    spreading     = true;
    description   = "Stand Replacing Fire";
    color         = Color.RED;
    yearlyProcess = false;

  }

  /**
   * Performs spread calculations if the fire is not suppressed, there are uniform size polygons, and this is not
   * southwest Utah. Returns true if the process was spread.
   */
  public boolean doSpread(RegionalZone zone, Evu fromEvu, Evu evu) {

    boolean fireSuppression = Simpplle.getCurrentSimulation().fireSuppression();

    int ts = Simulation.getCurrentTimeStep();

    VegSimStateData state = evu.getState();

    FireSuppBeyondClassALogic logicInst = FireSuppBeyondClassALogic.getInstance();

    if (fireSuppression &&
        zone.getId() != ValidZones.SOUTHWEST_UTAH &&
        !Simpplle.getCurrentArea().hasUniformSizePolygons() &&
        logicInst.isSuppressed(state.getVeg(),ProcessType.SRF,fromEvu,evu,ts,state.getLifeform())) {

      return false;

    } else {

      return FireEvent.doFireSpread(zone,this,fromEvu,evu);

    }
  }

  public String toString () {
    return printName;
  }

}

