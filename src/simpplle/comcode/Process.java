package simpplle.comcode;

import java.io.*;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.*;
import java.util.*;
import java.awt.Color;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-0.9.  By copying this file the user accepts the University of Montana 
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class is abstract and serves to create a common parent for
 * the numerous Process subclasses.  It also allows the instances
 * of the subclasses to be cast to type Process for storage in a
 * data structure such as an array or more commonly an arraylist, because the amount of processes for an EVU 
 * is often a variable quantity .  This class also contains some
 * abstract methods which are implemented by its subclasses.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Kirk A. Moeller
 *
 */

public abstract class Process
{
  protected static final int probLogicVersion = 1;

  protected Color color = Color.CYAN;

  protected boolean        spreading;
  protected int[][][]      probData;
  protected File           spreadFile;
  protected static boolean changed;
  protected boolean        spreadChanged;
  protected String         description;
  protected ProcessType    processType;
  protected boolean        yearlyProcess;
  protected boolean        uniqueUI;

  protected ArrayList rules;
  protected String[]  probLabels;
  protected ArrayList<String>  defaultVisibleColumns = new ArrayList<String>();

  protected ArrayList<Lifeform> validLifeforms;

  private static Hashtable     processHt;
  private static ArrayList<ProcessType> legalProcesses;
  private static ArrayList     simulationProcesses;
  private static ArrayList     summaryProcesses;
  private static ArrayList<ProcessType> probLogicProcesses;

  private static HashMap<Lifeform,ArrayList<ProcessType>> lifeformSimProcesses;

//  private static Hashtable     aquaticProcessHt;
  private static ProcessType[] aquaticLegalProcesses;
  private static ProcessType[] aquaticSimulationProcesses;
  private static ProcessType[] aquaticSummaryProcesses;
  /**
   * Constructor for process. Initilizes process description, spreading, yearly process, valid lifeforms and uniqueUI.
   */
  protected Process () {
    super();
    description = null;
    spreading = false;
    yearlyProcess = true;
    validLifeforms = new ArrayList<Lifeform>(5);
    uniqueUI = false;
  }
/**
 * Gets the default base logic default visible columns for Process.  This will be used in the GUI table models.  
 * @return arraylist of all default visible columns by their string name.  
 */
  public ArrayList<String> getDefaultVisibleColumns() {
    return defaultVisibleColumns;
  }
  /**
   * Adds a lifeform to the valid lifeforms arraylist.  Possible lifeforms are trees, shrubs, herbacious, agriculture, and no classifciation.
   * @param lifeform
   */
  public void addValidLifeform(Lifeform lifeform) {
    validLifeforms.add(lifeform);
  }
  /**
   * Checks if a lifeform is in the validLifeForms arraylist.  Lifeforms are trees, shrubs, herbacious, agriculture, and no classifciation.
   * @param lifeform
   * @return
   */
  public boolean isValidLifeform(Lifeform lifeform) {
    return validLifeforms.contains(lifeform);
  }
  /**
   * Gets the color of Process.  Used in GUI.
   * @return
   */
  public Color getColor() { return color; }
/**
 * Sets whether this process is a spreading process
 * @return true if this process is a spreading process
 */
  public boolean isSpreading () {
    return spreading;
  }
/**
 * Checks if process is a unique user interface
 * @return true if unique user interface
 */
  public boolean isUniqueUI() { return uniqueUI; }
/**
 * Checks if this is a yearly process.
 * @return true if yearly process
 */
  public boolean isYearly() { return yearlyProcess; }
  /**
   * Sets whether this process is a yearly process.    
   * @param isYearly 
   */
  public void setYearlyStatus(boolean isYearly) {
    yearlyProcess = isYearly;
  }
/**
 * Gets the process type.
 * @return 
 */
  public ProcessType getType() { return processType; }
  public void setType(ProcessType processType) { this.processType = processType; }

  /**
   * Gets the appropriate process next state probability from
   * the VegetativeType associated with the Evu in the parameter.
   * This is the default doProbability method which subclasses
   * end up calling if they don't implement their own method.
   * @param evu is an Evu.
   * @return an int, the process probability
   *
   */
  protected int doProbability (Evu evu) {
    VegSimStateData state = evu.getState();
    if (state == null) { return 0; }

    return state.getVeg().getProcessProbability(this);
  }

