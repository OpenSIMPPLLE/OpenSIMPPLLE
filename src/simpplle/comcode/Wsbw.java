package simpplle.comcode;

/** The University of Montana owns copyright of the designated documentation contained 
* within this file as part of the software product designated by Uniform Resource Identifier 
* UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
* Open Source License Contract pertaining to this documentation and agrees to abide by all 
* restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
*
* <p>This class provides a places fields and methods common to
* LightWsbw and SevereWsbw.  Additionally these two processes
* calculate probabilities a little different from the others
* making this class necessary.
* 
* @author Documentation by Brian Losi
* <p>Original source code authorship: Kirk A. Moeller
*  
*/

public abstract class Wsbw {
  private static boolean enabled=true;

  public static void setEnabled(boolean value) {
    enabled = value;
  }
  public static boolean isEnabled() {
    return enabled;
  }
  /**
   * Holds the probability of LightWsbw occurring.
   */
  public static int lightProb = -1;

  /**
   * Holds the probability of SevereWsbw occurring.
   */
  public static int severeProb = -1;

  /**
   * Calculate the probability of LightWsbw and SevereWsbw
   * occurring.
   * @param sIndex an int, holds the Susceptibility Index.
   */
  public static void doProbability (int sIndex) {
    if (sIndex == -1) {
      Wsbw.lightProb  = 0;
      Wsbw.severeProb = 0;
    }
    else if (sIndex > 0 && sIndex < 20) {
      Wsbw.lightProb  = sIndex;
      Wsbw.severeProb = 0;
    }
    else if (sIndex > 20 && sIndex < 50) {
      Wsbw.lightProb  = sIndex;
      Wsbw.severeProb = sIndex;
    }
    else if (sIndex > 50) {
      Wsbw.lightProb  = 0;
      Wsbw.severeProb = sIndex;
    }
    else {
      Wsbw.lightProb  = 0;
      Wsbw.severeProb = 0;
    }
  }

  /**
   * Calculate the Susceptibility Index for the WestsideRegionOne
   * zone in the Evu evu.
   * @param zone is WestsideRegionOne
   * @param evu is an Evu.
   */
  public static int computeSusceptibilityIndex (WestsideRegionOne zone,
                                                 Evu evu) {
    float index = 0.0f;

    if (oneOfHosts(zone,evu)) {
      index  = percentHostIndex(zone, evu);
      index *= percentClimateHostIndex(zone,evu);
      index *= densityIndex(zone,evu);
      index *= structureIndex(zone,evu);
      index *= standVigorIndex(zone,evu);
      index *= maturityIndex(zone,evu);
      index *= siteClimateIndex(zone,evu);
      index *= regionalClimateIndex(zone,evu);
      index *= characterOfAdjacentIndex(zone,evu);
    }
    else {
      return -1;
    }

    return Math.round(index);
  }

  /**
   * Calculate the Susceptibility Index for the EastsideRegionOne
   * zone in the Evu evu.
   * @param zone is a EastsideRegionOne
   * @param evu is an Evu.
   */
  public static int computeSusceptibilityIndex (EastsideRegionOne zone, Evu evu) {
    float index = 0.0f;

    if (oneOfHosts(zone,evu)) {
      index  = percentHostIndex(zone, evu);
      index *= percentClimateHostIndex(zone,evu);
      index *= densityIndex(zone,evu);
      index *= structureIndex(zone,evu);
      index *= standVigorIndex(zone,evu);
      index *= maturityIndex(zone,evu);
      index *= siteClimateIndex(zone,evu);
      index *= regionalClimateIndex(zone,evu);
      index *= characterOfAdjacentIndex(zone,evu);
    }
    else {
      return -1;
    }

    return Math.round(index);
  }
  public static int computeSusceptibilityIndex (Teton zone, Evu evu) {
    float index = 0.0f;

    if (oneOfHosts(zone,evu)) {
      index  = percentHostIndex(zone, evu);
      index *= percentClimateHostIndex(zone,evu);
      index *= densityIndex(zone,evu);
      index *= structureIndex(zone,evu);
      index *= standVigorIndex(zone,evu);
      index *= maturityIndex(zone,evu);
      index *= siteClimateIndex(zone,evu);
      index *= regionalClimateIndex(zone,evu);
      index *= characterOfAdjacentIndex(zone,evu);
    }
    else {
      return -1;
    }

    return Math.round(index);
  }
  public static int computeSusceptibilityIndex (NorthernCentralRockies zone, Evu evu) {
    float index = 0.0f;

    if (oneOfHosts(zone,evu)) {
      index  = percentHostIndex(zone, evu);
      index *= percentClimateHostIndex(zone,evu);
      index *= densityIndex(zone,evu);
      index *= structureIndex(zone,evu);
      index *= standVigorIndex(zone,evu);
      index *= maturityIndex(zone,evu);
      index *= siteClimateIndex(zone,evu);
      index *= regionalClimateIndex(zone,evu);
      index *= characterOfAdjacentIndex(zone,evu);
    }
    else {
      return -1;
    }

    return Math.round(index);
  }

