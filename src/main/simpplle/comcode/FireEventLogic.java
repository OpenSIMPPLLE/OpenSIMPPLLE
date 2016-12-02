/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;
import java.io.*;

/**
 * This class defines Fire Event Logic, a type of Base Logic.
 *
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class FireEventLogic extends BaseLogic {
  private static final int version = 2;

  public enum Kinds { SPREAD, TYPE, FIRE_SPOTTING };

  public static final Kinds  SPREAD     = Kinds.SPREAD;
  public static final String SPREAD_STR = SPREAD.toString();

  public static final Kinds  TYPE     = Kinds.TYPE;
  public static final String TYPE_STR = TYPE.toString();

  public static final Kinds  FIRE_SPOTTING     = Kinds.FIRE_SPOTTING;
  public static final String FIRE_SPOTTING_STR = FIRE_SPOTTING.toString();

  public static final int ORIGIN_PROCESS_COL = LAST_COL+1;
  public static final int POSITION_COL       = LAST_COL+2;
  public static final int AVERAGE_COL        = LAST_COL+3;
  public static final int EXTREME_COL        = LAST_COL+4;

  public static final int WETTER_COL = LAST_COL+1;
  public static final int NORMAL_COL = LAST_COL+2;
  public static final int DRIER_COL  = LAST_COL+3;

  public static final int CHANGE_RATE_COL            = BaseLogic.LAST_COL+1;
  public static final int STATE_CHANGE_THRESHOLD_COL = BaseLogic.LAST_COL+2;

  public static final int FIRE_PROCESS_COL  = BaseLogic.LAST_COL+1;
  public static final int SPREAD_TYPE_COL   = BaseLogic.LAST_COL+2;
  public static final int START_DIST_COL    = BaseLogic.LAST_COL+3;
  public static final int END_DIST_COL      = BaseLogic.LAST_COL+4;
  public static final int PROB_COL          = BaseLogic.LAST_COL+5;

  private static FireEventLogic instance;

  public static void initialize() {
    instance = new FireEventLogic();
  }

  public static FireEventLogic getInstance() { return instance; }

  private FireEventLogic() {

    super(new String[] { SPREAD_STR, TYPE_STR, FIRE_SPOTTING_STR });

    sysKnowKind = SystemKnowledge.Kinds.FIRE_SPREAD_LOGIC;

    addColumn(SPREAD_STR,"ORIGIN_PROCESS_COL");
    addColumn(SPREAD_STR,"POSITION_COL");
    addColumn(SPREAD_STR,"AVERAGE_COL");
    addColumn(SPREAD_STR,"EXTREME_COL");

    addColumn(TYPE_STR,"WETTER_COL");
    addColumn(TYPE_STR,"NORMAL_COL");
    addColumn(TYPE_STR,"DRIER_COL");

    addColumn(FIRE_SPOTTING_STR,"FIRE_PROCESS_COL");
    addColumn(FIRE_SPOTTING_STR,"SPREAD_TYPE_COL");
    addColumn(FIRE_SPOTTING_STR,"MAX_SPOT_DIST_COL");
    addColumn(FIRE_SPOTTING_STR,"START_DIST_COL");
    addColumn(FIRE_SPOTTING_STR,"END_DIST_COL");
    addColumn(FIRE_SPOTTING_STR,"PROB_COL");

    addVisibleColumn(FIRE_SPOTTING.toString(),ROW_COL);
    addVisibleColumn(FIRE_SPOTTING.toString(),FIRE_PROCESS_COL);
    addVisibleColumn(FIRE_SPOTTING.toString(),SPREAD_TYPE_COL);
    addVisibleColumn(FIRE_SPOTTING.toString(),START_DIST_COL);
    addVisibleColumn(FIRE_SPOTTING.toString(),END_DIST_COL);
    addVisibleColumn(FIRE_SPOTTING.toString(),PROB_COL);

    addVisibleColumnAll(SPREAD_STR);
    addVisibleColumnAll(TYPE_STR);

  }

  public void addRow(int insertPos, String kind) {

    AbstractLogicData logicData;

    if (kind.equals(Kinds.SPREAD.toString())) {
      logicData = new FireSpreadLogicData();
    } else if (kind.equals(Kinds.TYPE.toString())) {
      logicData = new FireTypeLogicData();
    } else if (kind.equals(FIRE_SPOTTING_STR)) {
      logicData = new FireSpottingLogicData();
    } else {
      return;
    }
    super.addRow(insertPos,kind,logicData);
  }

  public void duplicateRow(int row,int insertPos, String kind) {

    AbstractLogicData logicData = getData(kind).get(row);

    if (logicData == null) {
      return;
    }
    AbstractLogicData newLogicData = logicData.duplicate();
    super.addRow(insertPos,kind,newLogicData);
  }

  public void addLegacyData(FireResistance fireResist,
                            SizeClass[] sizeClasses,
                            ArrayList<SimpplleType> densities,
                            ProcessType[] processes,
                            Climate.Season season,
                            ArrayList<ProcessType> originProcesses,
                            ArrayList<FireEvent.Position> positions,
                            ProcessType average,
                            ProcessType extreme) {

    FireSpreadLogicData fireData = new FireSpreadLogicData();

    fireData.addFireResistance(fireResist);

    if (sizeClasses != null) {
      fireData.sizeClassList   = new ArrayList<SimpplleType>(Arrays.asList(sizeClasses));
    }

    if (densities != null) {
      fireData.densityList.addAll(densities);
    }

    if (processes != null) {
      fireData.processList = new ArrayList<SimpplleType>(Arrays.asList(processes));
    }

    fireData.season         = season;
    fireData.originProcessList.addAll(originProcesses);
    fireData.positions.addAll(positions);
    fireData.average        = average;
    fireData.extreme        = extreme;

    fireData.sortLists();
    addRow(SPREAD_STR,fireData);
  }

  public Kinds getKindValue(String kindStr) {
    return (Kinds.valueOf(kindStr));
  }

  public void combineCompatibleRules(String kind) {
    switch (getKindValue(kind)) {
      case SPREAD: combineCompatibleRulesSpread(); break;
      case TYPE:   combineCompatibleRulesTypeOfFire(); break;
      default: return;
    }

  }

  private void combineCompatibleRulesSpread() {
    FireSpreadLogicData rule1, rule2;
    ArrayList<Integer> deleted = new ArrayList<Integer>();

    for (int j=0; j<getData(SPREAD_STR).size()-1; j++) {
      if (deleted.contains(j)) { continue; }
      rule1 = (FireSpreadLogicData)getData(SPREAD_STR).get(j);
      for (int i = j+1; i < data.size(); i++) {
        if (deleted.contains(i)) { continue; }

        rule2 = (FireSpreadLogicData)getData(SPREAD_STR).get(i);
        if (rule1.rulesCompatible(rule2)) {
          rule1.AddRuleData(rule2);
          deleted.add(i);
        }
      }
    }

    ArrayList<AbstractLogicData> oldData = getData(SPREAD_STR);
    clearData(SPREAD_STR);
    for (int i=0; i<oldData.size(); i++) {
      if (deleted.contains(i)) { continue; }
      addRow(SPREAD_STR,oldData.get(i));
    }
  }

  // ********************************
  // *** Type of Fire legacy Code ***
  // ********************************

  public void addLegacyDataTypeOfFire(FireResistance  fireResist,
                                      ArrayList<SimpplleType> sizeClasses,
                                      Density[]       densities,
                                      ProcessType[]   processes,
                                      boolean         notTreatment,
                                      TreatmentType[] treatments,
                                      Climate.Season  season,
                                      ProcessType     wetter,
                                      ProcessType     normal,
                                      ProcessType     drier) {

    FireTypeLogicData fireData = new FireTypeLogicData();

    fireData.addFireResistance(fireResist);

//    fireData.addStructure(structure);
    if (sizeClasses != null) {
      fireData.sizeClassList.addAll(sizeClasses);
    }

    if (densities != null) {
      fireData.densityList   = new ArrayList<SimpplleType>(Arrays.asList(densities));
    }

    if (processes != null) {
      fireData.processList = new ArrayList<SimpplleType>(Arrays.asList(processes));
    }

    if (treatments != null) {
      ArrayList<SimpplleType> tmpList =
          new ArrayList<SimpplleType>(Arrays.asList(treatments));
      if (notTreatment) {
        fireData.treatmentList = new ArrayList<SimpplleType>();
        for (SimpplleType treat : SimpplleType.getList(SimpplleType.TREATMENT)) {
          if (tmpList.contains(treat) == false) {
            fireData.treatmentList.add(treat);
          }
        }
        fireData.treatmentList.add(TreatmentType.NONE);
      } else {
        fireData.treatmentList = tmpList;
      }
    }

    fireData.season = season;
    fireData.wetter = wetter;
    fireData.normal = normal;
    fireData.drier  = drier;

    fireData.sortLists();

    addRow(TYPE_STR,fireData);

  }

  public void combineCompatibleRulesTypeOfFire() {
    FireSpreadLogicData rule1, rule2;
    ArrayList<Integer> deleted = new ArrayList<Integer>();

    for (int j=0; j<getData(TYPE_STR).size()-1; j++) {
      if (deleted.contains(j)) { continue; }
      rule1 = (FireSpreadLogicData)getData(TYPE_STR).get(j);
      for (int i = j+1; i < data.size(); i++) {
        if (deleted.contains(i)) { continue; }

        rule2 = (FireSpreadLogicData)getData(TYPE_STR).get(i);
        if (rule1.rulesCompatible(rule2)) {
          rule1.AddRuleData(rule2);
          deleted.add(i);
        }
      }
    }

    ArrayList<AbstractLogicData> oldData = getData(TYPE_STR);
    clearData(TYPE_STR);
    for (int i=0; i<oldData.size(); i++) {
      if (deleted.contains(i)) { continue; }
      addRow(TYPE_STR,oldData.get(i));
    }
  }

  // ************************************
  // *** End Type of Fire Legacy Code ***
  // ************************************


  public static void save(String kind, ObjectOutputStream os) throws IOException {

    boolean includeVisibleCol = FIRE_SPOTTING_STR.equals(kind);
    
    os.writeInt(version);

    getInstance().saveData(kind,os,includeVisibleCol);

  }

  public static void read(String kind, ObjectInputStream in) throws IOException, ClassNotFoundException {

    try {
      boolean includeVisibleCol = FIRE_SPOTTING_STR.equals(kind);
      int version = in.readInt();
      getInstance().readData(kind,in,version,includeVisibleCol);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public int getColumnNumFromName(String name) {
    if (name.equalsIgnoreCase("Origin Processes")) {
      return ORIGIN_PROCESS_COL;
    }
    else if (name.equalsIgnoreCase("Position")) {
      return POSITION_COL;
    }
    else if (name.equalsIgnoreCase("Average")) {
      return AVERAGE_COL;
    }
    else if (name.equalsIgnoreCase("Extreme")) {
      return EXTREME_COL;
    }
    else if (name.equalsIgnoreCase("Wetter")) {
      return WETTER_COL;
    }
    else if (name.equalsIgnoreCase("Normal")) {
      return NORMAL_COL;
    }
    else if (name.equalsIgnoreCase("Drier")) {
      return DRIER_COL;
    }

    else if (name.equalsIgnoreCase("From Process")) {
      return FIRE_PROCESS_COL;
    }
    else if (name.equalsIgnoreCase("Spread Type")) {
      return SPREAD_TYPE_COL;
    }
    else if (name.equalsIgnoreCase("Start Dist (Miles)")) {
      return START_DIST_COL;
    }
    else if (name.equalsIgnoreCase("End Dist (Miles)")) {
      return END_DIST_COL;
    }
    else if (name.equalsIgnoreCase("Prob")) {
      return PROB_COL;
    }
    else {
      return super.getColumnNumFromName(name);
    }
  }

  public String getColumnName(String kindStr, int visibleCol) {
    String colName = visibleColumnsHm.get(kindStr).get(visibleCol);
    int col = getColumnPosition(kindStr,colName);

    Kinds kind = Kinds.valueOf(kindStr);
    switch (kind) {
      case SPREAD:        return getColumnNameSpread(col);
      case TYPE:          return getColumnNameType(col);
      case FIRE_SPOTTING: return getColumnNameFireSpotting(col);
      default:            return "";
    }
  }

  private String getColumnNameSpread(int col) {
    switch (col) {
      case ORIGIN_PROCESS_COL: return "Origin Processes";
      case POSITION_COL:       return "Position";
      case AVERAGE_COL:        return "Average";
      case EXTREME_COL:        return "Extreme";
      default: return super.getColumnName(col);
    }
  }

  private String getColumnNameType(int col) {
    switch (col) {
      case WETTER_COL: return "Wetter";
      case NORMAL_COL: return "Normal";
      case DRIER_COL:  return "Drier";
      default: return super.getColumnName(col);
    }
  }

  private String getColumnNameFireSpotting(int col) {
    switch (col) {
      case FIRE_PROCESS_COL: return "From Process";
      case SPREAD_TYPE_COL:  return "Spread Type";
      case START_DIST_COL:   return "Start Dist (Miles)";
      case END_DIST_COL:     return "End Dist (Miles)";
      case PROB_COL:         return "Prob";
      default: return super.getColumnName(col);
    }
  }

  // ***********************
  // *** Simulation Code ***
  // ***********************

  /**
   * Returns the type of fire for fire spread and records the applied rule index in the EVU.
   *
   * @param processType ProcessType
   * @param resistance FireResistance
   * @param fromEvu Evu
   * @param evu Evu
   * @return ProcessType
   */
  public ProcessType getSpreadingTypeOfFire(ProcessType processType, FireResistance resistance, Evu fromEvu, Evu evu, Lifeform toLifeform) {

    List<AbstractLogicData> logicDataArray = getData(SPREAD_STR);

    for (int i = 0; i < getData(SPREAD_STR).size(); i++) {

      FireSpreadLogicData logicData = (FireSpreadLogicData)logicDataArray.get(i);

      //Get the process currently associated with the cell
      VegSimStateData state2 = evu.getState(toLifeform);
      ProcessType currentProcess = state2.getProcess();

       //Get the fire process of a matching rule if you can find a matching rule
       ProcessType fireType = logicData.getFireTypeIfMatch(processType, resistance, fromEvu, evu, toLifeform);

      if (fireType != null) {

        //check to see if this is the same process that has been identified before
        if (fireType == currentProcess) {
          if (!fireType.isFireProcess()) { // fireProcess is ProcessType.NONE
            fireType = null;
          }
          return fireType;
        }

        if (Simulation.getInstance().isDoSimLoggingFile()) {
          logSpread(evu, fromEvu, toLifeform.toString(), fireType.toString(), i);
        }

        if (!fireType.isFireProcess()) { // fireProcess is ProcessType.NONE
          fireType = null;
          return fireType; //rule is not used to set fire type
        }

        VegSimStateData state = evu.getState(toLifeform);
        state.setFireSpreadRuleIndex(i);
        return fireType;
      }
    }
    if (Simulation.getInstance().isDoSimLoggingFile()) {
      logSpread(evu, fromEvu, toLifeform.toString(), "No Matching Rules for Spread", -1);
    }
    return null;
  }

  /**
   * Helper method, log fire type data to simulation log
   * @param evu Evu
   * @param fromEvu Evu that fire is spreading from
   * @param toLifeformStr LifeForm in String form
   * @param fireTypeStr Process Type in string form, will be a warming message if no rule is found
   * @param index
   */
  private void logSpread(Evu evu, Evu fromEvu, String toLifeformStr, String fireTypeStr, int index){
    PrintWriter logOut = Simulation.getInstance().getSimLoggingWriter();
    int ts = Simulation.getCurrentTimeStep();
    logOut.println(ts + "," + fromEvu.getId() + "," + evu.getId() + "," + toLifeformStr + "," + fireTypeStr
        + "," + index);
  }

  /**
   * Returns a fire type from the first fire type rule that applies to the arguments.
   *
   * @param resistance A fire resistance level
   * @param evu A vegetation unit
   * @return A process type if there is a matching rule, otherwise null
   */
  public ProcessType getTypeOfFire(FireResistance resistance, Evu evu, Lifeform lifeform) {

    for (int i = 0; i < getData(TYPE_STR).size(); i++) {

      FireTypeLogicData logicData = (FireTypeLogicData)getData(TYPE_STR).get(i);
      ProcessType processType = logicData.getFireTypeIfMatch(resistance, evu,lifeform);

      if (processType != null) {
        return processType;
      }
    }
    return null;
  }

  public boolean isWithinMaxFireSpottingDistance(Evu fromEvu, Evu toEvu) {
    return FireSpottingLogicData.isWithinMaxDistance(fromEvu, toEvu);
  }
  
  /**
   * @param fromEvu  Evu Fire is Spotting from
   * @param toEvu    Evu that Fire Spotting may occur
   * @param simFireProcess Process of from Evu
   * @param isExtremeSpread is the Fire Extreme spread
   * @return The probability of Spotting, -1 if no matching rule.
   */
  public int getFireSpottingProbability(Evu fromEvu, Evu toEvu, ProcessType simFireProcess, boolean isExtremeSpread) {
    FireSpottingLogicData logicData;
    
    for (int i=0; i<getData(FIRE_SPOTTING_STR).size(); i++) {
      logicData = (FireSpottingLogicData)getData(FIRE_SPOTTING_STR).get(i);
      if (logicData.isMatch(fromEvu, toEvu, simFireProcess, isExtremeSpread)) {
        return logicData.getProbability();
      }
    }
    return -1;
  }
}
