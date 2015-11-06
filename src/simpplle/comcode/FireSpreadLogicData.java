package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;

import simpplle.comcode.process.FireEvent.Position;
import java.util.Collections;
/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Spread Logic Data, a type of Logic Data
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *  
 */
public class FireSpreadLogicData extends LogicData implements Externalizable {
  static final long serialVersionUID = -5941778815538800658L;
  static final int  version          = 1;


  public static transient final Position ABOVE   = Position.ABOVE;
  public static transient final Position BELOW   = Position.BELOW;
  public static transient final Position NEXT_TO = Position.NEXT_TO;

  public transient ArrayList<ProcessType> originProcessList;
  public transient ArrayList<Position> positions;
  public transient ProcessType average;
  public transient ProcessType extreme;
/**
 * Constructor for Fire Spread Logic Data class.  Inherits from LogicData superclass and initializes position, average, extreme, an array list for processes, 
 * and the system knowledge to fire spread logic
 */
  public FireSpreadLogicData() {
    super();
    originProcessList = new ArrayList<ProcessType>(3);
    positions = new ArrayList<Position>(3);
    average = ProcessType.LSF;
    extreme = ProcessType.LSF;
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SPREAD_LOGIC;
  }
/**
 * Instantiates a new FireSpreadLogicData object and duplicates all Fire Spread Logic Data.
 */
  public AbstractLogicData duplicate() {
    FireSpreadLogicData logicData = new FireSpreadLogicData();
    super.duplicate(logicData);

    logicData.originProcessList = new ArrayList<ProcessType>(originProcessList);
    logicData.positions         = new ArrayList<Position>(positions);
    logicData.average           = average;
    logicData.extreme           = extreme;

    return logicData;
  }
  /**
   * Gets the origin process, position, average, or extreme info from specified column.
   */
  
