
package simpplle.comcode;

import java.awt.Color;

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

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * 
 * Returns common probability of drought mortality within a given zone and ecological vegetative unit of juniper pine if climate is drought and prior state process type is root disease, light bark beetles, or severe bark beetles
 * @param zone regional zone
 * @param evu  ecological vegetative unit to be evaluated
 * @return 0 if not juniper-ponderosa, 50 if juniper ponderosa without root disease, light or severe bark beetles, 100 with them
 */
  public int doProbabilityCommon(RegionalZone zone, Evu evu) {
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
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    return 0;
  }

  /**
   * Drought Mortality does not occur in Eastside Region 1, so returns 0.
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return 0;
  }

  public int doProbability (SierraNevada zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  public int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  /**
   * Drought Mortality does not occur in Gila, so returns 0.
   */
  public int doProbability (Gila               zone, Evu evu) { return 0; }
  /**
   * Drought Mortality does not occur in South Central Alaska, so returns 0.
   */
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  /**
   * Drought Mortality does not occur in Westside Region 1, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Drought Mortality does not occur in Eastside Region 1, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon();
  }

  /**
   * Drought Mortality does not occur in Gila, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Drought Mortality does not occur in South Central Alaska, so returns false meaning no spread computaion.  
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * outputs "DROUGHT-MORTALITY"
   */
  public String toString () {
    return printName;
  }
}
