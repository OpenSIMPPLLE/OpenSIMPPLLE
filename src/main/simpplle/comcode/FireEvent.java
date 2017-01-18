/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * This class defines Fire event, a type of Process.
 *
 * <p> Original source code authorship: Kirk A. Moeller
 */

public class FireEvent extends Process {

  private static final String printName = "FIRE-EVENT";

  private static int[]   fireSeasonData;
  private static boolean extremeDataChanged;

  public  static int extremeProb = 0;
  private static int extremeEventAcres = 1000;

  private static boolean useRegenPulse = false;
  private static boolean doRegenPulse  = false;

  public enum Position {ABOVE, BELOW, NEXT_TO }
  public static transient final Position ABOVE   = Position.ABOVE;
  public static transient final Position BELOW   = Position.BELOW;
  public static transient final Position NEXT_TO = Position.NEXT_TO;

  // Fire Type (for Spread)
  public static final int LMSF_CLASS = 0;  // Light or Mixed Severity Fire
  public static final int SRF_CLASS  = 1;

  // Relative Position
  public static final int A  = 0; // Above
  public static final int BN = 1; // Below or Next to

  // Density Grouping
  public static final int LOW_DENSITY  = 0;  // Density 1 or 2
  public static final int HIGH_DENSITY = 1;  // Density 3 or 4

  // Resistance.
  public static final int LOW      = 0;
  public static final int MODERATE = 1;
  public static final int HIGH     = 2;

  // Ownership
  public  static final int NF_WILDERNESS = 0;
  public  static final int NF_OTHER      = 1;
  public  static final int OTHER         = 2;
  private static final int NUM_OWNER     = 3;

  public static final String allOwnership[] = { "NF-WILDERNESS", "NF-OTHER", "OTHER" };

  // Structure
  // Must be in this order!
  public static final SizeClass.Structure NON_FOREST     = SizeClass.NON_FOREST;
  public static final SizeClass.Structure SINGLE_STORY   = SizeClass.SINGLE_STORY;
  public static final SizeClass.Structure MULTIPLE_STORY = SizeClass.MULTIPLE_STORY;

  public static ProcessOccurrenceSpreadingFire currentEvent = null;

  /**
   * Creates a non-spreading, non-yearly process.
   */
  public FireEvent () {

    spreading     = false;
    description   = "Fire Event";
    yearlyProcess = false;

  }

  // *************************
  // *** Extreme Fire Data ***
  // *************************

  /**
   * Returns the extreme fire probability, which is shared by all instances.
   */
  public static int getExtremeProb() {
    return extremeProb;
  }
 
  /**
   * Sets the extreme fire probability, and marks the data as changed if the value doesn't match the current value.
   * @param prob A probability in the range 0 - 100
   */
  public static void setExtremeProb(int prob) {

    if (extremeProb != prob) {
      markExtremeDataChanged();
    }

    extremeProb = prob;

  }

  public static int getExtremeEventAcres() {
    return extremeEventAcres;
  }

  public static void setExtremeEventAcres(int acres) {

    if (extremeEventAcres != acres) {
      markExtremeDataChanged();
    }

    extremeEventAcres = acres;

  }

  /**
   * @return True if the extreme event probability or acres have been modified.
   */
  public static boolean hasExtremeDataChanged() {
    return extremeDataChanged;
  }

  /**
   * Marks the extreme data as having been changed in this class and system knowledge.
   */
  private static void markExtremeDataChanged() {
    setExtremeDataChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.EXTREME_FIRE_DATA);
  }

  /**
   * Sets the extreme data changed flag.
   */
  public static void setExtremeDataChanged(boolean value) {
    extremeDataChanged = value;
  }

  public static void resetExtremeData() {
    extremeProb = 0;
    extremeEventAcres = 1000;
    setExtremeDataChanged(false);
  }

  /**
   * Reads a line containing two comma-delimited values; the probability of extreme fire and the number of extreme
   * event acres.
   * @param fin A buffered file reader
   * @throws ParseError Thrown when there is not enough data or the values are not integers
   */
  public static void readExtremeData(BufferedReader fin) throws ParseError {

    String line = null;

    try {

      line = fin.readLine();
      if (line == null) {
        throw new ParseError("No data found");
      }

      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() != 2) {
        throw new ParseError("Incorrect Number of fields");
      }

      int value = strTok.getIntToken();
      if (value == -1) { throw new ParseError("Invalid Probability found"); }
      extremeProb = value;

      value = strTok.getIntToken();
      if (value == -1) { throw new ParseError("Invalid Event Acres found"); }
      extremeEventAcres = value;

    } catch (ParseError err) {
      throw new ParseError("While reading Extreme Fire Data in line: " + line + err.msg);
    } catch (IOException err) {
      throw new ParseError("Error trying to read Extreme Fire Data file");
    }
  }

  /**
   * Writes the extreme probability, a comma, and the number of acres.
   * @param fout A print writer
   */
  public static void saveExtremeData(PrintWriter fout) {
    fout.println(getExtremeProb() + "," + getExtremeEventAcres());
  }

  // ************************
  // *** Fire Season Data ***
  // ************************

  /**
   * Sets the probability of fire in each season. These probabilities are shared by all fire event instances.
   */
  public static void setFireSeasonData(int spring, int summer, int fall, int winter) {

    fireSeasonData[Climate.Season.SPRING.ordinal()] = spring;
    fireSeasonData[Climate.Season.SUMMER.ordinal()] = summer;
    fireSeasonData[Climate.Season.FALL.ordinal()]   = fall;
    fireSeasonData[Climate.Season.WINTER.ordinal()] = winter;

    markFireSeasonDataChanged();

  }

  /**
   * Returns the probability of a fire season occurring in the given season.
   * @param season One of four seasons; spring, summer, fall, or winter
   * @return The probability that fire occurs in this season, or -1 if not a season (like YEAR)
   */
  public static int getFireSeasonData(Climate.Season season) {

    if (season != Climate.Season.SPRING &&
        season != Climate.Season.SUMMER &&
        season != Climate.Season.FALL &&
        season != Climate.Season.WINTER) {

      return -1;

    } else {

      return fireSeasonData[season.ordinal()];

    }
  }

  private static void markFireSeasonDataChanged() {
    SystemKnowledge.markChanged(SystemKnowledge.FIRE_SEASON);
  }

  /**
   * Reads fire season probabilities from a comma-separated value (CSV) file. The file contains a single row containing
   * an integer probability (0-100) for spring, summer, fall, and optionally winter. The probabilities must total 100%,
   * so if winter is not available, then its value is implied to equal 100 - (spring + summer + fall).
   * @param fin A file reader
   * @throws SimpplleError Thrown if the file is unable to be read or there is missing data
   */
  public static void readFireSeasonData(BufferedReader fin) throws SimpplleError {

    try {

      String line = fin.readLine();

      if (line == null) {
        throw new ParseError("Fire Season Data file is empty.");
      }

      fireSeasonData = new int[4];

      StringTokenizerPlus strTok = new StringTokenizerPlus(line,",");

      int count = strTok.countTokens();
      if (count != 3 && count != 4) {
        throw new ParseError("Invalid file! Incorrect number of items");
      }

      fireSeasonData[Climate.Season.SPRING.ordinal()] = strTok.getIntToken();
      fireSeasonData[Climate.Season.SUMMER.ordinal()] = strTok.getIntToken();
      fireSeasonData[Climate.Season.FALL.ordinal()]   = strTok.getIntToken();

      if (count == 3) {

        fireSeasonData[Climate.Season.WINTER.ordinal()] = 100 - ( fireSeasonData[Climate.Season.SPRING.ordinal()] +
                                                                  fireSeasonData[Climate.Season.SUMMER.ordinal()] +
                                                                  fireSeasonData[Climate.Season.FALL.ordinal()] );

      } else {

        fireSeasonData[Climate.Season.WINTER.ordinal()] = strTok.getIntToken();

      }

      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SEASON,false);

    } catch (ParseError pe) {
      throw new SimpplleError(pe.msg,pe);
    } catch (IOException e) {
      throw new SimpplleError("Problems reading from fire season data file.");
    } catch (Exception e) {
      e.printStackTrace();
      throw new SimpplleError("Invalid or missing data in Fire Season Data File.");
    }
  }

  /**
   * Writes a line of comma-delimited probabilities for spring, summer, fall, and winter respectively.
   * @param fout A file writer
   */
  public static void saveFireSeasonData(PrintWriter fout) {

    fout.println( fireSeasonData[Climate.Season.SPRING.ordinal()] + "," +
                  fireSeasonData[Climate.Season.SUMMER.ordinal()] + "," +
                  fireSeasonData[Climate.Season.FALL.ordinal()]   + "," +
                  fireSeasonData[Climate.Season.WINTER.ordinal()] );

    SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SEASON,false);

  }

  // ***************************
  // *** Probability Methods ***
  // ***************************

  /**
   * Computes the probability of a fire in an existing vegetation unit (EVU). The probability equals the number of
   * acres in the EVU * the number of fires per acre * 100 * 10 ^ probability precision.
   * @param zone A regional zone
   * @param evu An existing vegetation unit
   * @return A probability
   */
  public int doProbability (RegionalZone zone, Evu evu) {

    Fmz fmz = evu.getFmz();

    double prob = fmz.calculateProbability(Area.getFloatAcres(evu.getAcres()));

    // polygons with a large number of acres
    // can result in prob > 100 so set make
    // the max probability of fire 25%.

    if (prob > 25.0) prob = 25.0;

    if (Area.multipleLifeformsEnabled()) {
      Simulation.setProbPrecision(4);
    }

    int rationalProb = Simulation.getRationalProbability(prob);

    Simulation.setDefaultProbPrecision();

    return rationalProb;

  }