  private static boolean oneOfHosts(WestsideRegionOne zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);

    if (species == Species.DF         ||
        species == Species.AF         ||
        species == Species.ES         ||
        species == Species.GF         ||
        species == Species.L_DF_AF    ||
        species == Species.L_DF_GF    ||
        species == Species.DF_LP_AF   ||
        species == Species.ES_AF      ||
        species == Species.DF_AF      ||
        species == Species.PP_DF      ||
        species == Species.L_DF       ||
        species == Species.DF_LP      ||
        species == Species.L_DF_PP) {
      return true;
    }
    else {
      return false;
    }
  }

  private static boolean oneOfHosts(EastsideRegionOne zone, Evu evu) {
    return oneOfHostsEast(evu);
  }
  private static boolean oneOfHosts(Teton zone, Evu evu) {
    return oneOfHostsEast(evu);
  }
  private static boolean oneOfHosts(NorthernCentralRockies zone, Evu evu) {
    return oneOfHostsEast(evu);
  }
  private static boolean oneOfHostsEast(Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);

    if (species == Species.DF        ||
        species == Species.AF        ||
        species == Species.ES        ||
        species == Species.ES_AF     ||
        species == Species.ES_LP     ||
        species == Species.DF_AF     ||
        species == Species.DF_ES     ||
        species == Species.PP_DF     ||
        species == Species.DF_LP     ||
        species == Species.LP_AF     ||
        species == Species.DF_PP_LP  ||
        species == Species.DF_LP_ES  ||
        species == Species.AF_ES_LP  ||
        species == Species.DF_LP_AF) {
      return true;
    }
    else {
      return false;
    }
  }

  private static float percentHostIndex(WestsideRegionOne zone, Evu evu) {
    float   index = 0.0f;
    Species species = (Species)evu.getState(SimpplleType.SPECIES);

    if (species == Species.DF       || species == Species.AF       ||
        species == Species.ES       || species == Species.GF       ||
        species == Species.ES_AF    || species == Species.DF_AF    ||
        species == Species.L_DF_AF  || species == Species.L_DF_GF  ||
        species == Species.DF_LP_AF || species == Species.DF_LP_AF) {
      index = 2.4f;
    }
    else if (species == Species.PP_DF || species == Species.L_DF ||
             species == Species.DF_LP) {
      index = 2.0f;
    }
    else if (species == Species.L_DF_PP || species == Species.LATE_SERAL) {
      index = 2.0f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float percentHostIndex(EastsideRegionOne zone, Evu evu) {
    return percentHostIndexEast(evu);
  }
  private static float percentHostIndex(Teton zone, Evu evu) {
    return percentHostIndexEast(evu);
  }
  private static float percentHostIndex(NorthernCentralRockies zone, Evu evu) {
    return percentHostIndexEast(evu);
  }
  private static float percentHostIndexEast(Evu evu) {
    float   index   = 0.0f;
    Species species = (Species)evu.getState(SimpplleType.SPECIES);

    if (species == Species.DF    || species == Species.DF_ES    ||
        species == Species.DF_AF || species == Species.DF_LP_AF ||
        species == Species.DF_LP_ES) {
      index = 2.4f;
    }
    else if (species == Species.PP_DF || species == Species.DF_LP ||
             species == Species.LP_AF || species == Species.ES_LP) {
      index = 2.0f;
    }
    else if (species == Species.DF_PP_LP) {
      index = 1.0f;
    }
    else if (species == Species.AF    || species == Species.ES ||
             species == Species.ES_AF || species == Species.AF_ES_LP) {
      index = 0.2f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float percentClimateHostIndex(WestsideRegionOne zone,
                                               Evu evu) {
    float   index   = 0.0f;
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if ((species == Species.DF &&
         (groupType.equals(HabitatTypeGroupType.A2) ||
          groupType.equals(HabitatTypeGroupType.B2) ||
          groupType.equals(HabitatTypeGroupType.C1))) ||

        (species == Species.AF &&
         (groupType.equals(HabitatTypeGroupType.C1) ||
          groupType.equals(HabitatTypeGroupType.D3) ||
          groupType.equals(HabitatTypeGroupType.E2) ||
          groupType.equals(HabitatTypeGroupType.F1) ||
          groupType.equals(HabitatTypeGroupType.G1) ||
          groupType.equals(HabitatTypeGroupType.G2))) ||

        (species == Species.ES &&
         (groupType.equals(HabitatTypeGroupType.E2) ||
          groupType.equals(HabitatTypeGroupType.F1) ||
          groupType.equals(HabitatTypeGroupType.G2))) ||

        (species == Species.GF &&
         (groupType.equals(HabitatTypeGroupType.C1) ||
          groupType.equals(HabitatTypeGroupType.C2) ||
          groupType.equals(HabitatTypeGroupType.D3))) ||

        (species == Species.ES_AF &&
         (groupType.equals(HabitatTypeGroupType.E2) ||
          groupType.equals(HabitatTypeGroupType.F1) ||
          groupType.equals(HabitatTypeGroupType.G1))) ||

        (species == Species.L_DF_GF && groupType.equals(HabitatTypeGroupType.C1)) ||

        ((species == Species.DF_AF || species == Species.DF_LP_AF ||
          species == Species.L_DF_AF) && groupType.equals(HabitatTypeGroupType.C1))) {

      index = 2.4f;
    }
    else if ((species == Species.PP_DF || species == Species.L_DF ||
              species == Species.DF_LP) &&
             (groupType.equals(HabitatTypeGroupType.A2) ||
              groupType.equals(HabitatTypeGroupType.B2) ||
              groupType.equals(HabitatTypeGroupType.C1))) {
      index = 2.0f;
    }
    else if ((species == Species.L_DF_PP &&
              (groupType.equals(HabitatTypeGroupType.A2) ||
               groupType.equals(HabitatTypeGroupType.B2) ||
               groupType.equals(HabitatTypeGroupType.C1))) ||
             species == Species.LATE_SERAL) {
      index = 1.0f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float percentClimateHostIndex(EastsideRegionOne zone, Evu evu) {
    return percentClimateHostIndexEast(evu);
  }
  private static float percentClimateHostIndex(Teton zone, Evu evu) {
    return percentClimateHostIndexEast(evu);
  }
  private static float percentClimateHostIndex(NorthernCentralRockies zone, Evu evu) {
    return percentClimateHostIndexEast(evu);
  }
  private static float percentClimateHostIndexEast(Evu evu) {
    float                index     = 0.0f;
    Species              species   = (Species)evu.getState(SimpplleType.SPECIES);
    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if ((species == Species.DF &&
         (groupType.equals(HabitatTypeGroupType.A1) ||
          groupType.equals(HabitatTypeGroupType.A2) ||
          groupType.equals(HabitatTypeGroupType.B1) ||
          groupType.equals(HabitatTypeGroupType.B2) ||
          groupType.equals(HabitatTypeGroupType.B3) ||
          groupType.equals(HabitatTypeGroupType.C1) ||
          groupType.equals(HabitatTypeGroupType.C2))) ||

        ((species == Species.DF_AF || species == Species.DF_LP_AF) &&
         (groupType.equals(HabitatTypeGroupType.D3) ||
          groupType.equals(HabitatTypeGroupType.E2) ||
          groupType.equals(HabitatTypeGroupType.F1) ||
          groupType.equals(HabitatTypeGroupType.F2)))) {
      index = 2.4f;
    }
    else if ((species == Species.PP_DF || species == Species.DF_LP) &&
             (groupType.equals(HabitatTypeGroupType.A2) ||
              groupType.equals(HabitatTypeGroupType.B1) ||
              groupType.equals(HabitatTypeGroupType.B2) ||
              groupType.equals(HabitatTypeGroupType.B3) ||
              groupType.equals(HabitatTypeGroupType.C1))) {
      index = 2.0f;
    }
    else if (species == Species.DF_PP_LP &&
             (groupType.equals(HabitatTypeGroupType.A2) ||
              groupType.equals(HabitatTypeGroupType.B1) ||
              groupType.equals(HabitatTypeGroupType.B2) ||
              groupType.equals(HabitatTypeGroupType.B3) ||
              groupType.equals(HabitatTypeGroupType.C1) ||
              groupType.equals(HabitatTypeGroupType.C2))) {
      index = 1.0f;
    }
    else if ((species == Species.AF &&
             (groupType.equals(HabitatTypeGroupType.D2) ||
              groupType.equals(HabitatTypeGroupType.D3) ||
              groupType.equals(HabitatTypeGroupType.E2) ||
              groupType.equals(HabitatTypeGroupType.F1) ||
              groupType.equals(HabitatTypeGroupType.F2) ||
              groupType.equals(HabitatTypeGroupType.G1) ||
              groupType.equals(HabitatTypeGroupType.G2))) ||

            ((species == Species.ES_AF || species == Species.ES) &&
             (groupType.equals(HabitatTypeGroupType.E2) ||
              groupType.equals(HabitatTypeGroupType.F1) ||
              groupType.equals(HabitatTypeGroupType.F2)))) {
      index = 0.2f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float densityIndex(Evu evu) {
    float   index   = 0.0f;
    Density density = (Density)evu.getState(SimpplleType.DENSITY);

    if (density == Density.ONE) {
      index = 0.8f;
    }
    else if (density == Density.TWO || density == Density.THREE) {
      index = 1.1f;
    }
    else if (density == Density.FOUR) {
      index = 1.4f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float densityIndex(WestsideRegionOne zone, Evu evu) {
    return densityIndex(evu);
  }

  private static float densityIndex(EastsideRegionOne zone, Evu evu) {
    return densityIndex(evu);
  }
  private static float densityIndex(Teton zone, Evu evu) {
    return densityIndex(evu);
  }
  private static float densityIndex(NorthernCentralRockies zone, Evu evu) {
    return densityIndex(evu);
  }

  private static float structureIndex(RegionalZone zone, Evu evu) {
    float     index     = 0.0f;
    SizeClass sizeClass = (SizeClass)evu.getState(SimpplleType.SIZE_CLASS);

    if (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.LARGE ||
        sizeClass == SizeClass.VERY_LARGE) {
      index = 0.9f;
    }
    else if (sizeClass == SizeClass.MTS || sizeClass == SizeClass.LTS ||
             sizeClass == SizeClass.VLTS) {
      index = 1.5f;
    }
    else if (sizeClass == SizeClass.MMU || sizeClass == SizeClass.LMU ||
             sizeClass == SizeClass.VLMU) {
      index = 1.7f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float structureIndex(WestsideRegionOne zone, Evu evu) {
    return structureIndex((RegionalZone)zone,evu);
  }

  private static float structureIndex(EastsideRegionOne zone, Evu evu) {
    return structureIndex((RegionalZone)zone,evu);
  }
  private static float structureIndex(Teton zone, Evu evu) {
    return structureIndex((RegionalZone)zone,evu);
  }
  private static float structureIndex(NorthernCentralRockies zone, Evu evu) {
    return structureIndex((RegionalZone)zone,evu);
  }

  private static float standVigorIndex(RegionalZone zone, Evu evu) {
    float                index     = 0.0f;

    VegSimStateData state = evu.getState();
    if (state == null) { return index; }

    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();

    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if ((density == Density.FOUR || density == Density.THREE) &&
        sizeClass == SizeClass.LMU &&
        (groupType.equals(HabitatTypeGroupType.A1) ||
         groupType.equals(HabitatTypeGroupType.A2) ||
         groupType.equals(HabitatTypeGroupType.B1) ||
         groupType.equals(HabitatTypeGroupType.B2) ||
         groupType.equals(HabitatTypeGroupType.B3) ||
         groupType.equals(HabitatTypeGroupType.C1) ||
         groupType.equals(HabitatTypeGroupType.C2))) {
      index = 1.6f;
    }
    else if ((density == Density.ONE || density == Density.TWO) &&
             sizeClass == SizeClass.LMU &&
             (groupType.equals(HabitatTypeGroupType.A1) ||
              groupType.equals(HabitatTypeGroupType.A2) ||
              groupType.equals(HabitatTypeGroupType.B1) ||
              groupType.equals(HabitatTypeGroupType.B2) ||
              groupType.equals(HabitatTypeGroupType.B3) ||
              groupType.equals(HabitatTypeGroupType.C1) ||
              groupType.equals(HabitatTypeGroupType.C2))) {
      index = 1.3f;
    }
    else {
      index = 0.9f;
    }

    return index;
  }

  private static float standVigorIndex(WestsideRegionOne zone, Evu evu) {
    return standVigorIndex((RegionalZone)zone,evu);
  }

  private static float standVigorIndex(EastsideRegionOne zone, Evu evu) {
    return standVigorIndex((RegionalZone)zone,evu);
  }
  private static float standVigorIndex(Teton zone, Evu evu) {
    return standVigorIndex((RegionalZone)zone,evu);
  }
  private static float standVigorIndex(NorthernCentralRockies zone, Evu evu) {
    return standVigorIndex((RegionalZone)zone,evu);
  }

  private static float maturityIndex(RegionalZone zone, Evu evu) {
    float     index     = 0.0f;
    SizeClass sizeClass = (SizeClass)evu.getState(SimpplleType.SIZE_CLASS);

    if (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MTS   ||
        sizeClass == SizeClass.MMU    || sizeClass == SizeClass.LARGE ||
        sizeClass == SizeClass.LTS    || sizeClass == SizeClass.LMU) {
      index = 1.4f;
    }
    else if (sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLTS ||
             sizeClass == SizeClass.VLMU) {
      index = 1.7f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float maturityIndex(WestsideRegionOne zone, Evu evu) {
    return maturityIndex((RegionalZone)zone,evu);
  }
  private static float maturityIndex(EastsideRegionOne zone, Evu evu) {
    return maturityIndex((RegionalZone)zone,evu);
  }
  private static float maturityIndex(Teton zone, Evu evu) {
    return maturityIndex((RegionalZone)zone,evu);
  }
  private static float maturityIndex(NorthernCentralRockies zone, Evu evu) {
    return maturityIndex((RegionalZone)zone,evu);
  }

  private static float siteClimateIndex(WestsideRegionOne zone, Evu evu) {
    float                index;
    HabitatTypeGroupType groupType;

    groupType = evu.getHabitatTypeGroup().getType();

    if (groupType.equals(HabitatTypeGroupType.A1) ||
        groupType.equals(HabitatTypeGroupType.A2) ||
        groupType.equals(HabitatTypeGroupType.B1) ||
        groupType.equals(HabitatTypeGroupType.B2)) {
      index = 1.5f;
    }
    else if (groupType.equals(HabitatTypeGroupType.C1) ||
             groupType.equals(HabitatTypeGroupType.D3)) {
      index = 1.2f;
    }
    else if (groupType.equals(HabitatTypeGroupType.G1) ||
             groupType.equals(HabitatTypeGroupType.G2)) {
      index = 0.0f;
    }
    else if (groupType.equals(HabitatTypeGroupType.F1)) {
      index = 0.6f;
    }
    else if (groupType.equals(HabitatTypeGroupType.C2)) {
      index = 1.3f;
    }
    else if (groupType.equals(HabitatTypeGroupType.E2)) {
      index = 1.0f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float siteClimateIndex(EastsideRegionOne zone, Evu evu) {
    return siteClimateIndexEast(evu);
  }
  private static float siteClimateIndex(Teton zone, Evu evu) {
    return siteClimateIndexEast(evu);
  }
  private static float siteClimateIndex(NorthernCentralRockies zone, Evu evu) {
    return siteClimateIndexEast(evu);
  }
  private static float siteClimateIndexEast(Evu evu) {
    float                index = 0.0f;
    HabitatTypeGroupType groupType;

    groupType = evu.getHabitatTypeGroup().getType();

    if (groupType.equals(HabitatTypeGroupType.A1) ||
        groupType.equals(HabitatTypeGroupType.A2) ||
        groupType.equals(HabitatTypeGroupType.B1) ||
        groupType.equals(HabitatTypeGroupType.B2)) {
      index = 1.5f;
    }
    else if (groupType.equals(HabitatTypeGroupType.B3) ||
             groupType.equals(HabitatTypeGroupType.D3)) {
      index = 1.2f;
    }
    else if (groupType.equals(HabitatTypeGroupType.F2) ||
             groupType.equals(HabitatTypeGroupType.G2)) {
      index = 0.0f;
    }
    else if (groupType.equals(HabitatTypeGroupType.F1)) {
      index = 0.6f;
    }
    else if (groupType.equals(HabitatTypeGroupType.C1) ||
             groupType.equals(HabitatTypeGroupType.C2)) {
      index = 1.3f;
    }
    else if (groupType.equals(HabitatTypeGroupType.E2)) {
      index = 1.0f;
    }
    else {
      index = 0.0f;
    }

    return index;
  }

  private static float regionalClimateIndex() {
    return 1.2f;
  }

  private static float regionalClimateIndex(WestsideRegionOne zone, Evu evu) {
    return regionalClimateIndex();
  }

  private static float regionalClimateIndex(EastsideRegionOne zone, Evu evu) {
    return regionalClimateIndex();
  }
  private static float regionalClimateIndex(Teton zone, Evu evu) {
    return regionalClimateIndex();
  }
  private static float regionalClimateIndex(NorthernCentralRockies zone, Evu evu) {
    return regionalClimateIndex();
  }

  private static float characterOfAdjacentIndex(WestsideRegionOne zone,
                                                Evu evu) {
    int   immatureOneThird = 0;
    int   immatureOneHalf  = 0;
    int   immatureOverHalf = 0;
    int   matureOneThird   = 0;
    int   matureOneHalf    = 0;
    int   matureOverHalf   = 0;
    int   maximum          = 0;
    float index = 0.0f;

    AdjacentData[] adjacentData;
    Evu             adj;
    Species         species;
    SizeClass       sizeClass;

    adjacentData = evu.getAdjacentData();

    for(int i=0;i<adjacentData.length;i++) {
      adj       = adjacentData[i].evu;

      VegSimStateData state = adj.getState();
      if (state == null) { continue; }

      species   = state.getVeg().getSpecies();
      sizeClass = state.getVeg().getSizeClass();

      if ((species == Species.DF         || species == Species.AF       ||
           species == Species.ES         || species == Species.GF       ||
           species == Species.L_DF_AF    || species == Species.L_DF_GF  ||
           species == Species.DF_GF      || species == Species.DF_LP_AF ||
           species == Species.ES_AF      || species == Species.DF_AF    ||
           species == Species.DF_ES      || species == Species.DF_WP_GF ||
           species == Species.DF_RRWP_GF || species == Species.DF_PP_GF) &&
          (sizeClass == SizeClass.SS ||
           sizeClass == SizeClass.POLE   || sizeClass == SizeClass.PTS ||
           sizeClass == SizeClass.PMU    ||
           sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU ||
           sizeClass == SizeClass.MTS)) {
        immatureOverHalf += adj.getAcres();
      }
      else if ((species == Species.DF         || species == Species.AF       ||
                species == Species.ES         || species == Species.GF       ||
                species == Species.L_DF_AF    || species == Species.L_DF_GF  ||
                species == Species.DF_GF      ||
                species == Species.DF_LP_AF   || species == Species.ES_AF    ||
                species == Species.DF_AF      || species == Species.DF_WP_GF ||
                species == Species.DF_RRWP_GF || species == Species.DF_PP_GF) &&
               (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
                sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
                sizeClass == SizeClass.VLTS  || sizeClass == SizeClass.VLMU)) {
        matureOverHalf += adj.getAcres();
      }
      else if ((species == Species.PP_DF      || species == Species.L_DF ||
                species == Species.DF_LP      || species == Species.L_GF ||
                species == Species.LATE_SERAL) &&
               (sizeClass == SizeClass.SS ||
                sizeClass == SizeClass.POLE   || sizeClass == SizeClass.PTS ||
                sizeClass == SizeClass.PMU    ||
                sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MTS ||
                sizeClass == SizeClass.MMU)) {
        immatureOneHalf += adj.getAcres();
      }
      else if ((species == Species.PP_DF      || species == Species.L_DF ||
                species == Species.DF_LP      || species == Species.L_GF ||
                species == Species.LATE_SERAL) &&
               (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
                sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
                sizeClass == SizeClass.VLTS  || sizeClass == SizeClass.VLMU)) {
        matureOneHalf += adj.getAcres();
      }
      else if (species == Species.L_DF_PP &&
               (sizeClass == SizeClass.SS     || sizeClass == SizeClass.POLE ||
                sizeClass == SizeClass.PTS    || sizeClass == SizeClass.PMU  ||
                sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MTS  ||
                sizeClass == SizeClass.MMU)) {
        immatureOneThird += adj.getAcres();
      }
      else if (species == Species.L_DF_PP &&
               (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
                sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
                sizeClass == SizeClass.VLTS  || sizeClass == SizeClass.VLMU)) {
        matureOneThird += adj.getAcres();
      }
    }

    maximum = Math.max(maximum,immatureOneThird);
    maximum = Math.max(maximum,immatureOneHalf);
    maximum = Math.max(maximum,immatureOverHalf);
    maximum = Math.max(maximum,matureOneThird);
    maximum = Math.max(maximum,matureOneHalf);
    maximum = Math.max(maximum,matureOverHalf);

    if      (maximum == immatureOneThird) { index = 0.1f; }
    else if (maximum == immatureOneHalf)  { index = 0.2f; }
    else if (maximum == immatureOverHalf) { index = 0.5f; }
    else if (maximum == matureOneThird)   { index = 0.8f; }
    else if (maximum == matureOneHalf)    { index = 1.4f; }
    else { index = 1.7f; }

    return index;
  }
  private static float characterOfAdjacentIndex(EastsideRegionOne zone, Evu evu) {
    return characterOfAdjacentIndexEast(evu);
  }
  private static float characterOfAdjacentIndex(Teton zone, Evu evu) {
    return characterOfAdjacentIndexEast(evu);
  }
  private static float characterOfAdjacentIndex(NorthernCentralRockies zone, Evu evu) {
    return characterOfAdjacentIndexEast(evu);
  }

  private static float characterOfAdjacentIndexEast(Evu evu) {
    int   immatureOneThird = 0;
    int   immatureOneHalf  = 0;
    int   immatureOverHalf = 0;
    int   matureOneThird   = 0;
    int   matureOneHalf    = 0;
    int   matureOverHalf   = 0;
    int   maximum          = 0;
    float index = 0.0f;

    AdjacentData[] adjacentData;
    Evu            adj;
    Species        species;
    SizeClass      sizeClass;

    adjacentData = evu.getAdjacentData();

    for(int i=0;i<adjacentData.length;i++) {
      adj       = adjacentData[i].evu;

      VegSimStateData state = adj.getState();
      if (state == null) { continue; }

      species   = state.getVeg().getSpecies();
      sizeClass = state.getVeg().getSizeClass();

      if ((species == Species.DF       || species == Species.AF       ||
           species == Species.ES       || species == Species.DF_LP_AF ||
           species == Species.ES_AF    || species == Species.DF_AF    ||
           species == Species.AF_ES_LP || species == Species.DF_LP_ES ||
           species == Species.DF_ES) &&
          (sizeClass == SizeClass.SS   ||
           sizeClass == SizeClass.POLE || sizeClass == SizeClass.PTS    ||
           sizeClass == SizeClass.PMU  || sizeClass == SizeClass.MEDIUM ||
           sizeClass == SizeClass.MMU  || sizeClass == SizeClass.MTS)) {
        immatureOverHalf += adj.getAcres();
      }
      else if ((species == Species.DF       || species == Species.AF       ||
                species == Species.ES       || species == Species.DF_LP_AF ||
                species == Species.ES_AF    || species == Species.DF_AF    ||
                species == Species.AF_ES_LP || species == Species.DF_LP_ES ||
                species == Species.DF_ES) &&
               (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
                sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
                sizeClass == SizeClass.VLTS  || sizeClass == SizeClass.VLMU)) {
        matureOverHalf += adj.getAcres();
      }
      else if ((species == Species.PP_DF || species == Species.DF_LP ||
                species == Species.ES_LP || species == Species.LP_AF ||
                species == Species.DF_LP_ES) &&
               (sizeClass == SizeClass.SS     || sizeClass == SizeClass.POLE ||
                sizeClass == SizeClass.PTS    || sizeClass == SizeClass.PMU  ||
                sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MTS  ||
                sizeClass == SizeClass.MMU)) {
        immatureOneHalf += adj.getAcres();
      }
      else if ((species == Species.PP_DF || species == Species.DF_LP ||
                species == Species.ES_LP || species == Species.LP_AF ||
                species == Species.DF_LP_ES) &&
               (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
                sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
                sizeClass == SizeClass.VLTS  || sizeClass == SizeClass.VLMU)) {
        matureOneHalf += adj.getAcres();
      }
      else if (species == Species.DF_PP_LP &&
               (sizeClass == SizeClass.SS     || sizeClass == SizeClass.POLE ||
                sizeClass == SizeClass.PTS    || sizeClass == SizeClass.PMU  ||
                sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MTS  ||
                sizeClass == SizeClass.MMU)) {
        immatureOneThird += adj.getAcres();
      }
      else if (species == Species.DF_PP_LP &&
               (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LTS ||
                sizeClass == SizeClass.LMU   || sizeClass == SizeClass.VERY_LARGE ||
                sizeClass == SizeClass.VLTS  || sizeClass == SizeClass.VLMU)) {
        matureOneThird += adj.getAcres();
      }
      else {
      }

    }

    maximum = Math.max(maximum,immatureOneThird);
    maximum = Math.max(maximum,immatureOneHalf);
    maximum = Math.max(maximum,immatureOverHalf);
    maximum = Math.max(maximum,matureOneThird);
    maximum = Math.max(maximum,matureOneHalf);
    maximum = Math.max(maximum,matureOverHalf);

    if      (maximum == immatureOneThird) { index = 0.1f; }
    else if (maximum == immatureOneHalf)  { index = 0.2f; }
    else if (maximum == immatureOverHalf) { index = 0.5f; }
    else if (maximum == matureOneThird)   { index = 0.8f; }
    else if (maximum == matureOneHalf)    { index = 1.4f; }
    else { index = 1.7f; }

    return index;
  }

  /**
   * Determine whether a Process p will spread to Evu evu.
   * @param zone is a RegionalZone.
   * @param p is a Process, will be either LightWsbw or SevereWsbw.
   * @param evu is an Evu, the evu to try spread Process p to.
   * @return a boolean, true = process has spread to Evu evu.
   */
  public static boolean doSpread(RegionalZone zone, Process p, Evu evu) {
    int prob;

    if (p.getType().equals(ProcessType.LIGHT_WSBW)) {
      prob = 7500;
    }
    else {
      prob = 5000;
    }

    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         evuProb     = state.getProb();
    ProcessType processType = state.getProcess();

    if (evuProb != Evu.L &&
        processType.equals(ProcessType.SUCCESSION) &&
        (evu.getProcessProb(ProcessType.SEVERE_WSBW) >= prob ||
         evu.getProcessProb(ProcessType.LIGHT_WSBW)  >= prob)) {
      evu.updateCurrentProcess(p.getType());
      evu.updateCurrentProb(Evu.S);

      return true;
    }
    else {
      return false;
    }
  }

}




