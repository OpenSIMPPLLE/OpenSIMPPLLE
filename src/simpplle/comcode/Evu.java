package simpplle.comcode;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.List;

import org.apache.commons.collections.*;
import org.apache.commons.collections.keyvalue.*;
import org.apache.commons.collections.map.*;
import org.hibernate.*;
import simpplle.comcode.Climate.*;

/**
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public final class Evu extends NaturalElement implements Externalizable {

  static final long serialVersionUID        = -4593527729379592789L;
  static final int  version                 = 8;
  static final int  accumDataVersion        = 1;
  static final int  spatialRelationsVersion = 1;

  private ArrayList<ExistingLandUnit>    assocLandUnits;
  private ArrayList<ExistingAquaticUnit> assocAquaticUnits;
  private ArrayList<Roads>               assocRoadUnits;
  private ArrayList<Trails>              assocTrailUnits;

  private HabitatTypeGroup htGrp;

  /**
   * This is a bit ugly but only way I could think to do this since generic
   * arrays are not allowed.
   */
  // private ArrayList<HashMap<Lifeform,ArrayList<VegSimStateData>>> simulatedStatesNew;
  // one element for each lifeform, lifeform can be determine from species.
  // private HashMap<Lifeform,ArrayList<VegSimStateData>> vegStateNew;

  // Index: n=currentStep, n-1=currentStep-1, etc)
  // Size:  Simulation.pastTimeStepsInMemory + 1;
  // Key 1: Lifeform, Key 2: Season
  // Value: VegSimStateData
  private Flat3Map[] simData;

  // Key 1: Lifeform, Key 2: Season
  // We could end up with initial conditions with Season,
  // by running seasonally and using simulation results as new initial Conditions.
  private Flat3Map initialState;

  private Lifeform dominantLifeform;

  private String           unitNumber;
  private AdjacentData[]   adjacentData;

  private static ProcessProbability[] tempProcessProb;
  private ProcessProbability[]        needLaterProcessProb;

  private String           ownership;
  private Roads.Status     roadStatus;
  private int              ignitionProb;
  private Fmz              fmz;
  private String           specialArea;

  private String           source;
  private String           associatedLandtype;
  private int[]            location;
  private Vector           timberVolume;
  private Vector           volumeRemovals;
  private Vector           treatment;  // simulation
  private MtnPineBeetleHazard.Hazard lpMpbHazard;
  private MtnPineBeetleHazard.Hazard ppMpbHazard;

  private double latitude;
  private double longitude;

  // ** Simulation Related **
  private boolean        producingSeed;
  private Climate.Season fireSeason;
  private short          fireSeasonProb;
  private int[]          regenDelay = new int[Lifeform.getAllValues().length];
  private boolean[]      recentRegenDelay = new boolean[regenDelay.length];
  private SizeClass      cycleSizeClass=null;
  private int            cycleSizeClassCount=0;

  /**
   * Creates a Water Unit Data class.
   * This has three variables - a boolean for permanence of present water, water source - whether aquatic unit or water Evu,
   * Evu closest to water source.
   */
  public static class WaterUnitData {
    public boolean        permanentWater;
    public NaturalElement unit;           // Water Source (Aquatic Unit or Water Evu)
    public Evu            waterEvu;       // Unit closest to water source
  }

  private static ArrayList<ArrayList<WaterUnitData>> waterUnits;

  public static class RoadUnitData {
    public Roads road;
    public Evu   evu;
  }

  public static List<RoadUnitData> roadUnits = new ArrayList<>();

  // Outer array is index by time step.
  // Inner array is for future use.  If road status changes and we
  // use road status to determine qualifying roads then nearest road
  // may change, but we will still need to keep track of the original
  // closest road in case its status changes to available again.
  public ArrayList<ArrayList<RoadUnitData>> nearestRoad;

  public static double MAX_ROAD_DIST = 2 * 5280; // 2 Miles in Feet

  /**
   * creates a Trail Unit Data class with two variables for trail and evu.
   *
   *
   */
  public static class TrailUnitData {
    public Trails trail;
    public Evu    evu;
  }

  public static ArrayList<TrailUnitData> trailUnits = new ArrayList<TrailUnitData>();

  public ArrayList<ArrayList<TrailUnitData>> nearestTrail;

  public static double MAX_TRAIL_DIST=5280*2; // 2 Miles in Feet

  private static boolean haveHighSpruceBeetle=false;

  // Making this static avoids constant allocation/deallocation.
  private static LinkedList cumulProb = new LinkedList();
  private static ProcessType[] spruceBeetles = new ProcessType[] { ProcessType.LIGHT_SB, ProcessType.MEDIUM_SB, ProcessType.HIGH_SB };

  // Used to avoid excess temporaries.
  private static HashSet lifeformSet = new HashSet();
  private static ArrayList<Lifeform> lifeformList = new ArrayList<Lifeform>();

  // If a lifeform is removed keep its state here so that we don't have
  // to search the database for the information.
  private VegSimStateData[] lastLife;
  private Flat3Map lastLifeProcessHistory;

  private int lastFireTimeStep=-1;

  // Used in determining frequencies
  public static final SimpplleType.Types SPECIES    = SimpplleType.SPECIES;
  public static final SimpplleType.Types SIZE_CLASS = SimpplleType.SIZE_CLASS;
  public static final SimpplleType.Types DENSITY    = SimpplleType.DENSITY;
  public static final SimpplleType.Types PROCESS    = SimpplleType.PROCESS;

  // used in probList;
  // *** These values cannot be changed now due to releasing a version
  // *** with the numbers rather than string written out.
  // *** unless I do coversion code of some sort. 24 July 2004.
  public static final int NOPROB = -1;
  public static final int D      = -2;
  public static final int L      = -3; // Locked in Process
  public static final int S      = -4; // Spread, Average Fire Spread
  public static final int SUPP   = -5; // Fire was supressed
  public static final int SE     = -6; // Extreme Fire Spread
  public static final int SFS    = -7; // Fire Spotting
  public static final int COMP   = -8; // Competition
  public static final int GAP    = -9; // Gap Process

  // For use in writing files
  public static final String D_STR       = "D";
  public static final String L_STR       = "L"; // Locked in Process
  public static final String S_STR       = "S"; // Spread, Average Fire Spread
  public static final String SFS_STR     = "SFS"; // Spread via Fire Spotting.
  public static final String SUPP_STR    = "SUPP"; // Fire was supressed
  public static final String SE_STR      = "SE"; // Extreme Fire Spread
  public static final String NOPROB_STR  = "NA"; // No Probability
  public static final String COMP_STR    = "COMP"; // Competition
  public static final String GAP_STR     = "GAP"; // Gap Process

  /**
   * Returns the road status at the current time step if the simulation is running. Otherwise the road status is
   * returned for the first time step if there is no simulation, or the last if the simulation is not running.
   * @return Road status
   */
  public Roads.Status getRoadStatusNew() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int ts = 0;
    if (simulation != null && simulation.isSimulationRunning()) {
      ts = simulation.getCurrentTimeStep();
    } else if (simulation != null) {
      ts = simulation.getNumTimeSteps();
    }

    return getRoadStatusNew(ts);

  }

  /**
   * Returns the road status at a specific time step.
   * @param ts Simulation time step
   * @return Road status
   */
  public Roads.Status getRoadStatusNew(int ts) {
    ArrayList<Roads> roads = getAssociatedRoadUnits();
    Roads road = null;
    if (roads != null && roads.size() > 0) {
      road = roads.get(0);
    }

    if (road != null) {
      return road.getSimStatus(ts);
    } else {
      return roadStatus;
    }
  }

  /**
   * Returns the trail status at the current time step if the simulation is running. Otherwise the trail status is
   * returned for the first time step if there is no simulation, or the last if the simulation is not running.
   * @return Trail status
   */
  public Trails.Status getTrailStatus() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int ts = 0;
    if (simulation != null && simulation.isSimulationRunning()) {
      ts = simulation.getCurrentTimeStep();
    } else if (simulation != null) {
      ts = simulation.getNumTimeSteps();
    }

    return getTrailStatus(ts);

  }

  /**
   * Returns the trail status at a specific time step.
   * @param ts Simulation time step
   * @return Trail status
   */
  public Trails.Status getTrailStatus(int ts) {
    ArrayList<Trails> trails = getAssociatedTrailUnits();
    Trails trail = null;
    if (trails != null && trails.size() > 0) {
      trail = trails.get(0);
    }

    if (trail != null) {
      return trail.getSimStatus(ts);
    } else {
      return Trails.Status.UNKNOWN;
    }
  }

  /**
   * Sets the road status of this EVU.
   * @param status Road status
   */
  public void setRoadStatus(Roads.Status status) {
    roadStatus = status;
  }

  // Adjacent Units data
  public static final int  POSITION = 0;
  public static final int  WIND     = 1;

  public static final char DOWNWIND = 'D';
  public static final char NO_WIND  = 'N';

  public static final char ABOVE    = 'A';
  public static final char BELOW    = 'B';
  public static final char NEXT_TO  = 'N';

  // Location
  public static final int X = 0;
  public static final int Y = 1;

  private static final ProcessType defaultInitialProcess = ProcessType.SUCCESSION;

  /**
   * Initializes fields to default values.
   */
  public Evu() {

    super();

    htGrp               = null;
    initialState        = null;
    simData             = null;
    unitNumber          = null;
    adjacentData        = null;
    acres               = 0;

    ownership           = null;
    roadStatus          = Roads.Status.UNKNOWN;
    ignitionProb        = 0;
    fmz                 = Simpplle.getCurrentZone().getDefaultFmz();
    specialArea         = null;

    source              = null;
    associatedLandtype  = null;
    location            = new int[] {-1, -1};
    timberVolume        = null;
    volumeRemovals      = null;
    treatment           = null;
    lpMpbHazard         = null;
    ppMpbHazard         = null;
    producingSeed       = false;
    dominantLifeform    = Lifeform.NA;
    latitude            = Double.NaN;
    longitude           = Double.NaN;

  }

  /**
   * Initializes the fields to default values and sets the ID
   * @param newId
   */
  public Evu(int newId) {
    this();
    id = newId;
  }

  /**
   * Returns true if the object is an Evu with a matching ID
   */
  public boolean equals(Object obj) {
    if ((obj != null) && (obj instanceof Evu)) {
      return id == ((Evu)obj).id;
    }
    return false;
  }

  /**
   * Returns a hash code, which is the Evu ID
   */
  public int hashCode() {
    return id;
  }

  /**
   * If simulation null or data is not to be discarded returns parameter time step, if simulation is running gets the current time step -1
   * @param timeStep time step to be indexed
   * @return
   */
  private int getSimDataIndex(int timeStep) {
    Simulation simulation = Simulation.getInstance();
    if (simulation == null ||
        simulation.isDiscardData() == false) {
      return timeStep;
    }

    int cStep;

    if (Simulation.getInstance().isSimulationRunning()) {
      cStep = simulation.getCurrentTimeStep();
    } else {
      cStep = simulation.getNumTimeSteps();
    }

    if (cStep <= simData.length) {
      return timeStep - 1;
    }

    int diff = cStep - timeStep;

    return (simData.length - 1) - diff;

  }

  public static boolean haveHighSpruceBeetle() { return haveHighSpruceBeetle; }

  public static ProcessType getDefaultInitialProcess() { return defaultInitialProcess; }

  /**
   * Check whether Habitat Type Group, Current State, Fire management zone, acres, and initial process are valid.
   * @return true if all checked are valid
   */
  public boolean isValid() {
    return(isHabitatTypeGroupValid() &&
           isCurrentStateValid()     &&
           isFmzValid()              &&
           isAcresValid()            &&
           isInitialProcessValid());
  }

  /**
   * Returns the slope of the associated land units, or zero if there are none.
   */
  public float getSlope() {
    if (assocLandUnits == null || assocLandUnits.size() == 0) return 0;
    return assocLandUnits.get(0).getSlope();
  }

  /**
   * Returns associated land unit arraylist
   * @return an array list of associated land units
   */
  public ArrayList<ExistingLandUnit> getAssociatedLandUnits() { return assocLandUnits; }

  /**
   * Adds an existing land unit (ELU) to the list of associated land units if it isn't already a member.
   * @param elu existing land unit
   */
  public void addAssociatedLandUnit(ExistingLandUnit elu)  {
    if (assocLandUnits == null) {
      assocLandUnits = new ArrayList<ExistingLandUnit>();
    }
    if (assocLandUnits.contains(elu) == false) {
      assocLandUnits.add(elu);
    }
  }

  /**
   * Returns true if this contains associated road units
   * @return true if there are roads
   */
  public boolean hasRoadUnits() {
    return (assocRoadUnits != null && assocRoadUnits.size() > 0);
  }

  /**
   * Returns an ArrayList of associated road units
   * @return ArrayList of associated roads
   */
  public ArrayList<Roads> getAssociatedRoadUnits() { return assocRoadUnits; }

  /**
   * Adds a road to the list of associated road units if it isn't already a member.
   * @param roadUnit road unit to be added
   */
  public void addAssociatedRoadUnit(Roads roadUnit)  {
    if (assocRoadUnits == null) {
      assocRoadUnits = new ArrayList<Roads>();
    }
    if (assocRoadUnits.contains(roadUnit) == false) {
      assocRoadUnits.add(roadUnit);
    }
  }

  /**
   * Checks if this EVU has any associated roads. If it does, then the first road is recorded in the RoadUnitData.
   * @param roadData Road unit data for recording the first associated road
   * @param onlyOpen Only include roads with an open status
   * @return True if there are associated road units
   */
  private boolean hasAssociatedRoadUnit(RoadUnitData roadData, boolean onlyOpen) {

    if (assocRoadUnits == null) return false;

    for (Roads road : assocRoadUnits) {

      if (onlyOpen && road.getSimStatus() != Roads.Status.OPEN) continue;

      roadData.road = road;

      return true;

    }

    return false;

  }

  /**
   * Returns true if this contains associated trail units
   * @return true if there are trails
   */
  public boolean hasTrailUnits() {
    return (assocTrailUnits != null && assocTrailUnits.size() > 0);
  }

  /**
   * Gets the arraylist of trails associated with Evu.
   * @return Arraylist of trails associated with Evu
   */
  public ArrayList<Trails> getAssociatedTrailUnits() { return assocTrailUnits; }

  /**
   * Adds a trail unit to the list of associated trail units if it isn't already a member.
   * @param trailUnit trail to be added
   */
  public void addAssociatedTrailUnit(Trails trailUnit)  {
    if (assocTrailUnits == null) {
      assocTrailUnits = new ArrayList<Trails>();
    }
    if (assocTrailUnits.contains(trailUnit) == false) {
      assocTrailUnits.add(trailUnit);
    }
  }

  /**
   * Checks if this EVU has any associated trails. If it does, then the first trail is recorded in the TrailUnitData.
   * @param trailData Trail unit data for recording the first associated trail
   * @param onlyOpen Only include trails with an open status
   * @return True if there are associated trail units
   */
  private boolean hasAssociatedTrailUnit(TrailUnitData trailData, boolean onlyOpen) {

    if (assocTrailUnits == null) return false;

    for (Trails trail : assocTrailUnits) {

      if (onlyOpen && trail.getSimStatus() != Trails.Status.OPEN) continue;

      trailData.trail = trail;

      return true;

    }

    return false;

  }

  /**
   * Calculates whether something is aquatic based on whether the species present in current state are water species.
   * @return true if is aquatic
   */
  private boolean isAquatic() {
    return (Species)getState(SimpplleType.SPECIES) == Species.WATER;
  }

  /**
   * Check if an Evu has an associated aquatic unit, based on whether there is associated aquatic units,
   * and whether an Eau has water present and that water is permanent.
   * @param isPermanentWater
   * @return
   */
  private boolean hasAssociatedAquaticUnit(WaterUnitData isPermanentWater) {
    if (assocAquaticUnits == null || assocAquaticUnits.size() == 0) {
      return false;
    }
    for(int i=0; i<assocAquaticUnits.size(); i++) {
      ExistingAquaticUnit eau = assocAquaticUnits.get(i);
      if (eau.isWaterPresent()) {
        isPermanentWater.permanentWater = eau.isPermanentWater();
        isPermanentWater.unit = eau;
        return true;
      }
    }
    isPermanentWater.permanentWater = false;
    isPermanentWater.unit = null;
    return false;
  }

  /**
   * Empties the associated aquatic unit arraylist.
   */
  public void clearAssociatedAquaticUnits() {
    if (assocAquaticUnits != null) assocAquaticUnits.clear();
  }

  /**
   * Gets the existing aquatic unit (EAU) arraylist containing associated aquatic units
   * @return arraylist with associated EAUs
   */
  public ArrayList<ExistingAquaticUnit> getAssociatedAquaticUnits() {
    return assocAquaticUnits;
  }

  /**
   * Adds an existing aquatic unit (EAU) to the list of associated aquatic units if it isn't already a member.
   * @param eau existing aquatic unit to be added
   */
  public void addAssociatedAquaticUnit(ExistingAquaticUnit eau) {
    if (assocAquaticUnits == null) {
      assocAquaticUnits = new ArrayList<ExistingAquaticUnit>();
    }
    if (assocAquaticUnits.contains(eau) == false) {
      assocAquaticUnits.add(eau);
    }
  }


  /**
   * Gets the string literal name of the Evu which includes EVU + the Evu ID.
   * @return a String, the Evu's name. (e.g. "EVU-1")
   */
  public String getName() { return "EVU-" + id; }

  /**
   * Gets the Evu's id.
   * @return the Evu id.
   */
  public int getId() { return id; }

  /**
   * Gets the X coordinate.
   * @return X coordinate
   */
  public int getLocationX() { return location[X]; }

  /**
   * Sets X coordinate of a specified location.
   * @param x the coordinate to be set
   */
  public void setLocationX(int x) { location[X] = x; }

  /**
   * Gets the Y coordinate of a specified location.
   * @return the location's Y coordinate
   */
  public int getLocationY() { return location[Y]; }

  /**
   * Sets Y coordinate of a specified location.
   * @param y the coordinate to be set
   */
  public void setLocationY(int y) { location[Y] = y; }

  /**
   * Gets a location, which is a Point with X and Y Coordinates
   * @return point (x,y) which represents a location
   */
  public Point getLocation() { return new Point(location[X],location[Y]); }

  /**
   * Gets the string literal of Land type
   * @return land type
   */
  public String getLandtype() { return associatedLandtype; }

  /**
   * Sets the land type of an Evu.
   * @param value the land type
   */
  public void setLandtype(String value) { associatedLandtype = value; }

  /**
   * Gets the Evu's HabitatTypeGroup.
   * @return a HabitatTypeGroup
   */
  public HabitatTypeGroup getHabitatTypeGroup () { return htGrp; }

  /**
   * This is used to set the Evu data to a group that doesn't exist. This allows the user to correct incorrect data.
   * @param htGrpStr the invalid habitat type group
   */
  private void setInvalidHabitatTypeGroup(String htGrpStr) {
    HabitatTypeGroup group = new HabitatTypeGroup(htGrpStr);
    setHabitatTypeGroup(group);
  }

  /**
   * Sets the habitat type group based on habitat group string and lifeform. If habitat group does not exists sets
   * passed habitat group as invalid. Else sets the habitat type group and gets the current vegetative simulation state
   * of a lifeform and sets the vegsimstate to groups vegetative type.
   * @param htGrpStr habitat type group
   * @param currentLife lifeform to be used get current vegetative state
   */
  public void setHabitatTypeGroup(String htGrpStr, Lifeform currentLife) {
    HabitatTypeGroup group;

    group = HabitatTypeGroup.findInstance(htGrpStr);

    if (group == null) {

      setInvalidHabitatTypeGroup(htGrpStr);

    } else {

      setHabitatTypeGroup(group);

      // State may have been invalid due to invalid group.
      // Need to check if that was the case.
      VegSimStateData state = getState(currentLife);
      VegetativeType newVeg = group.getVegetativeType(state.getVeg().toString());

      if (newVeg == null) {
        newVeg = VegetativeType.UNKNOWN;
      }

      if (state != null) {
        setState(newVeg);
      }
    }
  }

  /**
   * Sets the habitat type group.
   * @param group the habitat type group to be set
   */
  public void setHabitatTypeGroup(HabitatTypeGroup group) { htGrp = group; }

  /**
   * Check if the habitat type group is valid by passing to HabitatTypeGroups ifValid() method.
   * @return true if valid
   */
  public boolean isHabitatTypeGroupValid() {
    return (htGrp != null) ? htGrp.isValid() : false;
  }

  /**
   * Check if has lifeform passed is present in any seasons.
   * @param lifeform the lifeform to be checked
   * @param map the map which contains lifeforms and season
   * @return true if life form is present in any season
   */
  private boolean hasLifeformAnySeason(Lifeform lifeform, Flat3Map map) {
//    if (map == null) { return false; }
    Season[] seasons = Climate.allSeasons;
    for (int i=0; i<seasons.length; i++) {
      MultiKey key = LifeformSeasonKeys.getKey(lifeform,seasons[i]);
      if (map.containsKey(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Uses Hibernate to query database if life form is present in any season.
   * @param lifeform life form being evaluated
   * @param timeStep
   * @return true if life form is present in any season database.
   */
  private boolean hasLifeformAnySeasonDatabase(Lifeform lifeform, int timeStep) {
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("from VegSimStateData as state where");
    strBuf.append(" state.slink=");
    strBuf.append(getId());
    strBuf.append(" and state.lifeform=");
    strBuf.append(lifeform.getSimId());
    strBuf.append(" and state.timeStep=");
    strBuf.append(timeStep);
    strBuf.append(" and state.run=");
    strBuf.append(Simulation.getInstance().getCurrentRun());

    Session     session = DatabaseCreator.getSessionFactory().openSession();
    Query q = session.createQuery(strBuf.toString());
    strBuf = null;
    List totList = q.list();

    session.close();

    if (totList == null || totList.size() == 0) {
      return false;
    }
    return true;
  }
  /**
   * Check if simulation has a specific life form.
   * @param lifeform the life form to be evaluated
   * @return If there is a current simulation will check life form at current
   * time step, else will check life form at time step 0.
   */
  public boolean hasLifeform(Lifeform lifeform) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int cStep = (simulation != null) ? simulation.getCurrentTimeStep() : 0;
    return hasLifeform(lifeform,cStep);
  }

  /**
   * Check if an Evu has a specific life form, based on passed time step.
   * This is based on a present in any season query of the life from and the simulation state of passed time step.
   * @param lifeform the life form to be evaluated
   * @param timeStep the time step used to
   * @return true if simulation has life form.
   * @throws - runtime exception if index is <0.  This is not caught here or thrown...
   */
  public boolean hasLifeform(Lifeform lifeform, int timeStep) {
    if (timeStep == 0) {
      return hasLifeformAnySeason(lifeform,initialState);
    }

    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation != null && simData != null) {
      int index = getSimDataIndex(timeStep);
      if (index < 0) {
        throw new RuntimeException("Attempted access to unavailable time step");
      }

      if (index >= 0) {
        return hasLifeformAnySeason(lifeform,simData[index]);
      } else {
        return hasLifeformAnySeasonDatabase(lifeform, timeStep);
      }
    }
    else {
      return hasLifeformAnySeason(lifeform,initialState);
    }
  }

  /**
   * This method contains some deprecated variables.  As of now it will return only 0 as the default time step... which
   * will indicate initial state.
   * @return 0 indicating initial state (time step 0) is default time step.
   */
  private int determineDefaultTimeStep() {
    Simulation simulation = Simulation.getInstance();
    if (simData != null && simulation != null) {
      return (simulation.isSimulationRunning() ? simulation.getCurrentTimeStep() : simulation.getNumTimeSteps());
    } else {
      return 0;
    }
  }
  /**
   * Gets the most dominant vegetative state based on time step.
   * The order it checks for dominance are (does the state have:) Trees, Shrubs Herbacious, Agriculture, then NA
   * @param timeStep time step
   * @return
   */
  public VegSimStateData getStateMostDominant(int timeStep) {

    VegSimStateData state = getState(timeStep,Lifeform.TREES);

    if (state == null) state = getState(timeStep,Lifeform.SHRUBS);
    if (state == null) state = getState(timeStep,Lifeform.HERBACIOUS);
    if (state == null) state = getState(timeStep,Lifeform.AGRICULTURE);
    if (state == null) state = getState(timeStep,Lifeform.NA);

    return state;

  }

  /**
   * Returns a simpplle type state from the current vegetation state.
   *
   * @param kind kind of simpplle type state
   * @return a simpplle type value or null if there is no vegetation state
   */
  public SimpplleType getState (SimpplleType.Types kind) {
    VegSimStateData state = getState();
    if (state == null) return null;
    switch (kind) {
      case SPECIES:    return state.getVeg().getSpecies();
      case SIZE_CLASS: return state.getVeg().getSizeClass();
      case DENSITY:    return state.getVeg().getDensity();
      case PROCESS:    return state.getProcess();
    }
    return null;
  }

  /**
   * Gets the vegetative state based on the life form of evaluated area.
   *
   * @return the result of getState(lifeform)
   */
  public VegSimStateData getState() {
    return getState(Area.currentLifeform); // Why isn't this dominantLifeform like in the other methods?
  }

  /**
   * Gets the vegetative state based on the dominant life form and passed in time step.
   *
   * @param timeStep time step to check
   * @return the result of getState(timeStep, lifeform)
   */
  public VegSimStateData getState (int timeStep) {
    return getState(timeStep, dominantLifeform);
  }

  /**
   * Gets the vegetative state based on passed life form and default time step. If the life form is null, then the
   * dominant life form of this evu will be used.
   *
   * @param lifeform life form to check
   * @return the result of getState(timeStep, lifeform)
   */
  public VegSimStateData getState (Lifeform lifeform) {
    if (lifeform == null) lifeform = dominantLifeform;
    return getState(determineDefaultTimeStep(),lifeform);
  }

  /**
   * Gets the vegetative state based on passed time step and life form. If the life form is null, then the dominant
   * life form of this evu will be used. This goes through the season array in reverse until it finds a season with
   * a vegetative state.
   *
   * @param timeStep time step to check
   * @param lifeform life form to check
   * @return the result of getState(timeStep, lifeform, season)
   */
  public VegSimStateData getState (int timeStep, Lifeform lifeform) {

    if (lifeform == null) lifeform = dominantLifeform;

    Season[] seasons = Climate.allSeasons;
    for (int i=seasons.length-1; i>=0; i--) {
      VegSimStateData state = getState(timeStep,lifeform,seasons[i]);
      if (state != null) return state;
    }

    return null;

  }

  /**
   * Gets the simulation vegetative state based on passed time step, life form, and season. If the life form is null,
   * the area's current life form or dominant life form will be used. If the time step is 0, the initial state is
   * returned. If there is no vegetative state otherwise, then the database is queried if one exists.
   *
   * @param timeStep time step to check
   * @param lifeform life form to check
   * @param season season to check
   * @return vegetative simulation state
   */
  public VegSimStateData getState (int timeStep, Lifeform lifeform, Season season) {

    if (lifeform == null) lifeform = Area.currentLifeform;
    if (lifeform == null) lifeform = dominantLifeform;

    if (timeStep < 0) return null;
    if (timeStep > 0 && simData == null) return null;

    if (timeStep == 0) {

      if (initialState == null) return null;

      MultiKey key = LifeformSeasonKeys.getKey(lifeform,Season.YEAR);
      VegSimStateData foundState = (VegSimStateData)initialState.get(key);
      return foundState;

    }

    int simDataIndex = this.getSimDataIndex(timeStep);

    if (simDataIndex >= 0 && simData[simDataIndex] != null) {

      MultiKey key = LifeformSeasonKeys.getKey(lifeform,season);
      VegSimStateData foundState = (VegSimStateData)simData[simDataIndex].get(key);
      return foundState;

    } else { // Get From Database

      // Check first to see if the state we are looking for is in the lastLife variable
      // This does happen in the case of succession regen searching for in-landscape seed.

      VegSimStateData lastLifeState = lastLife[lifeform.getId()];

      if (lastLifeState               != null &&
          lastLifeState.getSlink()    == getId() &&
          lastLifeState.getLifeform() == lifeform &&
          lastLifeState.getTimeStep() == timeStep &&
          lastLifeState.getRun()      == Simulation.getInstance().getCurrentRun() &&
          lastLifeState.getSeason()   == season) {

        return lastLifeState;

      }

      if (Simulation.getInstance().getWriteDatabase() == false) return null;

      StringBuffer strBuf = new StringBuffer();
      strBuf.append("from VegSimStateData as state where");
      strBuf.append(" state.slink=");
      strBuf.append(getId());
      strBuf.append(" and state.lifeform=");
      strBuf.append(lifeform.getSimId());
      strBuf.append(" and state.seasonOrd=");
      strBuf.append(season.ordinal());
      strBuf.append(" and state.timeStep=");
      strBuf.append(timeStep);
      strBuf.append(" and state.run=");
      strBuf.append(Simulation.getInstance().getCurrentRun());

      Session session = DatabaseCreator.getSessionFactory().openSession();
      Query q = session.createQuery(strBuf.toString());
      List totList = q.list();
      session.close();

      if (totList == null || totList.size() == 0) return null;

      return (VegSimStateData)totList.get(0);

    }
  }

  /**
   * Removes a state based on a specified time step, life form, and season.
   * @param ts time step used to find state to be removed
   * @param lifeform the life form used to find state to be removed
   * @param season the season used to find state to be removed
   * @return
   * @throws RuntimeException - not caught or thrown...
   */
  private VegSimStateData removeState(int ts, Lifeform lifeform, Season season) {
    MultiKey key = LifeformSeasonKeys.getKey(lifeform,season);
    if (ts > 0) {
      int index = getSimDataIndex(ts);
      if (index < 0) {
        throw new RuntimeException("Attempted access to unavailable time step");
      }
      return (VegSimStateData)simData[index].remove(key);
    } else {
      return (VegSimStateData)initialState.remove(key);
    }
  }

  public VegSimStateData getStateLastSeason(int tStep) {
    return getStateLastSeason(tStep,dominantLifeform);
  }

  /**
   * Gets the state that occurred in the next to last season;
   * Note this is only used for legacy type of fire code.
   * @param ts time step
   * @param lifeform the life form used to find last season
   * @return VegSimStateData a vegetative simulation state representing the last fire season
   */
  public VegSimStateData getStateLastSeason (int ts, Lifeform lifeform) {
    if (lifeform == null) { lifeform = dominantLifeform; }

    int count=0;
    // work our way backward through seasons until we find one with data.
    Season[] seasons = Climate.allSeasons;
    for (int i=seasons.length-1; i>=0; i--) {
      VegSimStateData state = getState(ts,lifeform,seasons[i]);
      if (state != null && count == 0) { count++; }
      if (state != null && count > 0) { return state; }
    }

    // If we are here that we must be on the first season.
    if (ts > 0) {
      return getState(ts - 1, lifeform);
    }
    return null;
  }

  /**
   * This version is used during the all states report data gathering.
   * Find the last state that occurred in the given time step.
   * If no states found (could only be a error) return null.
   * @param ts time step
   * @param lifeform the life form used to find last season
   * @return VegSimStateData
   */
  public VegSimStateData getStateFinalSeason(int ts, Lifeform lifeform) {
    if (lifeform == null) { lifeform = dominantLifeform; }

    // work our way backward through seasons until we find one with data.
    Season[] seasons = Climate.allSeasons;
    for (int i=seasons.length-1; i>=0; i--) {
      VegSimStateData state = getState(ts,lifeform,seasons[i]);
      if (state != null) { return state; }
    }
    return null;
  }

  /**
   * If state has trees, shrubs, or herbacious, has primary life is true.
   * If veg state is not null for each life form present gets the vegetative simulation state and adds to the buffere as:
   * "Species__Size Class__Density__Process"
   * If veg state is null, will use the primary life form to decide if needs to return NONE
   * @param tStep time step
   * @param kind
   * @return "Species__Size Class__Density__Process" if state is not null,
   * NONE if state is null and trees, Shrubs, herbacious are present, empty string buffer otherwise.
   */
  public String getStateCombineLives(int tStep, SimpplleType.Types kind) {
    boolean      first = true;
    boolean      hasPrimaryLife=false;
    Lifeform[]   lives = Lifeform.getAllValues();
    StringBuffer buf = new StringBuffer(40);

    hasPrimaryLife = (hasLifeform(Lifeform.TREES,tStep) ||
                      hasLifeform(Lifeform.SHRUBS,tStep) ||
                      hasLifeform(Lifeform.HERBACIOUS,tStep));

    for (int i=0; i<lives.length; i++) {
      VegSimStateData state = getState(tStep,lives[i]);
      if (state != null) {
        if (!first) { buf.append("__"); }
        first = false;
        switch (kind) {
          case SPECIES:
            buf.append(state.getVegType().getSpecies().toString());
            break;
          case SIZE_CLASS:
            buf.append(state.getVegType().getSizeClass().toString());
            break;
          case DENSITY:
            buf.append(state.getVegType().getDensity().toString());
            break;
          case PROCESS:
            buf.append(state.getProcess().toString());
            break;
        }
      } else if (hasPrimaryLife && (lives[i] == Lifeform.TREES ||
                                    lives[i] == Lifeform.SHRUBS ||
                                    lives[i] == Lifeform.HERBACIOUS)) {
        if (!first) { buf.append("__"); }
        buf.append("NONE");
        first = false;
      }
    }
    return buf.toString();
  }


  /**
   * Clears the state data by set initial state and simulation data to null.  This class has been deprecated.
   */
  public void clearStateData() {
    initialState = null;
    simData      = null;
  }

  /**
   * If an area does not have multiple life forms enabled will set dominant life form as NA, then sets states based on
   * vegetative type passed and season.YEAR (often used as default setting).
   * @param newState vegetative type to be set
   */
  public void setState(VegetativeType newState) {
    if (Area.multipleLifeformsEnabled() == false) {
      dominantLifeform = Lifeform.NA;
    }
    setState(newState,Season.YEAR);
  }

  /**
   * Overloaded setState() method .  If multiple life forms are not enabled in an area and there is a dominant life form this is used
   * to get the vegetative state.  If multiple life forms are enabled the life form is gotten based on the new states life form.
   * The state is gotten based on life form set, and a new vegetative state object is created with id, and new inputveg type and season.
   * @param newState the vegetative type used to set veg state
   * @param season the season used to set veg state
   */
  public void setState(VegetativeType newState, Season season) {
    Lifeform lifeform;
    if (Area.multipleLifeformsEnabled() == false) {
      if (dominantLifeform == null) { dominantLifeform = Lifeform.NA; }
      lifeform = dominantLifeform;
    } else {
      lifeform = newState.getSpecies().getLifeform();
    }
    VegSimStateData state = getState(lifeform);
    if (state == null) {
      setState(new VegSimStateData(getId(),newState),season);
    } else {
      state.setVegType(newState);
    }
    simData = null;
  }

  /**
   * Sets the initial state of the unit. If a state already is present
   * for the new states lifeform then this one will be added to it.
   * Which will never happen the import code and attributes input file
   * is not setup for multiple states for a given lifeform.
   * @param newState VegSimStateData
   */
  public void setState(VegSimStateData newState, Season season) {
    Lifeform lifeform = newState.getLifeform();
    if (Area.multipleLifeformsEnabled() == false) {
      if (dominantLifeform == null) { dominantLifeform = Lifeform.NA; }
      lifeform = dominantLifeform;
    }

    MultiKey key = LifeformSeasonKeys.getKey(lifeform, season);
    if (initialState == null) {
      initialState = new Flat3Map();
    }
    initialState.put(key,newState);

    dominantLifeform = Lifeform.getMostDominant(dominantLifeform, lifeform);
    simData = null;
  }

  /**
   * Checks if habitat type group is valid, if so loops through the seasons and gets the vegetative state based on
   * time step, parameter life form, and season. If the state's toString is empty, means the state is not valid.
   * @param lifeform
   * @return True if current state is valid.
   */
  public boolean isCurrentStateValid(Lifeform lifeform) {
    if (isHabitatTypeGroupValid() == false) { return false; }

    int ts = determineDefaultTimeStep();

    Season[] seasons = Climate.allSeasons;
    for (int i=0; i<seasons.length; i++) {

      VegSimStateData state = getState(ts,lifeform,seasons[i]);

      // 3 Feb 2009, added check for getVegType == null
      // not sure if this is correct thing to do as do not know why it is null.
      if (state == null || state.getVegType() == null) { continue; }

      if (htGrp.getVegetativeType(state.getVegType().toString()) == null) {
        return false;
      }
    }
    return true;
  }
  /**
   * Overloaded isCurrentStateValid().
   * Checks if habitat type group is valid, if so loops through the seasons and gets the vegetative state based on time step, life form from all life forms [], and season.
   * If the state's toString is empty, means the state is not valid.
   * @return True if current vegetative state not found invalid.
   */
  public boolean isCurrentStateValid() {

    if (isHabitatTypeGroupValid() == false) return false;

    Lifeform[] lives = Lifeform.getAllValues();
    ArrayList<VegSimStateData> states;

    for (Lifeform lifeform : lives) {
      if (isCurrentStateValid(lifeform) == false) return false;
    }
    return true;
  }

  /**
   * Gets a treatment state based on parameter time step.
   * @param tStep time step used to get the Treatment
   * @return the saved state of treatment which will be the past treated state.
   */
  private VegetativeType getPastTreatedState(int tStep) {
    Treatment treat = getTreatment(tStep);

    if (treat == null) {
      return null;
    } else {
      return treat.getSavedState();
    }
  }

  /**
   * Checks if a species is valid by first getting the vegetative state. The state is then used to get their species and results of its isValid methods.
   * @return True if species is valid.
   */
  public boolean isSpeciesValid() {
    VegSimStateData state = getState();
    return (state != null ? state.getVeg().getSpecies().isValid() : false);
  }

  /**
   * Checks if a size class is valid by first getting the vegetative state.  The state is then used to get its size class and results of its isValid methods.
   * @return True if size class is valid.
   */
  public boolean isSizeClassValid() {
    VegSimStateData state = getState();
    return (state != null ? state.getVeg().getSizeClass().isValid() : false);
  }

  /**
   * Checks if a density is valid by first getting the vegetative state.  The state is then used to get its density and results of its isValid methods.
   * @return True if density is valid.
   */
  public boolean isDensityValid() {
    VegSimStateData state = getState();
    return (state != null ? state.getVeg().getDensity().isValid() : false);
  }

  /**
   * Checks if an age is valid by first getting the vegetative state.  The state is then used to get its age.  If this is >=1 the age is valid.
   * @return True if age is valid.
   */
  public boolean isAgeValid() {
    VegSimStateData state = getState();
    return (state != null ? state.getVeg().getAge() >= 1 : false);
  }

  /**
   * Creates a new vegetative type group (component used in GUI).  First checks if this an already valid habitat type group, if not will create a
   * new vegetative type with species, size class, age, and density.
   * @param species the species to be set
   * @param sizeClass the size class of vegetative type to be set
   * @param age the age of vegetative type to be set
   * @param density the density of vegetative type to be set
   */
  private void newVegTypeComponent(Species species, SizeClass sizeClass, int age, Density density) {

    VegetativeType newVegState = null;

    if (isHabitatTypeGroupValid()) {
      newVegState = htGrp.getVegetativeType(species,sizeClass,age,density);
    }
    if (newVegState == null) {
      newVegState = new VegetativeType(species,sizeClass,age,density);
    }
    setState(newVegState);

    updateInitialTreatmentSavedState(newVegState);
  }

  /**
   * Used to update the initial treatment saved state for a new vegetative type.
   * If treatment is null of there has been now old initial treatments, returns null, else creates a new initial treatment based on type of old initial treatment type.
   *
   * @param newVegState The newly added vegetative state to have initial treatment evaluated.
   */
  public void updateInitialTreatmentSavedState(VegetativeType newVegState) {
    // Update initial Treatment with new saved state.
    if (treatment == null || treatment.size() == 0) return;

    Treatment oldInitialTreat = (Treatment)treatment.get(0);
    if (oldInitialTreat == null) return;

    Treatment newInitialTreat = Treatment.createInitialTreatment(oldInitialTreat.getType(), newVegState);

    treatment.clear();
    addTreatment(newInitialTreat);

  }

  /**
   * Creates a new vegetative type group for a passed species.
   * This is done by creating a new object and adding the species, rather than updating the old veg type group.
   * @param newSpecies the species to be set
   * @param life used to find the vegetative state (since species is not set)
   */
  public void setSpecies(Species newSpecies, Lifeform life) {
    VegSimStateData state = getState(life);
    if (state == null) return;

    newVegTypeComponent(newSpecies,
                        state.getVeg().getSizeClass(),
                        state.getVeg().getAge(),
                        state.getVeg().getDensity());
  }

  /**
   * Creates a new vegetative type group for a parameter size class.
   * This is done by creating a new object and adding the size class, rather than updating the old veg type group.
   * @param newSizeClass the size class to be set
   * @param life used to find the vegetative state.
   *
   */
  public void setSizeClass(SizeClass newSizeClass, Lifeform life) {
    VegSimStateData state = getState(life);
    if (state == null) return;

    newVegTypeComponent(state.getVeg().getSpecies(),
                        newSizeClass,
                        state.getVeg().getAge(),
                        state.getVeg().getDensity());
  }

  /**
   * Creates a new vegetative type state for a parameter density.
   * This is done by creating a new object and adding the density, rather than updating the old veg type group.
   * @param newDensity the density to be set
   * @param life used to find the vegetative state.
   */
  public void setDensity(Density newDensity, Lifeform life) {
    VegSimStateData state = getState(life);
    if (state == null) return;

    newVegTypeComponent(state.getVeg().getSpecies(),
                        state.getVeg().getSizeClass(),
                        state.getVeg().getAge(),
                        newDensity);
  }

  /**
   * Creates a new vegetative type state for a parameter age.
   * This is done by creating a new object and adding the age, rather than updating the old veg type group.
   * @param age the age to be set
   * @param life used to find the vegetative state.
   */
  public void setAge(int age, Lifeform life) {
    VegSimStateData state = getState(life);
    if (state == null) return;

    newVegTypeComponent(state.getVeg().getSpecies(),
                        state.getVeg().getSizeClass(),
                        age,
                        state.getVeg().getDensity());
  }

  /**
   * Gets the Evu's Acres in int form.
   * @return acres are always returned as an int, then / by 10^2
   */
  public int getAcres() { return acres; }

  /**
   * Gets the Evu's Acres in float form.
   * @return acres returned as a float, by dividing int acres/10^2.
   */
  public float getFloatAcres() {
    return ( (float)acres / (float)Utility.pow(10,Area.getAcresPrecision()) );
  }

  /**
   * Sets an Evu's acres.  Acres is always stored as an int.
   */
  public void setAcres(int val) { acres = val; }

  /**
   * Sets an Evu's acres from a float value by rounding the float value multiplied by 10^2
   */
  public void setAcres(float val) {
    acres = Math.round(val * Utility.pow(10,Area.getAcresPrecision()));
  }

  /**
   * Check if Evu's acres is valid. This checks if acres are greater than 0 or there is a species present and acres is >=0
   *@return true if acres are valid
   */
  public boolean isAcresValid() {
    RegionalZone zone = Simpplle.getCurrentZone();
    int tmpAcres = getAcres();

    if (tmpAcres > 0 || ((Species)getState(SimpplleType.SPECIES) == Species.ND && tmpAcres >= 0)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Gets the Fire Management Zone (FMZ)
   * @see simpplle.comcode.Fmz
   * @return an Fmz
   */
  public Fmz getFmz() { return fmz; }

  /**
   * Sets the Evu's Fire Management Zone by creating a new fire management zone instance from the current zone's FMZ, or
   * setting it without new instance using parameter name
   * @param fmzName the fire management zone name to be set
   */
  public void setFmz(String fmzName) {
    Fmz tmpFmz = Simpplle.getCurrentZone().getFmz(fmzName);

    if (tmpFmz == null) {
      setFmz(new Fmz(fmzName));
    }
    else {
      setFmz(tmpFmz);
    }
  }

  /**
   * Overloaded setFmz().  Sets the fire management zone without referring to current zone fmz.
   * @param newFmz
   */
  public void setFmz(Fmz newFmz) { fmz = newFmz; }

  /**
   * Checks if there is a fire management zone present in the current zone .
   * @return true if fire management zone is valid.
   */
  public boolean isFmzValid() {
    if (fmz == null) return false;
    return (Simpplle.getCurrentZone().getFmz(fmz.getName()) != null);
  }

  /**
   * Make sure the currently assigned Fmz is valid.
   * It can become invalid if the user load a new fmz file.
   * else updates the fire mangement zone with either the default zone or a the new fmz name.
   * @return true if the fmz was changed to the default fmz
   */
  public boolean updateFmz() {
    String fmzName = getFmz().getName();
    RegionalZone zone = Simpplle.getCurrentZone();

    if (zone.getFmz(fmzName) == null) {
      fmz = zone.getDefaultFmz();
      return true;
    } else {
      fmz = zone.getFmz(fmzName);
    }
    return false;
  }

  /**
   * Gets the Mountain Pine Beetle hazard.  Will either return the Ponderosa pine Mountain pine beetle hazard, or
   * LpMpbHaszard
   * @param process
   * @return
   */
  public MtnPineBeetleHazard.Hazard getMpbHazard(ProcessType process) {
    if (process == ProcessType.PP_MPB) {

      return getPpMpbHazard();

    } else if (process == ProcessType.LIGHT_LP_MPB ||
               process == ProcessType.SEVERE_LP_MPB) {

      return getLpMpbHazard();

    }
    return null;
  }

  /**
   * Gets the value of LpMpbHazard, used during simulations.
   * @return an int.
   */
  public MtnPineBeetleHazard.Hazard getLpMpbHazard() {
    return lpMpbHazard;
  }

  /**
   * Sets the value of LpMpbHazard, used during simulations.
   */
  public void setLpMpbHazard(MtnPineBeetleHazard.Hazard hazard) {
    lpMpbHazard = hazard;
  }

  /**
   * Gets the value of PpMpbHazard, used during simulations.
   * @return an int.
   */
  public MtnPineBeetleHazard.Hazard getPpMpbHazard() {
    return ppMpbHazard;
  }

  /**
   * Sets the value of PpMpbHazard, used during simulations.
   */
  public void setPpMpbHazard(MtnPineBeetleHazard.Hazard hazard) {
    ppMpbHazard = hazard;
  }

  /**
   * Returns the distance to another EVU. The distance is equals the distance between the grid cell locations times
   * the width of a polygon.
   * @param evu An existing vegetation unit
   * @return The distance in feet to the other EVU
   */
  public double distanceToEvu(Evu evu) {

    int x1 = location[X];
    int y1 = location[Y];
    int x2 = evu.getLocationX();
    int y2 = evu.getLocationY();

    int sqrDeltaX = (x2 - x1) * (x2 - x1);
    int sqrDeltaY = (y2 - y1) * (y2 - y1);

    return Simpplle.getCurrentArea().getPolygonWidth() * Math.sqrt(sqrDeltaX + sqrDeltaY);

  }

  /**
   * Returns the distance to another EVU in meters.
   * @param evu An existing vegetation unit
   * @return The distance in meters to the other EVU
   */
  public double distanceToEvuMeters(Evu evu) {

    return distanceToEvu(evu) * 0.3048;

  }

  /**
   * Gets all permanent water units in an area's Evu's. This includes if an evu isaquatic or has associated aquatic
   * units with permantent water.
   */
  public static void findWaterUnits() {
    Evu[] allUnits = Simpplle.getCurrentArea().getAllEvu();

    if (waterUnits == null) {
      waterUnits = new ArrayList<ArrayList<WaterUnitData>>();
    }

    ArrayList<WaterUnitData> list = new ArrayList<WaterUnitData>();
    waterUnits.add(list);

    WaterUnitData isPermanentWater;
    Evu evu;
    boolean isDrySuccession = Simpplle.getClimate().isDrySuccession();

    for(int i=0; i<allUnits.length; i++) {
      evu = allUnits[i];
      if (evu == null) { continue; }

      if (evu.isAquatic() == false &&
          (evu.assocAquaticUnits == null || evu.assocAquaticUnits.size() == 0)) {
        continue;
      }
      isPermanentWater = new WaterUnitData();
      if (evu.isAquatic()) {
        isPermanentWater.permanentWater = true;
        isPermanentWater.unit = evu;
        isPermanentWater.waterEvu = evu;
        list.add(isPermanentWater);
      } else if (evu.hasAssociatedAquaticUnit(isPermanentWater)) {
        isPermanentWater.waterEvu = evu;
        list.add(isPermanentWater);
      }
    }
  }

  /**
   * Calls to overloaded distanceToWater with arraylist conataining water unit data arraylist
   * @param isPermanentWater
   * @return
   */
  public double distanceToWater(WaterUnitData isPermanentWater) {
    return distanceToWater(isPermanentWater, waterUnits.size()-1);
  }

  /**
   * This calculates the minimum distance to water by evaluating an areas Evu's water unit data.  Calculations are based on min distance to permanant water
   * or min temporary distance tow water, and always returns the lesser.
   * @param isPermanentWater
   * @param index
   * @return -1 if there is no water unit data otherwise will return minimum distance to water
   */
  public double distanceToWater(WaterUnitData isPermanentWater, int index) {
    if (waterUnits == null || waterUnits.size() == 0) { return -1; }

    double minDistWater     = Double.MAX_VALUE;
    double minTempDistWater = Double.MAX_VALUE;
    double minPermDistWater = Double.MAX_VALUE;
    int tempWaterEvu = -1;
    int permWaterEvu = -1;
    int waterEvu = -1;
    double dist;
    Evu    evu;

    ArrayList<WaterUnitData> list = waterUnits.get(index);
    if (list.size() == 0) { return -1; }

    for (int i=0; i<list.size(); i++) {
      WaterUnitData tmp = list.get(i);
      evu = tmp.waterEvu;

      dist = distanceToEvu(evu);
      if (dist > minPermDistWater && dist > minTempDistWater) { continue; }

      if (tmp.permanentWater) {
        if (dist < minPermDistWater) {
          permWaterEvu = i;
          minPermDistWater = dist;
        }
      } else {
        if (dist < minTempDistWater) {
          tempWaterEvu = i;
          minTempDistWater = dist;
        }
      }
    }

    if (permWaterEvu != -1 && tempWaterEvu != -1 && (minTempDistWater < minPermDistWater)) {
      if (BisonGrazing.isSameCategory(minTempDistWater, minPermDistWater)) {
        minDistWater = minPermDistWater;
        waterEvu     = permWaterEvu;
      } else {
        minDistWater = minTempDistWater;
        waterEvu     = tempWaterEvu;
      }
    } else if (minTempDistWater < minPermDistWater) {
      minDistWater = minTempDistWater;
      waterEvu     = tempWaterEvu;
    } else if (minPermDistWater < minTempDistWater) {
      minDistWater = minPermDistWater;
      waterEvu     = permWaterEvu;
    }

    if (waterEvu == -1) return -1;

    WaterUnitData tmp = list.get(waterEvu);

    isPermanentWater.permanentWater = tmp.permanentWater;
    isPermanentWater.unit = tmp.unit;
    isPermanentWater.waterEvu = tmp.waterEvu;

    return minDistWater;

  }

  /**
   * Stores road units from the current area that have an EVU. Each road unit is stored in a static array with
   * the road's first associated vegetation unit.
   */
  public static void findRoadUnits() {

    Roads[] allUnits = Simpplle.getCurrentArea().getAllRoads();

    roadUnits.clear();

    for(int i = 0; i < allUnits.length; i++) {

      if (allUnits[i] != null) continue;

      Evu evu = allUnits[i].getFirstVegUnit();
      if (evu == null) continue;

      RoadUnitData roadData = new RoadUnitData();
      roadData.road = allUnits[i];
      roadData.evu  = evu;

      roadUnits.add(roadData);

    }
  }

  /**
   * Calculates the distance of a nearest road at current time step.
   * @return  nearest road if there is road unit data or else returns MAX_ROAD_DIST
   */
  public double findDistanceNearestRoad() {
    int ts = Simulation.getCurrentTimeStep();
    ArrayList<RoadUnitData> list = null;

    if (nearestRoad != null && nearestRoad.size() > 0) {
      list = nearestRoad.get(ts);
    }
    if (list == null || list.size() == 0) {
      return MAX_ROAD_DIST;
    }

    RoadUnitData data = null;
    for (int i=0; i<list.size(); i++) {
      data = list.get(i);
      if (data != null && data.road != null && (data.road.getSimStatus(ts) == Roads.Status.OPEN)) {
        break;
      }
      data = null;
    }

    if (data == null) {
      return MAX_ROAD_DIST;
    }

    if (data.evu == this) {
      return 1;
    } else if (data.evu == null) {
      return MAX_ROAD_DIST;
    }

    return distanceToEvu(data.evu);

  }

  /**
   * Clears the array of nearest roads and adds the nearest road of any status.
   */
  public void findNearestRoad() {

    RoadUnitData data = new RoadUnitData();
    distanceToRoad(data,false);

    ArrayList<RoadUnitData> list = new ArrayList<>(2);
    list.add(data);

    nearestRoad = new ArrayList<>();
    nearestRoad.add(list);

  }

  /**
   * Currently this simply copies the nearest road from the previous time step.
   * However, in the future if we add logic to restrict which roads qualify then
   * this will change.
   */
  public void updateNearestRoad() {
    int ts = Simulation.getCurrentTimeStep();

    ArrayList<RoadUnitData> list = new ArrayList<RoadUnitData>(nearestRoad.get(ts-1));
    nearestRoad.add(list);


    for (int i=0; i<list.size(); i++) {
      RoadUnitData data = list.get(i);
      if (data == null || data.road == null) {
        continue;
      }
      if (data.road.getSimStatus(ts) == Roads.Status.OPEN) {
        return;
      }
    }

    // No open roads found search again.
    RoadUnitData data = new RoadUnitData();
    double dist = distanceToRoad(data,true);
    list.add(data);

  }

  /**
   * gets the nearest road from road unit data and checks if is open.  If that road unit data is returned
   * @param ts time step to be evaluated
   * @return the road unit data object of the nearest open orad
   */
  private RoadUnitData getNearestOpenRoad(int ts) {
    ArrayList<RoadUnitData> list = new ArrayList<RoadUnitData>(nearestRoad.get(ts));
    for (int i=0; i<list.size(); i++) {
      RoadUnitData data = list.get(i);
      if (data == null || data.road == null) {
        continue;
      }
      if (data.road.getSimStatus(ts) == Roads.Status.OPEN) {
        return data;
      }
    }
    return null;
  }

  /**
   * Returns the distance in feet to the nearest road. The nearest road unit to this EVU is recorded in the RoadUnitData.
   * @param roadData A road unit data instance to record the nearest road
   * @param onlyOpen True if only open roads should be considered
   * @return The minimum distance or MAX_ROAD_DIST if there are no road units
   */
  private double distanceToRoad(RoadUnitData roadData, boolean onlyOpen) {

    // If there are no roads in any EVU, return the max road distance

    if (roadUnits == null || roadUnits.size() == 0) {
      roadData.road = null;
      roadData.evu  = null;
      return MAX_ROAD_DIST;
    }

    // If this EVU has roads, return a very small distance

    if (assocRoadUnits != null && assocRoadUnits.size() > 0) {
      if (hasAssociatedRoadUnit(roadData,onlyOpen)) {
        roadData.evu = this;
        return 1;
      }
    }

    // Otherwise find the nearest road

    roadData.road = null;
    roadData.evu  = null;

    double minDistRoad = MAX_ROAD_DIST;

    for (RoadUnitData roadUnit : roadUnits) {

      if (onlyOpen && roadUnit.road.getSimStatus() != Roads.Status.OPEN) continue;

      Evu evu = findClosestRoadEvu(roadUnit.road);
      if (evu == null) continue;

      double dist = distanceToEvu(evu);

      if (dist < minDistRoad) {

        minDistRoad = dist;

        roadData.road = roadUnit.road;
        roadData.evu  = evu;

      }
    }

    return minDistRoad;

  }

  /**
   * Returns the closest EVU associated with the given road.
   * @param road A road unit
   * @return The closest EVU
   */
  private Evu findClosestRoadEvu(Roads road) {

    List<Evu> vegUnits = road.getAssociatedVegUnits();
    if (vegUnits == null) return null;

    Evu closestEvu = null;
    double lowestDist = Double.MAX_VALUE;
    for (Evu evu : vegUnits) {
      double dist = distanceToEvu(evu);
      if (dist < lowestDist) {
        lowestDist = dist;
        closestEvu = evu;
      }
    }

    return closestEvu;

  }

  /**
   * Changes the roads status to Open.
   */
  public void openRoads() {
    changeRoadsStatus(Roads.Status.OPEN);
  }

  /**
   * Changes the road status to closed.
   */
  public void closeRoads() {
    changeRoadsStatus(Roads.Status.CLOSED);
  }

  /**
   * Method to take a road status and change the Evu's associated road units to the passed status
   * @param status this can be open or closed.
   */
  public void changeRoadsStatus(Roads.Status status) {
    if (assocRoadUnits == null) { return; }

    for (int i=0; i<assocRoadUnits.size(); i++) {
      Roads road = assocRoadUnits.get(i);
      road.setSimStatus(status);
    }
  }

  /**
   * Change trail status to Open.
   */
  public void openTrails() {
    changeTrailsStatus(Trails.Status.OPEN);
  }

  /**
   * Change Trail status to Closed.
   */
  public void closeTrails() {
    changeTrailsStatus(Trails.Status.CLOSED);
  }

  /**
   * Method to change trail status by taking in a trail status and changing the Evu's trails to that status
   * @param status this can either open or closed
   */
  public void changeTrailsStatus(Trails.Status status) {
    if (assocTrailUnits == null) { return; }

    for (int i=0; i<assocTrailUnits.size(); i++) {
      Trails trail = assocTrailUnits.get(i);
      trail.setSimStatus(status);
    }
  }

  /**
   * Stores trail units from the current area that have an EVU. Each trail unit is stored in a static array with
   * the trail's first associated vegetation unit.
   */
  public static void findTrailUnits() {

    Trails[] allUnits = Simpplle.getCurrentArea().getAllTrails();

    trailUnits.clear();

    for(int i = 0; i < allUnits.length; i++) {

      if (allUnits[i] == null) continue;

      Evu evu = allUnits[i].getFirstVegUnit();
      if (evu == null) continue;

      TrailUnitData trailData = new TrailUnitData();
      trailData.trail = allUnits[i];
      trailData.evu   = evu;

      trailUnits.add(trailData);

    }
  }

  /**
   * Calculates the distance to nearest trail at the current time step.
   * @return MAX_TRAIL_DIST if there are no trails, 1 if trail is in this evu, or the minimum distance to trail.
   */
  public double findDistanceNearestTrail() {
    int ts = Simulation.getCurrentTimeStep();
    ArrayList<TrailUnitData> list = null;

    if (nearestTrail != null && nearestTrail.size() > 0) {
      list = nearestTrail.get(ts);
    }
    if (list == null || list.size() == 0) {
      return MAX_TRAIL_DIST;
    }

    TrailUnitData data = null;
    for (int i=0; i<list.size(); i++) {
      data = list.get(i);
      if (data != null && data.trail != null && (data.trail.getSimStatus(ts) == Trails.Status.OPEN)) {
        break;
      }
      data = null;
    }

    if (data == null) {
      return MAX_TRAIL_DIST;
    }

    if (data.evu == this) {
      return 1;
    } else if (data.evu == null) {
      return MAX_TRAIL_DIST;
    }

    return distanceToEvu(data.evu);

  }

  /**
   * Clears the array of nearest trails and adds the nearest trail of any status.
   */
  public void findNearestTrail() {

    TrailUnitData data = new TrailUnitData();
    distanceToTrail(data,false);

    ArrayList<TrailUnitData> list = new ArrayList<>(2);
    list.add(data);

    nearestTrail = new ArrayList<>();
    nearestTrail.add(list);

  }

  /**
   * Gets the nearest trail at current time step-1. Searches through to see if there is an open nearest trail.
   * If not will call to find a new nearest trail.
   */
  public void updateNearestTrail() {

    int ts = Simulation.getCurrentTimeStep();

    ArrayList<TrailUnitData> list = new ArrayList<TrailUnitData>(nearestTrail.get(ts-1));
    nearestTrail.add(list);

    for (int i=0; i<list.size(); i++) {
      TrailUnitData data = list.get(i);
      if (data == null || data.trail == null) {
        continue;
      }
      if (data.trail.getSimStatus(ts) == Trails.Status.OPEN) {
        return;
      }
    }

    // No open trails found search again.
    TrailUnitData data = new TrailUnitData();
    double dist = distanceToTrail(data,true);
    list.add(data);
  }

  /**
   * Calculates the nearest open trail by going through the nearesttrail array list at passed time step.
   * @param ts time step to be evaluated
   * @return closest open trail data, if none is found  - null
   */
  private TrailUnitData getNearestOpenTrail(int ts) {
    ArrayList<TrailUnitData> list = new ArrayList<TrailUnitData>(nearestTrail.get(ts));
    for (int i=0; i<list.size(); i++) {
      TrailUnitData data = list.get(i);
      if (data == null || data.trail == null) {
        continue;
      }
      if (data.trail.getSimStatus(ts) == Trails.Status.OPEN) {
        return data;
      }
    }
    return null;
  }

  /**
   * Returns the distance in feet to the nearest trail. The nearest trail unit to this EVU is recorded in the TrailUnitData.
   * @param trailData A trail unit data instance to record the nearest trail
   * @param onlyOpen True if only open trails should be considered
   * @return The minimum distance or MAX_TRAIL_DIST if there are no trail units
   */
  private double distanceToTrail(TrailUnitData trailData, boolean onlyOpen) {

    // If there are no trails in any EVU, return the max trail distance

    if (trailUnits == null || trailUnits.size() == 0) {
      trailData.trail = null;
      trailData.evu   = null;
      return MAX_TRAIL_DIST;
    }

    // If this EVU has trails, return a very small distance

    if (assocTrailUnits != null && assocTrailUnits.size() > 0) {
      if (hasAssociatedTrailUnit(trailData,onlyOpen)) {
        trailData.evu = this;
        return 1;
      }
    }

    // Otherwise find the nearest trail

    trailData.trail = null;
    trailData.evu   = null;

    double minDistTrail = MAX_TRAIL_DIST;

    for (TrailUnitData trailUnit : trailUnits) {

      if (onlyOpen && trailUnit.trail.getSimStatus() != Trails.Status.OPEN) continue;

      Evu evu = findClosestTrailEvu(trailUnit.trail);
      if (evu == null) continue;

      double dist = distanceToEvu(evu);

      if (dist < minDistTrail) {

        minDistTrail = dist;

        trailData.trail = trailUnit.trail;
        trailData.evu   = evu;

      }
    }

    return minDistTrail;

  }

  /**
   * Returns the closest EVU associated with the given trail.
   * @param trail A trail unit
   * @return The closest EVU
   */
  private Evu findClosestTrailEvu(Trails trail) {

    List<Evu> vegUnits = trail.getAssociatedVegUnits();
    if (vegUnits == null) return null;

    Evu closestEvu = null;
    double lowestDist = Double.MAX_VALUE;
    for (Evu evu : vegUnits) {
      double dist = distanceToEvu(evu);
      if (dist < lowestDist) {
        lowestDist = dist;
        closestEvu = evu;
      }
    }

    return closestEvu;

  }

  /**
   * Gets the spreading process for the current vegetative simulation state.
   * @return the spreading process
   */
  private ProcessOccurrence findSpreadEvent() {
    VegSimStateData state = getState();
    if (state == null) { return null; }

    ProcessType process = state.getProcess();

    return Simpplle.getAreaSummary().findSpreadingProcessEvent(this,process);
  }

  /**
   * Gets the current simulation time step and uses the time step and dominant fire life form object to get the fire process for
   * for the state.
   * @return the fire spread process event at current simulation time step.
   */
  private ProcessOccurrence findFireSpreadEvent() {
    Lifeform life  = getDominantLifeformFire();
    int      cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    VegSimStateData state = getState(cTime,life);
    if (state == null) { return null; }

    ProcessType process = state.getProcess();

    return Simpplle.getAreaSummary().findSpreadingProcessEvent(this,process);
  }

  /**
   * Gets the Origin Evu that spread a given process to this Evu.
   *
   * @return the origin Evu.
   */
  public Evu getOriginUnit() {
    ProcessOccurrence event = findSpreadEvent();
    return ( (event != null) ? event.getUnit() : this);
  }

  public Evu getOriginUnitFire() {
    ProcessOccurrence event = findFireSpreadEvent();
    return ( (event != null) ? event.getUnit() : this);
  }

  /**
   * Gets the Origin Process that spread to this unit.
   * Processes can be overridden by fire, so it is necessary to store
   * this information, because cannot reliably loop up the information in the origin EVU since it might have changed.
   * This information together with the Origin unit is used to determine how a given
   * process spread to its adjacent units.
   * @return the original process that spread here.
   */
  public ProcessType getOriginProcess() {
    ProcessOccurrence event = findSpreadEvent();

    return ((event != null) ? event.getProcess() : getState().getProcess());
  }

  /**
   * Gets the Process that occurred during the simulation time
   * step provided as the parameter, which will be the last season process unless there is not one.
   * @param tStep the time step being evaluated
   * @return a Process representing the process last season, or null (ProcessType.NONE).
   */
  public ProcessType getProcessLastSeason(int tStep) {
    VegSimStateData state = this.getStateLastSeason(tStep);
    if (state != null) { return state.getProcess(); }

    return ProcessType.NONE;
  }

  /**
   * Checks if an Evu has a specified process at a specific time step.
   * @param tStep the time step being evaluated
   * @param process the process being evaluated
   * @return true if has the process being evaluated.
   */
  public boolean hasProcess(int tStep, ProcessType process) {
    return hasProcess(tStep,dominantLifeform,process);
  }

  /**
   * Loops through all the life forms in the Evu to check if the specified process exists within the Evu
   * @param tStep the time step being evaluated
   * @param process the process being evaluated
   * @return true if the Evu has the process
   */
  public boolean hasProcessAnyLifeform(int tStep, ProcessType process) {
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      if (hasProcess(tStep,allLives[i],process)) { return true; }
    }
    return false;
  }

  /**
   * Searches to see if fire is present in the Evu life form at the current simulation time step.
   * @return true if process type is either SRF (Stand Replacing Fire), LSF, or MSF (Mixed Severity Fire
   */
  public boolean hasFireAnyLifeform() {
    Lifeform[] allLives = Lifeform.getAllValues();
    int cStep = Simulation.getCurrentTimeStep();
    for (int i=0; i<allLives.length; i++) {
      if (hasProcess(cStep,allLives[i],ProcessType.SRF)) { return true; }
      if (hasProcess(cStep,allLives[i],ProcessType.MSF)) { return true; }
      if (hasProcess(cStep,allLives[i],ProcessType.LSF)) { return true; }
    }
    return false;
  }

  /**
   * Checks if there is a life form present in Evu that has 'locking' probability - (getProb = L)
   * @return true if has any life form has locking probability
   */
  public boolean hasLockinProcessAnyLifeform() {
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      VegSimStateData state = getState(allLives[i]);
      if (state == null) { continue; }
      if (hasLifeform(allLives[i]) && state.getProb() == L) { return true; }
    }
    return false;
  }

  /**
   * Checks whether a the vegetative siumlation state at a particular time step and for a specified life form contains
   * a specific process.
   * @param tStep the time step used to returned the query vegetative state
   * @param lifeform the life form used to return the query vegetative state
   * @param process the process being evaluated
   * @return true if the Evu has the process at specified time step and for specific life form.
   */
  private boolean hasProcess(int tStep, Lifeform lifeform, ProcessType process) {
    VegSimStateData state = getState(tStep,lifeform);
    return (state != null) ? state.getProcess().equals(process) : false;
  }

  /**
   * This function will return true if one the supplied treatments occurred within the last 10 years - passes the parameter of 1 = N for decades to
   * processOccurredLastNDecades method.
   */
  public boolean checkPastProcesses(ProcessType[] processes) {
    return processOccurredLastNDecades(processes,1);
  }

  /**
   * Checks if one or more specific processes occurred in the last N number of decades for the current simulation at the current
   * time step.  If time steps are yearly, multiplies int n by 10.  Else gets current simulation time step and subtracts the number of time steps
   * from it.  Loops through the parameter process array and evaluates whether the process is the state processes.
   * @param processes array of processes to be checked if they occurred in Evu in last N number of decades.
   * @param n - algebraic variable n which represents number of decades i.e. n = 2 == 2 decades.
   * @return true if process occurred in last N Decades
   */
  private boolean processOccurredLastNDecades(ProcessType[] processes, int n) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    ProcessType  process;
    int cTime    = simulation.getCurrentTimeStep();
    int numSteps = (simulation.isYearlyTimeSteps()) ? (n*10) : n;

    for(int ts=cTime; ts>(cTime-numSteps); ts--) {
      VegSimStateData state = getState(ts);
      if (state == null) { continue; }
      process = state.getProcess();
      for(int i=0; i<processes.length; i++) {
        if (processes[i].equals(process)) { return true; }
      }
    }
    return false;
  }

  /**
   * Method to check if a process has occurred at all in the past based only on proceses, variable n representing time steps being evaluated.
   *
   * @param processes the array of processes to be evaluated within an Evu for past n time steps
   * @param n variable representing time steps
   * @param checkCurrentTimeStep true if should include current time step in query
   * @return true if process occurred in specified number of time steps
   */
  private boolean processOccurredAllPastNTimeSteps(ProcessType[] processes, int n, boolean checkCurrentTimeStep) {
    return processOccurredAllPastNTimeSteps(null,processes,n,checkCurrentTimeStep);
  }

  /**
   * Checks if a process occurred in past time step quantity specified in passed int n argument. Gets the current simulation and time step
   * and time step.  If current time step is included counts backward from them, else subtracts 1 from it.  The time steps are then counted down
   * and the vegetative state is gotten using either life form and time step or just time step, and the the processes array passed is looped through
   * to see if the process occurred in past N time steps.
   * @param lifeform
   * @param processes the array of processes to be evaluated within an Evu for past n time steps
   * @param n variable representing time steps being evaluated
   * @param checkCurrentTimeStep true if should include current time step in query
   * @return
   */
  private boolean processOccurredAllPastNTimeSteps(Lifeform lifeform, ProcessType[] processes, int n, boolean checkCurrentTimeStep) {

    Simulation  simulation = Simpplle.getCurrentSimulation();
    int         cTime    = simulation.getCurrentTimeStep();
    boolean     found=false;

    if (checkCurrentTimeStep == false) { cTime--; }

    ProcessType processType;
    for (int ts=cTime; ts>(cTime-n); ts--) {
      found = false;
      if (lifeform != null) {
        VegSimStateData state = getState(ts,lifeform);
        if (state == null) { return false; }
        processType = state.getProcess();
      }
      else {
        VegSimStateData state = getState(ts);
        if (state == null) { return false; }
        processType = state.getProcess();
      }

      for(int i=0; i<processes.length; i++) {
        if (processes[i].equals(processType)) {
          found = true;
          break;
        }
      }
      if (!found) { return false; }
    }
    return true;
  }

  /**
   * Updates the current Process based on specified lifeforma and process type - season parameter is passed as null.
   * @param p the new current process.
   */
  public void updateCurrentProcess(Lifeform lifeform, ProcessType p) {
    updateCurrentProcess(lifeform,p,null);
  }

  /**
   * Overloaded updateCurrenProcess method.  Requires only the process type and then uses the areas current life form if there is one to get
   * update the current process.
   * @param p the new process
   */
  public void updateCurrentProcess(ProcessType p) {
    if (Area.multipleLifeformsEnabled()) {
      updateCurrentProcess(Area.currentLifeform,p);
    } else {
      updateCurrentProcess(null, p);
    }
  }

  /**
   * Overloaded updateCurrenProcess method.  Updates the current process by process type and season.
   * @param p the new process
   * @param season season in which the new process is occurring
   */
  public void updateCurrentProcess(ProcessType p, Climate.Season season) {
    updateCurrentProcess(null, p, season);
  }

  /**
   * Overloaded updateCurrenProcess method.  Uses life form to get current vegetative sim state.  Then sets teh process for the
   * current state and sets the season if season does not equal null.
   * @param lifeform the life form used to find current vegetative state
   * @param p the new process
   * @param season the season in which the new process is occurring
   */
  public void updateCurrentProcess(Lifeform lifeform, ProcessType p, Climate.Season season) {
    VegSimStateData state = getState(lifeform);
    state.setProcess(p);
    if (season != null) {
      state.setSeason(season);
    }
  }

  /**
   * Updates the current state for all life forms.  Loops through all life forms and gets the state, then sets the process and probability for thoses states.
   * @param p the new process
   * @param prob probability
   * @param season the season where the new process is occurring - used to get vegetative simulation state
   */
  public void updateCurrentStateAllLifeforms(ProcessType p, short prob,Climate.Season season) {
    for (Lifeform lifeform : getLifeforms(season)) {
      VegSimStateData state = getState(lifeform);
      state.setProcess(p);
      state.setProb(prob);
      if (season != null) {
        state.setSeason(season);
      }
    }
  }

  /**
   * Gets the initial process for vegetative siumlation state.
   * @return the initial process for a veg simulation state
   */
  public ProcessType getInitialProcess() {
    return getState(0).getProcess();
  }

  /**
   *Gets the initial treatment.
   * @returnthe initial treatment
   */
  public Treatment getInitialTreatment() { return getTreatment(0,false); }

  /**
   * Sets the initial process for a given state, by making an invalid instance (way to make new instances),
   * setting that instances initial type to passed process type, then passes to the overloaded setInitialProcess()
   * @param processName the string literal used to find process type
   */
  public void setInitialProcess(String processName) {
    ProcessType pt = ProcessType.get(processName);
    if (pt == null) {
      pt = ProcessType.makeInvalidInstance(processName);
      setInitialProcess(pt);
      return;
    }
    setInitialProcess(pt);
  }

  /**
   * Sets the initial process for a specified process, by betting the initial state - time step = 0 and setting the process for that
   * state as the passed process.
   * @param p the initial process to be set
   */
  public void setInitialProcess(ProcessType p) {
    VegSimStateData state = getState(0);
    state.setProcess(p);
  }

  /**
   * Checks if the initial process is valid.
   * @return true if initial process is not an invalid process
   */
  public boolean isInitialProcessValid() {
    return ((Process.findInstance(getInitialProcess())instanceof InvalidProcess) == false);
  }

  /**
   * Gets the probabilitiy at a particular time step.  Uses that in a switch to get the string literal version of the probability.
   * @param tStep the time step being evaluated; used to get the states probability
   * @return the string literal version of probability type
   */
  public String getProbStr(int tStep) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);

    int tmpProb = getState(tStep).getProb();
    switch (tmpProb) {
      case D:    return D_STR;
      case L:    return L_STR;
      case S:    return S_STR;
      case SE:   return SE_STR;
      case SFS:  return SFS_STR;
      case SUPP: return SUPP_STR;
      case COMP: return COMP_STR;
      case GAP:  return GAP_STR;
      case NOPROB: return NOPROB_STR;
      default:   return nf.format((tmpProb/100));
    }
  }

  /**
   * Gets the float probability based on the integer probability.  for all cases returns probability/100 by default
   * @param prob probability
   * @return the probability as a float
   */
  public static float getFloatProb(int prob) {
    switch (prob) {
      case D:
      case L:
      case S:
      case SE:
      case SFS:
      case COMP:
      case GAP:
      case SUPP: return prob;
      default:   return (prob/100);
    }
  }

  /**
   * Updates the current Process's probability based on vegetative state found using specified life form.
   * @param prob the new probability.
   */
  public void updateCurrentProb(Lifeform lifeform, int prob) {
    VegSimStateData state = getState(lifeform);
    state.setProb((short) prob);
  }
  /**
   * Overloaded updateCurrentProb method.  Uses the areas current life form to get the current state and updates the current processes probability
   * @param prob
   */
  public void updateCurrentProb(int prob) {
    Lifeform lifeform = Area.currentLifeform;
    updateCurrentProb(lifeform,prob);
  }

  /**
   * Gets the process probability.
   * @param pt the process being evaluated
   * @return null if no process or by default, else gets the process probability if later process probability is not needed
   */
  public ProcessProbability findProcessProb(ProcessType pt) {
    if (pt == null) { return null; }

    for (int i=0; i<needLaterProcessProb.length; i++) {
      if (needLaterProcessProb[i].processType == pt) {
        return needLaterProcessProb[i];
      }
    }
    if (tempProcessProb == null) { return null; }
    for (int i=0; i<tempProcessProb.length; i++) {
      if (tempProcessProb[i].processType == pt) {
        return tempProcessProb[i];
      }
    }
    return null;
  }

  /**
   * Gets the previously calculated probability of Process p occurring
   * for this Evu.
   * @param p a Process.
   * @return an int, the probability.
   */
  public int getProcessProb(ProcessType p) {
    ProcessProbability probData = findProcessProb(p);
    if (probData != null) { return probData.probability; }
    return 0;
  }

  /**
   * Sets the probability of a process occurring for this Evu.
   * @param pt process whose probability will be set
   * @param prob the probability to be set
   */
  public void setProcessProb(ProcessType pt, int prob) {
    ProcessProbability probData = findProcessProb(pt);
    if (probData != null) {
      probData.probability = prob;
    }
  }

  /**
   * Gets the float version of process probability.
   * @param p the process whose probability is sought
   * @return the float version of a specified process proability
   */
  public float getFloatProcessProb(ProcessType p) {
    return ( (float)getProcessProb(p) / (float)Utility.pow(10,2) );
  }

  /**
   * Attempts to determine whether this unit spread an average of extreme fire.
   * This is used by the Emissions class.
   * @param tStep time step being evaluated
   * @return the probability of a fire spread event, returns NOPROB if state is null or state does not have any fires.
   */
  public int getFireSpreadType(int tStep) {
    VegSimStateData state = getState(tStep);
    if (state == null) { return NOPROB; }

    ProcessType  processType = getState(tStep).getProcess();

    if ((processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
        (processType.equals(ProcessType.MIXED_SEVERITY_FIRE)  == false) &&
        (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE)  == false)) {
      return NOPROB;
    }

    int tmpProb = state.getProb();
    int result = Evu.S;

    if (tmpProb == Evu.S || tmpProb == Evu.SE) {
      return tmpProb;
    } else {
      AreaSummary areaSummary = Simpplle.getAreaSummary();

      return areaSummary.getOriginFireSpreadType(this,tStep);
    }
  }

  /**
   * Gets the Treatment that was last applied to this unit (if any).
   * @return a Treatment or null.
   */
  public Treatment getLastTreatment() {
    int       status;
    Treatment treat = null;

    if (treatment == null) {
      return null;
    }

    if (treatment.size() == 0) {
      treat = null;
    } else {
      treat  = (Treatment) treatment.lastElement();
      status = treat.getStatus();
      if (status != Treatment.EFFECTIVE &&
          status != Treatment.APPLIED) {
        treat = null;
      }
    }
    return treat;
  }

 /**
  * Checks if the supplied treatment occurred within the last 10 years by passing 1 as N to treatmentOccurredLastNDecades.
  * @param treatments the array of treatments being checked if it occured in the past
  * @return true if treatment occurred in the past
  */
  public boolean checkPastTreatments(TreatmentType[] treatments) {
    return treatmentOccurredLastNDecades(treatments,1);
  }

  /**
   * Method to check if a treatment has occurred at all in the past based only on treatment,
   * variable n representing time steps being evaluated.
   *
   * @param treatments the array of treatments to be evaluated within an Evu for past n time steps
   * @param n variable representing time steps
   * @return true if treatment occurred in specified number of time steps
   */
  private boolean treatmentOccurredLastNDecades(TreatmentType[] treatments, int n) {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    Treatment    treat;
    int cTime    = simulation.getCurrentTimeStep();
    int numSteps = (simulation.isYearlyTimeSteps()) ? (n*10) : n;

    for(int ts=cTime; ts>(cTime-numSteps); ts--) {
      treat = getTreatment(ts);
      if (treat == null) { continue; }
      for(int i=0; i<treatments.length; i++) {
        if (treatments[i] == treat.getType()) { return true; }
      }
    }
    return false;
  }

  /**
   * Gets the Treatment being applied in the current time step, if any.
   * @return a Treatment or null.
   */
  public Treatment getCurrentTreatment() {
    return getTreatment(Simulation.getInstance().getCurrentTimeStep());
  }

  /**
   * Gets the treatment in the Evu at a particular time step.
   * @param tStep time step being evaluated
   * @return the treatment occurring at a specified time step.
   */
  public Treatment getTreatment(int tStep) {
    return getTreatment(tStep,true);
  }

  /**
   * Gets the vector for a particular set of treatments.
   * @return a vector of treatments.
   */
  public Vector getTreatments() { return treatment; }

  /**
   * Gets the Treatment for a given time step, if any.
   * @param tStep the time step being evaluated
   * @param noInfeasible true for only successful treatments
   * @return a Treatment or null.
   */
  public Treatment getTreatment(int tStep, boolean noInfeasible) {
    int       size, i, status = -1;
    Treatment treat = null;

    if (treatment == null) {
      return null;
    }

    size = treatment.size();

    for(i=0;i<size;i++) {
      treat = (Treatment) treatment.elementAt(i);
      if (treat.getTimeStep() == tStep) {
        status = treat.getStatus();
        break;
      }
      treat = null;
    }
    if (treat != null && noInfeasible &&
        (status == Treatment.INFEASIBLE || status == Treatment.NOT_APPLIED)) {
      treat = null;
    }
    return treat;
  }

  /**
   * Once a treatment has been performed, store the information in the unit for future use.
   * @param treat the treatment to be added
   */
  public void addTreatment(Treatment treat) {
    if (treat == null) { return; }

    if (treatment == null) {
      treatment = new Vector();
    }

    treatment.addElement(treat);
  }

  /**
   * Applies specified treatment on a specified vegetative type.
   * @param treat the treatment being applied
   * @param newVegType the vegetative type being treated
   */
  public void applyTreatment(Treatment treat, VegetativeType newVegType) {
    addTreatment(treat);
    if (newVegType != null) {
      VegetativeType newState = validateNewState(dominantLifeform,newVegType, true);
      if (newState != null) {
        getState().setVegType(newState);
      }
    }
    dominantLifeform = Lifeform.findDominant(getLifeforms());
  }

  /**
   * Sets the data for adjacent Evu
   * @param newAdjData the adjacent data array being set
   */
  public void setAdjacentData(AdjacentData[] newAdjData) {
    adjacentData = newAdjData;
  }

  /**
   * Gets the data for adjacent Evu
   * @return the Adjacent Evu data array
   */
  public AdjacentData[] getAdjacentData() { return adjacentData; }

  /**
   * Loops through adjacent evu's in an area, counts them and checks their ID's validity.
   * If the count of their Id's validity is same as adjacent data array length returns , else will create a new adjacent evu array limited in size
   * to only valid count and transfer adjacent evu data to it.
   */
  public void removeInvalidAdjacents() {
    Area           area = Simpplle.getCurrentArea();
    AdjacentData[] newData;
    int            i, j=0, validCount=0;

    for (i=0; i<adjacentData.length; i++) {
      if (area.isValidUnitId(adjacentData[i].getEvu().getId())) {
        validCount++;
      }
    }
    if (validCount == adjacentData.length) { return; }

    newData = new AdjacentData[validCount];

    for (i=0; i<adjacentData.length; i++) {
      if (area.isValidUnitId(adjacentData[i].getEvu().getId())) {
        newData[j] = adjacentData[i];
        j++;
      }
    }
    adjacentData = newData;
    newData      = null;
  }

  /**
   * Checks if specified Evu is neighboring to this Evu
   * @param unit the evu being evaluated.
   * @return true if Evu being evaluated is adjacent to this Evu
   */
  public boolean isNeighbor(Evu unit) {
    for (int i=0; i<adjacentData.length; i++) {
      if (adjacentData[i].getEvu().getId() == unit.id) {
        return true;
      }
    }
    return false;
  }

  /**
   * Calculates the position of an adjacent evu relative to this Evu.  Choices are N - next to, A - above, B - below, or Unknown.
   * @param adj the adjacent Evu ID
   * @return the char representing relative postion of adjacent evu
   */
  public char getAdjPosition(Evu adj) {
    int adjId = adj.getId();

    for (int i=0; i<adjacentData.length; i++) {
      if (adjacentData[i].getEvu().getId() == adjId) {
        return Simpplle.getCurrentArea().calcRelativePosition(this, adjacentData[i]);
      }
    }
    return NEXT_TO;
  }

  /**
   * Determines if the adjacent unit is downwind relative to this Evu.
   * @param adj the Adjacent Evu ID.
   * @return true if downwind.
   */
  public boolean isAdjDownwind(Evu adj) {
    int adjId = adj.getId();

    for (int i=0; i<adjacentData.length; i++) {
      if (adjacentData[i].getEvu().getId() == adjId) {
        return (adjacentData[i].getWind() == DOWNWIND);
      }
    }
    return false;
  }

  /**
    * Returns an array of strings for display in a list box in the evu analysis dialog.
    *   (e.g. "346 (Above, Downwind)"
    */
  public String[] getAdjAnalysisDisplay() {
    String   pos, wind;
    String[] strList = new String[adjacentData.length];

    for(int i=0;i<adjacentData.length;i++) {
      char posChar = Simpplle.getCurrentArea().calcRelativePosition(this, adjacentData[i]);

      switch (posChar) {
        case 'N': pos = "Next to"; break;
        case 'A': pos = "Above";   break;
        case 'B': pos = "Below";   break;
        default:  pos = "Unknown"; break;
      }
      wind = (isAdjDownwind(adjacentData[i].getEvu())) ? "Downwind" : "No wind";

      strList[i] = Integer.toString(adjacentData[i].getEvu().getId()) +
                              " (" + pos + ", " + wind + ")";
    }
    return strList;
  }

  /**
   * Gets the string literal array of this Evu's associated Aquatic Units.
   * @return the associated Aquatic Units of this Evu
   */
  public String[] getAssosAqauticUnitDisplay() {
    if (assocAquaticUnits == null) { return null; }

    String   status;
    String[] strList = new String[assocAquaticUnits.size()];
    int i=0;
    for (ExistingAquaticUnit eau : assocAquaticUnits) {
      status = eau.getStatus().toString();
      strList[i] = Integer.toString(eau.getId()) + " (" + status + ")";
      i++;
    }
    return strList;
  }

  /**
   * Sets the state at begining time step.  Sets the life forms, probability, season, succession, treatment thinning cycle.
   */
  public void setBeginTimeStepState() {
    int cStep = Simulation.getCurrentTimeStep();

    Season season = Simulation.getInstance().getCurrentSeason();
    if (Simulation.getInstance().isDiscardData() &&
        cStep > simData.length &&
        (season == Season.SPRING || season == Season.YEAR)) {
      Flat3Map map = simData[0];
      for (int i=0; i<simData.length-1; i++) {
        simData[i] = simData[i+1];
      }
      simData[simData.length-1] = map;


      simData[simData.length-1].clear();

    }

    Set<Lifeform> lives = findLifeformsPriorSeason(cStep, season);

    for (Lifeform lifeform : lives) {
      int run = Simulation.getCurrentRun();
      VegSimStateData priorState = getStatePriorSeason(cStep,lifeform,season);
      VegSimStateData state = new VegSimStateData(getId(),cStep,run,priorState);
      // Make sure process is not re-locked in.
      if (state.getProb() == Evu.L) {
        state.setProb((short) 0);
      }
      state.setSeason(Simulation.getInstance().getCurrentSeason());

      MultiKey key = LifeformSeasonKeys.getKey(lifeform,state.getSeason());
      int index = getSimDataIndex(cStep);
      if (index < 0) {
        throw new RuntimeException("Attempted access to unavailable time step");
      }
      simData[index].put(key,state);
    }

    // Update the Succession/Treatment Thinning Cycle count;
    VegSimStateData state = getState(cStep-1);
    SizeClass sizeClass = state.getVeg().getSizeClass().getBase();
    ProcessType process = state.getProcess();

    if (process != ProcessType.SUCCESSION &&
        process != ProcessType.MIXED_SEVERITY_FIRE &&
        process != ProcessType.LIGHT_SEVERITY_FIRE) {
      cycleSizeClass = null;
      cycleSizeClassCount = 0;
    }
    if (sizeClass != SizeClass.MEDIUM &&
        sizeClass != SizeClass.POLE   &&
        sizeClass != SizeClass.LARGE) {
      cycleSizeClass = null;
      cycleSizeClassCount = 0;
    }
    if (cycleSizeClass == null || cycleSizeClass != sizeClass.getBase()) {
      cycleSizeClass = sizeClass.getBase();
      cycleSizeClassCount = 1;
    }
    else {
      cycleSizeClassCount++;
    }
  }

  /**
   * Find the last season that has data, in the case of the wyoming and similar zone there may not be data in all seasons.
   * @param ts time step
   * @param season the season being evaluated - prior season occurs previous to this one
   * @return Set of life forms from a prior season
   */
  private Set<Lifeform> findLifeformsPriorSeason(int ts, Season season) {
    Set<Lifeform> lives=null;

    if (season == Season.YEAR) {
      return getLifeforms(ts - 1, Season.YEAR);
    }

    if (ts == 1 && season == Season.SPRING) {
      return getLifeforms(0, Season.YEAR);
    }

    int count=0;
    while (lives==null && count < 4) {
      if (season == Season.SPRING) {
        ts--;
        count = 0;
      }
      if (ts == 0) { return getLifeforms(0, Season.YEAR); }

      Season priorSeason = Season.getPriorSeason(season);
      lives = getLifeforms(ts, priorSeason);
      if (lives.size() == 0) { lives = null; }
      season = priorSeason;
      count++;
    }

    return lives;

  }

  /**
   * Gets the vegetative simulation state of a prior season for this Evu.
   * @param ts time step used to get current state.
   * @param lifeform the life form used to find current state
   * @param season the season used to find current state
   * @return the prior vegetative state for Evu
   */
  private VegSimStateData getStatePriorSeason(int ts, Lifeform lifeform, Season season) {
    VegSimStateData state=null;

    if (season == Season.YEAR) {
      return getState(ts-1,lifeform,Season.YEAR);
    }

    if (ts == 1 && season == Season.SPRING) {
      return getState(0,lifeform,Season.YEAR);
    }

    int count=0;
    while (state==null && count < 4) {
      if (season == Season.SPRING) {
        ts--;
        count = 0;
      }
      if (ts == 0) { return getState(0,lifeform,Season.YEAR); }

      Season priorSeason = Season.getPriorSeason(season);
      state = getState(ts,lifeform,priorSeason);
      season = priorSeason;
      count++;
    }

    return state;
  }

  /**
   * Updates the current state with a new vegetative type.
   * @param vegType the new vegetative type being set.
   */
  public void updateState(VegetativeType vegType) {
    getState().setVegType(vegType);
  }

  /**
   * Uses the current simulation time step along with life form to get current vegetative state, then sets a process, probability, and season for that state.
   *
   * @param lifeform used to get the current vegetative state
   * @param process the new process
   * @param prob the new probability
   * @param season the new season
   */
  public void updateState(Lifeform lifeform, ProcessType process, short prob,Climate.Season season) {
    int cStep = Simulation.getCurrentTimeStep();
    VegSimStateData state = getState(cStep,lifeform);

    state.setProcess(process);
    state.setProb(prob);
    state.setSeason(season);
  }

  /**
   * Creates a new vegetative simultion state data object with the passed in parameters
   * @param tStep the time step of the new state.
   * @param run the run of the new state
   * @param vegType the vegetative type of the new state
   * @param process the process of the new state
   * @param prob the probability of the new state
   * @param season the season of the new state
   * @return
   */
  public VegSimStateData newState(int tStep, int run, VegetativeType vegType, ProcessType process, short prob, Season season) {
    VegSimStateData state = new VegSimStateData(getId(),tStep,run,vegType,process,prob);
    newState(tStep, state, season);
    return state;
  }

  /**
   * Creates a new veg state object.
   * @param tStep
   * @param state
   * @param season
   */
  private void newState(int tStep, VegSimStateData state, Season season) {
    Lifeform lifeform = state.getLifeform();
    if (Area.multipleLifeformsEnabled() == false) {
      lifeform = dominantLifeform;
    }

    MultiKey key = LifeformSeasonKeys.getKey(lifeform,season);
    int index = getSimDataIndex(tStep);
    if (index < 0) {
      throw new RuntimeException("Attempted access to unavailable time step");
    }
    simData[index].put(key,state);
  }

  /**
   * Called if the user loads their own pathway file which replaces
   * one already loaded by the system.  The function makes sure that
   * the Veg Types are remapped to the newly loaded ones and that
   * Veg Types that do not exist are created so they can be identified
   * to the user as invalid states and corrected via the Evu editor
   * or by loading a pathway file that has the missing data.
   * Note:  Since we have to change the current state, all simulation
   *        will be cleared before this function is called.
   * @return returns true if loading of the new pathway created an invalid VegetativeType
   */
  public boolean updatePathwayData() {
    boolean   isInvalid  = false;

    if (updateHtGrp()) { isInvalid = true; }

    for (Lifeform lifeform : getLifeforms(0,Season.YEAR)) {
      VegSimStateData state = getState(0,lifeform,Season.YEAR);
      if (state == null) {
        isInvalid = true;
        continue;
      }


      VegetativeType vegType = htGrp.getVegetativeType(state.getVeg().toString());
      if (vegType == null) {
        Species   species    = state.getVeg().getSpecies();
        SizeClass sizeClass  = state.getVeg().getSizeClass();
        int       age        = state.getVeg().getAge();
        Density   density    = state.getVeg().getDensity();

        vegType = new VegetativeType(species,sizeClass,age,density);
        isInvalid = true;
      }
      state.setVeg(vegType);

    }

    return isInvalid;
  }

  /**
   * Remap the htGrp reference to the newly loaded pathway.
   * if the habitat type group does not exist then mark create
   * a new one.  The area will later be marked invalid because
   * we created an invalid habitat type group.
   */
  public boolean updateHtGrp() {
    String groupName = htGrp.getName();
    htGrp = HabitatTypeGroup.findInstance(groupName);
    if (htGrp == null) {
      htGrp = new HabitatTypeGroup(groupName);
      return true;
    }
    return false;
  }

  /**
   * Gets the string literal version of this Evu's special area.
   * @return the special area , unknown if one is not present
   */
  public String getSpecialArea() {
    return ((specialArea == null) ? "UNKNOWN" : specialArea);
  }

  /**
   * Gets a string version of the special area within this Evu
   * @return the special area.
   */
  public String getSpecialAreaEditor() { return specialArea; }

  /**
   * Sets the special area for this Evu.
   * @param str the special area to be set for this Evu.
   */
  public void setSpecialArea(String str) { specialArea = str; }

  /**
   * Gets the string literal version of this Evu's ownership.
   * @return the ownership for this Evu, unknown if null
   */
  public String getOwnership() {
    return ((ownership == null) ? "UNKNOWN" : ownership);
  }

  /**
   *Gets the string literal ownership for this Evu
   * @return the ownership for this Evu, unknown if null
   */
  public String getOwnershipEditor() { return ownership; }

  /**
   * Sets the ownership for this evu
   * @param str the ownership to be set
   */
  public void setOwnership(String str) { ownership = str; }

  /**
   * Gets the Evu unit number.
   * @return the unit number
   */
  public String getUnitNumber() { return unitNumber; }

  /**
   * Sets the Unit number
   * @param newUnitNum the unit number to be set.
   */
  public void setUnitNumber(String newUnitNum) { unitNumber = newUnitNum; }

  /**
   * Gets the ignition probability for this Evu
   * @return the ignition probability
   */
  public int getIgnitionProb() { return ignitionProb; }

  /**
   * Gest the fire season probabilty for this Evu
   * @return the fire season probabilt
   */
  public short getFireSeasonProb() {
    return fireSeasonProb;
  }

  /**
   * Gets the fire season for this Evu
   * @return the fire season
   */
  public Season getFireSeason() {
    return fireSeason;
  }

  /**
   * Updates the fire season to the passed in season.  Sets the fire season probability to 1.
   * @param season the fire season
   */
  public void updateFireSeason(Climate.Season season) {
    fireSeason = season;
    fireSeasonProb = 1;
  }

  /**
   * Gets the dominant life form for this Evu
   * @return the Evu's dominant life form
   */
  public Lifeform getDominantLifeform() {
    return dominantLifeform;
  }

  /**
   * Gets the latitude of this Evu
   * @return the Evu latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Gets the longitude of this Evu
   * @return the Evu's longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Gets the Recent regeneration delay for a specified life form
   * @param lifeform the life form being evaluated for recent regeneration delay
   * @return true if there was  a recent regeneration delay
   */
  public boolean getRecentRegenDelay(Lifeform lifeform) {
    return recentRegenDelay[lifeform.getId()];
  }

  /**
   * Calculates the dominant life form for an Evu.  The dominant life form is caluculated in life form from the array of life forms available.
   * These are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, NA
   * Uses the current time step and life form array to get state, then makes sure the evu has life form and that the process is the state process,
   *
   * @param process
   * @return gets the dominant life forms array
   */
  public Lifeform getDominantLifeform(ProcessType process) {
    Lifeform[] lives = Lifeform.getAllValues();
    for (int i=dominantLifeform.getId(); i<lives.length; i++) {
      VegSimStateData state = getState(Simulation.getCurrentTimeStep(),lives[i]);
      if (state == null) { continue; }
      if (hasLifeform(lives[i]) && (process == state.getProcess())) {
        return lives[i];
      }
    }
    return null;
  }

  /**
   * Gets the dominant life form and uses that as the upper limit for loop to go through life forms.  Then checks to make sure the current state process
   * is a fire process
   * @return an array of life dominant fire process life forms
   */
  public Lifeform getDominantLifeformFire() {
    Lifeform[] lives = Lifeform.getAllValues();
    for (int i=dominantLifeform.getId(); i<lives.length; i++) {
      if (hasLifeform(lives[i]) == false) { continue; }

      VegSimStateData state = getState(Simulation.getCurrentTimeStep(),lives[i]);
      if (state != null && state.getProcess().isFireProcess()) {
        return lives[i];
      }
    }
    return null;
  }

  /**
   * Sets the ignition probability of the Evu
   * @param val the Evu's ignition probability.
   */
  public void setIgnitionProb(int val) { ignitionProb = val; }

  /**
   * Sets the latitude of the Evu.
   * @param latitude The Evu's latitude
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * Sets the logitude for the Evu.
   * @param longitude the Evu's longitude
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  /**
   * Sets the recent regeneration delay within an the recent Regen Delay boolean array for a specified life form.
   * @param lifeform life form to have its recent regeneration delay set... uses the ID as index into boolean recen regen delay array
   * @param recentRegenDelay true if recent regeneration delay
   */
  public void setRecentRegenDelay(Lifeform lifeform, boolean recentRegenDelay) {
    this.recentRegenDelay[lifeform.getId()] = recentRegenDelay;
  }

  /**
   * Gets species frequency for this Evu
   * @return hash map with Evu's species frequency keyed with to this Evu
   * @throws SimpplleError caught in GUI
   */
  public HashMap<SimpplleType,MyInteger> getSpeciesFrequency() throws SimpplleError {
    MultipleRunSummary mrSummary = Simpplle.getCurrentSimulation().getMultipleRunSummary();
    return mrSummary.getFrequency(this,Evu.SPECIES);
  }

  /**
   * Gets size class frequency for this Evu
   * @return hash map with Evu's size class frequency keyed with to this Evu
   * @throws SimpplleError caught in GUI
   */
  public HashMap<SimpplleType,MyInteger> getSizeClassFrequency() throws SimpplleError {
    MultipleRunSummary mrSummary = Simpplle.getCurrentSimulation().getMultipleRunSummary();
    return mrSummary.getFrequency(this,Evu.SIZE_CLASS);
  }

  /**
   * Gets density frequency for this Evu
   * @return hash map with Evu's densityfrequency keyed with to this Evu
   * @throws SimpplleError caught in GUI
   */
  public HashMap<SimpplleType,MyInteger> getDensityFrequency() throws SimpplleError {
    MultipleRunSummary mrSummary = Simpplle.getCurrentSimulation().getMultipleRunSummary();
    return mrSummary.getFrequency(this,Evu.DENSITY);
  }

  /**
   * Gets Process frequency for this Evu
   * @return hash map with Evu's Process frequency keyed with to this Evu
   * @throws SimpplleError caught in GUI
   */
  public HashMap<SimpplleType,MyInteger> getProcessFrequency() throws SimpplleError {
    MultipleRunSummary mrSummary = Simpplle.getCurrentSimulation().getMultipleRunSummary();
    return mrSummary.getFrequency(this,Evu.PROCESS);
  }

  /**
   * Method to decipher if history is to be no multiple history or a single history.  As long as current simulation is not null this will depend on whether the
   * user choose a multiple run summary or there is an existing muliple run summmary (has been choosen in past)
   * @return result of call to either getMultipleHistory() or getSingleHistory()
   * @throws SimpplleError
   */
  public String getHistory() throws SimpplleError {
    Simulation simulation = Simpplle.getCurrentSimulation();

    if (simulation != null && simulation.isMultipleRun() &&
        simulation.existsMultipleRunSummary()) {
      return getMultipleHistory();
    }
    else {
      return getSingleHistory();
    }
  }

  /**
   * Method to get single history of an Evu with all information pertaining to Evu, Eau, associated aquatic units, roads, and trails.  This will include time, season, lifeform, resulting state, process, probability,
   * original process, distance to water, distance to road, distance trail, tracking species, life form , vegetative types,
   * Note** In the past this had an expanded section for Wyoming.  This was eliminated for OpenSimpple V1.0
   * Brian Losi 10/28/13
   * @return
   */
  public String getSingleHistoryExpanded() {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    StringBuilder sb = new StringBuilder();
    Formatter formatter = new Formatter(sb, Locale.US);

    WaterUnitData distWaterData = new WaterUnitData();

    int nSteps = 0;
    if (simulation != null) {
      nSteps = simulation.getNumTimeSteps();
    }

    formatter.format("%4s %6s %17s %41s %25s %4s %25s %23s %20s %21s %s%n",
            "Time", "Season",  "Lifeform", "Resulting State", "Process", "Prob",
            "Original Process", "Distance to Water", "Distance to Road",
            "Distance to Trail", "Tracking Species");

    ProcessType oProcess        = null;
    MultiKeyMap gappedProcesses = null;
    MultiKeyMap DProcesses      = null;
    if (Simpplle.getAreaSummary() != null) {
      gappedProcesses = Simpplle.getAreaSummary().getGappedProcesses();
      DProcesses = Simpplle.getAreaSummary().getDProcesses();
    }

    Season[] seasons = Climate.allSeasons;

    int index=0;
    for (int ts=0; ts<=nSteps; ts++) {
      if (getSimDataIndex(ts) < 0) { continue; }
      for (int i=0; i<seasons.length; i++) {
        boolean roadsDone=false;
        boolean trailsDone=false;
        for (Lifeform lifeform : getSortedLifeforms(ts,seasons[i])) {
          int run = Simulation.getCurrentRun();
          if (gappedProcesses != null) {
            oProcess = (ProcessType) gappedProcesses.get(this,lifeform,run,ts);
          }
          if (oProcess == null && DProcesses != null) {
            oProcess = (ProcessType) DProcesses.get(this, lifeform, run, ts);
          }
          String oProcessStr = (oProcess != null) ? oProcess.toString() : "N/A";

          VegSimStateData state = getState(ts, lifeform, seasons[i]);
          if (state == null) { continue; }

          formatter.format("%4d %6s %17s %41s %25s ",
                           ts, state.getSeasonString(),
                           state.getLifeform().toString(),
                           state.getVegType().toString(),
                           state.getProcess().toString());

          int prob = state.getProb();
          if (prob > 0) {
            formatter.format("%4.0f ", (prob / 100.0f));
          }
          else {
            formatter.format("%4s ", state.getProbString());
          }

          formatter.format("%25s ", oProcessStr);

          if (ts == 0 || state.getSeason() == Season.YEAR) {
            formatter.format("%23s ", "N/A");
          }
          else {
            index = ((ts-1)*4) + state.getSeason().ordinal();
            double dist = distanceToWater(distWaterData, index);
            boolean isPerm = distWaterData.permanentWater;
            NaturalElement unit = distWaterData.unit;
            char kind = (isPerm ? 'P' : 'T');
            if (unit instanceof ExistingAquaticUnit) {
              ExistingAquaticUnit eau = (ExistingAquaticUnit) unit;
              formatter.format("%6.0fft EAU-%-6d (%s) ", dist, eau.getId(),
                               kind);
            }
            else if (unit instanceof Evu) {
              Evu evu = (Evu) unit;
              formatter.format("%6.0fft EVU-%-6d (%s) ", dist, evu.getId(),
                               kind);
            }
          }

          if (nearestRoad == null) { roadsDone = true; }

          if (ts == 0  || roadsDone) {
            formatter.format("%20s ", "N/A");
          }
          else {
            roadsDone = true;

            index = ts;
            RoadUnitData distRoadData = getNearestOpenRoad(index);
            double dist;
            if (distRoadData == null || distRoadData.evu == null) {
              dist = MAX_ROAD_DIST;
            }
            else if (distRoadData.evu == this) {
              dist = 1;

            }
            else {
              dist = distanceToEvu(distRoadData.evu);
            }
            ManmadeElement unit = null;
            if (distRoadData != null) {
              unit = distRoadData.road;
            }

            if (unit instanceof Roads) {
              Roads road = (Roads)unit;
              formatter.format("%6.0fft ROAD-%-6d ", dist, road.getId());
            }
            if (unit == null) {
              formatter.format("%6.0fft %-11s ", dist, "MAX-DIST");
            }
          }

          if (nearestTrail == null) { trailsDone = true; }

          if (ts == 0  || trailsDone) {
            formatter.format("%21s ", "N/A");
          }
          else {
            trailsDone = true;

            index = ts;
            TrailUnitData distTrailData = getNearestOpenTrail(index);
            double dist;
            if (distTrailData == null || distTrailData.evu == null) {
              dist = MAX_TRAIL_DIST;
            }
            else if (distTrailData.evu == this) {
              dist = 1;

            }
            else {
              dist = distanceToEvu(distTrailData.evu);
            }
            ManmadeElement unit = null;
            if (distTrailData != null) {
              unit = distTrailData.trail;
            }

            if (unit instanceof Trails) {
              Trails trail = (Trails)unit;
              formatter.format("%6.0fft TRAIL-%-6d ", dist, trail.getId());
            }
            if (unit == null) {
              formatter.format("%6.0fft %-11s ", dist, "MAX-DIST");
            }
          }

          InclusionRuleSpecies[] allSp = state.getTrackingSpeciesArray();
          if (allSp != null) {
            allSp = allSp.clone();
            Arrays.sort(allSp);
            for (InclusionRuleSpecies species : allSp) {
              float pct = state.getSpeciesPercent(species);
              formatter.format("%s %3.1f ", species.toString(), pct);
            }
          }
          formatter.format("%n");
        }
      }
      formatter.format("%n");
    }

    return sb.toString();
  }

  /**
   * Method to get single history.  In the past this had an expanded section for Wyoming.  This was eliminated for OpenSimpple V1.0
   * Now this just calls getSingleHistoryExpanded().
   * Brian Losi 10/28/13
   * @return call to single history expanded.
   */
  public String getSingleHistory() {
    return getSingleHistoryExpanded();
  }

  /**
   * This method creates a formatted string of species size, class, density, and process frequencies
   * @return
   * @throws SimpplleError
   */
  public String getMultipleHistory() throws SimpplleError {
    PrintWriter  fout;
    StringWriter strOut = new StringWriter();

    HashMap<SimpplleType,MyInteger> speciesFreq;
    HashMap<SimpplleType,MyInteger> sizeClassFreq;
    HashMap<SimpplleType,MyInteger> densityFreq;
    HashMap<SimpplleType,MyInteger> processFreq;

    int           numData, i;
    SimpplleType  key;
    MyInteger     freq;

    fout = new PrintWriter(strOut);

    speciesFreq   = getSpeciesFrequency();
    sizeClassFreq = getSizeClassFrequency();
    densityFreq   = getDensityFrequency();
    processFreq   = getProcessFrequency();

    if (speciesFreq == null || sizeClassFreq == null||
        densityFreq == null || processFreq == null) {
      fout.println("No Data available to compute frequencies");
      fout.flush();
      strOut.flush();
      return strOut.toString();
    }

    fout.print(Formatting.fixedField("Species",22,true));
    fout.print(Formatting.fixedField("Size Class",23,true));
    fout.print(Formatting.fixedField("Density",17,true));
    fout.println(Formatting.fixedField("Process",26,true));

    fout.print(Formatting.fixedField("Frequencies",22,true));
    fout.print(Formatting.fixedField("Frequencies",23,true));
    fout.print(Formatting.fixedField("Frequencies",17,true));
    fout.println(Formatting.fixedField("Frequencies",26,true));

    Iterator speciesIt   = speciesFreq.keySet().iterator();
    Iterator sizeClassIt = sizeClassFreq.keySet().iterator();
    Iterator densityIt   = densityFreq.keySet().iterator();
    Iterator processIt   = processFreq.keySet().iterator();

    // Determine which one has the most number items
    numData = speciesFreq.size();
    if (numData < sizeClassFreq.size()) { numData = sizeClassFreq.size(); }
    if (numData < densityFreq.size()) { numData = densityFreq.size(); }
    if (numData < processFreq.size()) { numData = processFreq.size(); }

    // Now print out the information
    // Field widths:
    //    SPECIES (16), SIZE CLASS (17), DENSITY (1), PROCESS (20)
    //    FREQUENCY (4)  (1 space, 3 digits)

    for(i=0;i<numData;i++) {
      if (speciesIt.hasNext()) {
        key  = (SimpplleType)speciesIt.next();
        freq = speciesFreq.get(key);
        fout.print(Formatting.fixedField(key.toString(),16,true));
        fout.print(Formatting.fixedField(freq.intValue(),4) + "% ");
      } else {
        fout.print(Formatting.fixedField("",22,true));
      }
      if (sizeClassIt.hasNext()) {
        key  = (SimpplleType)sizeClassIt.next();
        freq = sizeClassFreq.get(key);
        fout.print(Formatting.fixedField(key.toString(),17,true));
        fout.print(Formatting.fixedField(freq.intValue(),4) + "% ");
      } else {
        fout.print(Formatting.fixedField("",23,true));
      }
      if (densityIt.hasNext()) {
        key  = (SimpplleType)densityIt.next();
        freq = densityFreq.get(key);
        fout.print(Formatting.fixedField(key.toString(),11,true));
        fout.print(Formatting.fixedField(freq.intValue(),4) + "% ");
      } else {
        fout.print(Formatting.fixedField("",17,true));
      }
      if (processIt.hasNext()) {
        key  = (SimpplleType)processIt.next();
        freq = processFreq.get(key);
        fout.print(Formatting.fixedField(key.toString(),20,true));
        fout.print(Formatting.fixedField(freq.intValue(),4) + "%");
      } else {
        fout.print(Formatting.fixedField("",26,true));
      }
      fout.println();
    }

    fout.flush();
    strOut.flush();
    return strOut.toString();
  }

  /**
   * This method creates a string for treatment history of this Evu.  The report includes treatment information (if there is any)
   * including time treatement, status, treatment state change and effectiveness.
   * @return
   */
  public String getTreatmentHistory() {
    PrintWriter  fout;
    StringWriter strOut   = new StringWriter();
    Simulation   simulation = Simpplle.getCurrentSimulation();
    int          nSteps, step;
    Treatment    treat;
    int          status;

    fout = new PrintWriter(strOut);

    if ((simulation != null &&
         simulation.isMultipleRun() &&
         simulation.existsMultipleRunSummary()) ||
        (simulation == null && (getTreatment(0,false) == null))) {
      fout.println("No Treatment History available.");
      fout.flush();
      strOut.flush();
      return strOut.toString();
    }

    nSteps = (simulation == null) ? 0 : simulation.getNumTimeSteps();
    fout   = new PrintWriter(strOut);

    fout.println("Time Treatment (Status)");
    fout.print(Formatting.fixedField("",5));
    fout.println("State Change");

    for(step=0;step<=nSteps;step++) {
      fout.print(Formatting.fixedField(step,5,true));
      treat = getTreatment(step,false);
      if (treat == null) {
        fout.println("** No Scheduled Treatment **");
        continue;
      }
      fout.print(treat.toString());

      status = treat.getStatus();
      switch (status) {
        case Treatment.EFFECTIVE:   fout.println(" (Effective)");    break;
        case Treatment.APPLIED:     fout.println(" (Applied)");      break;
        case Treatment.INFEASIBLE:  fout.println(" (Not Feasible)"); break;
        case Treatment.NOT_APPLIED: fout.println(" (Not Applied)");  break;
        default: fout.println();
      }

      Season season = Season.YEAR;
      if (RegionalZone.isWyoming()) {
        season = Season.SPRING;
      }

      Set<Lifeform> lives = findLifeformsPriorSeason(step,season);
      Lifeform prevDomLife = Lifeform.findDominant(lives);
      if ((status == Treatment.EFFECTIVE || status == Treatment.APPLIED) && (step > 0)) {
        fout.print(Formatting.fixedField("",5));

        VegSimStateData state = getState(step-1,prevDomLife);
        String stateStr = (state != null ? state.getVeg().toString() : "Unknown");
        fout.print(stateStr + " --> ");
        fout.println(treat.getSavedState().toString());
      }
      else if ((status == Treatment.EFFECTIVE || status == Treatment.APPLIED) && (step == 0)) {
        fout.print(Formatting.fixedField("",5));
        fout.print("Unknown --> ");
        fout.println(treat.getSavedState().toString());
      }
    }

    fout.flush();
    strOut.flush();
    return strOut.toString();
  }

  /**
   * Print out an human readable representation of the Evu. This will include the Evu ID, the current vegetative state,
   * the habitat group, acreage, ownership, special area, road status, fire management zon, adjacent Evu's, associated
   * aquatic units, roads, trails, treatments - status, effectiveness, and history,
   * @param fout a PrintWriter.
   */
  public void printHistory(PrintWriter fout) throws SimpplleError {
    Formatter formatter = new Formatter(fout, Locale.US);

    formatter.format("%21s: %d%n","Unit Id",getId());

    VegSimStateData state = getState();
    String stateStr = (state != null ? state.getVeg().toString() : "Unknown");
    formatter.format("%21s: %s%n","Current State",stateStr);

    formatter.format("%21s: %s%n","Ecological Grouping",getHabitatTypeGroup().toString());
    formatter.format("%21s: %8.2f%n","Acres",getFloatAcres());

    formatter.format("%21s: %s%n","Ownership",ownership);
    formatter.format("%21s: %s%n","Special Area",specialArea);
    formatter.format("%21s: %s%n","Road Status",roadStatus.toString());
    formatter.format("%21s: %s%n","FMZ",fmz);

    {
      formatter.format("%21s:%n", "Adjacent Units");
      String[] tmpUnits = getAdjAnalysisDisplay();
      for (int i = 0; i < tmpUnits.length; i++) {
        formatter.format("  %s%n", tmpUnits[i]);
      }
      fout.println();
    }

    if (assocAquaticUnits != null) {
      formatter.format("%21s:%n", "Associated Aquatic Units");
      String[] tmpUnits = getAssosAqauticUnitDisplay();
      if (tmpUnits != null) {
        for (int i = 0; i < tmpUnits.length; i++) {
          formatter.format("  %s%n", tmpUnits[i]);
        }
      }
      fout.println();
    }

    if (assocRoadUnits != null) {
      formatter.format("%21s:%n", "Road Units");
      for (int i=0; i<assocRoadUnits.size(); i++) {
        formatter.format("  %d%n", assocRoadUnits.get(i).getId());
      }
      fout.println();
    }

    if (assocTrailUnits != null) {
      formatter.format("%21s:%n", "Trail Units");
      for (int i=0; i<assocTrailUnits.size(); i++) {
        formatter.format("  %d%n", assocTrailUnits.get(i).getId());
      }
      fout.println();
    }

    int       status;
    Treatment treat;
    if (treatment != null) {
      formatter.format("%21s:%n", "Treatments");
      for(int i=0; i<treatment.size(); i++) {
        treat = (Treatment) treatment.elementAt(i);
        formatter.format("  %14s: %s%n","Name",treat.toString());
        formatter.format("  %14s: %s%n","Time Step",treat.getTimeStep());
        formatter.format("  %14s: %s%n","Result State",treat.getSavedState());

        status = treat.getStatus();
        formatter.format("  %14s:","Status");
        if (status == Treatment.EFFECTIVE || status == Treatment.APPLIED) {
          formatter.format(" %s%n","Successfully Applied");
        }
        else if (status == Treatment.INFEASIBLE) {
          formatter.format(" %s%n","Infeasible");
        }
        else {
          formatter.format(" %s%n","Not Applied");
        }
      }
    }

    if (Simulation.getInstance() != null) {
      formatter.format("%21s:%n","History");
      fout.println(getHistory());
      fout.println();
    }

    for (int i = 0; i < 100; i++) { fout.print("-"); }
    fout.println();
    fout.println();
  }

  /**
   * Makes an arraylist of all the possible special probabilities.
   * @return
   */
  public static ArrayList<String> getAllProbabilitySpecial() {
    ArrayList<String> values = new ArrayList<String>();

    values.add(D_STR);
    values.add(L_STR);
    values.add(S_STR);
    values.add(SE_STR);
    values.add(SFS_STR);
    values.add(SUPP_STR);
    values.add(COMP_STR);
    values.add(GAP_STR);

    return values;
  }

  /**
   * Gets the string version of probability logic for use in data storage.
   * @param prob the probability in integer form
   * @return string version of probabiliyt
   */
  public static String getProbabilitySaveString(int prob) {
    switch (prob) {
      case Evu.D:      return Evu.D_STR;
      case Evu.L:      return Evu.L_STR;
      case Evu.S:      return Evu.S_STR;
      case Evu.SE:     return Evu.SE_STR;
      case Evu.SFS:    return Evu.SFS_STR;
      case Evu.SUPP:   return Evu.SUPP_STR;
      case Evu.COMP:   return Evu.COMP_STR;
      case Evu.GAP:    return Evu.GAP_STR;
      case Evu.NOPROB: return Evu.NOPROB_STR;
      default:         return IntToString.get(prob);
    }
  }

  /**
   * Gets the probability in readable format.
   * @param prob probability
   * @return string literal version of probility
   */
  public static String getProbabilityPrintString(int prob) {
    return getProbabilityPrintString(prob,2);
  }

  /**
   * Gets the probability in a printable string literal format
   * @param prob the probability
   * @param numDigits
   * @return probability in string literal format
   */
  public static String getProbabilityPrintString(int prob, int numDigits) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(numDigits);

    switch (prob) {
      case Evu.D:      return Evu.D_STR;
      case Evu.L:      return Evu.L_STR;
      case Evu.S:      return Evu.S_STR;
      case Evu.SE:     return Evu.SE_STR;
      case Evu.SFS:    return Evu.SFS_STR;
      case Evu.SUPP:   return Evu.SUPP_STR;
      case Evu.COMP:   return Evu.COMP_STR;
      case Evu.GAP:    return Evu.GAP_STR;
      case Evu.NOPROB: return Evu.NOPROB_STR;
      default:         return nf.format((prob/100));
    }
  }

  /**
   * Parses the probability from string literal.  Then returns the int version of the string.
   * @param str the Evu probability to be parsed
   * @return an int representation of probability passed as a string.
   */
  public static int parseProbabilityString(String str) {

    if      (str.equals(Evu.D_STR))      return Evu.D;
    else if (str.equals(Evu.L_STR))      return Evu.L;
    else if (str.equals(Evu.S_STR))      return Evu.S;
    else if (str.equals(Evu.SE_STR))     return Evu.SE;
    else if (str.equals(Evu.SFS_STR))    return Evu.SFS;
    else if (str.equals(Evu.SUPP_STR))   return Evu.SUPP;
    else if (str.equals(Evu.COMP_STR))   return Evu.COMP;
    else if (str.equals(Evu.GAP_STR))    return Evu.GAP;
    else if (str.equals(Evu.NOPROB_STR)) return Evu.NOPROB;
    else {
      try {
        return Integer.parseInt(str);
      } catch (NumberFormatException ex) {
        return Evu.NOPROB;
      }
    }
  }

  /**
   * Replaces the initial state with the last season of VegSimStateData for each lifeform from the last timestep
   * and calls restoreInitialConditions().
   */
  public void makeSimulationReady() {

    if (simData != null) {

      initialState.clear();

      // Create an array of unique lifeforms from the the last time step

      ArrayList<Lifeform> lives = new ArrayList<>();

      Flat3Map data = simData[simData.length - 1];
      MapIterator it = data.mapIterator();

      while (it.hasNext()) {

        MultiKey key = (MultiKey)it.next();
        Lifeform lifeform = (Lifeform)key.getKey(0);

        if (lives.contains(lifeform) == false) {
          lives.add(lifeform);
        }
      }

      // Store the last season of VegSimStateData for each lifeform in the initial state with a year season.

      for (int i = 0; i < lives.size(); i++) {

        Lifeform lifeform = lives.get(i);
        Season[] seasons = Climate.allSeasons;

        for (int s = seasons.length - 1; s >= 0; s--) {

          MultiKey key = LifeformSeasonKeys.getKey(lifeform,seasons[s]);
          VegSimStateData state = (VegSimStateData)data.get(key);

          if (state != null) {
            key = LifeformSeasonKeys.getKey(lifeform,Season.YEAR);
            initialState.put(key,state);
            break;
          }
        }
      }
    }

    restoreInitialConditions();

  }

  /**
   * Clears stored VegSimStateData and Treatments, and sets haveHighSpruceBeetle to false
   */
  public void restoreInitialConditions() {

    simData   = null;
    treatment = null;
//    processProb.clear();

    haveHighSpruceBeetle = false;

  }

  /**
   * Creates a temporary treatment instance for this Evu, then clears out the treatment vector.  If temporary treatment is not null, adds it back as a treatment.
   */
  private void clearSimulationTreatments() {
    if (treatment == null) { return; }

    Treatment tmpTreatment = this.getTreatment(0);
    treatment.clear();

    if (tmpTreatment != null) {
      addTreatment(tmpTreatment);
    }
  }

  /**
   * Initializes a simulation by setting water units to null and getting the number of time steps at the simulation instance.
   */
  public static void staticInitSimulation() {
    waterUnits = null;
    int numSteps = Simulation.getInstance().getNumTimeSteps();
  }

  /**
   * Used LegacyEvu in loading old simulation data
   * @param numSteps int
   */
  public void initSimDataLegacy(int numSteps) {
    simData = new Flat3Map[numSteps+1];
  }

  /**
   * Initialize some fields now that we know the number of time steps the simulation will have. This gets called at
   * the beginning of each simulation.
   */
  public void initSimulation() {

    Simulation simulation = Simpplle.getCurrentSimulation();

    int numSteps = simulation.getNumTimeSteps();

    MapIterator it = initialState.mapIterator();
    int count = 0;
    while (it.hasNext()) {
      MultiKey key = (MultiKey)it.next();
      Lifeform lifeform = (Lifeform)key.getKey(0);
      if (count == 0) {
        dominantLifeform = lifeform;
      } else {
        dominantLifeform = Lifeform.getMostDominant(dominantLifeform, lifeform);
      }
      count++;
    }

    if (simulation.getCurrentRun() == 0) {
      addMissingTrackSpecies();
    }

    if (Simulation.getInstance().isDiscardData()) {

      int pastInMem = Simulation.getInstance().getPastTimeStepsInMemory();
      simData = new Flat3Map[pastInMem + 1];
      simData[0] = new Flat3Map();

    } else {

      simData = new Flat3Map[numSteps + 1];
      simData[0] = initialState;

    }

    for(int i = 1; i < simData.length; i++) {
      simData[i] = new Flat3Map();
    }

    if (simulation.isMultipleRun() == false || simulation.getCurrentRun() == 0) {

      ArrayList processTypes = Process.getSimulationProcesses();

      int needLaterCount = 0;

      for (int i=0; i<processTypes.size(); i++) {
        ProcessType processType = (ProcessType)processTypes.get(i);
        if (processType == ProcessType.LIGHT_WSBW ||
            processType == ProcessType.SEVERE_WSBW ||
            processType == ProcessType.LIGHT_LP_MPB ||
            processType == ProcessType.SEVERE_LP_MPB ||
            processType == ProcessType.PP_MPB) {
          needLaterCount++;
        }
      }

      needLaterProcessProb = new ProcessProbability[needLaterCount];
      tempProcessProb      = new ProcessProbability[processTypes.size() - needLaterCount];

      int probIndex = 0;
      int needLaterIndex = 0;
      for (int i = 0; i < processTypes.size(); i++) {
        ProcessType processType = (ProcessType)processTypes.get(i);
        if (processType != ProcessType.LIGHT_WSBW &&
            processType != ProcessType.SEVERE_WSBW &&
            processType != ProcessType.LIGHT_LP_MPB &&
            processType != ProcessType.SEVERE_LP_MPB &&
            processType != ProcessType.PP_MPB) {
          tempProcessProb[probIndex] = new ProcessProbability(processType);
          probIndex++;
        } else {
          needLaterProcessProb[needLaterIndex] = new ProcessProbability(processType);
          needLaterIndex++;
        }
      }
    }

    if (simulation.isMultipleRun() && simulation.getCurrentRun() == 0) {
      MultipleRunSummary mrSummary = simulation.getMultipleRunSummary();
      mrSummary.initFrequencyCount(this);
    }

    if (simulation.isMultipleRun()) {
      MultipleRunSummary mrSummary = simulation.getMultipleRunSummary();
      mrSummary.updateSummaries(this);
    }

    clearSimulationTreatments();
    haveHighSpruceBeetle = false;

    for (int i = 0; i < regenDelay.length; i++) {
      regenDelay[i] = 0;
      recentRegenDelay[i] = false;
    }

    lastLife = new VegSimStateData[Lifeform.numValues()];
    lastLifeProcessHistory = new Flat3Map();
    // Restore Current State.
//    updateCurrentState(getVegetativeType());

    FireSuppEventLogic.getInstance().clearSuppressed();

    cycleSizeClass = null;
    cycleSizeClassCount = 0;

  }

  /**
   * Initialize some fields at the beginning of a multiple run simulation.
   */
  public void initMultipleSimulation() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int numRuns  = simulation.getNumSimulations();
    int numSteps = simulation.getNumTimeSteps();
  }

  /**
   * Initialize the CumulProb field, used to pick a process.
   * Only need one of these, hence the reason its static.
   */
  public static void initCumulProb() {
    cumulProb.clear();
    ArrayList processTypes = Process.getSimulationProcesses();
    for (int i=0; i<processTypes.size(); i++) {
      cumulProb.add(new CumulativeProcessProb((ProcessType)processTypes.get(i)));
    }
  }

