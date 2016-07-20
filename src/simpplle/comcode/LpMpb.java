/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class serves to provide a place for fields and methods that are common to LightLpMpb and SevereLpMpb.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 * @see simpplle.comcode.LightLpMpb
 * @see simpplle.comcode.SevereLpMpb
 */

public abstract class LpMpb implements HazardValues {
/*
    // Low Hazard
    {{1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {10,0}, {15,0}},

    // Moderate Hazard
    {{5,5}, {6,6}, {5,5}, {6,6}, {15,10}, {20,15}, {25,20}, {30,25}},

    // High Hazard
    {{5,0}, {10,5}, {15,10}, {20,15}, {30,20}, {40,30}, {50,40}, {60,50},
     {70,60}, {75,60}, {85,70}, {95,80}}};
*/
  /**
   * Holds the calculated probability for LightLpMpb.
   */
  public static int lightProb = -1;

  /**
   * Holds the calculated probability for SevereLpMpb.
   */
  public static int severeProb = -1;

  public static int getProbData(int page, int row, int col) {
    // Prob data is not related to LIGHT/SEVERE.  The data
    // had to be stored in one of them, I simply chose light.
    Process p = Process.findInstance(ProcessType.LIGHT_LP_MPB);
    return p.getProbData(page,row,col);
  }

  public static void computeHazard(RegionalZone zone, Evu evu) {
    switch (zone.getId()) {
      case ValidZones.COLORADO_FRONT_RANGE:
      case ValidZones.COLORADO_PLATEAU:
        computeHazardColorado(zone,evu);
        break;
      default:
        computeHazardCommon(zone,evu);
        break;
    }
  }

