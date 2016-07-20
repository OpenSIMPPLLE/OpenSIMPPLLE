/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Externalizable;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 * 
 * <p>This class contains methods to get and use the USGS Gap Analysis Program National Land Cover data, a type of Base Logic
 *  
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */

public class GapProcessLogicData extends LogicData implements Externalizable {
  static final long serialVersionUID = -6033703491707993712L;
  static final int  version          = 2;

  ProcessType gapProcess;
  Integer     prob;
/**
 * Constructor.  Initializes the gap process to stand replacing fire, sets the probability to 100 and sets the system knowledge to gap process logic
 */
  public GapProcessLogicData() {
    super();
    gapProcess = ProcessType.STAND_REPLACING_FIRE;
    prob = 100;

    sysKnowKind = SystemKnowledge.Kinds.GAP_PROCESS_LOGIC;
  }
/**
 * duplicates gap process logic data 
 */
  public AbstractLogicData duplicate() {
    GapProcessLogicData logicData = new GapProcessLogicData();
    super.duplicate(logicData);

    logicData.gapProcess = gapProcess;
    logicData.prob       = prob;

    return logicData;
  }
  
  /**
   * compares evu and lifeform to see evu and life form is a match with current.  
   * @return true if a match, false if not or is not a gap process
   */
  public boolean isMatch(Evu evu, Lifeform life) {
    if (super.isMatch(evu,life) == false) {
      return false;
    }

    VegSimStateData state = evu.getState(Simulation.getCurrentTimeStep(),life);
    ProcessType process = (state != null ? state.getProcess() : ProcessType.NONE);
    if (process != gapProcess) {
      return false;
    }

    return true;
  }
/**
 * gets current simulation and compares random number with the probability *100.  if number is less gets the vegetative simulation state with current time stpe and passed in lifeform
 * sets the process to scucession and sets probability
 * <p>Note: simulation returns a random number between 0-10000
 * 
 */
  public void doAction(Evu evu, Lifeform lifeform) {
    Simulation simulation = Simpplle.getCurrentSimulation();
    int num   = simulation.random();
    int cStep = Simulation.getCurrentTimeStep();

    // Simulation returns number between 0-10000
    if (num > (prob*100)) {
      return;
    }

    VegSimStateData state = evu.getState(cStep,lifeform);
    state.setProcess(ProcessType.SUCCESSION);
    state.setProb((short) Evu.GAP);
  }

  public Object getValueAt(int col) {
    switch (col) {
      case GapProcessLogic.GAP_PROCESS_COL: return gapProcess;
      case GapProcessLogic.PROB_COL:        return prob;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets the value in a column for gap process with new gap process type and integer value
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case GapProcessLogic.GAP_PROCESS_COL:
        if (gapProcess != (ProcessType)value) {
          gapProcess = (ProcessType)value;
        }
        break;
      case GapProcessLogic.PROB_COL:
        if (prob != (Integer)value) {
          prob = (Integer) value;
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * Reads object information about gap processes from external source.  These include gap process, and proabilty.  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    super.readExternal(in);

    if (version > 1) {
      gapProcess = ProcessType.readExternalSimple(in);
    }
    prob       = in.readInt();
  }
  /**
   * Writes to an external source the GapProcessLogicData object with gap process and probability.  
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    gapProcess.writeExternalSimple(out);
    out.writeInt(prob);
  }

}
