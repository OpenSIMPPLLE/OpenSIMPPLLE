

package simpplle.comcode;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for Light Bark Beetles, a type of Process.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
 */


// Does not occur in Eastside or Westside.
public class LightBarkBeetles extends Process {
  private static final String printName = "LIGHT-BARK-BEETLES";
  /**
   * Constructor for Light Bark Beetles.  Inherits from Process superclass and initializes visible columns, description and spreading to false
   */
  public LightBarkBeetles() {
    super();

    spreading   = false;
    description = "Bark Beetles";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }
/**
 * common probability calculations.  If drought conditions, the process will be Severe Bark Beetles and light will have zero probability.
 * @param zone regional zone being evaluated
 * @param evu ecological vegetative unit being evaluated
 * @return integer probability
 */
  public int doProbabilityCommon(RegionalZone zone, Evu evu) {
  
    if (Simpplle.getClimate().isDrought()) {
      return 0;
    }

    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }
    Species   species     = state.getVeg().getSpecies();
    SizeClass sizeClass   = state.getVeg().getSizeClass();
    Density   density     = state.getVeg().getDensity();

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
    else if (sizeClass == SizeClass.MTS  || sizeClass == SizeClass.MMU ||
             sizeClass == SizeClass.LTS  || sizeClass == SizeClass.LMU ||
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
    probability = getProbData(page,row,col);
    return probability;
  }

  /**
   * Westside Region 1 does not have this process, doProbability returns a 0.
   */
  public int doProbability (WestsideRegionOne zone, Evu evu) {return 0;}

  /**
   * Eastside Region 1 does not have this process, doProbability returns a 0.
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {return 0;}

  public int doProbability (SierraNevada zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  public int doProbability (SouthernCalifornia zone, Evu evu) {
    return doProbabilityCommon(zone,evu);
  }

  
  /**
   * Gila does not have this process, doProbability returns a 0.
   */
  public int doProbability (Gila               zone, Evu evu) { return 0; }
  /**
   * South Central Alaska does not have this process, doProbability returns a 0.
   */
  public int doProbability (SouthCentralAlaska zone, Evu evu) { return 0; }

  private boolean doSpreadCommon () {
    return false;
  }

  /**
   * Westside Region 1 does not have this process, doSpread returns false.
   */
  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return false;
  }

 /**
  * Eastside Region 1 does not have this process, doSpread returns false.
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
  *Gila does not have this process, doSpread returns false.
  */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * South Central Alaska does not have this process, doSpread returns false.
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * outputs "LIGHT-BARK-BEETLES"
   */
  public String toString () {
    return printName;
  }
}
