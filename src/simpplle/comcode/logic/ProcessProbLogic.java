package simpplle.comcode.logic;

import simpplle.comcode.*;
import simpplle.comcode.Process;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Process Probability Logic, a type of Base Logic.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 *
 */
public class ProcessProbLogic extends BaseLogic {
  private static final int version = 2;

  public enum Columns {
    ADJ_PROCESS_COL,MPB_HAZARD_COL,ADJ_MOD_HAZARD_COL,ADJ_HIGH_HAZARD_COL,PROB_COL
  }

  public static final int ADJ_PROCESS_COL     = BaseLogic.LAST_COL+1;
  public static final int MPB_HAZARD_COL      = BaseLogic.LAST_COL+2;
  public static final int ADJ_MOD_HAZARD_COL  = BaseLogic.LAST_COL+3;
  public static final int ADJ_HIGH_HAZARD_COL = BaseLogic.LAST_COL+4;
  public static final int PROB_COL            = BaseLogic.LAST_COL+5;

  private static ProcessProbLogic instance;
/**
 * Initializes the Process Probability logic for the current zone and transfering into 
 */
  public static void initialize() {
    RegionalZone zone = Simpplle.getCurrentZone();
    ArrayList<ProcessType> processes = simpplle.comcode.Process.getProbLogicProcesses();
    String[]      processNames = new String[processes.size()];

    for (int i=0; i<processes.size(); i++) {
      processNames[i] = processes.get(i).toString();
    }
    instance = new ProcessProbLogic(processes,processNames);
  }
  /**
   * Constructor for Process Probability Logic.  Takes in the arraylist of processes, and string array of processnames.  
   * The string array is sent to the BaseLogic superclass, where columns are added.  Then loops through the process arraylist and adds the default 
   * visible columns for each process to the visible columns for this ProcessProbability Logic object.  
   * @param processes the arraylist of porcess objects.  Process objects contain a process name, classname, spreading boolean 
   * @param processNames the names of the proceses which are in the process arraylist also passed in parameter
   */
  private ProcessProbLogic(ArrayList<ProcessType> processes, String[] processNames) {
    super(processNames);

    sysKnowKind = SystemKnowledge.Kinds.PROCESS_PROB_LOGIC;


    for (int i=0; i<processes.size(); i++) {
      Process process = Process.findInstance(processes.get(i));
      if (process.isUniqueUI()) { continue; }

      addColumns(processes.get(i).toString());
      ArrayList<String> columns = process.getDefaultVisibleColumns();
      for (String column : columns) {
        addVisibleColumn(processes.get(i).toString(),column);
      }
    }
  }
/**
 * Private method to add adjacent process, mountain pine beetle hazard, adjacent moderate hazard, adjacent high hazard, and probability columns 
 * @param processName
 */
  private void addColumns(String processName) {
    addColumn(processName,"ADJ_PROCESS_COL");
    addColumn(processName,"MPB_HAZARD_COL");
    addColumn(processName,"ADJ_MOD_HAZARD_COL");
    addColumn(processName, "ADJ_HIGH_HAZARD_COL");
    addColumn(processName,"PROB_COL");
  }
/**
 * Gets this instance of ProcessProbLogic
 * @return
 */
  public static ProcessProbLogic getInstance() { return instance; }

/**
 * Adds a process to the table model, by adding a row.  
 */
  public void addRow(int insertPos, String processName) {
    super.addRow(insertPos,processName,new ProcessProbLogicData(processName));
  }
/**
 * Duplicates a row, by creating a new instance of Process probability logic data and getting the value at a row and process name.  
 */
  public void duplicateRow(int row,int insertPos, String processName) {
    ProcessProbLogicData logicData = (ProcessProbLogicData)getValueAt(row,processName);
    AbstractLogicData newLogicData = logicData.duplicate();
    super.addRow(insertPos,processName,newLogicData);
  }
/**
 * This was added to enable easily copying existing data from the process
 * Root Disease to the three new Root Disease process (Low, Moderate, and High Severity).
 * Added only for the developers use, later disabled.  Left here for possible future use.
 */
//  public static void fillNewRoots() {
//    ProcessProbLogic logicInst = ProcessProbLogic.getInstance();
//
//    ProcessType process = ProcessType.ROOT_DISEASE;
//
//    ProcessProbLogicData logicData, newLogicData;
//    Integer prob=null;
//    for (int i=0; i<logicInst.getData(process.toString()).size(); i++) {
//      logicData = (ProcessProbLogicData)logicInst.getData(process.toString()).get(i);
//
//      // Light Severity Root Disease
//      newLogicData = (ProcessProbLogicData) logicData.duplicate();
//      newLogicData.process = ProcessType.LS_ROOT_DISEASE;
//
//      logicInst.addRow(ProcessType.LS_ROOT_DISEASE.toString(), newLogicData);
//
//      // Moderate Severity Root Disease
//      newLogicData = (ProcessProbLogicData) logicData.duplicate();
//      newLogicData.process = ProcessType.MS_ROOT_DISEASE;
//
//      logicInst.addRow(ProcessType.MS_ROOT_DISEASE.toString(), newLogicData);
//
//      // High Severity Root Disease
//      newLogicData = (ProcessProbLogicData) logicData.duplicate();
//      newLogicData.process = ProcessType.HS_ROOT_DISEASE;
//
//      logicInst.addRow(ProcessType.HS_ROOT_DISEASE.toString(), newLogicData);
//
//    }
//  }
/**
 * takes in a column name and returns the column Id.  These column Id's are taken by adding a constant to the BaseLogic columns (which total 15).  The resulting 
 * column Id's are ADJ_PROCESS_COL = 16, MPB_HAZARD_COL =17, ADJ_MOD_HAZARD_COL = 18, ADJ_HIGH_HAZARD_COL = 19, PROB_COL = 20
 */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Adjacent Process")) {
      return ADJ_PROCESS_COL;
    }
    else if (name.equalsIgnoreCase("MPB Hazard")) {
      return MPB_HAZARD_COL;
    }
    else if (name.equalsIgnoreCase("Adj Moderate Hazard")) {
      return ADJ_MOD_HAZARD_COL;
    }
    else if (name.equalsIgnoreCase("Adj High Hazard")) {
      return ADJ_HIGH_HAZARD_COL;
    }
    else if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }
/**
 * returns a string literal of the columns in process probability.  
 */
  public String getColumnName(String processName,int visibleCol) {
    String colName = visibleColumnsHm.get(processName).get(visibleCol);
    int col = getColumnPosition(processName,colName);
    switch (col) {
      case ADJ_PROCESS_COL:     return "Adjacent Process";
      case MPB_HAZARD_COL:    return "MPB Hazard";
      case ADJ_MOD_HAZARD_COL:  return "Adj Moderate Hazard";
      case ADJ_HIGH_HAZARD_COL: return "Adj High Hazard";
      case PROB_COL:            return "Probability";
      default:
        return super.getColumnName(col);
    }
  }
/**
 * 
 * @param process process type being evaluated
 * @return true if the instance has probability logic data
 */
  public static boolean hasLogic(ProcessType process) {
    return getInstance().getData(process.toString(),false) != null;
  }
/**
 * get the probability for a specified process at a designated ev unit
 * @param process
 * @param evu
 * @return 
 */
  public int getProbability(ProcessType process, simpplle.comcode.element.Evu evu) {
    ProcessProbLogicData logicData;
    Integer prob=null;
    for (int i=0; i<getData(process.toString()).size(); i++) {
      logicData = (ProcessProbLogicData)getData(process.toString()).get(i);
      prob = logicData.getProbability(evu);
      if (prob != null) { return prob; }
    }

    return 0;
  }