//  public static boolean isExtremeEvent(Evu evu) {
//    AreaSummary areaSummary = Simpplle.getAreaSummary();
//    int tmpAcres = Math.round(areaSummary.getEventAcres(evu));
//
//    return (tmpAcres > getExtremeEventAcres());
//  }

  /**
   * Returns true if the fire spread is classified as extreme. This is determined stochastically.
   */
  public static boolean isExtremeSpread() {

    int randNum = Simulation.getInstance().random() + 1;

    // Should not be possible, but just in case.

    if (randNum > 10000) {
      randNum = 10000;
    }

    int bound = 10000 - (extremeProb * 100);

    if (randNum > bound) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the fire season. This is determined stochastically.
   * @return A season; spring, summer, fall, or winter
   */
  public static Climate.Season getFireSeason() {

    int randNum = Simulation.getInstance().random();

    int spring = fireSeasonData[Climate.Season.SPRING.ordinal()] * 100;
    int summer = fireSeasonData[Climate.Season.SUMMER.ordinal()] * 100;
    int fall   = fireSeasonData[Climate.Season.FALL.ordinal()]   * 100;
    int winter = fireSeasonData[Climate.Season.WINTER.ordinal()] * 100;

    int springEnd = spring - 1;
    int summerEnd = spring + summer - 1;
    int fallEnd   = spring + summer + fall - 1;

    if ((spring > 0) && (randNum <= springEnd)) {
      return Climate.Season.SPRING;
    } else if ((summer > 0) && (randNum <= summerEnd)) {
      return Climate.Season.SUMMER;
    } else if ((fall > 0) && (randNum <= fallEnd)) {
      return Climate.Season.FALL;
    } else {
      return Climate.Season.WINTER;
    }
  }

  /**
   * Spreads a fire from one unit to another. This is entirely controlled by fire spreading logic rules. If a matching
   * rule is found, state in the 'to' unit is updated and this method returns true.
   *
   * @param zone The regional zone containing the EVU
   * @param process The spreading process
   * @param fromEvu The EVU where the fire is coming from
   * @param toEvu The EVU that the fire may spread to
   *
   * @return True if fireType is not null
   */
  public static boolean doFireSpread(RegionalZone zone, Process process, Evu fromEvu, Evu toEvu) {

    Lifeform toLifeform = Area.currentLifeform;
    FireResistance resistance = getSpeciesResistance(zone, toEvu, toLifeform);
    ProcessType fireType = FireEventLogic.getInstance().getSpreadingTypeOfFire(process.getType(),resistance,fromEvu,toEvu,toLifeform);

    return (fireType != null);

  }

  /**
   * Returns a fire process type from the first matching fire type logic rule. If no rules apply to the unit, then
   * a light severity fire is returned, unless the current zone is in Wyoming, which results in a stand replacing fire.
   *
   * @param zone Unused
   * @param evu A vegetation unit
   * @param lifeform A life form
   * @return A process type
   */
  public static ProcessType getTypeOfFire(RegionalZone zone, Evu evu, Lifeform lifeform) {

    FireResistance resistance = getSpeciesResistance(zone,evu,lifeform);

    ProcessType fireType = FireEventLogic.getInstance().getTypeOfFire(resistance,evu,lifeform);

    if (fireType == null) {

      fireType = RegionalZone.isWyoming() ? ProcessType.SRF : ProcessType.LSF;

    }

    return fireType;

  }

  /**
   * finds current state, vegetative type, lifeform, and time step of evu then returns suppresion boolean based on these,
   * @param zone regionalzone being evaluated
   * @param evu ecological vegetative unit being evaluated
   * @return true if do suppression
   */
  public static boolean doSuppression(RegionalZone zone, Evu evu) {
//    int prob    = FireSuppressionData.getProbability(zone,evu);
//    int randNum = Simulation.getInstance().random();
//
//    prob *= 100;
//    if (randNum >= prob) {
//      return false;
//    }
//    else {
//      evu.suppressFire();
//      return true;
//    }

    VegSimStateData state    = evu.getState();
    VegetativeType  vegType  = state.getVeg();
    Lifeform        lifeform = state.getLifeform();
    int tStep = Simulation.getCurrentTimeStep();

    boolean suppress = FireSuppClassALogic.getInstance().isSuppressed(vegType,evu,tStep,lifeform);
    if (suppress) {
      evu.suppressFire();
    }

    return suppress;

  }

/**
 * calculates if a weather event can suppress fire
 * @param zone
 * @param evu
 * @return true if random variable is not greater than probability of suppression
 */
  public static boolean doWeatherEvent(RegionalZone zone, Evu evu) {
    VegSimStateData state    = evu.getState();
    VegetativeType  vegType  = state.getVeg();
    Lifeform        lifeform = state.getLifeform();

    int ts = Simulation.getCurrentTimeStep();
    int prob = FireSuppWeatherClassALogic.getInstance().getProbability(vegType,evu,ts,lifeform);

    int randNum = Simulation.getInstance().random();
    int ratProb = Simulation.getInstance().getRationalProbability(prob);

    if (randNum >= ratProb) {
      return false;
    }
    else {
      evu.suppressFire();

      if (Simulation.getInstance().isDoSimLoggingFile()) {
        PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
        logOut.printf("Time: %d, Unit: %d, Life: %s, Weather Class A Suppression%n",
            ts, evu.getId(), lifeform.toString());
      }
    
      return true;
    }
  }
/**
 * calculates if a weather event will end spread based on fire suppression weather data
 * @param zone regional zone
 * @param eventAcres the acres of fire
 * @param fireSeason choices are spring, summer, fall, winter
 * @param randNum 
 * @return true if ranom number is less than probability
 */
  public static boolean doSpreadEndingWeather(RegionalZone zone, int eventAcres, Climate.Season fireSeason, int randNum) {
    int prob = FireSuppWeatherData.getProbability(zone,eventAcres,fireSeason);
    prob *= 100;

    return (randNum < prob);
  }
/**
 * method to calculate if regeneration state based on zone and evu
 * @param zone regional zone to be evaluated
 * @param evu ecological vegetative unit to be evaluated
 * @return true if RegenerationLogic has data, a boolean value returned from other isRegenState methods (not overloaded) 
 */
  public static boolean isRegenState(RegionalZone zone, Evu evu) {
    int zoneId = zone.getId();

    if (RegenerationLogic.isDataPresent()) {
//      if ((zoneId == ValidZones.EASTSIDE_REGION_ONE) ||
//          (zoneId == ValidZones.TETON) ||
//          (zoneId == ValidZones.NORTHERN_CENTRAL_ROCKIES)) {
//        return isRegenStateEastsideNew(evu);
//      }
      return isRegenStateNew(evu);
    }

    if ((zoneId == ValidZones.EASTSIDE_REGION_ONE) ||
        (zoneId == ValidZones.TETON) ||
        (zoneId == ValidZones.NORTHERN_CENTRAL_ROCKIES)) {
      return isRegenStateEast(zone,evu);
    }
    else if (zoneId == ValidZones.WESTSIDE_REGION_ONE) {
      return isRegenStateWest(zone,evu);
    }
    else { return true; }

  }
  /**
   *  
   * @param evu
   * @return true if evu has regenerationLogic data
   */
  public static boolean isRegenStateNew(Evu evu) {
    return true;
  }
  /**
   * boolean to tell if an EastSide region is in regeneration state
   * @param evu
   * @return true if habitat groug type != A1, A2, B2, D3, AND E2
   */
  public static boolean isRegenStateEastsideNew(Evu evu) {
    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    return (groupType.equals(HabitatTypeGroupType.A1) == false &&
            groupType.equals(HabitatTypeGroupType.A2) == false &&
            groupType.equals(HabitatTypeGroupType.B2) == false &&
            groupType.equals(HabitatTypeGroupType.D3) == false &&
            groupType.equals(HabitatTypeGroupType.E2) == false);
  }

  /**
   * boolean to tell if a westside region is in regeneration state
   * @param zone
   * @param evu
   * @return true if habitat type is A1, A2, B1, B2, B3, C1, C2, D1, D2, D3, E1, E2, F1, F2, G1, OR G2 
   */
  public static boolean isRegenStateWest(RegionalZone zone, Evu evu) {
    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if (groupType.equals(HabitatTypeGroupType.A1) ||
        groupType.equals(HabitatTypeGroupType.A2) ||
        groupType.equals(HabitatTypeGroupType.B1) ||
        groupType.equals(HabitatTypeGroupType.B2) ||
        groupType.equals(HabitatTypeGroupType.B3) ||
        groupType.equals(HabitatTypeGroupType.C1) ||
        groupType.equals(HabitatTypeGroupType.C2) ||
        groupType.equals(HabitatTypeGroupType.D1) ||
        groupType.equals(HabitatTypeGroupType.D2) ||
        groupType.equals(HabitatTypeGroupType.D3) ||
        groupType.equals(HabitatTypeGroupType.E1) ||
        groupType.equals(HabitatTypeGroupType.E2) ||
        groupType.equals(HabitatTypeGroupType.F1) ||
        groupType.equals(HabitatTypeGroupType.F2) ||
        groupType.equals(HabitatTypeGroupType.G1) ||
        groupType.equals(HabitatTypeGroupType.G2)) {
      return true;
    }
    else {
      return false;
    }
  }
/**
 * boolean to tell if an eastside region is in regeneration state
 * @param zone
 * @param evu
 * @return true if habitat group is B1, B3, C1, C2, D1, D2, E1, F1, F2, G1, OR G2
 */
  public static boolean isRegenStateEast(RegionalZone zone, Evu evu) {
    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if (groupType.equals(HabitatTypeGroupType.B1) ||
        groupType.equals(HabitatTypeGroupType.B3) ||
        groupType.equals(HabitatTypeGroupType.C1) ||
        groupType.equals(HabitatTypeGroupType.C2) ||
        groupType.equals(HabitatTypeGroupType.D1) ||
        groupType.equals(HabitatTypeGroupType.D2) ||
        groupType.equals(HabitatTypeGroupType.E1) ||
        groupType.equals(HabitatTypeGroupType.F1) ||
        groupType.equals(HabitatTypeGroupType.F2) ||
        groupType.equals(HabitatTypeGroupType.G1) ||
        groupType.equals(HabitatTypeGroupType.G2)) {
      return true;
    }
    else {
      return false;
    }
  }

  public static VegetativeType regen(Lifeform lifeform, Evu evu) {
    if (RegenerationLogic.isDataPresent()) {
      return regenNew(lifeform,evu);
    }
    else {
      return regenCommon(evu);
    }
  }
  /**
   * regeneration information - uses current time step, habitat type group, vegetative state, and adjacent data to calculate resprouting, adjacent resprouting, 
   * in place seed, in landscape seed, adjacent producing seed, 
   * @param lifeform
   * @param evu
   * @return
   */
  private static VegetativeType regenNew(Lifeform lifeform, Evu evu) {
    AdjacentData[]   adjacentData;
    int              acres;
    Integer          keyValueIndex;
    int              index;
    VegetativeType   newState = null;
    HabitatTypeGroup htGrp;
    int              i,j;
    int              numAdj;
    int              cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    adjacentData = evu.getAdjacentDataNotNull();
    numAdj        = adjacentData.length;
    htGrp         = evu.getHabitatTypeGroup();

    HabitatTypeGroupType ecoGroup = evu.getHabitatTypeGroup().getType();


    // *** Resprouting ***
    // *******************
    {
      VegetativeType tmpState = RegenerationLogic.getResproutingState(ecoGroup, evu,lifeform);
      if (tmpState != null) {
        newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                           tmpState.getSizeClass(),
                                           tmpState.getAge(),
                                           tmpState.getDensity());
        if (newState != null) {
          return newState;
        }
      }
    }
    // *** Adjacent Resprouting ***
    /// ***************************

    // Regarding adjacent Species:
    // Do not want current because doNextState may have already happened
    // for this adj unit, in which case we would be getting the wrong Species.

    /* Currently this will choose the first state it comes across.
       There isn't anyway for the use to specify preferred species, like
       for the adjacent column.  Currently this is not a problem, since we
       don't allow multiple values right now.
    */
    for (i=0; i<adjacentData.length; i++) {
      Evu adj = adjacentData[i].evu;

      // Do not want current because doNextState may have already happened
      // for this adj unit, in which case we would be getting the wrong Species.
      if (adj.getState(cStep-1,lifeform) == null) { continue; }

      VegetativeType tmpState = RegenerationLogic.getAdjResproutingState(ecoGroup,adj,cStep-1,lifeform);
      if (tmpState != null) {
        newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                           tmpState.getSizeClass(),
                                           tmpState.getAge(),
                                           tmpState.getDensity());
        if (newState != null) { return newState; }
      }
    }

    // *** In Place Seed ***
    // *********************
    if (evu.producingSeed(lifeform,Evu.IN_PLACE_SEED)) {
      VegetativeType tmpState = RegenerationLogic.getInPlaceSeedState(ecoGroup, evu,lifeform);
      if (tmpState != null) {
        newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                           tmpState.getSizeClass(),
                                           tmpState.getAge(),
                                           tmpState.getDensity());
        if (newState != null) {
          return newState;
        }
      }
    }
    // *** In Landscape Seed ***
    // *************************
    if (evu.producingSeed(lifeform,Evu.IN_LANDSCAPE_SEED)) {
      VegetativeType tmpState = RegenerationLogic.getInLandscapeSeedState(ecoGroup,evu,cStep,lifeform);
      if (tmpState != null) {
        newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                           tmpState.getSizeClass(),
                                           tmpState.getAge(),
                                           tmpState.getDensity());
        if (newState != null) {
          return newState;
        }
      }
    }
    // *** Adjacent Producing Seed ***
    // *******************************
    HashMap<Evu,Integer> seedSource = null;
    Evu[] key = null;
    int[]     value = null;
    if (numAdj > 0) {
      seedSource = new HashMap<Evu,Integer>(numAdj);
      key        = new Evu[numAdj];
      value      = new int[numAdj];
      index      = 0;

      for(i=0;i<key.length;i++) {
        key[i]   = null;
        value[i] = -1;
      }
    }
    else {
      seedSource = new HashMap<Evu,Integer>(1);
      key        = new Evu[1];
      value      = new int[1];
      index      = 0;

      key[0]   = null;
      value[0] = -1;
    }

    for(i=0;i<numAdj;i++) {
      Evu adj = adjacentData[i].evu;

      if (adj.producingSeed(lifeform,Evu.ADJACENT_SEED)) {
        // Do not want current because doNextState may have already happened
        // for this adj unit, in which case we would be getting the wrong Species.
        if (adj.getState(cStep-1,lifeform) == null) { continue; }

        keyValueIndex = (Integer) seedSource.get(adj);
        if (keyValueIndex == null) {
          acres         = adj.getAcres();
          keyValueIndex = new Integer(index);
          seedSource.put(adj,keyValueIndex);
          index++;
        }
        else {
          acres = value[keyValueIndex.intValue()] + adj.getAcres();
        }
        key[keyValueIndex.intValue()]   = adj;
        value[keyValueIndex.intValue()] = acres;
      }
    }

    ArrayList<Species>  prefSpecies = RegenerationLogic.getAdjacentPreferredSpecies();
    ArrayList<VegetativeType>  v;
    if (prefSpecies != null && prefSpecies.size() > 0) {
      for (i=0; i<prefSpecies.size(); i++) {
        Species tmpSpecies = prefSpecies.get(i);
        Evu adj=null;
        for (Iterator it=seedSource.keySet().iterator(); it.hasNext(); ) {
          adj = (Evu)it.next();
          if (adj.getState(cStep-1,lifeform).getVegType().getSpecies() == tmpSpecies) {
            break;
          }
          adj = null;
        }
        if (adj == null) { continue; }

        v = RegenerationLogic.getAdjacentStates(ecoGroup,adj,cStep-1,lifeform);
        if (v == null || v.size() == 0) { continue; }

        for (j=0; j<v.size(); j++) {
          VegetativeType tmpState = (VegetativeType)v.get(j);
          newState = htGrp.getVegetativeType(tmpState.getSpecies(),
                                             tmpState.getSizeClass(),
                                             tmpState.getAge(),
                                             tmpState.getDensity());
          if (newState != null) { return newState; }
        }
      }
    }

    Utility.sort(key,value);
    Evu[] sortedKeys = key;

    for (i=0; i<sortedKeys.length; i++) {
      if (sortedKeys[i] == null) { continue; }
      v = RegenerationLogic.getAdjacentStates(ecoGroup,sortedKeys[i],cStep-1,lifeform);
      if (v == null || v.size() == 0) { continue; }
      for (j=0; j<v.size(); j++) {
        VegetativeType tmpState = (VegetativeType)v.get(j);
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
   * calculates common regeneration based on current time step, current area, adjacent species, habitat type group, vegetative type, landscape seed, in place seed, 
   * <p>note: often the adjacent state is not the current state, this is because doNextState may have already happened which will result in the wrong adjacent data. 
   * This is noted in the code where applicable  
   * @param evu
   * @return the vegetative type commonly regenerated
   */
  private static VegetativeType regenCommon(Evu evu) {
    AdjacentData[]   adjacentData;
    Hashtable        seedSource = null;
    Species          species, adjSpecies;
    Density          density;
    int              zoneId;
    int              acres;
    Species[]        key = null, sortedKeys;
    int[]            value = null;
    Integer          keyValueIndex;
    int              index;
    VegetativeType   newState = null, tmpState;
    HabitatTypeGroup htGrp;
    Area             area = Simpplle.currentArea;
    Evu              adj;
    Enumeration      e;
    int              i,j;
    int              numAdj;
    HabitatTypeGroupType groupType;
    int              cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();

    adjacentData = evu.getAdjacentDataNotNull();
    numAdj        = adjacentData.length;
    htGrp         = evu.getHabitatTypeGroup();

    {
      VegSimStateData state = evu.getState();
      if (state == null) { return null; }
      species = state.getVeg().getSpecies();
      density = state.getVeg().getDensity();
      groupType = htGrp.getType();
    }
    zoneId        = Simpplle.getCurrentZone().getId();

    if (numAdj > 0) {
      seedSource = new Hashtable(numAdj);
      key        = new Species[numAdj];
      value      = new int[numAdj];
      index      = 0;

      for(i=0;i<key.length;i++) {
        key[i]   = null;
        value[i] = -1;
      }
    }
    else {
      seedSource = new Hashtable(1);
      key        = new Species[1];
      value      = new int[1];
      index      = 0;

      key[0]   = null;
      value[0] = -1;
    }

    // Regarding adjacent Species:
    // Do not want current because doNextState may have already happened
    // for this adj unit, in which case we would be getting the wrong Species.

    if (species == Species.QA || species == Species.QA_MC) {
      newState = htGrp.getVegetativeType(Species.QA, SizeClass.SS, density);
      if (newState != null) {
        return newState;
      }
    }

    for(i=0;i<numAdj;i++) {
      adj = adjacentData[i].evu;
      VegSimStateData state = adj.getState(cStep-1);
      if (state == null) { continue; }
      adjSpecies = state.getVeg().getSpecies();
      if (adjSpecies == Species.QA || adjSpecies == Species.QA_MC) {
        newState = htGrp.getVegetativeType(Species.QA, SizeClass.SS, density);
        if (newState != null) {
          return newState;
        }
      }
    }

    if (species == Species.CW || species == Species.CW_MC) {
      newState = htGrp.getVegetativeType(Species.CW, SizeClass.SS, density);
      if (newState != null) {
        return newState;
      }
    }

    if (zoneId == ValidZones.SIERRA_NEVADA ||
        zoneId == ValidZones.SOUTHERN_CALIFORNIA) {
      newState = caRegen(htGrp,species,groupType);
      if (newState != null) { return newState; }
    }

    // Should be larger than pole size check...
    //  ** In Place seed **
    if ((zoneId != ValidZones.SIERRA_NEVADA ||
         zoneId == ValidZones.SOUTHERN_CALIFORNIA) &&
        (species == Species.LP    || /*species == Species.LP_DF ||*/
         species == Species.DF_LP || species == Species.L_LP  ||
         species == Species.LP_AF || species == Species.LP_GF ||
         species == Species.ES_LP || species == Species.LP_AF
         /*|| species == Species.WB_LP*/)) {
      newState = htGrp.getVegetativeType(Species.LP,SizeClass.SS,density);
      if (newState == null) {
        newState = htGrp.getVegetativeType(species,SizeClass.SS,density);
      }
      if (newState != null) {
        return newState;
      }
    }

    // **  ** In landscape **
    // ** add conditions for birds spreading seed for wb and pf           **
    // ** assume there are other seed bearing stands within the landscape **
    if (species == Species.WB) {
      newState = htGrp.getVegetativeType(Species.WB, SizeClass.SS, density);
      if (newState != null) {
        return newState;
      }
    }
    else if (species == Species.PF) {
      newState = htGrp.getVegetativeType(Species.PF, SizeClass.SS, density);
      if (newState != null) {
        return newState;
      }
    }
    // ** In Place seed **
    else if (evu.producingSeed() &&
             (species != Species.C  && species != Species.WH   &&
              species != Species.MH && species != Species.WH_C &&
              species != Species.WH_C_GF && species != Species.IC )) {
      seedSource.put(species,new Integer(index));
      key[index]   = species;
      value[index] = evu.getAcres();
      index++;
    }
    else if (species == Species.NF) {
      newState = htGrp.getVegetativeType("NF/NF/1");
      if (newState != null) {
        return newState;
      }
    }
    else if (species == Species.GRASS) {
      newState = htGrp.getVegetativeType("GRASS/GRASS/1");
      if (newState != null) {
        return newState;
      }
    }


    if (seedSource.size() == 0) {
      // Build a hashtable: key = species, value = acres;
      //   if species is seed producing add its acres to the
      //   the value already in the hashtable.
      for(i=0;i<numAdj;i++) {
        adj = adjacentData[i].evu;

        if (adj.producingSeed()) {
          // Do not want current because doNextState may have already happened
          // for this adj unit, in which case we would be getting the wrong Species.
          VegSimStateData state = adj.getState(cStep-1);
          if (state == null) { continue; }

          species  = state.getVeg().getSpecies();
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
    }

    // ** This is the adjacent section **
    if (seedSource.size() != 0) {
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
    return null;
  }

  /**
   * California regeneration vegetative type - passes habitat type group, species, habitat type group type to overloaded caRegen method
   * @param zone
   * @param evu
   * @return
   */
  public static VegetativeType caRegen(RegionalZone zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return null; }

    return caRegen(evu.getHabitatTypeGroup(), species,
                   evu.getHabitatTypeGroup().getType());
  }
/**
 * method to calculate vegetative regeneration type for california
 * @param htGrp habitat type group passed from caRegen
 * @param species species passed from caRegen
 * @param groupType the habitat type group type passed from caRegen
 * @return the vegetative type regenerated
 */
  private static VegetativeType caRegen(HabitatTypeGroup htGrp,
                                        Species species,
                                        HabitatTypeGroupType groupType)
  {
    VegetativeType newState = null;

    if (species == Species.CLO) {
      newState = htGrp.getVegetativeType("CLO/SS/2");
    }
    else if (species == Species.ILO) {
      newState = htGrp.getVegetativeType("ILO/SS/2");
    }
    else if (species == Species.BO_PP) {
      newState = htGrp.getVegetativeType("BO/SS/2");
    }
    else if (species == Species.BSB) {
      newState = htGrp.getVegetativeType("BSB/CLUMPED/1");
    }
    else if (species == Species.SB) {
      newState = htGrp.getVegetativeType("SB/CLUMPED/1");
    }
    else if (species == Species.MDS) {
      newState = htGrp.getVegetativeType("MDS/CLUMPED/1");
    }
    else if (species == Species.TBSB) {
      newState = htGrp.getVegetativeType("GRASS/CLUMPED/1");
    }
    else if (species == Species.CSS) {
      newState = htGrp.getVegetativeType("CSS/OPEN-MID-SHRUB/1");
    }
    else if (species == Species.C_CHP) {
      newState = htGrp.getVegetativeType("C-CHP/OPEN-TALL-SHRUB/1");
    }
    else if (species == Species.NM_CHP) {
      newState = htGrp.getVegetativeType("NM-CHP/OPEN-TALL-SHRUB/1");
    }
    else if (species == Species.MTN_CHP) {
      newState = htGrp.getVegetativeType("MTN-CHP/OPEN-TALL-SHRUB/1");
    }
    else if (species == Species.SD_CHP) {
      newState = htGrp.getVegetativeType("SD-CHP/OPEN-TALL-SHRUB/1");
    }
    else if (species == Species.EXOTIC_GRASSES) {
      newState = htGrp.getVegetativeType("EXOTIC-GRASSES/UNIFORM/1");
    }
    else if (species == Species.CSS_EXOTICS) {
      newState = htGrp.getVegetativeType("CSS-EXOTICS/OPEN-MID-SHRUB/1");
    }
    else if (species == Species.GRASS) {
      if (groupType.equals(HabitatTypeGroupType.SA)) {
        newState = htGrp.getVegetativeType("GRASS/CLUMPED/1");
      }
      else {
        newState = htGrp.getVegetativeType("GRASS/UNIFORM/1");
      }
    }
    else {
      newState = null;
    }
    return newState;
  }

  /**
   * Returns the fire resistance of the vegetation unit. The result depends on the area's current life form and the
   * current simulation time step.
   *
   * @param zone A regional zone
   * @param evu An ecological vegetative unit being evaluated
   * @return A fire resistance; low, moderate, or high
   */
  public static FireResistance getSpeciesResistance(RegionalZone zone, Evu evu) {
    return getSpeciesResistance(zone,evu,Area.getCurrentLifeform(evu));
  }

  /**
   * Determines the fire resistance of the species in a vegetation unit's life form at the current time step. If a
   * species has conditional resistance, then the resistance is based on the habitat type group of the unit.
   *
   * @todo Remove the regional zone parameter. This class previously read fire resistance from hard-coded zone types.
   *
   * @param zone Ignore -- unused
   * @param evu An vegetation unit
   * @param lifeform A life form
   * @return A fire resistance; low, moderate, or high
   */
  public static FireResistance getSpeciesResistance(RegionalZone zone, Evu evu, Lifeform lifeform) {

    VegSimStateData state = evu.getState(lifeform);

    if (state == null) {
      return FireResistance.LOW;
    }

    Species species = state.getVeg().getSpecies();
    FireResistance resistance = species.getFireResistance();

    if (resistance == FireResistance.CONDITIONAL) {

      ArrayList groups;

      groups = species.getResistanceGroups(FireResistance.LOW);
      if (groups != null && groups.contains(evu.getHabitatTypeGroup().getType())) {
        return FireResistance.LOW;
      }

      groups = species.getResistanceGroups(FireResistance.MODERATE);
      if (groups != null && groups.contains(evu.getHabitatTypeGroup().getType())) {
        return FireResistance.MODERATE;
      }

      groups = species.getResistanceGroups(FireResistance.HIGH);
      if (groups != null && groups.contains(evu.getHabitatTypeGroup().getType())) {
        return FireResistance.HIGH;
      }

      return FireResistance.LOW;

    } else if (resistance == FireResistance.UNKNOWN) {

      return FireResistance.LOW;

    }

    return resistance;

  }

  /**
   * calculates common species resistance based on habitat group type and evu species
   *  <p> note species is from an list data structure requiring a cast to Species object
   * @param zone
   * @param evu
   * @return an integer representation of fire resistance choices are low=0, moderate=1, high=2
   */
  private static int getSpeciesResistanceCommon(RegionalZone zone, Evu evu) {
    Species              species   = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return LOW; }

    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if (species == Species.EARLY_SERAL &&
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
         groupType.equals(HabitatTypeGroupType.G2))) {
      return HIGH;
    }
    else if (species == Species.LATE_SERAL &&
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
              groupType.equals(HabitatTypeGroupType.G2))) {
      return MODERATE;
    }
    else if (species == Species.ALTERED_NOXIOUS || species == Species.NOXIOUS        ||
             species == Species.EARLY_SERAL     || species == Species.MID_SERAL      ||
             species == Species.LATE_SERAL      || species == Species.FESCUE         ||
             species == Species.AGSP            || species == Species.HERBS          ||
             species == Species.ALPINE_GRASSES  || species == Species.UPLAND_GRASSES ||
             species == Species.ALTERED_GRASSES || species == Species.NATIVE_FORBS   ||
             species == Species.ES              || species == Species.ES_AF          ||
             species == Species.AF              || species == Species.WH             ||
             species == Species.MH              || species == Species.WH_C           ||
             species == Species.AF_MH           || species == Species.CW             ||
             species == Species.CW_MC           || species == Species.QA             ||
             species == Species.QA_MC           || species == Species.WH_C_GF) {
      return LOW;
    }
    else if (species == Species.FS_S_G       || species == Species. MESIC_SHRUBS ||
             species == Species.RIP_SHRUBS   || species == Species.RIP_S_GRAMS   ||
             species == Species.RIP_DECID    || species == Species.RIP_GRAMS     ||
             species == Species.ALPINE_HERBS || species == Species.ALPINE_SHRUBS ||
             species == Species.JUSC_AGSP    || species == Species.JUSC_ORMI     ||
             species == Species.LP           || species == Species.AL            ||
             species == Species.PF           || species == Species.GF            ||
             species == Species.C            || species == Species.WP            ||
             species == Species.RRWP         || species == Species.WB            ||
             species == Species.PF_LP        || species == Species.DF_AF         ||
             species == Species.DF_LP        || species == Species.DF_ES         ||
             species == Species.ES_LP        || species == Species.LP_AF         ||
             species == Species.WB_DF        || species == Species.DF_GF         ||
             species == Species.L_ES         || species == Species.L_GF          ||
             species == Species.AL_AF        || species == Species.DF_WP         ||
             species == Species.L_WP         || species == Species.DF_RRWP       ||
             species == Species.L_RRWP       || /*species == Species.WB_LP         ||*/
             species == Species.WB_ES        || species == Species.WB_AF         ||
             species == Species.DF_AF_ES     || species == Species.DF_LP_ES      ||
             species == Species.L_DF_GF      || species == Species.DF_PP_GF      ||
             species == Species.L_LP_GF      || species == Species.DF_RRWP_GF    ||
             species == Species.L_WP_GF      || species == Species.L_RRWP_GF     ||
             species == Species.L_DF_RRWP    || species == Species.WB_ES_LP      ||
             species == Species.L_DF_ES      || species == Species.WB_LP_AF      ||
             species == Species.WB_ES_AF     || species == Species.AL_WB_AF      ||
             species == Species.DF_LP_AF     || species == Species.AF_ES_LP) {
      return MODERATE;
    }
    else if (species == Species.MTN_FS_SHRUBS || species == Species.MTN_MAHOGANY    ||
             species == Species.XERIC_SHRUBS  || species == Species.XERIC_FS_SHRUBS ||
             species == Species.MTN_SHRUBS    || species == Species.DF              ||
             species == Species.PP            || species == Species.L               ||
             species == Species.AL            || species == Species.PP_DF           ||
             species == Species.L_DF          || species == Species.L_PP            ||
             species == Species.L_LP          || species == Species.DF_PP_LP        ||
             species == Species.L_DF_PP       || species == Species.L_DF_LP         ||
             species == Species.L_PP_LP) {
      return HIGH;
    }
    else {
      return LOW;
    }
  }

  /**
   * fire resistence for species in the specific region of SierraNevada.  this does not use the habitat group type as the common method did.  goes solely off species
   * <p> note species is from an list data structure requiring a cast to a Species object
   * @param zone
   * @param evu
   * @return an integer representation of fire resistance choices are low=0, moderate=1, high=2
   */
  private static int getSpeciesResistanceSierraNevada(RegionalZone zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return LOW; }

    if (species == Species.AGR            || species == Species.GRASS   ||
        species == Species.EXOTIC_GRASSES || species == Species.NF      ||
        species == Species.NS             || species == Species.CSS     ||
        species == Species.MDS            || species == Species.MTN_CHP ||
        species == Species.NM_CHP         || species == Species.C_CHP   ||
        species == Species.CA_CHP         || species == Species.SD_CHP  ||
        species == Species.BSB            || species == Species.SB      ||
        species == Species.TBSB           || species == Species.WH      ||
        species == Species.CW             || /*species == Species.ASPEN   ||*/
        species == Species.MH             || species == Species.MC_IC   ||
        species == Species.MC_WF          || species == Species.WP      ||
        species == Species.CSS_EXOTICS) {
      return LOW;
    }
    else if (species == Species.ILO   || species == Species.CLO   || species == Species.BO     ||
             species == Species.BO_PP || species == Species.PC    || species == Species.PJ     ||
             species == Species.LP    || species == Species.WB    || species == Species.WJ     ||
             species == Species.MC_DF || species == Species.MC_PP || species == Species.MC_SEQ ||
             species == Species.MC_RF || species == Species.LP_RF) {
      return MODERATE;
    }
    else if (species == Species.JP || species == Species.PP) {
      return HIGH;
    }
    else {
      return LOW;
    }
  }

  /**
   * fire resistence for species in the specific region of Southern California.  this does not use the habitat group type as the common method did.  goes solely off species
   * <p> note species is from an list data structure requiring a cast to a Species object
   * @param zone
   * @param evu
   * @return an integer representation of fire resistance choices are low=0, moderate=1, high=2
   */
  private static int getSpeciesResistanceSouthernCalifornia(RegionalZone zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return LOW; }

    if (species == Species.AGR            || species == Species.GRASS   ||
        species == Species.EXOTIC_GRASSES || species == Species.NF      ||
        species == Species.NS             || species == Species.CSS     ||
        species == Species.MDS            || species == Species.MTN_CHP ||
        species == Species.NM_CHP         || species == Species.C_CHP   ||
        species == Species.CA_CHP         || species == Species.SD_CHP  ||
        species == Species.BSB            || species == Species.SB      ||
        species == Species.TBSB           || species == Species.WH      ||
        species == Species.CW             || /*species == Species.ASPEN   ||*/
        species == Species.MH             || species == Species.MC_IC   ||
        species == Species.MC_WF          || species == Species.WP      ||
        species == Species.CSS_EXOTICS) {
      return LOW;
    }
    else if (species == Species.ILO   || species == Species.CLO   || species == Species.BO     ||
             species == Species.BO_PP || species == Species.PC    || species == Species.PJ     ||
             species == Species.LP    || species == Species.WB    || species == Species.WJ     ||
             species == Species.MC_DF || species == Species.MC_PP || species == Species.MC_SEQ ||
             species == Species.MC_RF || species == Species.LP_RF) {
      return MODERATE;
    }
    else if (species == Species.JP || species == Species.PP) {
      return HIGH;
    }
    else {
      return LOW;
    }
  }

  /**
   * fire resistence for species in the specific region of Gila.  this does not use the habitat group type as the common method did.  goes solely off species
   * <p> note species is from an list data structure requiring a cast to a Species object
   * @param zone
   * @param evu
   * @return an integer representation of fire resistance choices are low=0, moderate=1, high=2
   */
  private static int getSpeciesResistanceGila(RegionalZone zone, Evu evu) {
    Species species   = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return LOW; }

    HabitatTypeGroupType groupType = evu.getHabitatTypeGroup().getType();

    if (species == Species.GRA      || species == Species.GRA_SMS  ||
        species == Species.GRA_TAA  || /*species == Species.GRA_TAD ||*/
        species == Species.GRA_TDF  || /*species == Species.GRA_TDO  ||*/
        species == Species.GRA_TMC  || species == Species.GRA_TJW  ||
        species == Species.GRA_TPI  || species == Species.GRA_TPJ  ||
        species == Species.GRA_FW   || species == Species.GRA_FD   ||
        species == Species.GRA_TOC  || species == Species.GRA_TODF ||
        species == Species.GRA_TOPP || species == Species.GRA_TMS  ||
        species == Species.GRA_TOW  || species == Species.GMU      ||
        species == Species.GWE      || species == Species.GWE_TCF  ||
        species == Species.GWE_TCW  || species == Species.GWE_TRF  ||
        species == Species.SHR      || species == Species.SHR_TCF  ||
        species == Species.SHR_TRF  || species == Species.SHR_TODF ||
        species == Species.SHR_TOPP || species == Species.SHR_TOW  ||
        species == Species.SMS      || species == Species.SGO      ||
        species == Species.SMZ      || species == Species.TAA      ||
        species == Species.TCF      || species == Species.TCW      ||
        species == Species.TDF_OAK  || species == Species.TPP_GRA  ||
        species == Species.TES      || species == Species.TMS      ||
        species == Species.TGO      || species == Species.TOC      ||
        species == Species.TOW      || species == Species.TRE      ||
        species == Species.TRF      || species == Species.TRJ      ||
        species == Species.TSF      || species == Species.TWF) {
      return LOW;
    }
    else if (species == Species.TADF    || species == Species.TDFA    ||
             species == Species.TDF     || species == Species.TPP_TDF ||
             species == Species.TDF_TPP || species == Species.TJW     ||
             species == Species.TODF    || species == Species.TDFO    ||
             species == Species.TPI     || species == Species.TPJ     ||
             species == Species.TWP) {
      return MODERATE;
    }
    else if (species == Species.NRK  || species == Species.NFL  ||
             species == Species.NPT  || species == Species.WET  ||
             species == Species.WAT  || species == Species.TOPP ||
             species == Species.TPPO || species == Species.TPP) {
      return HIGH;
    }
    else if ((species == Species.TMC) &&
             (groupType.equals(HabitatTypeGroupType.FD))) {
      return HIGH;
    }
    else if ((species == Species.TMC) &&
             (groupType.equals(HabitatTypeGroupType.FW))) {
      return MODERATE;
    }
    else {
      return LOW;
    }
  }

  /**
   * fire resistence for species in the specific region of Utah.  this does not use the habitat group type as the common method did.  goes solely off species
   * <p> note species is from an list data structure requiring a cast to a Species object
   *
   * @param zone
   * @param evu
   * @return an integer representation of fire resistance choices are low=0, moderate=1, high=2 
   */
  private static int getSpeciesResistanceUtah(RegionalZone zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return LOW; }

    if (species == Species.AG     || species == Species.GW_PG     ||
        species == Species.MS_PG  || species == Species.MS_PJU_PG ||
        species == Species.PJU    || species == Species.PJU_MM_OK ||
        species == Species.PJU_MS || species == Species.PJU_OK    ||
        species == Species.PJU_WS || species == Species.SD_PG     ||
        species == Species.WS_PG  || species == Species.WS_PJU_PG) {
      return LOW;
    }
    else if (species == Species.A || species == Species.A_MF ||
             species == Species.A_MS || species == Species.A_PG ||
             species == Species.A_SF || species == Species.MF ||
             species == Species.MF_A || species == Species.MM_OK ||
             species == Species.MM_OK_PJU || species == Species.OK ||
             species == Species.OK_PJU || species == Species.PP_MF ||
             species == Species.SF || species == Species.SF_A) {
      return MODERATE;
    }
    else if (species == Species.PG ||species == Species.PP) {
      return HIGH;
    }
    else {
      return LOW;
    }
  }
