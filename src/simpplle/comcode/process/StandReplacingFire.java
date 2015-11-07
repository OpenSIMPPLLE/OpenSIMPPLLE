
package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.FireEvent;
import simpplle.comcode.Process;

import java.awt.Color;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class defines Stand Replacing Fire (SRF), a type of Disturbance Process.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *@see simpplle.comcode.Process
 *
 */

public class StandReplacingFire extends Process {
  private static final String printName = "STAND-REPLACING-FIRE";
/**
 * Constructor for Stand Replacing Fire.  Inherits from process, and initializes spreading to true, color to red and yearly process to false
 */
  public StandReplacingFire () {
    super();

    spreading   = true;
    description = "Stand Replacing Fire";
    color       = Color.RED;
    yearlyProcess = false;
  }
/**
 * Method to calculate if Stand replacing fire spreads.  If fire is suppressed returns false, otherwise returns the result of fire spread calculation based
 * on the fire resistance of Evu.  
 */
  public boolean doSpread(RegionalZone zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    boolean fireSupp = Simpplle.getCurrentSimulation().fireSuppression();
    int     ts       = Simulation.getCurrentTimeStep();

    VegSimStateData state    = evu.getState();
    VegetativeType  vegType  = state.getVeg();
    Lifeform        lifeform = state.getLifeform();

    simpplle.comcode.logic.FireSuppBeyondClassALogic logicInst = simpplle.comcode.logic.FireSuppBeyondClassALogic.getInstance();
    boolean hasUniformPolygons = Simpplle.getCurrentArea().hasUniformSizePolygons();

    if (fireSupp &&
        zone.getId() != ValidZones.SOUTHWEST_UTAH &&
        !hasUniformPolygons &&
        logicInst.isSuppressed(vegType,ProcessType.SRF,fromEvu,evu,ts,lifeform))
    {
      return false;
    }
    else {
      return FireEvent.doFireSpread(zone,this,fromEvu,evu);
    }
  }

  /**
   * outputs "STAND-REPLACING-FIRE"
   */
  public String toString () {
    return printName;
  }

}

