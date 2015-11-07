
package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class has methods for Drought Mortality, a type of Process. 
 * It occurs in Sierra Nevada.   
 * It does not occur in Eastside Region 1 or Westside Region 1
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.Process
 */

// Does not occur in Eastside or Westside.
public class DroughtMortality extends Process {
  private static final String printName = "DROUGHT-MORTALITY";
  public DroughtMortality() {
    super();

    spreading   = false;
    description = "Drought Mortality";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * 
 * Returns common probability of drought mortality within a given zone and ecological vegetative unit of juniper pine if climate is drought and prior state process type is root disease, light bark beetles, or severe bark beetles
 * @param zone regional zone
 * @param evu  ecological vegetative unit to be evaluated
 * @return 0 if not juniper-ponderosa, 50 if juniper ponderosa without root disease, light or severe bark beetles, 100 with them
 */
  public int doProbabilityCommon(RegionalZone zone, simpplle.comcode.element.Evu evu) {
    Species     species     = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    int         cTime       = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData priorState = evu.getState(cTime-1);
    ProcessType processType = (priorState != null ? priorState.getProcess() : ProcessType.NONE);

    if (species != Species.JP) { return 0; }

    if (Simpplle.getClimate().isDrought()) {
      if (processType.isRootDisease() ||
          processType.equals(ProcessType.LIGHT_BARK_BEETLES) ||
          processType.equals(ProcessType.SEVERE_BARK_BEETLES)) {
        return 100;
      }
      else {
        return 50;
      }
    }
    else {
      return 0;
    }
  }

 /**
  * Drought Mortality does not occur in Westside Region 1, so returns 0.
  */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }

  /**
   * Drought Mortality does not occur in Eastside Region 1, so returns 0.
   */
  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return 0;
  }

  public int doProbability (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  public int doProbability (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  /**
   * Drought Mortality does not occur in Gila, so returns 0.
   */
  public int doProbability (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu evu) { return 0; }
  /**
   * Drought Mortality does not occur in South Central Alaska, so returns 0.
   */
  public int doProbability (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  /**
   * Drought Mortality does not occur in Westside Region 1, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }

  /**
   * Drought Mortality does not occur in Eastside Region 1, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return false;
  }

  public boolean doSpread (simpplle.comcode.zone.SierraNevada zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }

  public boolean doSpread (simpplle.comcode.zone.SouthernCalifornia zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadCommon();
  }

  /**
   * Drought Mortality does not occur in Gila, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (simpplle.comcode.zone.Gila zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }
  /**
   * Drought Mortality does not occur in South Central Alaska, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (simpplle.comcode.zone.SouthCentralAlaska zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) { return false; }

  /**
   * outputs "DROUGHT-MORTALITY"
   */
  public String toString () {
    return printName;
  }
}