/**
  * fire resistence for species in the specific region of Alaska .  this does not use the habitat group type as the common method did.  goes solely off species
  * <p> note species is from an list data structure requiring a cast to a Species object
  *
 * @param zone
 * @param evu
 * @return an integer representation of fire resistance choices are low=0, moderate=1, high=2 
 */
  private static int getSpeciesResistanceAlaska(RegionalZone zone, Evu evu) {
    Species species = (Species)evu.getState(SimpplleType.SPECIES);
    if (species == null) { return LOW; }

    if (species.equals(Species.A_DWS) || species.equals(Species.DWS_A) ||
        species.equals(Species.AB_DWS) || species.equals(Species.DWS_AB) ||
        species.equals(Species.AGR) || species.equals(Species.B_DWS) ||
        species.equals(Species.DWS_B) || species.equals(Species.CW_DWS) ||
        species.equals(Species.DWS_CW) || species.equals(Species.DWS) ||
        species.equals(Species.DWS_C) || species.equals(Species.DWS_BS) ||
        species.equals(Species.BS_DWS) || species.equals(Species.DWS_HD) ||
        species.equals(Species.HD_DWS) || species.equals(Species.DWS_WS) ||
        species.equals(Species.WS_DWS) || species.equals(Species.GH) ||
        species.equals(Species.HERB)) {
      return LOW;
    }
    else if (species.equals(Species.A_BS) || species.equals(Species.BS_A) ||
             species.equals(Species.A_WS) || species.equals(Species.WS_A) ||
             species.equals(Species.AB_BS) || species.equals(Species.BS_AB) ||
             species.equals(Species.AB_WS) || species.equals(Species.WS_AB) ||
             species.equals(Species.B_BS) || species.equals(Species.BS_B) ||
             species.equals(Species.B_WS) || species.equals(Species.WS_B) ||
             species.equals(Species.BS) || species.equals(Species.BS_HD) ||
             species.equals(Species.HD_BS) || species.equals(Species.BS_CW) ||
             species.equals(Species.CW_WS) || species.equals(Species.WS_CW) ||
             species.equals(Species.HD_WS) || species.equals(Species.WS_HD) ||
             species.equals(Species.MH) || species.equals(Species.MSH) ||
             species.equals(Species.WS) || species.equals(Species.WS_B) ||
             species.equals(Species.WS_BS) || species.equals(Species.BS_WS)) {
      return MODERATE;
    }
    else if (species.equals(Species.A) ||
             species.equals(Species.AB) || species.equals(Species.BA) ||
             species.equals(Species.ALD) || species.equals(Species.AQU) ||
             species.equals(Species.B) || species.equals(Species.B_CW) ||
             species.equals(Species.CW_B) || species.equals(Species.CW) ||
             species.equals(Species.MIXED_DWARF_SHRUB) ||
             species.equals(Species.HD) ||
             species.equals(Species.MIXED_LOW_SHRUB) ||
             species.equals(Species.WIL) ||
             species.equals(Species.MIXED_TALL_SHRUB)) {
      return HIGH;
    }
    else { return LOW; }
  }

  /**
   * This is a special function for use with the Cheesman area only.
   * It provides additional regeneration for that particular area.
   *
   * @param evu
   * @return
   */
  public static VegetativeType regenPulse(Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return null; }

    Species          species = state.getVeg().getSpecies();
    SizeClass        sizeClass = state.getVeg().getSizeClass();
    Density          density   = state.getVeg().getDensity();
    HabitatTypeGroup htGrp = evu.getHabitatTypeGroup();

    Species   newSpecies = species;
    SizeClass newSizeClass;
    Density   newDensity;

    if (sizeClass == SizeClass.POLE)            { newSizeClass = SizeClass.PTS; }
    else if (sizeClass == SizeClass.PTS)        { newSizeClass = SizeClass.PMU; }
    else if (sizeClass == SizeClass.MEDIUM)     { newSizeClass = SizeClass.MTS; }
    else if (sizeClass == SizeClass.MTS)        { newSizeClass = SizeClass.MMU; }
    else if (sizeClass == SizeClass.LARGE)      { newSizeClass = SizeClass.LTS; }
    else if (sizeClass == SizeClass.LTS)        { newSizeClass = SizeClass.LMU; }
    else if (sizeClass == SizeClass.LMU)        { newSizeClass = SizeClass.LMU; }
    else if (sizeClass == SizeClass.VERY_LARGE) { newSizeClass = SizeClass.VLTS; }
    else if (sizeClass == SizeClass.VLTS)       { newSizeClass = SizeClass.VLMU; }
    else if (sizeClass == SizeClass.VLMU)       { newSizeClass = SizeClass.VLMU; }
    else { return null; }

    if (density == Density.TWO)        { newDensity = Density.THREE; }
    else if (density == Density.THREE) {newDensity = Density.FOUR; }
    else { return null; }

    return htGrp.getVegetativeType(newSpecies,newSizeClass,newDensity);
  }

  public static void setUseRegenPulse(boolean value) { useRegenPulse = value; }

  public static boolean useRegenPulse() { return useRegenPulse; }

  public static void setRegenPulse() {
    int prob    = 25;
    int randNum = Simulation.getInstance().random();

    prob *= 100;
    doRegenPulse = (randNum < prob);
  }

  public static boolean isRegenPulse() { return doRegenPulse; }

  /**
   * Converts an ownership name string to an enumeration constant.
   * @param ownership An ownership string
   * @return An ownership constant if the string matches, or OTHER.
   */
  public static int findOwnership(String ownership) {
    if (ownership.equals(allOwnership[NF_WILDERNESS])) {
      return NF_WILDERNESS;
    } else if (ownership.equals(allOwnership[NF_OTHER])) {
      return NF_OTHER;
    } else if (ownership.equals(allOwnership[OTHER])) {
      return OTHER;
    } else {
      return OTHER;
    }
  }

  /**
   * @return The number of defined ownership constants.
   */
  public static int getNumOwnership() { return NUM_OWNER; }

  public static String getOwnershipName(int ownership) {
    if (ownership < 0 || (ownership >= allOwnership.length)) {
      return allOwnership[OTHER];
    } else {
      return allOwnership[ownership];
    }
  }

  public String toString () {
    return printName;
  }

