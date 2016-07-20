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
 * <p>This class contains methods for Light Western Spruce Budworm, a type of Process. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 * 
 */


public class LightWsbw extends Process {
  private static final String printName = "LIGHT-WSBW_OLD";
  /**
   * Constructor for  Light Western Spruce Budworm.  Inherits from Process superclass and intializes spreading to true and description. 
   */
  public LightWsbw () {
    super();

    spreading   = true;
    description = "Western Spruce Budworm";
  }

  private int doProbabilityCommon() {
    int prob = Wsbw.lightProb;

    Wsbw.lightProb = -1;

    return prob;
  }

  public int doProbability (WestsideRegionOne zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int sIndex;

    if (Wsbw.lightProb == -1) {
      sIndex = Wsbw.computeSusceptibilityIndex(zone,evu);
      Wsbw.doProbability(sIndex);
    }
    return doProbabilityCommon();
  }
/**
 * Probability of Light Western Spruce Budworm for Teton zone.  if no species return 0, else calculates light and sever probability based on presence of host vegetative species.  
 * 
 */
  public int doProbability (Teton zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int     sIndex;

    if (Wsbw.lightProb == -1) {
      Species species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species == null) { return 0; }

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
   * Probability of Light Western Spruce Budworm for Northern Central Rockies.  If none present returns 0, else 
   * returns light and sever probability based on presence of host vegetative species.  
   */
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int     sIndex;

    if (Wsbw.lightProb == -1) {
      Species species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species == null) { return 0; }

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
   * Probability of Light Western Spruce Budworm for Eastside Region 1.  If no Wsbw present returns 0, else 
   * returns light and sever probability based on presence of host vegetative species.  
   */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    if (Wsbw.isEnabled()==false) { return 0; }

    int     sIndex;

    if (Wsbw.lightProb == -1) {
      Species species = (Species)evu.getState(SimpplleType.SPECIES);
      if (species == null) { return 0; }

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
   * Sierra Nevada does not have this process so returns false by default.
   */
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Southern California does not have this process so returns false by default.
   */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }

 /**
  * Gila does not have this process so returns false by default.
  */
  public boolean doSpread (Gila               zone, Evu fromEvu, Evu evu) { return false; }
  /**
   * South Central Alaska does not have this process so returns false by default.
   */
  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  public String toString () {
    return printName;
  }

}