/**
 * Finds the cumulative probability for a specified process
 * @param pt the process being evaluated
 * @return the cumulative process probabilty for the passed process, or null if there is none
 */
  public static CumulativeProcessProb findCumulProb(ProcessType pt) {
    if (pt == null) { return null; }

    CumulativeProcessProb data;
    for (int i=0; i<cumulProb.size(); i++) {
      data = (CumulativeProcessProb)cumulProb.get(i);
      if (data.processType.equals(pt)) { return data; }
    }
    return null;
  }

  /**
   * Reset the lpMpbhazard and Ppmpb hazard to null.
   */
  public void reset() {
    setLpMpbHazard(null);
    setPpMpbHazard(null);
  }


  /**
   * Returns the adjusted probability values in a new hash table, which will only have to exist  long enough to choose
   * a process
   */
  private void getAdjustedProb() {
    int                   lower = 0, prob, upper = -1;
    CumulativeProcessProb cumulProbData;

    ArrayList simulationProcesses = Process.getSimulationProcesses();

    // Now adjust the probabilities so they cover a range
    // of numbers.
    for(int i=0; i<simulationProcesses.size(); i++) {
      cumulProbData = findCumulProb((ProcessType)simulationProcesses.get(i));
      if (cumulProbData.lower == 0) {
        cumulProbData.lower = -1;
        cumulProbData.upper = -1;
      }
      else {
        upper += cumulProbData.lower;
        cumulProbData.lower = lower;
        cumulProbData.upper = upper;
        lower = upper + 1;
      }
    }
  }

  /**
   * Compute the hazard for LP-MPB and PP-MPB for this unit.
   */
  public void doHazard() {
    RegionalZone zone = Simpplle.currentZone;

    int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    Area.currentLifeform = Lifeform.TREES;
    if (Area.multipleLifeformsEnabled() == false) {
      Area.currentLifeform = null;
    }
    else if (hasLifeform(Area.currentLifeform,cStep) == false) {
      Area.currentLifeform = null;
      return;
    }
    LpMpb.computeHazard(zone,this);
    PpMpb.computeHazard(zone,this);
    Area.currentLifeform = null;
  }

  /**
   * Calculate the process probabilities for this unit.
   * The code below to set cases where probability is greater than 100% to 100%.  This leads to the 100% process covering most of the 0-100 range
   *    when doing stochastic selection.
   *
   */
  public void doProbability() {
    if (Area.multipleLifeformsEnabled()) {
      doProbabilityMultipleLifeform();
      return;
    }

    ArrayList             simulationProcesses = Process.getSimulationProcesses();
    Process               process;
    ProcessType           processType;
//    ProcessProbability    processProbData;
    CumulativeProcessProb cumulProbData;
    int                   prob, totalProb = 0;
    RegionalZone          zone = Simpplle.getCurrentZone();

    boolean isYearlyRun = Simpplle.getCurrentSimulation().isYearlyTimeSteps();

    // Go through all of the processes and get their
    // probabilities.
    for(int i=0; i<simulationProcesses.size(); i++) {
      processType = (ProcessType)simulationProcesses.get(i);
      if (RegionalZone.isWyoming() &&
          processType.equals(ProcessType.FIRE_EVENT)) {
        prob = 0;
      }
      else {
        process = Process.findInstance(processType);
        prob = process.doProbability(zone, this);

        if (isYearlyRun && process.isYearly() == false &&
            process.getType() != ProcessType.SUCCESSION) {
          prob = Math.round((float)prob / 10.0f);
        }
      }
      if (processType.equals(ProcessType.FIRE_EVENT) == false) {
        prob *= 100;
      }

      if (prob > 10000) { prob = 10000; }
      setProcessProb(processType,prob);

      cumulProbData   = Evu.findCumulProb(processType);
      cumulProbData.lower         = prob;
      totalProb                  += prob;
    }
    // Now adjust succession in cumulProb so that all total 10000.
    ProcessProbability processProbData = findProcessProb(ProcessType.SUCCESSION);
    cumulProbData   = findCumulProb(ProcessType.SUCCESSION);
    processProbData.probability += 10000 - totalProb;
    cumulProbData.lower          = processProbData.probability;
  }


  /**
   * Creates a static arraylist for process types.  is Temporary to save memory.
   */
  private static ArrayList<ProcessType> tmpSimProcesses = new ArrayList<ProcessType>();