//  public static boolean doFireSpottingOld(RegionalZone zone, Evu fromEvu,
//                                       boolean[] origin, boolean[] newOrigin) {
//    AdjacentData[] adjacentData;
//    Evu            adj, fromAdj;
//    Vector         spotFrom, newSpotFrom, tmpVector;
//    int            i, j, k;
//    char           position;
//    boolean        extreme, moreSpread = false;
//    Area           area = Simpplle.getCurrentArea();
//    AreaSummary    areaSummary = Simpplle.getAreaSummary();
//
//    extreme = area.extremeFireEvent(fromEvu.getOriginUnit());
//
//    if (extreme == false ||
//        Utility.getFireSpotting() == false) {
//      return false;
//    }
//
//    adjacentData = fromEvu.getNeighborhood();
//    if (adjacentData == null) { return false; }
//    spotFrom    = new Vector();
//    newSpotFrom = new Vector();
//
//    for(i=0;i<adjacentData.length;i++) {
//      adj = adjacentData[i].evu;
//      position = adjacentData[i].position;
//      if (position == Evu.ABOVE && fromEvu.isAdjDownwind(adj) &&
//          spotFrom.contains(adj) != true) {
//        spotFrom.addElement(adj);
//      }
//    }
//
//    for(i=0;i<3;i++) {
//      for(j=0;j<spotFrom.size();j++) {
//        fromAdj = (Evu) spotFrom.elementAt(j);
//        adjacentData = fromAdj.getNeighborhood();
//
//        for(k=0;k<adjacentData.length;k++) {
//          adj = adjacentData[k].evu;
//          if (adj == null) { continue; }
//
//          position = adjacentData[k].position;
//          if (position == Evu.ABOVE && fromEvu.isAdjDownwind(adj) &&
//              newSpotFrom.contains(adj) != true) {
//            newSpotFrom.addElement(adj);
//            if (determineSpotFireOld(zone,fromEvu,adj)) {
//              // Fire could have overrode a spreading process
//              // so make sure to turn if off as an origin, so
//              // we don't get incorrect spread.
//              origin[adj.getId()] = false;
//              newOrigin[adj.getId()] = true;
//              areaSummary.updateProcessSpreadTo(fromEvu,adj);
//              moreSpread = true;
//            }
//          }
//        }
//      }
//      // Swap.
//      tmpVector   = spotFrom;
//      spotFrom    = newSpotFrom;
//      newSpotFrom = tmpVector;
//      newSpotFrom.removeAllElements();
//    }
//    return moreSpread;
//  }
//
//  private static boolean determineSpotFireOld(RegionalZone zone, Evu fromEvu,
//                                        Evu evu) {
//    ProcessType processType;
//    Density     density;
//    Process     fireType;
//    boolean     extreme;
//
//    density     = evu.getDensity();
//    processType = evu.getCurrentProcess().getType();
//
//    extreme =
//      Simpplle.getCurrentArea().extremeFireEvent(fromEvu.getOriginUnit());
//
//    if ((density == Density.TWO || density == Density.THREE) &&
//        processType.isFireProcess() == false) {
//
//      fireType = getTypeOfFire(zone,evu);
//      evu.updateCurrentProcess(fireType);
//      int prob = (extreme) ? Evu.SE : Evu.S;
//      evu.updateCurrentProb(Evu.SFS);
//
//      return true;
//    }
//    return false;
//  }

}




