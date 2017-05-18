/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * This class has methods pertaining to climate.  The main categories are Season, Moisture, Temperature.
 * The seasons are spring, summer, fall, winter, year.  
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class Climate {

  // Ordinal values must not be changed unless the change
  // is accounted for in database interaction (esp. in EvuSimData)
  // Do not change order of these seasons as other code is dependent on
  // the order, including the position of YEAR.

  public enum Season {

    SPRING, SUMMER, FALL, WINTER, YEAR;

    public static int numValues() { return 5; }

    public static Season getPriorSeason(Season season) {
      switch (season) {
        case SPRING: return WINTER;
        case SUMMER: return SPRING;
        case FALL:   return SUMMER;
        case WINTER: return FALL;
        case YEAR:   return YEAR;
        default:     return YEAR;
      }
    }
  }

  public static Season[] allSeasons = Season.values();

  public enum Moisture { WETTER, NORMAL, DRIER }
  public static final Moisture WETTER = Moisture.WETTER;
  public static final Moisture DRIER  = Moisture.DRIER;

  public enum Temperature { COOLER, NORMAL, WARMER }
  public static final Temperature COOLER = Temperature.COOLER;
  public static final Temperature WARMER = Temperature.WARMER;

  // Decided to use sparse arrays here rather than waste memory on
  // a Hashmap or ArrayList with elements being an inner class.
  // Also this way getting temp/moisture will be lightning fast.
  private Temperature[][] temperature;
  private Moisture[][] moisture;
  private TreeMap<Integer,ArrayList<Season>> userValues;
//  private ArrayList<Integer> userTimes;
//  private ArrayList<Season>  userSeasons;

  // Needed for GUI state information.
  private boolean changed;

//  private static String allTemperatures[] = {"COOLER", "NORMAL", "WARMER"};
//  private static String allMoisture[] = {"WETTER", "NORMAL", "DRIER"};

  /**
   * Initializes the TreeMap to store user values, Temperature [][] and Moisture [][], and a new random variable maker
   * Then calls initProbs which sets all temperature and moisture for seasons to normal
   */
  public Climate() {

    userValues  = new TreeMap<Integer,ArrayList<Season>>();
    changed     = false;
    temperature = new Temperature[Season.numValues()][Simulation.MAX_TIME_STEPS+1];
    moisture    = new Moisture[Season.numValues()][temperature[0].length];

    initProbs();

  }

  /**
   * Initialize probablities.  outer loop goes through all the seasons, inner loop sets the temperature and moisture for all season to normal
   */
  private void initProbs() {
    int prob;
    for (Season season : Climate.allSeasons) {
      for (int i = 0; i < temperature[0].length; i++) {
        temperature[season.ordinal()][i] = Temperature.NORMAL;
        moisture[season.ordinal()][i] = Moisture.NORMAL;
      }
    }
  }

  /**
   * Will set any moisture or temperature value not input by user to NORMAL
   */
  public void allNonUserNormal() {
    for (Season season : Climate.allSeasons) {
      for (int ts = 0; ts < temperature[0].length; ts++) {
        // Want to make sure we don't change user time steps.
        if (isUserClimate(ts,season)) { continue; }
        temperature[season.ordinal()][ts] = Temperature.NORMAL;
        moisture[season.ordinal()][ts] = Moisture.NORMAL;
      }
    }
  }

  /**
   * Calls pickNewValues to put randomized values into the temperature and moisture arrays for each season in spots where user values are not stored.
   */
  public void randomizeClimate() {
    for (Season season : Climate.allSeasons) {
      for (int ts = 0; ts < temperature[0].length; ts++) {
        // Want to make sure we don't change user time steps.
        if (isUserClimate(ts,season)) { continue; }

        pickNewValues(ts,season);
      }
    }
  }
  
  /**
   * Method to create random temperature and moisture conditions for
   * @param tStep the time step
   * @param season from season enumeration.  
   */
  private void pickNewValues(int tStep, Season season) {

    Random random = Simulation.getInstance().getRandom();

    int prob = random.nextInt(100);

    if      (prob >= 0  && prob <= 32)  temperature[season.ordinal()][tStep] = COOLER;
    else if (prob >= 33 && prob <= 67)  temperature[season.ordinal()][tStep] = Temperature.NORMAL;
    else if (prob >= 68 && prob <= 100) temperature[season.ordinal()][tStep] = WARMER;

    prob = random.nextInt(100);

    if      (prob >= 0  && prob <= 32)  moisture[season.ordinal()][tStep] = WETTER;
    else if (prob >= 33 && prob <= 67)  moisture[season.ordinal()][tStep] = Moisture.NORMAL;
    else if (prob >= 68 && prob <= 100) moisture[season.ordinal()][tStep] = DRIER;

  }

  /**
   * Gets all the temperatures in this Climate object.  It is an array of temperatures.
   * @return the array of temperatures for this climate.
   */
  public static Temperature[] getAllTemperatures() { return Temperature.values(); }

  /**
   * Gets all the moistures for this Climate object.  It is an array of moisture values.  
   * @return an arrray of moisture values for this climate
   */
  public static Moisture[] getAllMoisture() { return Moisture.values(); }

  /**
   * gets the default temperature.  This defaults to NORMAL throughout the climate class
   * @return normal temperature
   */
  public static Temperature getDefaultTemperature() { return Temperature.NORMAL; }

  /**
   * gets the default Moisture.  This defaults to NORMAL throughout the climate class
   * @return normal Moisture
   */
  public static Moisture getDefaultMoisture() { return Moisture.NORMAL; }

  /**
   * gets the Integer key to mapping of timeStep to season.  
   * @return time steps
   */
  public Set<Integer> getTimeSteps() { return userValues.keySet(); }

  /**
   * gets temperature from current simulation
   * @return temperature of current simulation, if simulation is null returns default temperature = NORMAL
   */
  public Temperature getTemperature() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation != null) {
      return getTemperature(simulation.getCurrentSeason());
    } else {
      return getDefaultTemperature();
    }
  }
  
  /**
   * overloaded getTemperature function - passes to getTemerature( int, Season) 
   * @param season in which temperature is to be gotten from based on current time step
   * @return for a running simulation returns the temperature at current time step within season parameter, if no simulation returns default temperature = NORMAL
   */
  public Temperature getTemperature(Season season) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation == null) {
      return getDefaultTemperature();
    }

    return getTemperature(simulation.getCurrentTimeStep(),season);
  }

  /**
   * overloaded getTemperature function.  temperature from temperature [][] for season
   * @param tStep current simulation time step
   * @param season current simulation season
   * @return returns temperature from temperature [][] for season - from enumeration, and  time step
   */
  public Temperature getTemperature(int tStep, Season season) {
    if (tStep < 0 || tStep >= temperature[0].length) {
      return getDefaultTemperature();
    }
    return temperature[season.ordinal()][tStep];
  }

  /**
   *Gets a temperature object which can be COOLER, NORMAL, WARMER
   * @param str the temperature condition
   * @return the temperature
   */
  private Temperature getTemperatureId(String str) {
    if      (str.equals("COOLER")) return COOLER;
    else if (str.equals("NORMAL")) return Temperature.NORMAL;
    else if (str.equals("WARMER")) return WARMER;
    else                           return Temperature.NORMAL;
  }

  /**
   * uses enum to get string name of temperature condition
   * @param temp from temperature enum
   * @return name of temperature condition in uppercase
   */
  private String getTemperatureStr(Temperature temp) {
    switch (temp) {
      case COOLER: return "COOLER";
      case NORMAL: return "NORMAL";
      case WARMER: return "WARMER";
      default:     return "NORMAL";
    }
  }
  
  /**
   * Gets moisture from currrent simulation for the current season.  If simulation is null return default Moisture.
   * Choices are WETTER, NORMAL, DRIER.
   * @return Moisture 
   */
  public Moisture getMoisture() {
    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation != null) {
      return getMoisture(simulation.getCurrentSeason());
    } else {
      return getDefaultMoisture();
    }
  }

  /**
   * Gets season moisture condition from moisture enum for current time step.  Choices are WETTER, NORMAL, DRIER
   * @param season the current season passed 
   * @return moisture condition of current simulation by current season unless simulation is null and then defaults to NORMAL
   */
  public Moisture getMoisture(Season season) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    if (simulation == null) {
      return getDefaultMoisture();
    }

    return getMoisture(simulation.getCurrentTimeStep(),season);
  }

