/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class defines Severe Western Spruce Budworm, a type of process.
 * <p>There is no Severe Western Spruce Budworm in Gila, South Central Alaska, Sierra Nevada, or Southern California.  No methods 
 * are given for those regions probability (as opposed to normal 0 probability return methods.  doSpread still returns false for those 
 * regions though. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *@see simpplle.comcode.Process
 */

public class SevereWsbw extends Process {
  private static final String printName = "SEVERE-WSBW_OLD";
  /**
   * Constructor for Severe Western Spruce Budworm.  Inherits from Process superclass and initializing spread to true.  
   */
  public SevereWsbw () {
    super();

    spreading   = true;
    description = "Severe Western Spruce Budworm";
  }

  private int doProbabilityCommon() {
    int prob = Wsbw.severeProb;

    Wsbw.severeProb = -1;
    return prob;
  }
/**
 * returns 0 if no western spruce budworm or computes wspw severe probability according to compute suscepti
 */
  public int doProbability (WestsideRegionOne zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int sIndex;

    if (Wsbw.severeProb == -1) {
      sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
      Wsbw.doProbability(sIndex);
    }
    return doProbabilityCommon();
  }
/**
 * if no wsbw returns 0, else if severe probability is set to -1, which is the flag which starts probability calculation
 * based on species 
 */
  public int doProbability (Teton zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int     sIndex;
    Species species;

    if (Wsbw.severeProb == -1) {
      species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.DF       && species != Species.AF       &&
          species != Species.ES       && species != Species.ES_LP    &&
          species != Species.ES_AF    && species != Species.DF_AF    &&
          species != Species.DF_ES    && species != Species.PP_DF    &&
          species != Species.DF_LP    && species != Species.LP_AF    &&
          species != Species.DF_PP_LP && species != Species.DF_LP_ES &&
          species != Species.AF_ES_LP && species != Species.DF_LP_AF) {
        Wsbw.lightProb  = 0;
        Wsbw.severeProb = 0;
      }
      else {
        sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
        Wsbw.doProbability(sIndex);
      }
    }
    return doProbabilityCommon();
  }
  /**
   * if no western spruce budworm in area, returns 0.  else if the -1 flag for severe probability
   * the probability is calculated based on species 
   */
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int     sIndex;
    Species species;

    if (Wsbw.severeProb == -1) {
      species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.DF       && species != Species.AF       &&
          species != Species.ES       && species != Species.ES_LP    &&
          species != Species.ES_AF    && species != Species.DF_AF    &&
          species != Species.DF_ES    && species != Species.PP_DF    &&
          species != Species.DF_LP    && species != Species.LP_AF    &&
          species != Species.DF_PP_LP && species != Species.DF_LP_ES &&
          species != Species.AF_ES_LP && species != Species.DF_LP_AF) {
        Wsbw.lightProb  = 0;
        Wsbw.severeProb = 0;
      }
      else {
        sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
        Wsbw.doProbability(sIndex);
      }
    }
    return doProbabilityCommon();
  }
  /**
   * if no western spruce budworm in area, returns 0.  else if the -1 flag for severe probability
   * the probability is calculated based on species  
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int     sIndex;
    Species species;

    if (Wsbw.severeProb == -1) {
      species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species != Species.DF       && species != Species.AF       &&
          species != Species.ES       && species != Species.ES_LP    &&
          species != Species.ES_AF    && species != Species.DF_AF    &&
          species != Species.DF_ES    && species != Species.PP_DF    &&
          species != Species.DF_LP    && species != Species.LP_AF    &&
          species != Species.DF_PP_LP && species != Species.DF_LP_ES &&
          species != Species.AF_ES_LP && species != Species.DF_LP_AF) {
        Wsbw.lightProb  = 0;
        Wsbw.severeProb = 0;
      }
      else {
        sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
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

 /**
  * Western Spruce Budworm does not occur in Sierra Nevada so returns false for spread.
  */
  public boolean doSpread(SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

 /**
  * Western Spruce Budworm does not occur in Southern California so returns false for spread.
  */
  public boolean doSpread(SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

 /**
  * Western Spruce Budworm does not occur in Gila so returns false for spread.
  */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * Western Spruce Budworm does not occur in South Central Alaska so returns false for spread.
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  public String toString () {
    return printName;
  }

}

