package simpplle.comcode.process;

import simpplle.comcode.*;
import simpplle.comcode.Process;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.awt.Color;
import java.util.ArrayList;
import java.util.*;

/** 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class deals Succession, a type of process.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *   
 */
public class Succession extends Process {
  private static final String printName = "SUCCESSION";

  public Succession () {
    super();

    spreading   = false;
    description = "Succession";
    color       = new Color(0,170,0);

    defaultVisibleColumns.add(BaseLogic.Columns.ROW_COL.toString());
    defaultVisibleColumns.add(ProcessProbLogic.Columns.PROB_COL.toString());
  }

  public static ProcessType getColoradoSuccession() {
    Climate climate  = Simpplle.getClimate();
    Climate.Moisture    moisture = climate.getMoisture();
    Climate.Temperature temp  = climate.getTemperature();

    if ((temp == Climate.COOLER) && (moisture == Climate.WETTER)) {
      return ProcessType.WET_SUCCESSION;
    }
    else if ((temp == Climate.WARMER) && (moisture == Climate.DRIER)) {
      return ProcessType.DRY_SUCCESSION;
    }
    else { return ProcessType.SUCCESSION; }
  }


  protected int doProbability (Evu evu) {
    return 100;
  }

  public int doProbability (RegionalZone zone, Evu evu) {
    return doProbability(evu);
  }

  public String toString () {
    return printName;
  }

  public static boolean isRegenState(RegionalZone zone, Evu evu, Lifeform lifeform) {
    if (RegenerationLogic.isDataPresent()) {
      if (zone.getId() == ValidZones.SOUTH_CENTRAL_ALASKA) {
        return isRegenStateAlaska(zone,evu,lifeform);
      }
      return isRegenStateNew(evu,lifeform);
    }

    switch (zone.getId()) {
      case ValidZones.WESTSIDE_REGION_ONE:
      case ValidZones.EASTSIDE_REGION_ONE:
      case ValidZones.TETON:
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
      case ValidZones.GILA:
        return isRegenStateCommon(zone,evu);
      case ValidZones.SIERRA_NEVADA:
      case ValidZones.SOUTHERN_CALIFORNIA:
        return isRegenStateCalifornia(zone,evu);
      case ValidZones.SOUTH_CENTRAL_ALASKA:
        return isRegenStateAlaska(zone,evu,lifeform);
      default:
        return false;
    }
  }

