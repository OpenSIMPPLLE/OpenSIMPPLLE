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
 * <p>This class contains methods for Light Lodgepole Pine Mountain Pine Beetle , a type of Process.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *
 * 
 */


public class LightLpMpb extends Process {
  private static final String printName = "LIGHT-LP-MPB";
  /**
   * Constructor for light lodgepole pine mountain pine beetle.  Inherits from Process superclass, and initializes spreading to true, description, and default visible columns
   *  Note: bug fixed: corrected name of description - BLosi -9/27/2013 
   */
  public LightLpMpb () {
    super();

    spreading   = true;
    description = "Light LodgePole Pine Mountain Pine Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.MPB_HAZARD_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_HIGH_HAZARD_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_MOD_HAZARD_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  private int doProbabilityCommon () {
    int prob = LpMpb.lightProb;

    LpMpb.lightProb = -1;
    return prob;
  }

  public int doProbability (WestsideRegionOne zone, Evu evu) {
    if (LpMpb.lightProb == -1) {
      LpMpb.adjust(zone,evu);
    }
    return doProbabilityCommon();
  }
  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    if (LpMpb.lightProb == -1) {
      LpMpb.adjust(zone,evu);
    }
    return doProbabilityCommon();
  }

  public int doProbability (EastsideRegionOne zone, Evu evu) {
    return doProbabilityEastAndSimilar(zone, evu);
  }
  public int doProbability (Teton zone, Evu evu) {
    return doProbabilityEastAndSimilar(zone, evu);
  }
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    return doProbabilityEastAndSimilar(zone, evu);
  }
  /**
   * this method is private because it is called from NorthCentralRockies, Teton, Eastside Region 1.  
   * This is different from many of the process types which usually refer to the superclass 
   * @param zone
   * @param evu
   * @return
   */
  private int doProbabilityEastAndSimilar(RegionalZone zone, Evu evu) {
    int     cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    boolean isRecentSevereLpMpb = false;  // recent being the last 6 time steps.

    int tsCount=0;
    int ts=cTime;
    VegSimStateData state = evu.getState(ts);
    if (state == null) { return 0; }

    isRecentSevereLpMpb = (ProcessType.SEVERE_LP_MPB == state.getProcess());
    ts--;
    tsCount++;

    while (!isRecentSevereLpMpb && ts >=0 && tsCount<=6) {
      VegSimStateData tmpState = evu.getState(ts);
      if (tmpState == null) { continue; }
      isRecentSevereLpMpb = (ProcessType.SEVERE_LP_MPB == tmpState.getProcess());
      ts--;
      tsCount++;
    }

   if (LpMpb.lightProb == -1) {
      Species species = state.getVeg().getSpecies();
      if (species == Species.LP       || species == Species.DF_LP    ||
          species == Species.LP_AF    || species == Species.ES_LP    ||
          species == Species.DF_LP_AF || species == Species.DF_PP_LP ||
          species == Species.DF_LP_ES || species == Species.WB_ES_LP ||
          species == Species.AF_ES_LP &&
          (isRecentSevereLpMpb == false)) {
        LpMpb.adjust(zone,evu);
      }
      else {
        LpMpb.lightProb  = 0;
        LpMpb.severeProb = 0;
      }
    }
    return doProbabilityCommon();
  }

/**
 * method to calculate spread of Light LodgePole Pine Mountain Pine Beetle
 * @param zone regional zone being evaluated
 * @param evu existing vegetative unit being evaluated
 * @return
 */
  private boolean doSpreadCommon (RegionalZone zone, Evu evu) {
    if (LpMpb.doSpread((RegionalZone)zone,evu)) {
      evu.updateCurrentProcess(this.getType());
      evu.updateCurrentProb(Evu.S);
      return true;
    }
    else { return false; }
  }

  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(zone,evu);
  }
/**
 * overloaded doSpread method for the Colorado Front Range based on severity of Light LodgePole Pine Mountain Pine Beetle - light or severe
 */
  public boolean doSpread (ColoradoFrontRange zone, Evu fromEvu, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    if (state.getProb() == Evu.L ||
        (state.getProcess().equals(ProcessType.SUCCESSION) == false)) {
      return false;
    }

    if (evu.getProcessProb(ProcessType.LIGHT_LP_MPB) >= 5000 ||
        evu.getProcessProb(ProcessType.SEVERE_LP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.LIGHT_LP_MPB);
      evu.updateCurrentProb(Evu.S);
      return true;
    }

    return false;
  }
/**
 * outputs  "LIGHT-LP-MPB"
 */
  public String toString () {
    return printName;
  }

}