  /**
   * Calculates the Hazard of LightLpMpb and SevereLpMpb.  
   * The first if block is used when lodge pole is the dominant species or occurs with one other species,
   * the else if block when lodgepole pine is a minor component occurrring with two other species.  
   * @param zone is a RegionalZone.
   * @param evu is an Evu, the Evu to calculate the hazard for.
   */
  public static void computeHazardCommon(RegionalZone zone, Evu evu) {
    Species              species;
    SizeClass            sizeClass;
    Density              density;
    HabitatTypeGroupType groupType;
    MtnPineBeetleHazard.Hazard   hazard = null;

    int tStep = Simulation.getCurrentTimeStep();

    if (Area.multipleLifeformsEnabled() &&
        evu.hasLifeform(Lifeform.TREES,tStep) == false) {
      evu.setLpMpbHazard(hazard);
      return;
    }

    VegetativeType state;
    if (Area.multipleLifeformsEnabled()) {
      state = evu.getState(tStep, Lifeform.TREES).getVeg();
    }
    else {
      state = evu.getState(tStep).getVeg();
    }
    if (state == null) { return; }

    species   = state.getSpecies();
    sizeClass = state.getSizeClass();
    density   = state.getDensity();

    groupType = evu.getHabitatTypeGroup().getType();

    if ((species == Species.LP    || species == Species.DF_LP ||
         species == Species.LP_AF || species == Species.ES_LP) &&
        (sizeClass != SizeClass.DEAD && sizeClass != SizeClass.POLE &&
         sizeClass != SizeClass.PMU  && sizeClass != SizeClass.PTS  &&
         sizeClass != SizeClass.SS)) {

      if (groupType.equals(HabitatTypeGroupType.G2) ||
          groupType.equals(HabitatTypeGroupType.G1)) {
        hazard = MtnPineBeetleHazard.Hazard.LOW;
      }
      else if ((groupType.equals(HabitatTypeGroupType.A2) ||
                groupType.equals(HabitatTypeGroupType.B2)) &&
               (density == Density.THREE  || density == Density.FOUR)) {
        hazard = MtnPineBeetleHazard.Hazard.HIGH;
      }
      else if ((groupType.equals(HabitatTypeGroupType.A2) ||
                groupType.equals(HabitatTypeGroupType.B2)) &&
               density == Density.TWO) {
        hazard = MtnPineBeetleHazard.Hazard.MODERATE;
      }
      else if ((groupType.equals(HabitatTypeGroupType.B3) ||
                groupType.equals(HabitatTypeGroupType.C2) ||
                groupType.equals(HabitatTypeGroupType.D3) ||
                groupType.equals(HabitatTypeGroupType.E2) ||
                groupType.equals(HabitatTypeGroupType.C1) ||
                groupType.equals(HabitatTypeGroupType.F1)) &&
               (density == Density.THREE || density == Density.FOUR)) {
        hazard = MtnPineBeetleHazard.Hazard.HIGH;
      }
      else if ((groupType.equals(HabitatTypeGroupType.B3) ||
                groupType.equals(HabitatTypeGroupType.C2) ||
                groupType.equals(HabitatTypeGroupType.D3) ||
                groupType.equals(HabitatTypeGroupType.E2) ||
                groupType.equals(HabitatTypeGroupType.C1) ||
                groupType.equals(HabitatTypeGroupType.F1)) &&
               density == Density.TWO) {
        hazard = MtnPineBeetleHazard.Hazard.MODERATE;
      }
    }
    else if ((species == Species.DF_LP_AF || species == Species.DF_PP_LP ||
              species == Species.DF_LP_ES || species == Species.WB_ES_LP ||
              species == Species.AF_ES_LP) &&
             (sizeClass != SizeClass.DEAD && sizeClass != SizeClass.POLE &&
              sizeClass != SizeClass.PMU  && sizeClass != SizeClass.PTS  &&
              sizeClass != SizeClass.SS)) {

      if (groupType.equals(HabitatTypeGroupType.G2) ||
          groupType.equals(HabitatTypeGroupType.G1)) {
        hazard = MtnPineBeetleHazard.Hazard.LOW;
      }
      else if ((groupType.equals(HabitatTypeGroupType.A2) ||
                groupType.equals(HabitatTypeGroupType.B2)) &&
               (density == Density.THREE || density == Density.FOUR)) {
        hazard = MtnPineBeetleHazard.Hazard.MODERATE;
      }
      else if ((groupType.equals(HabitatTypeGroupType.A2) ||
                groupType.equals(HabitatTypeGroupType.B2)) &&
               density == Density.TWO) {
        hazard = MtnPineBeetleHazard.Hazard.LOW;
      }
      else if ((groupType.equals(HabitatTypeGroupType.B3) ||
                groupType.equals(HabitatTypeGroupType.C2) ||
                groupType.equals(HabitatTypeGroupType.D3) ||
                groupType.equals(HabitatTypeGroupType.E2) ||
                groupType.equals(HabitatTypeGroupType.C1) ||
                groupType.equals(HabitatTypeGroupType.F1)) &&
               (density == Density.THREE || density == Density.FOUR)) {
        hazard = MtnPineBeetleHazard.Hazard.MODERATE;
      }
      else if ((groupType.equals(HabitatTypeGroupType.B3) ||
                groupType.equals(HabitatTypeGroupType.C2) ||
                groupType.equals(HabitatTypeGroupType.D3) ||
                groupType.equals(HabitatTypeGroupType.E2) ||
                groupType.equals(HabitatTypeGroupType.C1) ||
                groupType.equals(HabitatTypeGroupType.F1)) &&
               density == Density.TWO) {
        hazard = MtnPineBeetleHazard.Hazard.LOW;
      }
    }
    else {
      hazard = null;
    }

    evu.setLpMpbHazard(hazard);
  }
/**
 * Computes the LpMpbHazard for Colorado region.  
 * @param zone
 * @param evu
 */
  public static void computeHazardColorado(RegionalZone zone, Evu evu) {
    MtnPineBeetleHazard.Hazard hazard = null;

    int tStep = Simulation.getCurrentTimeStep();

    if (Area.multipleLifeformsEnabled() &&
        evu.hasLifeform(Lifeform.TREES,tStep) == false) {
      evu.setLpMpbHazard(hazard);
      return;
    }

    VegetativeType state;
    if (Area.multipleLifeformsEnabled()) {
      state = evu.getState(tStep, Lifeform.TREES).getVeg();
    }
    else {
      state = evu.getState(tStep).getVeg();
    }
    if (state == null) { return; }

    Species species   = state.getSpecies();
    SizeClass sizeClass = state.getSizeClass();
    Density density   = state.getDensity();

    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if ((species == Species.PICO ||
         species == Species.PICO_ABLA ||
         species == Species.PICO_PIEN ||
         species == Species.PICO_PIFL2 ||
         species == Species.PICO_POTR5 ||
         species == Species.PICO_PSME ||
         species == Species.ABLA_PICO ||
         species == Species.PIAR_PICO ||
         species == Species.PIEN_PICO ||
         species == Species.PIFL2_PICO ||
         species == Species.PIPO_PICO ||
         species == Species.POTR5_PICO ||
         species == Species.PSME_PICO) &&
        (sizeClass == SizeClass.E || sizeClass == SizeClass.SS ||
         sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU)) {

      if (groupType == HabitatTypeGroupType.ALPINE) {
        hazard = MtnPineBeetleHazard.Hazard.LOW;
      }
      else if (groupType == HabitatTypeGroupType.LOWER_MONTANE ||
               groupType == HabitatTypeGroupType.FOOTHILLS ||
               groupType == HabitatTypeGroupType.UPPER_MONTANE) {
        if (density == Density.ONE || density == Density.TWO) {
          hazard = MtnPineBeetleHazard.Hazard.MODERATE;
        }
        else if (density == Density.THREE || density == Density.FOUR) {
          hazard = MtnPineBeetleHazard.Hazard.HIGH;
        }
      }
    }

    evu.setLpMpbHazard(hazard);
  }
  /**
   * Determine the probability of the Evu getting LightLpMpb
   * and SevereLpMpb once the hazard has been calculated.  
   * @param zone is a RegionalZone.
   * @param evu is an Evu, the Evu to calculate probabilities for.
   */
  public static void adjust(RegionalZone zone, Evu evu) {
    Area           area = Simpplle.currentArea;
    AdjacentData[] adjacentData;
    Evu            adj;
    boolean        lowHazard, modHazard, highHazard;
    MtnPineBeetleHazard.Hazard unitHazard, adjHazard;
    int            adjLowCount = 0, adjModerateCount = 0, adjHighCount = 0;
    int            adjProcessCount;
    ProcessType    adjProcessType;
    boolean        lightLpMpb;
    int            ppMpb = 0, lightLpMpbCount = 0, severeLpMpb = 0;
    int            standReplacingFire = 0, mixedSeverityFire = 0;
    int            page = -1, row = -1, col = 0;
    int            cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    unitHazard  = evu.getLpMpbHazard();
    lowHazard   = (unitHazard == MtnPineBeetleHazard.Hazard.LOW);
    modHazard   = (unitHazard == MtnPineBeetleHazard.Hazard.MODERATE);
    highHazard  = (unitHazard == MtnPineBeetleHazard.Hazard.HIGH);

    VegSimStateData state = evu.getState(cTime-1);
    lightLpMpb   = (state != null ? state.getProcess().equals(ProcessType.LIGHT_LP_MPB) : false);
    adjacentData = evu.getAdjacentData();

    for(int i=0;i<adjacentData.length;i++) {
      adj            = adjacentData[i].evu;
      adjHazard      = adj.getLpMpbHazard();

      VegSimStateData adjState = evu.getState(cTime-1);
      if (adjState == null) { continue; }
      adjProcessType = adjState.getProcess();

      switch (adjHazard) {
        case LOW:
          adjLowCount++;
          break;
        case MODERATE:
          adjModerateCount++;
          break;
        case HIGH:
          adjHighCount++;
          break;
      }

      if (adjProcessType.equals(ProcessType.PP_MPB)) {
        ppMpb = 1;
      }
      else if (adjProcessType.equals(ProcessType.LIGHT_LP_MPB)) {
        lightLpMpbCount = 1;
      }
      else if (adjProcessType.equals(ProcessType.SEVERE_LP_MPB)) {
        severeLpMpb = 1;
      }
      else if (adjProcessType.equals(ProcessType.STAND_REPLACING_FIRE)) {
        standReplacingFire = 1;
      }
      else if (adjProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
        mixedSeverityFire = 1;
      }
    }
    adjProcessCount  = ppMpb + lightLpMpbCount + severeLpMpb;
    adjProcessCount += standReplacingFire + mixedSeverityFire;

    // ** combinations for low existing hazard **
    // **
    if (!lightLpMpb && lowHazard && (adjProcessCount == 0) &&
        (adjModerateCount == 0) && (adjHighCount == 0)) {
      row = 0;
    }
    else if (!lightLpMpb && lowHazard && (adjProcessCount == 0) &&
             (adjModerateCount > 0 || adjHighCount > 0)) {
      row = 1;
    }
    else if (!lightLpMpb && lowHazard && (adjProcessCount > 0) &&
             adjModerateCount == 0 && adjHighCount == 0) {
      row = 2;
    }
    else if (!lightLpMpb && lowHazard && (adjProcessCount > 0) &&
             (adjModerateCount > 0 || adjHighCount > 0)) {
      row = 3;
    }
     else if (lightLpMpb && lowHazard && (adjProcessCount == 0) &&
             adjModerateCount == 0 && adjHighCount == 0) {
      row = 4;
    }
    else if (lightLpMpb && lowHazard && (adjProcessCount == 0) &&
             (adjModerateCount > 0 || adjHighCount > 0)) {
      row = 5;
    }
    else if (lightLpMpb && lowHazard && (adjProcessCount > 0) &&
             adjModerateCount == 0 && adjHighCount == 0) {
      row = 6;
    }
    else if (lightLpMpb && lowHazard && (adjProcessCount > 0) &&
             (adjModerateCount > 0 || adjHighCount > 0)) {
      row = 7;
    }
    // ** combinations for mod existing hazard **
    // **
    else if (!lightLpMpb && modHazard && (adjProcessCount == 0) &&
           (adjHighCount == 0)) {
      row = 0;
    }
    else if (!lightLpMpb && modHazard && (adjProcessCount == 0) &&
             (adjHighCount > 0)) {
      row = 1;
    }

    else if (!lightLpMpb && modHazard && (adjProcessCount > 0) &&
             (adjHighCount == 0)) {
      row = 2;
    }
    else if (!lightLpMpb && modHazard && (adjProcessCount > 0) &&
             (adjHighCount > 0)) {
      row = 3;
    }
    else if (lightLpMpb && modHazard && (adjProcessCount == 0) &&
             (adjHighCount == 0)) {
      row = 4;
    }
    else if (lightLpMpb && modHazard && (adjProcessCount == 0) &&
             (adjHighCount > 0)) {
      row = 5;
    }
    else if (lightLpMpb && modHazard && (adjProcessCount > 0) &&
             (adjHighCount == 0)) {
      row = 6;
    }
    else if (lightLpMpb && modHazard && (adjProcessCount > 0) &&
             (adjHighCount > 0)) {
      row = 7;
    }

    // ** combinations for existing hazard of high **
    // **
    else if (!lightLpMpb && highHazard && (adjProcessCount == 0) &&
             (adjModerateCount == 0) && (adjHighCount == 0)) {
      row = 0;
    }
    else if (!lightLpMpb && highHazard && (adjProcessCount == 0) &&
             (adjModerateCount > 0) && (adjHighCount == 0)) {
      row = 1;
    }
    else if (!lightLpMpb && highHazard && (adjProcessCount == 0) &&
             (adjHighCount > 0)) {
      row = 2;
    }
    else if (!lightLpMpb && highHazard && (adjProcessCount > 0) &&
             (adjModerateCount == 0) && (adjHighCount == 0)) {
      row = 3;
    }
    else if (!lightLpMpb && highHazard && (adjProcessCount > 0) &&
             (adjModerateCount > 0) && (adjHighCount == 0)) {
      row = 4;
    }
    else if (!lightLpMpb && highHazard && (adjProcessCount > 0) &&
             (adjHighCount > 0)) {
      row = 5;
    }
    else if (lightLpMpb && highHazard && (adjProcessCount == 0) &&
             (adjModerateCount == 0) && (adjHighCount == 0)) {
      row = 6;
    }
    else if (lightLpMpb && highHazard && (adjProcessCount == 0) &&
             (adjModerateCount > 0) && (adjHighCount == 0)) {
      row = 7;
    }
    else if (lightLpMpb && highHazard && (adjProcessCount == 0) &&
             (adjHighCount > 0)) {
      row = 8;
    }
    else if (lightLpMpb && highHazard && (adjProcessCount > 0) &&
             (adjModerateCount == 0) && (adjHighCount == 0)) {
      row = 9;
    }
    else if (lightLpMpb && highHazard && (adjProcessCount > 0) &&
             (adjModerateCount > 0) && (adjHighCount == 0)) {
      row = 10;
    }
    else if (lightLpMpb && highHazard && (adjProcessCount > 0) &&
             (adjHighCount > 0)) {
      row = 11;
    }
    else { row = -1; }

    if (row != -1) {
      if      (lowHazard)  { page = 0; }
      else if (modHazard)  { page = 1; }
      else if (highHazard) { page = 2; }
      else { page = -1;}
    }

    if (page != -1 && row != -1) {
      lightProb  = getProbData(page,row,0);
      severeProb = getProbData(page,row,1);
    }
    else {
      lightProb  = 0;
      severeProb = 0;
    }
  }

