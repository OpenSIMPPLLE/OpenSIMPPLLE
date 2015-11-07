
package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.FireEvent;
import simpplle.comcode.Process;

import java.awt.Color;



/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Mixed Severity Fire, a type of Disturbance Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 * @see simpplle.comcode.Process
 */
public class MixedSeverityFire extends Process {
  private static final String printName = "MIXED-SEVERITY-FIRE";
  /**
   * Constructor for Mixed Severity Fire.  Inherits from Process superclass and initializes spreading to true (cause fires spread)
   * and yearly process to false (not able to be sure that Mixed Severity Fire are yearly processes), the colors and description.
   */
  public MixedSeverityFire () {
    super();

    spreading   = true;
    description = "Mixed Severity Fire";
    color       = new Color(255,110,0);
    yearlyProcess = false;
  }
/**
 * Calculates whether the Mixed Severity Fire spreads.  
 * Returns false if fire is suppressed otherwise returns spreading boolean based on to and from Evu and fire resistance.  
 * 
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
        logicInst.isSuppressed(vegType,ProcessType.MSF,fromEvu,evu,ts,lifeform)) {
      return false;
    }
    else {
      return FireEvent.doFireSpread(zone,this,fromEvu,evu);
    }
  }
/**
 * outputs "MIXED-SEVERITY-FIRE"
 */
  public String toString () {
    return printName;
  }

}

