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
 * This class has the methods for making simulations.  Many of these are input by user in Simulation Parameter Dialog.  The order of making a simulation for an existing
 * landscape file is - select a working directory, select a regional zone, load a landscape, set simulation parameters, and run simulation.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public final class Simulation implements SimulationTypes, Externalizable {

  static final long serialVersionUID = -6185381763708611851L;
  static final int  version          = 6;

  public enum InvasiveKind { R1, MSU, MESA_VERDE_NP, NONE};

  private int          numSimulations;
  private int          numTimeSteps;
  private int          pastTimeStepsInMemory;
  private boolean      fireSuppression;
  private int          simulationMethod;
  private Random       random;
  private File         outputFile;
  private float        discount;
  private long         seed;
  private boolean      fixedSeed;
  private boolean      trackSpecialArea;
  private boolean      trackOwnership;
  private boolean      yearlySteps;
  private boolean      writeDatabase=false;
  private boolean      writeAccess=false;
  private boolean      writeProbFiles=false;
  private InvasiveKind invasiveSpeciesKind=InvasiveKind.NONE;
  private boolean      discardData=false;
  private boolean      doProbArcFiles = false;
  private boolean      doAllStatesSummary=true;
  private boolean      doTrackingSpeciesReport=false;
  private boolean      doGisFiles=false;
  private boolean      doSimLoggingFile=false;
  private File         allStatesRulesFile;
  private boolean      inSimulation;
  private AreaSummary        areaSummary;
  private MultipleRunSummary multipleRunSummary;
  private PrintWriter   simLoggingWriter;
  private PrintWriter   invasiveSpeciesMSUProbOut;
  private PrintWriter[] accessEvuSimDataOut;
  private PrintWriter   accessProcessOut;
  private PrintWriter   accessSpeciesOut;
  private PrintWriter   accessSizeClassOut;
  private PrintWriter   accessDensityOut;
  //private PrintWriter   accessEcoGroupOut;
  //private PrintWriter[] accessAreaSummaryOut;
  //private PrintWriter   accessFmzOut;
  private PrintWriter   accessInclusionRuleSpecies;
  private PrintWriter   accessLifeformOut;
  private PrintWriter   accessOwnershipOut;
  private PrintWriter   accessSpecialAreaOut;
  private PrintWriter   accessTrackingSpeciesOut;
  private PrintWriter   accessSlinkMetricsOut;
  private PrintWriter   accessTreatmentOut;

  public static final int MAX_TIME_STEPS = 10000;
  public static final int MAX_SIMULATIONS = 100;

  private static final int DEFAULT_PROB_PRECISION = 2;
  private static int probPrecision = DEFAULT_PROB_PRECISION;
  private static int maxProbability = 10000;

  private int            currentTimeStep;
  private int            currentRun = 0;
  private Climate.Season currentSeason;

  private static final String COMMA         = ",";
  private static final String COLON         = ":";
  private static final String SEMICOLON     = ";";
  private static final String QUESTION_MARK = "?";

  /**
   * Initializes some class variables. 1-5 time steps are considered short term simulations. Greater than 5 time
   * steps are long term simulations.
   */
  public Simulation () {

    numSimulations        = 1;
    numTimeSteps          = 5;
    pastTimeStepsInMemory = 10;
    fireSuppression       = false;
    simulationMethod      = STOCHASTIC;
    random                = null;
    outputFile            = null;
    discount              = 1.0f;
    fixedSeed             = false;
    trackSpecialArea      = false;
    trackOwnership        = false;
    yearlySteps           = false; // means decade time steps are the default
    writeDatabase         = false;
    writeAccess           = false;
    writeProbFiles        = false;
    currentTimeStep       = -1;
    inSimulation          = false;
    invasiveSpeciesKind   = InvasiveKind.NONE;

    /* Uncomment if we want to use the System Property for a Fixed Seed
    String value = System.getProperty("simpplle.fixedRandom");
    if (value != null && value.equalsIgnoreCase("enabled")) {
      fixedSeed = true;
      seed = 42;
    } else {
      fixedSeed = false;
    }
    */
  }

  public static void setProbPrecision(int digits) {
    probPrecision = digits;
    maxProbability = getRationalProbability(100);
  }

  public static void setDefaultProbPrecision() {
    probPrecision = DEFAULT_PROB_PRECISION;
    maxProbability = getRationalProbability(100);
  }

  public static int getRationalProbability(double prob) {
    return (int)Math.round(prob * Math.pow(10,probPrecision));
  }

  public static double getFloatProbability(int ratProb) {
    return (ratProb / Math.pow(10,probPrecision));
  }

  /**
   * Calls the default constructor as well as
   * initializing some class variables with the
   * provided parameters.  Most of these are set by the user in GUI Simulation Parameter dialog.
   * @param nSims The number of simulations.
   * @param nSteps The number of time steps.
   * @param suppression True if fire Suppression is enabled.
   */
  public Simulation (int nSims,
                     int nSteps,
                     boolean suppression,
                     File outfile,
                     float discount,
                     boolean spArea,
                     boolean owner,
                     boolean yearlySteps,
                     String simMethod,
                     boolean writeDatabase,
                     boolean writeAccess,
                     boolean writeProbFiles,
                     InvasiveKind invasiveKind,
                     int nStepsInMemory,
                     File allStatesRulesFile,
                     boolean discardData,
                     boolean doProbArcFiles,
                     boolean doAllStatesSummary,
                     boolean doTrackingSpeciesReport,
                     boolean doGisFiles,
                     boolean fixedSeed,
                     long seed) {

    this();

    this.numSimulations          = nSims;
    this.numTimeSteps            = nSteps;
    this.pastTimeStepsInMemory   = nStepsInMemory;
    this.fireSuppression         = suppression;
    this.outputFile              = outfile;
    this.allStatesRulesFile      = allStatesRulesFile;
    this.discount                = discount;
    this.yearlySteps             = yearlySteps;
    this.writeDatabase           = writeDatabase;
    this.writeAccess             = writeAccess;
    this.writeProbFiles          = writeProbFiles;
    this.invasiveSpeciesKind     = invasiveKind;
    this.discardData             = discardData;
    this.doProbArcFiles          = doProbArcFiles;
    this.doAllStatesSummary      = doAllStatesSummary;
    this.doTrackingSpeciesReport = doTrackingSpeciesReport;
    this.doGisFiles              = doGisFiles;
    this.fixedSeed               = fixedSeed;
    this.seed                    = seed;

    if (simMethod.toUpperCase().equals("STOCHASTIC")) {
      simulationMethod = STOCHASTIC;
    } else if (simMethod.toUpperCase().equals("STAND DEVELOPMENT")) {
      simulationMethod = STAND_DEVELOPMENT;
    } else if (simMethod.toUpperCase().equals("HIGHEST")) {
      simulationMethod = HIGHEST;
    }

    if (nSims > 1) {
      trackSpecialArea = spArea;
      trackOwnership   = owner;
    }

    accessEvuSimDataOut  = new PrintWriter[numSimulations];
    //accessAreaSummaryOut = new PrintWriter[numSimulations];

  }

  public static Simulation instance;

  /**
   * Sets the current simulation instance.
   * @param simulation
   */
  public static void setInstance(Simulation simulation) {
    instance = simulation;
  }

  /**
   * Boolean to see if data will be discarded.  This helps in memory management.
   * @param discardData true if data can be discarded.
   */
  public void setDiscardData(boolean discardData) {
    this.discardData = discardData;
  }

  /**
   * Method to get the current simulation instance.
   * @return current simulation instance
   */
  public static Simulation getInstance() {
    return instance;
  }

  /**
   * Sets the reference to the simulation instance to null.
   */
  public static void clearInstance() {
    instance = null;
  }

  /**
   * If yearly time steps is to be used.  The default is decade, except in Wyoming grassland regional zone. Some of the
   * interactions between processes such as fire and insects or response of grasses to yearly moisture changes make
   * more sense if run yearly.
   * @return
   */
  public boolean isYearlyTimeSteps() { return yearlySteps; }

  /**
   * Chooses decade time steps by making yearlySteps boolean false.  This is the default of OpenSimpplle - which mostly
   * focuses on vegetative change in landscapes over longer periods of time, except for grassland areas (Wyoming) where
   * yearly is the default.
   * @return
   */
  public boolean isDecadeTimeSteps() { return (yearlySteps == false); }

  /**
   * If running yearly time steps, returns true if a decade has passed.
   * @return
   */
  public boolean isDecadeStep() {
    if (yearlySteps == false) return true;
    return ((getCurrentTimeStep() % 10) == 0);
  }

  public float getDiscount() { return discount; }

  /**
   * Gets the simulation's output file.
   * @return a File
   */
  public File getOutputFile() { return outputFile; }

  /**
   * @return an AreaSummary, the Area Summary Instance.
   * @see simpplle.comcode.AreaSummary
   */
  public AreaSummary getAreaSummary() {
    return areaSummary;
  }

  /**
   * @return an MultipleRunSummary, the Multiple Run Summary instance.
   * @see simpplle.comcode.MultipleRunSummary
   */
  public MultipleRunSummary getMultipleRunSummary() {
    return multipleRunSummary;
  }

  /**
   * Gets the current run number.
   * @return the run number.
   */
  public static int getCurrentRun() {
    if (getInstance() == null) { return 1; }
    return getInstance().currentRun;
  }

  /**
   * Gets the current time step number.
   * @return the time step number.
   */
  public static int getCurrentTimeStep() {
    if (getInstance() == null) { return 0; }
    return getInstance().currentTimeStep;
  }

  /**
   * Gets the number of simulations.  This must be between 1-100.
   * @return the number of simulations.
   */
  public int getNumSimulations () {
    return numSimulations;
  }

  /**
   * Returns true if the number of simulations more than one.
   * @return a boolean.
   */
  public boolean isMultipleRun () {
    return (numSimulations > 1);
  }

  /**
    * @return true if multiple run summary exists
    */
  public boolean existsMultipleRunSummary() {
    return (multipleRunSummary != null);
  }

  /**
   * Gets the number of time steps.
   * @return the number of time steps.
   */
  public int getNumTimeSteps () {
    return numTimeSteps;
  }

  /**
   * Gets the past time steps stored in memory.  Minimum is 10.
   * @return
   */
  public int getPastTimeStepsInMemory() {
    return pastTimeStepsInMemory;
  }

  /**
   * Returns current value for fire suppression, true or false.
   * True indicates fire suppression is turned on.
   * @return a boolean indiciating current value of fire suppression.
   */
  public boolean fireSuppression() { return fireSuppression;}

  /**
   * Returns true if we are tracking data by Special Area, false otherwise.
   * @return a boolean.
   */
  public boolean trackSpecialArea() { return trackSpecialArea; }

  /**
   * Returns true if we are tracking data by Ownership, false otherwise.
   * @return a boolean.
   */
  public boolean trackOwnership() { return trackOwnership; }

  /**
    * Returns the current simulation method.
    * Possible values are:
    *   STOCHASTIC
    *   HIGHEST
    *   STAND_DEVELOPMENT (succession)
    */
  public int getSimulationMethod() { return simulationMethod;}

  public Climate.Season getCurrentSeason() {
    return currentSeason;
  }

  public boolean isDoInvasiveSpecies() {
    return invasiveSpeciesKind != InvasiveKind.NONE;
  }

  /**
   * Simulation probability= stand development (succession). One of the three possible types of simulation probabilities.
   * @return true if simulation prob is stand development (succession).
   */
  public boolean isStandDevelopment() {
    return (simulationMethod == STAND_DEVELOPMENT);
  }

  /**
   * Simulation probability= stochastic. One of the three possible types of simulation probabilities.
   * @return true if simulation prob is stochastic.
   */
  public boolean isStochastic() {
    return (simulationMethod == STOCHASTIC);
  }

  /**
   * Simulation probability= highest probability. One of the three possible types of simulation probabilities.
   * @return true if simulation prob is highest.
   */
  public boolean isHighestProbability() {
    return (simulationMethod == HIGHEST);
  }

  /**
   * Converts the value of simulationMethod to a string description.  Choices are stochastic, highest and stand development (succession).
   */
  private String printSimulationMethod () {
    switch(simulationMethod) {
      case STOCHASTIC:        return "STOCHASTIC";
      case HIGHEST:           return "HIGHEST";
      case STAND_DEVELOPMENT: return "STAND DEVELOPMENT";
      default:                return "";
    }
  }

  /**
   * Resets simulation data to defaults. Called before running a new simulation.
   */
  public void reset() {
    numSimulations        = 1;
    numTimeSteps          = 5;
    pastTimeStepsInMemory = 10;
    fireSuppression       = false;
    simulationMethod      = STOCHASTIC;
  }

  public boolean isSimulationRunning() { return inSimulation; }

  /**
   * Invasive species predictions require the nearest distance to each road and trail.
   * @return True if there is an invasive species
   */
  public boolean needNearestRoadTrailInfo() {
    return invasiveSpeciesKind != InvasiveKind.NONE;
  }

  /**
   * This method starts a simulation running.
   * @throws SimpplleError caught in GUI
   */
  public void runSimulation () throws SimpplleError {

    inSimulation = true;

    try {

      if (outputFile != null) {

        if (simpplle.JSimpplle.simLoggingFile()) {
          this.doSimLoggingFile=true;
        }

        File logFile = Utility.makeSuffixedPathname(outputFile, "-log", "txt");

        PrintWriter logOut;

        try {

          logOut = new PrintWriter(new FileOutputStream(logFile));

          if (doSimLoggingFile) {
            File tmpFile = Utility.makeSuffixedPathname(outputFile, "-detaillog", "txt");
            simLoggingWriter = new PrintWriter(new FileOutputStream(tmpFile));
          }

        } catch (Exception ex) {

          throw new SimpplleError(ex.getMessage(),ex);

        }

        logOut.println("SIMPPLLE Simulation Log File");
        logOut.println();
        logOut.println("Date : " + Simpplle.currentDate());
        logOut.println("Time : " + Simpplle.currentTime());
        logOut.println();
        logOut.println("Current Zone : " + Simpplle.getCurrentZone().toString());
        logOut.println("Current Area : " + Simpplle.getCurrentArea().toString());
        logOut.println();
        logOut.println("Number of Simulations : " + numSimulations);
        logOut.print("Number of Time Steps  : " + numTimeSteps);
        String str = (yearlySteps) ? " (Yearly)" : " (Decade)";
        logOut.println(str);
        logOut.println();
        logOut.println("Fire Suppression      : " + fireSuppression);
        if (fireSuppression && (getDiscount() > 1.0f)) {
          logOut.println("Fire Cost Discount    : " + getDiscount());
        }
        logOut.println("Simulation Method     : " + printSimulationMethod());
        logOut.println();

        logOut.println("Data files:");

        File dataFile = SystemKnowledge.getFile(SystemKnowledge.FMZ);
        str = (dataFile == null) ? "Default" : dataFile.toString();
        logOut.println("  Fire Management Zones : " + str);

        dataFile = SystemKnowledge.getFile(SystemKnowledge.FIRE_SPREAD_LOGIC);
        str = (dataFile == null) ? "Default" : dataFile.toString();
        logOut.println("  Fire Spread           : " + str);

        dataFile = SystemKnowledge.getFile(SystemKnowledge.FIRE_TYPE_LOGIC);
        str = (dataFile == null) ? "Default" : dataFile.toString();
        logOut.println("  Type of Fire          : " + str);

        logOut.flush();
        logOut.close();

        Simpplle.setStatusMessage(Simpplle.endl + "Writing initial Simulation Data Files" + Simpplle.endl);

        writeInitialAreaFile();

      }

      if (outputFile != null && writeDatabase) {
        Simpplle.setStatusMessage("Creating Database");
        DatabaseCreator.initHibernate(true, getDatabasePath());
        writeDatabaseManagerBatFile();
        writeOpenOfficeBaseInstructions();
      }

      if (outputFile != null && writeAccess) {
        initAccessTreeMaps();
        openAccessTextFiles();
      }

      Area area = Simpplle.getCurrentArea();
      if ((area.hasRoads() || area.hasTrails()) && needNearestRoadTrailInfo()) {
        Simpplle.setStatusMessage("Calculating Nearest Roads/Trails");
      }
      if (area.hasRoads()) {
        Evu.findRoadUnits();
      }
      if (area.hasTrails()) {
        Evu.findTrailUnits();
      }

      if (simpplle.JSimpplle.invasiveSpeciesMSUProbFile() && outputFile != null) {
        File path = new File(outputFile + "-invasiveprob.txt");
        invasiveSpeciesMSUProbOut = new PrintWriter(new FileWriter(path, true));
        invasiveSpeciesMSUProbOut.println("Species,Evu ID,Time Step,Intercept,Aspect,elev,elevResult,slope,slopeResult,cosasp,cosaspResult,sinasp,sinaspResult,annrad,andradResult,distroad,distroadResult,disttrail,disttrailResult,process value,treatment value,shrub,shrubResult,grass,grassResult,tree,treeResult, prob");
      }

      if (numSimulations > 1) {
//        copyHsqldb();
        doMultipleRun();
        writePrefixFileMultipleRun();
      } else {
        doFuture();
        if (outputFile != null) {
          writePrefixFileSingleRun();
        }
      }

      if (outputFile != null && writeDatabase) {
        DatabaseCreator.closeHibernate();
      }
      if (outputFile != null && writeAccess) {
        writeAccessSlinkMetrics();
        writeAccessTreeMaps();
        closeAccessTextFiles();
      }

      if (simpplle.JSimpplle.invasiveSpeciesMSUProbFile() && outputFile != null) {
        invasiveSpeciesMSUProbOut.flush();
        invasiveSpeciesMSUProbOut.close();
      }

      if (doSimLoggingFile) {
        simLoggingWriter.flush();
        simLoggingWriter.close();
      }
    } catch (Exception err) {
      throw new SimpplleError("The following Runtime Exception occurred:\n" + err.getMessage(),err);
    } finally {
      inSimulation = false;
    }
  }

  void writeAccessSlinkMetrics() {

    PrintWriter out = Simulation.getInstance().getAccessSlinkMetricsOut();

    Evu[] allEvu = Simpplle.currentArea.getAllEvu();

    for (Evu evu : allEvu) {

      if (evu == null) continue;

      int    slink       = evu.getId();
      float  acres       = evu.getFloatAcres();
      String ecoGroup    = evu.getHabitatTypeGroup().getName();
      String ownership   = evu.getOwnership();
      String specialArea = evu.getSpecialArea();
      String fmz         = evu.getFmz().getName();

      out.printf("%d,%f,%s,%s,%s,%s%n",slink,acres,ecoGroup,ownership,specialArea,fmz);

    }
  }

  private TreeMap<Short,String> accessProcessList        = new TreeMap<>();
  private TreeMap<Short,String> accessSpeciesList        = new TreeMap<>();
  private TreeMap<Short,String> accessSizeClassList      = new TreeMap<>();
  private TreeMap<Short,String> accessDensityList        = new TreeMap<>();
  private TreeMap<Short,String> accessEcoGroupList       = new TreeMap<>();
  private TreeMap<Short,String> accessFmzList            = new TreeMap<>();
  private TreeMap<Short,String> accessIncRuleSpeciesList = new TreeMap<>();
  private TreeMap<Short,String> accessLifeformList       = new TreeMap<>();
  private TreeMap<Short,String> accessOnwershipList      = new TreeMap<>();
  private TreeMap<Short,String> accessSpecialAreaList    = new TreeMap<>();
  private TreeMap<Short,String> accessTreatmentTypeList  = new TreeMap<>();

  private void initAccessTreeMaps() {

    accessProcessList.clear();
    accessSpeciesList.clear();
    accessSizeClassList.clear();
    accessDensityList.clear();
    accessEcoGroupList.clear();
    accessFmzList.clear();
    accessIncRuleSpeciesList.clear();
    accessLifeformList.clear();
    accessOnwershipList.clear();
    accessSpecialAreaList.clear();
    accessTreatmentTypeList.clear();

  }

  private void writeAccessTreeMaps() throws IOException {

    writeAccessTreeMap(accessProcessOut,accessProcessList);
    writeAccessTreeMap(accessSpeciesOut,accessSpeciesList);
    writeAccessTreeMap(accessSizeClassOut,accessSizeClassList);
    writeAccessTreeMap(accessDensityOut,accessDensityList);
    //writeAccessTreeMap(accessEcoGroupOut,accessEcoGroupList);
    //writeAccessTreeMap(accessFmzOut,accessFmzList);
    writeAccessTreeMap(accessTrackingSpeciesOut,accessIncRuleSpeciesList);
    writeAccessTreeMap(accessLifeformOut,accessLifeformList);
    writeAccessTreeMap(accessOwnershipOut,accessOnwershipList);
    writeAccessTreeMap(accessSpecialAreaOut,accessSpecialAreaList);
    writeAccessTreeMap(accessTreatmentOut, accessTreatmentTypeList);

  }

  private void writeAccessTreeMap(PrintWriter fout, TreeMap<Short,String> map) throws IOException {
    for (Short id : map.keySet()) {
      String value = map.get(id);

      fout.printf("%d,%s%n", id, value);
    }
    fout.flush();
  }

  public void addAccessProcess(ProcessType process) {
    accessProcessList.put(process.getSimId(), process.toString());
  }

  public void addAccessSpecies(Species species) {
    accessSpeciesList.put(species.getSimId(), species.toString());
  }

  public void addAccessSizeClass(SizeClass sizeClass) {
    accessSizeClassList.put(sizeClass.getSimId(), sizeClass.toString());
  }

  public void addAccessDensity(Density density) {
    accessDensityList.put(density.getSimId(), density.toString());
  }

  public void addAccessEcoGroup(HabitatTypeGroupType ecoGroup) {
    accessEcoGroupList.put(ecoGroup.getSimId(), ecoGroup.toString());
  }

  public void addAccessFmz(Fmz fmz) {
    accessFmzList.put(fmz.getSimId(), fmz.toString());
  }

  public void addAccessIncRuleSpecies(InclusionRuleSpecies species) {
    accessIncRuleSpeciesList.put(species.getSimId(), species.toString());
  }

  public void addAccessLifeform(Lifeform lifeform) {
    accessLifeformList.put(lifeform.getSimId(), lifeform.toString());
  }

  public void addAccessOwnership(Ownership ownership) {
    accessOnwershipList.put(ownership.getSimId(), ownership.toString());
  }

  public void addAccessSpecialArea(SpecialArea specialArea) {
    accessSpecialAreaList.put(specialArea.getSimId(), specialArea.toString());
  }

  public void addAccessTreatment(TreatmentType treatmentType) {
    accessTreatmentTypeList.put(treatmentType.getSimId(),treatmentType.toString());
  }

  public PrintWriter getAccessEvuSimDataOut() {
    return accessEvuSimDataOut[currentRun];
  }

  public PrintWriter getAccessTrackingSpeciesOut() {
    return accessTrackingSpeciesOut;
  }

  public PrintWriter getAccessSlinkMetricsOut() {
    return accessSlinkMetricsOut;
  }

  private void openAccessTextFiles() throws SimpplleError, IOException {
    makeAccessFilesDir();

    File path;

    for (int run=0; run<numSimulations; run++) {
      path = new File (getAccessFilesPath(),"EVU_SIM_DATA" + Integer.toString(run+1) + ".txt");
      //GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(path));
      //accessEvuSimDataOut[run] = new PrintWriter(out);
      accessEvuSimDataOut[run] = new PrintWriter(new FileWriter(path, true));
      accessEvuSimDataOut[run].println("RUN,TIMESTEP,SEASON_ID,SLINK,ACRES,LIFEFORM_ID,SPECIES_ID,SIZECLASS_ID,AGE,DENSITY_ID,PROCESS_ID,PROB,PROBSTR,OWNERSHIP_ID,SPECIAL_AREA_ID");
    }

    path = new File (getAccessFilesPath(),"PROCESS.txt");
    accessProcessOut = new PrintWriter(new FileWriter(path, true));
    accessProcessOut.println("ID,PROCESS");

    path = new File (getAccessFilesPath(),"SPECIES.txt");
    accessSpeciesOut = new PrintWriter(new FileWriter(path, true));
    accessSpeciesOut.println("ID,SPECIES");

    path = new File (getAccessFilesPath(),"SIZECLASS.txt");
    accessSizeClassOut = new PrintWriter(new FileWriter(path, true));
    accessSizeClassOut.println("ID,SIZECLASS");

    path = new File (getAccessFilesPath(),"DENSITY.txt");
    accessDensityOut = new PrintWriter(new FileWriter(path, true));
    accessDensityOut.println("ID,DENSITY");

    //path = new File (getAccessFilesPath(),"ECOGROUP.txt");
    //accessEcoGroupOut = new PrintWriter(new FileWriter(path, true));
    //accessEcoGroupOut.println("ID,ECOGROUP");

    //for (int run=0; run<numSimulations; run++) {
    //  path = new File (getAccessFilesPath(),"AREASUMMARY" + Integer.toString(run+1) + ".txt");
    //  accessAreaSummaryOut[run] = new PrintWriter(new FileWriter(path, true));
    //  accessAreaSummaryOut[run].println("RUN,TIMESTEP,ORIGINUNITID,UNITID,TOUNITID,PROCESS_ID,PROB,ACRES,SEASON_ID,GROUP_ID,OWNERSHIP_ID,SPECIAL_AREA_ID,FMZ_ID");
    //}

    //path = new File (getAccessFilesPath(),"FMZ.txt");
    //accessFmzOut = new PrintWriter(new FileWriter(path, true));
    //accessFmzOut.println("ID,FMZNAME");

    path = new File (getAccessFilesPath(),"TRACKSPECIES.txt");
    accessInclusionRuleSpecies = new PrintWriter(new FileWriter(path, true));
    accessInclusionRuleSpecies.println("ID,INCSPECIES");

    path = new File (getAccessFilesPath(),"LIFEFORM.txt");
    accessLifeformOut = new PrintWriter(new FileWriter(path, true));
    accessLifeformOut.println("ID,LIFEFORM");

    path = new File (getAccessFilesPath(),"OWNERSHIP.txt");
    accessOwnershipOut = new PrintWriter(new FileWriter(path, true));
    accessOwnershipOut.println("ID,OWNERSHIP");

    path = new File (getAccessFilesPath(),"SPECIALAREA.txt");
    accessSpecialAreaOut = new PrintWriter(new FileWriter(path, true));
    accessSpecialAreaOut.println("ID,SPCAREA");

    path = new File (getAccessFilesPath(),"TRACKINGSPECIESPCT.txt");
    accessTrackingSpeciesOut = new PrintWriter(new FileWriter(path, true));
    accessTrackingSpeciesOut.println("RUN,TIMESTEP,SLINK,LIFEFORM_ID,SPECIES_ID,PCT");

    path = new File (getAccessFilesPath(),"SLINKMETRICS.txt");
    accessSlinkMetricsOut = new PrintWriter(new FileWriter(path, true));
    accessSlinkMetricsOut.println("SLINK,ACRES,ECOGROUP,OWNERSHIP,SPECIALAREA,FMZ");

    path = new File (getAccessFilesPath(),"TREATMENT.txt");
    accessTreatmentOut = new PrintWriter(new FileWriter(path,true));
    accessTreatmentOut.println("ID,TREATMENT");

  }

  private void closeAccessTextFiles() throws SimpplleError {

    for (int run=0; run<numSimulations; run++) {
      accessEvuSimDataOut[run].flush();
      accessEvuSimDataOut[run].close();
    }

    accessProcessOut.flush();
    accessProcessOut.close();

    accessSpeciesOut.flush();
    accessSpeciesOut.close();

    accessSizeClassOut.flush();
    accessSizeClassOut.close();

    accessDensityOut.flush();
    accessDensityOut.close();

    //accessEcoGroupOut.flush();
    //accessEcoGroupOut.close();

    //for (int run=0; run<numSimulations; run++) {
    //  accessAreaSummaryOut[run].flush();
    //  accessAreaSummaryOut[run].close();
    //}

    //accessFmzOut.flush();
    //accessFmzOut.close();

    accessInclusionRuleSpecies.flush();
    accessInclusionRuleSpecies.close();

    accessLifeformOut.flush();
    accessLifeformOut.close();

    accessOwnershipOut.flush();
    accessOwnershipOut.close();

    accessSpecialAreaOut.flush();
    accessSpecialAreaOut.close();

    accessTrackingSpeciesOut.flush();
    accessTrackingSpeciesOut.close();

    accessSlinkMetricsOut.flush();
    accessSlinkMetricsOut.close();

    accessTreatmentOut.flush();
    accessTreatmentOut.close();

  }

  public PrintWriter getInvasiveSpeciesMSUPrintWriter() {
    if (outputFile == null) {
      return null;
    }
    return invasiveSpeciesMSUProbOut;
  }

  private void writePrefixFileMultipleRun() throws SimpplleError {
    RegionalZone zone = Simpplle.getCurrentZone();
    File         prefixFile=null;
    PrintWriter  fout;

    try {
      prefixFile = Utility.makeSuffixedPathname(outputFile,"","minfo");
      fout = new PrintWriter(new FileWriter(prefixFile));
    } catch (IOException err) {
      throw new SimpplleError("Problem writing " + prefixFile + " file.");
    }

    fout.println("PREFIX," + "\"" + outputFile.getName() + "\"");

    String dir = System.getProperty("user.dir");
    File   file = new File(dir,zone.getArcviewDir());
    fout.println("LEGEND_DIR," + "\"" + file.toString() + "\"");

    fout.println("PROBABILITY");
    fout.println("  NAME,\"PROBABILITY\"");
    fout.println("  TIMESTEPS,0," + getNumTimeSteps());
    fout.println("  ALL_PROB_TIMESTEP," + Area.ALL_PROB_STEP);
    fout.println("  TYPES,sim_species,sim_size,sim_canopy,sim_process");

    String[] typeNames = new String[SimpplleType.MAX];
    int[]    types = new int[] {SimpplleType.SPECIES.ordinal(), SimpplleType.SIZE_CLASS.ordinal(),
                                SimpplleType.DENSITY.ordinal(), SimpplleType.PROCESS.ordinal()};

    typeNames[SimpplleType.SPECIES.ordinal()]    = "species";
    typeNames[SimpplleType.SIZE_CLASS.ordinal()] = "size";
    typeNames[SimpplleType.DENSITY.ordinal()]    = "canopy";
    typeNames[SimpplleType.PROCESS.ordinal()]    = "process";

    int i,j;
    Area area = Simpplle.getCurrentArea();
    SimpplleType[][] allAttributes = getMultipleRunSummary().getAllAttributes();

    for(i=0; i<types.length; i++) {
      fout.println("  " + typeNames[types[i]]);
      fout.println("    FILE_SUFFIX,\"-#-" + typeNames[types[i]] + "\"");
      fout.println("    FILE_EXT,\"txt\"");
      fout.println("    DATA_START,2");
      fout.print("    FIELDS,Slink");
      for(j=0; j<allAttributes[types[i]].length; j++) {
        fout.print("," + allAttributes[types[i]][j].getGISPrintName());
      }
      fout.println();
      fout.print("    FIELD_TYPES,LONG");
      for(j=0; j<allAttributes[types[i]].length; j++) {
        fout.print(",INTEGER");
      }
      fout.println();
      fout.println("  END");
    }
    fout.println("END");

    fout.flush();
    fout.close();
  }

  private void writePrefixFileSingleRun() throws SimpplleError {
    File tmpOutputFile = outputFile;
    if (Simulation.getInstance().isMultipleRun()) {
      String dir = tmpOutputFile.getParent();
      String name = tmpOutputFile.getName();
      File newDir = new File(dir, "gis_run" + (Simulation.getCurrentRun()+1));
      if (!newDir.exists() && !newDir.mkdir()) {
        throw new SimpplleError("Unable to create directory: " + newDir.toString());
      }
      tmpOutputFile = new File(newDir, name);
    }

    if (Area.multipleLifeformsEnabled()) {
      Lifeform[] lives = Lifeform.getAllValues();
      for (int i=0; i<lives.length; i++) {
        File tmpFile = Area.createLifeformOutputFile(tmpOutputFile,lives[i],false);
        writePrefixFileSingleRun(tmpFile,false);
      }
      File tmpFile = Area.createLifeformOutputFile(tmpOutputFile,null,true);
      writePrefixFileSingleRun(tmpFile,false);
    }
    writePrefixFileSingleRun(tmpOutputFile,true);
  }

  public void writePrefixFileSingleRun(File outfile, boolean doSpreadSection) throws SimpplleError {
    if (outfile == null) { return; }

    RegionalZone zone = Simpplle.getCurrentZone();
    File         prefixFile=null;
    PrintWriter  fout;

    try {
      prefixFile = Utility.makeSuffixedPathname(outfile,"","sinfo");
      fout = new PrintWriter(new FileWriter(prefixFile));
    } catch (IOException err) {
      throw new SimpplleError("Problem writing " + prefixFile + " file.");
    }

    fout.println("PREFIX," + "\"" + outfile.getName() + "\"");

    String dir = System.getProperty("user.dir");
    File   file = new File(dir,zone.getArcviewDir());
    fout.println("LEGEND_DIR," + "\"" + file.toString() + "\"");

    fout.println("UPDATE");
    fout.println("  NAME,\"Update\"");
    fout.println("  TIMESTEPS,0," + getNumTimeSteps());
    fout.println("  FILE_SUFFIX," + "\"-#-update\"");
    fout.println("  FILE_EXT," + "\"txt\"");
    fout.println("  DATA_START,2");
    fout.println("  FIELDS,Slink,sim_Species,sim_Size,sim_Canopy,sim_Process,sim_Treatment,sim_Probability");
    fout.println("  FIELD_TYPES,LONG,STRING,STRING,STRING,STRING,STRING,FLOAT");
    fout.println("END");

    if (doSpreadSection) {
      fout.println("SPREAD");
      fout.println("  NAME,\"Spread\"");
      fout.println("  TIMESTEPS,1," + getNumTimeSteps());
      fout.println("  FILE_SUFFIX," + "\"-#-spread\"");
      fout.println("  FILE_EXT," + "\"txt\"");
      fout.println("  DATA_START,2");
      fout.print("  FIELDS,Slink");
      ProcessType[] summaryProcesses = Process.getSummaryProcesses();
      int i;
      for (i = 0; i < summaryProcesses.length; i++) {
        fout.print("," + summaryProcesses[i].getGISPrintName());
      }
      fout.println();
      fout.print("  FIELD_TYPES,LONG");
      for (i = 0; i < summaryProcesses.length; i++) {
        fout.print("," + "STRING");
      }
      fout.println();
      fout.println("END");
    }
    fout.flush();
    fout.close();
  }

  /**
   * Will generate a random number between 0 and 10000 (exclusive).  This is of extreme importance in simulation methodologies.
   * @return the random number
   */
  public int random() {
    return random.nextInt(maxProbability);
  }

  /**
   * Since wyoming uses yearly time steps, seasonal variation are important.  If it is succession probability, spring is the last season, otherwise winter is used.
   * @return
   */
  public boolean isLastSeason() {
    if (RegionalZone.isWyoming()) {
      Climate.Season lastSeason = isStandDevelopment() ?
                                  Climate.Season.SPRING : Climate.Season.WINTER;

      return currentSeason == lastSeason;
    }
    return true;
  }

  /**
   * Calculates free memory
   * @return
   */
  public static String getMemoryString() {

    long freeMem = Runtime.getRuntime().freeMemory();
    long totMem  = Runtime.getRuntime().totalMemory();
    long maxMem  = Runtime.getRuntime().maxMemory();
    long usedMem = ((totMem-freeMem) / 1024) / 1024;

    maxMem = (maxMem / 1024) / 1024;

    return usedMem + "MB/" + maxMem;

  }

  public void doAllStatesSummaryAllTimeSteps(File rulesFile) throws SimpplleError {
    if (isDiscardData()) { return; }

    areaSummary.initializeAllStateReportSummary(rulesFile);
    areaSummary.clearAllStatesReportSummaryData();
    for (int i=0; i<=numTimeSteps; i++) {
      areaSummary.updateAllStatesReportSummary(i);
    }
  }

  public void doTrackingSpeciesReportAllTimeSteps() throws SimpplleError {
    if (isDiscardData()) { return; }

    areaSummary.clearTrackingSpeciesReportSummaryData();
    for (int i=0; i<=numTimeSteps; i++) {
      areaSummary.updateTrackingSpeciesReportSummary(i);
    }
  }

  /**
   * Called by runSimulation. Used to simulate
   * the current area into the future.
   */
  private void doFuture() throws SimpplleError {

    //Use of Fixed Seed
    if (fixedSeed) {
      random = new Random(seed);
    } else {
      random = new Random();
    }

    Area currentArea = Simpplle.currentArea;
    // 1. Create a landscape.
    // 2. Initialize spread to slots.

    currentTimeStep = 0;
    currentArea.initSimulation();
    areaSummary = new AreaSummary();

    if (needNearestRoadTrailInfo()) {
      currentArea.findNearestRoadsTrails();
    }

    // Note this method will handle the case of null.
    if (doAllStatesSummary) {
      if (currentRun == 0) {
        areaSummary.initializeAllStateReportSummary(allStatesRulesFile);
      }
      areaSummary.clearAllStatesReportSummaryData();
      areaSummary.updateAllStatesReportSummary(0);
    }

    if (doTrackingSpeciesReport) {
      areaSummary.clearTrackingSpeciesReportSummaryData();
      areaSummary.updateTrackingSpeciesReportSummary(0);
    }

    areaSummary.updateEvuSummaryData();

    if (outputFile != null) {
      areaSummary.fireSpreadReportHeader(getFireSpreadReportPath());
      if (doGisFiles) {
        // Note this will also make the call to write the sinfo files.
        currentArea.produceArcFiles(outputFile);
        if (isMultipleRun()) {
          writePrefixFileSingleRun();
        }
      }
    }

    // ~~~~~~~~~~ Write time step zero before this comment ~~~~~~~~~

    try {
      for(int i=0;i<numTimeSteps;i++) {
        currentTimeStep++;
        if (currentTimeStep > 1) {
          areaSummary.doBeginTimeStepInitialize();
        }
        String msg = "Project Area for Time Step: " + (i+1) + " Run #" + (currentRun+1) + " Mem: " + getMemoryString();
        Simpplle.setStatusMessage(msg);
        if (FireEvent.useRegenPulse()) { FireEvent.setRegenPulse(); }

        if (RegionalZone.isWyoming()) {
          Climate.Season lastSeason = isStandDevelopment() ? Climate.Season.SPRING : Climate.Season.WINTER;
          for (Climate.Season s : Climate.allSeasons) {
            currentSeason = s;
            currentArea.doFuture();
            if (s == lastSeason) { break; }
          }
        } else {
          currentSeason = Climate.Season.YEAR;
          currentArea.doFuture();
        }

        areaSummary.updateEvuSummaryData();
        areaSummary.updateTotalFireEventAcres();
        areaSummary.doFireEventSummary(currentTimeStep);
        areaSummary.cleanup();

        if (doAllStatesSummary) {
          areaSummary.updateAllStatesReportSummary();
        }

        if (doTrackingSpeciesReport) {
          areaSummary.updateTrackingSpeciesReportSummary();
        }

        if (outputFile != null) {
          areaSummary.fireSpreadReportUpdate(getFireSpreadReportPath());
        }
        if (outputFile != null && writeDatabase) {
          Simpplle.setStatusMessage("Writing Area Summary Data to database");
          areaSummary.writeDatabase();
          Simpplle.clearStatusMessage();
        }
        if (fireSuppression()) {
          areaSummary.doSuppressionCosts(currentTimeStep);
        }
        if (outputFile != null && doGisFiles) {
          currentArea.produceSpreadArcFiles(outputFile,false);
          currentArea.produceArcFiles(outputFile);
        }
      }

      if (fireSuppression()) {
        areaSummary.doSuppressionCostsFinal();
      }

      if (outputFile != null) {
        areaSummary.fireSpreadReportFinish(getFireSpreadReportPath());
      }
      if (outputFile != null && !isMultipleRun()) {
        save();
      }
      if (doAllStatesSummary) {
        if (outputFile != null) {
          Reports.generateAllStatesReport(getOutputFile());
        }
      }

      if (doTrackingSpeciesReport) {
        if (outputFile != null) {
          Reports.generateTrackingSpeciesReport(getOutputFile());
        }
      }

    } catch (SimpplleError err) {
      currentArea.resetTreatmentSchedule();
      Simpplle.clearStatusMessage();
      throw err;
    }

    currentArea.resetTreatmentSchedule();

    // Manually clean things up until simpplle in made more efficient.
    if (currentArea.doManualGC() == false) { System.gc(); }

  }

  public File getDatabasePath() {
    File newDir = new File(outputFile+"db");
    File path = new File(newDir,outputFile.getName()+"db");
    return path;
  }

  public File getSimFilePath() {
    File path = new File(outputFile+".simdata_bin");
    return path;
  }

//  public File getAllStatesFilePath() {
//    File path = new File(outputFile+"-allstates-"+Integer.toString(currentRun+1)+".txt");
//    File path = new File(outputFile+"-allstates.txt");
//    return path;
//  }

  /**
   * Gets the fire spread report filepath.  This is a .txt file.
   * @return
   */
  public File getFireSpreadReportPath() {
    File path = new File(outputFile+"-firespread-"+Integer.toString(currentRun+1)+".txt");
    return path;
  }

  public File getAccessFilesPath() throws SimpplleError {
    File path = new File(outputFile.getParent(),"textdata");
    return path;
  }

  public void makeAccessFilesDir() throws SimpplleError {
    File outputDir = new File(outputFile.getParent(),"textdata");
    if (!outputDir.mkdir()) {
      throw new SimpplleError("Unable to create necessary output directory" + outputDir);
    }
  }

  /**
   * Does a multiple runs simulation.
   * @throws SimpplleError
   */
  private void doMultipleRun() throws SimpplleError {
    Area currentArea = Simpplle.currentArea;
    String msg;

    currentRun = 0;

    multipleRunSummary = new MultipleRunSummary(this);
    currentArea.initMultipleSimulation();

    for(int i=0;i<numSimulations;i++) {

      msg = Simpplle.endl + "Performing Simulation #" + (i+1) + Simpplle.endl + Simpplle.endl;
      Simpplle.setStatusMessage(msg);

      doFuture();  // Run a simulation.

      // Update Area Summary data.
      if (fireSuppression()) {
        multipleRunSummary.updateFireSuppressionCostSummary(discount);
      }
      currentArea.updateSummaries(multipleRunSummary);
      multipleRunSummary.finishEmissionsSummary();
      multipleRunSummary.computeFrequencies();
      save();
      currentRun++;
    }

    // Compute Statistical Information.
    currentArea.initializeSpecialLists(this,multipleRunSummary);
    multipleRunSummary.calculateStatistics();

    // Output the Automatically generated reports.
    multipleRunSummary.asciiSummaryReport(outputFile);
    if (trackSpecialArea()) {
      multipleRunSummary.asciiSpecialAreaSummaryReport(outputFile);
    }
    if (trackOwnership()) {
      multipleRunSummary.asciiOwnershipSummaryReport(outputFile);
    }

//    if (writeProbFiles) {
//      multipleRunSummary.calculateFrequency();
//    }

    if (writeProbFiles && doProbArcFiles) {
      currentArea.produceDecadeProbabilityArcFiles(outputFile);
      currentArea.produceProbabilityArcFiles(outputFile);
    }

    saveWithMrSummary();
//    DatabaseCreator.closeHibernate();
  }

  private void save() throws SimpplleError {
    save(outputFile);
  }

  private void save(File outfile) throws SimpplleError {
    File newFile;
    int cRun = getCurrentRun();

    Simpplle.setStatusMessage("Saving Results From Run " + (cRun + 1) + "...");

    newFile = Utility.makeNumberedPathname(Utility.stripExtension(outfile), cRun + 1, "simdata");
    try {
      simpplle.JSimpplle.getComcode().saveSimulation(newFile,cRun+1);
      Simpplle.clearStatusMessage();
    } catch (SimpplleError ex) {
      throw ex;
    }
  }

  private void saveWithMrSummary() throws SimpplleError {
    File newFile;
    try {
      Simpplle.setStatusMessage("Saving Multiple Run Summary Data ...");
      newFile = Utility.makeSuffixedPathname(Utility.stripExtension(outputFile),
                                             "-end","simdata");
      simpplle.JSimpplle.getComcode().saveSimulation(newFile,-1);
      Simpplle.clearStatusMessage();
    } catch (SimpplleError ex) {
      throw ex;
    }
  }

/*
  private void save(File outfile) throws SimpplleError {
    GZIPOutputStream out;
    PrintWriter      fout;
    String           dir, name, msg;
    File             newFile;
    int              cRun = getCurrentRun();

    if (isMultipleRun()) {
      msg = "Saving Results From Run " + (cRun + 1) + "...";
      Simpplle.setStatusMessage(msg);
      newFile = Utility.makeNumberedPathname(Utility.stripExtension(outfile),cRun + 1,"area");
    }
    else {
      Simpplle.setStatusMessage("Saving Results ...");
      newFile = outfile;
    }

    try {

//      out = new GZIPOutputStream(new FileOutputStream(outfile));
//      fout = new PrintWriter(out);
//      fout = new PrintWriter(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(outfile),1048576)),false);
//      fout = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
      // using 10k buffer size, with no autoflush
//      fout = new PrintWriter(new GZIPOutputStream(new FileOutputStream(outfile),10240));
      // Save The current Area
      Area currentArea = Simpplle.getCurrentArea();
      if (isMultipleRun()) {
        // Give the area a useful new name.
        String areaName = currentArea.getName();
        String newAreaName =
          currentArea.getName() + " (Run #" + (cRun+1) + " of " +
          getNumSimulations() + ")";
        currentArea.setName(newAreaName);
//        currentArea.saveArea(fout);
        simpplle.JSimpplle.getComcode().saveCurrentArea(newFile);
        currentArea.setName(areaName);
      }
      else {
//        currentArea.saveArea(fout);
        simpplle.JSimpplle.getComcode().saveCurrentArea(newFile);
      }

      // Save Simulation Data
//      saveSimulation(fout,cRun+1);

//      Simpplle.setStatusMessage("Finished Saving Units",true);
      // Save Area Summary
//      Simpplle.getAreaSummary().saveSimulation(fout);

//      Simpplle.setStatusMessage("Finished Saving Simulation data",true);
//      fout.flush();
//      out.close();
//      fout.close();

      Simpplle.clearStatusMessage();
    }
    catch (SimpplleError ex) {
      throw ex;
    }
//    catch (IOException IOX) {
//      System.out.println("Problems writing output file.");
//    }
  }
*/
  /**
   * Saves the simulation.
   * @param fout
   */
  public void saveSimulation(PrintWriter fout) {
    saveSimulation(fout,numSimulations);
  }

  private void saveSimulation(PrintWriter fout, int nRuns) {
    int num;

    fout.println("CLASS SIMULATION");
    fout.print(nRuns);
    fout.print(COMMA);
    fout.print(numTimeSteps);
    fout.print(COMMA);

    fout.print(discount);
    fout.print(COMMA);
    num = fireSuppression ? 1 : 0;
    fout.print(num);
    fout.print(COMMA);

    num = trackSpecialArea ? 1 : 0;
    fout.print(num);
    fout.print(COMMA);

    num = trackOwnership ? 1 : 0;
    fout.print(num);
    fout.println();
  }

  /**
   * Reads in simulation data.  This includes number of simulations, current run, num time steps, fire suppression discounted cost, booleans for
   * fire suppression, tracking special area and ownership, boolean for yearly or decade time steps, and area summary.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    int version = in.readInt();

    if (version == 1) {
      numSimulations = in.readInt();
      currentRun     = numSimulations-1;
    } else if (version > 1) {
      currentRun     = in.readInt();
      numSimulations = in.readInt();
    }

    numTimeSteps      = in.readInt();
    discount          = in.readFloat();
    fireSuppression   = in.readBoolean();
    trackSpecialArea  = in.readBoolean();
    trackOwnership    = in.readBoolean();
    yearlySteps       = in.readBoolean();
    areaSummary       = (AreaSummary)in.readObject();

    if (version > 3) {
      pastTimeStepsInMemory = in.readInt();
      simulationMethod      = in.readInt();
      writeDatabase         = in.readBoolean();
      writeProbFiles        = in.readBoolean();

      if (version > 4) {
        invasiveSpeciesKind = (InvasiveKind)in.readObject();
      } else {
        boolean invasive = in.readBoolean();
        invasiveSpeciesKind = (invasive ? InvasiveKind.MESA_VERDE_NP : InvasiveKind.NONE);
      }
      discardData           = in.readBoolean();
      doAllStatesSummary    = in.readBoolean();

      if (version > 5) {
        doTrackingSpeciesReport = in.readBoolean();
        doProbArcFiles          = in.readBoolean();
      }
    }

    currentTimeStep = numTimeSteps;
  }

  /**
   * Writes simulation data.  This includes number of simulations, current run, num time steps, fire suppression discounted cost, booleans for
   * fire suppression, tracking special area and ownership, boolean for yearly or decade time steps, and area summary.
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    out.writeInt(currentRun);
    out.writeInt(numSimulations);
    out.writeInt(numTimeSteps);
    out.writeFloat(discount);
    out.writeBoolean(fireSuppression);
    out.writeBoolean(trackSpecialArea);
    out.writeBoolean(trackOwnership);
    out.writeBoolean(yearlySteps);
    out.writeObject(areaSummary);
    out.writeInt(pastTimeStepsInMemory);
    out.writeInt(simulationMethod);
    out.writeBoolean(writeDatabase);
    out.writeBoolean(writeProbFiles);
    out.writeObject(invasiveSpeciesKind);
    out.writeBoolean(discardData);
    out.writeBoolean(doAllStatesSummary);
    out.writeBoolean(doTrackingSpeciesReport);
    out.writeBoolean(doProbArcFiles);
  }

  /**
   * Reads and parses simulation data.
   * @param fin
   * @throws ParseError
   * @throws IOException
   */
  public void readSimulation(BufferedReader fin) throws ParseError, IOException {
    String              line;
    StringTokenizerPlus strTok;
    int                 count, val;

    line = fin.readLine();
    strTok = new StringTokenizerPlus(line,",");
    count  = strTok.countTokens();

    if (count != 6) {
      throw new ParseError("Invalid Class Simluation data");
    }

    numSimulations = strTok.getIntToken();
    numTimeSteps  = strTok.getIntToken();
    discount      = strTok.getFloatToken();

    val             = strTok.getIntToken();
    fireSuppression = (val == 1) ? true : false;

    val              = strTok.getIntToken();
    trackSpecialArea = (val == 1) ? true : false;

    val            = strTok.getIntToken();
    trackOwnership = (val == 1) ? true : false;

    areaSummary   = new AreaSummary(this);
  }

  /**
   * Read the area-summary data from all of the simulation files
   * in a multiple-run simulation.  Thus we will be able to compute
   * stuff again, (in this case fire suppression cost).
   * @param simPrefix
   */
//  public void readAllSimulationFiles(File simPrefix) throws SimpplleError {
//    Vector      areaFiles = new Vector();
//    File        file;
//    int         r=0;
//    Area        area = Simpplle.getCurrentArea();
//
//    do {
//      r++;
//
//      file = Utility.makeSuffixedPathname(simPrefix,"-" + r,"area");
//      if (file != null && file.exists()) {
//        areaFiles.addElement(file);
//      }
//    }
//    while (file != null && file.exists());
//    int nRuns = r - 1;
//    numSimulations = nRuns;
//
//    trackSpecialArea   = true;
//    multipleRunSummary = new MultipleRunSummary(this);
//
//    Evu[] allEvu = Simpplle.getCurrentArea().getAllEvu();
//    if (trackSpecialArea()) {
//      for (int i=0; i<allEvu.length; i++) {
//        if (allEvu[i] != null) {
//          multipleRunSummary.updateAllSpecialArea(allEvu[i].getSpecialArea());
//        }
//      }
//    }
//
//    if (nRuns <= 0) {
//      throw new SimpplleError("Could not find any simulation files");
//    }
//
//    try {
//      for (r=0; r<nRuns; r++) {
//        file = (File)areaFiles.elementAt(r);
//        currentRun = r;
//        if (file == null) {
//          throw new SimpplleError("File \"" + simPrefix + "-" + (r+1) + ".area\"" +
//                                  "does not exist");
//        }
//        Simpplle.setStatusMessage("Reading Area Summary from: " + file.toString());
//        area.readAreaSummary(file,this);
//        numSimulations = nRuns;  // gets reset to 1 when readSimulation called.
//        Simpplle.setStatusMessage("Calculating Fire Suppression Costs...");
//        areaSummary.doSuppressionCosts();
//        multipleRunSummary.updateFireSuppressionCostSummary(discount,this);
//      }
//      Simpplle.clearStatusMessage();
//    }
//    catch (Exception err) {
//      err.printStackTrace();
//      throw new SimpplleError("Error reading a simulation file:" + err.getMessage());
//    }
//  }

  /**
   * Used to write the area and Units to the output files for a
   * multiple run simulation.  This part of the files is always the same,
   * so this method writes the first one and copies the file to make the
   * rest of the files.
   * @throws SimpplleError
   */
  private void writeInitialAreaFile() throws SimpplleError {
    GZIPInputStream gin = null;
    BufferedInputStream fin = null;
    int data;

    int cRun = getCurrentRun();

    String dir = outputFile.getParent();
    String name = outputFile.getName();

    File outputDir = new File(dir,name + "-simulation");
    if (!outputDir.mkdir()) {
      throw new SimpplleError("Unable to create necessary output directory" + outputDir);
    }

    outputFile = new File(Utility.makePathname(outputDir.toString(), name));

    File newFile = Utility.makeSuffixedPathname(Utility.stripExtension(outputFile), "", "area");
    simpplle.JSimpplle.getComcode().saveCurrentArea(newFile,false);
  }

  public static File findSimulationAreaFile (File datafile) {
    String str = Utility.stripExtension(datafile).getName();
    int    index = str.lastIndexOf("-");
    String name = str.substring(0,index) + ".area";

    return new File(datafile.getParent(),name);
  }

  /**
   * Finds the simulation files.  Simulation files end in simdata, area files end in .area
   * @param dir
   * @return
   */
  public static ArrayList findSimulationDataFiles(File dir) {
    File[] files = dir.listFiles();
    if (files == null) { return null; }

    String ext;
    ArrayList simfiles = new ArrayList();
    boolean areaFound = false, simdataFound = false;
    for (int i=0; i<files.length; i++) {
      ext = Utility.getFileExtension(files[i]);
      if (ext == null) { continue; }

      if (ext.equalsIgnoreCase("simdata")) {
        simfiles.add(files[i]);
      }
    }
    if (simfiles.size() == 0) { return null; }

    String str = Utility.stripExtension((File)simfiles.get(0)).getName();
    int    index = str.lastIndexOf("-");
    String name = str.substring(0,index) + ".area";
    for(int i=0; i<files.length; i++) {
      if (files[i].getName().equals(name)) {
        simfiles.add(0,files[i]);
        return simfiles;
      }
    }

    return null;
  }

  public static String getDataFileDescription(File file) {
    String str = Utility.stripExtension(file).getName();
    int    index = str.lastIndexOf("-");
    String name = str.substring(0,index);
    String run  = str.substring(index+1);
    return name + " Run #" + run;
  }

//  public void copyHsqldb() throws SimpplleError {
//    SystemKnowledge.copyDummyDatabaseFile(outputFile.getParent(),outputFile.getName(),"script");
//    SystemKnowledge.copyDummyDatabaseFile(outputFile.getParent(),outputFile.getName(),"properties");
//    writeDatabaseManagerBatFile();
//  }

  private void writeDatabaseManagerBatFile() {
    File batFile = new File(outputFile.getParent(),"viewdata.bat");
    try {
      PrintWriter out = new PrintWriter(batFile);
      File dbPath = getDatabasePath();
      File jre = new File(System.getProperty("java.home"),"/bin/java");
      File hsqldbJar = new File(simpplle.JSimpplle.getInstallDirectory(),"lib/hsqldb.jar");
      out.println(jre + " -Xmx1200M -classpath " + hsqldbJar +
                  " org.hsqldb.util.DatabaseManagerSwing -url jdbc:hsqldb:file:" + dbPath);
      out.flush();
      out.close();
    }
    catch (FileNotFoundException ex) {
    }
  }

  /**
   * OpenOfficeBase_To_SIMPPLLE_Database_Instructions.txt
   */
  private void writeOpenOfficeBaseInstructions() {
    File batFile = new File(outputFile.getParent(),"OpenOfficeBase_To_SIMPPLLE_Database_Instructions.txt");
    try {
      PrintWriter out = new PrintWriter(batFile);
      File dbPath = getDatabasePath();

      String url = "hsqldb:file:" + dbPath;

      out.println("1. Launch OpenOffice Writer");
      out.println();
      out.println("2. Click on \"Options\" under the \"Tools\" Menu.");
      out.println();
      out.println("3. Under OpenOffice.org click on \"Java\"");
      out.println();
      out.println("4. Click on the \"Class Path\" button.");
      out.println();
      out.println("5. Make sure the \"hsqldb.jar\" archive is listed.  (If not:)");
      out.println();
      out.println("     1.Click on Add Archive");
      out.println();
      out.println("2. Navigate to the directory in the OpenOffice installation folder that contains this file.");
      out.println();
      out.println("3. On a Windows XP computer this probably is: \"C:\\Program Files\\OpenOffice.org 3\\Basis\\program\\classes.\"");
      out.println();
      out.println("4. In any case once in the installation directory the file is located in \"Basis\\program\\classes\"");
      out.println();
      out.println("5. Select \"hsqldb.jar\"");
      out.println();
      out.println("6. Click Open");
      out.println();
      out.println("7. Click Ok");
      out.println();
      out.println("6. Continue Clicking \"Ok\" until out of the Options dialog.");
      out.println();
      out.println("7. You will need to close the application in order for these changes to take effect.  It is very important to make sure the OpenOffice Quick Launch App in the Windows System Tray (near the clock) is closed.  Right click and select exit.");
      out.println();
      out.println("8. Start up OpenOffice Base");
      out.println();
      out.println("9. Click on \"Connect to existing database\" in the Database Wizard.");
      out.println();
      out.println("10. Make sure the database is JDBC (which should be the default)");
      out.println();
      out.println("11. Click on \"Next\"");
      out.println();
      out.println("12. In the URL Field copy the following and paste after \"jdbc:\"");
      out.println();
      out.println("     " + url + ";default_schema=true");
      out.println();
      out.println("13. In the JDBC driver class copy this: (make sure \"D\" is capitalized)");
      out.println();
      out.println("     org.hsqldb.jdbcDriver");
      out.println();
      out.println("14. Click on Test class.  It should say \"The JDBC Driver was loaded Successfully\"  If it does not start over any make sure the hsqldb.jar archive was correctly added in the options.  Also make sure all applications (including the sys tray app) are closed.");
      out.println();
      out.println("15. Click Next");
      out.println();
      out.println("16. There is no user name.  Here you can also test the connection to the database. Click Next.");
      out.println();
      out.println("17. Click Finish.");
      out.println();
      out.println("18. Give the database a name and continue.");

      out.flush();
      out.close();
    } catch (FileNotFoundException ex) { }

  }

  public void recreateMultipleRunSummary() {
    multipleRunSummary = new MultipleRunSummary(this);

    Evu[] allEvu = Simpplle.getCurrentArea().getAllEvu();
    if (trackSpecialArea() || trackOwnership()) {
      for (int i=0; i<allEvu.length; i++) {
        if (allEvu[i] != null) {
          if (trackSpecialArea()) {
            multipleRunSummary.updateAllSpecialArea(allEvu[i].getSpecialArea());
          }
          if (trackOwnership()) {
            multipleRunSummary.updateAllSpecialArea(allEvu[i].getOwnership());
          }
        }
      }
    }

    // Restore all data with all possibles
    Vector v= HabitatTypeGroup.getValidSpecies();
    for (Object elem : v) {
      multipleRunSummary.updateAll((SimpplleType)elem,SimpplleType.SPECIES.ordinal());
    }

    v= HabitatTypeGroup.getValidSizeClass();
    for (Object elem : v) {
      multipleRunSummary.updateAll((SimpplleType)elem,SimpplleType.SIZE_CLASS.ordinal());
    }

    v= HabitatTypeGroup.getValidDensity();
    for (Object elem : v) {
      multipleRunSummary.updateAll((SimpplleType)elem,SimpplleType.DENSITY.ordinal());
    }

    ArrayList list = Process.getSimulationProcesses();
    for (Object elem : list) {
      multipleRunSummary.updateAll((SimpplleType)elem,SimpplleType.SPECIES.ordinal());
    }
  }

  public void restoreOutfile(File simFile) {
    File dir = simFile.getParentFile();
    String name = simFile.getName();
    int index = name.lastIndexOf('-');
    name = name.substring(0,index);

    outputFile = new File(dir,name);
  }

  public void saveMultipleRunSummary(ObjectOutputStream s)
    throws SimpplleError, IOException
  {
    s.writeObject(multipleRunSummary);
  }

  public void readMultipleRunSummary(ObjectInputStream s)
    throws ClassNotFoundException, IOException
  {
    multipleRunSummary = (MultipleRunSummary) s.readObject();
  }

  public boolean getWriteDatabase() {
    return writeDatabase;
  }

  public boolean getWriteAccess() {
    return writeAccess;
  }

  public boolean isDiscardData() {
    return discardData;
  }

  public boolean isDoProbArcFiles() {
    return doProbArcFiles;
  }

  public boolean isDoAllStatesSummary() {
    return doAllStatesSummary;
  }

  /**
   *  Gets the boolean for tracking species report
   * @return true if simulation is to do tracking species report.
   */
  public boolean isDoTrackingSpeciesReport() {
    return doTrackingSpeciesReport;
  }

  public simpplle.comcode.Simulation.InvasiveKind getInvasiveSpeciesKind() {
    return invasiveSpeciesKind;
  }

  public boolean isAllStatesRulesFile() {
    return allStatesRulesFile != null;
  }

  public PrintWriter getSimLoggingWriter() {
    return simLoggingWriter;
  }

  public boolean isDoSimLoggingFile() {
    return doSimLoggingFile;
  }
}