//  public boolean isMoisturePresent(int tStep, Moisture moisture) {
//    Season[] seasons = Climate.allSeasons;
//    for (int i=0; i<seasons.length; i++) {
//      if (getMoisture(tStep,seasons[i]) == moisture) { return true; }
//    }
//    return false;
//  }
  
  /**
   * overloaded getMoisture function
   * @param tStep time step 
   * @param season current simulation season
   * @return the moisture from moisture [][] indexed by Season enum ordinal and current time step.  
   */
  public Moisture getMoisture(int tStep, Season season) {
    if (tStep < 0 || tStep >= temperature[0].length) {
      return getDefaultMoisture();
    }
    return moisture[season.ordinal()][tStep];
  }

  /**
   * 
   * @param str name of moisture condition
   * @return id in enum of parameter moisture condition
   */
  private Moisture getMoistureId(String str) {
    if      (str.equals("WETTER")) return WETTER;
    else if (str.equals("NORMAL")) return Moisture.NORMAL;
    else if (str.equals("DRIER"))  return DRIER;
    else                           return Moisture.NORMAL;
  }

  /**
   * Gets moisture string from moisture enum.  Choices are WETTER, NORMAL, DRIER
   * @param moisture requested moisture condition
   * @return name of moisture condition in uppercase
   */
  private String getMoistureStr(Moisture moisture) {
    switch (moisture) {
      case WETTER: return "WETTER";
      case NORMAL: return "NORMAL";
      case DRIER:  return "DRIER";
      default:     return "NORMAL";
    }
  }

  /**
   * Initializes a climate arraylist with all seasons. adds to userValues keyed by time step
   * @param tStep time step is key into userValue mapping
   * @param season if YEAR will initialize a climate arrayList,
   * <p>if null will create ArrayList for season and map the userValues to the time step and null season
   * <p> if season arraylist does not contain the parameter season, will add the season to arrayList and mark changed.
   */
  public void addClimate(int tStep, Season season) {

    ArrayList<Season> seasons;

    if (season == Season.YEAR) {

      seasons = new ArrayList<Season>();

      seasons.add(Season.YEAR);
      seasons.add(Season.SPRING);
      seasons.add(Season.SUMMER);
      seasons.add(Season.FALL);
      seasons.add(Season.WINTER);

      userValues.put(tStep,seasons);

      markChanged();
      return;

    }

    seasons = userValues.get(tStep);
    if (seasons == null) {
      seasons = new ArrayList<Season>();
      userValues.put(tStep,seasons);
    }
    if (seasons.contains(season) == false) {
      seasons.add(season);
      markChanged();
    }
  }

  /**
   * takes a string name of temperature condition turns to temperarureID and passes to overloaded setTemperature function
   * @param tStep time step
   * @param season
   * @param str name of condition to be turned to ordinal and used as temperatureID
   */
  public void setTemperature(int tStep, Season season, String str) {
    setTemperature(tStep,season,getTemperatureId(str));
  }

  /**
   * overloaded setTemperature function.  sets the temperature [season][time step] to the season and time step
   * if season = YEAR, will set the temperature [season] [time step] to the temperatureID for all seasons in enum
   * @param tStep time step
   * @param season from Season enum choices are SPRING, SUMMER, FALL, WINTER, YEAR
   * @param value temperatureID value
   */
  public void setTemperature(int tStep, Season season, Temperature value) {
    temperature[season.ordinal()][tStep] = value;
    if (season == Season.YEAR) {
      temperature[Season.YEAR.ordinal()][tStep]   = value;
      temperature[Season.SPRING.ordinal()][tStep] = value;
      temperature[Season.SUMMER.ordinal()][tStep] = value;
      temperature[Season.FALL.ordinal()][tStep]   = value;
      temperature[Season.WINTER.ordinal()][tStep] = value;
    }
    markChanged();
  }

  /**
   * Sets the moisture at a particular time step, season and moisture string (used to get ordinal into Moisture Enum
   * @param tStep time step
   * @param season season to be set
   * @param str the string to be used to get the ordinal MoistureID
   */
  public void setMoisture(int tStep, Season season, String str) {
    setMoisture(tStep,season,getMoistureId(str));
  }

  /**
   * overloaded setMoisture function.  sets the moisture [season] [timestep] to moistureID value
   * If season = YEAR will set the [season] [time step] for all seasons to the moistureID 
   * @param tStep time step
   * @param season season in which the moisture value will be set
   * @param value the moistureID value
   */
  public void setMoisture(int tStep, Season season, Moisture value) {
    moisture[season.ordinal()][tStep] = value;
    if (season == Season.YEAR) {
      moisture[Season.YEAR.ordinal()][tStep]   = value;
      moisture[Season.SPRING.ordinal()][tStep] = value;
      moisture[Season.SUMMER.ordinal()][tStep] = value;
      moisture[Season.FALL.ordinal()][tStep]   = value;
      moisture[Season.WINTER.ordinal()][tStep] = value;
    }
    markChanged();
  }

  /**
   * removes a specific time step of climate in userValues map either by removing the season, time step - if no corresponding season, or by setting moisture and temperature to NORMAL
   * @param tStep time step
   * @param season
   */
  public void removeClimate(int tStep, Season season) {
    if (userValues.containsKey(tStep) == false) { return; }

    if (season == Climate.Season.YEAR) {
      userValues.remove(tStep);
    } else {
      ArrayList<Season> seasons = userValues.get(tStep);
      if (seasons == null) { return; }

      seasons.remove(season);
      if (seasons.size() == 0) {
        userValues.remove(tStep);
      }
    }
    temperature[season.ordinal()][tStep] = Temperature.NORMAL;
    moisture[season.ordinal()][tStep] = Moisture.NORMAL;
    markChanged();
  }

  /**
   * removes all climate information for a given time step by setting the temperature[][] and moisture [][] to the default NORMAL, then removes the timestep from UserValue map
   * @param tStep
   */
  public void removeAllClimate(int tStep) {
    if (userValues.containsKey(tStep) == false) { return; }

    ArrayList<Season> seasons = userValues.get(tStep);
    if (seasons == null) { return; }

    for (Season season : seasons) {
      temperature[season.ordinal()][tStep] = Temperature.NORMAL;
      moisture[season.ordinal()][tStep] = Moisture.NORMAL;
    }
    userValues.remove(tStep);
    markChanged();
  }

  /**
   * sets all indexes in temperature []][]and moisture[][] to the default temperature and moisture - NORMAL then clears the userValue map
   */
  public void removeAll() {
    for (Integer ts : userValues.keySet()) {
      ArrayList<Season> seasons = userValues.get(ts);
      for (Season season : seasons) {
        temperature[season.ordinal()][ts] = getDefaultTemperature();
        moisture[season.ordinal()][ts]    = getDefaultMoisture();
      }
    }

    userValues.clear();
    markChanged();
  }

  /**
   * Closes climate file.
   * Sets all indexes in temperature []][]and moisture[][] to the default temperature and moisture - NORMAL then clears the userValue map
   * Clears the Climate file and sets changed to false.
   */
  public void closeFile() {
    removeAll();
    clearFilename();
    setChanged(false);
  }

  /**
   * Checks if a particular time step has a user input climate.
   * @param tStep time step
   * @return true if climate at time step is a user value.
   */
  public boolean isUserClimate(int tStep) {
    return (userValues.containsKey(tStep));
  }

  /**
   * Gets the user seasons by using the parameter time step to get the 
   * ArrayList of seasons from the user values tree map <time step, ArrayList<Season>> then 
   * checking if the parameter season is contained in it.
   * @param tStep to get the ArrayList of seasons from the user values tree map
   * @param season season being checked
   * @return true if season is in the user values tree map
   */
  public boolean isUserClimate(int tStep, Season season) {
    ArrayList<Season> seasons = userValues.get(tStep);
    return (seasons != null && seasons.contains(season));
  }

  /**
   * Checks if user scheduled climate exists.
   * @return True if there is a climate object in the user values arraylist.
   */
  public boolean userScheduleClimateExists() {
    return (userValues.size() != 0);
  }

  /**
   * gets the first time step.  checks for null or size ==0
   * @return if null or size ==0 returns -1, else returns the first time step
   */
  public int getFirstTimeStep() {
    if (userValues == null || userValues.size() == 0) {
      return -1;
    }
    return userValues.firstKey();
  }

  /**
   * method to get the first season using first time step
   * @return the season of the first time step
   */
  public Season getFirstTimeStepSeason() {
    return getFirstUserSeason(userValues.firstKey());
  }

  /**
   * Gets the last key value in user values <time step, ArrayList<Season>>
   * @return last key which is the last time step.
   */
  public int getLastTimeStep() {
    return userValues.lastKey();   
  }

  /**
   * Gets the first index into the season arraylist <time step, ArrayList<Season>> by first using the parameter time step to get the 
   * ArrayList
   * @param timeStep used to get all the user values at 
   * @return
   */
  public Season getFirstUserSeason(int timeStep) {
    ArrayList<Season> seasons = userValues.get(timeStep);
    return seasons.get(0);
  }

  /**
   * Gets the previous user season by first using the parameter time step to get the 
   * ArrayList of seasons from the user values tree map <time step, ArrayList<Season>> then using the parameter 
   * current season variable -1 to index into ArrayList.
   * @param timeStep used to get season ArrayList    
   * @param currentSeason Subtract 1 to get the previous.
   * @return returns previous user season
   */
  public Season getPrevUserSeason(int timeStep, Season currentSeason) {
    ArrayList<Season> seasons = userValues.get(timeStep);
    int index = seasons.indexOf(currentSeason);
    if (index < 1) { return null; }

    return seasons.get(index-1);
  }

  /**
   * Gets the previous user season by first using the parameter time step to get the 
   * ArrayList of seasons from the user values tree map <time step, ArrayList<Season>> then using the parameter 
   * current season variable  + 1 to index into ArrayList.
   * @param timeStep used to get season ArrayList
   * @param currentSeason Add 1 to get the previous.
   * @return if index is -1 or season is the last one in index returns null, else returns next season
   */
  public Season getNextUserSeason(int timeStep, Season currentSeason) {
    ArrayList<Season> seasons = userValues.get(timeStep);
    int index = seasons.indexOf(currentSeason);
    if (index == -1 || index >= (seasons.size() - 1) ) { return null; }

    return seasons.get(index+1);
  }

  public int getPrevTimeStep(int currentTime, boolean userOnly) {
    if (!userOnly && currentTime == 1) {
      return temperature[0].length - 1;
    } else if (!userOnly) {
      return currentTime - 1;
    } else if (userValues.size() == 0) {
      return 1;
    } else {
      int tStep = currentTime - 1;
      int firstTime = getFirstTimeStep();
      while (isUserClimate(tStep) == false) {
        if (tStep > firstTime) {
          tStep--;
        } else if (tStep < firstTime) {
          tStep = getLastTimeStep();
        }
        // otherwise it will be equal to firstTime which is Ok.
      }
      return tStep;
    }
  }

  /**
   * Calculates the next time step from the current time passed in the parameter.
   * @param currentTime
   * @param userOnly
   * @return
   */
  public int getNextTimeStep(int currentTime, boolean userOnly) {
    if (!userOnly && currentTime == (temperature[0].length - 1)) {
      return 1;
    } else if (!userOnly) {
      return currentTime+1;
    } else if (userValues.size() == 0) {
      return -1;
    } else {
      int tStep = currentTime + 1;
      int lastTime = getLastTimeStep();
      while (isUserClimate(tStep) == false) {
        if (tStep < lastTime) {
          tStep++;
        } else if (tStep > lastTime) {
          tStep = getFirstTimeStep();
        }
        // otherwise it will be equal to lastTime which is Ok.
      }
      return tStep;
    }
  }

  /**
   * Calculates whether climate is wet Succession.
   * @return true if wet succession.
   */
  public boolean isWetSuccession() {
    return (getMoisture() == WETTER);
  }

  /**
   * Calculates whether the climate is dry succession.  
   * @return
   */
  public boolean isDrySuccession() {
    return (getMoisture() == Climate.DRIER);
  }

  /**
   * calculates if current simulation season is a drought
   * @return true if temp is WARMER and moisture is DRIER
   */
  public boolean isDrought() {
    Season season = Simpplle.getCurrentSimulation().getCurrentSeason();
    return ((getTemperature(season) == WARMER) && (getMoisture(season) == DRIER));
  }

  /**
   * Calculates Climate object is a wet climate instance.
   * @return true if moisture of current simulation and current season is WETTER
   */
  public boolean isWet() {
    Season season = Simpplle.getCurrentSimulation().getCurrentSeason();
    return (getMoisture(season) == WETTER);
  }

  /**
   * Calculates if time step instance is a drought
   * @param tStep time step
   * @return true if moisture of current simulation and current season is WETTER
   */
  public boolean isDrought(int tStep) {
    Season season = Simpplle.getCurrentSimulation().getCurrentSeason();
    return ((getTemperature(tStep,season) == WARMER) && (getMoisture(tStep,season) == DRIER));
  }

  public void loadData (File infile) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;
    boolean         result = false;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(infile));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      readData(fin);
      setFilename(infile);
      fin.close();
      gzip_in.close();
    } catch (IOException e) {
      throw new SimpplleError("Problems reading from Climate data file:" + infile);
    }
  }

  /**
   * 
   * @param fin Buffered reader passed from loadData
   * @throws SimpplleError
   */
  public void readData(BufferedReader fin) throws SimpplleError {
    String              line;
    StringTokenizerPlus strTok;
    RegionalZone        zone = Simpplle.getCurrentZone();
    String              msg;
    String              tempStr, moistStr;
    Season              season;
    int                 nSteps, tStep;

    try {
      line   = fin.readLine();
      if (line == null) {
        throw new ParseError("Climate Data file is empty.");
      }

      try {
        nSteps = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        throw new ParseError("Invalid number of time steps in Climate data file.");
      }

      removeAll();
      for(int i=0; i<nSteps; i++) {
        line   = fin.readLine();
        if (line == null) {
          throw new ParseError("Invalid Climate Data file.");
        }
        strTok = new StringTokenizerPlus(line,",");

        if (strTok.countTokens() == 3) {
          tStep = strTok.getIntToken();
          season = Season.YEAR;
          tempStr = strTok.getToken();
          moistStr = strTok.getToken();
        } else {
          tStep = strTok.getIntToken();
          season = Season.valueOf(strTok.getToken());
          tempStr = strTok.getToken();
          moistStr = strTok.getToken();

        }
        addClimate(tStep,season);
        setTemperature(tStep,season,tempStr);
        setMoisture(tStep,season,moistStr);
      }
      setChanged(false);
    } catch (ParseError pe) {
      throw new SimpplleError(pe.msg);
    } catch (IOException e) {
      throw new SimpplleError("Problems reading from Climate data file.");
    }
  }

  /**
   * True if changed
   * @return true if changed
   */
  public boolean hasChanged() { return changed; }

  /**
   * Marks system knowledge changed for Climate.  
   */
  private void markChanged() {
    setChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.CLIMATE);
  }

  private void setChanged(boolean value) { changed = value; }

  /**
   * Sets the file to a climate file.
   * @param file
   */
  private void setFilename(File file) {
    SystemKnowledge.setFile(SystemKnowledge.CLIMATE,file);
    markChanged();
  }

  /**
   * Clears the Climate file.
   */
  public void clearFilename() {
    SystemKnowledge.clearFile(SystemKnowledge.CLIMATE);
  }

  /**
   * Saves climate file to a particular parameter file name.
   * @param outfile
   */
  public void saveAs(File outfile) {
    setFilename(outfile);
    save();
  }

  /**
   * saves file as zip GZIPOutputStream and printwrter. The file while have pathname of the Climate and .climate
   *
   * @throws IOException - caught if there are problems outputting file
   */
  public void save() {
    File outfile = Utility.makeSuffixedPathname(SystemKnowledge.getFile(SystemKnowledge.CLIMATE),"","climate");
    GZIPOutputStream out;
    PrintWriter      fout;
    try {
      out = new GZIPOutputStream(new FileOutputStream(outfile));
      fout = new PrintWriter(out);
    } catch (IOException e) {
      System.out.println("Problems opening output file.");
      return;
    }

    save(fout);
    fout.flush();
    fout.close();

    setChanged(false);
  }

  public void save(PrintWriter fout) {
    Integer key;
    String  tempStr, moistStr;

    int count=0;
    for (Integer ts : userValues.keySet()) {
      ArrayList<Season> seasons = userValues.get(ts);
      count += seasons.size();
    }
    fout.println(count);
    for (Integer ts : userValues.keySet()) {
      ArrayList<Season> seasons = userValues.get(ts);
      for (Season season : seasons) {
        tempStr  = getTemperatureStr(getTemperature(ts,season));
        moistStr = getMoistureStr(getMoisture(ts,season));
        fout.println(ts + "," + season.toString() + "," + tempStr + "," + moistStr);
      }
    }
  }

}