  /**
   * Called during a simulation to get the probability of a
   * given Process occurring in a given Evu.
   * Casts the provided RegionalZone parameter to the correct
   * subclass of RegionalZone so that the correct doProbability
   * method can be called.
   * @param zone is a RegionalZone.
   * @param evu is an Evu.
   */
  protected int doProbability (RegionalZone zone, Evu evu) {
    int zoneId = zone.getId();
    int result;

    if (validLifeforms == null || validLifeforms.size() == 0) {
      Area.currentLifeform = null;
    }
    else if (Simpplle.getCurrentArea().multipleLifeformsEnabled() == false)  {
      Area.currentLifeform = null;
    }
//    else if (Simpplle.getCurrentArea().multipleLifeformsEnabled())  {
//      int cStep = Simpplle.getCurrentSimulation().getCurrentTimeStep();
//      Area.currentLifeform = validLifeforms.get(0);
//      if (evu.hasLifeform(Area.currentLifeform,cStep) == false) {
//        System.out.println(evu.getId());
//        Area.currentLifeform = null;
//        return 0;
//      }
//    }
//    else {
//      Area.currentLifeform = null;
//    }

    if (ProcessProbLogic.hasLogic(this.getType()) &&
        isUniqueUI() == false) {
      return ProcessProbLogic.getInstance().getProbability(this.getType(),evu);
    }

    switch (zoneId) {
      case ValidZones.WESTSIDE_REGION_ONE:
        result = doProbability((WestsideRegionOne) zone, evu);
        break;
      case ValidZones.EASTSIDE_REGION_ONE:
        result = doProbability((EastsideRegionOne) zone, evu);
        break;
      case ValidZones.TETON:
        result = doProbability((Teton) zone, evu);
        break;
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
        result = doProbability((NorthernCentralRockies) zone, evu);
        break;
      case ValidZones.SIERRA_NEVADA:
        result = doProbability((SierraNevada) zone, evu);
        break;
      case ValidZones.SOUTHERN_CALIFORNIA:
        result = doProbability((SouthernCalifornia) zone, evu);
        break;
      case ValidZones.GILA:
        result = doProbability((Gila)zone,evu);
        break;
      case ValidZones.SOUTH_CENTRAL_ALASKA:
        result = doProbability((SouthCentralAlaska) zone, evu);
        break;
      case ValidZones.SOUTHWEST_UTAH:
        result = doProbability((SouthwestUtah)zone,evu);
        break;
      case ValidZones.COLORADO_FRONT_RANGE:
        result = doProbability((ColoradoFrontRange)zone,evu);
        break;
      case ValidZones.COLORADO_PLATEAU:
        result = doProbability((ColoradoPlateau)zone,evu);
        break;
      case ValidZones.WESTERN_GREAT_PLAINS_STEPPE:
        result = doProbability((WesternGreatPlainsSteppe)zone,evu);
        break;
      case ValidZones.GREAT_PLAINS_STEPPE:
        result = doProbability((GreatPlainsSteppe)zone,evu);
        break;
      case ValidZones.MIXED_GRASS_PRAIRIE:
        result = doProbability((MixedGrassPrairie)zone,evu);
        break;
      default:
        result = 0;
    }

    Area.currentLifeform = null;

    return result;
  }

  /**
   * Calculates the Process probability for the a given zone.
   * Implemented by subclasses of Process. -defaults to 0 unless overwritten in subclass
   * @param zone a subclass of RegionalZone
   * @param evu .
   * @return the probability.
   */
  protected int doProbability (WestsideRegionOne        zone, Evu evu) { return 0; }
  protected int doProbability (EastsideRegionOne        zone, Evu evu) { return 0; }
  protected int doProbability (Teton                    zone, Evu evu) { return 0; }
  protected int doProbability (NorthernCentralRockies   zone, Evu evu) { return 0; }
  protected int doProbability (SierraNevada             zone, Evu evu) { return 0; }
  protected int doProbability (SouthernCalifornia       zone, Evu evu) { return 0; }
  protected int doProbability (Gila                     zone, Evu evu) { return 0; }
  protected int doProbability (SouthCentralAlaska       zone, Evu evu) { return 0; }
  protected int doProbability (SouthwestUtah            zone, Evu evu) { return 0; }
  protected int doProbability (ColoradoFrontRange       zone, Evu evu) { return 0; }
  protected int doProbability (ColoradoPlateau          zone, Evu evu) { return 0; }
  protected int doProbability (WesternGreatPlainsSteppe zone, Evu evu) { return 0; }
  protected int doProbability (GreatPlainsSteppe        zone, Evu evu) { return 0; }
  protected int doProbability (MixedGrassPrairie        zone, Evu evu) { return 0; }

  /**
   * Gets a probability value from the data structure that
   * holds data for Bark Beetle Probability.
   * @param grouping an int, refers to the diagram page or other grouping.
   * @param row an int, refers the row in the probability diagram.
   & param col an int, 0 = LSF or MSF, 1 = No Past Fire.
   * @return the probability.
   */
  public int getProbData(int grouping, int row, int col) {
    return probData[grouping][row][col];
  }

  public void setProbData(int grouping, int row, int col, int value) {
    if (probData[grouping][row][col] == value) { return; }

    markChanged();
    probData[grouping][row][col] = value;
  }

