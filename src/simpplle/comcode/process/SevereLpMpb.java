
package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-001.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Mountain Pine Beetle in Lodgepole Pine, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * @since V2.5 SIMPPLLE
 *
 *@see simpplle.comcode.Process
 *
 */
public class SevereLpMpb extends Process {
  private static final String printName = "SEVERE-LP-MPB";
/**
 * Constructor for severe Lodgepole Pine Mountain Pine Beetle.  
 */
  public SevereLpMpb () {
    super();

    spreading   = true;
    description = "Severe LodgePole Pine Mountain Pine Beetle";

    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.ADJ_PROCESS_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.MPB_HAZARD_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.ADJ_HIGH_HAZARD_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.ADJ_MOD_HAZARD_COL.toString());
    defaultVisibleColumns.add(simpplle.comcode.logic.ProcessProbLogic.Columns.PROB_COL.toString());
  }

  private int doProbabilityCommon () {
    int prob = LpMpb.severeProb;

    LpMpb.severeProb = -1;
    return prob;
  }
/**
 * For westside region 1 do common probability
 */
  public int doProbability (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    if (LpMpb.severeProb == -1) {
      LpMpb.adjust(zone,evu);
    }
    return doProbabilityCommon();
  }
  public int doProbability (simpplle.comcode.zone.ColoradoFrontRange zone, simpplle.comcode.element.Evu evu) {
    if (LpMpb.severeProb == -1) {
      LpMpb.adjust(zone,evu);
    }
    return doProbabilityCommon();
  }

  public int doProbability (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityEastAndSimilar(zone,evu);
  }
  public int doProbability (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityEastAndSimilar(zone,evu);
  }
  public int doProbability (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu evu) {
    return doProbabilityEastAndSimilar(zone,evu);
  }
  /**
   * Probability of Severe Lodgepole Mountain Pine Beetle, for Eastside Region one, teton, and northern central Rockies.
   * 
   * @param zone regional zone being evaluated
   * @param evu
   * @return probability of severe lodgepole mountain pine beetle
   */
  private int doProbabilityEastAndSimilar (RegionalZone zone, simpplle.comcode.element.Evu evu) {
    Species     species;
    int         cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    boolean isRecentSevereLpMpb = false;  // recent being the last 6 time steps.

    int tsCount=0;
    int ts = cTime;
    VegSimStateData state = evu.getState(ts);
    if (state == null) { return 0; }
    isRecentSevereLpMpb = (ProcessType.SEVERE_LP_MPB == state.getProcess());
    ts--;
    tsCount++;

    while (!isRecentSevereLpMpb && ts >=0 && tsCount<=6) {
      VegSimStateData tmpState = evu.getState(ts);
      if (tmpState != null) {
        isRecentSevereLpMpb = (ProcessType.SEVERE_LP_MPB == tmpState.getProcess());
      }
      ts--;
      tsCount++;
    }

    if (LpMpb.severeProb == -1) {
      species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.LP       && species != Species.DF_LP    &&
          species != Species.LP_AF    && species != Species.ES_LP    &&
          species != Species.DF_LP_AF && species != Species.DF_PP_LP &&
          species != Species.DF_LP_ES && species != Species.WB_ES_LP &&
          species != Species.AF_ES_LP &&
          !isRecentSevereLpMpb) {

        LpMpb.lightProb  = 0;
        LpMpb.severeProb = 0;
      }
      else {
       LpMpb.adjust(zone,evu);
      }
    }
    return doProbabilityCommon();
  }
/**
 * Calculates spread for Westside Region.  if Lodge Pole Mountain pine beetle are currently designated to spread for this zone, returns true
 * else checks if evu the ponderosa pine mountain pine beetle process probability is above a threshold, it returns true, else returns false
 */
  public boolean doSpread (simpplle.comcode.zone.WestsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    if (LpMpb.doSpread(zone,evu)) {
      evu.updateCurrentProcess(this.getType());
      evu.updateCurrentProb(simpplle.comcode.element.Evu.S);
      return true;
    }
    else {
      int ppMpbProb = evu.getProcessProb(ProcessType.PP_MPB);

      if (ppMpbProb >= 1000) {
        evu.updateCurrentProcess(ProcessType.PP_MPB);
        evu.updateCurrentProb(simpplle.comcode.element.Evu.S);
        return true;
      }
    }
    return false;
  }
  /**
   * 
   */
  public boolean doSpread (simpplle.comcode.zone.ColoradoFrontRange zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }
    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (prob == simpplle.comcode.element.Evu.L ||
        (processType.equals(ProcessType.SUCCESSION) == false)) {
      return false;
    }

    if (evu.getProcessProb(ProcessType.LIGHT_LP_MPB) >= 5000 ||
        evu.getProcessProb(ProcessType.SEVERE_LP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.SEVERE_LP_MPB);
      evu.updateCurrentProb(simpplle.comcode.element.Evu.S);
      return true;
    }
    else if (evu.getProcessProb(ProcessType.PP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.PP_MPB);
      evu.updateCurrentProb(simpplle.comcode.element.Evu.S);
      return true;
    }

    return false;
  }
/**
 * uses the spread calculation from east and similar regions.  
 */
  public boolean doSpread (simpplle.comcode.zone.EastsideRegionOne zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadEastAndSimilar(zone,fromEvu,evu);
  }
  /**
   * uses the spread calculation from east and similar regions.
   */
  public boolean doSpread (simpplle.comcode.zone.Teton zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadEastAndSimilar(zone,fromEvu,evu);
  }
  /**
   * uses the spread calculation from east and similar regions.
   */
  public boolean doSpread (simpplle.comcode.zone.NorthernCentralRockies zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    return doSpreadEastAndSimilar(zone,fromEvu,evu);
  }
  /**
   * 
   * @param zone
   * @param fromEvu
   * @param evu
   * @return
   */
  private boolean doSpreadEastAndSimilar(RegionalZone zone, simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu evu) {
    int         cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    Density     density;

    boolean isRecentSevereLpMpb = false;  // recent being the last 6 time steps.

    int tsCount=0;
    int ts = cTime-1;
    VegSimStateData state = evu.getState(ts);
    if (state == null) { return false; }

    isRecentSevereLpMpb = (ProcessType.SEVERE_LP_MPB == state.getProcess());
    ts--;
    tsCount++;

    while (!isRecentSevereLpMpb && ts >=0 && tsCount<=5) {
      VegSimStateData tmpState = evu.getState(ts);
      if (tmpState != null) {
        isRecentSevereLpMpb = (ProcessType.SEVERE_LP_MPB == tmpState.getProcess());
      }
      ts--;
      tsCount++;
    }

    density = state.getVeg().getDensity();

    if (LpMpb.doSpread(zone,evu) &&
        !isRecentSevereLpMpb &&
        (density == Density.THREE || density == Density.FOUR)) {
      evu.updateCurrentProcess(this.getType());
      evu.updateCurrentProb(simpplle.comcode.element.Evu.S);
      return true;
    }
    else {
      int ppMpbProb = evu.getProcessProb(ProcessType.PP_MPB);

      if (ppMpbProb >= 1000) {
        evu.updateCurrentProcess(ProcessType.PP_MPB);
        evu.updateCurrentProb(simpplle.comcode.element.Evu.S);
        return true;
      }
    }
    return false;
  }

/**
 * outputs  "SEVERE-LP-MPB"
 */
  public String toString () {
    return printName;
  }

}