/**
 * Method to calculate probabilities for multiple life forms based on current simulation at current time step.
 */
  public void doProbabilityMultipleLifeform() {
    doneSummaryProcesses.clear();

    int tStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    ArrayList<ProcessType> simulationProcesses;
    Lifeform[] allLives = Lifeform.getAllValues();

    for (int l=0; l<allLives.length; l++) {
      if (hasLifeform(allLives[l],tStep) == false) {
        continue;
      }
      Area.currentLifeform = allLives[l];

      tmpSimProcesses.clear();
      simulationProcesses = Process.getSimulationProcesses(allLives[l]);

      for (int i=0; i<simulationProcesses.size(); i++) {
        if (doneSummaryProcesses.contains(simulationProcesses.get(i)) == false) {
          tmpSimProcesses.add(simulationProcesses.get(i));
          doneSummaryProcesses.add(simulationProcesses.get(i));
        }
      }
      doProbabilityMultipleLifeform(tmpSimProcesses);
    }


    Area.currentLifeform = null;
  }

  public void doProbabilityMultipleLifeform(ArrayList<ProcessType> simulationProcesses) {
    Process               process;
    ProcessType           processType;
    double                prob;
    int                   rationalProb;
    RegionalZone          zone = Simpplle.getCurrentZone();

    boolean isYearlyRun = Simpplle.getCurrentSimulation().isYearlyTimeSteps();

    // Go through all of the processes and get their
    // probabilities.
    for(int i=0; i<simulationProcesses.size(); i++) {
      processType = (ProcessType)simulationProcesses.get(i);
      if (RegionalZone.isWyoming() &&
          processType.equals(ProcessType.FIRE_EVENT)) {
        prob = 0;
      }
      else {
        process = Process.findInstance(processType);
        prob = process.doProbability(zone, this);

        if (isYearlyRun && process.isYearly() == false &&
            process.getType() != ProcessType.SUCCESSION) {
          prob = prob / 10.0;
        }
      }
      if (processType.equals(ProcessType.FIRE_EVENT) == false) {
        rationalProb = Simulation.getRationalProbability(prob);

        if (prob > 10000) { prob = 10000; }
        setProcessProb(processType,rationalProb);
      }
      else {
        // Fire Events are already rationalized
        setProcessProb(processType,(int)Math.round(prob));
      }
    }
  }