  /**
   * Calculate probabilities for the WestsideRegionOne zone.
   * @param zone is a WestsideRegionOne.
   * @param Evu is an evu, the Evu to calculate probabilities for.
   */
  public static void adjust(WestsideRegionOne zone, Evu evu) {
    adjust((RegionalZone)zone,evu);
  }

  /**
   * Calculate probabilities for the EastsideRegionOne zone.
   * @param zone is a EastsideRegionOne.
   * @param Evu is an evu, the Evu to calculate probabilities for.
   */
  public static void adjust(EastsideRegionOne zone, Evu evu) {
    adjust((RegionalZone)zone,evu);
  }
  public static void adjust(Teton zone, Evu evu) {
    adjust((RegionalZone)zone,evu);
  }
  public static void adjust(NorthernCentralRockies zone, Evu evu) {
    adjust((RegionalZone)zone,evu);
  }

  /**
   * Determines whether spreading of Process p to Evu evu occurs.
   * @param zone is a RegionalZone.
   * @param evu is an Evu, the unit to spread a process to.
   */
  public static boolean doSpread(RegionalZone zone, Evu evu) {
    switch (zone.getId()) {
      default:
        return doSpreadCommon(zone,evu);
    }
  }
/**
 * Does the spreading calculation common for zones.  
 * @param zone
 * @param evu
 * @return
 */
  public static boolean doSpreadCommon(RegionalZone zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();
    Species     species   = state.getVeg().getSpecies();
    SizeClass   sizeClass = state.getVeg().getSizeClass();

    if (prob != Evu.L &&
        processType.equals(ProcessType.SUCCESSION) &&
        (species == Species.LP       || species == Species.DF_LP    ||
         species == Species.ES_LP    || species == Species.LP_AF    ||
         species == Species.DF_PP_LP || species == Species.DF_LP_ES ||
         species == Species.DF_LP_AF || species == Species.AF_ES_LP ||
         species == Species.WB_ES_LP) &&
        (sizeClass == SizeClass. MEDIUM     || sizeClass == SizeClass. MTS   ||
         sizeClass == SizeClass. MMU        || sizeClass == SizeClass. LARGE ||
         sizeClass == SizeClass. LTS        || sizeClass == SizeClass. LMU   ||
         sizeClass == SizeClass. VERY_LARGE || sizeClass == SizeClass. VLTS  ||
         sizeClass == SizeClass. VLMU)) {
      return true;
    }
    return false;
  }

}





