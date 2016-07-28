/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * ProcessProbLogic determines the probability that a process occurs in a vegetated unit. This
 * probability is based on the default logic columns in addition to mountain pine beetle hazards
 * and adjacent processes. Each instance of ProcessProbLogic contains a set of rules for each
 * process type passed into the constructor.
 */

public class ProcessProbLogic extends BaseLogic {

  private static final int version = 2;

  public enum Columns {

    ADJ_PROCESS_COL,
    MPB_HAZARD_COL,
    ADJ_MOD_HAZARD_COL,
    ADJ_HIGH_HAZARD_COL,
    PROB_COL

  }

  public static final int ADJ_PROCESS_COL     = BaseLogic.LAST_COL + 1;
  public static final int MPB_HAZARD_COL      = BaseLogic.LAST_COL + 2;
  public static final int ADJ_MOD_HAZARD_COL  = BaseLogic.LAST_COL + 3;
  public static final int ADJ_HIGH_HAZARD_COL = BaseLogic.LAST_COL + 4;
  public static final int PROB_COL            = BaseLogic.LAST_COL + 5;

  private static ProcessProbLogic instance;

  /**
   * Creates a set of probability rules for each process type.
   *
   * @param processTypes A list of process types
   * @param processNames An array of process names, which must align with the process types
   */
  private ProcessProbLogic(List<ProcessType> processTypes, String[] processNames) {

    super(processNames);

    sysKnowKind = SystemKnowledge.Kinds.PROCESS_PROB_LOGIC;

    for (ProcessType processType : processTypes) {

      Process process = Process.findInstance(processType);
      if (process.isUniqueUI()) continue;

      String processName = processType.toString();

      addColumn(processName,"ADJ_PROCESS_COL");
      addColumn(processName,"MPB_HAZARD_COL");
      addColumn(processName,"ADJ_MOD_HAZARD_COL");
      addColumn(processName,"ADJ_HIGH_HAZARD_COL");
      addColumn(processName,"PROB_COL");

      ArrayList<String> columnNames = process.getDefaultVisibleColumns();
      for (String columnName : columnNames) {
        addVisibleColumn(processName,columnName);
      }
    }
  }

  /**
   * Creates an instance of ProcessProbLogic. The processes are loaded from a 'legal file', which
   * contains a list of processes represented by the current regional zone.
   */
  public static void initialize() {

    List<ProcessType> processTypes = Process.getProbLogicProcesses();
    String[] processNames = new String[processTypes.size()];

    for (int i = 0; i < processTypes.size(); i++) {
      processNames[i] = processTypes.get(i).toString();
    }

    instance = new ProcessProbLogic(processTypes,processNames);

  }

  /**
   * Returns an instance of ProcessProbLogic. Call initialize once before calling this method.
   */
  public static ProcessProbLogic getInstance() {
    return instance;
  }

  /**
   * Returns an index for the column matching the provided name.
   *
   * @param name The name to search for
   * @return A column index
   */
  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Adjacent Process")) {
      return ADJ_PROCESS_COL;
    } else if (name.equalsIgnoreCase("MPB Hazard")) {
      return MPB_HAZARD_COL;
    } else if (name.equalsIgnoreCase("Adj Moderate Hazard")) {
      return ADJ_MOD_HAZARD_COL;
    } else if (name.equalsIgnoreCase("Adj High Hazard")) {
      return ADJ_HIGH_HAZARD_COL;
    } else if (name.equalsIgnoreCase("Probability")) {
      return PROB_COL;
    } else {
      return super.getColumnNumFromName(name);
    }
  }

  /**
   * Returns the name of the column at columnIndex. This only searches visible columns.
   *
   * @param columnIndex A column index
   * @return The column name, or an empty string otherwise
   */
  public String getColumnName(String processName, int columnIndex) {
    String colName = visibleColumnsHm.get(processName).get(columnIndex);
    int col = getColumnPosition(processName,colName);
    switch (col) {
      case ADJ_PROCESS_COL:
        return "Adjacent Process";
      case MPB_HAZARD_COL:
        return "MPB Hazard";
      case ADJ_MOD_HAZARD_COL:
        return "Adj Moderate Hazard";
      case ADJ_HIGH_HAZARD_COL:
        return "Adj High Hazard";
      case PROB_COL:
        return "Probability";
      default:
        return super.getColumnName(col);
    }
  }

  /**
   * Creates a row of logic at insertPos.
   *
   * @param insertPos The index of the new row
   * @param processName The process that the rule applies to
   */
  public void addRow(int insertPos, String processName) {
    super.addRow(insertPos,processName,new ProcessProbLogicData(processName));
  }

  /**
   * Duplicates a row of logic.
   *
   * @param row The index of the row to duplicate
   * @param insertPos The index of the new row
   * @param processName The process containing the row
   */
  public void duplicateRow(int row,int insertPos, String processName) {
    ProcessProbLogicData logicData = (ProcessProbLogicData)getValueAt(row,processName);
    AbstractLogicData newLogicData = logicData.duplicate();
    super.addRow(insertPos,processName,newLogicData);
  }

  /**
   * Returns true if this contains logic for the process type.
   *
   * @param process A process type
   * @return True if this contains logic for the process type
   */
  public static boolean hasLogic(ProcessType process) {
    return getInstance().getData(process.toString(),false) != null;
  }

  /**
   * Returns the probability that a process occurs in a vegetated unit.
   *
   * @param process A process type
   * @param evu A vegetated unit
   * @return The probability that the process type occurs in the unit
   */
  public int getProbability(ProcessType process, Evu evu) {

    for (int i = 0; i < getData(process.toString()).size(); i++) {
      ProcessProbLogicData logicData = (ProcessProbLogicData)getData(process.toString()).get(i);
      Integer prob = logicData.getProbability(evu);
      if (prob != null) return prob;
    }

    return 0;

  }

  /**
   * Saves this process probability logic to an object output stream. The version, visible column
   * names, column data, process names, and logic rules are written to the stream.
   */
  public void save(ObjectOutputStream os) throws IOException {

    os.writeInt(version);

    super.save(os);

    List<ProcessType> processes = Process.getProbLogicProcesses();
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
   * Reads process probability logic into this instance. All values written by the save method
   * are read.
   */
  public void read(ObjectInputStream in) throws IOException, ClassNotFoundException {
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

//  /**
//   * This was added to copy existing data from Root Disease to the three new processes; low,
//   * moderate, and high severity. This was added only for developer's use and later disabled.
//   * This has been left here for possible future use.
//   */
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

//  /**
//   * Adds process probability logic from legacy systems.
//   *
//   * @param process
//   * @param speciesList
//   * @param sizeClassList
//   * @param densityList
//   * @param processList
//   * @param adjProcessList
//   * @param tempList
//   * @param moistureList
//   */
//  public static void addFromLegacy(ProcessType process,
//                                   ArrayList<SimpplleType> speciesList,
//                                   ArrayList<SimpplleType> sizeClassList,
//                                   ArrayList<SimpplleType> densityList,
//                                   ArrayList<SimpplleType> processList,
//                                   ArrayList<ProcessType> adjProcessList,
//                                   ArrayList<Climate.Temperature> tempList,
//                                   ArrayList<Climate.Moisture> moistureList) {
//
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
//  }

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