/**
 * Calculates the fire process probability by passing fire event in the current zone to doProbability
 * @return the probability (as an integer) of fire process.  If somehow this is above 10000 will return 10000
 */
  public int doFireProcessProb() {
    ProcessType processType = ProcessType.FIRE_EVENT;
    RegionalZone zone = Simpplle.getCurrentZone();
    Process process = Process.findInstance(processType);
    Simulation simulation = Simpplle.getCurrentSimulation();

    int prob = process.doProbability(zone, this);

    if (simulation.isYearlyTimeSteps()) {
      prob = Math.round((float)prob / 10.0f);
    }

    if (prob > 10000) { prob = 10000; }

    return prob;
  }
/**
 * Calculates the fire season according to the current simulation and current season.
 */
  public void determineFireSeason() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    Climate.Season season = simulation.getCurrentSeason();

    if (season.equals(Climate.Season.SPRING)) {
      fireSeason = FireEvent.getFireSeason();
    }
  }
  public void doGetProcessWyoming() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    Climate.Season season = simulation.getCurrentSeason();

    int cStep = simulation.getCurrentTimeStep();

    VegSimStateData state = getState(cStep);
    int         unitProb  = state.getProb();
    ProcessType tmpProcess = state.getProcess();
    if (tmpProcess != null && unitProb == L &&
        (tmpProcess.equals(ProcessType.PRAIRIE_DOG_ACTIVE) ||
         tmpProcess.equals(ProcessType.PRAIRIE_DOG_INACTIVE))) {
      if (season == Climate.Season.SPRING) {
        return;
      }
      else {
        updateCurrentProcess(ProcessType.NONE);
        updateCurrentProb(Evu.NOPROB);
        return;
      }
    }

    if (simulation.isStandDevelopment()) {
      if (season == Climate.Season.SPRING) {
        doWyomingSuccession(simulation);
      }
      else {
        updateState(null,ProcessType.NONE, (short)Evu.NOPROB,simulation.getCurrentSeason());
      }
      return;
    }

    if (season == Climate.Season.SPRING) {
      fireSeasonProb = (short)doFireProcessProb();
      int rand = simulation.random();
      if (rand > fireSeasonProb || fireSeasonProb == 0) {
        fireSeasonProb = 0;
      }
    }

    if (season == Climate.Season.SPRING) {
      if (fireSeasonProb > 0 && fireSeason == season) {
        doWyomingFire(simulation,season);
      }
      else {
        doWyomingSuccession(simulation);
      }
    }
    else if (season == Climate.Season.SUMMER) {
      if (fireSeasonProb > 0 && fireSeason == season) {
        doWyomingFire(simulation,season);
      }
      else if (fireSeasonProb > 0 && fireSeason == Climate.Season.SPRING) {
        doWyomingSuccession(simulation);
      }
      else if (fireSeasonProb == 0) {
        doWyomingStochastic(simulation);
      }
      else {
        updateState(null,ProcessType.NONE, (short)Evu.NOPROB,simulation.getCurrentSeason());
      }
    }
    else if (season == Climate.Season.FALL || season == Climate.Season.WINTER) {
      if (fireSeasonProb > 0 && fireSeason == season) {
        doWyomingFire(simulation,season);
      }
      else if (fireSeasonProb > 0 &&
               (fireSeason == Climate.Season.SPRING)) {
        doWyomingStochastic(simulation);
      }
      else {
        updateState(null,ProcessType.NONE, (short)Evu.NOPROB,simulation.getCurrentSeason());
      }
    }

  }
/**
 * Method to calculate wyoming succession.
 * @param simulation
 */
  private void doWyomingSuccession(Simulation simulation) {
//    newState(ProcessType.SUCCESSION,(short)10000,Climate.Season.YEAR);
    updateState(null,ProcessType.SUCCESSION,(short)10000,simulation.getCurrentSeason());
  }
  private void doWyomingFire(Simulation simulation, Climate.Season season) {
    VegSimStateData state = getState();
    if (state != null && fireSeasonProb > 0) {
      updateState(null,ProcessType.FIRE_EVENT, fireSeasonProb,
               simulation.getCurrentSeason());
      lastFireTimeStep = Simulation.getCurrentTimeStep();
      doFireSpecificDoGetProcess(state.getVeg().getSpecies(),dominantLifeform);

    }
    else {
      updateState(null,ProcessType.NONE, (short)Evu.NOPROB,Climate.Season.YEAR);
    }
  }
  private void doWyomingStochastic(Simulation simulation) {
    ProcessType selected = randomSelect();
    short       prob;

    if (selected == ProcessType.SUCCESSION) {
      selected = ProcessType.NONE;
      prob     = (short)Evu.NOPROB;
    }
    else {
      prob = (short) getProcessProb(selected);
    }
//    newState(selected, prob, Climate.Season.YEAR);
    updateState(null,selected, prob, simulation.getCurrentSeason());
  }
