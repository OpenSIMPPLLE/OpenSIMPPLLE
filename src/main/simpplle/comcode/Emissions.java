/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import simpplle.comcode.Species;

import java.io.*;
import java.util.*;

/**
 * This class is contains static methods used in determining pm10 emissions for a given evu.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class Emissions {
  private static Hashtable data;

  public static final int PROCESS   = 0;
  public static final int TREATMENT = 1;

  // Weather and fuels conditions
  public static final int SURFACE_LOW      = 0;
  public static final int SURFACE_MODERATE = 1;
  public static final int SURFACE_HIGH     = 2;
  public static final int SURFACE_EXTREME  = 3;
  public static final int CROWN_HIGH       = 4;
  public static final int CROWN_EXTREME    = 5;

  // temporary used for key to hashtable in getPM10
  private static EmissionData emissionData = new EmissionData();

  private static CurrentState tmpCurrentState = new CurrentState();

  private Emissions() {}

  public static void readData (BufferedReader fin) throws SimpplleError {
    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SOUTHERN_CALIFORNIA ||
        zoneId == ValidZones.SIERRA_NEVADA) {
      readCaliforniaData(fin);
    }
    else {
      readOldData(fin);
    }
  }

  private static void readOldData(BufferedReader fin) throws SimpplleError {
    int                 crbId, strStg;
    String              moisture, line, value;
    double              pm10;
    EmissionData        emit;
    StringTokenizerPlus strTok;
    boolean             eof = false;

    data = new Hashtable();

    try {
      do {
        line = fin.readLine();
        if (line == null) { eof = true; continue; }

        strTok   = new StringTokenizerPlus(line,",");
        crbId    = strTok.getIntToken();
        strStg   = strTok.getIntToken();
        value    = strTok.getToken();
        moisture = value.trim().toUpperCase();
        pm10     = strTok.getDoubleToken();

        emit = new EmissionData(crbId, strStg, moisture);
        data.put(emit,new Double(pm10));
      }
      while (eof != true);
    }
    catch (ParseError pe) {
      String msg = "Error reading Emissions Data File.\n" + pe.msg;
      throw new SimpplleError(msg);
    }
    catch (IOException IOE) {
      String msg = "Problems while trying to read input file.";
      throw new SimpplleError(msg);
    }
  }
/**
 * Reads in california data, stores in hashtable.  PM10 data is converted from tons per acre to pounds per acre.
 * @param fin 
 * @throws SimpplleError to be caught by gui if could not parse file or another exception occurs
 */
  private static void readCaliforniaData(BufferedReader fin)
    throws SimpplleError
  {
    int                 fccNo; // currently not used.
    StringTokenizerPlus strTok;
    Vector              speciesVect, sizeClassVect, densityVect;
    Species             species;
    SizeClass           sizeClass;
    CurrentState        key;
    Density             density;
    double[]            pm10;
    String              line;

    data = new Hashtable();

    try {
      do {
        line = fin.readLine();
        if (line == null) { continue; }

        strTok = new StringTokenizerPlus(line,",");
        fccNo         = strTok.getIntToken();
        speciesVect   = strTok.getListValue();
        sizeClassVect = strTok.getListValue();
        densityVect   = strTok.getListValue();

        // The data in the file is in tons per acre
        // We want pounds per acre.
        pm10 = new double[6];
        pm10[SURFACE_LOW]      = strTok.getFloatToken() * 2000.0f;
        pm10[SURFACE_MODERATE] = strTok.getFloatToken() * 2000.0f;
        pm10[SURFACE_HIGH]     = strTok.getFloatToken() * 2000.0f;
        pm10[SURFACE_EXTREME]  = strTok.getFloatToken() * 2000.0f;
        pm10[CROWN_HIGH]       = strTok.getFloatToken() * 2000.0f;
        pm10[CROWN_EXTREME]    = strTok.getFloatToken() * 2000.0f;

        int sp, sz, d;
        for(sp=0; sp<speciesVect.size(); sp++) {
          for(sz=0; sz<sizeClassVect.size(); sz++) {
            for(d=0; d<densityVect.size(); d++) {
              species   = Species.get((String)speciesVect.elementAt(sp));
              sizeClass = SizeClass.get((String)sizeClassVect.elementAt(sz));
              density   = Density.get((String)densityVect.elementAt(d));

              key = new CurrentState(species,sizeClass,density);
              data.put(key,pm10);
            }
          }
        }

      }
      while (line != null);
    }
    catch (ParseError e) {
      throw new SimpplleError(e.msg);
    }
    catch (Exception e) {
      throw new SimpplleError(e.getMessage());
    }
  }

  /**
   * Computes the PM2.5 value given the pm10 value.
   * @return a float, the pm2.5 value
   */
  public static double getPM2_5(double pm10) {
    return (pm10 * 0.8);
  }

  public static double getProcessPM10(Evu evu, int timeStep) {
    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SOUTHERN_CALIFORNIA ||
        zoneId == ValidZones.SIERRA_NEVADA) {
      return getCaliforniaPM10(evu, timeStep, PROCESS);
    }
    else {
      return getPM10(evu, timeStep, PROCESS);
    }
  }
