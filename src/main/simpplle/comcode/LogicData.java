/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.IOException;
import java.util.ArrayList;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.io.Externalizable;
import java.util.HashMap;

/**
 * This is an abstract class for LogicData, itself an extension of AbstractLogicData.
 * It provides many of the methods used in the logic data classes throughout OpenSimpplle.
 * While it cannot be instantiated directly, it is subclassed by many other classes.
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller</p>
 */

public abstract class LogicData extends AbstractLogicData implements Externalizable {
  static final long serialVersionUID = -2751426285950107341L;
  static final int  version          = 12;

//  public String printAll() {
//    StringBuffer buf = new StringBuffer();
//    
//    buf.append(ecoGroupList.toString());
//    buf.append(",");
//    buf.append(speciesLifeforms.toString());
//    buf.append(speciesResistance.toString());
//    buf.append(speciesList.toString());
//    buf.append(sizeClassStructure.toString());
//    buf.append(sizeClassList.toString());
//    buf.append(densityList.toString());
//    buf.append(processList.toString());
//    buf.append(processTSteps);
//    buf.append(treatmentList.toString());
//    buf.append(treatmentTSteps);
//    buf.append(season.toString());
//    buf.append(moistureList.toString());
//    buf.append(moistureCountTS);
//    buf.append(moistureNumTS);
//    buf.append(tempList.toString());
//    buf.append(tempCountTS);
//    buf.append(tempNumTS);
//    
//    return buf.toString();
//  }
  public ArrayList<SimpplleType>  ecoGroupList;
  public String                   ecoGroupDesc;
  public boolean                  defaultEcoGroupDesc;

  public ArrayList<Lifeform>        speciesLifeforms;
  public ArrayList<FireResistance>  speciesResistance;
  public ArrayList<SimpplleType>    speciesList;
  public String                     speciesDescription;
  public boolean                    defaultSpeciesDesc;

  public  ArrayList<Structure>      sizeClassStructure;
  public  ArrayList<SimpplleType>   sizeClassList;
  public  String                    sizeClassDesc;
  public  boolean                   defaultSizeClassDesc;

  public  ArrayList<SimpplleType>  densityList;
  public  String                   densityDesc;
  public  boolean                  defaultDensityDesc;

  public  ArrayList<SimpplleType>  processList;
  public  Integer                  processTSteps;
  public  String                   processDesc;
  public  boolean                  defaultProcessDesc;
  public  boolean                  processInclusiveTimeSteps;
  public  boolean                  processAnyExcept;

  public  ArrayList<SimpplleType>  treatmentList;
  public  Integer                  treatmentTSteps;
  public  String                   treatmentDesc;
  public  boolean                  defaultTreatmentDesc;
  public  boolean                  treatmentInclusiveTimeSteps;
  public  boolean                  treatmentAnyExcept;

  public Season season;

  public  ArrayList<Climate.Moisture>    moistureList;
  public  int                            moistureCountTS;
  public  int                            moistureNumTS;

  public  ArrayList<Climate.Temperature> tempList;
  public  int                            tempCountTS;
  public  int                            tempNumTS;

  public HashMap<Species,Integer> trackingSpecies;
  public String                   trackingSpeciesDesc;
  public boolean                  defaultTrackingSpeciesDesc;

  public ArrayList<Ownership> ownershipList;
  public String               ownershipDesc;
  public boolean              defaultOwnershipDesc;

  public ArrayList<SpecialArea> specialAreaList;
  public String                 specialAreaDesc;
  public boolean                defaultSpecialAreaDesc;

  public ArrayList<Roads.Status> roadStatusList;
  public String                  roadStatusDesc;
  public boolean                 defaultRoadStatusDesc;
  
  public ArrayList<Trails.Status> trailStatusList;
  public String                   trailStatusDesc;
  public boolean                  defaultTrailStatusDesc;
  
  public ArrayList<Landtype>  landtypeList;
  public String               landtypeDesc;
  public boolean              defaultLandtypeDesc;
  /**
   * Constructor for Logic Data abstract class. This will inititialize a series of arraylists, and descriptions for objects in OpenSimpplle which could
   * can have logic data for them.  
   */
  protected LogicData() {
    ecoGroupList          = new ArrayList<SimpplleType>();
    speciesLifeforms      = new ArrayList<Lifeform>();
    speciesResistance     = new ArrayList<FireResistance>();
    speciesList           = new ArrayList<SimpplleType>();
    sizeClassList         = new ArrayList<SimpplleType>();
    sizeClassStructure    = new ArrayList<Structure>();
    densityList           = new ArrayList<SimpplleType>();
    processList           = new ArrayList<SimpplleType>();
    treatmentList         = new ArrayList<SimpplleType>();
    processTSteps         = 1;
    treatmentTSteps       = 1;
    processInclusiveTimeSteps   = false;
    treatmentInclusiveTimeSteps = false;
    treatmentAnyExcept          = false;
    processAnyExcept            = false;

    ownershipList = new ArrayList<Ownership>();
    specialAreaList = new ArrayList<SpecialArea>();

    speciesDescription = "[]";
    sizeClassDesc      = "[]";
    densityDesc        = "[]";
    processDesc        = "[]";
    treatmentDesc      = "[]";
    ecoGroupDesc      = "[]";
    trackingSpeciesDesc = "{}";
    ownershipDesc = "[]";
    specialAreaDesc = "[]";
    roadStatusDesc  = "[]";
    trailStatusDesc = "[]";
    landtypeDesc    = "[]";

    defaultSpeciesDesc         = true;
    defaultSizeClassDesc       = true;
    defaultDensityDesc         = true;
    defaultProcessDesc         = true;
    defaultTreatmentDesc       = true;
    defaultEcoGroupDesc        = true;
    defaultTrackingSpeciesDesc = true;
    defaultOwnershipDesc       = true;
    defaultSpecialAreaDesc     = true;
    defaultRoadStatusDesc      = true;
    defaultTrailStatusDesc     = true;
    defaultLandtypeDesc        = true;

    season          = Season.YEAR;

    moistureList = new ArrayList<Climate.Moisture>();
    moistureCountTS = 1;
    moistureNumTS   = 1;

    tempList     = new ArrayList<Climate.Temperature>();
    tempCountTS = 1;
    tempNumTS   = 1;

    trackingSpecies = new HashMap<Species,Integer>();
    
    roadStatusList = new ArrayList<Roads.Status>();
    trailStatusList = new ArrayList<Trails.Status>();
    
    landtypeList = new ArrayList<Landtype>();
  }

