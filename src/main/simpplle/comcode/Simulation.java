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
 * Simulation projects changes to an area from disturbance processes and succession. Projections
 * are created at discrete time steps by applying system knowledge to vegetation units in the
 * current area. The length of the time step depends on the processes being simulated. Interaction
 * between processes such as fire and insects or response of grasses to yearly moisture changes
 * make more sense if run yearly. This is the default in grassland zones, like Wyoming. Decade
 * time steps are typical for most zones, which focus on vegetative change in landscapes over long
 * periods of time.
 */

public final class Simulation implements SimulationTypes, Externalizable {

  /**
   * Enumerates kinds of invasive species logic.
   */
  public enum InvasiveKind {

    R1,
    MSU,
    MESA_VERDE_NP,
    NONE

  }

  /**
   * The serialization ID for this class, which is required by the Externalizable interface.
   */
  static final long serialVersionUID = -6185381763708611851L;

  /**
   * The version number for this class's serialized representation.
   */
  static final int version = 6;

  /**
   * The maximum number of runs in a simulation.
   */
  public static final int MAX_SIMULATIONS = 100;

  /**
   * The maximum number of time steps in a run.
   */
  public static final int MAX_TIME_STEPS = 10000;

  /**
   * The default number of fixed-point decimal places in generated random numbers.
   */
  private static final int DEFAULT_PROB_PRECISION = 2;

  /**
   * The number of fixed-point decimal places in generated random numbers.
   */
  private static int probPrecision = DEFAULT_PROB_PRECISION;

  /**
   * The maximum value of a random number.
   */
  private static int maxProbability = 10000; // 100 with a fixed-point precision of two.

  /**
   * A shared simulation instance.
   */
  public static Simulation instance;

  /**
   * Active fire spreading model
   */
  public static FireSpreadModel fireSpreadModel = FireSpreadModel.BASIC;

  /**
   * A flag indicating if the simulation is running.
   */
  private boolean inSimulation = false;

  /**
   * Defines how disturbance processes are applied.
   */
  private int simulationMethod = STOCHASTIC;

  /**
   * The number of times to run the simulation.
   */
  private int numSimulations = 1;

  /**
   * The number of time steps in a run.
   */
  private int numTimeSteps = 5;

  /**
   * The index of the current run, which starts at one.
   */
  private int currentRun = 0;

  /**
   * The index of the current time step, which starts at zero.
   */
  private int currentTimeStep = -1;

  /**
   * The current season, which is used with yearly time steps.
   */
  private Season currentSeason = Season.YEAR;

  /**
   * A flag indicating if time increments in years. Otherwise it increments in decades.
   */
  private boolean yearlySteps = false;

  /**
   * The number of time steps to keep in memory.
   */
  private int pastTimeStepsInMemory = 10;

  /**
   * A flag indicating if the number of time steps in memory is capped.
   */
  private boolean discardData = false;

  /**
   * A flag indicating if fires use suppression.
   */
  private boolean fireSuppression = false;

  /**
   * The discount rate for fire suppression costs.
   */
  private float discount = 1.0f;

  /**
   * A per-simulation pseudo-random number generator.
   */
  private Random random = new Random();

  /**
   * A number that initializes the pseudo-random number generator.
   */
  private long seed = 0;

  /**
   * A flag indicating if the generator is initialized with a fixed seed.
   */
  private boolean fixedSeed = false;

  /**
   *
   */
  private boolean doAllStatesSummary = true;

  /**
   *  A flag indicating if area summary files should be written
   */
  private boolean doAreaSummary = false;

  /**
   *
   */
  private boolean doGisFiles = false;

  /**
   *
   */
  private boolean doProbArcFiles = false;

  /**
   *
   */
  private boolean doSimLoggingFile = false;

  /**
   *
   */
  private boolean doTrackingSpeciesReport = false;

  /**
   * A flag indicating if ownership is tracked during multiple runs.
   */
  private boolean trackOwnership = false;

  /**
   * A flag indicating if special area is tracked during multiple runs.
   */
  private boolean trackSpecialArea = false;

  /**
   * A flag indicating if access files are written.
   */
  private boolean writeAccess = false;

  /**
   * A flag indicating if a database is written to.
   */
  private boolean writeDatabase = false;

  /**
   * A flag indicating if probability files are written.
   */
  private boolean writeProbFiles = false;

  /**
   * The type of invasive species logic applied.
   */
  private InvasiveKind invasiveSpeciesKind = InvasiveKind.NONE;