/**
 * Clears the dummy processes at current simulation time step.
 */
  public void clearDummyProcesses() {
    int ts = Simulation.getCurrentTimeStep();

    Lifeform[] lives = Lifeform.getAllValues();
    Season[]   seasons = Climate.allSeasons;
    for (int i=0; i<seasons.length; i++) {
      for (Lifeform lifeform : lives) {
        VegSimStateData state = getState(ts,lifeform,seasons[i]);
        if (state != null && state.getProcess().equals(ProcessType.NONE)) {
          removeState(ts,lifeform,seasons[i]);
        }
      }
    }
  }

  /**
   * Choose the Process that will occur in this Evu.
   * @throws SimpplleError caught in GUI
   */
  public void doGetProcess() throws SimpplleError {
    if (Area.multipleLifeformsEnabled()) {
      doGetProcessMultipleLifeform();
      return;
    }

    // Selected will only be non-null if a process
    // has been locked in.
    RegionalZone zone = Simpplle.currentZone;

    if (RegionalZone.isWyoming()) {
      getAdjustedProb();
      doGetProcessWyoming();
      if (doGapProcesses(dominantLifeform)) {}
      return;
    }

    ProcessType  selected;
    Species      species;
    Simulation   simulation = Simpplle.getCurrentSimulation();
    int          cTime = simulation.getCurrentTimeStep();

    VegSimStateData state = getState();
    int         prob        = state.getProb();
    ProcessType processType = state.getProcess();

    if (processType != null && prob == L) {
      selected = processType;
      prob     = prob;
    }
    else {
      getAdjustedProb();
      if (simulation.isStochastic()) {
        selected  = randomSelect();
      }
      else if (simulation.isHighestProbability()) {
        selected = getHighest();
      }
      // If we are in stand development the process will always
      // be SUCCESSION.  The exception is if a process has been
      // locked in, this is handled above.
      //
      else if (simulation.isStandDevelopment()) {
        updateState(null,ProcessType.SUCCESSION,(short)L,simulation.getCurrentSeason());
        return;
      }
      else {
        String msg = "Unexpected Simulation Method value. Quiting.";
        throw new SimpplleError(msg);
      }


      // First set the selected process and Probability.
      processType = selected;
      if (selected.equals(ProcessType.SRF) ||
          selected.equals(ProcessType.MSF) ||
          selected.equals(ProcessType.LSF)) {
        prob = getProcessProb(ProcessType.FIRE_EVENT);
      }
      else {
        prob = getProcessProb(processType);
      }
    }
    updateState(null,selected,(short)prob,simulation.getCurrentSeason());

    // Now find out if we need to change process and/or probability.
    state = getState();
    prob  = state.getProb();


    processType = selected;
    species = state.getVeg().getSpecies();

    if (doGapProcesses(dominantLifeform)) {
      selected = getState().getProcess();
      processType = selected;
    }

    if (selected == ProcessType.FIRE_EVENT) {
      lastFireTimeStep = cTime;
      doFireSpecificDoGetProcess(species,dominantLifeform);
    }

    ProcessType prevProcess = getState(cTime-1).getProcess();
    // Once Root Disease starts it will continue,
    // unless taken over by Fire.
    if (((processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
         (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)  &&
         (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false)  &&
         (!processType.isRootDisease())) &&
         (prevProcess.isRootDisease()))
    {
      if (Simpplle.getCurrentZone() instanceof WestsideRegionOne) {
        selected = getHighestRootDisease(prevProcess);
      }
      else {
        selected = prevProcess;
      }
      processType = selected;
      updateCurrentProcess(selected);
      if (prob != Evu.SUPP) {
        updateCurrentProb(10000);
      }
    }



    // Once Spruce Beetle starts it will continue for 5 yearly time steps,
    // unless taken over by Fire.

    if ( ( (processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
          (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false) &&
          (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false))) {
      processType = prevProcess;
      if ( (processType.equals(ProcessType.LIGHT_SB) ||
            processType.equals(ProcessType.MEDIUM_SB) ||
            processType.equals(ProcessType.HIGH_SB)) &&
           (processOccurredAllPastNTimeSteps(spruceBeetles,5, false)) == false) {
        selected = processType;
        updateCurrentProcess(selected);
        if (prob != Evu.SUPP) {
          updateCurrentProb(10000);
        }

      }

    }

    if (selected == ProcessType.HIGH_SB) {
      haveHighSpruceBeetle = true;
    }
  }

  public void doGetProcessMultipleLifeform() throws SimpplleError {
    // Selected will only be non-null if a process
    // has been locked in.

    ProcessProbability selected = null;
    Simulation   simulation = Simpplle.getCurrentSimulation();
    int          cTime = simulation.getCurrentTimeStep();
    AreaSummary  areaSummary = Simpplle.getAreaSummary();

    if (simulation.isStandDevelopment() == false) {
      // Determine if we have a Fire Event
      // Fire Event is a unit process not decided on a lifeform basis.
      simulation.setProbPrecision(4);
      int random = simulation.random();
      int prob = getProcessProb(ProcessType.FIRE_EVENT);
      simulation.setDefaultProbPrecision();

      if (random < prob) {
        selected = new ProcessProbability(ProcessType.FIRE_EVENT);
        // This could result in 0 probability, but until I can
        // make a better solution, perhaps float prob, this is necessary.
        selected.probability = Math.round((float) prob / 100);
      }
    }

    ProcessProbability treeSelected = null;
    if (hasLifeform(Lifeform.TREES,cTime)) {
      treeSelected = doGetProcessLifeform(Lifeform.TREES,selected);
      doTreeSpecificProcessChanges(treeSelected, cTime);
      areaSummary.updateProcessOriginatedIn(this,Lifeform.TREES,treeSelected,cTime);
    }

    ProcessProbability shrubSelected = null;
    if (hasLifeform(Lifeform.SHRUBS,cTime)) {
      shrubSelected = doGetProcessLifeform(Lifeform.SHRUBS,selected);
      areaSummary.updateProcessOriginatedIn(this,Lifeform.SHRUBS,shrubSelected,cTime);
    }

    ProcessProbability herbaciousSelected = null;
    if (hasLifeform(Lifeform.HERBACIOUS,cTime)) {
      if (shrubSelected != null && ProcessType.WILDLIFE_BROWSING == shrubSelected.processType) {
        herbaciousSelected = shrubSelected;
        updateState(Lifeform.HERBACIOUS,herbaciousSelected.processType,(short)herbaciousSelected.probability,simulation.getCurrentSeason());
      }
      else {
        herbaciousSelected = doGetProcessLifeform(Lifeform.HERBACIOUS,selected);
      }
      areaSummary.updateProcessOriginatedIn(this,Lifeform.HERBACIOUS,herbaciousSelected,cTime);

    }

    ProcessProbability agricultureSelected = null;
    if (hasLifeform(Lifeform.AGRICULTURE,cTime)) {
      agricultureSelected = doGetProcessLifeform(Lifeform.AGRICULTURE,selected);
      areaSummary.updateProcessOriginatedIn(this,Lifeform.AGRICULTURE,agricultureSelected,cTime);
    }

    ProcessProbability naSelected = null;
    if (hasLifeform(Lifeform.NA,cTime)) {
      naSelected = doGetProcessLifeform(Lifeform.NA,selected);
      areaSummary.updateProcessOriginatedIn(this,Lifeform.NA,naSelected,cTime);
    }

  }

  private ProcessProbability doGetProcessLifeform(Lifeform lifeform, ProcessProbability selected)
    throws SimpplleError
  {
    Simulation   simulation = Simpplle.getCurrentSimulation();
    ArrayList<ProcessType> processes;
    ProcessType        process;
    boolean isLockin = false;
    ProcessProbability newSelected;

    if (selected != null &&
        selected.processType == ProcessType.FIRE_EVENT &&
        isSuppressed(lifeform)) {
      selected = null;
    }

    int cStep = simulation.getCurrentTimeStep();
    VegSimStateData state = getState(cStep,lifeform);
    if (state != null && state.getProcess() != null && state.getProb() == L) {
      newSelected = new ProcessProbability(state.getProcess());
      newSelected.probability = state.getProb();
      isLockin = true;
    }
    // If selected is != null then we have Fire Event already chosen
    // for the whole unit.
    else if (selected == null) {
      processes = Process.getSimulationProcesses(lifeform,false);
        // Done via above boolean now.
//      processes.remove(ProcessType.FIRE_EVENT);
      if (simulation.isStochastic()) {
        process = ProcessChooser.doStochastic(this, processes);
        newSelected = new ProcessProbability(process);
        newSelected.probability = getProcessProb(process);
      }
      else if (simulation.isHighestProbability()) {
        process  = ProcessChooser.doHighest(this, processes);
        newSelected = new ProcessProbability(process);
        newSelected.probability = getProcessProb(process);
      }
      else if (simulation.isStandDevelopment()) {
        newSelected = new ProcessProbability(ProcessType.SUCCESSION);
        newSelected.probability = L;
      }
      else {
        String msg = "Unexpected Simulation Method value. Quiting.";
        throw new SimpplleError(msg);
      }
    }
    else {
      newSelected = new ProcessProbability(selected.processType);
      newSelected.probability = selected.probability;
    }

    if (!isLockin) {
      updateState(lifeform, newSelected.processType, (short) newSelected.probability,
               simulation.getCurrentSeason());
      state = getState(cStep,lifeform);
    }
    if (newSelected.processType == ProcessType.FIRE_EVENT) {
      lastFireTimeStep = cStep;
      Area.currentLifeform = lifeform;
      doFireSpecificDoGetProcess(state.getVegType().getSpecies(),lifeform);

      VegSimStateData updatedState = getState();
      newSelected.processType = updatedState.getProcess();
      newSelected.probability = updatedState.getProb();
      Area.currentLifeform = null;

    }

    if (doGapProcesses(lifeform)) {
      VegSimStateData updatedState = getState();
      newSelected.processType = updatedState.getProcess();
      newSelected.probability = updatedState.getProb();
    }

    return newSelected;
  }
/**
 * This method calculates tree specific process changes.  These will include Root disease, and beech bark disease both of which once start continue unless a fire process
 * occurs (LSF, MSF, or SRF), and spruce beetle which once starts continues for 5 years unless a fire event occurs
 * @param processData
 * @param cTime
 */
  private void doTreeSpecificProcessChanges(ProcessProbability processData, int cTime) {

    ProcessType selected = null;
    VegSimStateData state = getState(cTime-1,Lifeform.TREES);
    ProcessType pastProcess = (state != null) ? state.getProcess() : ProcessType.SUCCESSION;
    ProcessType processType = processData.processType;

    // Root Disease.
    if (((processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
         (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false)  &&
         (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false)  &&
         (!processType.isRootDisease())) &&
         (pastProcess.isRootDisease())) {
      if (Simpplle.getCurrentZone() instanceof WestsideRegionOne) {
        selected = getHighestRootDisease(pastProcess);
      }
      else {
        selected = pastProcess;
      }
      processType = selected;
      processData.processType = selected;
      updateCurrentProcess(Lifeform.TREES,selected);
      if (getState(Lifeform.TREES).getProb() != Evu.SUPP) {
        updateCurrentProb(Lifeform.TREES,10000);
        processData.probability = 10000;
      }
    }



    // Spruce Beetle

    if ( ( (processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) &&
          (processType.equals(ProcessType.MIXED_SEVERITY_FIRE) == false) &&
          (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE) == false))) {
      processType = pastProcess;
      if ( (pastProcess.equals(ProcessType.LIGHT_SB) ||
            pastProcess.equals(ProcessType.MEDIUM_SB) ||
            pastProcess.equals(ProcessType.HIGH_SB)) &&
           (processOccurredAllPastNTimeSteps(Lifeform.TREES,spruceBeetles,5, false)) == false) {
        selected = processType;
        updateCurrentProcess(Lifeform.TREES,selected);
        processData.processType = selected;
        if (getState(Lifeform.TREES).getProb() != Evu.SUPP) {
          updateCurrentProb(Lifeform.TREES,10000);
          processData.probability = 10000;
        }

      }

    }

    if (selected == ProcessType.HIGH_SB) {
      haveHighSpruceBeetle = true;
    }
  }
/**
 * This method calculates fire specific process changes.  This will be based on the species present in current zone and fire suppression.
 * it updates current process and updates current probability
 * @param species
 * @param lifeform
 */
  private void doFireSpecificDoGetProcess(Species species, Lifeform lifeform) {
    RegionalZone zone = Simpplle.getCurrentZone();
    ProcessType selected = ProcessType.FIRE_EVENT;
    boolean     suppressed;

    // Species types of ND, AGR, NF, and WATER
    // do not have any fires.
    if ((species == Species.ND || species == Species.AGR ||
         species == Species.NF || species == Species.WATER ||
         species == Species.ROCK_BARE ||
         species == Species.AGR_URB ||
         species.getFireResistance() == FireResistance.UNKNOWN)) {
      selected    = ProcessType.SUCCESSION;
      updateCurrentProcess(selected);
      updateCurrentProb(Evu.L);
      return;
    }

    VegSimStateData state = getState();
    if (Simulation.getInstance().fireSuppression() &&
        state.getProb() != Evu.L) {
      suppressed = FireEvent.doSuppression(zone, this);
    }
    else if (Simulation.getInstance().fireSuppression() == false &&
             state.getProb() != Evu.L &&
             ((Simpplle.getCurrentZone() instanceof ColoradoPlateau) == false)) {
      suppressed = FireEvent.doWeatherEvent(zone, this);
    }
    else {
      suppressed = false;
    }

    if (!suppressed) {
      selected = FireEvent.getTypeOfFire(zone, this, lifeform);
      if (selected == ProcessType.NONE) {
        selected = ProcessType.SUCCESSION;
        updateCurrentProb(Evu.L);
      }
      updateCurrentProcess(selected);
    }
  }

/**
 * Method to rcalculate whether a random number is between lower and upper range of cumulative probability data for a process.
 * @return
 */
  private ProcessType randomSelect() {
    ArrayList             simulationProcesses = Process.getSimulationProcesses();
    int                   randNum = Simulation.getInstance().random();
    CumulativeProcessProb cumulProbData;
    ProcessType           processType;

    for(int i=0; i<simulationProcesses.size(); i++) {
      processType = (ProcessType)simulationProcesses.get(i);
      cumulProbData = findCumulProb(processType);

      if ((randNum >= cumulProbData.lower) && (randNum <= cumulProbData.upper)) {
        return processType;
      }
    }

    // just in case.
    return ProcessType.SUCCESSION;
  }
/**
 * Method to find highest process probability.  Initially sets the process to succession and highest probability to 0 so that any
 * processes or probabilities above that will be higher.  then searches through simulation processes and gets their probability.
 *
 * @return process with highest probability
 */
  private ProcessType getHighest() {
    ProcessType   selectedType = ProcessType.SUCCESSION;
    int           highestProb = 0, prob;

    ArrayList simulationProcesses = Process.getSimulationProcesses();
    ProcessType           processType;

    for(int i=0; i<simulationProcesses.size(); i++) {
//      if (simulationProcesses[i].equals(ProcessType.SUCCESSION)) { continue; }
      processType = (ProcessType)simulationProcesses.get(i);
      prob = getProcessProb(processType);
      if (prob > highestProb) {
        highestProb = prob;
        selectedType = processType;
      }
    }

    return selectedType;
  }

  private ProcessType getHighestRootDisease(ProcessType pastProcess) {
    ProcessType selectedType;

    int lsProb = getProcessProb(ProcessType.LS_ROOT_DISEASE);
    int msProb = getProcessProb(ProcessType.MS_ROOT_DISEASE);
    int hsProb = getProcessProb(ProcessType.HS_ROOT_DISEASE);

    int prob = lsProb;
    selectedType = ProcessType.LS_ROOT_DISEASE;
    if (msProb > prob) {
      prob = msProb;
      selectedType = ProcessType.MS_ROOT_DISEASE;
    }
    if (hsProb > prob) {
      prob = hsProb;
      selectedType = ProcessType.HS_ROOT_DISEASE;
    }

    if (prob == 0) {
      return pastProcess;
    }
    return selectedType;
  }

  /**
   * This method will determine if an event spread from fromEvu in the param
   * list to this unit.
   * This method is static and synchronized in order to be certain that the
   * fields we need in the fromEvu and toEvu are not modified by another
   * thread while this method is executing.  There is probably a non-static
   * way of doing this.
   * @todo make this method non-static and less restricted (if possible).
   *
   * @param fromEvu The Evu we are trying to spread from.
   * @param toEvu   The Evu we are trying to spread to.
   * @return boolean true if spread was successfull
   */
  public static synchronized boolean doSpread(Evu fromEvu, Evu toEvu, Lifeform fromLifeform) {
    ProcessType  fromProcess   = fromEvu.getState(fromLifeform).getProcess();
    if (fromProcess.isFireProcess()) {
      return doFireSpread(fromEvu,toEvu,fromLifeform);
    }

    Area.currentLifeform = fromLifeform;

    Process processInst;
    ProcessType  toProcess;

    if (toEvu.hasLifeform(fromLifeform) == false) {
      Area.currentLifeform = null;
      return false;
    }
    else {
      toProcess = toEvu.getState(fromLifeform).getProcess();
      if (toEvu.getState(fromLifeform).getProb() == L) {
        Area.currentLifeform = null;
        return false;
      }
    }

    if (toProcess.equals(ProcessType.SUCCESSION)) {
      processInst = Process.findInstance(fromProcess);
      if (processInst.doSpread(Simpplle.getCurrentZone(), fromEvu, toEvu)) {
        Area.currentLifeform = null;
        return true;
      }
    }

    Area.currentLifeform = null;
    return false;
  }

  /**


   * This method will determine if an fire event spread from fromEvu in the param
   * list to this unit.
   * This method is static and synchronized in order to be certain that the
   * fields we need in the fromEvu and toEvu are not modified by another
   * thread while this method is executing.  There is probably a non-static
   * way of doing this.
   * @todo make this method non-static and less restricted (if possible).
   *
   * @param fromEvu The Evu we are trying to spread from.
   * @param toEvu   The Evu we are trying to spread to.
   * @return boolean true if spread was successfull
   */

  public static synchronized boolean doFireSpread(Evu fromEvu, Evu toEvu, Lifeform fromLifeform) {
    // Don't spread into a unit that has fire or a lock-in process.
    if (toEvu.hasFireAnyLifeform()) { return false; }
    if (toEvu.hasLockinProcessAnyLifeform()) { return false; }
    if (toEvu.isSuppressed()) { return false; }

    ProcessType fromProcess   = fromEvu.getState(fromLifeform).getProcess();
    ProcessType fireProcess=null;
    int         fireProb=S;
    boolean  fireStarted=false;


    Climate.Season currentSeason = Simulation.getInstance().getCurrentSeason();

    Lifeform[] lives = Lifeform.getAllValues();
    for (int i=0; i<lives.length; i++) {
      Lifeform toLifeform = lives[i];
      Area.currentLifeform = toLifeform;
      if (toEvu.hasLifeform(toLifeform) == false) { continue; }

      Process processInst = Process.findInstance(fromProcess);

      if (fireStarted) {
        ProcessType p =
            FireEvent.getTypeOfFire(Simpplle.getCurrentZone(),toEvu,toLifeform);
        if (p != null && p.isFireProcess()) {
          if (p.isFireLessIntense(fireProcess)) {
            p = fireProcess;
          }
          toEvu.lastFireTimeStep = Simulation.getCurrentTimeStep();
          toEvu.updateCurrentProcess(toLifeform, p, currentSeason);
          toEvu.updateCurrentProb(toLifeform, fireProb);
          toEvu.updateFireSeason(currentSeason);
        }
      }
      else if (processInst.doSpread(Simpplle.getCurrentZone(),fromEvu,toEvu)) {
        toEvu.lastFireTimeStep = Simulation.getCurrentTimeStep();
        fireStarted = true;
        fireProcess = toEvu.getState(toLifeform).getProcess();
        fireProb    = toEvu.getState(toLifeform).getProb();
      }

    }
    Area.currentLifeform = null;
    return fireStarted;
  }
/**
 * Method to calculate treatment thinning state.  This is based on current simulation vegetative state and time steps, treatment, original size class,
 * current size class, fire event if any, and species.
 * @return
 */
  private VegetativeType getTreatmentThinningCycleState() {
    Simulation     simulation = Simpplle.getCurrentSimulation();
    int            cStep = simulation.getCurrentTimeStep();
    int            tStep = cStep;
    Treatment      treatment = null;
    boolean        foundSomething = false;
    VegetativeType savedState=null;
    SizeClass      origSizeClass, sizeClass;
    int            sizeClassCount=0;
    ProcessType    process;

    VegSimStateData currentState = getState();
    // Forget yearly steps for now, this can be handled later if the
    // need arises.
    if (simulation.isYearlyTimeSteps()) {
      return null;
    }

    // Determine if we have an appropriate treatment in the last 3 time decades
    int i;
    for (i=tStep; (i>=0 && i>=(tStep-2)); i--) {
      treatment = getTreatment(i);
      if (treatment == null) { continue; }

      sizeClass = treatment.getSavedState().getSizeClass();
      if ((i == cStep) || (i == 0)) {
        origSizeClass = (SizeClass)getState(SimpplleType.SIZE_CLASS);
      }
      else {
        VegSimStateData state = getState(i-1);
        origSizeClass = (state != null ? state.getVeg().getSizeClass() : null);
      }
      if ((origSizeClass != null && sizeClass != null) &&
          (origSizeClass != sizeClass) &&
          (origSizeClass.getBase() == sizeClass.getBase())) {
        foundSomething = true;
        tStep = i;
        savedState = treatment.getSavedState();
        break;
      }
    }
    if (savedState == null) {
      foundSomething = false;
      tStep = cStep;
      for (i=tStep; (i>=0 && i>=(tStep-2)); i--) {
        VegSimStateData state = getState(i);
        if (state == null) { continue; }
        process = state.getProcess();
        if (process == ProcessType.MIXED_SEVERITY_FIRE ||
            process == ProcessType.LIGHT_SEVERITY_FIRE) {
          foundSomething = true;
          tStep = i;
          break;
        }
      }
      if (foundSomething) {
        VegSimStateData state = getState(tStep);
        savedState = (state != null ? state.getVeg() : null);
      }
    }
    if (savedState == null) { return null; }

    origSizeClass = savedState.getSizeClass();

    if (cycleSizeClass == null) { return null; }

    if (origSizeClass.getBase() == cycleSizeClass) {
      sizeClassCount = cycleSizeClassCount;
    }



    if (sizeClassCount == 0) { return null; }

    Process succession = Process.findInstance(ProcessType.SUCCESSION);
    VegSimStateData state = getState();
    VegetativeType newState=null;
    if (state != null) {
      newState = state.getVeg();
      for (i = 0; i < sizeClassCount; i++) {
        if (newState.getProcessNextState(succession) == null) {
          newState = newState.getProcessNextState(succession);
        }
        newState = newState.getProcessNextState(succession);
        if (newState == null) {
          break;
        }
      }
    }
    sizeClass = (newState != null ? newState.getSizeClass() : null);
    if (sizeClass == null || origSizeClass.getBase() == sizeClass.getBase()) {
      return null;
    }


    if (sizeClass == SizeClass.LTS) { sizeClass = SizeClass.LARGE; }
    else if (sizeClass == SizeClass.LMU) { sizeClass = SizeClass.LTS; }
    else if (sizeClass == SizeClass.MTS) { sizeClass = SizeClass.MEDIUM; }
    else if (sizeClass == SizeClass.MMU) { sizeClass = SizeClass.MTS; }
    else if (sizeClass == SizeClass.PTS) { sizeClass = SizeClass.POLE; }
    else if (sizeClass == SizeClass.PMU) { sizeClass = SizeClass.PTS; }

    if (state == null) { return null; }

    VegetativeType result =
        htGrp.getVegetativeType(state.getVeg().getSpecies(),
                                sizeClass,state.getVeg().getDensity());

    if (result == null) {
      result = htGrp.getVegetativeType(state.getVeg().getSpecies(),
                                       sizeClass.getBase(),state.getVeg().getDensity());
    }

    return result;
  }
/**
 * adds a tracked species to the initial state.
 */
  public void addMissingTrackSpecies() {
    MapIterator it = initialState.mapIterator();
    while (it.hasNext()) {
      MultiKey key = (MultiKey)it.next();  // getValue requires this line.
      VegSimStateData state = (VegSimStateData)it.getValue();

      state.addMissingTrackSpecies();
    }
  }
  /**
   * Special method to calculate next state for Wyoming.
   */
  private void doNextStateWyoming() {
    VegSimStateData simState = getState();
    if (simState == null) { return; }

    ProcessType processType = simState.getProcess();

    if (processType.equals(ProcessType.NONE)) {
      return;
    }

    VegetativeType  state = simState.getVeg();
    if (simState.getProb() == Evu.L) {
      getState().setVegType(state);
      return;
    }

    VegetativeType newState;
    Flat3Map trkSpecies = simState.getTrackingSpeciesMap();

    if (trkSpecies == null) {
      newState = state.getProcessNextState(processType);
    }
    else {
      MapIterator it = trkSpecies.mapIterator();
      while (it.hasNext()) {
        InclusionRuleSpecies sp = (InclusionRuleSpecies)it.next();

        float change = state.getSpeciesChange(processType, simState.getSeason(),sp);
        simState.updateTrackingSpecies(sp,change);
      }

      newState = htGrp.findSpeciesChangeNextState(trkSpecies);
    }

    if (newState != null) {
      simState.setVegType(newState);
    }
    else {
      newState = state.getProcessNextState(processType);
    }

    if (newState == null) {
      newState = state;
    }

    getState().setVegType(newState);
  }

/**
 * Uses the passed lifeform to query the lifeform array, and loop through it till it finds the next lower life
 * @param lifeform the lifeform being evaluated.
 * @return the lower life form if one exists, or null if there is no lower lifeform.
 */
  private Lifeform findNextLowerLifeform(Lifeform lifeform) {
    Lifeform lowerLife=Lifeform.getLowerLifeform(lifeform);

    while (lowerLife != null) {
      if (hasLifeform(lowerLife)) {
        return lowerLife;
      }
      lowerLife=Lifeform.getLowerLifeform(lowerLife);
    }
    return null;
  }
  /**
   * Loops through the regeneration delay and uses the lifeform ID to the lifeform within it and set the index with corresponding to that life form
   * and setting the regeneration delay to the passed in delay, else it will add the delay to an already existing regeneration delay.
   * @param delay
   * @param lifeform
   */
  private void setRegenDelay(int delay, Lifeform lifeform) {
    for (int i=0; i<regenDelay.length; i++) {
      if (i == lifeform.getId()) {
        regenDelay[i] = delay;
      }
      else if (regenDelay[i] > 0) {
        regenDelay[i] += delay;
      }
    }
  }

  // Avoid excess temporaries.
  private static ArrayList<VegetativeType> newStatesTemp = new ArrayList<VegetativeType>();
/**
 * Calculates the next state for multiple life forms
 */
  public void doNextStateMultipleLifeform() {
    if (getId() == 60 && Simulation.getCurrentTimeStep() == 51) {
      System.out.println("Evu:4568");
    }
    newStatesTemp.clear();
    ArrayList<Lifeform> nextStateToDo =
      getLifeformsList(Simulation.getCurrentTimeStep(),
                       Simulation.getInstance().getCurrentSeason());


    if (Simpplle.getCurrentZone().isWyoming() == false) {
      Lifeform[] allLives = Lifeform.getAllValues();
      for (int i=0; i<allLives.length; i++) {
        if (this.hasLifeform(allLives[i]) == false) {
          if (isRegenDelay(allLives[i]) == false) {
            Lifeform lowerLifeform = findNextLowerLifeform(allLives[i]);
            if (lowerLifeform != null) {
              VegetativeType newState = doRegen(lowerLifeform, allLives[i]);
              if (newState != null) { newStatesTemp.add(newState); }
            }
          }
        }
      }
    }

    for (int i=0; i<nextStateToDo.size(); i++) {
      Area.currentLifeform = nextStateToDo.get(i);
      ProcessType    processType = getState(Area.currentLifeform).getProcess();
      VegetativeType newState=null;

      if (canDoFireRegen(processType,Simpplle.getCurrentZone())) {
        int delay = RegenerationDelayLogic.getInstance().getDelay(this,Area.currentLifeform);
        if (delay > 0) {
          setRegenDelay(delay,Area.currentLifeform);
        }
        else {
          newState = doFireRegen(Area.currentLifeform);
        }
      }
      if (Area.currentLifeform == Lifeform.SHRUBS && newState != null && newState.getSpecies().getLifeform() == Lifeform.TREES) {
        System.out.println("Evu:4606");
      }
      doNextStateNew(newState);
    }

    for (int i=0; i<newStatesTemp.size(); i++) {
      ProcessProbability selected = new ProcessProbability(ProcessType.SUCCESSION,10000);

      int ts  = Simulation.getCurrentTimeStep();
      int run = Simulation.getCurrentRun();
      VegSimStateData state =
        new VegSimStateData(getId(),ts,run,newStatesTemp.get(i),selected.processType,
                            (short)selected.probability,Climate.Season.YEAR);

      int cStep = Simulation.getCurrentTimeStep();
      newState(cStep,state,Simulation.getInstance().getCurrentSeason());
      if (selected.probability >= 0) {
        Lifeform newLife = newStatesTemp.get(i).getSpecies().getLifeform();
        Simulation.getInstance().getAreaSummary().updateProcessOriginatedIn(this,newLife,selected,cStep);
      }
    }
    dominantLifeform = Lifeform.findDominant(getLifeforms());
    Area.currentLifeform = null;
  }
  /**
   * Uses the current time step and current simulation run to create a new vegetative simulation step.
   * @param vegType
   */

  public void addNewLifeformState(VegetativeType vegType) {
    ProcessProbability selected = new ProcessProbability(ProcessType.SUCCESSION,10000);

    int ts  = Simulation.getCurrentTimeStep();
    int run = Simulation.getCurrentRun();
    VegSimStateData state =
      new VegSimStateData(getId(),ts,run,vegType,selected.processType,
                          (short)selected.probability,Climate.Season.YEAR);

    int cStep = Simulation.getCurrentTimeStep();
    newState(cStep,state,Climate.Season.YEAR);
    if (selected.probability >= 0) {
      Lifeform newLife = vegType.getSpecies().getLifeform();
      Simulation.getInstance().getAreaSummary().updateProcessOriginatedIn(this,newLife,selected,cStep);
    }
    dominantLifeform = Lifeform.findDominant(getLifeforms());
  }

  /**
   * These methods make sure that the next state generated is properly handled,
   * in the case where one lifeforms next state is another lifeform.
   * (e.g. Fire burns the trees and results in grasses, in this case we would
   *       remove the trees and add grasses if none are currently present.)
   * @param newState VegetativeType
   * @return VegetativeType
   */
  private VegetativeType validateNewState(VegetativeType newState) {
    return validateNewState(newState,false);
  }
  /**
   * These methods make sure that the next state generated is properly handled,
   * in the case where one lifeforms next state is another lifeform
   * @param newState
   * @param remove
   * @return
   */
  private VegetativeType validateNewState(VegetativeType newState, boolean remove) {
    return validateNewState(Area.currentLifeform,newState,remove);
  }
  /**
   * Makes sure the new state is a valid state.  It will check the new state against the old state
   * @param lifeform
   * @param newState
   * @param remove
   * @return
   */
  private VegetativeType validateNewState(Lifeform lifeform, VegetativeType newState, boolean remove) {
    if (newState == null) { return null; }

    Season   season = Simulation.getInstance().getCurrentSeason();
    Lifeform newLife = newState.getSpecies().getLifeform();
    if (Area.multipleLifeformsEnabled() && (lifeform != null) &&
        newLife.equals(lifeform) == false) {
      if (remove) {
        Simulation simulation = Simpplle.getCurrentSimulation();
        int cStep = simulation.getCurrentTimeStep();

        if (getState(cStep,newLife,season) == null) {

          VegSimStateData oldState = getState();
          ProcessProbability selected = new ProcessProbability(oldState.getProcess(),oldState.getProb());

          int run = Simulation.getCurrentRun();
          VegSimStateData state =
            new VegSimStateData(getId(),cStep,run,newState,selected.processType,
                                (short)selected.probability,oldState.getSeason());

          newState(cStep,state,Simulation.getInstance().getCurrentSeason());
          if (selected.probability >= 0) {
            simulation.getAreaSummary().updateProcessOriginatedIn(this,newLife,selected,cStep);
          }
        }
        else {
          updateState(newState);
        }
        removeState(cStep,lifeform,season);
        // Because the lifeform was removed the last alive state is in
        // the previous time step.
        updateLastLifeData(cStep-1,lifeform,season);
        dominantLifeform = Lifeform.findDominant(getLifeforms(season));
      }
      else {
        if (hasLifeform(newLife)) {
          return null;
        }
        return newState;
      }
    }
    return newState;
  }

  private void updateLastLifeData(int tStep, Lifeform lifeform, Season season) {
    lastLife[lifeform.getId()] = getState(tStep,lifeform,season);
    ProcessType[] processes = (ProcessType[])lastLifeProcessHistory.get(lifeform);
    if (processes == null) {
      processes = new ProcessType[simData.length];
      lastLifeProcessHistory.put(lifeform,processes);
    }

    int index=processes.length-2;
    for (int i=tStep; (i >= 0 && index >= 0); i--) {
      VegSimStateData state = getState(i,lifeform,season);
      processes[index] = (state != null ? state.getProcess() : ProcessType.NONE);
      index--;
    }
  }

  private boolean isRegenDelay(Lifeform lifeform) {
    int delay = regenDelay[lifeform.getId()];
    if (delay > 0 && timeSinceFire() >= delay) {
      regenDelay[lifeform.getId()] = 0;
      delay = 0;
      recentRegenDelay[lifeform.getId()] = true;
    }

    return (delay > 0);
  }
  /**
   * Calculate the next vegetative state that this Evu will have.  If is wyoming will pass to that special next state class.
   *
   * Notes:
   *   added some logic here to make succession look at
   *   the pathways for wet/dry succession for ColoradoFrontRange on
   *   Herbacious species if the appropriate climate exists.
   *   (i.e. (cooler/wetter == wet-succ and warmer/drier == dry-succ)
   */
  public void doNextState() {

    RegionalZone     zone = Simpplle.currentZone;
    Area             area = Simpplle.getCurrentArea();
    VegetativeType   newState = null;
    Simulation       simulation = Simpplle.getCurrentSimulation();


    VegSimStateData simState    = getState();
    VegetativeType  state       = simState.getVeg();
    ProcessType     processType = simState.getProcess();
    Species         species     = state.getSpecies();



    MultiKeyMap DProcesses = Simpplle.getAreaSummary().getDProcesses();

    if (RegionalZone.isWyoming()) {
      doNextStateWyoming();
      return;
    }

    if ((processType.equals(ProcessType.SUCCESSION) &&
         (isRegenDelay(dominantLifeform) != true) &&
         Succession.isRegenState(zone,this,dominantLifeform) &&
         (FireEvent.useRegenPulse() == false || FireEvent.isRegenPulse())) ||

        (processType.equals(ProcessType.SEVERE_LP_MPB) && species == Species.LP &&
         Succession.isRegenState(zone,this,dominantLifeform) &&
         (FireEvent.useRegenPulse() == false || FireEvent.isRegenPulse()))) {
      newState = Succession.regen(zone,this,dominantLifeform,dominantLifeform);
      newState = validateNewState(newState);
    }
    else if (processType.equals(ProcessType.SUCCESSION) &&
             (FireEvent.useRegenPulse() && FireEvent.isRegenPulse())) {
      newState = FireEvent.regenPulse(this);
      newState = validateNewState(newState);
    }
    else if (canDoFireRegen(processType,zone) &&
             isRegenDelay(dominantLifeform) == false) {
      newState = FireEvent.regen(dominantLifeform,this);
      newState = validateNewState(newState);
    }

    boolean isYearlyPathway = htGrp.isYearlyPathwayLifeform(species.getLifeform());

    // For Succession, next state only changes if a
    // decade has passed.
    if (newState == null &&
        processType.equals(ProcessType.SUCCESSION) &&
        simulation.isYearlyTimeSteps() &&
        (isYearlyPathway == false) &&
        (simulation.isDecadeStep() == false)) {
      newState = state;
    }

    // Since Grasses/Shrubs are yearly pathways we need to project forward
    // thru 10 next state changes if we are running in decade time steps.
    if (newState == null &&
        processType.equals(ProcessType.SUCCESSION) &&
        (simulation.isYearlyTimeSteps() == false) &&
        isYearlyPathway)
    {
      // Make sure we are using the correct kind of SUCCESSION
      ProcessType succession = ProcessType.SUCCESSION;
      if (((zone instanceof ColoradoFrontRange) ||
           (zone instanceof ColoradoPlateau) ||
           (RegionalZone.isWyoming()) ||
          (species.getLifeform() == Lifeform.HERBACIOUS)))
      {
        if (Simpplle.getClimate().isWetSuccession()) {
          succession = ProcessType.WET_SUCCESSION;
        }
        else if (Simpplle.getClimate().isDrySuccession()) {
          succession = ProcessType.DRY_SUCCESSION;
        }

      }
      newState = state;
      for (int i=0; i<10; i++) {
        newState = newState.getProcessNextState(succession);

      }
      newState = validateNewState(newState,true);
      if (newState != null) {
        getState().setVegType(newState);
        return;
      }
    }



    if (newState == null) {
        newState = getTreatmentThinningCycleState();
        newState = validateNewState(newState);
    }
    if (newState == null) {
        newState = state.getProcessNextState(processType);
        newState = validateNewState(newState,true);
    }

    ProcessType tmpProcessType;
    if (newState == null && processType.isFireProcess() &&
        RegionalZone.isWyoming() == false) {
      tmpProcessType = processType;
      if (tmpProcessType.equals(ProcessType.STAND_REPLACING_FIRE)) {
        tmpProcessType = ProcessType.MIXED_SEVERITY_FIRE;
        newState = state.getProcessNextState(tmpProcessType);
        newState = validateNewState(newState,true);
      }

      if (newState == null && tmpProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
        tmpProcessType = ProcessType.LIGHT_SEVERITY_FIRE;
        newState = state.getProcessNextState(tmpProcessType);
        newState = validateNewState(newState,true);
      }
      if (newState != null) {
        int ts = Simulation.getCurrentTimeStep();
        int run = Simulation.getCurrentRun();
        DProcesses.put(this,getDominantLifeform(),run,ts,processType);
        updateCurrentProcess(tmpProcessType);
        updateCurrentProb(Evu.D);
      }
    }

    if (newState == null) {
      int ts = Simulation.getCurrentTimeStep();
      int run = Simulation.getCurrentRun();
      DProcesses.put(this,getDominantLifeform(),run,ts,processType);
      updateCurrentProcess(ProcessType.SUCCESSION);
      updateCurrentProb(Evu.D);
      newState = state.getProcessNextState(ProcessType.SUCCESSION);
      newState = validateNewState(newState,true);
    }

    if (newState != null &&
        (getState().getProcess() == ProcessType.SUCCESSION) &&
        ((zone instanceof ColoradoFrontRange) ||
         (RegionalZone.isWyoming()) ||
         (zone instanceof ColoradoPlateau) &&
        (species.getLifeform() == Lifeform.HERBACIOUS)))
    {
      VegetativeType tmpState=newState;

      ProcessType wetDryProcess = ProcessType.SUCCESSION;
      if (Simpplle.getClimate().isWetSuccession()) {
        wetDryProcess = ProcessType.WET_SUCCESSION;
        newState = state.getProcessNextState(ProcessType.WET_SUCCESSION);
        newState = validateNewState(newState,true);
      }
      else if (Simpplle.getClimate().isDrySuccession()) {
        wetDryProcess = ProcessType.DRY_SUCCESSION;
        newState = state.getProcessNextState(ProcessType.DRY_SUCCESSION);
        newState = validateNewState(newState,true);
      }

      // Pathways didn't have a process of wet/dry so go back to
      // what it was before and mark prob as D to indicate missing pathway info.
      if (newState == null) {
        int ts = Simulation.getCurrentTimeStep();
        int run = Simulation.getCurrentRun();
        DProcesses.put(this,getDominantLifeform(),run,ts,wetDryProcess);
        updateCurrentProb(Evu.D);
        newState = tmpState;
      }
    }
    if (newState == null) {
      updateCurrentProb(Evu.D);
      newState = state;
    }

    // New VegSimStateData instance already added when process was
    // selected, with current state as state.  So we simply need to
    // update this instance.
    getState().setVegType(newState);
  }

  /**
   * Use the multiple lifeform doNextState
   */
  public VegetativeType doRegen(Lifeform lowerLifeform, Lifeform adjLifeform) {
    RegionalZone   zone = Simpplle.currentZone;

    VegSimStateData state = getState(lowerLifeform);
    if (state == null) { return null; }

    ProcessType    processType = state.getProcess();
    Species        species = state.getVeg().getSpecies();
    VegetativeType newState=null;

    if ((processType.equals(ProcessType.SUCCESSION) &&
         (isRegenDelay(lowerLifeform) != true) &&
         Succession.isRegenState(zone,this,lowerLifeform) &&
         (FireEvent.useRegenPulse() == false || FireEvent.isRegenPulse())) ||

        (processType.equals(ProcessType.SEVERE_LP_MPB) && species == Species.LP &&
         Succession.isRegenState(zone,this,lowerLifeform) &&
         (FireEvent.useRegenPulse() == false || FireEvent.isRegenPulse()))) {
      newState = Succession.regen(zone,this,lowerLifeform,adjLifeform);
      newState = validateNewState(newState);
    }
    else if (processType.equals(ProcessType.SUCCESSION) &&
             (FireEvent.useRegenPulse() && FireEvent.isRegenPulse())) {
      newState = FireEvent.regenPulse(this);
      newState = validateNewState(newState);
    }
    return newState;
  }
  public VegetativeType doFireRegen(Lifeform lifeform) {
    VegetativeType newState=null;

    newState = FireEvent.regen(lifeform,this);
    newState = validateNewState(newState);
    return newState;
  }
  public void doNextStateNew(VegetativeType newState) {

    RegionalZone     zone = Simpplle.currentZone;
    Simulation       simulation = Simpplle.getCurrentSimulation();
//    TreatmentType    currentTreatment;
//    Treatment        treat;

    VegSimStateData simState    = getState();
    ProcessType     processType = simState.getProcess();
    VegetativeType  state       = simState.getVeg();
    Species         species     = state.getSpecies();

//    treat            = getCurrentTreatment();
//    currentTreatment = (treat != null) ? treat.getType() : null;

    MultiKeyMap DProcesses = Simpplle.getAreaSummary().getDProcesses();

    if (RegionalZone.isWyoming()) {
      doNextStateWyoming();
      return;
    }

    boolean isYearlyPathway = htGrp.isYearlyPathwayLifeform(species.getLifeform());

    // For Succession, next state only changes if a
    // decade has passed.
    if (newState == null &&
        processType.equals(ProcessType.SUCCESSION) &&
        simulation.isYearlyTimeSteps() &&
        (isYearlyPathway == false) &&
        (simulation.isDecadeStep() == false)) {
      newState = state;
    }

    // Since Grasses/Shrubs are yearly pathways we need to project forward
    // thru 10 next state changes if we are running in decade time steps.
    if (newState == null &&
        processType.equals(ProcessType.SUCCESSION) &&
        (simulation.isYearlyTimeSteps() == false) &&
        isYearlyPathway)
    {
      // Make sure we are using the correct kind of SUCCESSION
      ProcessType succession = ProcessType.SUCCESSION;
      if (((zone instanceof ColoradoFrontRange) ||
           (zone instanceof ColoradoPlateau) ||
           (RegionalZone.isWyoming()) ||
          (species.getLifeform() == Lifeform.HERBACIOUS)))
      {
        if (Simpplle.getClimate().isWetSuccession()) {
          succession = ProcessType.WET_SUCCESSION;
        }
        else if (Simpplle.getClimate().isDrySuccession()) {
          succession = ProcessType.DRY_SUCCESSION;
        }

      }
      newState = state;
      for (int i=0; i<10; i++) {
        newState = newState.getProcessNextState(succession);

      }
      newState = validateNewState(newState,true);
      if (newState != null) {
        getState(newState.getSpecies().getLifeform()).setVegType(newState);
        return;
      }
    }

    if (newState == null) {
        newState = getTreatmentThinningCycleState();
        newState = validateNewState(newState);
    }
    if (newState == null) {
        newState = state.getProcessNextState(processType);
        newState = validateNewState(newState,true);
    }

    ProcessType tmpProcessType;
    if (newState == null && processType.isFireProcess() &&
        RegionalZone.isWyoming() == false) {
      tmpProcessType = processType;
      if (tmpProcessType.equals(ProcessType.STAND_REPLACING_FIRE)) {
        tmpProcessType = ProcessType.MIXED_SEVERITY_FIRE;
        newState = state.getProcessNextState(tmpProcessType);
        newState = validateNewState(newState,true);
      }

      if (newState == null && tmpProcessType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
        tmpProcessType = ProcessType.LIGHT_SEVERITY_FIRE;
        newState = state.getProcessNextState(tmpProcessType);
        newState = validateNewState(newState,true);
      }
      if (newState != null) {
        int ts = Simulation.getCurrentTimeStep();
        int run = Simulation.getCurrentRun();
        DProcesses.put(this,Area.currentLifeform,run,ts,processType);
        updateCurrentProcess(tmpProcessType);
        updateCurrentProb(Evu.D);
      }
    }

    if (newState == null) {
      int ts = Simulation.getCurrentTimeStep();
      int run = Simulation.getCurrentRun();
      DProcesses.put(this,Area.currentLifeform,run,ts,processType);

      updateCurrentProcess(ProcessType.SUCCESSION);
      updateCurrentProb(Evu.D);
      newState = state.getProcessNextState(ProcessType.SUCCESSION);
      newState = validateNewState(newState,true);
    }

    if (newState != null &&
        (processType == ProcessType.SUCCESSION) &&
        ((zone instanceof ColoradoFrontRange) ||
         (zone instanceof ColoradoPlateau) ||
         (RegionalZone.isWyoming()) ||
         (zone instanceof ColoradoPlateau) &&
        (species.getLifeform() == Lifeform.HERBACIOUS)))
    {
      VegetativeType tmpState=newState;

      ProcessType wetDryProcess = ProcessType.SUCCESSION;
      if (Simpplle.getClimate().isWetSuccession()) {
        wetDryProcess = ProcessType.WET_SUCCESSION;
        newState = state.getProcessNextState(ProcessType.WET_SUCCESSION);
        newState = validateNewState(newState,true);
      }
      else if (Simpplle.getClimate().isDrySuccession()) {
        wetDryProcess = ProcessType.DRY_SUCCESSION;
        newState = state.getProcessNextState(ProcessType.DRY_SUCCESSION);
        newState = validateNewState(newState,true);
      }

      // Pathways didn't have a process of wet/dry so go back to
      // what it was before and mark prob as D to indicate missing pathway info.
      if (newState == null) {
        int ts = Simulation.getCurrentTimeStep();
        int run = Simulation.getCurrentRun();
        DProcesses.put(this,Area.currentLifeform,run,ts,wetDryProcess);

        updateCurrentProb(Evu.D);
        newState = tmpState;
      }
    }
    if (newState == null) {
      newState = state;
      updateCurrentProb(Evu.D);
    }

    if (getState(newState.getSpecies().getLifeform()) == null) {
      System.out.println("Evu:5097, " + getId());
    }
    // New VegSimStateData instance already added when process was
    // selected, with current state as state.  So we simply need to
    // update this instance.
    getState(newState.getSpecies().getLifeform()).setVegType(newState);
  }
/**
 * Checks if can do Fire regeneration for this Evu.  If is a stand replacing fire, or zone is south central alaska, or michigan and it is spring returns false
 * else evaluates whether regen pulse is false, and if so returns false
 * @param processType the process - if SRF is the only process that is directly used.
 * @param zone the regional zone being evaluated
 * @return true if can do fire regneration.
 */
  private boolean canDoFireRegen(ProcessType processType, RegionalZone zone) {
    if (processType.equals(ProcessType.STAND_REPLACING_FIRE) == false) {
      return false;
    }
    if (zone.getId() == ValidZones.SOUTH_CENTRAL_ALASKA  &&
        Simpplle.getAreaSummary().getFireOccurrenceSeason(this) ==
        Climate.Season.SPRING) {
      return false;
    }
    if (FireEvent.useRegenPulse() && FireEvent.isRegenPulse() == false) {
      return false;
    }
    if (FireEvent.isRegenState(zone,this) == false) {
      return false;
    }

    return true;
  }

  /**
   * Will return true if the unit is seed producing.
   * @return true if unit is seed producing.
   */
  public boolean producingSeed(int tStep, Lifeform lifeform, RegenTypes regenType) {
    return calculateProducingSeed(tStep, false,lifeform,regenType);
  }
  /**
   * Overloaded producingSeed method.  Will return true if Evu is seed producing based on current time step, dominant life form
   * @return true if Evu is seed producing
   */
  public boolean producingSeed() {
    return calculateProducingSeed(Simulation.getCurrentTimeStep(),false,dominantLifeform,null);
  }
  /**
   * verloaded producingSeed methhod.
   * @param timeStep
   * @param postTreatment
   * @return
   */
  public boolean producingSeed(int timeStep, boolean postTreatment) {
    return calculateProducingSeed(timeStep,postTreatment,dominantLifeform,null);
  }

  public boolean producingSeed(Lifeform lifeform, RegenTypes regenType) {
    return calculateProducingSeed(Simulation.getCurrentTimeStep(),false,lifeform,regenType);
  }
  /**
   * Will return true if the unit is seed producing
   * for the past numDecades decades.
   * @param numDecades is an int.
   * @return a boolean.
   */
  public boolean producingSeed(int numDecades) {
    Simulation     simulation = Simpplle.getCurrentSimulation();
    VegetativeType state;
    int            cTime = simulation.getCurrentTimeStep();

    if (simulation.isYearlyTimeSteps()) {
      numDecades *= 10;
    }

    if (numDecades > cTime) {
      return false;
    }

    for(int i=cTime; i>(cTime - numDecades); i--) {
      if (producingSeed(i,true) != true) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines whether or not this Evu will produce seed,
   * for the current time step.
   */
//  private boolean calculateProducingSeed() {
//    return calculateProducingSeed(false);
//  }

  public enum RegenTypes {IN_PLACE_SEED, IN_LANDSCAPE_SEED, ADJACENT_SEED};

  public static final RegenTypes IN_PLACE_SEED     = RegenTypes.IN_PLACE_SEED;
  public static final RegenTypes IN_LANDSCAPE_SEED = RegenTypes.IN_LANDSCAPE_SEED;
  public static final RegenTypes ADJACENT_SEED     = RegenTypes.ADJACENT_SEED;

  private boolean calculateProducingSeed(int tStep, boolean postTreatment,
                                         Lifeform lifeform, RegenTypes regenType)
  {
    Simulation     simulation = Simpplle.getCurrentSimulation();

    if (simulation == null) {
      return false;
    }

    int cTime = simulation.getCurrentTimeStep();

    VegetativeType state;
    if (cTime == tStep) {
      VegSimStateData simState = getState(lifeform);
      if (simState == null) { return false; }
      state = simState.getVeg();
    }
    else {
      if (postTreatment) {
        state = getPastTreatedState(tStep);
      }
      else {
        VegSimStateData simState = getState(lifeform);
        state = (simState != null ? simState.getVeg() : null);
      }
      if (state == null) {
        VegSimStateData simState = getState(tStep,lifeform);
        state = (simState != null ? simState.getVeg() : null);
      }
    }
    if (state == null) { return false; }

    return calculateProducingSeed(state,tStep,lifeform,regenType);
  }

  /**
   * Determines whether or not this Evu will produce seed,
   * for the given time step.
   *   -- If postTreatment is true, seed production will be
   *      calculated based on the post treated state.
   */
  public boolean calculateProducingSeed(VegetativeType state,
                                         int tStep,
                                         Lifeform lifeform,
                                         RegenTypes regenType)
  {
    RegionalZone   zone = Simpplle.currentZone;

    if ((zone instanceof ColoradoPlateau) ||
        (zone instanceof WestsideRegionOne) ||
        (zone instanceof EastsideRegionOne))
    {
      return ProducingSeedLogic.getInstance().isSeedProducing(state,this,tStep,lifeform,regenType);
    }

    // For other zones try producing seed logic if any.  If no seed revert to old logic below.
    boolean producing = ProducingSeedLogic.getInstance().isSeedProducing(state,this,tStep,lifeform,regenType);
    if (producing) { return producing; }


    SizeClass sizeClass = state.getSizeClass();
    Species species   = state.getSpecies();

    if (zone.getId() == ValidZones.SIERRA_NEVADA ||
        zone.getId() == ValidZones.SOUTHERN_CALIFORNIA) {
      if (species == Species.CA_CHP) {
        VegSimStateData priorState = getState(tStep-1,lifeform);
        if (priorState == null) { return false; }
        if (priorState.getProcess() == ProcessType.STAND_REPLACING_FIRE) {
          return false;
        }
        else {
          return true;
        }
      }
      if (species == Species.MTN_CHP) {
        return true;
      }
    }

    if (zone.getId() == ValidZones.GILA) {
      if (sizeClass == SizeClass.POLE ||
          sizeClass == SizeClass.MEDIUM ||
          sizeClass == SizeClass.LARGE ||
          sizeClass == SizeClass.VERY_LARGE) {
        return true;
      }
      else { return false; }
    }
    else if (zone.getId() == ValidZones.SOUTH_CENTRAL_ALASKA) {
      if (sizeClass == SizeClass.POLE ||
          sizeClass == SizeClass.LARGE ||
          sizeClass == SizeClass.SS_LARGE ||
          sizeClass == SizeClass.SS_POLE ||
          sizeClass == SizeClass.POLE_SS ||
          sizeClass == SizeClass.POLE_LARGE ||
          sizeClass == SizeClass.POLE_POLE ||
          sizeClass == SizeClass.LARGE_SS ||
          sizeClass == SizeClass.LARGE_POLE ||
          sizeClass == SizeClass.LARGE_LARGE) {
        return true;
      }
      else { return false; }
    }
    else if (zone.getId() == ValidZones.SOUTHWEST_UTAH) {
      if (sizeClass == SizeClass.POLE ||
          sizeClass == SizeClass.MEDIUM ||
          sizeClass == SizeClass.LARGE ||
          sizeClass == SizeClass.MS ||
          sizeClass == SizeClass.TS) {
        return true;
      }
      else { return false; }
    }
    else if (zone instanceof ColoradoFrontRange) {
      if (species.getLifeform() == Lifeform.SHRUBS ||
          species.getLifeform() == Lifeform.HERBACIOUS) {
        return true;
      }
      else if ((sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
                sizeClass == SizeClass.VERY_LARGE  || sizeClass == SizeClass.VLMU) &&
               (species == Species.ABLA_PICO) ||
               (species == Species.PIAR_PICO) ||
               (species == Species.PIAR_PIEN) ||
               (species.equals(Species.get("PIAR-PIFL"))) ||
               (species == Species.PIAR_PIPO) ||
               (species == Species.PIAR_POTR5) ||
               (species == Species.PIAR_PSME) ||
               (species == Species.PICO) ||
               (species == Species.PIED_PIPO) ||
               (species == Species.PIEN_PICO) ||
               (species == Species.PIFL2_PICO) ||
               (species == Species.POTR5_PICO)) {
        return true;
      }
      else if ((sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU ||
                sizeClass == SizeClass.LARGE  || sizeClass == SizeClass.LMU ||
                sizeClass == SizeClass.VERY_LARGE  || sizeClass == SizeClass.VLMU) &&
               (species == Species.ABCO_PIEN) ||
               (species == Species.ABCO_PIFL2) ||
               (species == Species.ABCO_PIPO) ||
               (species == Species.ABCO_POTR5) ||
               (species == Species.ABCO_PSME) ||
               (species == Species.ABLA) ||
               (species == Species.ACNE2) ||
               (species == Species.JUSC2_POTR5) ||
               (species == Species.PIED_JUMO) ||
               (species == Species.PIEN) ||
               (species == Species.PIEN_ABLA) ||
               (species == Species.PIEN_PIPU) ||
               (species == Species.POTR5_ABLA) ||
               (species == Species.POTR5_PIPO) ||
               (species == Species.PSME_JUSC2) ||
               (species == Species.PSME_PIPO)) {
        return true;
      }
      else if ((sizeClass == SizeClass.SS ||
                sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU ||
                sizeClass == SizeClass.LARGE  || sizeClass == SizeClass.LMU ||
                sizeClass == SizeClass.VERY_LARGE  || sizeClass == SizeClass.VLMU) &&

               (species == Species.ABLA_PIAR) ||
               (species == Species.ABLA_PIEN) ||
               (species == Species.ABLA_PIFL2) ||
               (species == Species.ACNE2_PSME) ||
               (species == Species.JUMO) ||
               (species == Species.JUMO_PIED) ||
               (species == Species.JUSC2) ||
               (species == Species.JUSC2_PIED) ||
               (species == Species.JUSC2_PIPO) ||
               (species == Species.JUSC2_PSME) ||
               (species == Species.PIAR) ||
               (species == Species.PICO_ABLA) ||
               (species == Species.PICO_PIEN) ||
               (species == Species.PICO_PIFL2) ||
               (species == Species.PICO_POTR5) ||
               (species == Species.PICO_PSME) ||
               (species == Species.PIED) ||
               (species == Species.PIED_ABCO) ||
               (species == Species.PIED_JUSC2) ||
               (species == Species.PIED_PIAR) ||
               (species == Species.PIED_POTR5) ||
               (species == Species.PIED_PSME) ||
               (species == Species.PIEN_ABCO) ||
               (species == Species.PIEN_PIAR) ||
               (species == Species.PIEN_PIFL2) ||
               (species == Species.PIEN_POTR5) ||
               (species == Species.PIEN_PSME) ||
               (species == Species.PIFL2) ||
               (species == Species.PIFL2_ABCO) ||
               (species == Species.PIFL2_PIAR) ||
               (species == Species.PIFL2_PIEN) ||
               (species == Species.PIFL2_PIPO) ||
               (species == Species.PIFL2_POTR5) ||
               (species == Species.PIFL2_PSME) ||
               (species == Species.PIPO) ||
               (species == Species.PIPO_ABCO) ||
               (species == Species.PIPO_JUSC2) ||
               (species == Species.PIPO_PIAR) ||
               (species == Species.PIPO_PICO) ||
               (species == Species.PIPO_PIED) ||
               (species == Species.PIPO_PIFL2) ||
               (species == Species.PIPO_PIPU) ||
               (species == Species.PIPO_POAN3) ||
               (species == Species.PIPO_POTR5) ||
               (species == Species.PIPO_PSME) ||
               (species == Species.PIPU) ||
               (species == Species.PIPU_PIPO) ||
               (species == Species.PIPU_POAN3) ||
               (species == Species.PIPU_POTR5) ||
               (species == Species.PIPU_PSME) ||
               (species == Species.POAN3) ||
               (species == Species.POAN3_PIPO) ||
               (species == Species.POAN3_PIPU) ||
               (species == Species.POAN3_POTR5) ||
               (species == Species.POAN3_PSME) ||
               (species == Species.POTR5) ||
               (species == Species.POTR5_ABCO) ||
               (species == Species.POTR5_PIAR) ||
               (species == Species.POTR5_PIEN) ||
               (species == Species.POTR5_PIFL2) ||
               (species == Species.POTR5_PIPU) ||
               (species == Species.POTR5_POAN3) ||
               (species == Species.POTR5_PSME) ||
               (species == Species.PSME) ||
               (species == Species.PSME_ABCO) ||
               (species == Species.PSME_PIAR) ||
               (species == Species.PSME_PICO) ||
               (species == Species.PSME_PIED) ||
               (species == Species.PSME_PIEN) ||
               (species == Species.PSME_PIFL2) ||
               (species == Species.PSME_PIPU) ||
               (species == Species.PSME_POTR5) ||
               (species == Species.PSME_POTR5)) {
        return true;
      }
      else {
        return false;
      }

    }
    else if (zone instanceof ColoradoPlateau) {
      if (regenType == null) { return false; }

      if (species.getLifeform() == Lifeform.HERBACIOUS &&
          (regenType == ADJACENT_SEED)) {
        if (
            species == Species.get("ACHY") ||
            species == Species.BOGR2 ||
            species == Species.BRTE ||
            species == Species.CACA4 ||
            species == Species.CAEL3_CARUD ||
            species == Species.CAFO3 ||
            species == Species.CAGE2 ||
            species == Species.get("CANU4") ||
            species == Species.CAPU ||
            species == Species.CAREX ||
            species == Species.CAREX_JUNCU ||
            species == Species.CAREXU ||
            species == Species.CAREXU_CARU ||
            species == Species.CARO5 ||
            species == Species.CARUD_FEBRC ||
            species == Species.FEAR2 ||
            species == Species.FEAR2_BOGR2 ||
            species == Species.FEAR2_DAPA2 ||
            species == Species.FEAR2_MUMO ||
            species == Species.FEID ||
            species == Species.FETH ||
            species == Species.get("GERI") ||
            species == Species.HECO26 ||
            species == Species.get("HECO26-PLJA") ||
            species == Species.get("JUBAL-CAGE") ||
            species == Species.LEKI2 ||
            species == Species.MUMO ||
            species == Species.get("NIAT-CHFR3") ||
            species == Species.get("ORSE") ||
            species == Species.PASM ||
            species == Species.PG_FORBS ||
            species == Species.PHCO9_POAL2 ||
            species == Species.POAL2_CAEL3 ||
            species == Species.POAL2_KOMY ||
            species == Species.POFE ||
            species == Species.POPR ||
            species == Species.get("POPR-VIAM") ||
            species == Species.get("POSE-PASM") ||
            species == Species.PSSP6 ||
            species == Species.get("SPAI") ||
            species == Species.get("THFE"))
        {
          return true;
        }
      }
      else if (species.getLifeform() == Lifeform.SHRUBS &&
               (regenType == ADJACENT_SEED)) {
        if (
            species == Species.ACGL ||
            species == Species.ALINT ||
            species == Species.ALINT_BEOC2 ||
            species == Species.AMAL2 ||
            species == Species.ARTR2 ||
            species == Species.ARTR2_CEMO2 ||
            species == Species.ARTR2_JUCO6 ||
            species == Species.ARTRV ||
            species == Species.ARTRV_PUTR2 ||
            species == Species.ARTRW8 ||
            species == Species.ARTRW8_CHVI8 ||
            species == Species.ARUV ||
            species == Species.CEMO2 ||
            species == Species.CEMO2_ARTR2 ||
            species == Species.CEMO2_ARUV ||
            species == Species.CEMO2_DAFL3 ||
            species == Species.CEMO2_JUCO6 ||
            species == Species.CEMO2_PHMO4 ||
            species == Species.CEMO2_PUTR2 ||
            species == Species.CEMO2_QUGA ||
            species == Species.CEMO2_RIBES ||
            species == Species.CEMO2_RICE ||
            species == Species.CEMO2_SALIXU ||
            species == Species.get("CEMO2-SYOR2") ||
            species == Species.CHVI8 ||
            species == Species.DAFL3 ||
            species == Species.ERPAA4 ||
            species == Species.JAAM ||
            species == Species.JUCO6 ||
            species == Species.JUCO6_ARUV ||
            species == Species.JUCO6_SALIXU ||
            species == Species.get("LOIN5") ||
            species == Species.PHMO4 ||
            species == Species.PUTR2 ||
            species == Species.PUTR2_ARTRV ||
            species == Species.PUTR2_CEMO2 ||
            species == Species.PUTR2_RIBES ||
            species == Species.QUGA ||
            species == Species.get("QUGA-ACGL") ||
            species == Species.QUGA_AMAL2 ||
            species == Species.QUGA_ARUV ||
            species == Species.QUGA_CEMO2 ||
            species == Species.QUGA_JUCO6 ||
            species == Species.get("QUGA-PRVI") ||
            species == Species.QUGA_SALIXU ||
            species == Species.QUGA_VASC ||
            species == Species.RIBES ||
            species == Species.RIBES_PUTR2 ||
            species == Species.RICE ||
            species == Species.get("SAEX") ||
            species == Species.get("SAGE2") ||
            species == Species.SAGL ||
            species == Species.SALIX ||
            species == Species.SALIX_ALINT ||
            species == Species.SALIX_BEOC2 ||
            species == Species.SALIXU ||
            species == Species.SALIXU_RIBES ||
            species == Species.get("SAVE4") ||
            species == Species.get("SYORO") ||
            species == Species.SHCA ||
            species == Species.get("TARA") ||
            species == Species.VAMY2 ||
            species == Species.get("VAMY2-RIMO2") ||
            species == Species.VASC)
        {
          return true;
        }
      }
      else if (species.getLifeform() == Lifeform.TREES) {
        if (regenType == ADJACENT_SEED) {
          if (
              species == Species.ABLA_PIAR ||
              species == Species.ABLA_PIEN ||
              species == Species.ABLA_PIFL2 ||
              species == Species.ACNE2_PSME ||
              species == Species.JUMO ||
              species == Species.JUMO_PIED ||
              species == Species.JUSC2 ||
              species == Species.JUSC2_PIED ||
              species == Species.JUSC2_PIPO ||
              species == Species.JUSC2_PSME ||
              species == Species.PIAR ||
              species == Species.PICO_ABLA ||
              species == Species.PICO_PIEN ||
              species == Species.PICO_PIFL2 ||
              species == Species.PICO_POTR5 ||
              species == Species.PICO_PSME ||
              species == Species.PIED ||
              species == Species.PIED_ABCO ||
              species == Species.PIED_JUMO ||
              species == Species.get("PIED-JUOS") ||
              species == Species.PIED_JUSC2 ||
              species == Species.PIED_PIAR ||
              species == Species.PIED_POTR5 ||
              species == Species.PIED_PSME ||
              species == Species.PIEN_ABCO ||
              species == Species.PIEN_PIAR ||
              species == Species.PIEN_PIFL2 ||
              species == Species.PIEN_POTR5 ||
              species == Species.PIEN_PSME ||
              species == Species.PIFL2 ||
              species == Species.PIFL2_ABCO ||
              species == Species.PIFL2_PIAR ||
              species == Species.PIFL2_PIEN ||
              species == Species.PIFL2_PIPO ||
              species == Species.PIFL2_POTR5 ||
              species == Species.PIFL2_PSME ||
              species == Species.PIPO ||
              species == Species.PIPO_ABCO ||
              species == Species.PIPO_JUSC2 ||
              species == Species.PIPO_PIAR ||
              species == Species.PIPO_PICO ||
              species == Species.PIPO_PIED ||
              species == Species.PIPO_PIFL2 ||
              species == Species.PIPO_PIPU ||
              species == Species.PIPO_POAN3 ||
              species == Species.PIPO_POTR5 ||
              species == Species.PIPO_PSME ||
              species == Species.PIPU ||
              species == Species.PIPU_PIPO ||
              species == Species.PIPU_POAN3 ||
              species == Species.PIPU_POTR5 ||
              species == Species.PIPU_PSME ||
              species == Species.POAN3 ||
              species == Species.POAN3_PIPO ||
              species == Species.POAN3_PIPU ||
              species == Species.POAN3_POTR5 ||
              species == Species.POAN3_PSME ||
              species == Species.POTR5 ||
              species == Species.POTR5_ABCO ||
              species == Species.POTR5_PIAR ||
              species == Species.POTR5_PIEN ||
              species == Species.POTR5_PIFL2 ||
              species == Species.POTR5_PIPU ||
              species == Species.POTR5_POAN3 ||
              species == Species.POTR5_PSME ||
              species == Species.PSME ||
              species == Species.PSME_ABCO ||
              species == Species.PSME_PIAR ||
              species == Species.PSME_PICO ||
              species == Species.PSME_PIED ||
              species == Species.PSME_PIEN ||
              species == Species.PSME_PIFL2 ||
              species == Species.PSME_PIPU ||
              species == Species.PSME_POTR5 ||
              species == Species.PSME_POTR5) {
            return true;
          }
          else if (
              (sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU ||
               sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
               sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) &&

              (species == Species.ABCO ||
               species == Species.ABCO_PIEN ||
               species == Species.ABCO_PIFL2 ||
               species == Species.ABCO_PIPO ||
               species == Species.ABCO_POTR5 ||
               species == Species.ABCO_PSME ||
               species == Species.ABLA ||
               species == Species.ABLA_PICO ||
               species == Species.ACNE2 ||
               species == Species.JUSC2_POTR5 ||
               species == Species.PIAR_PICO ||
               species == Species.PIAR_PIEN ||
               species == Species.get("PIAR-PIFL") ||
               species == Species.PIAR_PIPO ||
               species == Species.PIAR_POTR5 ||
               species == Species.PIAR_PSME ||
               species == Species.PICO ||
               species == Species.PIED_PIPO ||
               species == Species.PIEN ||
               species == Species.PIEN_ABLA ||
               species == Species.get("PIEN-ABLA-POTR5") ||
               species == Species.PIEN_PICO ||
               species == Species.PIEN_PIPU ||
               species == Species.PIFL2_PICO ||
               species == Species.POTR5_ABLA ||
               species == Species.POTR5_PICO ||
               species == Species.POTR5_PIPO ||
               species == Species.PSME_JUSC2 ||
               species == Species.PSME_PIPO)) {
            return true;
          }
        }

        else if (regenType == IN_PLACE_SEED) {
          if ((sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU ||
               sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
               sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) &&

              (species == Species.PICO_PSME ||
               species == Species.PIPO ||
               species == Species.PIPO_ABCO ||
               species == Species.PIPO_JUSC2 ||
               species == Species.PIPO_PIAR ||
               species == Species.PIPO_PICO ||
               species == Species.PIPO_PIED ||
               species == Species.PIPO_PIFL2 ||
               species == Species.PIPO_PIPU ||
               species == Species.PIPO_POAN3 ||
               species == Species.PIPO_POTR5 ||
               species == Species.PIPO_PSME ||
               species == Species.PSME ||
               species == Species.PSME_ABCO ||
               species == Species.PSME_PIAR ||
               species == Species.PSME_PICO ||
               species == Species.PSME_PIED ||
               species == Species.PSME_PIEN ||
               species == Species.PSME_PIFL2 ||
               species == Species.PSME_PIPU ||
               species == Species.PSME_POTR5 ||
               species == Species.PSME_POTR5 ||
               species == Species.PICO ||
               species == Species.PSME_JUSC2 ||
               species == Species.PSME_PIPO))
          {
            return true;
          }
        }

        else if (regenType == IN_LANDSCAPE_SEED) {
          if ((sizeClass == SizeClass.MEDIUM || sizeClass == SizeClass.MMU ||
               sizeClass == SizeClass.LARGE || sizeClass == SizeClass.LMU ||
               sizeClass == SizeClass.VERY_LARGE || sizeClass == SizeClass.VLMU) &&

              (species == Species.JUMO_PIED ||
               species == Species.JUSC2 ||
               species == Species.JUSC2_PIED ||
               species == Species.PIED ||
               species == Species.PIED_JUMO ||
               species == Species.PIED_JUSC2 ||
               species == Species.get("PIED-JUOS")))
          {
            return true;
          }
        }
        return false;

      } // End of Lifeform.TREES

      return false;
    }

    VegSimStateData simState = getState(tStep,lifeform);
    if ((sizeClass == SizeClass.NF      ||
         sizeClass == SizeClass.NS      ||
         sizeClass == SizeClass.AGR     ||
         sizeClass == SizeClass.WATER   ||
         sizeClass == SizeClass.SS                || sizeClass == SizeClass.CLUMPED           ||
         sizeClass == SizeClass.SCATTERED         || sizeClass == SizeClass.UNIFORM           ||
         sizeClass == SizeClass.OPEN_HERB         || sizeClass == SizeClass.CLOSED_HERB       ||
         sizeClass == SizeClass.OPEN_LOW_SHRUB    || sizeClass == SizeClass.CLOSED_LOW_SHRUB  ||
         sizeClass == SizeClass.OPEN_MID_SHRUB    || sizeClass == SizeClass.CLOSED_MID_SHRUB  ||
         sizeClass == SizeClass.OPEN_TALL_SHRUB   || sizeClass == SizeClass.CLOSED_TALL_SHRUB ||
         sizeClass == SizeClass.WOODLAND) ||
        (sizeClass == SizeClass.POLE &&
         (species != Species.ES    && species != Species.AF    && species != Species.LP       &&
          species != Species.ES_AF && species != Species.LP_AF && species != Species.AF_ES_LP &&
          species != Species.WB_ES_AF && species != Species.JUSC)) ||
        simState.getProcess().equals(ProcessType.SEVERE_WSBW)) {
      return false;
    }
    else {
      return true;
    }
  }

  /**
   * Returns true if the process has been succession for
   * the past numDecades decades.
   * @param numDecades is an int.
   * @return a boolean.
   */
  public boolean succession_n_decades(int numDecades) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int processId;
    int cTime = simulation.getCurrentTimeStep();

    if (simulation.isYearlyTimeSteps()) {
      numDecades *= 10;
    }

    if (numDecades > cTime) {
      return false;
    }

    for(int i=cTime; i>(cTime - numDecades); i--) {
      VegSimStateData state = getState(i);
      if (state != null && state.getProcess().equals(ProcessType.SUCCESSION) == false) {
        return false;
      }
    }
    return true;
  }
  public boolean succession_n_decades(int numDecades, Lifeform lifeform) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int processId;
    int cTime = simulation.getCurrentTimeStep();

    if (simulation.isYearlyTimeSteps()) {
      numDecades *= 10;
    }

    if (numDecades > cTime) {
      return false;
    }

    for(int i=cTime; i>(cTime - numDecades); i--) {
      VegSimStateData state = getState(i,lifeform);
      if (state != null && (state.getProcess() == ProcessType.SUCCESSION)  == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return true if this Evu is a Candidate for Weed encroachment.
   * @return a boolean.
   */
  public boolean isWeedCandidate() {
    int age, density;

    HabitatTypeGroupType groupType = getHabitatTypeGroup().getType();
    Species              species   = (Species)getState(SimpplleType.SPECIES);
    if (species == null) { return false; }

    if ((groupType.equals(HabitatTypeGroupType.NF1A) ||
         groupType.equals(HabitatTypeGroupType.NF1B) ||
         groupType.equals(HabitatTypeGroupType.NF1C) ||
         groupType.equals(HabitatTypeGroupType.NF2A) ||
         groupType.equals(HabitatTypeGroupType.NF2B) ||
         groupType.equals(HabitatTypeGroupType.NF2C) ||
         groupType.equals(HabitatTypeGroupType.NF2D) ||
         groupType.equals(HabitatTypeGroupType.NF3A) ||
         // Sierra Nevada and Southern California Zones.
         groupType.equals(HabitatTypeGroupType.FTH_M) ||
         groupType.equals(HabitatTypeGroupType.FTH_X) ||
         groupType.equals(HabitatTypeGroupType.LM_M)  ||
         groupType.equals(HabitatTypeGroupType.LM_X)  ||
         groupType.equals(HabitatTypeGroupType.UM_M)  ||
         groupType.equals(HabitatTypeGroupType.UM_X)  ||
         groupType.equals(HabitatTypeGroupType.SA)) &&
        (species == Species.EARLY_SERAL     || species == Species.MID_SERAL ||
         species == Species.ALTERED_GRASSES || species == Species.HERBS     ||
         species == Species.MESIC_SHRUBS    || species == Species.MTN_MAHOGANY ||
         species == Species.FS_S_G          || species == Species.MTN_FS_SHRUBS ||
         species == Species.MTN_SHRUBS      || species == Species.XERIC_SHRUBS  ||
         species == Species.FESCUE          || species == Species.AGSP          ||
         /*species == Species.AGSM        ||*/ species == Species.JUSC          ||
         species == Species.JUSC_ORMI       || species == Species.JUSC_AGSP     ||
         species == Species.XERIC_FS_SHRUBS ||
         // Sierra Nevada and Southern California Zones.
         species == Species.GRASS || species == Species.CSS)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Return true if Weed will spread from this Evu to another.
   * @return a boolean.
   */
  public boolean weedWillSpread(Evu adj) {
    boolean      grazing = false;
    ProcessType  processType;
    int          prob = Simulation.getInstance().random();

    Treatment     treat = adj.getCurrentTreatment();
    TreatmentType treatType = TreatmentType.NONE;
    if (treat != null) { treatType = treat.getType(); }

    VegSimStateData adjState = adj.getState();
    if (adjState == null) { return false; }
    processType = adj.getState().getProcess();

    if (treatType == TreatmentType.LOW_INTENSITY_GRAZING ||
        treatType == TreatmentType.MODERATE_INTENSITY_GRAZING ||
        treatType == TreatmentType.HIGH_INTENSITY_GRAZING) {
      grazing = true;
    }
    else { grazing = false;}

    if (processType.equals(ProcessType.STAND_REPLACING_FIRE)) {
      return true;
    }
    else if ((getRoadStatusNew() == Roads.Status.OPEN &&
              adj.getRoadStatusNew() == Roads.Status.OPEN) &&
             ((grazing && prob < 9000) || (prob < 8000))) {
      return true;
    }
    else if (grazing && prob < 7000) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * This functions attempts to fix the invalid state information
   * this unit.  If the current state is invalid it is because one
   * of its' components is invalid (i.e species, size class, or density).
   * If these are all correct then we must have a state that is not
   * valid for the habitat type group.
   *
   * The logic for fixing these is broken down into 3 cases:
   *   1. Where the Species or Size Class is "NF" or "NS" and
   *      the Habitat Type Group is valid
   *
   *   2. The Species is Valid for the Habitat Type Group,
   *      which is also valid.
   *
   *   3. The Species is NOT Valid for the Habitat Type Group,
   *      which is Valid.
   *
   */
  public void fixIncorrectState() {
    HabitatTypeGroupType groupType;

    VegSimStateData state = getState();
    if (state == null) { return; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();

    if (isCurrentStateValid()) { return; }

    // If the Habitat Type is not valid then user will
    // have to fix by hand.
    if (isHabitatTypeGroupValid() == false) { return; }

    while (isValid() == false) {
      groupType = getHabitatTypeGroup().getType();

      if ((species == Species.NS     || species == Species.NF ||
           sizeClass == SizeClass.NS || sizeClass == SizeClass.NF ||
           (groupType.equals(HabitatTypeGroupType.XX1) ||
            groupType.equals(HabitatTypeGroupType.XX4) ||
            groupType.equals(HabitatTypeGroupType.XX5))) &&
          isHabitatTypeGroupValid()) {
        fixInvalidCaseOne();
        if (isValid()) { return; }
      }

      if (isSpeciesValid() && isHabitatTypeGroupValid() &&
          getHabitatTypeGroup().isMemberSpecies(species)) {
        fixInvalidCaseTwo();
      }

      if (isHabitatTypeGroupValid() &&
          getHabitatTypeGroup().isMemberSpecies(species) == false) {
        fixInvalidCaseThree();
      }

      if (isValid() == false) {
        // We Give Up.  User will have to correct by hand.
        return;
      }
    }
  }
  /**
   * Fix of Invalid states - a flag that something is invalid within a state.  This is fix of case one:
   *  Where the Species or Size Class is "NF" or "NS" and
   *      the Habitat Type Group is valid
   *
   */

  private void fixInvalidCaseOne() {
    HabitatTypeGroupType groupType = htGrp.getType();
    String               newStateStr;

    if (groupType.equals(HabitatTypeGroupType.XX1))        { newStateStr = "AGR/AGR/1"; }
    else if (groupType.equals(HabitatTypeGroupType.XX4))   { newStateStr = "NF/NF/1"; }
    else if (groupType.equals(HabitatTypeGroupType.XX5))   { newStateStr = "WATER/WATER/1"; }
    else if (groupType.equals(HabitatTypeGroupType.NF1A))  { newStateStr = "MID-SERAL/UNIFORM/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF1B))  { newStateStr = "MID-SERAL/UNIFORM/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF1C))  { newStateStr = "MID-SERAL/UNIFORM/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF2A))  { newStateStr = "MESIC-SHRUBS/CLUMPED/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF2B))  { newStateStr = "FESCUE/UNIFORM/3"; }
    else if (groupType.equals(HabitatTypeGroupType.NF2C))  { newStateStr = "AGSP/UNIFORM/3"; }
    else if (groupType.equals(HabitatTypeGroupType.NF2D))  { newStateStr = "AGSP/UNIFORM/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF3A))  { newStateStr = "FESCUE/UNIFORM/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF3B))  { newStateStr = "HERBS/SCATTERED/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF3C))  { newStateStr = "AGSP/UNIFORM/3"; }
    else if (groupType.equals(HabitatTypeGroupType.NF3D))  { newStateStr = "QA/SS/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF4A))  { newStateStr = "RIP-SHRUBS/UNIFORM/4"; }
    else if (groupType.equals(HabitatTypeGroupType.NF4B))  { newStateStr = "RIP-SHRUBS/CLUMPED/4"; }
    else if (groupType.equals(HabitatTypeGroupType.NF4C))  { newStateStr = "RIP-SHRUBS/CLUMPED/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF4D))  { newStateStr = "RIP-GRAMS/UNIFORM/4"; }
    else if (groupType.equals(HabitatTypeGroupType.NF4E))  { newStateStr = "RIP-DECID/SS/2"; }
    else if (groupType.equals(HabitatTypeGroupType.NF5A))  { newStateStr = "ALPINE-HERBS/UNIFORM/3"; }
    else if (groupType.equals(HabitatTypeGroupType.NF5B))  { newStateStr = "ALPINE-SHRUBS/CLUMPED/2"; }

    else if (groupType.equals(HabitatTypeGroupType.A1))  { newStateStr = "UPLAND-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.A2))  { newStateStr = "UPLAND-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.B1))  { newStateStr = "UPLAND-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.B2))  { newStateStr = "UPLAND-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.B3))  { newStateStr = "UPLAND-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.C1))  { newStateStr = "UPLAND-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.C2))  { newStateStr = "MESIC-SHRUBS/OPEN-TALL-SHRUB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.D1))  { newStateStr = "MESIC-SHRUBS/OPEN-TALL-SHRUB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.D2))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.D3))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.E1))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.E2))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.F1))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.F2))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.G1))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else if (groupType.equals(HabitatTypeGroupType.G2))  { newStateStr = "ALPINE-GRASSES/CLOSED-HERB/1"; }
    else { newStateStr = null; }

    if (newStateStr != null) {
      VegetativeType newState = htGrp.getVegetativeType(newStateStr);
      if (newState != null) {
        setState(newState);
      }
    }
  }
  /**
   * Fix of Invalid states - a flag that something is invalid within a state.  This is fix of case two:
   * The Species is Valid for the Habitat Type Group,  which is also valid.
   *
   */
  private void fixInvalidCaseTwo() {
    VegSimStateData state = getState();
    if (state == null) { return; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();
    int       age       = state.getVeg().getAge();

    Lifeform       speciesType = species.getLifeform();
    SizeClass      newSizeClass;
    Density        newDensity;
    VegetativeType newState = null;


    // Case 2A.
    if (speciesType == Lifeform.TREES && SizeClass.isForested(sizeClass)) {
      if (density == Density.ONE) {
        newDensity = Density.TWO;
        newState = htGrp.getVegetativeType(species,sizeClass,age,newDensity);
      }
      else {
        newDensity = density;
      }

      // Case 2B.
      if (newState == null) {
        newSizeClass = null;

        if (sizeClass == SizeClass.VERY_LARGE)  { newSizeClass = SizeClass.LARGE;  }
        else if (sizeClass == SizeClass.LARGE)  { newSizeClass = SizeClass.MEDIUM; }
        else if (sizeClass == SizeClass.MEDIUM) { newSizeClass = SizeClass.POLE;   }
        else if (sizeClass == SizeClass.MMU)    { newSizeClass = SizeClass.MTS;    }
        else if (sizeClass == SizeClass.PMU)    { newSizeClass = SizeClass.PTS;    }
        else if (sizeClass == SizeClass.LMU)    { newSizeClass = SizeClass.LTS;    }
        else if (sizeClass == SizeClass.VLMU)   { newSizeClass = SizeClass.VLTS;   }
        else { newSizeClass = null; }

        if (newSizeClass != null) {
          newState = htGrp.getVegetativeType(species,newSizeClass,age,newDensity);
        }
        if (newState == null && newSizeClass != null) {
          newState = htGrp.getVegetativeType(species,newSizeClass,age,density);
        }
      }
    }

    // Case 2C.
    if (newState == null) {
      if (species.getLifeform() == Lifeform.TREES &&
          SizeClass.isNonForested(sizeClass)) {
        newState = htGrp.getVegetativeType(species,SizeClass.SS,age,Density.TWO);
      }
    }

    // Case 2D.
    if (newState == null) {
      if (species == Species.MESIC_SHRUBS || species == Species.XERIC_SHRUBS) {
        newState = htGrp.getVegetativeType(species,SizeClass.CLOSED_LOW_SHRUB,
                                           age,density);
      }
    }

    // Case 2E.
    if (newState == null) {
      if (speciesType != Lifeform.TREES) {
        newState = null;
        if (density == Density.ONE)        { newDensity = Density.TWO; }
        else if (density == Density.TWO)   { newDensity = Density.THREE; }
        else if (density == Density.THREE) { newDensity = Density.FOUR; }
        else { newDensity = null; }

        if (newDensity != null) {
          newState = htGrp.getVegetativeType(species,sizeClass,age,newDensity);
        }

        if  (newState == null) {

          if (sizeClass == SizeClass.UNIFORM)                { newSizeClass = SizeClass.OPEN_HERB;         }
          else if (sizeClass == SizeClass.OPEN_HERB)         { newSizeClass = SizeClass.CLOSED_HERB;       }
          else if (sizeClass == SizeClass.CLOSED_HERB)       { newSizeClass = SizeClass.SCATTERED;         }
          else if (sizeClass == SizeClass.SCATTERED)         { newSizeClass = SizeClass.CLUMPED;           }
          else if (sizeClass == SizeClass.CLUMPED)           { newSizeClass = SizeClass.OPEN_LOW_SHRUB;    }
          else if (sizeClass == SizeClass.OPEN_TALL_SHRUB)   { newSizeClass = SizeClass.OPEN_MID_SHRUB;    }
          else if (sizeClass == SizeClass.OPEN_MID_SHRUB)    { newSizeClass = SizeClass.OPEN_LOW_SHRUB;    }
          else if (sizeClass == SizeClass.OPEN_LOW_SHRUB)    { newSizeClass = SizeClass.CLOSED_TALL_SHRUB; }
          else if (sizeClass == SizeClass.CLOSED_TALL_SHRUB) { newSizeClass = SizeClass.CLOSED_MID_SHRUB;  }
          else if (sizeClass == SizeClass.CLOSED_MID_SHRUB)  { newSizeClass = SizeClass.CLOSED_LOW_SHRUB;  }
          else { newSizeClass = null; }

          newState = htGrp.getVegetativeType(species,newSizeClass,age,density);
        }
      }
    }

    if (newState != null) {
      setState(newState);
    }
  }
  /**
   * Fix of Invalid states - a flag that something is invalid within a state.  This is fix of case two:
   * The Species is NOT Valid for the Habitat Type Group,
   *      which is Valid.
   *
   *
   */
  private void fixInvalidCaseThree() {
    VegSimStateData state = getState();
    if (state == null) { return; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();
    int       age       = state.getVeg().getAge();

    HabitatTypeGroupType groupType   = htGrp.getType();
    Lifeform             speciesType = species.getLifeform();
    Species              newSpecies = null;
    VegetativeType       newState = null;

    // Case 3A.
    if (speciesType == Lifeform.TREES &&
        HabitatTypeGroup.isForested(getHabitatTypeGroup())) {
      newSpecies = Species.fixOrder(species);

      if (newSpecies == null) {
        if (species == Species.ES_AF) { newSpecies = Species.ES; }
        else if (species == Species.PP_DF) { newSpecies = Species.DF; }
        else if (species == Species.DF_AF) { newSpecies = Species.DF; }
        else if (species == Species.WB_AF) { newSpecies = Species.AF; }

        // Note: Case of LP-->WB handled below.
        //       We want to try LP-->PF first.
        else if (species == Species.WB) { newSpecies = Species.LP; }
        else if (species == Species.DF) { newSpecies = Species.AF; }
        else if (species == Species.LP) { newSpecies = Species.PF; }
        else if (species == Species.PF) { newSpecies = Species.LP; }
        else { newSpecies = null; }
      }

      if (newSpecies != null) {
        newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,density);
        if (newState == null && species == Species.LP) {
          newSpecies = Species.WB;
          newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,density);
        }
      }
    }

    // Case 3B.
    if (newState == null && speciesType == Lifeform.TREES &&
        HabitatTypeGroup.isNonForested(getHabitatTypeGroup())) {
      if (species == Species.LP || species == Species.WB) {
        newState = htGrp.getVegetativeType(Species.PF,sizeClass,age,density);
      }

      // Case 3C
      if (newState == null) {
        String newStateStr;
        if (groupType.equals(HabitatTypeGroupType.XX1))        { newStateStr = "AGR/AGR/1"; }
        else if (groupType.equals(HabitatTypeGroupType.XX4))   { newStateStr = "NF/NF/1"; }
        else if (groupType.equals(HabitatTypeGroupType.XX5))   { newStateStr = "WATER/WATER/1"; }
        else if (groupType.equals(HabitatTypeGroupType.NF1A))  { newStateStr = "MID-SERAL/UNIFORM/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF1B))  { newStateStr = "MID-SERAL/UNIFORM/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF1C))  { newStateStr = "MID-SERAL/UNIFORM/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF2A))  { newStateStr = "MESIC-SHRUBS/CLUMPED/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF2B))  { newStateStr = "FESCUE/UNIFORM/3"; }
        else if (groupType.equals(HabitatTypeGroupType.NF2C))  { newStateStr = "AGSP/UNIFORM/3"; }
        else if (groupType.equals(HabitatTypeGroupType.NF2D))  { newStateStr = "AGSP/UNIFORM/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF3A))  { newStateStr = "FESCUE/UNIFORM/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF3B))  { newStateStr = "HERBS/SCATTERED/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF3C))  { newStateStr = "AGSP/UNIFORM/3"; }
        else if (groupType.equals(HabitatTypeGroupType.NF3D))  { newStateStr = "QA/SS/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF4A))  { newStateStr = "RIP-SHRUBS/UNIFORM/4"; }
        else if (groupType.equals(HabitatTypeGroupType.NF4B))  { newStateStr = "RIP-SHRUBS/CLUMPED/4"; }
        else if (groupType.equals(HabitatTypeGroupType.NF4C))  { newStateStr = "RIP-SHRUBS/CLUMPED/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF4D))  { newStateStr = "RIP-GRAMS/UNIFORM/4"; }
        else if (groupType.equals(HabitatTypeGroupType.NF4E))  { newStateStr = "RIP-DECID/SS/2"; }
        else if (groupType.equals(HabitatTypeGroupType.NF5A))  { newStateStr = "ALPINE-HERBS/UNIFORM/3"; }
        else if (groupType.equals(HabitatTypeGroupType.NF5B))  { newStateStr = "ALPINE-SHRUBS/CLUMPED/2"; }
        else { newStateStr = null; }

        if (newStateStr != null) {
          newState = htGrp.getVegetativeType(newStateStr);
        }
      }
    }

    // Case 3D
    if (newState == null && speciesType != Lifeform.TREES) {
      newSpecies = null;

      if (species == Species.XERIC_SHRUBS) {
        newSpecies = Species.MESIC_SHRUBS;
      }
      else if (species == Species.MESIC_SHRUBS) {
        newSpecies = Species.ALPINE_SHRUBS;
        newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,density);
        if (newState == null) { newSpecies = Species.XERIC_SHRUBS; }
      }
      newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,density);

      if (newState == null) {
        fixInvalidCaseOne();
        return;
      }
    }

    // Case 3E
    if (newState == null && species == Species.EARLY_SERAL &&
        HabitatTypeGroup.isForested(getHabitatTypeGroup())) {

      if (groupType.equals(HabitatTypeGroupType.A1) ||
          groupType.equals(HabitatTypeGroupType.A2) ||
          groupType.equals(HabitatTypeGroupType.B1) ||
          groupType.equals(HabitatTypeGroupType.B2) ||
          groupType.equals(HabitatTypeGroupType.B3) ||
          groupType.equals(HabitatTypeGroupType.C1) ||
          groupType.equals(HabitatTypeGroupType.C2)) {
        newSpecies = Species.PP_DF;
      }
      else if (groupType.equals(HabitatTypeGroupType.D1) ||
               groupType.equals(HabitatTypeGroupType.D2) ||
               groupType.equals(HabitatTypeGroupType.D3) ||
               groupType.equals(HabitatTypeGroupType.E1) ||
               groupType.equals(HabitatTypeGroupType.E2) ||
               groupType.equals(HabitatTypeGroupType.F1) ||
               groupType.equals(HabitatTypeGroupType.F2)) {
        newSpecies = Species.DF_LP;
      }
      else if (groupType.equals(HabitatTypeGroupType.G1)) {
        newSpecies = Species.ES_LP;
      }
      else if (groupType.equals(HabitatTypeGroupType.G2)) {
        newSpecies = Species.ES_AF;
      }
      else {
        newSpecies = null;
      }

      if (newSpecies != null) {
        newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,density);
      }
    }

    // Case 3F
    if (newState == null && species == Species.LATE_SERAL &&
        HabitatTypeGroup.isForested(getHabitatTypeGroup())) {

      if (groupType.equals(HabitatTypeGroupType.A1) ||
          groupType.equals(HabitatTypeGroupType.A2) ||
          groupType.equals(HabitatTypeGroupType.B1) ||
          groupType.equals(HabitatTypeGroupType.B2) ||
          groupType.equals(HabitatTypeGroupType.B3) ||
          groupType.equals(HabitatTypeGroupType.C1) ||
          groupType.equals(HabitatTypeGroupType.C2) ||
          groupType.equals(HabitatTypeGroupType.D1)) {
        newSpecies = Species.DF;
      }
      else if (groupType.equals(HabitatTypeGroupType.D2) ||
               groupType.equals(HabitatTypeGroupType.D3) ||
               groupType.equals(HabitatTypeGroupType.E1) ||
               groupType.equals(HabitatTypeGroupType.E2) ||
               groupType.equals(HabitatTypeGroupType.F1) ||
               groupType.equals(HabitatTypeGroupType.F2) ||
               groupType.equals(HabitatTypeGroupType.G1) ||
               groupType.equals(HabitatTypeGroupType.G2)) {
        newSpecies = Species.AF;
      }
      else {
        newSpecies = null;
      }

      if (newSpecies != null) {
        newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,density);
      }
    }

    // Case 3G
    if (newState == null &&
        (species == Species.EARLY_SERAL || species == Species.LATE_SERAL) &
        HabitatTypeGroup.isNonForested(getHabitatTypeGroup())) {
      fixInvalidCaseOne();
      return;
    }

    if (newState == null && newSpecies != null) {
      fixInvalidCaseFour(newSpecies);
    }

    if (newState != null) {
      setState(newState);
    }
  }

  private void fixInvalidCaseFour(Species newSpecies) {
    VegSimStateData state = getState();
    if (state == null) { return; }

    Species   species   = state.getVeg().getSpecies();
    SizeClass sizeClass = state.getVeg().getSizeClass();
    Density   density   = state.getVeg().getDensity();
    int       age       = state.getVeg().getAge();

    Density        newDensity = null;
    VegetativeType newState = null;
    Lifeform       speciesType = newSpecies.getLifeform();

    // Case 1
    if (speciesType == Lifeform.TREES) {
      if (density == Density.FOUR)       { newDensity = Density.THREE; }
      else if (density == Density.THREE) { newDensity = Density.TWO; }
      else if (density == Density.TWO)   { newDensity = Density.ONE; }
      else { newDensity = null; }
    }
    else {
      if (density == Density.ONE)       { newDensity = Density.TWO; }
      else if (density == Density.TWO)  { newDensity = Density.THREE; }
      else if (density == Density.THREE){ newDensity = Density.FOUR; }
      else { newDensity = null; }
    }
    if (newDensity != null) {
      newState = htGrp.getVegetativeType(newSpecies,sizeClass,age,newDensity);
    }

    // Case 2
    if (newState == null) {
      SizeClass newSizeClass = null;

      if (speciesType != Lifeform.TREES) {
        if (sizeClass == SizeClass.UNIFORM) {
          newSizeClass = SizeClass.OPEN_HERB;
        }
        else if (sizeClass == SizeClass.OPEN_HERB) {
          newSizeClass = SizeClass.CLOSED_HERB;
        }
        else if (sizeClass == SizeClass.CLOSED_HERB) {
          newSizeClass = SizeClass.SCATTERED;
        }
        else if (sizeClass == SizeClass.SCATTERED) {
          newSizeClass = SizeClass.CLUMPED;
        }
        else if (sizeClass == SizeClass.CLUMPED) {
          newSizeClass = SizeClass.OPEN_LOW_SHRUB;
        }
        else if (sizeClass == SizeClass.OPEN_TALL_SHRUB) {
          newSizeClass = SizeClass.OPEN_MID_SHRUB;
        }
        else if (sizeClass == SizeClass.OPEN_MID_SHRUB) {
          newSizeClass = SizeClass.OPEN_LOW_SHRUB;
        }
        else if (sizeClass == SizeClass.OPEN_LOW_SHRUB) {
          newSizeClass = SizeClass.CLOSED_TALL_SHRUB;
        }
        else if (sizeClass == SizeClass.CLOSED_TALL_SHRUB) {
          newSizeClass = SizeClass.CLOSED_MID_SHRUB;
        }
        else if (sizeClass == SizeClass.CLOSED_MID_SHRUB) {
          newSizeClass = SizeClass.CLOSED_LOW_SHRUB;
        }
        else {
          newSizeClass = null;
        }
      }
      else {
        if (sizeClass == SizeClass.VERY_LARGE) {
          newSizeClass = SizeClass.LARGE;
        }
        else if (sizeClass == SizeClass.LARGE) {
          newSizeClass = SizeClass.MEDIUM;
        }
        else if (sizeClass == SizeClass.MEDIUM) {
          newSizeClass = SizeClass.POLE;
        }
//        else if (sizeClass == SizeClass.MU) {
//          newSizeClass = SizeClass.TS;
//        }
        else {
          newSizeClass = null;
        }
      }
      if (newSizeClass != null) {
        newState = htGrp.getVegetativeType(newSpecies,newSizeClass,age,density);
      }
    }
    if (newState != null) {
      setState(newState);
    }
  }

  public float getReburnProbability() {

    return 0.0f;
  }

  public void fixEmptyData() {
    if (htGrp == null) {
      setHabitatTypeGroup(new HabitatTypeGroup("UNKNOWN"));
    }
    if (initialState == null) {
      setState(VegetativeType.UNKNOWN);
    }
  }

  /**
   * This method has been eliminated.  The call though remains untill I can make sure it does not cascade any ill effects
   * Brian Losi 10/28/13
   * release some memory that's no longer needed. method deprecated
   */
  public void cleanup() {
  }

  public void exportNeighbors(PrintWriter fout) {
    // unit, adj, elev, downwind
    for (int i=0; i<adjacentData.length; i++) {
      fout.print(getId());
      fout.print(COMMA);
      fout.print(adjacentData[i].getEvu().getId());
      fout.print(COMMA);
      if (isElevationValid()) {
        fout.print(getElevation());
      }
      else {
        fout.print(adjacentData[i].getPosition());
      }
      fout.print(COMMA);
      fout.print(adjacentData[i].getWind());
      fout.println();
    }
  }
  /**
   * Method to export attributes of Evu.  These will be output in following order:
   *   *slink, **row#, **col#, *unit#, *acres, *htgrp, #,
   * num lives,[species,size,density,process,process timesteps,
   * treatment,treatment timesteps,
   * num track species, [species, species percent]+]+

   * #,ownership, road status, ignition prob,
   * fmz, special area, landtype, initial process, initial Treatment
   * @param fout the print writer used
   */
  public void exportAttributes(PrintWriter fout) {

    NumberFormat nf = NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    nf.setMaximumFractionDigits(2);

    fout.print(getId());
    fout.print(COMMA);
    int x = getLocationX();
    int y = getLocationY();
    if (x != -1 && y != -1) {
      fout.print(x);
      fout.print(COMMA);
      fout.print(y);
      fout.print(COMMA);
    }
    fout.print(getUnitNumber());
    fout.print(COMMA);
    fout.print(nf.format(Area.getFloatAcres(getAcres())));
    fout.print(COMMA);
    fout.print(getHabitatTypeGroup());
    fout.print(COMMA);
    fout.print("#");

    Set<Lifeform> lives = getLifeforms(Season.YEAR);

    fout.print(COMMA);
    fout.print(lives.size());
    for (Lifeform life : lives) {
      VegSimStateData state = getState(life);
      if (state == null) { continue; }
      fout.print(COMMA);
      fout.print(state.getVegType().getSpecies());
      fout.print(COMMA);
      fout.print(state.getVegType().getSizeClass());
      fout.print(COMMA);
      fout.print(state.getVegType().getDensity());

      fout.print(COMMA);
      fout.print(state.getProcess());

      // Process Time steps ago.
      fout.print(COMMA);
      fout.print(QUESTION_MARK);

      // Initial Treatment
      fout.print(COMMA);
      Treatment treatment = getTreatment(0,false);
      if (treatment != null) {
        fout.print(treatment.getType());
      }
      else {
        fout.print(QUESTION_MARK);
      }

      // Treatment time steps in past.
      fout.print(COMMA);
      fout.print(QUESTION_MARK);


      Flat3Map trkSpecies = state.getTrackingSpeciesMap();
      fout.print(COMMA);
      if (trkSpecies == null) {
        fout.print(QUESTION_MARK);
        continue;
      }

      fout.print(trkSpecies.size());
      MapIterator it = trkSpecies.mapIterator();
      while (it.hasNext()) {
        InclusionRuleSpecies sp = (InclusionRuleSpecies)it.next();
        Float pctObj = (Float)it.getValue();
        float pct = (pctObj != null) ? pctObj : 0;

        fout.print(COMMA);
        fout.print(sp);
        fout.print(COMMA);
        fout.print(pct);
      }

    }

    fout.print(COMMA);
    fout.print("#");

    fout.print(COMMA);
    fout.print(getOwnership());
    fout.print(COMMA);
    fout.print(getRoadStatusNew().getSaveName());
    fout.print(COMMA);

    fout.print(QUESTION_MARK);
    fout.print(COMMA);
    fout.print(getFmz());
    fout.print(COMMA);
    fout.print(getSpecialArea());
    fout.print(COMMA);
    if (getLandtype() == null) { fout.print(QUESTION_MARK); }
    else { fout.print(getLandtype()); }
    fout.print(COMMA);

    if (Double.isNaN(longitude)) {
      fout.print(QUESTION_MARK);
    }
    else {
      fout.print(longitude);
    }

    fout.print(COMMA);
    if (Double.isNaN(latitude)) {
      fout.print(QUESTION_MARK);
    }
    else {
      fout.print(latitude);
    }
    fout.println();
  }