  public abstract AbstractLogicData duplicate();
  public void duplicate(LogicData logicData) {
    super.duplicate(logicData);
    logicData.ecoGroupList        = new ArrayList<SimpplleType>(ecoGroupList);
    logicData.ecoGroupDesc        = ecoGroupDesc;
    logicData.defaultEcoGroupDesc = defaultEcoGroupDesc;

    logicData.speciesLifeforms    = new ArrayList<Lifeform>(speciesLifeforms);
    logicData.speciesResistance   = new ArrayList<FireResistance>(speciesResistance);
    logicData.speciesList         = new ArrayList<SimpplleType>(speciesList);
    logicData.speciesDescription  = speciesDescription;
    logicData.defaultSpeciesDesc  = defaultSpeciesDesc;

    logicData.sizeClassStructure   = new ArrayList<Structure>(sizeClassStructure);
    logicData.sizeClassList        = new ArrayList<SimpplleType>(sizeClassList);
    logicData.sizeClassDesc        = sizeClassDesc;
    logicData.defaultSizeClassDesc = defaultSizeClassDesc;

    logicData.densityList        = new ArrayList<SimpplleType>(densityList);
    logicData.densityDesc        = densityDesc;
    logicData.defaultDensityDesc = defaultDensityDesc;

    logicData.processList        = new ArrayList<SimpplleType>(processList);
    logicData.processTSteps      = processTSteps;
    logicData.processDesc        = processDesc;
    logicData.defaultProcessDesc = defaultProcessDesc;
    logicData.processInclusiveTimeSteps = processInclusiveTimeSteps;
    logicData.processAnyExcept          = processAnyExcept;

    logicData.treatmentList        = new ArrayList<SimpplleType>(treatmentList);
    logicData.treatmentTSteps      = treatmentTSteps;
    logicData.treatmentDesc        = treatmentDesc;
    logicData.defaultTreatmentDesc = defaultTreatmentDesc;
    logicData.treatmentInclusiveTimeSteps = treatmentInclusiveTimeSteps;
    logicData.treatmentAnyExcept   = treatmentAnyExcept;


    logicData.season = season;

    logicData.moistureList = new ArrayList<Climate.Moisture>(moistureList);
    logicData.tempList     = new ArrayList<Climate.Temperature>(tempList);

    logicData.trackingSpecies            = new HashMap<Species,Integer>(trackingSpecies);
    logicData.trackingSpeciesDesc        = trackingSpeciesDesc;
    logicData.defaultTrackingSpeciesDesc = defaultTrackingSpeciesDesc;

    logicData.ownershipList = new ArrayList<Ownership>(ownershipList);
    logicData.ownershipDesc = ownershipDesc;
    logicData.defaultOwnershipDesc = defaultOwnershipDesc;

    logicData.specialAreaList = new ArrayList<SpecialArea>(specialAreaList);
    logicData.specialAreaDesc = specialAreaDesc;
    logicData.defaultSpecialAreaDesc = defaultSpecialAreaDesc;
    
    logicData.roadStatusList = new ArrayList<Roads.Status>(roadStatusList);
    logicData.roadStatusDesc = roadStatusDesc;
    logicData.defaultRoadStatusDesc = defaultRoadStatusDesc;
    
    logicData.trailStatusList = new ArrayList<Trails.Status>(trailStatusList);
    logicData.trailStatusDesc = trailStatusDesc;
    logicData.defaultTrailStatusDesc = defaultTrailStatusDesc;
    
    logicData.landtypeList = new ArrayList<Landtype>(landtypeList);
    logicData.landtypeDesc = landtypeDesc;
    logicData.defaultLandtypeDesc = defaultLandtypeDesc;
  }
  /**
   * Method to get past time steps for a process
   * @return
   */
  public Integer getProcessPastTimeSteps() { return processTSteps; } 
  /**
   * Returns if the intermediate time steps are to be included
   * @return
   */
  public boolean getProcessInclusiveTimeSteps() {
    return processInclusiveTimeSteps;
  }
/**
 * Gets the past time steps.  
 * @return
 */
  public Integer getTreatmentPastTimeSteps() { return treatmentTSteps; }
 /**
  *This allows users to decide whether they want to include intermediate time steps in simulation.   
  * @return true if include intermediate time steps.  
  */
  public boolean getTreatmentInclusiveTimeSteps() {
    return treatmentInclusiveTimeSteps;
  }
/**
 * Sets the past time steps for a process and marks system knowledge changed
 * @param ts time steps
 */
  public void setProcessPastTimeSteps(Integer ts) {
    if (processTSteps != ts) {
      processTSteps = ts;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * Sets the inclusive time step boolean.  This is to determine if intermediate time steps are to be evaluated.  
 * @param inclusiveTimeSteps
 */
  public void setProcessInclusiveTimeSteps(boolean inclusiveTimeSteps) {
    if (processInclusiveTimeSteps != inclusiveTimeSteps) {
      processInclusiveTimeSteps = inclusiveTimeSteps;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * Gets the boolean processAnyExcept.  This is used in the GUI to select certain processes to be eliminated.  
 * @return boolean variable. 
 */
  public boolean getProcessAnyExcept() {
    return processAnyExcept;
  }
  /**
   * Sets the process exception, if different than current boolean.  
   * This is used in the GUI to allow user to select processes to be eliminated from consideration.
   * @param anyExcept
   */
  public void setProcessAnyExcept(boolean anyExcept) {
    if (processAnyExcept != anyExcept) {
      processAnyExcept = anyExcept;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * Sets treatment past time steps, if different from current treatment time steps.  
 * @param ts time steps
 */
  public void setTreatmentPastTimeSteps(Integer ts) {
    if (treatmentTSteps != ts) {
      treatmentTSteps = ts;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Sets treatment inclusive time steps variable, if different from current boolean value.  This is used to allow user to select whether they want to include 
   * intermediate time steps.  
   * @param inclusiveTimeSteps
   */
  public void setTreatmentInclusiveTimeSteps(boolean inclusiveTimeSteps) {
    if (treatmentInclusiveTimeSteps != inclusiveTimeSteps) {
      treatmentInclusiveTimeSteps = inclusiveTimeSteps;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * Gets the boolean value for treatmentAnyExcept.  This allows users to select certain treatments to be excluded.  
 * @return true if treatment is to be excluded.  
 */
  public boolean getTreatmentAnyExcept() {
    return treatmentAnyExcept;
  }
  /**
   * Sets the treatmentAnyExcept variable, if different from current boolean value.  This boolean allows users to select certain treatments to be excluded.
   * @param anyExcept
   */
  public void setTreatmentAnyExcept(boolean anyExcept) {
    if (treatmentAnyExcept != anyExcept) {
      treatmentAnyExcept = anyExcept;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * Returns the object at at a particular column.  Choices for this are ecoGroupDesc;
 * speciesDescription, sizeClassDesc, densityDesc, processDesc, treatmentDesc, season, moistureList, tempList, trackingSpeciesDesc, ownershipDesc,
 * specialAreaDesc, roadStatusDesc, trailStatusDesc, landtypeDesc,
 */
  public Object getValueAt(int col) {
    switch (col) {
      case BaseLogic.ECO_GROUP_COL:        return ecoGroupDesc;
      case BaseLogic.SPECIES_COL:          return speciesDescription;
      case BaseLogic.SIZE_CLASS_COL:       return sizeClassDesc;
      case BaseLogic.DENSITY_COL:          return densityDesc;
      case BaseLogic.PROCESS_COL:          return processDesc;
      case BaseLogic.TREATMENT_COL:        return treatmentDesc;
      case BaseLogic.SEASON_COL:           return season;
      case BaseLogic.MOISTURE_COL:         return moistureList;
      case BaseLogic.TEMP_COL:             return tempList;
      case BaseLogic.TRACKING_SPECIES_COL: return trackingSpeciesDesc;
      case BaseLogic.OWNERSHIP_COL:        return ownershipDesc;
      case BaseLogic.SPECIAL_AREA_COL:     return specialAreaDesc;
      case BaseLogic.ROAD_STATUS_COL:      return roadStatusDesc;
      case BaseLogic.TRAIL_STATUS_COL:     return trailStatusDesc;
      case BaseLogic.LANDTYPE_COL:         return landtypeDesc;

      default: return "";
    }
  }
  /**
   * Sets the value at a particular column.  The base logic columns that are defined within the logic data object are
   * ECO_GROUP_COL,SPECIES_COL,SIZE_CLASS_COL,DENSITY_COL,PROCESS_COL, TREATMENT_COL,SEASON_COL,MOISTURE_COL,TEMP_COL,TRACKING_SPECIES_COL,
   * OWNERSHIP_COL, SPECIAL_AREA_COL, ROAD_STATUS_COL, TRAIL_STATUS_COL, LANDTYPE_COL 
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case BaseLogic.ECO_GROUP_COL:        break; // set in table editor
      case BaseLogic.SPECIES_COL:          break; // set in table editor
      case BaseLogic.SIZE_CLASS_COL:       break; // set in table editor
      case BaseLogic.DENSITY_COL:          break; // set in table editor
      case BaseLogic.PROCESS_COL:          break; // set in table editor
      case BaseLogic.TREATMENT_COL:        break; // set in table editor
      case BaseLogic.SEASON_COL:
        if (season != (Season)value) {
          season = (Season) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case BaseLogic.MOISTURE_COL:         break;
      case BaseLogic.TEMP_COL:             break;
      case BaseLogic.TRACKING_SPECIES_COL: break;
      case BaseLogic.OWNERSHIP_COL:        break;
      case BaseLogic.SPECIAL_AREA_COL:     break;
      case BaseLogic.ROAD_STATUS_COL:      break;
      case BaseLogic.TRAIL_STATUS_COL:     break;
      case BaseLogic.LANDTYPE_COL:         break; // set in table editor
      default:
    }
  }
/**
 * Gets the ownership, special area, road status, trail status, or landtype arraylists at a particular column id based on input index.  
 * 
 */
  public Object getListValueAt(int listIndex, int col) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:    return ownershipList.get(listIndex);
      case BaseLogic.SPECIAL_AREA_COL: return specialAreaList.get(listIndex);
      case BaseLogic.ROAD_STATUS_COL:  return roadStatusList.get(listIndex);
      case BaseLogic.TRAIL_STATUS_COL: return trailStatusList.get(listIndex);
      case BaseLogic.LANDTYPE_COL:     return landtypeList.get(listIndex);

      default: return null;
    }
  }
  public void addListValueAt(int col, Object value) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:
        ownershipList.add((Ownership)value);
        break;
      case BaseLogic.SPECIAL_AREA_COL:
        specialAreaList.add((SpecialArea)value);
        break;
      case BaseLogic.ROAD_STATUS_COL:
        roadStatusList.add((Roads.Status)value);
        break;
      case BaseLogic.TRAIL_STATUS_COL:
        trailStatusList.add((Trails.Status)value);
        break;
      case BaseLogic.LANDTYPE_COL:
        landtypeList.add((Landtype)value);
        break;

      default: return;
    }

  }
  /**
   * removes a object from the Ownership, Special Area, Road Status, Trail Status, or Landtype lists.
   */
  public void removeListValueAt(int col, Object value) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:
        ownershipList.remove((Ownership)value);
        break;
      case BaseLogic.SPECIAL_AREA_COL:
        specialAreaList.remove((SpecialArea)value);
        break;
      case BaseLogic.ROAD_STATUS_COL:
        roadStatusList.remove((Roads.Status)value);
        break;
      case BaseLogic.TRAIL_STATUS_COL:
        trailStatusList.remove((Trails.Status)value);
        break;
      case BaseLogic.LANDTYPE_COL:
        landtypeList.remove((Landtype)value);
        break;

      default: return;
    }
  }
/**
 * Gets the size of ArrayList for either ownership, specialArea, road status, trail status, landtype.  
 * The size of these arrays represents the row count.
 * 
 */
  public int getListRowCount(int col) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:    return ownershipList.size();
      case BaseLogic.SPECIAL_AREA_COL: return specialAreaList.size();
      case BaseLogic.ROAD_STATUS_COL:  return roadStatusList.size();
      case BaseLogic.TRAIL_STATUS_COL: return trailStatusList.size();
      case BaseLogic.LANDTYPE_COL:     return landtypeList.size();

      default: return 0;
    }
  }
/**
 * Checks to see if the parameter object for either ownership, special area, road status, trail status or landtype is in the corresponding 
 * arraylist.  
 */
  public boolean hasListValue(int col, Object value) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:
        return ownershipList.contains((Ownership)value);
      case BaseLogic.SPECIAL_AREA_COL:
        return specialAreaList.contains((SpecialArea)value);
      case BaseLogic.ROAD_STATUS_COL:
        return roadStatusList.contains((Roads.Status)value);
      case BaseLogic.TRAIL_STATUS_COL:
        return trailStatusList.contains((Trails.Status)value);
      case BaseLogic.LANDTYPE_COL:
        return landtypeList.contains((Landtype)value);

      default: return false;
    }
  }
/**
 * Based on the column ID passed in will return either empty string or description of ownership, special area, road status, trail status, or landtype
 * @param col
 * @return 
 */
  public String getListDescription(int col) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:    return ownershipDesc;
      case BaseLogic.SPECIAL_AREA_COL: return specialAreaDesc;
      case BaseLogic.ROAD_STATUS_COL:  return roadStatusDesc;
      case BaseLogic.TRAIL_STATUS_COL: return trailStatusDesc;
      case BaseLogic.LANDTYPE_COL:     return landtypeDesc;
      default: return "";
    }
  }
/**
 * Checks if the column id object logic data is set to its default description.  IN this constructor this is set by default to true, can be changed by other methods.  
 * @param col the column ID
 * @return true if logic data is set to default description.  
 */
  public boolean isDefaultListDescription(int col) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:    return defaultOwnershipDesc;
      case BaseLogic.SPECIAL_AREA_COL: return defaultSpecialAreaDesc;
      case BaseLogic.ROAD_STATUS_COL:  return defaultRoadStatusDesc;
      case BaseLogic.TRAIL_STATUS_COL: return defaultTrailStatusDesc;
      case BaseLogic.LANDTYPE_COL:     return defaultLandtypeDesc;
      default: return true;
    }
  }
