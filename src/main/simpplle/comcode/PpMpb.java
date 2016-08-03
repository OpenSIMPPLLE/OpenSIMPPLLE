/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

/**
 * This class defines methods for Mountain Pine Beetle in Ponderosa Pine, a type of Process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class PpMpb extends Process implements HazardValues {
  private static final String printName = "PP-MPB";
  private static int probability = 0;
/**
 * Constructor for Ponderosa Pine Mountain Pine Beetle.  Inherits from Process superclass and initializes
 * spreading to ture, and sets default visible columns for process, adjacent process, mountain pine beetle hazard,
 * adjacent high and moderate hazards, and probability 
 */
  public PpMpb () {
    super();

    spreading   = true;
    description = "Ponderosa Pine Mountain Pine Beetle";

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(BaseLogic.Columns.PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_PROCESS_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.MPB_HAZARD_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_HIGH_HAZARD_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.ADJ_MOD_HAZARD_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  private int doProbabilityCommon () {
    return probability;
  }

  public int doProbability (WestsideRegionOne zone, Evu evu) {
    adjustHazard(zone,evu);

    return doProbabilityCommon();
  }
/**
 * For Eastside Region if species is not a variety of ponderosa pine the probability is 0, else does probability common
 */
  public int doProbability (EastsideRegionOne zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.PP       && species != Species.PP_DF &&
        species != Species.L_PP     && species != Species.L_PP_LP &&
        species != Species.DF_PP_LP && species != Species.L_DF_PP) {
      probability = 0;
    }
    else {
      adjustHazard(zone,evu);
    }
    return doProbabilityCommon();
  }
  /**
   * Probability for Teton zone, if species is not a variety of ponderosa pine the probability is 0, else does probability common
   */
  public int doProbability (Teton zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.PP       && species != Species.PP_DF &&
        species != Species.L_PP     && species != Species.L_PP_LP &&
        species != Species.DF_PP_LP && species != Species.L_DF_PP) {
      probability = 0;
    }
    else {
      adjustHazard(zone,evu);
    }
    return doProbabilityCommon();
  }
  /**
   * Probability for Northern Central Rockies if species is not a variety of ponderosa pine the probability is 0, else does probability common
   */
  public int doProbability (NorthernCentralRockies zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return 0; }

    if (species != Species.PP       && species != Species.PP_DF &&
        species != Species.L_PP     && species != Species.L_PP_LP &&
        species != Species.DF_PP_LP && species != Species.L_DF_PP) {
      probability = 0;
    }
    else {
      adjustHazard(zone,evu);
    }
    return doProbabilityCommon();
  }

  public int doProbability (ColoradoFrontRange zone, Evu evu) {
    adjustHazard(zone,evu);

    return doProbabilityCommon();
  }
  public int doProbability (ColoradoPlateau zone, Evu evu) {
    adjustHazard(zone,evu);

    return doProbabilityCommon();
  }