/**
 * Method to compare acreage of Evu's neighbors.
 * @return true if the neightbors have the same acreage
 */
  public boolean hasSameSizeNeighbors() {
    for (int i=0; i<adjacentData.length; i++) {
      if (acres != adjacentData[i].getEvu().acres) { return false; }
    }
    return true;
  }
  public String toString() {
    VegSimStateData state = getState();
    String stateStr = (state != null ? state.getVeg().toString() : "Unknown");
    return id + "-" + stateStr;
  }

  /**
   * Warning:  This is only valid if the polygon is square.
   * @return the length of one side of the polygon
   */
  public int getSideLength() {
    float fAcres = this.getFloatAcres();
    return (int)Math.round(Math.sqrt(fAcres*43560));
  }

/**
 * Serialization methods
 * @param in
 * @param ts time step
 * @param run simulation run
 * @return
 * @throws IOException
 * @throws ClassNotFoundException
 */
  private Flat3Map readSimStateDataOld(ObjectInput in, int ts, int run)
    throws IOException, ClassNotFoundException
  {
    int size = in.readInt();

    Flat3Map map = new Flat3Map();

    for (int i=0; i<size; i++) {
      Lifeform lifeform = (Lifeform)in.readObject();

      if (dominantLifeform == null) {
        dominantLifeform = lifeform;
      }
      else {
        dominantLifeform = Lifeform.getMostDominant(dominantLifeform,lifeform);
      }

      int numStates = in.readInt();
      for (int s=0; s<numStates; s++) {
        VegSimStateData newState = (VegSimStateData)in.readObject();
        newState.setSlink(id);
        newState.setTimeStep(ts);
        newState.setRun(run);
        newState.setLifeform(lifeform);

        MultiKey key = LifeformSeasonKeys.getKey(lifeform,newState.getSeason());
        map.put(key,newState);
      }
    }
    return map;
  }
  private Flat3Map readSimStateData(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int size = in.readInt();

    Flat3Map map = new Flat3Map();

    for (int i=0; i<size; i++) {
      VegSimStateData newState = (VegSimStateData)in.readObject();

      MultiKey key = LifeformSeasonKeys.getKey(newState.getLifeform(),newState.getSeason());
      map.put(key,newState);
      if (dominantLifeform == null) {
        dominantLifeform = newState.getLifeform();
      }
      else {
        dominantLifeform = Lifeform.getMostDominant(dominantLifeform,
                                                     newState.getLifeform());
      }
    }

    return map;
  }