/**
 * method to get description for ownership, special area description, road status, trail status, and/or land type 
 * @param col
 * @param desc
 */
  public void setListDescription(int col, String desc) {
    switch (col) {
      case BaseLogic.OWNERSHIP_COL:
        if (desc == null || desc.length() == 0) {
          setOwnershipDescDefault();
          return;
        }
        ownershipDesc = desc;
        defaultOwnershipDesc = false;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case BaseLogic.SPECIAL_AREA_COL:
        if (desc == null || desc.length() == 0) {
          setSpecialAreaDescDefault();
          return;
        }
        specialAreaDesc = desc;
        defaultSpecialAreaDesc = false;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case BaseLogic.ROAD_STATUS_COL:
        if (desc == null || desc.length() == 0) {
          setRoadStatusDescDefault();
          return;
        }
        roadStatusDesc = desc;
        defaultRoadStatusDesc = false;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case BaseLogic.TRAIL_STATUS_COL:
        if (desc == null || desc.length() == 0) {
          setTrailStatusDescDefault();
          return;
        }
        trailStatusDesc = desc;
        defaultTrailStatusDesc = false;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      case BaseLogic.LANDTYPE_COL:
        if (desc == null || desc.length() == 0) {
          setLandtypeDescDefault();
          return;
        }
        landtypeDesc = desc;
        defaultLandtypeDesc = false;
        SystemKnowledge.markChanged(sysKnowKind);
        break;
      default: return;
    }
  }
/**
 * Gets the Simpplle types arraylist.  Choices for simpplle Types are Group, Species, Size Class, Density, Process, Treatment or null.  
 * @param kind the simpplle type
 * @return the arraylist containing a particular kind of simpplle type objects.  
 */
  protected ArrayList<SimpplleType> getList(SimpplleType.Types kind) {
    switch (kind) {
      case GROUP:      return ecoGroupList;
      case SPECIES:    return speciesList;
      case SIZE_CLASS: return sizeClassList;
      case DENSITY:    return densityList;
      case PROCESS:    return processList;
      case TREATMENT:  return treatmentList;
      default: return null;
    }
  }