  public int getProbGroupingCount() { return probData.length; }
  
  public int getProbRowCount(int group) {
    if (probData != null && group >= 0 && group < probData.length) {
      return probData[group].length;
    }
    else { return 0; }
  }

  /**
    * Load a user provided Probability data file.
    */
  public static void loadData (File infile) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader  fin;

    try {
      gzip_in = new GZIPInputStream(new FileInputStream(infile));
      fin = new BufferedReader(new InputStreamReader(gzip_in));

      readProbDataFile(fin);
      setFilename(infile);
      fin.close();
      gzip_in.close();
    }
    catch (IOException e) {
      String msg = "Problems reading from Probability data file:" + infile;
      throw new SimpplleError(msg);
    }
  }

  public static void loadProbData(InputStream is) throws SimpplleError {
    GZIPInputStream gzip_in;
    BufferedReader fin;

    try {
      gzip_in = new GZIPInputStream(is);
      fin = new BufferedReader(new InputStreamReader(gzip_in));
      readProbDataFile(fin);
      // *** Important ***
      // DO NOT CLOSE THESE STREAMS.
      // IT WILL CAUSE READING FROM JAR FILES TO FAIL.
    }
    catch (IOException e) {
      String msg = "Problems reading from " +
                   " Probability data file\n" +
                   "knowledge.jar file may be missing or damaged.";
      throw new SimpplleError(msg);
    }
  }