  public static boolean isRegenStateNew(Evu evu, Lifeform lifeform) {
    return RegenerationLogic.isSuccessionSpecies(evu.getHabitatTypeGroup().getType(),evu,lifeform);
  }
  public static boolean isRegenStateAlaska(RegionalZone zone, Evu evu, Lifeform lifeform) {
    boolean isRegenSpecies =
                 RegenerationLogic.isSuccessionSpecies(evu.getHabitatTypeGroup().getType(),evu,lifeform);
    Density density = (Density)evu.getState(SimpplleType.DENSITY);

    return (isRegenSpecies &&
            (density == Density.W || density == Density.O));
  }
/**
 * calculates if a zone and unit is in regeneration state.  This is based on species, habitat type group and size class
 * @param zone Regional zone
 * @param evu
 * @return true if zone is in regeneration state
 */
  public static boolean isRegenStateCommon(RegionalZone zone, Evu evu) {
    HabitatTypeGroup     htGrp;
    HabitatTypeGroupType groupType;

    htGrp     = evu.getHabitatTypeGroup();

    VegSimStateData state = evu.getState();
    if (state == null) { return false; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();

    groupType = htGrp.getType();

    if ((species == Species.NS && sizeClass == SizeClass.NS &&
         density == Density.ONE) ||
        ((species == Species.NATIVE_FORBS || species == Species.UPLAND_GRASSES  ||
          species == Species.XERIC_SHRUBS || species == Species.ALTERED_GRASSES ||
          species == Species.MESIC_SHRUBS || species == Species.ALPINE_GRASSES) &&
         (groupType.equals(HabitatTypeGroupType.A1) ||
          groupType.equals(HabitatTypeGroupType.A2) ||
          groupType.equals(HabitatTypeGroupType.B1) ||
          groupType.equals(HabitatTypeGroupType.B2) ||
          groupType.equals(HabitatTypeGroupType.B3) ||
          groupType.equals(HabitatTypeGroupType.D1) ||
          groupType.equals(HabitatTypeGroupType.D2) ||
          groupType.equals(HabitatTypeGroupType.D3) ||
          groupType.equals(HabitatTypeGroupType.C1) ||
          groupType.equals(HabitatTypeGroupType.C2) ||
          groupType.equals(HabitatTypeGroupType.E1) ||
          groupType.equals(HabitatTypeGroupType.E2) ||
          groupType.equals(HabitatTypeGroupType.F1) ||
          groupType.equals(HabitatTypeGroupType.F2) ||
          groupType.equals(HabitatTypeGroupType.G1) ||
          groupType.equals(HabitatTypeGroupType.G2)))) {
      return true;
    }
    else {
      return false;
    }
  }
/**
 * Calculates whether this is a regeneration state in the California regional zone.  
 * @param zone
 * @param evu
 * @return false if no species, true if species equals Bo, MTN_CHP, XERIC shrubs, Mesic Shrubs, or CA chp, false otherwise
 */
  public static boolean isRegenStateCalifornia(RegionalZone zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return false; }

    if (species == Species.BO     || species == Species.MTN_CHP      ||
        species == Species.CA_CHP || species == Species.XERIC_SHRUBS ||
        species == Species.MESIC_SHRUBS) {
      return true;
    }
    else {
      return false;
    }
  }
/**
 * designates the whether the zones and evu combination is regeneration delay 
 * Note: Alaska does not have regeneration delay.  
 * @param zone
 * @param evu
 * @return true if regen is regeneration delay, false otherwise, defaults to just whether an evu is in delay
 */
  public static boolean isRegenDelay(RegionalZone zone, Evu evu) {
    switch (zone.getId()) {
      case ValidZones.WESTSIDE_REGION_ONE:
      case ValidZones.EASTSIDE_REGION_ONE:
      case ValidZones.TETON:
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
      case ValidZones.SIERRA_NEVADA:
      case ValidZones.SOUTHERN_CALIFORNIA:
      case ValidZones.GILA:
        return isRegenDelayCommon(zone,evu);
      case ValidZones.SOUTH_CENTRAL_ALASKA:
        
        return false;
      default:
        return isRegenDelay(evu);
    }
  }
/**
 * overloaded isRegenDelay method, hard coded to only return false 
 * @param evu
 * @return false no matter the evu input into this method... it is designed only to invalidate an isRegenDelay
 */
  public static boolean isRegenDelay(Evu evu) {
    return false;
  }
/**
 * computes common regeneration delay for an evu in a zone.   
 * @param zone
 * @param evu
 * @return false if not in regeneration delay or no species found.  
 * <p>True if habitat type group is A1, A2, B2, D3, or E2 AND species = NS Mesic Shrubs, Upland Grasses, Xeric Shrubs, Alpine or Altered Grasses or Native vorbs
 * AND the evu succession is not 3 decades
 */
  private static boolean isRegenDelayCommon (RegionalZone zone, Evu evu) {
    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();
    Species              species   = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return false; }

