/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.awt.*;

/**
 * MixedSeverityFire is a spreading process that does not occur yearly.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class MixedSeverityFire extends Process {

  private static final String printName = "MIXED-SEVERITY-FIRE";

  /**
   * Creates a mixed severity fire with yearlyProcess = false.
   */
  public MixedSeverityFire () {

    spreading     = true;
    description   = "Mixed Severity Fire";
    color         = new Color(255,110,0);
    yearlyProcess = false;

  }

  /**
   * Performs spread calculations if the fire is not suppressed, there are uniform size polygons, and this is not
   * southwest Utah. Returns true if the process was spread.
   */
  public boolean doSpread(RegionalZone zone, Evu fromEvu, Evu evu) {

    int ts = Simulation.getCurrentTimeStep();

    VegSimStateData state = evu.getState();

    FireSuppBeyondClassALogic logicInst = FireSuppBeyondClassALogic.getInstance();

    if (Simpplle.getCurrentSimulation().fireSuppression() &&
        zone.getId() != ValidZones.SOUTHWEST_UTAH &&
        !Simpplle.getCurrentArea().hasUniformSizePolygons() &&
        logicInst.isSuppressed(state.getVeg(),ProcessType.MSF,fromEvu,evu,ts,state.getLifeform())) {

      return false;

    } else {

      return FireEvent.doFireSpread(zone,this,fromEvu,evu);

    }
  }

  public String toString () {
    return printName;
  }

}
