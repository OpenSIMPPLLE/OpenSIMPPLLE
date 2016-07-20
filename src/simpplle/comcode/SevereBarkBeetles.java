/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class defines Severe Bark Beetles, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *@see simpplle.comcode.Process
 */

// Does not occur in Eastside or Westside.
public class SevereBarkBeetles extends Process {
  private static final String printName = "SEVERE-BARK-BEETLES";
 /**
  * Constructor for Severe Bark Beetles.  Inherits from Process superclass and initializes spreading to false, and default visible columns to row and probability
  */
  public SevereBarkBeetles() {
    super();

    spreading   = true;
    description = "Severe Bark Beetles";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * Will not have Severe Bark Beetles unless there is drought conditions.  
 * @param zone the regional zone being evaluated for severe bark beetles
 * @param evu the existing vegetative unit being evaluated for severe bark beetles
 * @return 0 if not drought, the probability of severe bark beetles otherwise
 */
  public int doProbabilityCommon(RegionalZone zone, Evu evu) {
   
    if (Simpplle.getClimate().isDrought() == false) {
      return 0;
    }

    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();
    Density     density     = state.getVeg().getDensity();
    int         cTime       = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    int         probability = 0;
    int         page = -1;
    int         row  = -1;
    int         col  = 0;

    VegSimStateData priorState = evu.getState(cTime-1);
    if (priorState == null) { return 0; }
    ProcessType processType = priorState.getProcess();

    if (species == Species.MC_PP || species == Species.MC_IC ||
        species == Species.MC_WF || species == Species.MC_DF ||
        species == Species.MC_RF || species == Species.BO_PP ||
        species == Species.PC    || species == Species.PJ    ||
        species == Species.JP    || species == Species.PP    ||
        species == Species.LP    || species == Species.DF    ||
        species == Species.WB    || species == Species.WP    ||
        species == Species.LP_RF) {
      page = 0;
    }
    else {
      return 0;
    }

    if (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.LARGE ||
        sizeClass == SizeClass.VERY_LARGE) {
      row = 0;
    }
    else if (sizeClass == SizeClass.MTS || sizeClass == SizeClass.MMU ||
             sizeClass == SizeClass.LTS || sizeClass == SizeClass.LMU ||
             sizeClass == SizeClass.VLMU || sizeClass == SizeClass.VLTS) {
      row = 2;
    }
    else {
      return 0;
    }

    if (density == Density.THREE) { row += 0; }
    else if (density == Density.FOUR) { row += 1; }
    else {
      return 0;
    }

    if ((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
        (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) {
      col += 1;
    }

    probability = Process.findInstance(ProcessType.LIGHT_BARK_BEETLES).getProbData(page,row,col);
    return probability;
  }

 /**
  * Severe Bark Beetles do not occur in the Westside region 1, therefore it returns 0 by default
  */
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    return 0;
  }

 /**
  * Severe Bark Beetles do not occur in the Eastside region 1, therefore it returns 0 by default
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
  * Severe Bark Beetles do not occur in the Gila, therefore it returns 0 by default
  */
  public int doProbability (Gila               zone, Evu evu) { return 0; }
  /**
   * Severe Bark Beetles do not occur in the South Central Alaska, therefore it returns 0 by default
   */
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  /**
   * Severe Bark Beetles do not occur in the Westside region 1, therefore spread returns false by default
   */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Severe Bark Beetles do not occur in the Eastside region 1, therefore spread returns false by default
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
   * Severe Bark Beetles do not occur in the Gila, therefore spread returns false by default
   */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Severe Bark Beetles do not occur in the South Central Alaska, therefore spread returns false by default
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }
/**
 * outputs "SEVERE-BARK-BEETLES"
 */
  public String toString () {
    return printName;
  }
}