  /**
   * A path prefix for simulation output.
   */
  private File outputFile = null;

  /**
   *
   */
  private File allStatesRulesFile;

  /**
   *
   */
  private AreaSummary areaSummary;

  /**
   *
   */
  private MultipleRunSummary multipleRunSummary;

  private PrintWriter   simLoggingWriter;
  private PrintWriter   invasiveSpeciesMSUProbOut;
  private PrintWriter[] accessEvuSimDataOut;
  private PrintWriter[] accessAreaSummaryOut;
  private PrintWriter   accessTrackingSpeciesOut;

  private TreeMap<Short,String> accessProcessList        = new TreeMap<>();
  private TreeMap<Short,String> accessSpeciesList        = new TreeMap<>();
  private TreeMap<Short,String> accessSizeClassList      = new TreeMap<>();
  private TreeMap<Short,String> accessDensityList        = new TreeMap<>();
  private TreeMap<Short,String> accessEcoGroupList       = new TreeMap<>();
  private TreeMap<Short,String> accessFmzList            = new TreeMap<>();
  private TreeMap<Short,String> accessIncRuleSpeciesList = new TreeMap<>();
  private TreeMap<Short,String> accessLifeformList       = new TreeMap<>();
  private TreeMap<Short,String> accessOwnershipList      = new TreeMap<>();
  private TreeMap<Short,String> accessSpecialAreaList    = new TreeMap<>();
  private TreeMap<Short,String> accessTreatmentTypeList  = new TreeMap<>();

  /**
   * Constructs a simulation with default parameters.
   */
  public Simulation () {}

  /**
   * Constructs a simulation with user-defined parameters.
   */
  public Simulation (int numSimulations,
                     int numTimeSteps,
                     boolean fireSuppression,
                     File outputFile,
                     float discount,
                     boolean trackSpecialArea,
                     boolean trackOwnership,
                     boolean yearlySteps,
                     String simulationMethod,
                     boolean writeDatabase,
                     boolean writeAccess,
                     boolean writeProbFiles,
                     InvasiveKind invasiveSpeciesKind,
                     int pastTimeStepsInMemory,
                     File allStatesRulesFile,
                     boolean discardData,
                     boolean doProbArcFiles,
                     boolean doAllStatesSummary,
                     boolean doTrackingSpeciesReport,
                     boolean doGisFiles,
                     boolean fixedSeed,
                     long seed,
                     boolean doAreaSummary) {

    this();

    this.numSimulations          = numSimulations;
    this.numTimeSteps            = numTimeSteps;
    this.pastTimeStepsInMemory   = pastTimeStepsInMemory;
    this.fireSuppression         = fireSuppression;
    this.outputFile              = outputFile;
    this.allStatesRulesFile      = allStatesRulesFile;
    this.discount                = discount;
    this.yearlySteps             = yearlySteps;
    this.writeDatabase           = writeDatabase;
    this.writeAccess             = writeAccess;
    this.writeProbFiles          = writeProbFiles;
    this.invasiveSpeciesKind     = invasiveSpeciesKind;
    this.discardData             = discardData;
    this.doProbArcFiles          = doProbArcFiles;
    this.doAllStatesSummary      = doAllStatesSummary;
    this.doTrackingSpeciesReport = doTrackingSpeciesReport;
    this.doGisFiles              = doGisFiles;
    this.fixedSeed               = fixedSeed;
    this.seed                    = seed;
    this.doAreaSummary           = doAreaSummary;

    if (simulationMethod.toUpperCase().equals("STOCHASTIC")) {
      this.simulationMethod = STOCHASTIC;
    } else if (simulationMethod.toUpperCase().equals("STAND DEVELOPMENT")) {
      this.simulationMethod = STAND_DEVELOPMENT;
    } else if (simulationMethod.toUpperCase().equals("HIGHEST")) {
      this.simulationMethod = HIGHEST;
    }

    if (numSimulations > 1) {
      this.trackSpecialArea = trackSpecialArea;
      this.trackOwnership = trackOwnership;
    }

    if (fixedSeed) {
      random.setSeed(seed);
    }

    accessEvuSimDataOut  = new PrintWriter[this.numSimulations];
    accessAreaSummaryOut = new PrintWriter[numSimulations];

  }

  public static Simulation getInstance() {
    return instance;
  }

  public static void setInstance(Simulation simulation) {
    instance = simulation;
  }