/**
 * Adds a lifeform to the species lifeforms arraylist.  Types of lifeforms are Trees, Shrubs, Herbacious, Agriculture, and NA (no classification).  
 */
  public void addLifeform(Lifeform lifeform) {
    if (speciesLifeforms.contains(lifeform) == false) {
      speciesLifeforms.add(lifeform);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes a lifeform from the species lifeform arraylist, if it contains that lifeform.  
   * Types of lifeforms are Trees, Shrubs, Herbacious, Agriculture, and NA (no classification).
   * @param lifeform
   */
  public void removeLifeform(Lifeform lifeform) {
    if (speciesLifeforms.contains(lifeform)) {
      speciesLifeforms.remove(lifeform);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Checks if specieslifeforms arraylist has a particular lifeform.
   * Types of lifeforms are Trees, Shrubs, Herbacious, Agriculture, and NA (no classification
   * @param lifeform
   * @return
   */
  public boolean hasLifeform(Lifeform lifeform) {
    return speciesLifeforms != null && speciesLifeforms.contains(lifeform);
  }
/**
 * Adds a tracking species to tracing species hashmap.  Key for hash map is species.  Percent stored as int.  
 * @param species key to tracking species hashmap, it is the species being tracked.  
 * @param percent the integer percent.  
 */
  public void addTrackingSpecies(Species species, int percent) {
    Integer value = trackingSpecies.get(species);
    if (value == null || value != percent) {
      trackingSpecies.put(species,percent);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes a tracking species form the tracking species hashmap.  This is keyed by species, and will therefore eliminate the key and value (percent)
   * @param species
   */
  public void removeTrackingSpecies(Species species) {
    if (trackingSpecies.get(species) != null) {
      trackingSpecies.remove(species);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Checks if the species passed in parameter is in the tracking species hashmap
   * @param species species being evaluated whether it is in the tracking species hash map
   * @return true if in tracking species hashmap
   */
  public boolean hasTrackingSpecies(Species species) {
    return (trackingSpecies != null &&
            trackingSpecies.get(species) != null);
  }
  /**
   * Gets the tracking species percent.  This is the value in the tracking species hashmap which is keyed by the species.  
   * @param species this is the key into the hashmap which gets the tracking species percent
   * @return tracking species percent
   */
  public int getTrackingSpeciesPercent(Species species) {
    return trackingSpecies.get(species);
  }
/**
 * Adds a fire resistance to the speciesResistance arraylist, if not already in there.  Then marks the system knowledge changed.  
 * @param resistance
 */
  public void addFireResistance(FireResistance resistance) {
    if (speciesResistance.contains(resistance) == false) {
      speciesResistance.add(resistance);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes the fire resistance object from the speciesResistance arraylist, if it is in there.  Then marks the system knowleged changed)
   * @param resistance
   */
  public void removeFireResistance(FireResistance resistance) {
    if (speciesResistance.contains(resistance)) {
      speciesResistance.remove(resistance);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Gets the speciesReisistance arraylist.  This is a fire resistance arraylist, and represents the species resistance to fire.  
   * @return
   */
  public ArrayList<FireResistance> getFireResistance() {
    return speciesResistance;
  }
  /**
   * Checks if a fire resistance object is in the species resistance arraylist
   * @param resistance the fire resistance object being checked.
   * @return true if species resistance arraylist contains the fire resistance object
   */
  public boolean hasFireResistance(FireResistance resistance) {
    return speciesResistance != null && speciesResistance.contains(resistance);
  }

  // *** Size Class ***
  // ******************
  /**
   * Adds the Size class structure to the size class structure arraylist, if it is not already in there.  Then marks system knowledge changed.
   * Choices for size class structure are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY
   * @param structure
   */
  public void addStructure(Structure structure) {
    if (sizeClassStructure.contains(structure) == false) {
      sizeClassStructure.add(structure);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes a size class structure from the size class structure arraylist, if it is in there.  Then marks system knowledge changed.
   * @param structure the size class structure.  
   * Choices for size class structure are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY
   */
  public void removeStructure(Structure structure) {
    if (sizeClassStructure.contains(structure)) {
      sizeClassStructure.remove(structure);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Gets the size class structure arraylist.  
   * Choices for size class structure are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY
   * @return size class structure arraylist
   */
  public ArrayList<Structure> getStructure() {
    return sizeClassStructure;
  }
  /**
   * Checks if the size class structure is in the logic data size class structure arraylist.
   * Choices for size class structure are NON_FOREST, SINGLE_STORY, MULTIPLE_STORY
   * @param structure size class structure
   * @return true if size class structure is in logic data size class structure arraylist
   */
  public boolean hasStructure(Structure structure) {
    return sizeClassStructure != null && sizeClassStructure.contains(structure);
  }
/**
 * Checks if a simpplle type item is in the simpplle type logic data arraylist for a particular simpplle type.  
 * Choices for simplle type kinds are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
 * @param item simpplle type to be evaluated.  
 * @param kind the simpple type kind
 * @return true if simpplle type item is in the simpplle type arraylist.
 */
  public boolean isMemberSimpplleType(SimpplleType item, SimpplleType.Types kind) {
    ArrayList<SimpplleType> theList = getList(kind);
    return theList.contains(item);
  }
/**
 * Adds the simpplle type object passed in to the logic data simpplle types arraylist that corresponds to a particular simpplle type kind.
 * Choices for simpplle types kind are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP   
 * @param item the simpplle type object to be added
 * @param kind the simpplle type kind choices are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
 */
  public void addSimpplleType(SimpplleType item, SimpplleType.Types kind) {
    ArrayList<SimpplleType> theList = getList(kind);
    if (isMemberSimpplleType(item,kind) == false) {
      theList.add(item);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Removes a simpplle types item from its 
   * @param item
   * @param kind
   */
  public void removeSimpplleType(SimpplleType item, SimpplleType.Types kind) {
    ArrayList<SimpplleType> theList = getList(kind);
    if (theList.contains(item)) {
      theList.remove(item);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * Sets the description to either the passed in description or default description for species, size class, density, process, treatment and gropu
   * This will be passed in from the GUI.
   * @param desc description text taken from GUI
   * @param kind species, size class, density, process, treatment or group.
   */
  public void setDescription(String desc, SimpplleType.Types kind) {
    if (desc == null || desc.length() == 0) {
      setDescriptionDefault(kind);
      return;
    }

    switch (kind) {
      case SPECIES:
        speciesDescription = desc;
        defaultSpeciesDesc = false;
        break;
      case SIZE_CLASS:
        sizeClassDesc = desc;
        defaultSizeClassDesc = false;
        break;
      case DENSITY:
        densityDesc = desc;
        defaultDensityDesc = false;
        break;
      case PROCESS:
        processDesc = desc;
        defaultProcessDesc = false;
        break;
      case TREATMENT:
        treatmentDesc = desc;
        defaultTreatmentDesc = false;
        break;
      case GROUP:
        ecoGroupDesc = desc;
        defaultEcoGroupDesc = false;
        break;
      default:
        return;
    }
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Gets the simpplle type object description.  
   * Choices for simpplle types are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
   * @param kind simplle type to have its description gotten
   * @return particular simpplle type description
   */
  public String getDescription(SimpplleType.Types kind) {
    switch (kind) {
      case SPECIES:
        return speciesDescription;
      case SIZE_CLASS:
        return sizeClassDesc;
      case DENSITY:
        return densityDesc;
      case PROCESS:
        return processDesc;
      case TREATMENT:
        return treatmentDesc;
      case GROUP:
        return ecoGroupDesc;
      default:
        return "";
    }
  }
  /**
   * Checks if the simplle types kind logic data is the default description for corresponding simpplle type.  
   * Choices for simpplle types kind are SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, GROUP
   * @param kind
   * @return true if logic data is the default description
   */
  public boolean isDefaultDescription(SimpplleType.Types kind) {
    switch (kind) {
      case SPECIES:
        return defaultSpeciesDesc;
      case SIZE_CLASS:
        return defaultSizeClassDesc;
      case DENSITY:
        return defaultDensityDesc;
      case PROCESS:
        return defaultProcessDesc;
      case TREATMENT:
        return defaultTreatmentDesc;
      case GROUP:
        return defaultEcoGroupDesc;
      default:
        return false;
    }
  }

  // *** Tracking Species Description ***
  // ************************************
/**
 * Sets the tracking species description if different from default tracking species description.  
 * Turns off the default tracking species boolean and marks system knowledge changed.    
 * @param desc
 */
  public void setTrackingSpeciesDesc(String desc) {
    if (desc == null || desc.length() == 0) {
      setTrackingSpeciesDescDefault();
      return;
    }

    trackingSpeciesDesc = desc;
    defaultTrackingSpeciesDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Gets the string tracking species description.  
   * @return tracking species description
   */
  public String getTrackingSpeciesDesc() { return trackingSpeciesDesc; }
/**
 * Checks if default tracking species description boolean is set to true
 * @return true if tracking species description still set to true
 */
  public boolean isDefaultTrackingSpeciesDesc() {
    return defaultTrackingSpeciesDesc;
  }
  /**
   * Sets the tracking species default description.  
   */
  public void setTrackingSpeciesDescDefault() {
    StringBuffer strBuf = new StringBuffer("{");
    if (trackingSpecies != null) {
      int i=0;
      for (Species sp : trackingSpecies.keySet()) {
        if (i > 0) { strBuf.append(", "); }
        int pct = trackingSpecies.get(sp);
        strBuf.append(sp.toString());
        strBuf.append(" >= ");
        strBuf.append(pct);
        i++;
      }
      strBuf.append("}");
      trackingSpeciesDesc = strBuf.toString();
    }
    else {
      trackingSpeciesDesc = "{}";
    }

    defaultTrackingSpeciesDesc = true;
    SystemKnowledge.markChanged(sysKnowKind);
  }
/**
 * Sets the default ownership description to the ownership list toString (all the ownerships in there).  If the list is empty, returns "{".  
 */
  public void setOwnershipDescDefault() {
    if (ownershipList != null) {
      ownershipDesc = ownershipList.toString();
    }
    else {
      ownershipDesc = "{";
    }

    defaultOwnershipDesc = true;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Sets the default special area description to the ownership list toString (all the ownerships in there).  If the list is empty, returns "{".  
   */
  public void setSpecialAreaDescDefault() {
    if (specialAreaList != null) {
      specialAreaDesc = specialAreaList.toString();
    }
    else {
      specialAreaDesc = "{";
    }

    defaultSpecialAreaDesc = true;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Sets the default road status to the ownership list toString (all the ownerships in there).  If the list is empty, returns "{".  
   */
  public void setRoadStatusDescDefault() {
    if (roadStatusList != null) {
      roadStatusDesc = roadStatusList.toString();
    }
    else {
      roadStatusDesc = "{";
    }

    defaultRoadStatusDesc = true;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Sets the default trail status description to the ownership list toString (all the ownerships in there).  If the list is empty, returns "{".  
   */
  public void setTrailStatusDescDefault() {
    if (trailStatusList != null) {
      trailStatusDesc = trailStatusList.toString();
    }
    else {
      trailStatusDesc = "{";
    }

    defaultTrailStatusDesc = true;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * Sets the default landtype description to the ownership list toString (all the ownerships in there).  If the list is empty, returns "{".  
   */
  public void setLandtypeDescDefault() {
    if (landtypeList != null) {
      landtypeDesc = landtypeList.toString();
    }
    else {
      landtypeDesc = "{";
    }

    defaultLandtypeDesc = true;
    SystemKnowledge.markChanged(sysKnowKind);
  }
/**
 * Sets the default description for either SPECIES, SIZE_CLASS, DENSITY, PROCESS, TREATMENT, or GROUP, the simpplle types in logic data.  
 * @param kind simpplle types
 */
  public void setDescriptionDefault(SimpplleType.Types kind) {
    switch (kind) {
      case SPECIES:
      {
        StringBuffer strBuf = new StringBuffer("{");
        if (speciesLifeforms != null && speciesLifeforms.size() > 0) {
          strBuf.append(speciesLifeforms.toString());
        }
        if (speciesResistance != null && speciesResistance.size() > 0) {
          strBuf.append(speciesResistance.toString());
        }
        if (speciesList != null && speciesList.size() > 0) {
          strBuf.append(speciesList.toString());
        }
        strBuf.append("}");
        speciesDescription = strBuf.toString();
        defaultSpeciesDesc = true;
        break;
      }
      case SIZE_CLASS:
      {
        StringBuffer strBuf = new StringBuffer("{");
        if (sizeClassStructure != null && sizeClassStructure.size() > 0) {
          strBuf.append(sizeClassStructure.toString());
        }
        if (sizeClassList != null && sizeClassList.size() > 0) {
          strBuf.append(sizeClassList.toString());
        }
        strBuf.append("}");
        sizeClassDesc = strBuf.toString();
        defaultSizeClassDesc = true;
        break;
      }
      case DENSITY:
        densityDesc = (densityList != null) ? densityList.toString() : "[]";
        defaultDensityDesc = true;
        break;
      case PROCESS:
        processDesc = (processList != null) ? processList.toString() : "[]";
        defaultProcessDesc = true;
        break;
      case TREATMENT:
        treatmentDesc = (treatmentList != null) ? treatmentList.toString() : "[]";
        defaultTreatmentDesc = true;
        break;
      case GROUP:
        ecoGroupDesc = (ecoGroupList != null) ? ecoGroupList.toString() : "[]";
        defaultEcoGroupDesc = true;
        break;
      default:
        return;
    }
    SystemKnowledge.markChanged(sysKnowKind);
  }

  // *** End Species Description ***
  // *******************************

  // *** Temperature/Moisture ***
  // ****************************
  public void addTemperature(Climate.Temperature temp) {
    if (tempList.contains(temp) == false) {
      tempList.add(temp);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  public void removeTemperature(Climate.Temperature temp) {
    if (tempList.contains(temp)) {
      tempList.remove(temp);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  public ArrayList<Climate.Temperature> getTemperatureList() { return tempList; }

  public int getTempCountTimeStep() { return tempCountTS; }
  public int getTempNumTimeStep()   { return tempNumTS; }

  public void setTempCountTimeStep(int ts) { tempCountTS = ts; }
  public void setTempNumTimeStep(int ts)   { tempNumTS = ts; }

  public void addMoisture(Climate.Moisture moisture) {
    if (moistureList.contains(moisture) == false) {
      moistureList.add(moisture);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  public void removeMoisture(Climate.Moisture moisture) {
    if (moistureList.contains(moisture)) {
      moistureList.remove(moisture);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  public ArrayList<Climate.Moisture> getMoistureList() { return moistureList; }

  public int getMoistureCountTimeStep() { return moistureCountTS; }
  public int getMoistureNumTimeStep()   { return moistureNumTS; }

  public void setMoistureCountTimeStep(int ts) { moistureCountTS = ts; }
  public void setMoistureNumTimeStep(int ts)   { moistureNumTS = ts; }

/**
 * Method to compare two arraylists.  This is called amongst other things to compare logic rules.  
 * First compares size, since this is the most resource efficient way, then goes through the individual elements of the list and compares each.  
 * @param list1 first list
 * @param list2 second list
 * @return true if the two lists are equal.  version, 
 */
  protected static boolean listsEqual(ArrayList<SimpplleType> list1, ArrayList<SimpplleType> list2) {
    if (list1.size() != list2.size()) { return false; }

    for (int i=0; i<list1.size(); i++) {
      if (list1.get(i).equals(list2.get(i)) == false) { return false; }
    }

    return true;
  }

  public void sortLists() {
    Collections.sort(ecoGroupList);
    Collections.sort(speciesResistance);
    Collections.sort(speciesList);
    Collections.sort(sizeClassList);
    Collections.sort(densityList);
    Collections.sort(processList);
    Collections.sort(treatmentList);

    setDescriptionDefault(SimpplleType.Types.SPECIES);
    setDescriptionDefault(SimpplleType.Types.SIZE_CLASS);
    setDescriptionDefault(SimpplleType.Types.DENSITY);
    setDescriptionDefault(SimpplleType.Types.PROCESS);
    setDescriptionDefault(SimpplleType.Types.TREATMENT);
    setDescriptionDefault(SimpplleType.Types.GROUP);
    setTrackingSpeciesDescDefault();
  }
  protected boolean rulesCompatible(LogicData rule) {
    return ((listsEqual(processList,rule.processList)) &&
            (listsEqual(sizeClassList,rule.sizeClassList)) &&
            (listsEqual(densityList,rule.densityList)) &&
            (listsEqual(treatmentList,rule.treatmentList)) &&
            (listsEqual(speciesList,rule.speciesList)) &&
            season == rule.season);
  }

  public void AddRuleData(LogicData logicData) {
    Utility.combineLists(speciesResistance,logicData.speciesResistance);
    setDescriptionDefault(SimpplleType.Types.SPECIES);
  }
/**
 * Reads from external object input source the logic data.  These are stored in following order
 * ecoGroupList, ecoGroupDesc, defaultEcoGroupDesc, speciesLifeforms, speciesResistance, speciesList, speciesDescription, defaultSpeciesDesc,
 * sizeClassStructure, sizeClassList, sizeClassDesc, defaultSizeClassDesc, densityList, densityDesc, defaultDensityDesc,processList, 
 * processTSteps, processDesc, defaultProcessDesc,processInclusiveTimeSteps, processAnyExcept, treatmentList, treatmentTSteps, treatmentDesc,
 * defaultTreatmentDesc, treatmentInclusiveTimeSteps, treatmentAnyExcept, season.toString(), moistureList, moistureCountTS, moistureNumTS,
 * tempList, tempCountTS, tempNumTS, trackingSpeciesDesc, defaultTrackingSpeciesDesc, trackingSpecies.size(), all the species and tracking species,
 * ownershipList, ownershipDesc, defaultOwnershipDesc,specialAreaList, specialAreaDesc, defaultSpecialAreaDesc, roadStatusList,roadStatusDesc,
 * defaultRoadStatusDesc, trailStatusList, trailStatusDesc, defaultTrailStatusDesc, landtypeList, landtypeDesc, defaultLandtypeDesc 
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    if (version > 5) {
      super.readExternal(in);
    }
    else if (version > 3) {
      String knowledge = (String)in.readObject();
      SystemKnowledge.setKnowledgeSource(sysKnowKind,knowledge);
    }

    if (version > 1) {
      ecoGroupList = (ArrayList<SimpplleType>)in.readObject();
    }
    if (version > 2) {
      ecoGroupDesc = (String) in.readObject();
      defaultEcoGroupDesc = in.readBoolean();
    }

    speciesLifeforms  = (ArrayList<Lifeform>)in.readObject();
    speciesResistance = (ArrayList<FireResistance>)in.readObject();

    speciesList = (ArrayList<SimpplleType>) in.readObject();

    speciesDescription = (String)in.readObject();
    defaultSpeciesDesc = in.readBoolean();

    sizeClassStructure  = (ArrayList<Structure>)in.readObject();
    sizeClassList       = (ArrayList<SimpplleType>)in.readObject();
    sizeClassDesc       = (String)in.readObject();
    defaultSizeClassDesc  = in.readBoolean();

    densityList       = (ArrayList<SimpplleType>)in.readObject();
    densityDesc       = (String)in.readObject();
    defaultDensityDesc  = in.readBoolean();

    processList       = (ArrayList<SimpplleType>)in.readObject();
    processTSteps     = in.readInt();
    processDesc       = (String)in.readObject();
    defaultProcessDesc  = in.readBoolean();
    processInclusiveTimeSteps = false;
    if (version > 7) {
      processInclusiveTimeSteps = in.readBoolean();
    }
    if (version > 8) {
      processAnyExcept = in.readBoolean();
    }

    treatmentList       = (ArrayList<SimpplleType>)in.readObject();
    treatmentTSteps     = in.readInt();
    treatmentDesc       = (String)in.readObject();
    defaultTreatmentDesc  = in.readBoolean();
    treatmentInclusiveTimeSteps = false;
    if (version > 7) {
      treatmentInclusiveTimeSteps = in.readBoolean();
      treatmentAnyExcept        = in.readBoolean();
    }

    season = Season.valueOf((String)in.readObject());

    if (version > 1) {
      moistureList = (ArrayList<Climate.Moisture>)in.readObject();
      if (version > 4) {
        moistureCountTS = in.readInt();
        moistureNumTS   = in.readInt();
      }
      tempList = (ArrayList<Climate.Temperature>)in.readObject();
      if (version > 4) {
        tempCountTS = in.readInt();
        tempNumTS   = in.readInt();
      }
    }

    if (version > 3) {
      trackingSpecies.clear();
      trackingSpeciesDesc = (String)in.readObject();
      defaultTrackingSpeciesDesc = in.readBoolean();
      int size = in.readInt();
      for (int i=0; i<size; i++) {
        Species species = (Species)in.readObject();
        int     percent = in.readInt();
        trackingSpecies.put(species,percent);
      }

      if (version <= 5 && defaultTrackingSpeciesDesc) {
        setTrackingSpeciesDescDefault();
      }
    }

    if (version > 6) {
      ownershipList        = (ArrayList<Ownership>)in.readObject();
      ownershipDesc        = (String)in.readObject();
      defaultOwnershipDesc = in.readBoolean();
    }
    
    // made this version 12 because SIMPPLLE 3.0 increased the version
    // to 10 and then 11 prior to the change in 2.5, so using a special
    // version 12 that 3.0 will handle special so that 3.0 can load these
    // files properly.
    if (version == 12) {
      specialAreaList = (ArrayList<SpecialArea>)in.readObject();
      specialAreaDesc = (String)in.readObject();
      defaultSpecialAreaDesc = in.readBoolean();
      
      roadStatusList  = (ArrayList<Roads.Status>)in.readObject();
      roadStatusDesc  = (String)in.readObject();
      defaultRoadStatusDesc = in.readBoolean();
      
      trailStatusList = (ArrayList<Trails.Status>)in.readObject();
      trailStatusDesc = (String)in.readObject();
      defaultTrailStatusDesc = in.readBoolean();
      
      landtypeList    = (ArrayList<Landtype>)in.readObject();
      landtypeDesc    = (String)in.readObject();
      defaultLandtypeDesc = in.readBoolean();
    }
  }
/**
 * Writes logic data out to external source.  The order this logic data is written out is 
 * ecoGroupList, ecoGroupDesc, defaultEcoGroupDesc, speciesLifeforms, speciesResistance, speciesList, speciesDescription, defaultSpeciesDesc,
 * sizeClassStructure, sizeClassList, sizeClassDesc, defaultSizeClassDesc, densityList, densityDesc, defaultDensityDesc,processList, 
 * processTSteps, processDesc, defaultProcessDesc,processInclusiveTimeSteps, processAnyExcept, treatmentList, treatmentTSteps, treatmentDesc,
 * defaultTreatmentDesc, treatmentInclusiveTimeSteps, treatmentAnyExcept, season.toString(), moistureList, moistureCountTS, moistureNumTS,
 * tempList, tempCountTS, tempNumTS, trackingSpeciesDesc, defaultTrackingSpeciesDesc, trackingSpecies.size(), all the species and tracking species,
 * ownershipList, ownershipDesc, defaultOwnershipDesc,specialAreaList, specialAreaDesc, defaultSpecialAreaDesc, roadStatusList,roadStatusDesc,
 * defaultRoadStatusDesc, trailStatusList, trailStatusDesc, defaultTrailStatusDesc, landtypeList, landtypeDesc, defaultLandtypeDesc
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    out.writeObject(ecoGroupList);
    out.writeObject(ecoGroupDesc);
    out.writeBoolean(defaultEcoGroupDesc);
    out.writeObject(speciesLifeforms);
    out.writeObject(speciesResistance);
    out.writeObject(speciesList);

    out.writeObject(speciesDescription);
    out.writeBoolean(defaultSpeciesDesc);

    out.writeObject(sizeClassStructure);
    out.writeObject(sizeClassList);
    out.writeObject(sizeClassDesc);
    out.writeBoolean(defaultSizeClassDesc);

    out.writeObject(densityList);
    out.writeObject(densityDesc);
    out.writeBoolean(defaultDensityDesc);

    out.writeObject(processList);
    out.writeInt(processTSteps);
    out.writeObject(processDesc);
    out.writeBoolean(defaultProcessDesc);
    out.writeBoolean(processInclusiveTimeSteps);
    out.writeBoolean(processAnyExcept);

    out.writeObject(treatmentList);
    out.writeInt(treatmentTSteps);
    out.writeObject(treatmentDesc);
    out.writeBoolean(defaultTreatmentDesc);
    out.writeBoolean(treatmentInclusiveTimeSteps);
    out.writeBoolean(treatmentAnyExcept);

    out.writeObject(season.toString());

    out.writeObject(moistureList);
    out.writeInt(moistureCountTS);
    out.writeInt(moistureNumTS);

    out.writeObject(tempList);
    out.writeInt(tempCountTS);
    out.writeInt(tempNumTS);

    out.writeObject(trackingSpeciesDesc);
    out.writeBoolean(defaultTrackingSpeciesDesc);
    out.writeInt(trackingSpecies.size());
    for (Species species : trackingSpecies.keySet()) {
      out.writeObject(species);
      out.writeInt(trackingSpecies.get(species));
    }

    out.writeObject(ownershipList);
    out.writeObject(ownershipDesc);
    out.writeBoolean(defaultOwnershipDesc);
    
    out.writeObject(specialAreaList);
    out.writeObject(specialAreaDesc);
    out.writeBoolean(defaultSpecialAreaDesc);

    out.writeObject(roadStatusList);
    out.writeObject(roadStatusDesc);
    out.writeBoolean(defaultRoadStatusDesc);

    out.writeObject(trailStatusList);
    out.writeObject(trailStatusDesc);
    out.writeBoolean(defaultTrailStatusDesc);

    out.writeObject(landtypeList);
    out.writeObject(landtypeDesc);
    out.writeBoolean(defaultLandtypeDesc);
  }

  public boolean isLifeformMatch(Lifeform lifeform) {
    if (speciesLifeforms == null || speciesLifeforms.size() == 0) {
      return true;
    }
    return (speciesLifeforms.contains(lifeform));
  }

  private boolean isSpeciesMatch(FireResistance resistance, Species species) {
    if ((speciesResistance == null || speciesResistance.size() == 0) &&
        (speciesLifeforms == null || speciesLifeforms.size() == 0) &&
        (speciesList == null || speciesList.size() == 0)) {
      return true;
    }

    if (speciesResistance != null && speciesResistance.size() > 0 &&
        speciesResistance.contains(resistance)) {
      return true;
    }
    if (speciesLifeforms != null && speciesLifeforms.size() > 0 &&
        speciesLifeforms.contains(species.getLifeform())) {
      return true;
    }
    if (speciesList != null && speciesList.size() > 0 &&
        speciesList.contains(species)) {
      return true;
    }
    return false;

  }
  private boolean isSizeClassMatch(SizeClass sizeClass) {
    if ((sizeClassStructure == null || sizeClassStructure.size() == 0) &&
        (sizeClassList == null || sizeClassList.size() == 0)) {
      return true;
    }

    if (sizeClassStructure != null && sizeClassStructure.size() > 0 &&
        sizeClassStructure.contains(sizeClass.getStructure())) {
      return true;
    }
    if (sizeClassList != null && sizeClassList.size() > 0 &&
        sizeClassList.contains(sizeClass)) {
      return true;
    }
    return false;
  }
  // *** Simulation code ***
  // ***********************
  /**
   * return the true if the rule matches the data in the given evu.
   * @param evu Evu
   * @return boolean
   */
  public boolean isMatch(Evu evu) {
    FireResistance resistance =
        FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu);
    return isMatch(resistance,evu,null,null);
  }

  public boolean isMatch(Evu evu, Lifeform life) {
    int cStep = Simulation.getCurrentTimeStep();
    return isMatch(evu,cStep,life);
  }
  public boolean isMatch(Evu evu,Integer tStep, Lifeform lifeform) {
    FireResistance resistance =
        FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);
    return isMatch(resistance,evu,tStep,lifeform);
  }
  public boolean isMatch(FireResistance resistance, Evu evu) {
    return isMatch(resistance,evu,null,null);
  }
  public boolean isMatch(FireResistance resistance, Evu evu, Lifeform life) {
    int cStep = Simulation.getCurrentTimeStep();
    return isMatch(resistance,evu,cStep,life);
  }
  public boolean isMatch(FireResistance resistance, Evu evu,
                         Integer tStep, Lifeform lifeform)
  {
    VegSimStateData state;
    if (lifeform != null && tStep != null) {
      state = evu.getState(tStep,lifeform);
    }
    else if (lifeform != null && tStep == null) {
      state = evu.getState(lifeform);
    }
    else if (lifeform == null && tStep != null) {
      state = evu.getState(tStep);
    }
    else {
      state = evu.getState();
    }
    
    if (state == null) {
      return false;
    }
    
    return isMatch(resistance,evu,tStep,lifeform,state.getVegType());
  }

  public boolean isMatch(Evu evu,
                         VegSimStateData state, int tStep, Lifeform lifeform) {

    FireResistance resistance =
        FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    boolean isMatch=false;
    int procTS = processTSteps;
    if (processInclusiveTimeSteps) {
      procTS = 0;
    }
    for (int pts=procTS; pts<=processTSteps; pts++) {
      ProcessType process = evu.getLastLifePastProcess(pts,lifeform);

      isMatch = isMatch(resistance,evu,tStep,process,state.getVeg(),false,state.getSeason());
      if (isMatch) { return true; }
    }
    return false;
  }

  public boolean isMatch(FireResistance resistance, Evu evu,
                         Integer tStep, Lifeform lifeform,
                         VegetativeType vegType) {

    int cTime;
    Simulation simulation = Simulation.getInstance();
    if ((simulation != null && simulation.isSimulationRunning()) || tStep == null) {
      cTime = simulation.getCurrentTimeStep();
    }
    else {
      cTime = tStep;
    }

    boolean isMatch=false;
    int procTS = processTSteps;
    if (processInclusiveTimeSteps) {
      procTS = 0;
    }
    for (int pts=procTS; pts<=processTSteps; pts++) {
      // This is to make sure the search dialog works correctly.
      if (Simpplle.getCurrentZone().isWyoming() &&
          (simulation != null  && simulation.isSimulationRunning() == false)) {
        VegSimStateData state = evu.getState(cTime - pts, lifeform,this.season);
        ProcessType process = (state != null ? state.getProcess() : ProcessType.NONE);
        Season      season  = (state != null ? state.getSeason() : Season.YEAR);

        isMatch = isMatch(resistance,evu,tStep,process,vegType,false,season);
        if (isMatch) { return true; }
      }
      else {
        VegSimStateData state = evu.getState(cTime - pts, lifeform);
        ProcessType process = (state != null ? state.getProcess() : ProcessType.NONE);
        Season      season  = (state != null ? state.getSeason() : Season.YEAR);

        isMatch = isMatch(resistance,evu,tStep,process,vegType,false,season);
        if (isMatch) { return true; }
      }
    }

    return false;
  }

  private boolean isMatch(FireResistance resistance, Evu evu,
                         Integer tStep, ProcessType process,
                         VegetativeType vegType, boolean isSuccInLandscapeSeed,
                         Season unitSeason) {

    int cTime;
    Simulation simulation = Simulation.getInstance();
    if ((simulation != null && simulation.isSimulationRunning()) || tStep == null) {
      cTime = simulation.getCurrentTimeStep();
    }
    else {
      cTime = tStep;
    }

    if (treatmentList != null && treatmentList.size() > 0) {
      boolean match = false;
      int treatTS = treatmentTSteps;
      if (treatmentInclusiveTimeSteps) {
        treatTS = 0;
      }
      for (int pts = treatTS; pts <= treatmentTSteps; pts++) {
        Treatment treat = evu.getTreatment(cTime - pts);
        TreatmentType treatType = TreatmentType.NONE;
        if (treat != null) { treatType = treat.getType(); }

        match = treatmentList.contains(treatType);
        if (treatmentAnyExcept) { match = !match; }

        if (match) { break; }
      }
      if (!match) { return false; }
    }


    HabitatTypeGroupType group = evu.getHabitatTypeGroup().getType();
    if ((ecoGroupList != null && ecoGroupList.size() > 0 &&
         ecoGroupList.contains(group) == false)) {
      return false;
    }

    if (isSpeciesMatch(resistance,vegType.getSpecies()) == false) {
      return false;
    }
    if (isSizeClassMatch(vegType.getSizeClass()) == false) {
      return false;
    }

    if ((densityList != null && densityList.size() > 0 &&
         densityList.contains(vegType.getDensity()) == false)) {
      return false;
    }

    if (process == ProcessType.SUCCESSION &&
        (Simpplle.getCurrentZone().isWyoming() ||
         Simpplle.getCurrentZone() instanceof ColoradoFrontRange ||
         Simpplle.getCurrentZone() instanceof ColoradoPlateau)) {
      if (Simpplle.getClimate().isWetSuccession()) {
        process = ProcessType.WET_SUCCESSION;
      }
      if (Simpplle.getClimate().isDrySuccession()) {
        process = ProcessType.DRY_SUCCESSION;
      }
    }
    if ((processList != null && processList.size() > 0 &&
         processList.contains(process) == false)) {
      if (!processAnyExcept) { return false; }
    }

    Season currentSeason;
    if (simulation != null  && simulation.isSimulationRunning()) {
      currentSeason = Simpplle.getCurrentSimulation().getCurrentSeason();
      if (season != Season.YEAR && season != currentSeason) {
        return false;
      }
    }
    else if (simulation != null) {
      if (season != unitSeason) {
        return false;
      }
      currentSeason = unitSeason;
    }
    else {
      currentSeason = Season.YEAR;
    }

    if (isSuccInLandscapeSeed) {
      cTime = Simulation.getCurrentTimeStep();
    }

    if ((moistureList != null && moistureList.size() > 0)) {
      int count = 0;
      for (int i=0; i<moistureNumTS; i++) {
        Climate.Moisture moisture = Simpplle.getClimate().getMoisture(cTime-i,currentSeason);
        if (moistureList.contains(moisture)) {
          count++;
        }
      }
      if (count < moistureCountTS) return false;
    }

    if ((tempList != null && tempList.size() > 0 )) {
      int count = 0;
      for (int i=0; i<tempNumTS; i++) {
        Climate.Temperature temp = Simpplle.getClimate().getTemperature(cTime-i,currentSeason);
        if (tempList.contains(temp)) {
          count++;
        }
      }
      if (count < tempCountTS) return false;
    }

    if (ownershipList != null && ownershipList.size() > 0) {
      Ownership owner = Ownership.get(evu.getOwnership());
      if (ownershipList.contains(owner) == false) {
        return false;
      }
    }
    if (specialAreaList != null && specialAreaList.size() > 0) {
      SpecialArea sa = SpecialArea.get(evu.getSpecialArea());
      if (specialAreaList.contains(sa) == false) {
        return false;
      }
    }
    if (roadStatusList != null && roadStatusList.size() > 0) {
      Roads.Status status = evu.getRoadStatusNew();
      if (roadStatusList.contains(status) == false) {
        return false;
      }
    }
    if (trailStatusList != null && trailStatusList.size() > 0) {
      Trails.Status status = evu.getTrailStatus();
      if (trailStatusList.contains(status) == false) {
        return false;
      }
    }
    if (landtypeList != null && landtypeList.size() > 0) {
      Landtype landtype = Landtype.get(evu.getLandtype());
      if (landtypeList.contains(landtype) == false) {
        return false;
      }
    }

    if (trackingSpecies.size() == 0) {
      return true;
    }

    if (trackingSpeciesMatch(evu, tStep)) {
      return true;
    }

    // If we reached here then we had tracking species but found no match
    // Therefore no match for this rule.
    return false;
  }

  private boolean trackingSpeciesMatch(Evu evu, Integer tStep) {
    if (tStep == null) {
      tStep = Simulation.getCurrentTimeStep();
    }

    Lifeform[] lives = Lifeform.getAllValues();
    for (int i=0; i<lives.length; i++) {
      if (evu.hasLifeform(lives[i],tStep) == false) { continue; }

      VegSimStateData state = evu.getState(tStep,lives[i]);

      for (Species sp : trackingSpecies.keySet()) {
        InclusionRuleSpecies trkSp = InclusionRuleSpecies.get(sp.toString());
        if (trkSp == null) { continue; }
        int minPercent = trackingSpecies.get(sp);

        float pct = state.getSpeciesPercent(trkSp);

        if (pct > minPercent) {
          return true;
        }
      }
    }
    return false;
  }

  public void doAction(Evu evu, Lifeform lifeform) {}
  public void doAction(Evu evu) {}

  public ArrayList getPossibleValues(int col) { return null; }

}
