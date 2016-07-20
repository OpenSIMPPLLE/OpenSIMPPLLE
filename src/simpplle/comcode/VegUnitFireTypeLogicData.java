/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.ArrayList;
import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;

/**
 *
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class VegUnitFireTypeLogicData extends AbstractLogicData implements Externalizable {
  static final long serialVersionUID = 2319729666487038806L;
  static final int  version          = 1;

  // Storing these as strings so that we can display the fire process
  // short name's in the table.  This avoids confusing conversion code
  // to show short names.
  private ArrayList<String> trees   = new ArrayList<String>();
  private ArrayList<String> shrubs  = new ArrayList<String>();
  private ArrayList<String> grasses = new ArrayList<String>();
  private ProcessType       result;

  public VegUnitFireTypeLogicData() {
    super();

    result = ProcessType.LSF;

    sysKnowKind = SystemKnowledge.Kinds.GAP_PROCESS_LOGIC;
  }

  public AbstractLogicData duplicate() {
    VegUnitFireTypeLogicData logicData = new VegUnitFireTypeLogicData();
    super.duplicate(logicData);

    logicData.trees   = new ArrayList<String>(logicData.trees);
    logicData.shrubs  = new ArrayList<String>(logicData.shrubs);
    logicData.grasses = new ArrayList<String>(logicData.grasses);
    logicData.result  = result;

    return logicData;
  }

  public Object getValueAt(int col) {
    switch (col) {
      case VegUnitFireTypeLogic.TREES_COL:   return trees;
      case VegUnitFireTypeLogic.SHRUBS_COL:  return shrubs;
      case VegUnitFireTypeLogic.GRASS_COL:   return grasses;
      case VegUnitFireTypeLogic.RESULT_COL:  return result.getShortName();
      default: return "";
    }
  }

  public void setValueAt(int col, Object value) {
    switch (col) {
      case VegUnitFireTypeLogic.RESULT_COL:
        ProcessType process = ProcessType.get((String)value);
        if (result != process) {
          result = process;
        }
        break;
      default:
    }
  }
/**
 * Checks to make sure a particular tree, shrub, and grass process matches the fire type.  This is used to compare logic rules to data.  
 * @param treeProcess
 * @param shrubProcess
 * @param grassProcess
 * @return
 */
  public boolean isMatch(ProcessType treeProcess, ProcessType shrubProcess, ProcessType grassProcess) {
    return (trees.contains(treeProcess.getShortName()) &&
            shrubs.contains(shrubProcess.getShortName()) &&
            grasses.contains(grassProcess.getShortName()));
  }
/**
 * By default this is LSF.  
 * @return
 */
  public ProcessType getResult() {
    return result;
  }

  /**
   * Reads from external source the following processes: trees, shrubs, grasses, or another process type. 
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    super.readExternal(in);

    trees   = readProcesses(in);
    shrubs  = readProcesses(in);
    grasses = readProcesses(in);
    result  = ProcessType.readExternalSimple(in);
  }
  private ArrayList<String> readProcesses(ObjectInput in)
      throws IOException, ClassNotFoundException
  {
    ArrayList<String> values = new ArrayList<String>();
    int size = in.readInt();
    for (int i=0; i<size; i++) {
      ProcessType process = ProcessType.readExternalSimple(in);
      values.add(process.getShortName());
    }

    return values;
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    writeProcesses(out,trees);
    writeProcesses(out,shrubs);
    writeProcesses(out,grasses);
    result.writeExternalSimple(out);
  }
  private void writeProcesses(ObjectOutput out, ArrayList<String> values) throws IOException {
    out.writeInt(values.size());
    for (String processName : values) {
      ProcessType.get(processName).writeExternalSimple(out);
    }
  }

  public ArrayList getPossibleValues(int col) { return null; }
  public Object getListValueAt(int listIndex, int col) { return null; }
  public void addListValueAt(int col, Object value) {}
  public void removeListValueAt(int col, Object value) {}
  public int getListRowCount(int col) { return 0; }
  public boolean hasListValue(int col, Object value) { return false; }

}