/**
 * if current zone being evaluated is Southern California or Sierra Nevada passes to getCaliforniaPM10 else passes to get PM10
 * @param evu
 * @param timeStep
 * @return 
 */
  public static double getTreatmentPM10(Evu evu, int timeStep) {
    int zoneId = Simpplle.getCurrentZone().getId();

    if (zoneId == ValidZones.SOUTHERN_CALIFORNIA ||
        zoneId == ValidZones.SIERRA_NEVADA) {
      return getCaliforniaPM10(evu, timeStep, TREATMENT);
    }
    else {
      return getPM10(evu, timeStep, TREATMENT);
    }
  }
/**
 * calculates PM10 for california depending on evu, current timestep and whether there is a Process or Treatment.  
 * @param evu
 * @param timeStep
 * @param kind choices for this are treatment or process
 * @return
 */
  private static double getCaliforniaPM10(Evu evu, int timeStep, int kind) {
    VegSimStateData priorState = evu.getState(timeStep);
    if (priorState == null) { return 0.0; }

    ProcessType    processType = priorState.getProcess();
    Species        species;
    SizeClass      sizeClass;
    Density        density;
    int            weather;
    double[]       pm10Data;
    VegetativeType state;
    int            processProb;
    boolean        extremeFire, drought;
    double         emissionsPerAcre;

    Treatment     treat = evu.getTreatment(timeStep);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) {
      treatType = treat.getType();
    }

    state = getState(evu,timeStep,kind);
    if (state == null) { return 0.0; }

    species    = state.getSpecies();
    sizeClass  = state.getSizeClass();
    density    = state.getDensity();

    tmpCurrentState.setState(species,sizeClass,density);
    pm10Data = (double[]) data.get(tmpCurrentState);

    if (pm10Data == null) { return 0.0; }

    if (kind == TREATMENT) {
      weather = SURFACE_LOW;
      if (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN      ||
          treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
          treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN ||
          treatType == TreatmentType.CRUSHING_BURNING ||
          treatType == TreatmentType.CUTTING_BURNING) {
        emissionsPerAcre = pm10Data[weather];
      }
      else {
        return 0.0;
      }
    }
    else if (kind == PROCESS) {
      if ( (processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
          (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false) &&
          (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false)) {
        return 0.0;
      }
      processProb = evu.getFireSpreadType(timeStep);
      extremeFire = (processProb == Evu.SE || processProb == Evu.SFS);

      Climate.Season season = Simpplle.getCurrentSimulation().getCurrentSeason();
      drought = Simpplle.getClimate().isUserClimate(timeStep,season);

      if (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) ||
          processType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
        emissionsPerAcre = (extremeFire == false && drought == false) ?
            pm10Data[SURFACE_MODERATE] : pm10Data[SURFACE_EXTREME];
      }
      else if (processType.equals(ProcessType.STAND_REPLACING_FIRE)) {
        emissionsPerAcre = (extremeFire == false && drought == false) ?
            (pm10Data[SURFACE_HIGH] + pm10Data[CROWN_HIGH]) :
            (pm10Data[SURFACE_EXTREME] + pm10Data[CROWN_EXTREME]);
      }
      else {
        return 0.0;
      }
    }
    else { return 0.0; }

    return (emissionsPerAcre * Area.getFloatAcres(evu.getAcres()));
  }

  /**
   * Calcualtes the PM10 
   * @param evu
   * @param timeStep
   * @param kind
   * @return
   */
  private static double getPM10(Evu evu, int timeStep, int kind) {
    VegetativeType       state;
    Species              species;
    SizeClass            sizeClass;
    HabitatTypeGroupType groupType;
    ProcessType          processType;
    Density              density;
    int                  speciesCode, strStgCode;
    String               moisture;
    double               pm10, result;
    int                  acres;
    Double               tmpPM10;

    state = getState(evu,timeStep,kind);
    if (state == null) { return 0.0; }

    VegSimStateData priorState = evu.getState(timeStep);
    if (priorState == null) { return 0.0; }

    processType = priorState.getProcess();
    if (kind == PROCESS &&
        ((processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
         (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false) &&
         (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false))) {
      return 0.0;
    }

    Treatment     treat = evu.getTreatment(timeStep);
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    if (kind == TREATMENT &&
        (treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN      &&
         treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN &&
         treatType != TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN)) {
      return 0.0;
    }

    species   = state.getSpecies();
    sizeClass = state.getSizeClass();
    density   = state.getDensity();
    groupType = state.getHtGrp().getType();
    acres     = evu.getAcres();

    speciesCode = getSpeciesCode(species,groupType);
    strStgCode  =
      getStrStgCode(sizeClass,groupType,density,processType,treatType);
    if (kind == PROCESS) {
      moisture = getProcessMoisture(processType);
    }
    else {
      moisture = getTreatmentMoisture(treatType);
    }

    if (speciesCode == -1 || strStgCode == -1 || moisture == null) {
      pm10 = 0.0;
    }
    else {
      emissionData.setData(speciesCode,strStgCode,moisture);
      tmpPM10 =
        (Double)data.get(emissionData);
      if (tmpPM10 == null) {
        pm10 = 0.0;
      }
      else { pm10 = tmpPM10.doubleValue() * Area.getFloatAcres(acres); }
    }

    return pm10;
  }
/**
 * Gets the current simulation state if it is a process, else gets treatment state
 * @param evu vegetative unit evaluated
 * @param timeStep current time step
 * @param kind 
 * @return VegetativeType state 
 */

  private static VegetativeType getState(Evu evu, int timeStep, int kind) {
    Treatment treat;

    if (kind == PROCESS) {
      VegSimStateData state = evu.getState(timeStep-1);
      if (state == null) { return null; }
      return state.getVeg();
    }
    else {
      treat = evu.getTreatment(timeStep);
      if (treat == null) { return null; }

      return treat.getSavedState();
    }
  }
  /**
   * Gets the species code based on species passed in 
   * codes 8, 9, 10, 12, 13, 14, 15, 16, 19, 24, 25, 30, 39, 41, 45, 46 are species dependent  - code 39 is species and habitat group dependent
   * @param species species to be coded
   * @param groupType Habitat Type
   * @return species code, if no species found -1
   */

  private static int getSpeciesCode(Species species, HabitatTypeGroupType groupType) {
    int code;

    if (species == Species.ES       || species == Species.AF    || species == Species.AL      ||
        species == Species.MH       || species == Species.PF    || species == Species.ES_AF   ||
        species == Species.AF_MH    || species == Species.AL_AF || species == Species.L_ES_AF ||
        species == Species.AF_ES_MH || species == Species.AF_ES_LP) {
      code = 8;
    }
    else if (species == Species.WB       || species == Species.WB_DF    ||
             species == Species.WB_ES    || species == Species.WB_ES_AF ||
             species == Species.WB_ES_LP || species == Species.WB_LP_AF ||
             species == Species.AL_WB_AF) {
      code = 9;
    }
    else if (species == Species.DF       || species == Species.DF_GF ||
             species == Species.DF_LP    || species == Species.DF_ES ||
             species == Species.DF_AF    || species == Species.DF_LP_AF ||
             species == Species.DF_PP_PF || species == Species.DF_AF_ES ||
             species == Species.DF_PP_LP || species == Species.DF_PP_GF ||
             species == Species.DF_LP_GF || species == Species.DF_LP_ES) {
      code = 10;
    }
    else if (species == Species.L       || species == Species.L_DF    ||
             species == Species.L_PP    || species == Species.L_LP    ||
             species == Species.L_GF    || species == Species.L_ES    ||
             species == Species.L_DF_PP || species == Species.L_DF_LP ||
             species == Species.L_DF_ES || species == Species.L_DF_AF ||
             species == Species.L_PP_LP || species == Species.L_DF_GF ||
             species == Species.L_LP_GF || species == Species.EARLY_SERAL) {
      code = 12;
    }
    else if (species == Species.GF) {
      code = 13;
    }
    else if (species == Species.WP         || species == Species.RRWP      ||
             species == Species.L_WP       || species == Species.DF_WP     ||
             species == Species.DF_RRWP    || species == Species.L_RRWP    ||
             species == Species.L_DF_WP    || species == Species.DF_WP_GF  ||
             species == Species.L_WP_GF    || species == Species.L_DF_RRWP ||
             species == Species.DF_RRWP_GF || species == Species.L_RRWP_GF ||
             species == Species.LATE_SERAL) {
      code = 14;
    }
    else if (species == Species.QA || species == Species.QA_MC) {
      code = 15;
    }
    else if (species == Species.LP || species == Species.LP_AF || species == Species.ES_LP) {
      code = 16;
    }
    else if (species == Species.C || species == Species.WH || species == Species.WH_C ||
             species == Species.WH_C_GF) {
      code = 19;
    }
    else if (species == Species.CW        || species == Species.CW_MC ||
             species == Species.RIP_DECID || species == Species.WOODLAND) {
      code = 24;
    }
    else if (species == Species.PP || species == Species.PP_DF /*|| species == Species.PP_LP*/) {
      code = 25;
    }
    else if (species == Species.GRASS || species == Species.UPLAND_GRASSES ||
             species == Species.NATIVE_FORBS || species == Species.ALPINE_GRASSES ||
             species == Species.HERBS        || species == Species.ALPINE_HERBS   ||
             species == Species.RIP_GRAMS    || species == Species.RIPARIAN_GRASSES) {
      code = 30;
    }
    else if (species == Species.FESCUE || species == Species.AGSP ||
             species == Species.MID_SERAL) { // early/late seral handled below.
      code = 39;
    }
    else if (species == Species.XERIC_SHRUBS || species == Species.XERIC_FS_SHRUBS ||
             species == Species.MAHOGANY     || species == Species.MTN_MAHOGANY    ||
             species == Species.JUSC_AGSP    || species == Species.JUSC_ORMI) {
      code = 41;
    }
    else if (species == Species.MESIC_SHRUBS    || species == Species.MTN_FS_SHRUBS ||
             species == Species.MTN_SHRUBS      || species == Species.ALPINE_SHRUBS ||
             species == Species.RIPARIAN_SHRUBS || species == Species.RIP_SHRUBS    ||
             species == Species.FS_S_G          || species == Species.RIP_S_GRAMS) {
      code = 45;
    }
    else if (species == Species.ALTERED_GRASSES || species == Species.ALTERED_NOXIOUS ||
             species == Species.NOXIOUS) {
      code = 46;
    }
    else {
      code = -1;
    }

    if ((species == Species.EARLY_SERAL || species == Species.LATE_SERAL) &&
        ((groupType.equals(HabitatTypeGroupType.A1) == false) &&
         (groupType.equals(HabitatTypeGroupType.A2) == false) &&
         (groupType.equals(HabitatTypeGroupType.B1) == false) &&
         (groupType.equals(HabitatTypeGroupType.B2) == false) &&
         (groupType.equals(HabitatTypeGroupType.B3) == false) &&
         (groupType.equals(HabitatTypeGroupType.D1) == false) &&
         (groupType.equals(HabitatTypeGroupType.D2) == false) &&
         (groupType.equals(HabitatTypeGroupType.D3) == false) &&
         (groupType.equals(HabitatTypeGroupType.C1) == false) &&
         (groupType.equals(HabitatTypeGroupType.C2) == false) &&
         (groupType.equals(HabitatTypeGroupType.E1) == false) &&
         (groupType.equals(HabitatTypeGroupType.E2) == false) &&
         (groupType.equals(HabitatTypeGroupType.F1) == false) &&
         (groupType.equals(HabitatTypeGroupType.F2) == false) &&
         (groupType.equals(HabitatTypeGroupType.G1) == false) &&
         (groupType.equals(HabitatTypeGroupType.G2) == false))) {
      code = 39;
    }
    return code;
  }

  /**
   *Calculate the Emission stg code based on size class, habitat type group, process  type and treatment type.  This is used when dealing with Emision data.  
   * @param sizeClass
   * @param groupType
   * @param density
   * @param processType
   * @param treatType
   * @return
   */
  private static int getStrStgCode(SizeClass sizeClass, HabitatTypeGroupType groupType,
                                   Density density, ProcessType processType,
                                   TreatmentType treatType) {
    int code;

    if (sizeClass  == SizeClass.UNIFORM)                   { code = 8; }
    else if (sizeClass  == SizeClass.SCATTERED)            { code = 2; }
    else if (sizeClass  == SizeClass.CLUMPED)              { code = 5; }
    else if (sizeClass  == SizeClass.OPEN_HERB)            { code = 1; }
    else if (sizeClass  == SizeClass.CLOSED_HERB)          { code = 2; }
    else if (sizeClass  == SizeClass.OPEN_LOW_SHRUB)       { code = 4; }
    else if (sizeClass  == SizeClass.CLOSED_LOW_SHRUB)     { code = 4; }
    else if (sizeClass  == SizeClass.OPEN_MID_SHRUB)       { code = 3; }
    else if (sizeClass  == SizeClass.CLOSED_MID_SHRUB)     { code = 3; }
    else if (sizeClass  == SizeClass.OPEN_TALL_SHRUB)      { code = 8; }
    else if (sizeClass  == SizeClass.CLOSED_TALL_SHRUB)    { code = 8; }
    else if (sizeClass  == SizeClass.NF)                   { code = 0; }
    else if (sizeClass  == SizeClass.WATER)                { code = 0; }
    else if (sizeClass  == SizeClass.AGR)                  { code = 1; }
    else if (sizeClass  == SizeClass.NS)                   { code = 1; }
    else if (sizeClass  == SizeClass.ND)                   { code = 0; }
    else if (sizeClass  == SizeClass.GRASS)                { code = 5; }
    else { code = -1; }

    if (code != -1) {
      return code;
    }
    else if (sizeClass == SizeClass.SS) {
      if (density == Density.TWO) {
        if (groupType.equals(HabitatTypeGroupType.A1) ||
            groupType.equals(HabitatTypeGroupType.A2) ||
            groupType.equals(HabitatTypeGroupType.G2)) {
          code = 2;
        }
        else { code = 1;}
      }
      else if  (density == Density.THREE) { code = 1; }
      else if  (density == Density.FOUR)  { code = 3; }
      else { code = -1; }
    }
    else if (sizeClass == SizeClass.POLE) {
      if (density == Density.TWO) {
        if (groupType.equals(HabitatTypeGroupType.A1) ||
            groupType.equals(HabitatTypeGroupType.A2) ||
            groupType.equals(HabitatTypeGroupType.G2)) {
          code = 2;
        }
        else { code = 4;}
      }
      else if (density == Density.THREE) { code = 4; }
      else if (density == Density.FOUR)  { code = 3; }
      else { code = -1; }
    }
    else if (sizeClass == SizeClass.PTS || sizeClass == SizeClass.PMU) {
      if (density == Density.TWO)        { code = 4; }
      else if (density == Density.THREE) { code = 5; }
      else if (density == Density.FOUR)  { code = 5; }
      else { code = -1; }
    }
    else if (sizeClass == SizeClass.MEDIUM) {
      if (density ==  Density.TWO) {
        if (groupType.equals(HabitatTypeGroupType.A1) ||
            groupType.equals(HabitatTypeGroupType.A2) ||
            groupType.equals(HabitatTypeGroupType.G2)) {
          code = 2;
        }
        else if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
                  (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
                 (treatType == TreatmentType.NONE)) {
          code = 7;
        }
        else { code = 4; }
      }
      else if (density == Density.THREE) {
        if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
             (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
            (treatType == TreatmentType.NONE)) {
          code = 7;
        }
        else {code = 4; }
      }
      else if (density == Density.FOUR) {
        if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
             (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
            (treatType == TreatmentType.NONE)) {
          code = 7;
        }
        else {code = 3; }
      }
      else { code = -1; }
    }
    else if (sizeClass == SizeClass.MTS || sizeClass == SizeClass.MMU) {
      if (density == Density.TWO) {
        if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
             (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
            (treatType == TreatmentType.NONE)) {
          code = 5;
        }
        else { code = 4; }
      }
      else if (density == Density.TWO) {
        if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
             (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
            (treatType == TreatmentType.NONE)) {
          code = 5;
        }
        else { code = 4; }
      }
      else if (density == Density.FOUR) { code = 5; }
      else { code = -1; }
    }
    else if (sizeClass == SizeClass.LARGE || sizeClass == SizeClass.VERY_LARGE) {
      if (density == Density.TWO) {
        if (groupType.equals(HabitatTypeGroupType.A1) ||
            groupType.equals(HabitatTypeGroupType.A2) ||
            groupType.equals(HabitatTypeGroupType.G2)) {
          code = 2;
        }
        else if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
                  (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
                 (treatType == TreatmentType.NONE)) {
          code = 7;
        }
        else { code = 4; }
      }
      else if (density == Density.THREE) {
        if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
             (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
            (treatType == TreatmentType.NONE)) {
          code = 7;
        }
        else {code = 4; }
      }
      else if (density == Density.FOUR) {
        if (((processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false) &&
             (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)) ||
            (treatType == TreatmentType.NONE)) {
          code = 7;
        }
        else { code = 3; }
      }
      else { code = -1; }
    }
    else if (sizeClass == SizeClass.LMU  || sizeClass == SizeClass.LTS  ||
             sizeClass == SizeClass.VLMU || sizeClass == SizeClass.VLTS) {
      if (density == Density.TWO)        { code = 4; }
      else if (density == Density.THREE) { code = 6; }
      else if (density == Density.FOUR)  { code = 6; }
      else { code = -1; }
    }
    else {
      code = -1;
    }

    return code;
  }

  /**
   * gets process moisture based on fire severity process type
   * @param processType
   * @return NML for normal, Dry for dry.  
   */
  private static String getProcessMoisture (ProcessType processType) {
    if (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
      return "NML";
    }
    else if (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) ||
             processType.equals(ProcessType.STAND_REPLACING_FIRE)) {
      return "DRY";
    }
    else {
      return null;
    }
  }
  
  /**
   * Gets treatment moisture for underburn, broadcast burn, and thin and underburn
   * @param treatType treatment type used
   * @return nml if for above treatments null otherwise
   */

  private static String getTreatmentMoisture(TreatmentType treatType) {
    if (treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_UNDERBURN ||
        treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_BROADCAST_BURN ||
        treatType == TreatmentType.ECOSYSTEM_MANAGEMENT_THIN_AND_UNDERBURN) {
      return "NML";
    }
    else {
      return null;
    }
  }

}