/**
 * Computes the Hazard for Colorado front range and colorado plateau
 * @param zone regional zone 
 * @param evu existing vegetative unit
 */
  public static void computeHazard(RegionalZone zone, Evu evu) {
    switch (zone.getId()) {
      case ValidZones.COLORADO_FRONT_RANGE:
        computeHazardColorado(zone, evu);
        break;
      case ValidZones.COLORADO_PLATEAU:
        computeHazardColorado(zone, evu);
        break;
      default:
        computeHazardCommon(zone, evu);
    }
  }

  /**
   * computes the hazard of Ponderosa pine mountain pine beetle in an area.  This is based on species, size class, moisture, time step, and life froms. 
   * @param zone
   * @param evu
   */
  public static void computeHazardCommon(RegionalZone zone, Evu evu) {
    Species   species;
    SizeClass sizeClass;
    Density   density;
    MtnPineBeetleHazard.Hazard hazard=null;

    int tStep = Simulation.getCurrentTimeStep();

    if (Area.multipleLifeformsEnabled() &&
        evu.hasLifeform(Lifeform.TREES,tStep) == false) {
      evu.setPpMpbHazard(hazard);
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

    if (sizeClass == SizeClass.SS || sizeClass == SizeClass.POLE) {
      hazard = null;
    }
    // for low hazard because assume if pp occurs in l-df-pp,
    // it will never have enough ba to come our with a high hazard.
    //
    else if (species == Species.L_DF_PP) {
      hazard = MtnPineBeetleHazard.LOW;
    }
    // for low hazard with other species mixtures.
    else if ((species == Species.PP   || species == Species.PP_DF ||
              species == Species.L_PP || species == Species.L_PP_LP) &&
             (sizeClass == SizeClass.PMU  || sizeClass == SizeClass.PTS ||
              sizeClass == SizeClass.LMU  || sizeClass == SizeClass.MMU ||
              sizeClass == SizeClass.VLMU || sizeClass == SizeClass.LTS ||
              sizeClass == SizeClass.MTS  || sizeClass == SizeClass.VLTS) &&
             (density == Density.TWO || density == Density.THREE)) {
      hazard = MtnPineBeetleHazard.LOW;
    }
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.POLE || sizeClass == SizeClass.MEDIUM) &&
             density == Density.TWO) {
      hazard = MtnPineBeetleHazard.LOW;
    }
    // for moderate hazard rating.
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.POLE || sizeClass == SizeClass.MEDIUM) &&
             density == Density.THREE) {
      hazard = MtnPineBeetleHazard.MODERATE;
    }
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.PMU || sizeClass == SizeClass.PTS ||
              sizeClass == SizeClass.MMU || sizeClass == SizeClass.MTS) &&
             density == Density.FOUR) {
      hazard = MtnPineBeetleHazard.MODERATE;
    }
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.VERY_LARGE) &&
             density == Density.TWO) {
      hazard = MtnPineBeetleHazard.MODERATE;
    }
    //  for high hazard rating
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.LMU || sizeClass == SizeClass.VLMU ||
              sizeClass == SizeClass.PTS || sizeClass == SizeClass.VLTS) &&
             density == Density.FOUR) {
      hazard = MtnPineBeetleHazard.HIGH;
    }
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.VERY_LARGE) &&
             density == Density.FOUR) {
      hazard = MtnPineBeetleHazard.HIGH;
    }
    else if ((species == Species.PP   || species == Species.PP_DF   ||
              species == Species.L_PP || species == Species.L_PP_LP ||
              species == Species.DF_PP_LP) &&
             (sizeClass == SizeClass.POLE || sizeClass == SizeClass.MEDIUM) &&
             density == Density.FOUR) {
      hazard = MtnPineBeetleHazard.HIGH;
    }
    else {
      hazard = null;
    }

    evu.setPpMpbHazard(hazard);
  }
  /**
   * Method to compute hazard in Colorado.  This is based on species combinations, size class, density, vegetative state, and life form
   * @param zone Regional zone being evaluated
   * @param evu
   */
  public static void computeHazardColorado(RegionalZone zone, Evu evu) {
    Species   species;
    SizeClass sizeClass;
    Density   density;
    MtnPineBeetleHazard.Hazard hazard=null;

    int tStep = Simulation.getCurrentTimeStep();


    if (Area.multipleLifeformsEnabled() &&
        evu.hasLifeform(Lifeform.TREES,tStep) == false) {
      evu.setPpMpbHazard(hazard);
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

    if (species.contains(Species.PIPO)) {
      if (sizeClass == SizeClass.MEDIUM) {
        if (density == Density.ONE || density == Density.TWO) {
          hazard = MtnPineBeetleHazard.LOW;
        }
        else if (density == Density.THREE) {
          hazard = MtnPineBeetleHazard.MODERATE;
        }
        else if (density == Density.FOUR) {
          hazard = MtnPineBeetleHazard.HIGH;
        }
      }
      else if (sizeClass == SizeClass.MMU || sizeClass == SizeClass.LMU ||
               sizeClass == SizeClass.VLMU) {
        if (density == Density.ONE || density == Density.TWO || density == Density.THREE) {
          hazard = MtnPineBeetleHazard.LOW;
        }
      }
      else if (sizeClass == SizeClass.MMU) {
        if (density == Density.FOUR) {
          hazard = MtnPineBeetleHazard.MODERATE;
        }
      }
      else if (sizeClass == SizeClass.LMU || sizeClass == SizeClass.VLMU) {
        if (density == Density.FOUR) {
          hazard = MtnPineBeetleHazard.HIGH;
        }
      }
      else if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.VERY_LARGE) {
        if (density == Density.THREE || density == Density.FOUR) {
          hazard = MtnPineBeetleHazard.HIGH;
        }
      }
    }

    evu.setPpMpbHazard(hazard);
  }