    if ((groupType.equals(HabitatTypeGroupType.A1) ||
         groupType.equals(HabitatTypeGroupType.A2) ||
         groupType.equals(HabitatTypeGroupType.B2) ||
         groupType.equals(HabitatTypeGroupType.D3) ||
         groupType.equals(HabitatTypeGroupType.E2)) &&
        (species == Species.NS              || species == Species.XERIC_SHRUBS    ||
         species == Species.MESIC_SHRUBS    || species == Species.ALPINE_GRASSES  ||
         species == Species.UPLAND_GRASSES  || species == Species.ALTERED_GRASSES ||
         species == Species.NATIVE_FORBS) &&
        (evu.succession_n_decades(3) != true)) {
      return true;
    }
    else {
      return false;
    }
  }

  public static VegetativeType regen(RegionalZone zone,
                                     Evu evu,
                                     Lifeform lifeform,
                                     Lifeform adjLifeform) {
    if (RegenerationLogic.isDataPresent()) {
      switch (zone.getId()) {
        default:
          return regenNew(zone, evu, lifeform, adjLifeform);
      }
    }
    else {
      return regenCommon(zone, evu);
    }
  }

  // These variables are to eliminate excess temporaries being created
  // in the following method during simulations.
  private static HashMap<Evu,Integer> seedSource      = new HashMap<Evu,Integer>();
  private static ArrayList<Evu>       seedSourceKeys  = new ArrayList<Evu>();
  private static ArrayList<MyInteger>   seedSourceAcres = new ArrayList<MyInteger>();
  private static ArrayList<Species>   seedSourceSpecies = new ArrayList<Species>();
  /**
   *    The Adjacent Process we are looking at below and in the other R1 regen could be either the
   *    process at the begin or end of the current time step, depending on
   *    whether or not its doNextState function has been called yet.
   *    He decided that this gives the a bit of variability and its perhaps
   *    a good thing.
   *    Also discussed the idea of ordering the seed species first by
   *    lifeform type than by acres within lifeforms, with Trees being first.
   *
   * @param zone 
   * @param evu
   * @return
   */
  private static VegetativeType regenNew(RegionalZone zone,
                                         Evu evu,
                                         Lifeform lifeform,
                                         Lifeform regenLifeform) {
    AdjacentData[]   adjacentData;
    Species          species;
    int              index;
    VegetativeType   newState = null, tmpState;
    HabitatTypeGroup htGrp;
    int              i,j;
    int              numAdj;
    int              cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    adjacentData  = evu.getAdjacentData();
    numAdj        = adjacentData.length;
    htGrp         = evu.getHabitatTypeGroup();

    HabitatTypeGroupType ecoGroup = evu.getHabitatTypeGroup().getType();

    seedSource.clear();
    seedSourceKeys.clear();
    seedSourceAcres.clear();
    seedSourceSpecies.clear();

//    if (numAdj > 0) {
//      seedSource = new HashMap<Evu,Integer>(numAdj);
//      key        = new Evu[numAdj];
//      value      = new int[numAdj];
//      index      = 0;
//
//      for(i=0;i<key.length;i++) {
//        key[i]   = null;
//        value[i] = -1;
//      }
//    }
//    else {
//      seedSource = new HashMap<Evu,Integer>(1);
//      key        = new Evu[1];
//      value      = new int[1];
//      index      = 0;
//
//      key[0]   = null;
//      value[0] = -1;
//    }


    // *** Find units Producing Seed ***
    // *********************************
    Species tmpSpecies;
    index = 0;
    for(i=0;i<numAdj;i++) {
      Evu adj        = adjacentData[i].evu;
      // Do not want current because doNextState may have already happened
      // for this adj unit, in which case we would be getting the wrong Species.
      VegSimStateData adjState = adj.getState(cStep-1,regenLifeform);
      if (adjState == null) { continue; }

      if (adj.producingSeed(regenLifeform,Evu.ADJACENT_SEED))
      {
        MyInteger acres;
        Integer keyValueIndex = (Integer) seedSource.get(adj);
        if (keyValueIndex == null) {
          acres         = new MyInteger(adj.getAcres());
          keyValueIndex = index;
          seedSource.put(adj,keyValueIndex);
          seedSourceKeys.add(adj);
          seedSourceAcres.add(acres);
          seedSourceSpecies.add(adjState.getVeg().getSpecies());
          index++;
        }
        else {
          acres = seedSourceAcres.get(keyValueIndex);
          acres.plus(adj.getAcres());
        }
      }
    }

    // *** In Landscape Seed ***
    // *************************
    if (evu.getRecentRegenDelay(regenLifeform)) {
      VegSimStateData lastLifeState = evu.findLastLifeformState(regenLifeform);
      if (lastLifeState != null &&
          evu.calculateProducingSeed(lastLifeState.getVeg(),
                                     lastLifeState.getTimeStep(),
                                     regenLifeform,
                                     Evu.IN_LANDSCAPE_SEED))
      {
        tmpState = RegenerationLogic.getInLandscapeSeedState(ecoGroup,
          evu,
          lastLifeState,
          lastLifeState.getTimeStep(),
          regenLifeform);
        if (tmpState != null) {
          newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                             tmpState.getSizeClass(),
                                             tmpState.getAge(),
                                             tmpState.getDensity());
          if (newState != null) {
            evu.setRecentRegenDelay(regenLifeform,false);
            return newState;
          }
        }
      }
    }
    tmpState = null;

    ArrayList<RegenerationSuccessionInfo>  prefSpecies =
      RegenerationLogic.getSuccessionSpecies(ecoGroup,evu,lifeform);
    RegenerationSuccessionInfo regenInfo;
    if (prefSpecies != null && prefSpecies.size() > 0) {
      for (i=0; i<prefSpecies.size(); i++) {
        regenInfo = (RegenerationSuccessionInfo)prefSpecies.get(i);
        if (seedSourceSpecies.contains(regenInfo.seedSpecies) == false) { continue; }

        newState = htGrp.getVegetativeType(regenInfo.nextState.getSpecies(),
                                           regenInfo.nextState.getSizeClass(),
                                           regenInfo.nextState.getAge(),
                                           regenInfo.nextState.getDensity());
        if (newState != null) { return newState; }
      }
      // If there are preferred species then that is all we can look at
      // otherwise we can continue.
      return null;
    }

    // If we made it here there must be no preferred species specified.
    if (seedSourceKeys == null || seedSourceKeys.size() == 0) {
      return null;
    }

    Utility.sort(seedSourceKeys,seedSourceAcres);
    ArrayList<Evu> sortedKeys = seedSourceKeys;

    ArrayList<VegetativeType>  v;
    for (i=0; i<sortedKeys.size(); i++) {
      if (sortedKeys.get(i) == null) { continue; }
      v = RegenerationLogic.getAdjacentStates(ecoGroup,sortedKeys.get(i),cStep-1,lifeform);
      if (v == null || v.size() == 0) { continue; }
      for (j=0; j<v.size(); j++) {
        tmpState = (VegetativeType)v.get(j);
        newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                           tmpState.getSizeClass(),
                                           tmpState.getAge(),
                                           tmpState.getDensity());
        if (newState != null) { return newState; }
      }
    }
    return null;
  }
  
  /**
   *  Build a hashtable: key = species, value = acres;
   *  if species is seed producing add its acres to the value already in the hashtable.
   *
   *  Special note:
   *  When two units of equal acres provide seed source,
   *  there is no code the distinguishes them, the species chosen
   *  is strictly dependent on the ordering of adjacent units.
   *
   * @param zone
   * @param evu
   * @return
   */
  private static VegetativeType regenCommon(RegionalZone zone, Evu evu) {
    AdjacentData[]   adjacentData;
    Hashtable        seedSource = null;
    int              zoneId;
    Integer          tmpAcres;
    int              acres;
    Species[]        sortedKeys, key = null;
    int[]            value = null;
    Integer          keyValueIndex;
    int              index = 0;
    VegetativeType   newState = null;
    HabitatTypeGroup htGrp;
    Area             area = Simpplle.currentArea;
    Evu              adj;
    Enumeration      e;
    int              i,j;
    int              numAdj;
    int              cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    adjacentData = evu.getAdjacentData();
    numAdj        = adjacentData.length;
    htGrp         = evu.getHabitatTypeGroup();


    if (numAdj > 0) {
      seedSource = new Hashtable(numAdj);
      key        = new Species[numAdj];
      value      = new int[numAdj];
      index      = 0;

      for(i=0;i<key.length;i++) {
        key[i] = null;
        value[i] = -1;
      }
    }

    for(i=0;i<numAdj;i++) {
      adj = adjacentData[i].evu;

      if (adj.producingSeed()) {
        // Do not want current because doNextState may have already happened
        // for this adj unit, in which case we would be getting the wrong Species.
        VegSimStateData state = adj.getState(cStep-1);
        if (state == null) { continue; }

        Species species = state.getVeg().getSpecies();
        keyValueIndex = (Integer) seedSource.get(species);
        if (keyValueIndex == null) {
          acres         = adj.getAcres();
          keyValueIndex = new Integer(index);
          seedSource.put(species,keyValueIndex);
          index++;
        }
        else {
          acres = value[keyValueIndex.intValue()] + adj.getAcres();
        }
        key[keyValueIndex.intValue()]   = species;
        value[keyValueIndex.intValue()] = acres;
      }
    }

    if ((seedSource != null) && (seedSource.size() != 0)) {
      Species tmpSpecies;
      // Larch takes takes precedence over everything so look for it first.
      for (i=0; i<key.length; i++) {
        tmpSpecies = key[i];
        if (tmpSpecies == Species.L) {
          newState = htGrp.getSeedSapState(tmpSpecies);
          if (newState != null) { return newState; }
        }
      }
      // Now see if there are any PP or PP_DF present.
      // If PP_DF remove DF component, if not match try PP_DF
      for (i=0; i<key.length; i++) {
        tmpSpecies = key[i];
        if (tmpSpecies == Species.PP || tmpSpecies == Species.PP_DF) {
          newState = htGrp.getSeedSapState(Species.PP);
          if (newState == null && tmpSpecies == Species.PP_DF) {
            newState = htGrp.getSeedSapState(tmpSpecies);
          }
          if (newState != null) { return newState; }
        }
      }

      // Find species with most acres since we didn't find
      // any of the species in the above searches.
      Utility.sort(key,value);
      sortedKeys = key;

      for(i=0;i<sortedKeys.length;i++) {
        if (sortedKeys[i] == null) {continue;}

        newState = htGrp.getSeedSapState(sortedKeys[i]);
        if (newState != null) {
          return newState;
        }
      }
    }

    // Seed source must be empty or we couldn't find a matching
    // SS State. In either case do the following.
    zoneId = zone.getId();
    if (zoneId == ValidZones.WESTSIDE_REGION_ONE ||
        zoneId == ValidZones.EASTSIDE_REGION_ONE ||
        zoneId == ValidZones.TETON ||
        zoneId == ValidZones.NORTHERN_CENTRAL_ROCKIES ||
        zoneId == ValidZones.SIERRA_NEVADA ||
        zoneId == ValidZones.SOUTHERN_CALIFORNIA ||
        zoneId == ValidZones.GILA ||
        zoneId == ValidZones.SOUTH_CENTRAL_ALASKA) {
      VegSimStateData state = evu.getState();
      if (state == null) { return newState; }

      newState = state.getVeg().getProcessNextState(Process.findInstance(ProcessType.SUCCESSION));
      if (newState == null) {
        newState = state.getVeg();
      }
      return newState;
    }
    else {
      newState = htGrp.getVegetativeType("NS/NS/0");
      return newState;
    }
  }
}

