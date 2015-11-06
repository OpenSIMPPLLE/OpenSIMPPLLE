
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
 *<p>This class defines the LightSeverityFire class, a type of Disturbance Process.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
 */

public class LightSeverityFire extends Process {
  private static final String printName = "LIGHT-SEVERITY-FIRE";
  /**
   * Constructor.  Inherits from Process superclass and initializes spreading to true, description, color, and yearly process to false
   */
  public LightSeverityFire () {
    super();

    spreading   = true;
    description = "Light Severity Fire";
    color       = new Color(255,165,0);
    yearlyProcess = false;
  }
/**
 * calculates fire spread.  If fire is suppressed returns false, otherwise calculates spread boolean
 * based on to and from Evu and fire resistance.  
 */
  public boolean doSpread(RegionalZone zone, Evu fromEvu, Evu evu) {
    boolean fireSupp = Simpplle.getCurrentSimulation().fireSuppression();
    int     ts       = Simulation.getCurrentTimeStep();

    VegSimStateData state    = evu.getState();
    VegetativeType  vegType  = state.getVeg();
    Lifeform        lifeform = state.getLifeform();

    FireSuppBeyondClassALogic logicInst = FireSuppBeyondClassALogic.getInstance();
    boolean hasUniformPolygons = Simpplle.getCurrentArea().hasUniformSizePolygons();

    if (fireSupp &&
        zone.getId() != ValidZones.SOUTHWEST_UTAH &&
        !hasUniformPolygons &&
        logicInst.isSuppressed(vegType,ProcessType.LSF,fromEvu,evu,ts,lifeform)) {
      return false;
    }
    else {
      return FireEvent.doFireSpread(zone,this,fromEvu,evu);
    }
  }
  /**
   * outputs "LIGHT-SEVERITY-FIRE"
   */

  public String toString () {
    return printName;
  }

}