  public Object getValueAt(int col) {
    switch (col) {
      case FireEventLogic.ORIGIN_PROCESS_COL:   return getOriginProcessListDesc();
      case FireEventLogic.POSITION_COL: return positions;
      case FireEventLogic.AVERAGE_COL:  return average.getShortName();
      case FireEventLogic.EXTREME_COL:  return extreme.getShortName();
      default: return super.getValueAt(col);
    }
  }
  /**
   * Sets the designated object value at a specified column.  Those specified in this class are position, average, or extreme columns.
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireEventLogic.POSITION_COL: break;
      case FireEventLogic.AVERAGE_COL:  average  = ProcessType.get((String)value); break;
      case FireEventLogic.EXTREME_COL:  extreme  = ProcessType.get((String)value); break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * Adds the position if it is not already in the position arraylist
 * @param position the to be added position.  
 */
  public void addPosition(Position position) {
    if (positions.contains(position) == false) {
      positions.add(position);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }
  /**
   * Remove the designated position and set the system fire spread logic knowledge changed.
   * @param position
   */
  public void removePosition(Position position) {
    if (positions.contains(position)) {
      positions.remove(position);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }
  /**
   * Checks if the positions arraylist contains the paramenter position.  
   * @param pos the position to be evaluated
   * @return true if positions arraylist contains the position.
   */
  public boolean hasPosition(Position pos) {
    return positions.contains(pos);
  }
/**
 * Adds an origin process if it does not already exist, then sets the stystem knowledge to fire spread logic.
 * @param process the process type to be evaluated
 */
  public void addOriginProcess(ProcessType process) {
    if (originProcessList.contains(process) == false) {
      originProcessList.add(process);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }
  /**
   * Removes an origin process if it exist in process type list and sets the system knowledge to changed fire spread logic.
   * sets the system knowledge for fire spread logic changed
   * @param process the fire type process
   */
  public void removeOriginProcess(ProcessType process) {
    if (originProcessList.contains(process)) {
      originProcessList.remove(process);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }
  /**
   * Checks if Fire Spread Logic has an origin process.  From the GUI this will be called with SRF, MSF, or LSF as process.  In other words if this has a type of fire
   * as an origin, will return true.   
   * @param process fire process
   * @return true if the passed fire process is in origin process arraylist
   */
  public boolean hasOriginProcess(ProcessType process) {
    return originProcessList.contains(process);
  }
/**
 * Sets the current fire process type to extreme, if it is not already extreme.  
 * Then sets system knowledge to changed fire spread logic.
 * @param extreme represents extreme fire process
 */
  public void setExtreme(ProcessType extreme) {
    if (this.extreme != extreme) {
      this.extreme = extreme;
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Sets the current fire process type to average if it is not already average.    
   * Then sets system knowledge to changed fire spread logic.
   * @param average
   */
  public void setAverage(ProcessType average) {
    if (this.average != average) {
      this.average = average;
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }
  /**
   * Using a loop through the origin process arraylist, this makes a string of the origin processes.  it is comma separated
   * @return a string representing the origin fire processes.
   */
  private String getOriginProcessListDesc() {
    StringBuffer strBuf = new StringBuffer("[");
    for (int i=0; i<originProcessList.size(); i++) {
      if (i != 0) { strBuf.append(","); }
      strBuf.append(originProcessList.get(i).getShortName());
    }
    strBuf.append("]");
    return strBuf.toString();
  }
/**
 * Uses the default sorting algorithm in java Collections.  Arranges in ascending order.  
 */
  public void sortLists() {
    super.sortLists();
    Collections.sort(originProcessList);
    Collections.sort(positions);
  }
  /**
   * Reads in from external source fire spread logic data in following order: origin process list, positions list, average, and extreme process types
   */
  
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    super.readExternal(in);

    {
      int size = in.readInt();
      originProcessList = new ArrayList<ProcessType>(size);
      for (int i=0; i<size; i++) {
        originProcessList.add(ProcessType.readExternalSimple(in));
      }
    }

    int size = in.readInt();
    positions = new ArrayList<Position>(size);
    for (int i=0; i<size; i++) {
      positions.add(Position.valueOf((String)in.readObject()));
    }
    average = ProcessType.readExternalSimple(in);
    extreme = ProcessType.readExternalSimple(in);

  }
  /**
   * Writes to external source fire spread data in following order: origin process list, positions, average, and extreme process types
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    out.writeInt(originProcessList.size());
    for (int i=0; i<originProcessList.size(); i++) {
      originProcessList.get(i).writeExternalSimple(out);
    }

    out.writeInt(positions.size());
    for (int i=0; i<positions.size(); i++) {
      out.writeObject(positions.get(i).toString());
    }
    average.writeExternalSimple(out);
    extreme.writeExternalSimple(out);

  }
/**
 * Adds a fire spread logic rule to systems logic data.
 * @param logicData
 */
  public void AddRuleData(FireSpreadLogicData logicData) {
    super.AddRuleData(logicData);
    combinePositions(this.positions,logicData.positions);
  }
  /**
   * method to join two lists into one sorted list
   * @param list1 
   * @param list2
   * @return a concatenated list of list1 and list 2, sorted in ascending order
   */
  private static ArrayList<simpplle.comcode.process.FireEvent.Position> combinePositions(ArrayList<simpplle.comcode.process.FireEvent.Position> list1, ArrayList<simpplle.comcode.process.FireEvent.Position> list2) {
    for (simpplle.comcode.process.FireEvent.Position position : list2) {
      if (list1.contains(position) == false) { list1.add(position); }
    }
    Collections.sort(list1);
    return list1;
  }
/**
 * Checks if rules for fire spread logic are equal.  This compares process, size class, density, treatment, species and seasons list to make sure they are 
 * equal to the rule's.  In addition also compares the average and extreme rules for this fire spread logic.    
 * @param rule
 * @return true if the fire data rule is compatible.  
 */
  public boolean rulesCompatible(FireSpreadLogicData rule) {
    return (super.rulesCompatible(rule) &&
            (average == rule.average) &&
            (extreme == rule.extreme));
  }

  // *** Simulation code ***
  // ***********************
  /**
   * Return the type of fire if the rule matches the data in the given evu.  
   * @param evu Evu
   * @return ProcessType the Type of Fire.
   */
  public ProcessType getFireTypeIfMatch(ProcessType process,
                                        FireResistance resistance,
                                        Evu fromEvu, Evu evu, Lifeform lifeform) {
    if (super.isMatch(resistance,evu,lifeform) == false) { return null; }

    if (originProcessList != null && originProcessList.size() > 0 &&
        originProcessList.contains(process) == false) {
      return null;
    }

    Climate.Season currentSeason =
      Simpplle.getCurrentSimulation().getCurrentSeason();

    Position adjPosition = null;

    switch (fromEvu.getAdjPosition(evu)) {
      case Evu.ABOVE: adjPosition = ABOVE; break;
      case Evu.BELOW: adjPosition = BELOW; break;
      case Evu.NEXT_TO: adjPosition = NEXT_TO; break;
    }
    if (positions.size() > 0 && positions.contains(adjPosition) == false) {
      return null;
    }

    // No longer needed.
//    Species species = evu.getSpecies();
//    if ((species == Species.ND || species == Species.AGR ||
//         species == Species.NF || species == Species.WATER ||
//         species == Species.ROCK_BARE ||
//         species == Species.AGR_URB ||
//         species.getFireResistance() == FireResistance.UNKNOWN)) {
//      return false;
//    }

    boolean isExtreme = simpplle.comcode.process.FireEvent.currentEvent.isExtremeEvent() &&
                        fromEvu.isAdjDownwind(evu);

    ProcessType fireProcessType = (isExtreme) ? extreme : average;
    if (fireProcessType.isFireProcess() == false) { return fireProcessType; }

    int prob = (isExtreme) ? Evu.SE : Evu.S;
    if (Area.multipleLifeformsEnabled()) {
//      evu.updateCurrentStateAllLifeforms(fireProcessType,(short)prob,currentSeason);
      evu.updateCurrentProcess(lifeform,fireProcessType, currentSeason);
      evu.updateCurrentProb(lifeform,prob);
      evu.updateFireSeason(currentSeason);
    }
    else {
      evu.updateCurrentProcess(fireProcessType, currentSeason);
      evu.updateCurrentProb(prob);
      evu.updateFireSeason(currentSeason);
    }
    return fireProcessType;
  }

}