  public static void clearInstance() {
    instance = null;
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

  public static int getCurrentRun() { // TODO: Make non-static, access 'this' directly
    if (instance == null) {
      return 1;
    } else {
      return instance.currentRun;
    }
  }

  public static int getCurrentTimeStep() { // TODO: Make non-static, access 'this' directly
    if (instance == null) {
      return 0;
    } else {
      return instance.currentTimeStep;
    }
  }

  public Season getCurrentSeason() {
    return currentSeason;
  }

  public int getNumSimulations () {
    return numSimulations;
  }

  public int getNumTimeSteps () {
    return numTimeSteps;
  }

  public int getSimulationMethod() {
    return simulationMethod;
  }

  private String getSimulationMethodName() {
    switch(simulationMethod) {
      case STOCHASTIC:
        return "STOCHASTIC";
      case HIGHEST:
        return "HIGHEST";
      case STAND_DEVELOPMENT:
        return "STAND DEVELOPMENT";
      default:
        return "";
    }
  }

  public boolean isHighestProbability() {
    return simulationMethod == HIGHEST;
  }

  public boolean isStandDevelopment() {
    return simulationMethod == STAND_DEVELOPMENT;
  }

  public boolean isStochastic() {
    return simulationMethod == STOCHASTIC;
  }

  public boolean isSimulationRunning() {
    return inSimulation;
  }

  public boolean isMultipleRun () {
    return numSimulations > 1;
  }

  public boolean isYearlyTimeSteps() {
    return yearlySteps;
  }

  public boolean isDecadeTimeSteps() {
    return !yearlySteps;
  }

  public boolean isDecadeStep() {
    if (yearlySteps) {
      return (getCurrentTimeStep() % 10) == 0; // WARNING: Uses shared instance, yearlySteps doesn't
    } else {
      return true;
    }
  }

  public boolean isLastSeason() {
    if (RegionalZone.isWyoming()) {
      if (isStandDevelopment()) {
        return currentSeason == Season.SPRING;
      } else {
        return currentSeason == Season.WINTER;
      }
    }
    return true;
  }

  public void setDiscardData(boolean discardData) {
    this.discardData = discardData;
  }

  public boolean isDiscardData() {
    return discardData;
  }

  public int getPastTimeStepsInMemory() {
    return pastTimeStepsInMemory;
  }

  public float getDiscount() {
    return discount;
  }

  public boolean fireSuppression() {
    return fireSuppression;
  }

  public InvasiveKind getInvasiveSpeciesKind() {
    return invasiveSpeciesKind;
  }

  public String getInvasiveSpeciesKindName() {
    switch(invasiveSpeciesKind) {
      case R1:
        return "R1";
      case MSU:
        return "MSU";
      case MESA_VERDE_NP:
        return "MESA_VERDE_NP";
      case NONE:
        return "NONE";
      default:
        return "";
    }
  }

  public boolean isDoInvasiveSpecies() {
    return invasiveSpeciesKind != InvasiveKind.NONE;
  }

  public boolean needNearestRoadTrailInfo() {
    return invasiveSpeciesKind != InvasiveKind.NONE;
  }

  public boolean trackOwnership() {
    return trackOwnership;
  }

  public boolean trackSpecialArea() {
    return trackSpecialArea;
  }

  public File getOutputFile() {
    return outputFile;
  }

  public AreaSummary getAreaSummary() {
    return areaSummary;
  }

  public MultipleRunSummary getMultipleRunSummary() {
    return multipleRunSummary;
  }

  public boolean existsMultipleRunSummary() {
    return multipleRunSummary != null;
  }

  public PrintWriter getAccessEvuSimDataOut() {
    return accessEvuSimDataOut[currentRun];
  }

  public PrintWriter getAccessTrackingSpeciesOut() {
    return accessTrackingSpeciesOut;
  }

  public PrintWriter getInvasiveSpeciesMSUPrintWriter() {
    if (outputFile != null) {
      return invasiveSpeciesMSUProbOut;
    } else {
      return null;
    }
  }

  public void makeAccessFilesDir() throws SimpplleError {
    File outputDir = new File(outputFile.getParent(),"textdata");
    if (!outputDir.mkdir()) {
      throw new SimpplleError("Unable to create output directory at " + outputDir);
    }
  }

  public File getAccessFilesPath() throws SimpplleError {
    File path = new File(outputFile.getParent(),"textdata");
    return path;
  }

//  public File getAllStatesFilePath() {
//    File path = new File(outputFile+"-allstates-"+Integer.toString(currentRun+1)+".txt");
//    File path = new File(outputFile+"-allstates.txt");
//    return path;
//  }

  public File getDatabasePath() {
    File newDir = new File(outputFile+"db");
    File path = new File(newDir,outputFile.getName()+"db");
    return path;
  }

  public File getFireSpreadReportPath() {
    File path = new File(outputFile+"-firespread-"+Integer.toString(currentRun+1)+".txt");
    return path;
  }

  public File getSimFilePath() {
    File path = new File(outputFile+".simdata_bin");
    return path;
  }

  public boolean getWriteAccess() {
    return writeAccess;
  }

  public boolean getWriteDatabase() {
    return writeDatabase;
  }

  public boolean isAllStatesRulesFile() {
    return allStatesRulesFile != null;
  }

  public boolean isDoAllStatesSummary() {
    return doAllStatesSummary;
  }

  public boolean isDoProbArcFiles() {
    return doProbArcFiles;
  }

  public boolean isDoSimLoggingFile() {
    return doSimLoggingFile;
  }

  public boolean isDoTrackingSpeciesReport() {
    return doTrackingSpeciesReport;
  }

  public PrintWriter getSimLoggingWriter() {
    return simLoggingWriter;
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
    accessOwnershipList.put(ownership.getSimId(), ownership.toString());
  }

  public void addAccessProcess(ProcessType process) {
    accessProcessList.put(process.getSimId(), process.toString());
  }

  public void addAccessSizeClass(SizeClass sizeClass) {
    accessSizeClassList.put(sizeClass.getSimId(), sizeClass.toString());
  }

  public void addAccessSpecialArea(SpecialArea specialArea) {
    accessSpecialAreaList.put(specialArea.getSimId(), specialArea.toString());
  }

  public void addAccessSpecies(Species species) {
    accessSpeciesList.put(species.getSimId(), species.toString());
  }

  public void addAccessTreatment(TreatmentType treatmentType) {
    accessTreatmentTypeList.put(treatmentType.getSimId(),treatmentType.toString());
  }

  /**
   * Generates a fixed-point random number in the range [0,100).
   *
   * @return A generated number
   */
  public int random() {
    return random.nextInt(maxProbability);
  }

  /**
   * Returns the pseudo-random number generator for this simulation
   *
   * @return a pseudo-random number generator
   */
  public Random getRandom() {
    return random;
  }

  public void runSimulation () throws SimpplleError {

    inSimulation = true;

    try {

      if (outputFile != null) {

        try {
          writeLog();
        } catch (IOException ex) {
          throw new SimpplleError(ex.getMessage(),ex);
        }

        if (simpplle.JSimpplle.simLoggingFile()) {
          doSimLoggingFile = true;
          try {
            File log = Utility.makeSuffixedPathname(outputFile, "-detaillog", "txt");
            simLoggingWriter = new PrintWriter(new FileOutputStream(log));
            simLoggingWriter.println("Time,Spread_From,Spread_To,To_Life_Form,Fire_Type,Rule");
          } catch (IOException ex) {
            throw new SimpplleError(ex.getMessage(),ex);
          }
        }

        Simpplle.setStatusMessage("Writing initial Simulation Data Files");

        writeInitialAreaFile();

        if (writeDatabase) {
          Simpplle.setStatusMessage("Creating Database");
          DatabaseCreator.initHibernate(true, getDatabasePath());
          writeDatabaseManagerBatFile();
          writeOpenOfficeBaseInstructions();
        }

        if (writeAccess) {
          clearLookupTables();
          makeAccessFilesDir();
          openAccessTextFiles();
        }
      }

      if (needNearestRoadTrailInfo()) {
        Simpplle.setStatusMessage("Calculating Nearest Roads/Trails");
        Area area = Simpplle.getCurrentArea();
        if (area.hasRoads()) {
          Evu.findRoadUnits();
        }
        if (area.hasTrails()) {
          Evu.findTrailUnits();
        }
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

      if (outputFile != null) {

        if (writeDatabase) {
          DatabaseCreator.closeHibernate();
        }

        if (writeAccess) {

          saveDensityLookup(new File(getAccessFilesPath(), "DENSITY.csv"));
          saveLifeformLookup(new File(getAccessFilesPath(), "LIFEFORM.csv"));
          saveOwnershipLookup(new File(getAccessFilesPath(), "OWNERSHIP.csv"));
          saveProbabilityLookup(new File(getAccessFilesPath(), "PROBSTR.csv"));
          saveProcessLookup(new File(getAccessFilesPath(), "PROCESS.csv"));
          saveSizeClassLookup(new File(getAccessFilesPath(), "SIZECLASS.csv"));
          saveSlinkMetrics(new File(getAccessFilesPath(), "SLINKMETRICS.csv"));
          saveSpecialAreaLookup(new File(getAccessFilesPath(), "SPECIALAREA.csv"));
          saveSpeciesLookup(new File(getAccessFilesPath(), "SPECIES.csv"));
          saveTrackSpeciesLookup(new File(getAccessFilesPath(), "TRACKSPECIES.csv"));
          saveTreatmentLookup(new File(getAccessFilesPath(), "TREATMENT.csv"));

          closeAccessTextFiles();
        }

        if (simpplle.JSimpplle.invasiveSpeciesMSUProbFile()) {
          invasiveSpeciesMSUProbOut.flush();
          invasiveSpeciesMSUProbOut.close();
        }

        if (doSimLoggingFile) {
          simLoggingWriter.flush();
          simLoggingWriter.close();
        }
      }
    } catch (Exception err) {
      throw new SimpplleError("The following Runtime Exception occurred:\n" + err.getMessage(),err);
    } finally {
      inSimulation = false;
    }
  }

  private void doMultipleRun() throws SimpplleError {

    currentRun = 0;

    multipleRunSummary = new MultipleRunSummary(this);

    Area currentArea = Simpplle.currentArea;
    currentArea.initMultipleSimulation();

    for (int i = 0; i < numSimulations; i++) {

      String msg = "Performing Simulation #" + (i + 1);
      Simpplle.setStatusMessage(msg);

      doFuture();

      // Update Area Summary data.
      if (fireSuppression()) {
        multipleRunSummary.updateFireSuppressionCostSummary(discount);
      }
      currentArea.updateSummaries(multipleRunSummary);
      multipleRunSummary.finishEmissionsSummary();
      multipleRunSummary.computeFrequencies();

      saveSimData();

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

  private void doFuture() throws SimpplleError {

    currentTimeStep = 0;

    Density.resetSimIds();
    HabitatTypeGroupType.resetSimIds();
    InclusionRuleSpecies.resetSimIds();
    Lifeform.resetSimIds();
    ProcessType.resetSimIds();
    SizeClass.resetSimIds();
    Species.resetSimIds();

    Area currentArea = Simpplle.currentArea;
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

    // write initial states to files (time step 0)
    if (writeAccess) {
      currentArea.writeSimulationAccessFiles(accessEvuSimDataOut[currentRun], accessTrackingSpeciesOut);
    }

    try {
      for(int i = 0; i < numTimeSteps; i++) {

        currentTimeStep++; // How is this different from i?
        if (currentTimeStep > 1) {
          areaSummary.doBeginTimeStepInitialize();
        }

        long freeMem = Runtime.getRuntime().freeMemory();
        long totMem  = Runtime.getRuntime().totalMemory();
        long maxMemMB  = Runtime.getRuntime().maxMemory() / 1024 / 1024;;
        long usedMemMB = ((totMem - freeMem) / 1024) / 1024;

        String msg = "Project Area for Time Step: " + (i + 1)
                   + " Run #" + (currentRun + 1)
                   + " Mem: " + usedMemMB + "MB/" + maxMemMB;

        Simpplle.setStatusMessage(msg);

        if (FireEvent.useRegenPulse()) { FireEvent.setRegenPulse(); }

        if (RegionalZone.isWyoming()) {
          Season lastSeason = isStandDevelopment() ? Season.SPRING : Season.WINTER;
          for (Season s : Season.values()) {
            currentSeason = s;
            currentArea.doFuture();
            if (s == lastSeason) { break; }
          }
        } else {
          currentSeason = Season.YEAR;
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
        if (outputFile != null && writeAccess && doAreaSummary) {
          Simpplle.setStatusMessage("Writing Area Summary Data to text file");
          areaSummary.writeAccessFiles(accessAreaSummaryOut[currentRun]);
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

        if (!isMultipleRun()) {
          saveSimData();
        }
        if (doAllStatesSummary) {
          Reports.generateAllStatesReport(getOutputFile());
        }
        if (doTrackingSpeciesReport) {
          Reports.generateTrackingSpeciesReport(getOutputFile());
        }
      }
    } catch (SimpplleError err) {
      currentArea.resetTreatmentSchedule();
      Simpplle.clearStatusMessage();
      throw err;
    }

    currentArea.resetTreatmentSchedule();

    // Manually clean things up until simpplle is made more efficient.
    if (!currentArea.doManualGC()) { System.gc(); }

  }

  /**
   * Writes a log to the output directory containing simulation parameters.
   *
   * @throws IOException
   */
  public void writeLog() throws IOException {

    File file = Utility.makeSuffixedPathname(outputFile, "-log", "txt");

    File areaAttributesAll    = null;
    File areaSpatialRelate    = null;
    File climateSchedule      = SystemKnowledge.getFile(SystemKnowledge.CLIMATE);
    File fireManagementZone   = SystemKnowledge.getFile(SystemKnowledge.FMZ);
    File fireSpotting         = SystemKnowledge.getFile(SystemKnowledge.FIRE_SPOTTING_LOGIC);
    File fireSpread           = SystemKnowledge.getFile(SystemKnowledge.FIRE_SPREAD_LOGIC);
    File fireSuppSpreadRate       = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_SPREAD_RATE_LOGIC);
    File fireSuppBeyondClsA   = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_BEYOND_CLASS_A_LOGIC);
    File fireSuppClsA         = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_CLASS_A_LOGIC);
    File fireSuppEvent        = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_EVENT_LOGIC);
    File fireSuppLineProdRate = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_PRODUCTION_RATE_LOGIC);
    File fireSuppWthrBeyClsA  = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_BEYOND_CLASS_A);
    File fireSuppWthrClsA     = SystemKnowledge.getFile(SystemKnowledge.FIRE_SUPP_WEATHER_CLASS_A_LOGIC);
    File fireType             = SystemKnowledge.getFile(SystemKnowledge.FIRE_TYPE_LOGIC);
    File processProbability   = SystemKnowledge.getFile(SystemKnowledge.PROCESS_PROB_LOGIC);
    File producingSeed        = SystemKnowledge.getFile(SystemKnowledge.PRODUCING_SEED_LOGIC);
    File regenerationFire     = SystemKnowledge.getFile(SystemKnowledge.REGEN_LOGIC_FIRE);
    File regenerationSucc     = SystemKnowledge.getFile(SystemKnowledge.REGEN_LOGIC_SUCC);
    File treatmentLogic       = SystemKnowledge.getFile(SystemKnowledge.TREATMENT_LOGIC);
    File treatmentSchedule    = SystemKnowledge.getFile(SystemKnowledge.TREATMENT_SCHEDULE);
    File systemKnowledge      = SystemKnowledge.loadedFile;
    File systemKnowledgeSave  = SystemKnowledge.savedFile;

    Area area = Simpplle.getCurrentArea();
    if (area != null) {
      areaAttributesAll = area.getAttributesAllFile();
      areaSpatialRelate = area.getSpatialRelateFile();
    }

    try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {

      writer.write("SIMPPLLE Simulation Log File\n"
                 + "\n"
                 + "Date : " + Simpplle.currentDate() + "\n"
                 + "Time : " + Simpplle.currentTime() + "\n"
                 + "\n"
                 + "Current Zone : " + Simpplle.getCurrentZone() + "\n"
                 + "Current Area : " + Simpplle.getCurrentArea() + "\n"
                 + "\n"
                 + "Number of Simulations : " + numSimulations + "\n"
                 + "Number of Time Steps  : " + numTimeSteps + (yearlySteps ? " (Years)" : " (Decades)") + "\n"
                 + "\n"
                 + "Simulation Method      : " + getSimulationMethodName() + "\n"
                 + "Fire Spread Model      : " + ((Simulation.fireSpreadModel == FireSpreadModel.BASIC) ? "SIMPPLLE" : "KEANE") + "\n"
                 + "Fire Suppression       : " + fireSuppression + "\n"
                 + "Fire Cost Discount     : " + getDiscount() + "\n"
                 + "Invasive Species Logic : " + getInvasiveSpeciesKindName() + "\n"
                 + "Random Number Seed     : " + (fixedSeed ? seed : "Auto") + "\n"
                 + "\n"
                 + "Data Files\n"
                 + "\n"
                 + "Area Spatial Relations   : " + ((areaSpatialRelate    == null) ? "Not Loaded" : areaSpatialRelate) + "\n"
                 + "Area Unit Attributes     : " + ((areaAttributesAll    == null) ? "Not Loaded" : areaAttributesAll) + "\n"
                 + "Climate Schedule         : " + ((climateSchedule      == null) ? "Default" : climateSchedule) + "\n"
                 + "Fire Management Zones    : " + ((fireManagementZone   == null) ? "Default" : fireManagementZone) + "\n"
                 + "Fire Spotting            : " + ((fireSpotting         == null) ? "Default" : fireSpotting) + "\n"
                 + "Fire Spread              : " + ((fireSpread           == null) ? "Default" : fireSpread) + "\n"
                 + "Fire Supp Bey Cls A      : " + ((fireSuppBeyondClsA   == null) ? "Default" : fireSuppBeyondClsA) + "\n"
                 + "Fire Supp Bey Cls A Wthr : " + ((fireSuppWthrBeyClsA  == null) ? "Default" : fireSuppWthrBeyClsA) + "\n"
                 + "Fire Supp Cls A          : " + ((fireSuppClsA         == null) ? "Default" : fireSuppClsA) + "\n"
                 + "Fire Supp Cls A Wthr     : " + ((fireSuppWthrClsA     == null) ? "Default" : fireSuppWthrClsA) + "\n"
                 + "Fire Supp Event          : " + ((fireSuppEvent        == null) ? "Default" : fireSuppEvent) + "\n"
                 + "Fire Supp Line Prod Rate : " + ((fireSuppLineProdRate == null) ? "Default" : fireSuppLineProdRate) + "\n"
                 + "Fire Supp Spread Rate    : " + ((fireSuppSpreadRate   == null) ? "Default" : fireSuppSpreadRate) + "\n"
                 + "Fire Type                : " + ((fireType             == null) ? "Default" : fireType) + "\n"
                 + "Process Probability      : " + ((processProbability   == null) ? "Default" : processProbability) + "\n"
                 + "Producing Seed           : " + ((producingSeed        == null) ? "Default" : producingSeed) + "\n"
                 + "Regeneration - Fire      : " + ((regenerationFire     == null) ? "Default" : regenerationFire) + "\n"
                 + "Regeneration - Succ      : " + ((regenerationSucc     == null) ? "Default" : regenerationSucc) + "\n"
                 + "System Knowledge         : " + ((systemKnowledge      == null) ? "Default" : systemKnowledge) + "\n"
                 + "System Knowledge Save    : " + ((systemKnowledgeSave  == null) ? "Not Saved" : systemKnowledgeSave) + "\n"
                 + "Treatment Logic          : " + ((treatmentLogic       == null) ? "Default" : treatmentLogic) + "\n"
                 + "Treatment Schedule       : " + ((treatmentSchedule    == null) ? "Default" : treatmentSchedule));

    }
  }