/**
 * Adjusts hazard of ponderosa pine mountain pine beetle.  Based on combinations for low existing hazard, moderate existing hazard, and high existing hazard
 * and the process type in adjacent and the beetle count in adjacent, and whether there was a stand replaceing fire and mixed severity fire  and time step
 * @param zone
 * @param evu
 */
  public void adjustHazard(RegionalZone zone, Evu evu) {
    Area           area = Simpplle.currentArea;
    AdjacentData[] adjacentData;
    Evu            adj;
    boolean        lowHazard, modHazard, highHazard;
    MtnPineBeetleHazard.Hazard unitHazard, adjHazard;
    int            adjLowCount = 0, adjModerateCount = 0, adjHighCount = 0;
    int            adjProcessCount;
    ProcessType    adjProcessType;
    boolean        ppMpb;
    int            ppMpbCount = 0, lightLpMpb = 0, severeLpMpb = 0;
    int            standReplacingFire = 0, mixedSeverityFire = 0;
    int            page = -1, row = -1, col = 0;
    int            cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    unitHazard  = evu.getPpMpbHazard();
    lowHazard   = (unitHazard == MtnPineBeetleHazard.LOW);
    modHazard   = (unitHazard == MtnPineBeetleHazard.MODERATE);
    highHazard  = (unitHazard == MtnPineBeetleHazard.HIGH);

    VegSimStateData state = evu.getState(cTime-1);
    ppMpb        = (state != null ? state.getProcess().equals(ProcessType.PP_MPB) : false);
    adjacentData = evu.getAdjacentData();

    for(int i=0;i<adjacentData.length;i++) {
      adj            = adjacentData[i].evu;

      VegSimStateData adjState = adj.getState(cTime-1);
      if (adjState == null) { continue; }
      adjProcessType = adj.getState(cTime - 1).getProcess();

      adjHazard      = adj.getPpMpbHazard();

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
        ppMpbCount = 1;
      }
      else if (adjProcessType.equals(ProcessType.LIGHT_LP_MPB)) {
        lightLpMpb = 1;
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
    adjProcessCount  = ppMpbCount + lightLpMpb + severeLpMpb;
    adjProcessCount += standReplacingFire + mixedSeverityFire;

    // ** combinations for low existing hazard **
    // **
    if (lowHazard) {
      if (!ppMpb && (adjProcessCount == 0) && (adjModerateCount > 0) &&
          (adjHighCount > 0)) {
        probability = 1;
      }
      else if (ppMpb && (adjProcessCount == 0) && (adjModerateCount == 0) &&
               (adjHighCount == 0)) {
        probability = 10;
      }
      else if (!ppMpb && (adjProcessCount > 0) && adjModerateCount == 0 &&
               adjHighCount == 0) {
        probability = 5;
      }
      else if (!ppMpb && (adjProcessCount > 0) && (adjModerateCount > 0) &&
               (adjHighCount > 0)) {
        probability = 10;
      }
      else if (!ppMpb && (adjProcessCount == 0) && adjModerateCount > 0 &&
               adjHighCount > 0) {
        probability = 15;
      }
      else if (ppMpb && (adjProcessCount > 0) && (adjModerateCount > 0) &&
               adjHighCount > 0) {
        probability = 20;
      }
      else {
        probability = 0;
      }
    }
    else if (modHazard) {
      // ** combinations for mod existing hazard **
      // **
      if (!ppMpb && (adjProcessCount == 0) && (adjHighCount == 0)) {
        probability = 5;
      }
      else if (!ppMpb && (adjProcessCount > 0) && (adjHighCount == 0)) {
        probability = 10;
      }
      else if (!ppMpb && (adjProcessCount == 0) && (adjHighCount > 0)) {
        probability = 15;
      }
      else if (ppMpb && (adjProcessCount == 0) && (adjHighCount > 0)) {
        probability = 20;
      }
      else if (!ppMpb && (adjProcessCount > 0) && (adjHighCount > 0)) {
        probability = 50;
      }
      else if (ppMpb && (adjProcessCount > 0) && (adjHighCount > 0)) {
        probability = 60;
      }
      else {
        probability = 0;
      }
    }
    else if (highHazard) {
      // ** combinations for existing hazard of high **
      // **
      if (!ppMpb && (adjProcessCount == 0) && (adjModerateCount == 0) &&
          (adjHighCount == 0)) {
        probability = 10;
      }
      else if (!ppMpb && (adjProcessCount > 0) && (adjModerateCount == 0) &&
               (adjHighCount == 0)) {
        probability = 50;
      }
      else if (!ppMpb && (adjProcessCount == 0) &&
               (adjModerateCount > 0 || adjHighCount > 0)) {
        probability = 15;
      }
      else if (!ppMpb && (adjProcessCount == 0) && adjHighCount > 0) {
        probability = 20;
      }
      else if (!ppMpb && (adjProcessCount > 0) && adjHighCount > 0) {
        probability = 60;
      }
      else if (ppMpb && (adjProcessCount == 0) && adjHighCount > 0) {
        probability = 65;
      }
      else if (ppMpb && (adjProcessCount > 0) && adjHighCount > 0) {
        probability = 70;
      }
      else {
        probability = 0;
      }
    }
    else {
      probability = 0;
    }
  }

  public void adjustHazard(WestsideRegionOne zone, Evu evu) {
    adjustHazard((RegionalZone)zone,evu);
  }

  public void adjustHazard(EastsideRegionOne zone, Evu evu) {
    adjustHazard((RegionalZone)zone,evu);
  }
  public void adjustHazard(Teton zone, Evu evu) {
    adjustHazard((RegionalZone)zone,evu);
  }
  public void adjustHazard(NorthernCentralRockies zone, Evu evu) {
    adjustHazard((RegionalZone)zone,evu);
  }

  /**
   * method to determine risk of an evu to have ponderosa pine mountain pine beetle based on size class, current time step, species, and density.
   * @param evu
   * @return
   */
  private int determineRisk(Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    Species     species     = state.getVeg().getSpecies();
    SizeClass   sizeClass   = state.getVeg().getSizeClass();
    Density     density     = state.getVeg().getDensity();
    int         cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    int         risk=0;

    if (species.equals(Species.TPP) || species.equals(Species.TPPO) ||
        species.equals(Species.TOPP) || species.equals(Species.PP)) {
      risk = 3;
    }
    else if (species.equals(Species.TDF_TPP) || species.equals(Species.TPP_TDF)) {
      risk = 2;
    }
    else { return 0; }

    if (sizeClass.equals(SizeClass.SS)) {
      risk += 1;
    }
    else if (sizeClass.equals(SizeClass.POLE) || sizeClass.equals(SizeClass.MEDIUM)) {
      risk += 2;
    }
    else if (sizeClass.equals(SizeClass.LARGE) || sizeClass.equals(SizeClass.VERY_LARGE)) {
      risk += 3;
    }

    if (density.equals(Density.TWO)) {
      risk += 1;
    }
    else if (density.equals(Density.THREE)) {
      risk += 2;
    }
    else if (density.equals(Density.FOUR)) {
      risk += 3;
    }

    VegSimStateData priorState = evu.getState(cTime-1);
    ProcessType process = (priorState != null ? priorState.getProcess() : ProcessType.NONE);
    if (process.equals(ProcessType.PP_MPB)) { risk += 3; }

    return risk;
  }
/**
 * Calculates probability for Gila zone based on risk designations.  
 * @param evu
 * @return 
 */
  private int doProbabilityGilaUtah(Evu evu) {
    int risk = determineRisk(evu);
    int prob = 0;
    int page=0, row=0, col=0;

    if (risk >= 1 && risk <= 5) {
      row = 0;
    }
    else if (risk >= 6 && risk <= 9) {
      row = 1;
    }
    else if (risk >= 10 && risk <= 12) {
      row = 2;
    }
    else { return 0; }

    return getProbData(page,row,col);
  }
  public int doProbability (SouthwestUtah zone, Evu evu) {
    return doProbabilityGilaUtah(evu);
  }

  public int doProbability (Gila zone, Evu evu) {
    return doProbabilityGilaUtah(evu);
  }

  private boolean doSpreadCommon (RegionalZone zone, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (prob != Evu.L &&
        processType.equals(ProcessType.SUCCESSION)) {
      if (evu.getProcessProb(ProcessType.PP_MPB) >= 1000) {
        evu.updateCurrentProcess(ProcessType.PP_MPB);
        evu.updateCurrentProb(Evu.S);

        // TO DO: landscape summary spread-to information.
        // Move to after original call of doSpread.
        return true;
      }
      else if (evu.getProcessProb(ProcessType.LIGHT_LP_MPB) >= 5000 ||
               evu.getProcessProb(ProcessType.SEVERE_LP_MPB) >= 5000) {
        evu.updateCurrentProcess(ProcessType.LIGHT_LP_MPB);
        evu.updateCurrentProb(Evu.S);

        // TO DO: landscape summary spread-to information.
        // Move to after original call of doSpread.
        return true;
      }
    }
    return false;
  }

  public boolean doSpread (WestsideRegionOne zone, Evu fromEvu, Evu evu) {
    return doSpreadCommon(zone,evu);
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
   * Ponderosa Pine Mountain Pine Beetle does not occur Sierra Nevada, so spread is set to false. 
   */
  public boolean doSpread (SierraNevada zone, Evu fromEvu, Evu evu) {
    return false;
  }

  /**
   * Ponderosa Pine Mountain Pine Beetle does not occur Sierra Nevada, so spread is set to false. 
   */
  public boolean doSpread (SouthernCalifornia zone, Evu fromEvu, Evu evu) {
    return false;
  }
/**
 * Ponderosa Pine Mountain Pine Beetle does not occur Sierra Nevada, so spread is set to false. 
 */

  public boolean doSpread (SouthCentralAlaska zone, Evu fromEvu, Evu evu) { return false; }

  /**
   * Spread for Gila zone.  This is based on current simulation vegetative state process type and probability 
   * returns true spread, false otherwise
   */
  public boolean doSpread (Gila zone, Evu fromEvu, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (prob != Evu.L &&
        processType.equals(ProcessType.SUCCESSION)) {
      if (evu.getProcessProb(ProcessType.PP_MPB) >= 1000) {
        evu.updateCurrentProcess(ProcessType.PP_MPB);
        evu.updateCurrentProb(Evu.S);
        return true;
      }
    }
    return false;
  }
/**
 * Spread for Southwest Utah zone.  This is based on current simulation vegetative state process type and probability 
 */
  public boolean doSpread (SouthwestUtah zone, Evu fromEvu, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (prob != Evu.L &&
        processType.equals(ProcessType.SUCCESSION)) {
      if (evu.getProcessProb(ProcessType.PP_MPB) >= 1000) {
        evu.updateCurrentProcess(ProcessType.PP_MPB);
        evu.updateCurrentProb(Evu.S);
        return true;
      }
    }
    return false;
  }
/**
 * Spread for Colorado Front Range zone.  This is based on current simulation vegetative state process type and probability 
 */
  public boolean doSpread(ColoradoFrontRange zone, Evu fromEvu, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (prob == Evu.L ||
        (processType.equals(ProcessType.SUCCESSION) == false)) {
      return false;
    }

    if (evu.getProcessProb(ProcessType.LIGHT_LP_MPB) >= 5000 ||
        evu.getProcessProb(ProcessType.SEVERE_LP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.LIGHT_LP_MPB);
      evu.updateCurrentProb(Evu.S);
      return true;
    }
    else if (evu.getProcessProb(ProcessType.PP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.PP_MPB);
      evu.updateCurrentProb(Evu.S);
      return true;
    }

    return false;
  }
  /**
   * Spread for Colorado Plateau zone.  This is based on current simulation vegetative state process type and probability 
   */
  public boolean doSpread(ColoradoPlateau zone, Evu fromEvu, Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (prob == Evu.L ||
        (processType.equals(ProcessType.SUCCESSION) == false)) {
      return false;
    }

    if (evu.getProcessProb(ProcessType.LIGHT_LP_MPB) >= 5000 ||
        evu.getProcessProb(ProcessType.SEVERE_LP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.LIGHT_LP_MPB);
      evu.updateCurrentProb(Evu.S);
      return true;
    }
    else if (evu.getProcessProb(ProcessType.PP_MPB) >= 5000) {
      evu.updateCurrentProcess(ProcessType.PP_MPB);
      evu.updateCurrentProb(Evu.S);
      return true;
    }

    return false;
  }

  /**
   * outputs "PP-MPB"
   */
  public String toString () {
    return printName;
  }

}

