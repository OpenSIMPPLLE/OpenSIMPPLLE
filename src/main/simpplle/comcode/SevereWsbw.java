/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class contains methods for Severe Western Spruce Budworm, a type of Process.
 */

public class SevereWsbw extends Process {

  private static final String printName = "SEVERE-WSBW";

  public SevereWsbw () {

    super();

    spreading = false;
    description = "Severe Western Spruce Budworm";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());

  }

  /*private int doProbabilityCommon() {
    int prob = Wsbw.severeProb;
    Wsbw.severeProb = -1;
    return prob;
  }

  public int doProbability (WestsideRegionOne zone, Evu evu) {
    if (!Wsbw.isEnabled()) return 0;
    if (Wsbw.severeProb == -1) {
      int sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
      Wsbw.doProbability(sIndex);
    }
    return doProbabilityCommon();
  }

  public int doProbability (Teton zone, Evu evu) {
    if (!Wsbw.isEnabled()) return 0;
    if (Wsbw.severeProb == -1) {
      Species species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.DF       && species != Species.AF       &&
          species != Species.ES       && species != Species.ES_LP    &&
          species != Species.ES_AF    && species != Species.DF_AF    &&
          species != Species.DF_ES    && species != Species.PP_DF    &&
          species != Species.DF_LP    && species != Species.LP_AF    &&
          species != Species.DF_PP_LP && species != Species.DF_LP_ES &&
          species != Species.AF_ES_LP && species != Species.DF_LP_AF) {
        Wsbw.lightProb  = 0;
        Wsbw.severeProb = 0;
      } else {
        int sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
        Wsbw.doProbability(sIndex);
      }
    }
    return doProbabilityCommon();
  }

  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    if (!Wsbw.isEnabled()) return 0;
    if (Wsbw.severeProb == -1) {
      Species species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.DF       && species != Species.AF       &&
          species != Species.ES       && species != Species.ES_LP    &&
          species != Species.ES_AF    && species != Species.DF_AF    &&
          species != Species.DF_ES    && species != Species.PP_DF    &&
          species != Species.DF_LP    && species != Species.LP_AF    &&
          species != Species.DF_PP_LP && species != Species.DF_LP_ES &&
          species != Species.AF_ES_LP && species != Species.DF_LP_AF) {
        Wsbw.lightProb  = 0;
        Wsbw.severeProb = 0;
      } else {
        int sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
        Wsbw.doProbability(sIndex);
      }
    }
    return doProbabilityCommon();
  }

  public int doProbability (EastsideRegionOne zone, Evu evu) {
    if (!Wsbw.isEnabled()) return 0;
    if (Wsbw.severeProb == -1) {
      Species species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.DF       && species != Species.AF       &&
          species != Species.ES       && species != Species.ES_LP    &&
          species != Species.ES_AF    && species != Species.DF_AF    &&
          species != Species.DF_ES    && species != Species.PP_DF    &&
          species != Species.DF_LP    && species != Species.LP_AF    &&
          species != Species.DF_PP_LP && species != Species.DF_LP_ES &&
          species != Species.AF_ES_LP && species != Species.DF_LP_AF) {
        Wsbw.lightProb  = 0;
        Wsbw.severeProb = 0;
      } else {
        int sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
        Wsbw.doProbability(sIndex);
      }
    }
    return doProbabilityCommon();
  }

  private boolean doSpreadCommon (RegionalZone zone, Evu evu) {
    return Wsbw.doSpread(zone,this,evu);
  }

  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(zone, evu);
  }

  public boolean doSpread (EastsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(zone,evu);
  }

  public boolean doSpread (Teton zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(zone,evu);
  }

  public boolean doSpread (NorthernCentralRockies zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(zone,evu);
  }

  public boolean doSpread(SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public boolean doSpread(SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) {
    return false;
  }

  public String toString () {
    return printName;
  }*/
}