/**
 * Reads in from external source.  Attributes are stored in file in following order: habitat type group, Evu ID, ownership, road status, ignition probability,
 * fire management zone, special area, source, associated land type, location, and initial treatment.
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    {
      HabitatTypeGroupType groupType = (HabitatTypeGroupType)in.readObject();
      htGrp = HabitatTypeGroup.findInstance(groupType);
      if (htGrp == null) {
        htGrp = new HabitatTypeGroup(groupType.getName());
      }
    }

    if (version == 1) {
      setState((VegetativeType) in.readObject());
    }
    else if (version == 2 || version == 3) {
      if (initialState == null) {
        initialState = new Flat3Map();
      }
      VegetativeState vegState = (VegetativeState)in.readObject();

      VegSimStateData state = new VegSimStateData(getId(),0,0,vegState.getVegetativeType(),vegState.getProcess(),(short)vegState.getProcessProb());
      state.setLifeform(Lifeform.NA);
      setState(state,Season.YEAR);
    }
    else if (version == 4) {
      initialState = readSimStateDataOld(in, 0, 0);

    }
    else {
      initialState = readSimStateData(in);
    }

    unitNumber = (String)in.readObject();

    ownership          = (String)in.readObject();
    // Make sure Ownership List is updated with this ownership;
    @SuppressWarnings("unused")
    Ownership tmpOwn = Ownership.get(ownership,true);

    if (version > 7) {
      roadStatus = (Roads.Status)in.readObject();
    }
    else {
      roadStatus = Roads.Status.lookup(in.readInt());
    }

    ignitionProb       = in.readInt();

    String tmpFmz      = (String)in.readObject();
    fmz = Simpplle.getCurrentZone().getFmz(tmpFmz);
    if (fmz == null) {
      fmz = Simpplle.getCurrentZone().getDefaultFmz().duplicate(tmpFmz);
      Simpplle.getCurrentZone().addFmz(fmz);
    }
    specialArea        = (String)in.readObject();

    // Make sure Special Area list is updated
    @SuppressWarnings("unused")
    SpecialArea sp = SpecialArea.get(specialArea,true);

    source             = (String)in.readObject();
    associatedLandtype = (String)in.readObject();

    // Make sure Landtype list is updated.
    @SuppressWarnings("unused")
    Landtype landtype = Landtype.get(associatedLandtype,true);

    location           = (int[])in.readObject();

    if (version > 6) {
      Treatment treat = (Treatment)in.readObject();
      if (treat != null) {
        addTreatment(treat);
      }
    }

    // Timber Volume
    // Volume removals
    if (version == 1) {
      getState().setProcess((ProcessType) in.readObject());
    }
    /*
     * Special note:
     *   AdjacentData member had to be deserialized in a seperate
     *   method due to circular references causing problems.
     */
  }
  /**
   * Change x and y coordinates of a location in Evu
   */
  public void swayXandY() {
    int x = location[Y];
    int y = location[X];
    this.setLocationX(x);
    this.setLocationY(y);
  }

  public void readExternalSimData(ObjectInput in, int run) throws IOException, ClassNotFoundException {

    Species   species;
    SizeClass sizeClass;
    int       age;
    Density   density;

    int version = in.readInt();

    if (version == 1) {

    } else if (version == 2) {

    } else if (version == 3) {

    } else if (version == 4) {

      simData = new Flat3Map[in.readInt()];

      for (int i = 0; i < simData.length; i++) {
        simData[i] = readSimStateDataOld(in,i,run);
      }

      simData[0] = initialState;

    } else {

      simData = new Flat3Map[in.readInt()];

      for (int i = 0; i < simData.length; i++) {
        simData[i] = readSimStateData(in);
      }
    }

    treatment = (Vector)in.readObject();

    if (version == 1) {

      VegetativeType[][] tmpAccumState = (VegetativeType[][]) in.readObject();
      ProcessType[][] accumProcess = (ProcessType[][]) in.readObject();

      int numValidRuns = 0;
      if (tmpAccumState != null && tmpAccumState[0] != null) {
        for (int r = 1; r <= tmpAccumState.length; r++) {
          if (tmpAccumState[r - 1][0] != null) {
            numValidRuns++;
          }
        }
      }

    } else if (version == 2) {

      VegetativeState[][] tmpAccumState = (VegetativeState[][])in.readObject();

      int numValidRuns = 0;
      if (tmpAccumState != null && tmpAccumState[0] != null) {
        for (int r = 1; r <= tmpAccumState.length; r++) {
          if (tmpAccumState[r - 1][0] != null) {
            numValidRuns++;
          }
        }
      }

    } else if (version > 5) {
      Area area = Simpplle.getCurrentArea();
      nearestRoad = null;
      nearestTrail = null;

      {
        int size = in.readInt();
        if (size > 0) {
          nearestRoad = new ArrayList<ArrayList<RoadUnitData>>();
          for (int i = 0; i < size; i++) {
            int sizeIn = in.readInt();
            ArrayList<RoadUnitData> list = new ArrayList<RoadUnitData>();
            nearestRoad.add(list);
            for (int j = 0; j < sizeIn; j++) {
              RoadUnitData data = new RoadUnitData();
              data.evu = null;
              data.road = null;

              int roadId = in.readInt();
              if (roadId != -1) {
                data.road = area.getRoadUnit(roadId);
              }

              int evuId = in.readInt();
              if (evuId != -1) {
                data.evu = area.getEvu(evuId);
              }

              list.add(data);
            }
          }
        }
      }

      {
        int size = in.readInt();
        if (size > 0) {
          nearestTrail = new ArrayList<ArrayList<TrailUnitData>>();
          for (int i = 0; i < size; i++) {
            int sizeIn = in.readInt();
            ArrayList<TrailUnitData> list = new ArrayList<TrailUnitData>();
            nearestTrail.add(list);
            for (int j = 0; j < sizeIn; j++) {
              TrailUnitData data = new TrailUnitData();
              data.evu = null;
              data.trail = null;

              int trailId = in.readInt();
              if (trailId != -1) {
                data.trail = area.getTrailUnit(trailId);
              }

              int evuId = in.readInt();
              if (evuId != -1) {
                data.evu = area.getEvu(evuId);
              }

              list.add(data);
            }
          }
        }
      }
    }
  }

  private void writeSimStateData(ObjectOutput out, Flat3Map map)
    throws IOException
  {
    out.writeInt(map.size());
    MapIterator it = map.mapIterator();
    while (it.hasNext()) {
      MultiKey key = (MultiKey)it.next();  // getValue requires this line.
      out.writeObject(it.getValue());
    }
  }