/**
 * method to save process probability logic.  This will save the version, all the current process probability logic
 * instances, a count of processes with unique UI's, the processes with unique UI
 */
  public void save(ObjectOutputStream os) throws IOException {
    os.writeInt(version);
    super.save(os);

    ArrayList<ProcessType> processes = Process.getProbLogicProcesses();
    int count = 0;
    for (int i=0; i<processes.size(); i++) {
      Process process = Process.findInstance(processes.get(i));
      if (process.isUniqueUI()) {
        count++;
      }
    }

    os.writeInt(count);
    for (int i=0; i<processes.size(); i++) {
      Process process = Process.findInstance(processes.get(i));
      if (process.isUniqueUI()) {
        processes.get(i).writeExternalSimple(os);
        process.writeExternalProbabilityLogic(os);
      }
    }


  }
/**
 * reads in process probability logic from a source.  Reads, in order: version, species count, process type, probabilty logic 
 */
  public void read(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readInt();
    int subVersion = 1;
    if (version >= 2) {
      subVersion = in.readInt();
    }
    super.read(in,version);
    if (version == 1) { return; }

    int speciesCount = in.readInt();

    for (int i=0; i<speciesCount; i++) {
      ProcessType processType = (ProcessType)ProcessType.readExternalSimple(in);
      Process process = Process.findInstance(processType);

      process.readExternalProbabilityLogic(in);
    }
  }
/**
 * method to get process probability logic from legacy systems
 * @param process
 * @param speciesList
 * @param sizeClassList
 * @param densityList
 * @param processList
 * @param adjProcessList
 * @param tempList
 * @param moistureList
 */
  public static void addFromLegacy(ProcessType process,
                                   ArrayList<SimpplleType> speciesList,
                                   ArrayList<SimpplleType> sizeClassList,
                                   ArrayList<SimpplleType> densityList,
                                   ArrayList<SimpplleType> processList,
                                   ArrayList<ProcessType> adjProcessList,
                                   ArrayList<Climate.Temperature> tempList,
                                   ArrayList<Climate.Moisture> moistureList)
  {

//    ArrayList<ProcessProbLogicData> dataList = getData(process,true);
//
//    ProcessProbLogicData logicData = new ProcessProbLogicData();
//
//    for (SimpplleType item : speciesList) {
//      logicData.addSimpplleType(item,SimpplleType.SPECIES);
//    }
//    for (SimpplleType item : sizeClassList) {
//      logicData.addSimpplleType(item,SimpplleType.SIZE_CLASS);
//    }
//    for (SimpplleType item : densityList) {
//      logicData.addSimpplleType(item,SimpplleType.DENSITY);
//    }
//    for (SimpplleType item : processList) {
//      logicData.addSimpplleType(item,SimpplleType.PROCESS);
//    }
//    for (ProcessType proc : adjProcessList) {
//      logicData.addAdjProcess(proc);
//    }
//    for (Climate.Temperature temp : tempList) {
//      logicData.addTemperature(temp);
//    }
//    for (Climate.Moisture moisture : moistureList) {
//      logicData.addMoisture(moisture);
//    }
//    dataList.add(logicData);
  }



//  public static void save(ObjectOutputStream os) {
//    try {
//      os.writeInt(version);
//      int size = (data != null) ? data.size() : 0;
//      os.writeInt(size);
//      for (ProcessType process : data.keySet()) {
//        process.writeExternalSimple(os);
//        os.writeObject(getData(process));
//      }
//      os.writeObject(data);
//      os.flush();
//    }
//    catch (IOException ex) {
//    }
//  }
//
//  public static void read(ObjectInputStream in) {
//    try {
//      int version = in.readInt();
//
//      int size = in.readInt();
//      data.clear();
//      for (int i=0; i<size; i++) {
//        ProcessType process = (ProcessType)SimpplleType.readExternalSimple(in,SimpplleType.PROCESS);
//        data.put(process,(ArrayList<ProcessProbLogicData>)in.readObject());
//      }
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }
}