/**
 * Reads the probability file.
 * @param fin
 * @throws SimpplleError
 * @throws IOException
 */
  public static void readProbDataFile(BufferedReader fin)
    throws SimpplleError, IOException
  {
    String       processName;
    Process      process;
    ProcessType  processType;
    boolean      eof = false;
    RegionalZone zone = Simpplle.getCurrentZone();

    processName = fin.readLine();
    if (processName == null) {
      throw new SimpplleError("Invalid or emtpy probability file.");
    }
    processType = ProcessType.get(processName);
    if (processType == null) {
      throw new SimpplleError("Invalid Process in probability file.");
    }

    while (!eof) {
      process = (Process)processHt.get(processType);
      if (process == null) {
        throw new SimpplleError("Invalid process: " + processName +
                                " found in data file");
      }
      process.readData(fin);
      processName = fin.readLine();
      if (processName == null) { eof = true; continue; }

      processType = ProcessType.get(processName);
      if (processType == null) {
        throw new SimpplleError("Invalid Process in probability file: " + processName);
      }
    }
  }

  /**
   * 
   *Probability data is organized into pages, rows, and columns;
  *Lines in the input file are the groupings (e.g. pages).  Each line has the rows comma delimited.
  *Each row is colon delimited and usually has two values.
  *The two values usually are light and severe probabilities.
  *Note: The first line of the input file is the number
  *  of lines in the file.
   * 
   * @param fin
   * @throws SimpplleError IOException, Parse Error, Number Format Exception caught in GUI
   */
  private void readData(BufferedReader fin) throws SimpplleError {
    String              line;
    StringTokenizerPlus strTok, values;
    int                 numGroups, numRows, numCols;
    int                 grouping, row, col;

    try {
      line = fin.readLine();
      if (line == null) {
        throw new ParseError("Probability Data file is empty.");
      }

      numGroups = Integer.parseInt(line);
      probData = new int[numGroups][][];

      for(grouping=0;grouping<numGroups;grouping++) {
        line    = fin.readLine();
        strTok  = new StringTokenizerPlus(line,",");
        numRows = strTok.countTokens();
        probData[grouping] = new int[numRows][];

        for(row=0;row<numRows;row++) {
          values = new StringTokenizerPlus(strTok.getToken(),":");
          numCols = values.countTokens();
          probData[grouping][row] = new int[numCols];
          for(col=0;col<numCols;col++) {
            setProbData(grouping,row,col,values.getIntToken());
          }
        }
      }
      setChanged(false);
    }
    catch (NumberFormatException nfe) {
      String msg = "Invalid value found in probability data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
    catch (ParseError pe) {
      System.out.println(pe.msg);
      throw new SimpplleError(pe.msg);
    }
    catch (IOException e) {
      String msg = "Problems read from Probability data file.";
      System.out.println(msg);
      throw new SimpplleError(msg);
    }
  }
/**
 * Returns the current boolean which marks whether a process has changed.  
 * @return true if process has changed
 */
  public static boolean hasChanged() { return changed; }
  /**
   * Marks the SystemKnowledge changed for insect disease probability.  
   */
  protected static void markChanged() {
    setChanged(true);
    SystemKnowledge.markChanged(SystemKnowledge.INSECT_DISEASE_PROB);
  }
  protected static void setChanged(boolean value) { changed = value; }
/**
 * sets the insect disease probability file and marks the system knowledge changed.  
 * @param file
 */
  protected static void setFilename(File file) {
    SystemKnowledge.setFile(SystemKnowledge.INSECT_DISEASE_PROB,file);
    SystemKnowledge.markChanged(SystemKnowledge.INSECT_DISEASE_PROB);
  }
/**
 * clears the insect disease probability file
 */
  protected static void clearFilename() {
    SystemKnowledge.clearFile(SystemKnowledge.INSECT_DISEASE_PROB);
  }
/**
 * 
 * @return based on whether the spread knowledge has changed
 */
  public boolean hasSpreadChanged() { return spreadChanged; }
  /**
   * marks the set spreadchanged method to true
   */
  protected void markSpreadChanged() { setSpreadChanged(true); }
  /**
   * 
   * @param value true if changed
   */
  protected void setSpreadChanged(boolean value) { spreadChanged = value; }
  /**
   * gets the spread file
   */
  public File getSpreadFilename() { return spreadFile; }
 
  /**
   * sets the spread file as the file passed in
   * @param file
   */
  protected void setSpreadFilename(File file) { spreadFile = file; }
  /**
   * Clears the spread file name by changing the file name to null.
   */
  protected void clearSpreadFilename() { spreadFile = null; }

  public static void saveAs(File outfile) {
    setFilename(Utility.makeSuffixedPathname(outfile,"","probability"));
    save();
  }
/**
 * method to save to System knowledge insect disease probability file and sets changed 
 * @throws IOException 
 */
  public static void save() {
    File             outfile = SystemKnowledge.getFile(SystemKnowledge.INSECT_DISEASE_PROB);
    GZIPOutputStream out;
    PrintWriter      fout;
    RegionalZone     zone = Simpplle.getCurrentZone();

    try {
      out = new GZIPOutputStream(new FileOutputStream(outfile));
      fout = new PrintWriter(out);
    }
    catch (IOException e) {
      System.out.println("Problems opening output file.");
      return;
    }

    saveProcesses(fout);
    fout.flush();
    fout.close();

    setChanged(false);
  }
/**
 * Saves the current zone user probability processes. 
 * @param fout uses printwriter to print
 */
  public static void saveProcesses(PrintWriter fout) {
    RegionalZone zone = Simpplle.getCurrentZone();
    ProcessType[] probProcesses = zone.getUserProbProcesses();
    if (probProcesses == null) { return; }
    Process  process;
    for(int i=0; i<probProcesses.length; i++) {
      process = (Process)processHt.get(probProcesses[i]);
      process.save(fout);
    }
  }
/**
 * Method to save process information.  
 * @param fout
 */
  protected void save(PrintWriter fout) {
    int groupCount;
    int group, row, col;

    groupCount = getProbGroupingCount();
    fout.println(toString());
    fout.println(getProbGroupingCount());

    for(group=0;group<groupCount;group++) {
      fout.print(getProbData(group,0,0) + ":" + getProbData(group,0,1));
      for(row=1;row<getProbRowCount(group);row++) {
        fout.print(",");
        fout.print(getProbData(group,row,0) + ":" + getProbData(group,row,1));
      }
      fout.println();
    }
  }
/**
 * Closes file by making the clearing the SystemKnowledge.INSECT_DISEASE_PROB and sets the changed boolean for this process to false
 */
  public static void closeFile() {
    clearFilename();
    setChanged(false);
  }
  /**
   * Closes file by making the clearing spreadfile and sets the changed boolean for this process to false
   */
  public void closeSpreadFile() {
    clearSpreadFilename();
    setSpreadChanged(false);
  }

  /**
   * Called during a simulation to determine if a given process
   * spreads Evu (fromEvu) to another (evu).
   * Casts the provided RegionalZone parameter to the correct
   * subclass of RegionalZone so that the correct doSpread
   * method can be called.
   * In the case where all zones do the same thing for a given process
   * the process subclass may override this method.
   * @param zone is a RegionalZone.
   * @param fromEvu is an Evu.
   * @param evu is an Evu.
   */
  protected boolean doSpread (RegionalZone zone, Evu fromEvu, Evu evu) {
    int zoneId = zone.getId();
    boolean result;

    switch (zoneId) {
      case ValidZones.WESTSIDE_REGION_ONE:
        result = doSpread((WestsideRegionOne)zone, fromEvu, evu);
        break;
      case ValidZones.EASTSIDE_REGION_ONE:
        result = doSpread((EastsideRegionOne)zone, fromEvu, evu);
        break;
      case ValidZones.TETON:
        result = doSpread((Teton)zone, fromEvu, evu);
        break;
      case ValidZones.NORTHERN_CENTRAL_ROCKIES:
        result = doSpread((NorthernCentralRockies)zone, fromEvu, evu);
        break;
      case ValidZones.SIERRA_NEVADA:
        result = doSpread((SierraNevada)zone, fromEvu, evu);
        break;
      case ValidZones.SOUTHERN_CALIFORNIA:
        result = doSpread((SouthernCalifornia)zone, fromEvu, evu);
        break;
      case ValidZones.GILA:
        result = doSpread((Gila)zone,fromEvu,evu);
        break;
      case ValidZones.SOUTH_CENTRAL_ALASKA:
        result = doSpread((SouthCentralAlaska)zone, fromEvu, evu);
        break;
      case ValidZones.SOUTHWEST_UTAH:
        result = doSpread((SouthwestUtah)zone,fromEvu,evu);
        break;
      case ValidZones.COLORADO_FRONT_RANGE:
        result = doSpread((ColoradoFrontRange)zone,fromEvu,evu);
        break;
      case ValidZones.COLORADO_PLATEAU:
        result = doSpread((ColoradoPlateau)zone,fromEvu,evu);
        break;
      case ValidZones.WESTERN_GREAT_PLAINS_STEPPE:
        result = doSpread((WesternGreatPlainsSteppe)zone,fromEvu,evu);
        break;
      case ValidZones.GREAT_PLAINS_STEPPE:
        result = doSpread((GreatPlainsSteppe)zone,fromEvu,evu);
        break;
      case ValidZones.MIXED_GRASS_PRAIRIE:
        result = doSpread((MixedGrassPrairie)zone,fromEvu,evu);
        break;
      default:
        result = false;
    }
    return result;
  }

  /**
   * Determines whether or not a Process spreads for the
   * given zone. Implemented by subclasses of Process.
   * @param zone is a subclass of RegionalZone.
   * @param fromEvu is an Evu.
   * @param evu is an Evu.
   * @return a boolean, true = Process does spread.
   */
  protected boolean doSpread (WestsideRegionOne        zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (EastsideRegionOne        zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (Teton                    zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (NorthernCentralRockies   zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (SierraNevada             zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (SouthernCalifornia       zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (Gila                     zone, Evu fromEvu, Evu evu)  { return false; }
  protected boolean doSpread (SouthCentralAlaska       zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (SouthwestUtah            zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (ColoradoFrontRange       zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (ColoradoPlateau          zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (WesternGreatPlainsSteppe zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (GreatPlainsSteppe        zone, Evu fromEvu, Evu evu) { return false; }
  protected boolean doSpread (MixedGrassPrairie        zone, Evu fromEvu, Evu evu) { return false; }

  public String toString() { return getType().toString(); }

  public String getDescription() {
    return (description == null) ? getType().toString() : description;
  }

/**
 * 
 * @return probability logic processes arraylist
 */
  public static ArrayList getProbLogicProcesses() {
    return probLogicProcesses;
  }
  /**
   * 
   * @return simulation processes arraylist
   */
  public static ArrayList getSimulationProcesses() {
    return simulationProcesses;
  }

  private static HashMap<Lifeform,ArrayList<ProcessType>> lifeformSimProcessesNoFireEvent;

  private static void makeNoFireEventSimProcessCopy() {
    lifeformSimProcessesNoFireEvent = new HashMap<Lifeform,ArrayList<ProcessType>>();

    for (Lifeform life : lifeformSimProcesses.keySet()) {
      ArrayList<ProcessType> processes = lifeformSimProcesses.get(life);
      ArrayList<ProcessType> newList = new ArrayList<ProcessType>();
      for (ProcessType process : processes) {
        if (process != ProcessType.FIRE_EVENT) {
          newList.add(process);
        }
      }
      lifeformSimProcessesNoFireEvent.put(life,newList);
    }
  }

  private static ArrayList<ProcessType> getSimulationProcessesNoFireEvent(Lifeform lifeform) {
    return lifeformSimProcessesNoFireEvent.get(lifeform);
  }
  public static ArrayList<ProcessType> getSimulationProcesses(Lifeform lifeform) {
    return getSimulationProcesses(lifeform,true);
  }
  public static ArrayList<ProcessType> getSimulationProcesses(Lifeform lifeform, boolean includeFireEvent) {
    if (includeFireEvent) {
      return lifeformSimProcesses.get(lifeform);
    }
    else {
      return getSimulationProcessesNoFireEvent(lifeform);
    }
  }
/**
 * Gets all the lifeforms for a particular process.  Choices of lifeforms are TREES, SHRUBS, HERBACIOUS, AGRICULTURE, OR NA (for no classification)
 * @param process the process being evaluated.  
 * @return an arraylist of all the lifeforms for a particular process. 
 */
  public static ArrayList<Lifeform> getProcessLifeforms(ProcessType process) {
    ArrayList<Lifeform> result = new ArrayList<Lifeform>();
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      if (isMemberLifeformProcesses(allLives[i],process)) {
        result.add(allLives[i]);
      }
    }
    return result;
  }
  /**
   * check to see if lifeform object is a member of the lifeform Process type
   * @param lifeform
   * @param process
   * @return true if process type contains the lifeform
   */
  public static boolean isMemberLifeformProcesses(Lifeform lifeform, ProcessType process) {
    ArrayList<ProcessType> processes = getSimulationProcesses(lifeform);
    return processes.contains(process);
  }
  /**
   * 
   * @param process process type
   * @return true if there are more than one life form.  
   */
  public static boolean isMultiLifeformProcess(ProcessType process) {
    if (process.isFireProcess()) { process = ProcessType.FIRE_EVENT; }

    int lifeCount = 0;
    Lifeform[] allLives = Lifeform.getAllValues();
    for (int i=0; i<allLives.length; i++) {
      if (isMemberLifeformProcesses(allLives[i],process)) {
        lifeCount++;
      }
    }
    return lifeCount > 1;
  }
/**
 * Makes the simulation processes arraylist into a process type array.  
 * @return
 */
  public static ProcessType[] getSimulationProcessesArray() {
    return (ProcessType[]) simulationProcesses.toArray(new ProcessType[simulationProcesses.size()]);
  }
  /**
   * Makes the simulation summary processes arraylist into a process type array.  
   * @return the array with all the summary processes 
   */
  public static ProcessType[] getSummaryProcesses() {
    return (ProcessType[]) summaryProcesses.toArray(new ProcessType[summaryProcesses.size()]);
  }

  public static ProcessType[] getLegalProcesses() {
    return (ProcessType[]) legalProcesses.toArray(new ProcessType[legalProcesses.size()]);
  }

  public static ArrayList getLegalProcessesList() {
    return legalProcesses;
  }

//  public static String[] getLegalProcessNames() {
//    String[] names = new String[legalProcesses.length];
//    for (int i=0; i<legalProcesses.length; i++) {
//      names[i] = legalProcesses[i].toString();
//    }
//    return names;
//  }

  /**
   * writes legal processes, simulation processes, summary processes, probability logic processes
   * @param fout
   */
  public static void writeLegalFile(PrintWriter fout) {
    fout.println("// ** Legal Processes -  All, Simulation, Summary");

    int i=0;
    for (i=0; i<legalProcesses.size(); i++) {
      if (i>0) { fout.print(","); }
      fout.print(legalProcesses.get(i).toString());
    }
    fout.println();
    for (i=0; i<simulationProcesses.size(); i++) {
      if (i>0) { fout.print(","); }
      fout.print(simulationProcesses.get(i).toString());
    }
    fout.println();
    for (i=0; i<summaryProcesses.size(); i++) {
      if (i>0) { fout.print(","); }
      fout.print(summaryProcesses.get(i).toString());
    }
    fout.println();
    for (i=0; i<probLogicProcesses.size(); i++) {
      if (i>0) { fout.print(","); }
      fout.print(probLogicProcesses.get(i).toString());
    }
    fout.println();
  }
  /**
   * Reads, in order, process types, and put into arraylists for legal processes, simulation processes, 
   * summary processes, lifeform simulation processes, probability logic processes  
   * @param fin
   * @throws SimpplleError thrown for Invalid zone definition file, no process found, null processes found 
   * invalid processes found caught in GUI
   */
  
  public static void readLegalFile(BufferedReader fin) throws SimpplleError {
    StringTokenizerPlus strTok;
    String              line, str;
    ProcessType         processType;
    Process             process;

    try {
      processHt = new Hashtable();

      line = fin.readLine(); // Comment
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      legalProcesses = new ArrayList(strTok.countTokens());
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) {
          processType = new ProcessType(str.toUpperCase(),"DummyProcess",false);
        }

        process = processType.makeProcess();
        processHt.put(processType,process);
        if (processType.equals(ProcessType.STAND_REPLACING_FIRE)) {
          processHt.put(ProcessType.SRF,process);
        }
        else if (processType.equals(ProcessType.MIXED_SEVERITY_FIRE)) {
          processHt.put(ProcessType.MSF,process);
        }
        else if (processType.equals(ProcessType.LIGHT_SEVERITY_FIRE)) {
          processHt.put(ProcessType.LSF,process);
        }

        legalProcesses.add(processType);
      }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      simulationProcesses = new ArrayList(strTok.countTokens());
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

        simulationProcesses.add(processType);
      }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      summaryProcesses = new ArrayList(strTok.countTokens());
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

        summaryProcesses.add(processType);
      }


      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      probLogicProcesses = new ArrayList<ProcessType>(strTok.countTokens());
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

        process = Process.findInstance(processType);
        if (process == null) {
          process = processType.makeProcess();
        }
        processHt.put(processType,process);

        probLogicProcesses.add(processType);
      }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      while (line.indexOf("END") == -1) {
        if (lifeformSimProcesses == null) {
          lifeformSimProcesses = new HashMap<Lifeform,ArrayList<ProcessType>>(5);
        }
        strTok = new StringTokenizerPlus(line,",");
        if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

        Lifeform life = Lifeform.get(strTok.getToken());
        ArrayList<ProcessType> pList = new ArrayList<ProcessType>(strTok.countTokens());
        lifeformSimProcesses.put(life,pList);
        while (strTok.hasMoreElements()) {
          str = strTok.getToken();
          if (str == null) { throw new SimpplleError("Null Process found"); }

          processType = ProcessType.get(str.toUpperCase());
          if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

          pList.add(processType);
          Process.findInstance(processType).addValidLifeform(life);
        }
        line = fin.readLine();
      }


      makeNoFireEventSimProcessCopy();

      if (Simpplle.getCurrentZone().hasAquatics() == false) {
        return;
      }

      // *** Aquatics Section ***
      // ************************
//      aquaticProcessHt = new Hashtable();

      line = fin.readLine(); // Comment
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      int i=0;
      aquaticLegalProcesses = new ProcessType[strTok.countTokens()];
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

        processHt.put(processType,processType.makeProcess());
        aquaticLegalProcesses[i] = processType;
        i++;
      }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      i=0;
      aquaticSimulationProcesses = new ProcessType[strTok.countTokens()];
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

        aquaticSimulationProcesses[i] = processType;
        i++;
      }

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Invalid zone Definition File."); }

      strTok = new StringTokenizerPlus(line,",");
      if (strTok.countTokens() < 1) { throw new SimpplleError("No processes found."); }

      i=0;
      aquaticSummaryProcesses = new ProcessType[strTok.countTokens()];
      while (strTok.hasMoreElements()) {
        str = strTok.getToken();
        if (str == null) { throw new SimpplleError("Null Process found"); }

        processType = ProcessType.get(str.toUpperCase());
        if (processType == null) { throw new SimpplleError("Invalid Process found:" + str); }

        aquaticSummaryProcesses[i] = processType;
        i++;
      }

    }
    catch (IOException ex) {
      throw new SimpplleError("Problems reading Legal Processes from zone definition file.");
    }
    catch (ParseError ex) {
      throw new SimpplleError(ex.msg);
    }

  }

  /**
   * reads a text file that contains a process name on each line and
   * creates those processes.
   *
   * @param filename File
   * @throws SimpplleError
   */
  public static void importLegalFile(File filename) throws SimpplleError {
    StringTokenizerPlus strTok;
    String              line, processStr, spreadingStr;
    ProcessType         processType;
    boolean             spreading;
    Process             process;
    BufferedReader      fin;

    try {
      fin = new BufferedReader(new FileReader(filename));

      line = fin.readLine();
      if (line == null) { throw new SimpplleError("Nothing in file"); }

      while (line != null) {
        while (line != null && line.trim().length() == 0) {
          line = fin.readLine();
        }
        if (line == null) { continue; }

        processStr = line.trim().toUpperCase();
        processType = ProcessType.get(processStr);
        if (processType == null) {
          processType = new ProcessType(processStr,"DummyProcess",false);
        }

        if (findInstance(processType) == null) {
          process = processType.makeProcess();
          processHt.put(processType, process);
        }
        if (!legalProcesses.contains(processType)) {
          legalProcesses.add(processType);
        }
        if (!simulationProcesses.contains(processType)) {
          simulationProcesses.add(processType);
        }
        if (!summaryProcesses.contains(processType)) {
          summaryProcesses.add(processType);
        }
        if (!probLogicProcesses.contains(processType)) {
          probLogicProcesses.add(processType);
        }

        line = fin.readLine();
      }
      fin.close();
    }
    catch (IOException ex) {
      throw new SimpplleError("Problems importing Legal Processes from file.");
    }
  }

  public static Process findInstance(String processName) {
    return findInstance(ProcessType.get(processName));
  }
  public static Process findInstance(ProcessType pt) {
    if (pt == null) { return null; }
    return (Process)processHt.get(pt);
  }
/**
 * Gets all the logic rules for the process.  
 * @return all the logic rules in list
 */
  public List getLogicRules() { return rules; }
  /**
   * Used in the GUI to construct base panel for process logic rules.  
   * @return string array of probability labels.  
   */
  public String[] getProbabilityLabels() { return probLabels; }

  public void readExternalProbabilityLogic(ObjectInput in) throws IOException,ClassNotFoundException {
    int numRules = in.readInt();
    if (numRules==0) {
      rules = null;
      return;
    }
    rules = new ArrayList(numRules);
    for (int i = 0; i < numRules; i++) {
      rules.add((LogicRule)in.readObject());
    }

    probLabels = (String[])in.readObject();
  }
  public void writeExternalProbabilityLogic(ObjectOutput out) throws IOException {
    out.writeInt(rules.size());
    for (Iterator iter = rules.iterator(); iter.hasNext();) {
      LogicRule rule = (LogicRule) iter.next();
      out.writeObject(rule);
    }

    out.writeObject(probLabels);
  }

  /**
   * @deprecated
   */
  public static void readProbabilityLogic(JarInputStream stream)
  throws IOException, ClassNotFoundException, SimpplleError
  {
    ObjectInputStream in = new ObjectInputStream(stream);

    int version = in.readInt();

    ProcessType process;
    int numProcesses = in.readInt();
    for (int i = 0; i < numProcesses; i++) {
      process = ProcessType.readExternalSimple(in);
      Process processInst = Process.findInstance(process);
      if (processInst == null) {
        processInst = process.makeProcess();
      }
      processInst.readExternalProbabilityLogic(in);
    }
  }
  /**
   * @deprecated
   */
  public static void writeProbabilityLogic(JarOutputStream stream, ProcessType[] processes)
  throws IOException, SimpplleError
  {
    ObjectOutputStream out = new ObjectOutputStream(stream);

    out.writeInt(probLogicVersion);

    out.writeInt(processes.length);
    for (int i = 0; i < processes.length; i++) {
      processes[i].writeExternalSimple(out);
      Process processInst = Process.findInstance(processes[i]);
      if (processInst == null) {
        processInst = processes[i].makeProcess();
      }
      processInst.writeExternalProbabilityLogic(out);
    }
  }
/**
 * Gets an arraylist of fire spread UI Processes.  This are used in the GUI to set visible combo boxes.  The choices are SRF, MSF or LSF
 * @param includeNone true if do to include none as a process.  
 * @return arraylist with legal fire process short name (SRF, MSF, or LSF)
 */
  public static ArrayList<String> getFireSpreadUIProcesses(boolean includeNone) {
    ArrayList<String> fireProcesses = new ArrayList<String>(3);

    for (ProcessType process : legalProcesses) {
      if (process.isFireProcess()) {
        fireProcesses.add(process.getShortName());
      }
    }
    if (includeNone) {
      fireProcesses.add(ProcessType.NONE.toString());
    }
    return fireProcesses;
  }

  public static ArrayList<ProcessType> getFireProcesses(boolean includeNone) {
    ArrayList<ProcessType> values = new ArrayList<ProcessType>();
    if (includeNone) {
      values.add(ProcessType.NONE);
    }
    values.add(ProcessType.SRF);
    values.add(ProcessType.MSF);
    values.add(ProcessType.LSF);

    return values;
  }

  public static ProcessType determineUnitFireProcess(ProcessType tree,
                                                     ProcessType shrub,
                                                     ProcessType grass)
  {
    return VegUnitFireTypeLogic.getInstance().getUnitProcess(tree,shrub,grass);

//    if (tree.isFireProcess() == false &&
//             shrub == ProcessType.SRF &&
//             grass == ProcessType.SRF)
//    {
//      return ProcessType.MSF;
//    }
//    else if (tree.isFireProcess() == false &&
//             shrub == ProcessType.MSF &&
//             grass == ProcessType.SRF)
//    {
//      return ProcessType.MSF;
//    }
//    else if (tree.isFireProcess() == false &&
//             shrub == ProcessType.LSF &&
//             grass == ProcessType.SRF)
//    {
//      return ProcessType.MSF;
//    }
//    else if (tree == ProcessType.LSF &&
//             shrub == ProcessType.SRF &&
//             grass == ProcessType.SRF)
//    {
//      return ProcessType.MSF;
//    }
//    else if ((tree == ProcessType.LSF || tree.isFireProcess() == false) &&
//             shrub == ProcessType.LSF &&
//             grass == ProcessType.LSF)
//    {
//      return ProcessType.LSF;
//    }
//    else if ((tree == ProcessType.LSF  || tree == ProcessType.MSF || tree.isFireProcess() == false) &&
//             (shrub == ProcessType.MSF || shrub == ProcessType.SRF) &&
//             (grass == ProcessType.MSF || grass == ProcessType.SRF))
//    {
//      return ProcessType.MSF;
//    }
//    else if (tree == ProcessType.SRF)
//    {
//      return ProcessType.SRF;
//    }
//    else if (tree.isFireProcess()) { return tree; }
//    else if (shrub.isFireProcess()) { return shrub; }
//    else if (grass.isFireProcess()) { return grass; }
//
//    return null;
  }


}