  private void clearLookupTables() {
    accessProcessList.clear();
    accessSpeciesList.clear();
    accessSizeClassList.clear();
    accessDensityList.clear();
    accessEcoGroupList.clear();
    accessFmzList.clear();
    accessIncRuleSpeciesList.clear();
    accessLifeformList.clear();
    accessOwnershipList.clear();
    accessSpecialAreaList.clear();
    accessTreatmentTypeList.clear();
  }

  private void saveDensityLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,DENSITY");
      for (Short id : accessDensityList.keySet()) {
        String value = accessDensityList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveLifeformLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,LIFEFORM");
      for (Short id : accessLifeformList.keySet()) {
        String value = accessLifeformList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveOwnershipLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,OWNERSHIP");
      for (Short id : accessOwnershipList.keySet()) {
        String value = accessOwnershipList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveProbabilityLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,DESCRIPTION");
      writer.println("D,Process no next state");
      writer.println("L,Locked in process");
      writer.println("S,Spreading process");
      writer.println("SUPP,Suppressed Process");
      writer.println("SE,Extreme fire spread");
      writer.println("SFS,Fire spotting spread");
      writer.println("COMP,Competition");
      writer.println("GAP,Gap process");
    }
  }

  private void saveProcessLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,PROCESS");
      for (Short id : accessProcessList.keySet()) {
        String value = accessProcessList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveSizeClassLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,SIZECLASS");
      for (Short id : accessSizeClassList.keySet()) {
        String value = accessSizeClassList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveSlinkMetrics(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("SLINK,ROW,COLUMN,ACRES,ECOGROUP,OWNERSHIP,SPECIALAREA,FMZ");
      for (Evu evu : Simpplle.currentArea.getAllEvu()) {
        if (evu == null) continue;
        writer.println(evu.getId() + ","
            + evu.getLocationX() + ","
            + evu.getLocationY() + ","
            + evu.getFloatAcres() + ","
            + evu.getHabitatTypeGroup().getName() + ","
            + evu.getOwnership() + ","
            + evu.getSpecialArea() + ","
            + evu.getFmz().getName());
      }
    }
  }

  private void saveSpecialAreaLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,SPCAREA");
      for (Short id : accessSpecialAreaList.keySet()) {
        String value = accessSpecialAreaList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveSpeciesLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,SPECIES");
      for (Short id : accessSpeciesList.keySet()) {
        String value = accessSpeciesList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveTrackSpeciesLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,INCSPECIES");
      for (Short id : accessIncRuleSpeciesList.keySet()) {
        String value = accessIncRuleSpeciesList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void saveTreatmentLookup(File file) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
      writer.println("ID,TREATMENT");
      for (Short id : accessTreatmentTypeList.keySet()) {
        String value = accessTreatmentTypeList.get(id);
        writer.println(id + "," + value);
      }
    }
  }

  private void openAccessTextFiles() throws SimpplleError, IOException {

    File path;

    for (int run=0; run<numSimulations; run++) {
      path = new File (getAccessFilesPath(),"EVU_SIM_DATA" + Integer.toString(run+1) + ".csv");
      //GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(path));
      //accessEvuSimDataOut[run] = new PrintWriter(out);
      accessEvuSimDataOut[run] = new PrintWriter(new FileWriter(path, true));
      accessEvuSimDataOut[run].println("RUN,TIMESTEP,SEASON_ID,SLINK,LIFEFORM_ID,SPECIES_ID,SIZECLASS_ID,AGE,DENSITY_ID,PROCESS_ID,PROB,PROBSTR,TREATMENT_ID,ORIGINUNITID,FROMUNITID,PROCESS_RULE,FIRE_REGENERATION_RULE,SUCCESSION_REGENERATION_RULE");
    }

    if(doAreaSummary) {
      // Open an area summary file for each simulation
      for (int run = 0; run < numSimulations; run++) {
        path = new File(getAccessFilesPath(), "AREASUMMARY" + Integer.toString(run + 1) + ".csv");
        accessAreaSummaryOut[run] = new PrintWriter(new FileWriter(path, true));
        accessAreaSummaryOut[run].println("RUN,TIMESTEP,ORIGINUNITID,UNITID,TOUNITID,PROCESS_ID,PROB,ACRES,SEASON_ID,GROUP_ID,OWNERSHIP_ID,SPECIAL_AREA_ID,FMZ_ID");
      }
    }

    path = new File (getAccessFilesPath(),"TRACKINGSPECIESPCT.csv");
    accessTrackingSpeciesOut = new PrintWriter(new FileWriter(path, true));
    accessTrackingSpeciesOut.println("RUN,TIMESTEP,SLINK,LIFEFORM_ID,SPECIES_ID,PCT");

  }

  private void closeAccessTextFiles() throws SimpplleError {

    for (int run=0; run<numSimulations; run++) {
      accessEvuSimDataOut[run].flush();
      accessEvuSimDataOut[run].close();
    }

    if(doAreaSummary){
      for (int run=0; run<numSimulations; run++) {
        accessAreaSummaryOut[run].flush();
        accessAreaSummaryOut[run].close();
      }
    }

    accessTrackingSpeciesOut.flush();
    accessTrackingSpeciesOut.close();

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
      Lifeform[] lives = Lifeform.getLifeformsByDominance();
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

  private void saveSimData() throws SimpplleError {
    saveSimData(outputFile);
  }

  private void saveSimData(File outfile) throws SimpplleError {
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

/*
  private void saveSimData(File outfile) throws SimpplleError {
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

  public void saveSimulation(PrintWriter fout) {
    saveSimulation(fout,numSimulations);
  }

  private void saveSimulation(PrintWriter fout, int nRuns) {
    int num;

    fout.println("CLASS SIMULATION");
    fout.print(nRuns);
    fout.print(",");
    fout.print(numTimeSteps);
    fout.print(",");

    fout.print(discount);
    fout.print(",");
    num = fireSuppression ? 1 : 0;
    fout.print(num);
    fout.print(",");

    num = trackSpecialArea ? 1 : 0;
    fout.print(num);
    fout.print(",");

    num = trackOwnership ? 1 : 0;
    fout.print(num);
    fout.println();
  }

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
      throw new SimpplleError("Unable to create necessary output directory " + outputDir);
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

}