/**
 * Writes to an external source attributes in following order: habitat type group, EVU ID, ownership, road status, ignizion probability,
 * fire management zone, special area, source, associated land type, location, and initial treatment.
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    VegetativeType.setLimitedSerialization();

    out.writeInt(version);
    super.writeExternal(out);

    out.writeObject(htGrp.getType());

    writeSimStateData(out,initialState);
    out.writeObject(unitNumber);
    out.writeObject(ownership);
    out.writeObject(roadStatus);
    out.writeInt(ignitionProb);
    out.writeObject(fmz.getName());
    out.writeObject(specialArea);
    out.writeObject(source);
    out.writeObject(associatedLandtype);
    out.writeObject(location);

    out.writeObject(getInitialTreatment());

    VegetativeType.clearLimitedSerialization();
    /*
     * Special note:
     *   AdjacentData member had to be serialized in a seperate
     *   method due to circular references causing problems.
     */
  }
/**
 * Reads and sets the spatial relations for an Evu.  This will make associated land units, associated aquatic units, associated road units,
 * and associated trail units for this Evu.
 * @param in the input objects
 * @param area
 * @param areaVersion
 * @throws IOException
 * @throws ClassNotFoundException
 */
  public void readSpatialRelations(ObjectInput in, Area area, int areaVersion) throws IOException, ClassNotFoundException {
    assocLandUnits = null;
    assocAquaticUnits = null;

    int version = 0;
    if (areaVersion >= 5) {
      version = in.readInt();
    }

    {
      int size = in.readInt();
      if (size > 0) {
        assocLandUnits = new ArrayList<ExistingLandUnit>(size);
        for (int i = 0; i < size; i++) {
          assocLandUnits.add(area.getElu(in.readInt()));
        }
      }
    }

    {
      int size = in.readInt();
      if (size > 0) {
        assocAquaticUnits = new ArrayList<ExistingAquaticUnit>(size);
        for (int i = 0; i < size; i++) {
          assocAquaticUnits.add(area.getEau(in.readInt()));
        }
      }
    }

    if (version > 0) {
      {
        int size = in.readInt();
        if (size > 0) {
          assocRoadUnits = new ArrayList<Roads>(size);
          for (int i = 0; i < size; i++) {
            assocRoadUnits.add(area.getRoadUnit(in.readInt()));
          }
        }
      }

      {
        int size = in.readInt();
        if (size > 0) {
          assocTrailUnits = new ArrayList<Trails>(size);
          for (int i = 0; i < size; i++) {
            assocTrailUnits.add(area.getTrailUnit(in.readInt()));
          }
        }
      }
    }

  }
  /**
   * Method to output the spatial relations.  These are stored in file as associated Elu ID's, Eau ID's, road ID, Trail, Id
   * @param out
   * @throws IOException
   */
  public void writeSpatialRelations(ObjectOutput out) throws IOException {
    out.writeInt(spatialRelationsVersion);

    out.writeInt((assocLandUnits != null) ? assocLandUnits.size() : 0);
    if (assocLandUnits != null) {
      for (ExistingLandUnit elu : assocLandUnits) {
        out.writeInt(elu.getId());
      }
    }

    out.writeInt((assocAquaticUnits != null) ? assocAquaticUnits.size() : 0);
    if (assocAquaticUnits != null) {
      for (ExistingAquaticUnit eau : assocAquaticUnits) {
        out.writeInt(eau.getId());
      }
    }

    out.writeInt((assocRoadUnits != null) ? assocRoadUnits.size() : 0);
    if (assocRoadUnits != null) {
      for (Roads road : assocRoadUnits) {
        out.writeInt(road.getId());
      }
    }

    out.writeInt((assocTrailUnits != null) ? assocTrailUnits.size() : 0);
    if (assocTrailUnits != null) {
      for (Trails trail : assocTrailUnits) {
        out.writeInt(trail.getId());
      }
    }
  }
  public void writeExternalSimData(ObjectOutput out) throws IOException {
    VegetativeType.setLimitedSerialization();

    out.writeInt(version);

    out.writeInt(simData.length);
    for (int i=0; i<simData.length; i++) {
        writeSimStateData(out, simData[i]);
    }
    out.writeObject(treatment);

    {
      int size = (nearestRoad != null ? nearestRoad.size() : 0);
      out.writeInt(size);
      for (int i = 0; i < size; i++) {
        ArrayList<RoadUnitData> list = nearestRoad.get(i);
        int sizeIn = (list != null ? list.size() : 0);
        out.writeInt(sizeIn);
        for (int j = 0; j < sizeIn; j++) {
          RoadUnitData data = list.get(j);
          int roadId = (data.road != null ? data.road.getId() : -1);
          out.writeInt(roadId);

          int evuId = (data.evu != null ? data.evu.getId() : -1);
          out.writeInt(evuId);
        }
      }
    }

    {
      int size = (nearestTrail != null ? nearestTrail.size() : 0);
      out.writeInt(size);
      for (int i = 0; i < size; i++) {
        ArrayList<TrailUnitData> list = nearestTrail.get(i);
        int sizeIn = (list != null ? list.size() : 0);
        out.writeInt(sizeIn);
        for (int j = 0; j < sizeIn; j++) {
          TrailUnitData data = list.get(j);
          int trailId = (data.trail != null ? data.trail.getId() : -1);
          out.writeInt(trailId);

          int evuId = (data.evu != null ? data.evu.getId() : -1);
          out.writeInt(evuId);
        }
      }
    }

    VegetativeType.clearLimitedSerialization();
  }

  /**
   * In these read/write methods for AdjacentData I have written out
   * the arrays explicitily to make things faster.  Although writing out the
   * actual array worked it nearly tripled the time to read/write the file.
   * In addition I am writing the Evu.id instead of the actual instance because
   * I suspect that was what was causing the delay.
   */
  public void readExternalAdjacentData(ObjectInput in, Area area) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    adjacentData = new AdjacentData[in.readInt()];
    for (int i=0; i<adjacentData.length; i++) {
      adjacentData[i] = new AdjacentData();
      adjacentData[i].setEvu(area.getEvu(in.readInt()));
      adjacentData[i].setPosition(in.readChar());
      adjacentData[i].setWind(in.readChar());
    }
  }
  public void writeExternalAdjacentData(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeInt(adjacentData.length);
    for (int i=0; i<adjacentData.length; i++) {
      out.writeInt(adjacentData[i].getEvu().getId());
      out.writeChar(adjacentData[i].getPosition());
      out.writeChar(adjacentData[i].getWind());
    }
  }


  /**
    * This Method has been eliminated, but the method shell remains till I can make sure it is not called anywhere.
  * Edit - Brian Losi 10/28/13
   * @param session
   * @throws HibernateException
   * @throws SQLException
   */
  public void writeAccumDatabase(Session session) throws HibernateException, SQLException {

  }
/**
 * This Method has been eliminated, but the method shell remains till I can make sure it is not called anywhere.
 * Edit - Brian Losi 10/28/13
 * @param simFile
 * @throws SimpplleError
 */
  public void writeRandomAccessFile(RandomAccessFile simFile) throws SimpplleError
  {
  }
/**
 * Uses hibernate session to write siumlation database of vegetative state.
 * @param session
 * @throws SimpplleError
 */
  public void writeSimulationDatabase(Session session) throws SimpplleError {
    int ts  = Simulation.getCurrentTimeStep();

    Lifeform[] lives = Lifeform.getAllValues();
    Season[]   seasons = Climate.allSeasons;

    VegSimStateData.clearWriteCount();
    for (int l=0; l<lives.length; l++) {
      for (int i = 0; i < seasons.length; i++) {
        VegSimStateData state = getState(ts, lives[l], seasons[i]);
        if (state == null) { continue; }

        VegSimStateData.writeDatabase(session,state);
      }
    }
  }
  /**
   * This method writes to access database.  Most likely this will go away with OpenSimpplle 1.0
   * @param fout
   * @param trackOut
   */
  public void writeSimulationAccessFiles(PrintWriter fout, PrintWriter trackOut) {
    int ts  = Simulation.getCurrentTimeStep();

    Lifeform[] lives = Lifeform.getAllValues();
    Season[]   seasons = Climate.allSeasons;

    VegSimStateData.clearWriteCount();
    for (int l=0; l<lives.length; l++) {
      for (int i = 0; i < seasons.length; i++) {
        VegSimStateData state = getState(ts, lives[l], seasons[i]);
        if (state == null) { continue; }

        VegSimStateData.writeAccessFiles(fout,trackOut,this,state);
      }
    }
  }
/**
 * Uses the season to get a set of lifeforms.
 * @return
 */
  public Set<Lifeform> getLifeforms() {
    return getLifeforms(Simulation.getInstance().getCurrentSeason());
  }
  public Set<Lifeform> getLifeforms(Season season) {
    int ts=0;
    if (simData != null && Simpplle.getCurrentSimulation().isSimulationRunning()) {
      ts = Simulation.getCurrentTimeStep();
    }
    return getLifeforms(ts,season);
  }

  /**
   * Uses a specific time step and season to make a set of  lifeforms.
   * @param tStep time step used to get lifeforms
   * @param season season used to get lifeforms
   * @return a set of lifeforms at a particular time step and season
   */

    public Set<Lifeform> getLifeforms(int tStep, Season season) {
        lifeformSet.clear();

        Flat3Map map;
        if (tStep > 0 && simData != null) {
            int index = getSimDataIndex(tStep);
            if (index < 0) {
                throw new RuntimeException("Attempted access to unavailable time step");
            }
            if (index >= 0) {
                map = simData[index];
            }
            else {
                return getLifeformsDatabase(tStep, season);
            }
        }
        else {
            map = initialState;
        }

        MapIterator it = map.mapIterator();
        while (it.hasNext()) {
            MultiKey key = (MultiKey)it.next();
            Lifeform lifeform    = (Lifeform)key.getKey(0);

            if ((Season)key.getKey(1) != season) { continue; }

            if (lifeformSet.contains(lifeform) == false) {
                lifeformSet.add(lifeform);
            }
        }

        return lifeformSet;
    }
  public ArrayList<Lifeform> getLifeformsList(int tStep, Season season) {
    lifeformList.clear();

    Flat3Map map;
    if (tStep > 0 && simData != null) {
      int index = getSimDataIndex(tStep);
      if (index < 0) {
        throw new RuntimeException("Attempted access to unavailable time step");
      }
      if (index >= 0) {
        map = simData[index];
      }
      else {
        return getLifeformsListDatabase(tStep, season);
      }
    }
    else {
      map = initialState;
    }

    MapIterator it = map.mapIterator();
    while (it.hasNext()) {
      MultiKey key = (MultiKey)it.next();
      Lifeform lifeform    = (Lifeform)key.getKey(0);

      if ((Season)key.getKey(1) != season) { continue; }

      if (lifeformList.contains(lifeform) == false) {
        lifeformList.add(lifeform);
      }
    }

    return lifeformList;
  }

/**
 * Creates an arraylist of lifeforms.
 * @param tStep
 * @param season
 * @return
 */
  public ArrayList<Lifeform> getSortedLifeforms(int tStep, Season season) {
    ArrayList<Lifeform> result = new ArrayList<Lifeform>();

    Set<Lifeform> lives = getLifeforms(tStep,season);
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      if (lives.contains(allLives[i])) {
        result.add(allLives[i]);
      }
    }
    return result;
  }
  /**
   * Uses hibernate to create a set of lifeforms from database at a particular time steps and season.
   * @param tStep time step used to find veg state to get lifeforms
   * @param season used to find veg state and get lifeforms.
   * @return
   */

  public Set<Lifeform> getLifeformsDatabase(int tStep, Season season) {
    lifeformSet.clear();

    StringBuffer strBuf = new StringBuffer();
    strBuf.append("from VegSimStateData as state where");
    strBuf.append(" state.slink=");
    strBuf.append(getId());
    strBuf.append(" and state.seasonOrd=");
    strBuf.append(season.ordinal());
    strBuf.append(" and state.timeStep=");
    strBuf.append(tStep);
    strBuf.append(" and state.run=");
    strBuf.append(Simulation.getInstance().getCurrentRun());

    Session     session = DatabaseCreator.getSessionFactory().openSession();
    Query q = session.createQuery(strBuf.toString());
    strBuf = null;
    List totList = q.list();

    session.close();

    if (totList == null || totList.size() == 0) {
      return null;
    }

    for (int i=0; i<totList.size(); i++) {
      VegSimStateData state = (VegSimStateData)totList.get(i);
      if (lifeformSet.contains(state.getLifeform()) == false) {
        lifeformSet.add(state.getLifeform());
      }
    }
    session.close();

    return lifeformSet;
  }
  /**
   * Uses hibernate to get the lifeforms list from database based on a specified time step and season.
   * @param tStep time step
   * @param season season used to find lifeform
   * @return arraylist of lifeforms
   */
  public ArrayList<Lifeform> getLifeformsListDatabase(int tStep, Season season) {
    lifeformList.clear();

    StringBuffer strBuf = new StringBuffer();
    strBuf.append("from VegSimStateData as state where");
    strBuf.append(" state.slink=");
    strBuf.append(getId());
    strBuf.append(" and state.seasonOrd=");
    strBuf.append(season.ordinal());
    strBuf.append(" and state.timeStep=");
    strBuf.append(tStep);
    strBuf.append(" and state.run=");
    strBuf.append(Simulation.getInstance().getCurrentRun());

    Session     session = DatabaseCreator.getSessionFactory().openSession();
    Query q = session.createQuery(strBuf.toString());
    strBuf = null;
    List totList = q.list();

    session.close();

    if (totList == null || totList.size() == 0) {
      return null;
    }

    for (int i=0; i<totList.size(); i++) {
      VegSimStateData state = (VegSimStateData)totList.get(i);
      if (lifeformSet.contains(state.getLifeform()) == false) {
        lifeformList.add(state.getLifeform());
      }
    }
    session.close();

    return lifeformList;
  }
/**
 * Checks if there are multiple lifeforms in an Evu by seeing if the lifeform set size is greater than 1.
 * @return
 */
  public boolean hasMultipleLifeforms() {
    // initial state doesn't have multiple seasons, therefore use YEAR.
    Set <Lifeform> lives = getLifeforms(0,Season.YEAR);

    if (lives.size() > 1) { return true; }

    if (lives.size() == 1) {
      return (lives.contains(Lifeform.NA) == false);
    }

    return false;
  }
/**
 * Uses the current simulation time step to get current veg state, which is then used to get previous veg state.
 * If previous state exists, gets its density and then subtracsts from current vegetative density.
 * @param lifeform
 * @return An int representing the diffrence in current minus previous canopy density, or just the current density if no previous states exist.
 */
  public int getCanopyChange(Lifeform lifeform) {
    int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    VegetativeType currentVeg = getState(lifeform).getVegType();

    VegSimStateData prevState = getState(cStep-1,lifeform);
    int prevVegCanopy = (prevState != null ? prevState.getVegType().getDensity().getValue() : 0);

    return currentVeg.getDensity().getValue() - prevVegCanopy;
  }
/**
 * This method has been predominiantly eliminated.  I kept the main call name until I can make sure there are no ill side effects.
 * Brian Losi 10/27/13
 */
  public void doCompetition() {

  }
/**
 * Checks if the Evu has a veg state with invasive species by looping through the life forms and passing to
 * overloaded hasInvasiveState().
 * @return true if veg state has invasive species
 */
  public boolean hasInvasiveState() {
    for (Lifeform lifeform : getLifeforms()) {
      if (hasInvasiveState(lifeform)) { return true; }
    }
    return false;
  }
  /**
   * Checks if the Evu veg state has an invasive species.
   * @param lifeform
   * @return
   */
  public boolean hasInvasiveState(Lifeform lifeform) {
    VegSimStateData state = getState(lifeform);
    if (state == null) { return false; }

    Species species = state.getVeg().getSpecies();
    if (species != null && species.isInvasive()) {
      return true;
    }
    return false;
  }
/**
 * Checks if any of the Evu's lifeforms are invasive species.
 * @return
 */
  public boolean hasInvasive() {
    for (Lifeform lifeform : getLifeforms()) {
      if (hasInvasive(lifeform)) { return true; }
    }
    return false;
  }
  /**
   * Checks if a specific life form is an instance of invasive species within the Evu's.
   * @param lifeform
   * @return
   */
  public boolean hasInvasive(Lifeform lifeform) {
    return getInvasiveTrackingSpecies(lifeform) != null;
  }
/**
 * Gets the inclusion rule array list with invasive tracking species.  Uses the lifeform to get veg state
 * then gets the name of any species.  If that species is invasive and adds to inclusion rule species array list
 * @param lifeform
 * @return the inclusion rule species representing the invasive species to be tracked.
 */
  public ArrayList<InclusionRuleSpecies> getInvasiveTrackingSpecies(Lifeform lifeform) {
    ArrayList<InclusionRuleSpecies> result=null;
    VegSimStateData state = getState(lifeform);
    if (state == null) { return null; }

    Flat3Map trkSpecies = state.getTrackingSpeciesMap();
    if (trkSpecies == null) { return null; }


    MapIterator it = trkSpecies.mapIterator();
    while (it.hasNext()) {
      InclusionRuleSpecies sp = (InclusionRuleSpecies)it.next();

      Species species = Species.get(sp.getName());
      if (species != null && species.isInvasive()) {
        if (result == null) { result = new ArrayList<InclusionRuleSpecies>(); }
        result.add(sp);
      }
    }
    if (result == null) { return null; }

    return result;
  }
/**
 * If there is an inclustion rule species gets it and assigns to temporary species, then goes through the species
 * and gets the vegetative state via the lifeform passed and updates the tracking species to the temporary species and the change int.
 * @param change
 * @param lifeform
 */
  public void updateInvasiveTrackSpecies(int change, Lifeform lifeform) {
    ArrayList<InclusionRuleSpecies> spList = getInvasiveTrackingSpecies(lifeform);
    if (spList == null) { return; }

    for (int i=0; i<spList.size(); i++) {
      InclusionRuleSpecies trackSpecies = spList.get(i);
      if (trackSpecies != null) {
        getState(lifeform).updateTrackingSpecies(trackSpecies, change);
      }

    }

  }
/**
 * Gets the tracking species percentage.
 * @param lifeform
 * @param trackSpecies
 * @return
 */
  public float getTrackingSpeciesPercent(Lifeform lifeform, InclusionRuleSpecies trackSpecies) {
    if (trackSpecies != null) {
      return getState(lifeform).getSpeciesPercent(trackSpecies);
    }
    return 0;
  }

  private static ArrayList<ProcessType> doneSummaryProcesses = new ArrayList<ProcessType>();


  // Used as the retrun value of the following method.
  // The caller of the method only uses the contents of the array not the array
  // itself.  Once finished the array is not needed so to avoid creating millions
  // of temporary arrays this is going to be created once and cleared each time.
  private static ArrayList<ProcessType> tmpSummaryProcesses = new ArrayList<ProcessType>();
/**
 * Creates an arraylist of the processes present in an Evu.
 * @param cStep current time step
 * @return arraylist of processes in Evu.
 */
  public ArrayList<ProcessType> getSummaryProcesses(int cStep) {
    tmpSummaryProcesses.clear();

    for (Climate.Season s : Climate.allSeasons) {
      if (cStep == 0 && s != Season.YEAR) { continue; }

      VegSimStateData trees  = getState(cStep,Lifeform.TREES, s);
      VegSimStateData shrubs = getState(cStep,Lifeform.SHRUBS, s);
      VegSimStateData grass  = getState(cStep,Lifeform.HERBACIOUS, s);
      VegSimStateData agr    = getState(cStep,Lifeform.AGRICULTURE, s);
      VegSimStateData na     = getState(cStep,Lifeform.NA, s);

      ProcessType treeProcess  = (trees != null) ? trees.getProcess() : ProcessType.NONE;
      ProcessType shrubProcess = (shrubs != null) ? shrubs.getProcess() : ProcessType.NONE;
      ProcessType grassProcess = (grass != null) ? grass.getProcess() : ProcessType.NONE;
      ProcessType agrProcess   = (agr != null) ? agr.getProcess() : ProcessType.NONE;
      ProcessType naProcess    = (na != null) ? na.getProcess() : ProcessType.NONE;

      int fireCount = 0;
      if (treeProcess.isFireProcess()) { fireCount++; }
      if (shrubProcess.isFireProcess()) { fireCount++; }
      if (grassProcess.isFireProcess()) { fireCount++; }

      if (Simpplle.getCurrentArea().multipleLifeformsEnabled() == false) {
        if (naProcess.isFireProcess()) { fireCount++; }
      }

      doneSummaryProcesses.clear();
      if (fireCount > 1)
      {
        ProcessType unitProcess =
          Process.determineUnitFireProcess(treeProcess,shrubProcess,grassProcess);

        if (unitProcess != null && tmpSummaryProcesses.contains(unitProcess) == false) {
          tmpSummaryProcesses.add(unitProcess);
        }
      }

      Lifeform[] lives = Lifeform.getAllValues();
      for(int i=0; i<lives.length; i++) {
        ProcessType process = ProcessType.NONE;
        if (lives[i] == Lifeform.TREES) { process = treeProcess; }
        else if (lives[i] == Lifeform.SHRUBS) { process = shrubProcess; }
        else if (lives[i] == Lifeform.HERBACIOUS) { process = grassProcess; }
        else if (lives[i] == Lifeform.AGRICULTURE) { process = agrProcess; }
        else if (lives[i] == Lifeform.NA) { process = naProcess; }

        if ((process.isFireProcess() == false || fireCount == 1) &&
            process != ProcessType.SUCCESSION &&
            process != ProcessType.NONE) {
          if (doneSummaryProcesses.contains(process) == false) {
            tmpSummaryProcesses.add(process);
            doneSummaryProcesses.add(process);
          }
        }
      }


    }
    return tmpSummaryProcesses;
  }

  private boolean doGapProcesses(Lifeform lifeform) {
    Area.currentLifeform = lifeform;

    MultiKeyMap gappedProcesses = Simpplle.getAreaSummary().getGappedProcesses();
    ProcessType gappedProcess = getState(lifeform).getProcess();
    boolean result = GapProcessLogic.getInstance().doLogic(this,lifeform);
    if (result) {
      int run = Simulation.getCurrentRun();
      int ts = Simulation.getCurrentTimeStep();
      gappedProcesses.put(this,lifeform,run,ts,gappedProcess);
    }

    Area.currentLifeform = null;

    return result;
  }
/**
 * Calculates time since last fire by getting the current simulation time step, looping backward through time steps and then
 * through the simulation's lifeforms and finding any instances of SRF, MSF, or LSF.  returns the current time minus
 * the time step where fire was found.
 *
 * @return the differnce in time steps between current time step and last instance of fire process.
 */
  private int timeSinceFire() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int cTime = simulation.getCurrentTimeStep();

    Lifeform[] allLives = Lifeform.getAllValues();
    for(int ts=cTime; ts>=0; ts--) {
      int simDataIndex = this.getSimDataIndex(ts);
      if (simDataIndex < 0) {
        return lastFireTimeStep;
      }
      for (int i=0; i<allLives.length; i++) {
        if ((hasProcess(ts, allLives[i], ProcessType.SRF)) ||
            (hasProcess(ts, allLives[i], ProcessType.MSF)) ||
            (hasProcess(ts, allLives[i], ProcessType.LSF)))
        {
          return (cTime - ts);
        }
      }
    }

    return -1;
  }
/**
 * Finds last life form by looping through simulation using current time steps minus the past time steps in memory and returns
 * the state when the lifeform was found.
 * @param lifeform the lifeform
 * @return
 */
  public VegSimStateData findLastLifeformState(Lifeform lifeform) {
    if (Simulation.getInstance().isDiscardData()) {
      // This line was at times trying to go to far back to data no longer present
      // so added code to make sure that does not happen.
      // This led to calculate producing seed to try and access an time step
      // that is not available.
      VegSimStateData state = findLastLifeformStateDatabase(lifeform);
      int firstAvailStep = Simulation.getCurrentTimeStep() - Simulation.getInstance().getPastTimeStepsInMemory() + 1;
      if (firstAvailStep < 0) { firstAvailStep = 0; }

      if (state != null && state.getTimeStep() >= firstAvailStep) {
        return state;
      }
      else {
        return null;
      }
    }

    int cTime = Simulation.getCurrentTimeStep();

    for(int ts=cTime-1; ts>=0; ts--) {
      if (hasLifeform(lifeform,ts)) {
        return getState(ts,lifeform);
      }
    }
    return null;
  }
/**
 * Increments backwards from current time step to find the last veg state with the specified life form.
 * @param life
 * @return
 */
  private VegSimStateData findLastLifeformStateDatabase(Lifeform life) {
    int cTime = Simulation.getCurrentTimeStep();

    for(int ts=cTime-1; ts>=0; ts--) {
      int index = this.getSimDataIndex(ts);
      if (index < 0) {
        VegSimStateData state = lastLife[life.getId()];
        if (state == null) { return null; }
        return state;
      }

      if (index >= 0 && hasLifeform(life,ts)) {
        return getState(ts,life);
      }
    }

    return null;

  }
/**
 * Passes to overloaded suppressFire() with true for is Class A logic.
 */
  public void suppressFire() {
    suppressFire(true);
  }
  /**
   * Updates the life forms and process type to succession and curent probability to suppression.
   * Uses the life form to get the vegetative states, then checks if this Evu has the life form, whether the veg state
   * has a fire, and then updates life form process type to succession and current probability of Evu to suppression.
   * Then updates the fire suppression in in the area if class A.
   * @param isClassA true if uses class A logic
   */
  public void suppressFire(boolean isClassA) {
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      Lifeform lifeform = allLives[i];
      VegSimStateData state = getState(lifeform);
      if (state == null) { continue; }
      if (hasLifeform(lifeform) &&
          (state.getProcess().isFireProcess() ||
           state.getProcess() == ProcessType.FIRE_EVENT)) {
        updateCurrentProcess(lifeform,ProcessType.SUCCESSION);
        updateCurrentProb(lifeform,Evu.SUPP);

      }
    }
    if (isClassA) {
      Simpplle.getAreaSummary().updateSuppressedFires(this);
    }
  }
/**
 * Uses the life forms to get vegetative state, then checks if Evu has life form and whether the vegetative state probability
 * equals suppression.
 * @return null if state is null, true if probability = suppression
 */
  public boolean isSuppressed() {
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      Lifeform lifeform = allLives[i];
      VegSimStateData state = getState(lifeform);
      if (state == null) { continue; }
      if (hasLifeform(lifeform) && state.getProb() == SUPP) {
        return true;
      }
    }
    return false;
  }

  /**
   * Used by doGetProcessLifeform to determine if a higher lifeform has
   * suppressed a FireEvent.  If a FireEvent has been suppressed it is
   * suppressed for the whole unit not just the lifeform.
   */
  public boolean isSuppressed(Lifeform life) {
    ArrayList<Lifeform> lives = Lifeform.getMoreDominant(life);
    for (int i=0; i<lives.size(); i++) {
      Lifeform lifeform = lives.get(i);
      VegSimStateData state = getState(lifeform);
      if (state == null) { continue; }
      if (hasLifeform(lifeform) && state.getProb() == SUPP) {
        return true;
      }
    }
    return false;
  }
/**
 * gets the last life form with a process occurring.
 * @param timeStepsBack
 * @param lifeform
 * @return
 */
  public ProcessType getLastLifePastProcess(int timeStepsBack, Lifeform lifeform) {
    ProcessType[] processes = (ProcessType[])lastLifeProcessHistory.get(lifeform);

    int index = processes.length - 1 - timeStepsBack;
    if (index < 0) {
      return ProcessType.NONE;
    }

    return processes[index];
  }
/**
 * Makes a single lifeform by getting the vegetative state  and seting the lifeform to NA
 */
  public void makeSingleLife() {
    VegSimStateData state = getState(0,dominantLifeform,Season.YEAR);

    state.setLifeform(Lifeform.NA);
    initialState.clear();

    dominantLifeform = Lifeform.NA;
    MultiKey key = LifeformSeasonKeys.getKey(Lifeform.NA,Season.YEAR);

    initialState.put(key,state);
  }

  public void makeMultipleLife() {
    VegSimStateData state = getState(0,dominantLifeform,Season.YEAR);

    dominantLifeform = state.getVeg().getSpecies().getLifeform();
    state.setLifeform(dominantLifeform);

    initialState.clear();

    MultiKey key = LifeformSeasonKeys.getKey(dominantLifeform,Season.YEAR);

    initialState.put(key,state);
  }
}





