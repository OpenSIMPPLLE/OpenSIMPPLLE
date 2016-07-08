package simpplle.comcode;

import simpplle.comcode.FireEvent.Position;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p> This class defines Fire Spread Logic Data, a type of Logic Data
 *
 * <p> Original source code authorship: Kirk A. Moeller
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

    originProcessList = new ArrayList<>(3);
    positions         = new ArrayList<>(3);
    average           = ProcessType.LSF;
    extreme           = ProcessType.LSF;
    sysKnowKind       = SystemKnowledge.Kinds.FIRE_SPREAD_LOGIC;

  }

  /**
   * Returns a duplicate of this instance.
   */
  public AbstractLogicData duplicate() {

    FireSpreadLogicData logicData = new FireSpreadLogicData();

    super.duplicate(logicData);

    logicData.originProcessList = new ArrayList<>(originProcessList);
    logicData.positions         = new ArrayList<>(positions);
    logicData.average           = average;
    logicData.extreme           = extreme;

    return logicData;

  }

  /**
   * Returns a value from the specified column index.
   */
  public Object getValueAt(int col) {
    switch (col) {
      case FireEventLogic.ORIGIN_PROCESS_COL: return getOriginProcessListDesc();
      case FireEventLogic.POSITION_COL:       return positions;
      case FireEventLogic.AVERAGE_COL:        return average.getShortName();
      case FireEventLogic.EXTREME_COL:        return extreme.getShortName();
      default: return super.getValueAt(col);
    }
  }

  /**
   * Stores a value at the specified column index.
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireEventLogic.POSITION_COL: break;
      case FireEventLogic.AVERAGE_COL:  average = ProcessType.get((String)value); break;
      case FireEventLogic.EXTREME_COL:  extreme = ProcessType.get((String)value); break;
      default: super.setValueAt(col,value);
    }
  }

  /**
   * Adds a position if it does not already exist.
   */
  public void addPosition(Position position) {
    if (positions.contains(position) == false) {
      positions.add(position);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Removes a position.
   */
  public void removePosition(Position position) {
    if (positions.contains(position)) {
      positions.remove(position);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Returns true if this has the specified position.
   */
  public boolean hasPosition(Position pos) {
    return positions.contains(pos);
  }

  /**
   * Adds an origin process if it does not already exist.
   */
  public void addOriginProcess(ProcessType process) {
    if (originProcessList.contains(process) == false) {
      originProcessList.add(process);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Removes an origin process.
   */
  public void removeOriginProcess(ProcessType process) {
    if (originProcessList.contains(process)) {
      originProcessList.remove(process);
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Returns true if this has the specified origin process.
   */
  public boolean hasOriginProcess(ProcessType process) {
    return originProcessList.contains(process);
  }

  /**
   * Sets the extreme fire process type.
   */
  public void setExtreme(ProcessType extreme) {
    if (this.extreme != extreme) {
      this.extreme = extreme;
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Sets the average fire process type.
   */
  public void setAverage(ProcessType average) {
    if (this.average != average) {
      this.average = average;
      SystemKnowledge.setHasChanged(SystemKnowledge.FIRE_SPREAD_LOGIC,true);
    }
  }

  /**
   * Returns a string representation of the contained origin processes.
   */
  private String getOriginProcessListDesc() {

    StringBuffer strBuf = new StringBuffer("[");

    for (int i = 0; i < originProcessList.size(); i++) {
      if (i != 0) { strBuf.append(","); }
      strBuf.append(originProcessList.get(i).getShortName());
    }

    strBuf.append("]");

    return strBuf.toString();

  }

  /**
   * Sorts contained lists of data ascending order.
   */
  public void sortLists() {

    super.sortLists();

    Collections.sort(originProcessList);
    Collections.sort(positions);

  }

  /**
   * Reads external fire spread logic data in following order: origin process list, positions list, average, and extreme process types
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
   * Writes to external fire spread data in following order: origin process list, positions, average, and extreme process types
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
   */
  public void AddRuleData(FireSpreadLogicData logicData) {

    super.AddRuleData(logicData);

    combinePositions(this.positions,logicData.positions);

  }

  /**
   * Returns list 1 combined with the members of list 2. The list is sorted in ascending order.
   */
  private static ArrayList<FireEvent.Position> combinePositions(ArrayList<FireEvent.Position> list1, ArrayList<FireEvent.Position> list2) {

    for (FireEvent.Position position : list2) {
      if (list1.contains(position) == false) { list1.add(position); }
    }

    Collections.sort(list1);

    return list1;

  }

  /**
   * Checks if the rules for fire spread logic are equal. The processes, size classes, densities, treatments, species,
   * seasons, average process type, and extreme process type are compared.
   */
  public boolean rulesCompatible(FireSpreadLogicData rule) {
    return (super.rulesCompatible(rule) &&
            (average == rule.average) &&
            (extreme == rule.extreme));
  }

  // ***********************
  // *** Simulation code ***
  // ***********************

  /**
   * Returns a fire process type if the rule applies to the provided arguments. The current process, current
   * probability, and fire season are updated in the destination unit if the rule matches. Empty lists are treated
   * as if they contain all possible values, so they will always match their respective inputs.
   *
   * @param process The spreading process
   * @param resistance The fire resistance of the 'to' EVU
   * @param fromEvu The unit we are trying to spread from
   * @param toEvu The unit we are trying to spread to
   * @param lifeform The life form to be considered
   *
   * @return A fire process type if the rule matches, otherwise null
   */
  public ProcessType getFireTypeIfMatch(ProcessType process, FireResistance resistance, Evu fromEvu, Evu toEvu, Lifeform lifeform) {

    if (!super.isMatch(resistance,toEvu,lifeform)) {

      return null;

    }

    if (originProcessList.size() > 0 && !originProcessList.contains(process)) {

      return null;

    }

    Position adjPosition = null;

    switch (fromEvu.getAdjPosition(toEvu)) {

      case Evu.ABOVE:   adjPosition = ABOVE;   break;
      case Evu.BELOW:   adjPosition = BELOW;   break;
      case Evu.NEXT_TO: adjPosition = NEXT_TO; break;

    }

    if (positions.size() > 0 && !positions.contains(adjPosition)) {

      return null;

    }

    boolean isExtreme = FireEvent.currentEvent.isExtremeEvent() && fromEvu.isAdjDownwind(toEvu);

    ProcessType fireProcessType = isExtreme ? extreme : average;

    if (!fireProcessType.isFireProcess()) {

      return fireProcessType;

    }

    Climate.Season currentSeason = Simpplle.getCurrentSimulation().getCurrentSeason();

    int prob = isExtreme ? Evu.SE : Evu.S;

    if (Area.multipleLifeformsEnabled()) {

      toEvu.updateCurrentProcess(lifeform, fireProcessType, currentSeason);
      toEvu.updateCurrentProb(lifeform, prob);
      toEvu.updateFireSeason(currentSeason);

    } else {

      toEvu.updateCurrentProcess(fireProcessType, currentSeason);
      toEvu.updateCurrentProb(prob);
      toEvu.updateFireSeason(currentSeason);

    }

    return fireProcessType;

  }

}



