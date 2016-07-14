package simpplle.comcode;

import java.io.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains several functions which implement user
 * requested actions.  This class also provides static public storage
 * of the Zone, Area, and Simulation instances.  These variables are
 * referenced throughout the package.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public final class Simpplle {
  private static final int version = 3;
  private static boolean recreateMrSummary = false;

  static Area         currentArea = null;
  static RegionalZone currentZone = null;
  private static Climate climate = null;

  public static final String endl = System.getProperty("line.separator");

  public static final int FORMATTED               = 0;
  public static final int FORMATTED_OWNERSHIP     = 1;
  public static final int FORMATTED_SPECIAL_AREA  = 2;
  public static final int FORMATTED_OWNER_SPECIAL = 3;
  public static final int CDF                     = 4;
  public static final int CDF_OWNERSHIP           = 5;
  public static final int CDF_SPECIAL_AREA        = 6;
  public static final int CDF_OWNER_SPECIAL       = 7;

  /**
   * Class constructor. Initializes the final variables.  
   */
  public Simpplle () {
  }
/**
 * Sets the Simpplle status message
 * @param msg
 */
  public static void setStatusMessage(String msg) {
    setStatusMessage(msg,false);
  }
  /**
   * Sets the simpple status message.  
   * @param msg
   * @param wait
   */
  public static void setStatusMessage(String msg, boolean wait) {
    simpplle.JSimpplle.setStatusMessage(msg.trim(),wait);
  }
/**
 * Clears the Simpplle status message.  
 */
  public static void clearStatusMessage() {
    simpplle.JSimpplle.clearStatusMessage();
  }

  /**
   * @return Current area instance
   * @see simpplle.comcode.Area
   */
  public static Area getCurrentArea () {
    return currentArea;
  }
/**
 * Sets the current Simpplle area.
 * @param area
 */
  public static void setCurrentArea(Area area) {
    currentArea = area;
  }
/**
 * Sets recreate multiple run summary boolean.  
 * @param value true if should recreate multiple run summary
 */
  public static void setRecreateMrSummary(boolean value) {
    recreateMrSummary = value;
  }

  /**
   * @return Current Zone instance
   * @see simpplle.comcode.RegionalZone
   * @see simpplle.comcode.WestsideRegionOne
   * @see simpplle.comcode.EastsideRegionOne
   */
  public static RegionalZone getCurrentZone () {
    return currentZone;
  }

  /**
   * @param zone Regional Zone
   */
  public static void setCurrentZone(RegionalZone zone) {
    currentZone = zone;
  }
  /**
    * Gets a climate instance.  
    * @return Climate instance
    * @see simpplle.comcode.Climate
    */
  public static Climate getClimate() { return climate; }

  /**
   * Gets the current simulation instance.  
   * @return an Simulation, the Current Simulation instance.
   * @see simpplle.comcode.Simulation
   */
  public static Simulation getCurrentSimulation() {
    return Simulation.getInstance();
  }

  /**
   * Clears the current simulation, if one exists.  
   */
  private static void clearSimulation() {
    Simulation sim = getCurrentSimulation();
    if (sim != null) {
      sim.clearInstance();
    }
  }
/**
 * The simpplle boolean for recreate multiple run summary.  
 * @return true if recreate multiple run summary.  
 */
  public static boolean isRecreateMrSummary() {
    return recreateMrSummary;
  }

  /**
   * Gets the Area Summary from the current simulation instance.
   * @return an AreaSummary.
   * @see simpplle.comcode.Simulation
   * @see simpplle.comcode.AreaSummary
   */
  public static AreaSummary getAreaSummary() {
    if (getCurrentSimulation() == null) {
      return null;
    }
    else {
      return getCurrentSimulation().getAreaSummary();
    }
  }

  /**
   * @return The current time (am/pm) in a string.
   */
  public static String currentTime () {
    Calendar   cal;
    DateFormat time;

    cal  = Calendar.getInstance();
    time = DateFormat.getTimeInstance();

    return time.format(cal.getTime());
  }

  /**
   * @return the Current Date (e.g. November 11,2013)
   */
  public static String currentDate () {
    DateFormat date = DateFormat.getDateInstance(DateFormat.LONG);

    return date.format(new Date());
  }

  /**
    * Returns an array of strings containing names of available zones.
    * Note: the array index matches the id of the zones.
    */
  public String[] availableZones() {
    return RegionalZone.availableZones();
  }

  /**
    * Returns an array of the available sample areas in the
    * currently loaded zone.
    * @return an array of type Area
    */
  public Area[] getSampleAreas() {
    return currentZone.getSampleAreas();
  }
/**
 * Loads a zone.  WESTSIDE_REGION_ONE,
 * @param choice
 * @throws SimpplleError
 */
  public void loadZone(int choice) throws SimpplleError {
    loadZone(choice,false);
  }

  /**
   * This method loads a zone, as well as all pathways.
   * @param choice is a int, which should be a zone id.
   * @see simpplle.comcode.ValidZones
   */
  public void loadZone (int choice, boolean historic) throws SimpplleError {

    RegionalZone zone;

    switch (choice) {
      case ValidZones.WESTSIDE_REGION_ONE:
        zone = new WestsideRegionOne();
        break;
      case ValidZones.EASTSIDE_REGION_ONE:
        zone = new EastsideRegionOne();
        break;
      case ValidZones.TETON:
        zone = new Teton();
        break;
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
        zone = new NorthernCentralRockies();
        break;
      case ValidZones.SIERRA_NEVADA:
        zone = new SierraNevada();
        break;
      case ValidZones.SOUTHERN_CALIFORNIA:
        zone = new SouthernCalifornia();
        break;
      case ValidZones.GILA:
        zone = new Gila();
        break;
      case ValidZones.SOUTH_CENTRAL_ALASKA:
        zone = new SouthCentralAlaska();
        break;
      case ValidZones.SOUTHWEST_UTAH:
        zone = new SouthwestUtah();
        break;
      case ValidZones.COLORADO_FRONT_RANGE:
        zone = new ColoradoFrontRange();
        break;
      case ValidZones.COLORADO_PLATEAU:
        zone = new ColoradoPlateau();
        break;
      case ValidZones.WESTERN_GREAT_PLAINS_STEPPE:
        zone = new WesternGreatPlainsSteppe();
        break;
      case ValidZones.GREAT_PLAINS_STEPPE:
        zone = new GreatPlainsSteppe();
        break;
      case ValidZones.MIXED_GRASS_PRAIRIE:
        zone = new MixedGrassPrairie();
        break;
      default:
        throw new SimpplleError("Unknown zone");
    }

    currentZone = zone;
    currentArea = null;

    clearSimulation();

    setStatusMessage("Loading Zone: " + zone.getName() + "...");

    try {
      // Clear Previous data
      RegenerationLogic.clearData(RegenerationLogic.FIRE);
      RegenerationLogic.clearData(RegenerationLogic.SUCCESSION);
      HabitatTypeGroup.clearGroups();
      FireSpreadDataNewerLegacy.clearData();
      FireTypeDataNewerLegacy.clearData();

      Ownership.reset();
      SpecialArea.reset();
      Landtype.reset();

      // Create the climate instance
      climate = new Climate();

      // load the data files.
      zone.readZoneDefinitionFile();
      SimpplleType.initializeProcessList();
      SimpplleType.initializeTreatmentList();

      ProcessProbLogic.initialize();
      InvasiveSpeciesLogic.initialize();
      FireEventLogic.initialize();
      DoCompetitionLogic.initialize();
      RegenerationDelayLogic.initialize();
      GapProcessLogic.initialize();
      EvuSearchLogic.initialize();
      ProducingSeedLogic.initialize();
      VegUnitFireTypeLogic.initialize();
      InvasiveSpeciesLogicMSU.initialize();
      FireSuppClassALogic.initialize();
      FireSuppBeyondClassALogic.initialize();
      FireSuppProductionRateLogic.initialize();
      FireSuppSpreadRateLogic.initialize();
      FireSuppWeatherClassALogic.initialize();
      TrackingSpeciesReportData.makeInstance();
      FireSuppEventLogic.initialize();

      zone.setUseHistoricPathways(historic);

      zone.readZoneSystemKnowledgeFile();

      SimpplleType.initializeSizeClassList();
      SimpplleType.initializeDensityList();
      SimpplleType.initializeGroupList();

      // This is necessary in case the species file is loaded after
      // pathways (which normally it is).
      // This is also called when pathways are loaded, which is necessary
      // because user may load another pathway later.  So we end of calling
      // this twice (hopefully no delay results).
      HabitatTypeGroup.findAllRegenerationStates();

      Density.initPercentCanopy();
      
      Process droughtProcess = Process.findInstance(ProcessType.DROUGHT);
      if (droughtProcess != null) {
        droughtProcess.setYearlyStatus((zone instanceof ColoradoPlateau));
      }
    } catch (SimpplleError err) {
      String msg = "Unable to load zone: " + err.getMessage();
      System.out.println(msg);
      currentZone = null;
      climate     = null;
      throw new SimpplleError(msg,err);
    } finally {
      clearStatusMessage();
    }
  }

  /**
   * This method will allow the user to choose
   * a sample area to load.
   */
  public void loadSampleArea (Area area) throws SimpplleError {
    clearSimulation();
    setStatusMessage("Loading Sample Area: " + area.getName() + "...");
    try {
      InclusionRuleSpecies.clearAllInstances();
      if (area.getPath().indexOf("SWEATHOUSE.AREA") != -1) {
        currentArea = area;
        SystemKnowledge.loadSampleArea(currentArea);
      }
      else {
        readSerializedArea(area);
      }
    }
    catch (Exception e) {
      currentArea = null;
      throw new SimpplleError("Problem encountered loading sample area",e);
    }
    finally {
      clearStatusMessage();
    }
  }

  /**
   * This method will load a user defined area.
   * @param file of type File is the input area data file.
   */
//  public void loadArea(File file, boolean skipAccumData,
//                       boolean saveSkippedAccumData) throws SimpplleError {
  public void loadArea(File file) throws SimpplleError {
    clearSimulation();
    setStatusMessage("Loading Area ...");

    boolean newAreaFile = isNewAreaFile(file);
//    boolean newAreaFile = true;

    if (newAreaFile == false) {
      currentArea = new Area(file, Area.USER);
    }
    try {
      InclusionRuleSpecies.clearAllInstances();
      if (newAreaFile) {
        readSerializedArea(file,null);
      }
      else {
        // Area creates a new simulation instance if needed.
        currentArea.loadArea();
        if (getCurrentSimulation() != null && getCurrentSimulation().fireSuppression()) {
          getAreaSummary().doSuppressionCosts();
        }
      }
    }
    catch (Exception e) {
      currentArea       = null;
      clearSimulation();
      throw new SimpplleError(e.getMessage(),e);
    }
    finally {
      clearStatusMessage();
    }
  }

  public void makeNewSimulation() {
    Simulation.setInstance(new Simulation());
  }

  /**
   * This method will load a previously simulated area.
   * @param files of type File is the input area data file.
   */
  public void loadPreviousSimulation(File[] files) throws SimpplleError {
    setStatusMessage("Loading Area ...");

    try {
      InclusionRuleSpecies.clearAllInstances();
      readSerializedArea(files[0],files[1]);
    }
    catch (Exception e) {
      currentArea       = null;
      clearSimulation();
      throw new SimpplleError(e.getMessage(),e);
    }
    finally {
      clearStatusMessage();
    }
  }

  public void removeCurrentArea() {
    currentArea = null;
  }

/**
 * imports and area from a file, if it is a GIS file will be listed as spatialrelate,
 * and will need to import associated files based on passed file, otherwise will simply
 * read area files.
 * @param file file path provided by chooser
 * @return
 * @throws SimpplleError
 */
  public boolean importArea(File file) throws SimpplleError {
    String extension = Utility.getFileExtension(file);
    if (extension == null) {
      throw new SimpplleError("Invalid import file");
    }

    File       prefix = Utility.stripExtension(file);
    ImportArea importer = new ImportArea();

    if (extension.equalsIgnoreCase("spatialrelate")) {
      currentArea = importer.importNewFiles(file);
    }
    else {
      currentArea = importer.readFiles(prefix);
    }

    return importer.attributesAdded();
  }

  public void importAttributeData(File file) throws SimpplleError {
    ImportArea importer = new ImportArea();
    importer.readAtributesFile(file);
  }

  /**
   * Saves the current area in the Area input file format.
   * Simulation data is also saved if present.
   * The file saved as a gzip to conserve disk space.
   * @param outputFile is a File.
   */

  public void saveCurrentArea(File outputFile) throws SimpplleError {
    saveCurrentArea(outputFile,true);
  }
  public void saveCurrentArea (File outputFile, boolean saveSimulation) throws SimpplleError {
    File prefix = Utility.stripExtension(outputFile);
    outputFile = Utility.makeSuffixedPathname(prefix,"","area");

//    if (simpplle.JSimpplle.developerMode()) {
      saveCurrentAreaNew(outputFile);

      if (getCurrentSimulation() != null && saveSimulation) {
        Simpplle.setStatusMessage("Saving Simulation Data");
        File newFile = Utility.makeNumberedPathname(prefix,1,"simdata");
        this.saveSimulation(newFile,1);
      }
      Simpplle.clearStatusMessage();
//      return;
//    }

//    AreaSummary      areaSummary = getAreaSummary();
//    Area             area        = getCurrentArea();
//    GZIPOutputStream out;
//    PrintWriter      fout;
//    String  dir,name;
//
//    if (area == null) {
//      System.out.println("No Area currently Loaded.");
//      return;
//    }
//
//    dir  = outputFile.getParent();
//    name = outputFile.getName();
//
//    try {
//      outputFile = new File(dir,name + ".area");
//
//      out = new GZIPOutputStream(new FileOutputStream(outputFile));
//      fout = new PrintWriter(out);
//
//      area.saveArea(fout);
//      if (currentSimulation != null) {
//        currentSimulation.saveSimulation(fout);
//      }
//      if (areaSummary != null) {
//        areaSummary.saveSimulation(fout);
//      }
//
//      fout.flush();
//      out.close();
//      fout.close();
//    }
//    catch (IOException IOX) {
//      System.out.println("Problems writing output file.");
//    }
  }

  public void saveCurrentAreaNew(File filename) throws SimpplleError {
    saveSerializedArea(filename);
  }

  public void createGisUpdateSpreadFiles(File file, Lifeform lifeform) throws SimpplleError {
    if (currentArea != null && getCurrentSimulation() != null && getAreaSummary() != null) {
      getCurrentSimulation().writePrefixFileSingleRun(file,true);
      currentArea.produceArcFiles(file,lifeform,false);
      currentArea.produceSpreadArcFiles(file);
    }
    else {
      throw new SimpplleError("A Previously Simulated Area must be loaded first.");
    }
  }

  /**
   * This method creates GIS data files for use in Arcview.
   * The files contains information on decade probabilities
   * for species, size class, density, and processes
   * There is one file for each time step.
   * @param file is a File, the output file.
   */
  public void createGisDecadeProbabilityFiles(File file) throws SimpplleError {
    if (currentArea != null && getAreaSummary() != null) {
      currentArea.produceDecadeProbabilityArcFiles(file);
    }
    else {
      throw new SimpplleError("A Previously Simulated Area must be loaded first.");
    }
  }

  /**
   * Runs a simulation.
   */
  public void runSimulation(int numSimulations, int numSteps,
                            boolean suppression, File outputFile,
                            float discount, boolean spArea, boolean owner,
                            boolean yearlySteps, String simulationMethod,
                            boolean writeDatabase, boolean writeAccess,
                            boolean writeProbFiles,
                            Simulation.InvasiveKind invasiveKind, int nStepsInMemory,
                            File allStatesRulesFile, boolean discardData,
                            boolean disableReporting, boolean doAllStatesSummary,
                            boolean doTrackingSpeciesReport,
                            boolean doGisFiles)
    throws SimpplleError {
    Simulation.setInstance(
      new Simulation(numSimulations,
              numSteps,
              suppression,
              outputFile,
              discount,
              spArea,
              owner,
              yearlySteps,
              simulationMethod,
              writeDatabase,
              writeAccess,
              writeProbFiles,
              invasiveKind,
              nStepsInMemory,
              allStatesRulesFile,
              discardData,
              disableReporting,
              doAllStatesSummary,
              doTrackingSpeciesReport,
              doGisFiles));
    getCurrentSimulation().runSimulation();
  }

  /**
   * This function only used in the gui. It is used to restore the units to their original state, as well as deleting
   * any simulation related stuff. This is no longer used before a simulation, the resetting of things is done as
   * needed when the simulation is run. The name resetSimulation is kept primary for consistency with prior versions
   * of this software.
   */
  public static void resetSimulation() {
    clearSimulation();
    if (currentArea != null) {
      currentArea.restoreInitialConditions();
    }
  }

  public static void makeAreaSimulationReady() {
    currentArea.makeSimulationReady();
    clearSimulation();
  }

  /**
   * This method saves the current area
   * in a human readable format.
   * @param file of type File is file to output to.
   */
  public void printCurrentArea (File file) throws SimpplleError {
    if (currentArea != null) {
      currentArea.printAll(file);
    }
    else {
      System.out.println("No Area currently Loaded.");
    }
  }

  /**
   * This Method prints out a summary report to a file.
   * The report is a summary of acres for processes
   * in Evu instances broken down by time step.
   * @param file is a File
   */
  public void summaryReport(File file, int format, boolean combineLifeforms) throws SimpplleError {
    if (getCurrentSimulation() == null && currentArea == null) {
      throw new SimpplleError("No Area loaded, or No Simulation has Ran.");
    }
    switch (format) {
      case FORMATTED:
        Reports.generateSummaryReport(file,Reports.NORMAL,combineLifeforms);
        break;
      case FORMATTED_OWNERSHIP:
        Reports.generateSummaryReport(file,Reports.OWNERSHIP,combineLifeforms);
        break;
      case FORMATTED_SPECIAL_AREA:
        Reports.generateSummaryReport(file,Reports.SPECIAL_AREA,combineLifeforms);
        break;
      case FORMATTED_OWNER_SPECIAL:
        Reports.generateSummaryReport(file,Reports.OWNER_SPECIAL,combineLifeforms);
        break;
      case CDF:
        Reports.generateSummaryReportCDF(file,Reports.NORMAL,combineLifeforms);
        break;
      case CDF_OWNERSHIP:
        Reports.generateSummaryReportCDF(file,Reports.OWNERSHIP,combineLifeforms);
        break;
      case CDF_SPECIAL_AREA:
        Reports.generateSummaryReportCDF(file,Reports.SPECIAL_AREA,combineLifeforms);
        break;
      case CDF_OWNER_SPECIAL:
        Reports.generateSummaryReportCDF(file,Reports.OWNER_SPECIAL,combineLifeforms);
        break;
      default:
        return;
    }
  }

  /**
   * This Method prints out a summary report to a file.
   * This report show means, and min/max values for
   * processes, species, size class, and density broken
   * down by time step.
   * @param file is a File
   */
   public void multipleRunSummaryReport(File file) {
    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
    }
    if (getCurrentSimulation().getNumSimulations() > 1) {
      getCurrentSimulation().getMultipleRunSummary().summaryReport(file);
    }
    else {
      System.out.println("No Multiple Run Simulation performed.");
    }
  }

  public void saMultipleRunSummaryReport(File file) {
    MultipleRunSummary mrSummary = getCurrentSimulation().getMultipleRunSummary();

    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
    }
    if (getCurrentSimulation().trackSpecialArea() != true) {
      System.out.println("Special Area data data was not tracked during" +
                          " the simulation");
      return;
    }
    if (getCurrentSimulation().getNumSimulations() > 1) {
      mrSummary.specialAreaSummaryReport(file);
    }
    else {
      System.out.println("No Multiple Run Simulation performed.");
    }
  }

  public void ownershipMultipleRunSummaryReport(File file) {
    MultipleRunSummary mrSummary = getCurrentSimulation().getMultipleRunSummary();

    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
    }
    if (getCurrentSimulation().trackOwnership() != true) {
      System.out.println("Ownership data data was not tracked during" +
                          " the simulation");
      return;
    }
    if (getCurrentSimulation().getNumSimulations() > 1) {
      mrSummary.ownershipSummaryReport(file);
    }
    else {
      System.out.println("No Multiple Run Simulation performed.");
    }
  }

  /**
   * This Method prints out a Fire Spread report to a file.
   * @param file is a File
   */
  public void fireSpreadReport(File file) {
    AreaSummary areaSummary;

    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
      return;
    }

    areaSummary = getAreaSummary();
    if (areaSummary == null) {
      System.out.println("No Area Summary exists.");
    }
    else {
      areaSummary.fireSpreadReport(file);
    }
  }

  /**
   * This Method prints out a Fire Suppression Cost report to a file.
   * @param file is a File
   */
  public void fireSuppressionCostReport(File file) {
    AreaSummary areaSummary;

    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
      return;
    }

    areaSummary = getAreaSummary();
    if (areaSummary == null) {
      System.out.println("No Area Summary exists.");
    }
    else {
      areaSummary.fireSuppressionCostReport(file);
    }
  }

  /**
   * This Method prints out a Emissions report to a file.
   * @param file is a File
   */
  public void emissionsReport(File file) throws SimpplleError {
    AreaSummary areaSummary;

    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
      return;
    }

    areaSummary = getAreaSummary();
    if (areaSummary == null) {
      System.out.println("No Area Summary exists.");
    }
    else {
      areaSummary.emissionsReport(file);
    }
  }

  public void emissionsReportCDF(File file) throws SimpplleError {
    AreaSummary areaSummary;

    if (getCurrentSimulation() == null || currentArea == null) {
      System.out.println("No Area loaded, or No Simulation has Ran.");
      return;
    }

    areaSummary = getAreaSummary();
    if (areaSummary == null) {
      System.out.println("No Area Summary exists.");
    }
    else {
      areaSummary.emissionsReportCDF(file);
    }
  }


  private static void debug(String str) {
    System.out.println(str);
  }

  private boolean isNewAreaFile(File file) {
    try {
      GZIPInputStream stream=null;
      ObjectInputStream s=null;
      try {
        stream = new GZIPInputStream(new FileInputStream(file));
        s = new ObjectInputStream(stream);
        int version = s.readInt();
        s.close();
        stream.close();
        return true;
      }
      catch (StreamCorruptedException ex) {
        s.close();
        stream.close();
        return false;
      }
    }
    catch (Exception ex) {
      return false;
    }
  }

  public void readSerializedArea(File areafile, File simfile) throws SimpplleError {
    readSerializedArea(areafile,simfile,false,null);
  }
  public void readSerializedArea(Area area) throws SimpplleError {
    readSerializedArea(null,null,true,area.getPath());
  }
  public void readSerializedArea(File areafile, File simfile,
                                 boolean sampleArea, String path)
    throws SimpplleError
  {
    try {
      InflaterInputStream stream;
      if (sampleArea) {
        stream = SystemKnowledge.getSampleAreaStream(path);
      }
      else {
        stream = new GZIPInputStream(new FileInputStream(areafile));
      }

      readSerializedArea(stream);
      stream.close();

      if (simfile != null && !sampleArea) {
        stream = new GZIPInputStream(new FileInputStream(simfile));
        readSimulationData(stream);
        getCurrentSimulation().restoreOutfile(simfile);
        stream.close();
      }
    }
    catch (IOException ex) {
      throw new SimpplleError(ex.getMessage());
    }
  }
  private void readSerializedArea(InflaterInputStream stream) throws SimpplleError {
    int     size;
    Species species;
      try {
        ObjectInputStream s = new MyObjectInputStream(stream);
        int version = s.readInt();
        currentArea = (Area)s.readObject();
        stream.close();
      }
      catch (ClassNotFoundException ex) {
        throw new SimpplleError(ex.getMessage(),ex);
      }
      catch (IOException ex) {
        ex.printStackTrace();
        throw new SimpplleError(ex.getMessage(),ex);
      }
  }

  private void readSimulationData(InflaterInputStream stream) throws SimpplleError {
    try {
      ObjectInputStream s = new MyObjectInputStream(stream);

      int version = s.readInt();

      int runNum = s.readInt();
      int nRuns  = s.readInt();

      String newAreaName;
      boolean mrSummary = false;
      if (runNum == -1 || (runNum == nRuns && recreateMrSummary)) {
        mrSummary = true;
        newAreaName = currentArea.getName() +
                      " (Run #" + nRuns + " of " + nRuns + " with Multiple Run Summary)";
      }
      else {
        newAreaName =
            currentArea.getName() + " (Run #" + runNum + " of " + nRuns + ")";
      }
      currentArea.readExternalSimData(s,runNum-1);
      currentArea.setName(newAreaName);
      Simulation.setInstance( (Simulation) s.readObject());

      if (version > 2) {
        Lifeform.readExternalSimIdHm(s);
        ProcessType.readExternalSimIdHm(s);
        Species.readExternalSimIdHm(s);
        SizeClass.readExternalSimIdHm(s);
        Density.readExternalSimIdHm(s);
        InclusionRuleSpecies.readExternalSimIdHm(s);
        HabitatTypeGroupType.readExternalSimIdHm(s);
      }

      if (runNum == -1) {
        getCurrentSimulation().readMultipleRunSummary(s);
      }
      else if (runNum == nRuns && recreateMrSummary) {
        getCurrentSimulation().recreateMultipleRunSummary();
      }
    }

    catch (IOException ex) {
      throw new SimpplleError(ex.getMessage(),ex);
    }
    catch (ClassNotFoundException ex) {
      throw new SimpplleError(ex.getMessage(),ex);
    }
  }
  /**
   * This method writes the simulation data into a separate file.
   * @param filename
   * @param currentRun
   * @throws SimpplleError
   */
  public void saveSimulation(File filename, int currentRun) throws SimpplleError {
    try {
      GZIPOutputStream gOut = new GZIPOutputStream(new FileOutputStream(filename));
      ObjectOutputStream s = new ObjectOutputStream(gOut);
      saveSimulationData(s,currentRun);
      s.flush();
      gOut.flush();
      gOut.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
      Simpplle.clearStatusMessage();
    }
  }

  private void saveSerializedArea(File filename) throws SimpplleError {
    try {
      GZIPOutputStream gOut = new GZIPOutputStream(new FileOutputStream(filename));
      saveSerializedArea(gOut);
      gOut.flush();
      gOut.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void saveSerializedArea(GZIPOutputStream stream) throws SimpplleError {
    try {
      ObjectOutputStream s = new ObjectOutputStream(stream);
      s.writeInt(version);
      s.writeObject(currentArea);
      Simpplle.setStatusMessage("Finished Saving Units");

      s.flush();
      s.close();
    }
    catch (IOException ex) {
      Simpplle.clearStatusMessage();
      throw new SimpplleError(ex.getMessage(),ex);
    }
  }

  private void saveSimulationData(ObjectOutputStream s, int currentRun)
    throws SimpplleError, IOException
  {
    s.writeInt(version);
    s.writeInt(currentRun);
    int nRuns = getCurrentSimulation().getNumSimulations();
    s.writeInt(nRuns);
    currentArea.writeExternalSimData(s);
    s.writeObject(getCurrentSimulation());

    Lifeform.writeExternalSimIdHm(s);
    ProcessType.writeExternalSimIdHm(s);
    Species.writeExternalSimIdHm(s);
    SizeClass.writeExternalSimIdHm(s);
    Density.writeExternalSimIdHm(s);
    InclusionRuleSpecies.writeExternalSimIdHm(s);
    HabitatTypeGroupType.writeExternalSimIdHm(s);

    if (currentRun == -1) {
      getCurrentSimulation().saveMultipleRunSummary(s);
    }

    Simpplle.setStatusMessage("Finished Saving Simulation data");
  }
}

