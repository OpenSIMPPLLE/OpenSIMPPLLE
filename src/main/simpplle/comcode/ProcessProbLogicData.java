/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods for a Process Probability Logic Data.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class ProcessProbLogicData
    extends LogicData implements Externalizable
{
  static final long serialVersionUID = -1445913843517091921L;
  static final int  version          = 1;

  protected ArrayList<SimpplleType>       adjProcessList;
  protected Integer             adjProcessTSteps;
  protected String              adjProcessDesc;
  protected boolean             defaultAdjProcessDesc;

  protected ArrayList<MtnPineBeetleHazard.Hazard> mpbHazardList;
  protected boolean                       adjModHazard;
  protected boolean                       adjHighHazard;
  protected int prob;
  protected ProcessType process;

  /**
   * Primary constructor for process probability logic data.  
   * Initializes variables and structures to handle adjacent area info like adjacent process list, adjacent process time steps, adjacent process description, probability to 0, mountain pine beetle arraylist
   * adjacent hazards - moderate and high, and system knowledge to process probability logic 
   */
  public ProcessProbLogicData() {
    adjProcessList = new ArrayList<SimpplleType>();
    adjProcessTSteps      = 1;
    adjProcessDesc        = "[]";
    defaultAdjProcessDesc = true;


    prob            = 0;
    mpbHazardList = new ArrayList<MtnPineBeetleHazard.Hazard>();
    adjModHazard    = false;
    adjHighHazard   = false;
    sysKnowKind     = SystemKnowledge.Kinds.PROCESS_PROB_LOGIC;
  }
  /**
   * Overloaded process probability logic data.  References primary constructor plus initializes the process name.
   * @param processName
   */
  public ProcessProbLogicData(String processName) {
    this();
    process = ProcessType.get(processName);
  }
  /**
   * 
   */
  public AbstractLogicData duplicate() {
    ProcessProbLogicData logicData = new ProcessProbLogicData();
    super.duplicate(logicData);

    logicData.process               = process;
    logicData.adjProcessList        = new ArrayList<SimpplleType>(adjProcessList);
    logicData.adjProcessTSteps      = adjProcessTSteps;
    logicData.adjProcessDesc        = adjProcessDesc;
    logicData.defaultAdjProcessDesc = defaultAdjProcessDesc;
    logicData.mpbHazardList         = new ArrayList<MtnPineBeetleHazard.Hazard>(mpbHazardList);
    logicData.adjModHazard          = adjModHazard;
    logicData.adjHighHazard         = adjHighHazard;
    logicData.prob                  = prob;

    return logicData;
  }
  /**
   * method to sort list.  Uses the Java collections default sort which is ascending order.  
   */
  public void sortLists() {
    super.sortLists();
    Collections.sort(adjProcessList);
    Collections.sort(mpbHazardList);
  }

  public Integer getAdjProcessPastTimeSteps() { return adjProcessTSteps; }
  /**
   * checks to see if time step in adjacent process area is equal to the parameter time step, if not sets the adjacent process step to time step.   
   * @param ts time step
   */
  public void setAdjProcessPastTimeSteps(Integer ts) {
    if (adjProcessTSteps != ts) {
      adjProcessTSteps = ts;
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * checks if process type is in the adjacent process list
 * @param process
 * @return true of an adjacent process
 */
  public boolean isMemberAdjProcess(ProcessType process) {
    return adjProcessList.contains(process);
  }
  /**
   * adds a process to the adjacent process list and marks system knowledge changed
   * @param process
   */
  public void addAdjProcess(ProcessType process) {
    if (adjProcessList.contains(process) == false) {
      adjProcessList.add(process);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * removes a process to the adjacent process list and marks the system knowledge changed
   * @param process
   */
  public void removeAdjProcess(ProcessType process) {
    if (adjProcessList.contains(process)) {
      adjProcessList.remove(process);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * sets the adjacent process description to the passed string literal, deactivates the default adjacent process description "[]", and 
   * marks the system knowledge changed.  
   * @param desc the description of the adjacent process
   */
  public void setAdjProcessDescription(String desc) {
    if (desc == null || desc.length() == 0) {
      setAdjProcessDescDefault();
      return;
    }
    adjProcessDesc = desc;
    defaultAdjProcessDesc = false;
    SystemKnowledge.markChanged(sysKnowKind);
  }
  /**
   * 
   * @return true if adjacent process is the default adjacent process description ("[]"
   */
  public boolean isDefaultAdjProcessDescription() {
    return defaultAdjProcessDesc;
  }
  /**
   * gets adjacent process description 
   * @return adjacent process as string literal
   */
  public String getAdjProcessDescription() { return adjProcessDesc; }
/**
 * Sets the default process description in adjacent Evu.
 */
  public void setAdjProcessDescDefault() {
    adjProcessDesc = (adjProcessList != null) ? adjProcessList.toString() : "[]";
    defaultAdjProcessDesc = true;
  }

  public void addMpbHazard(MtnPineBeetleHazard.Hazard hazard) {
    if (mpbHazardList.contains(hazard) == false) {
      mpbHazardList.add(hazard);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  public void removeMpbHazard(MtnPineBeetleHazard.Hazard hazard) {
    if (mpbHazardList.contains(hazard)) {
      mpbHazardList.remove(hazard);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
/**
 * method to get a columns value depending based on ordinal into process probability logic enum.
 * These are in order ADJ_PROCESS_COL,MPB_HAZARD_COL,ADJ_MOD_HAZARD_COL,ADJ_HIGH_HAZARD_COL,PROB_COL
 */
  public Object getValueAt(int col) {
    switch (col) {
      case ProcessProbLogic.ADJ_PROCESS_COL:     return adjProcessDesc;
      case ProcessProbLogic.MPB_HAZARD_COL:    return mpbHazardList;
      case ProcessProbLogic.ADJ_MOD_HAZARD_COL:  return adjModHazard;
      case ProcessProbLogic.ADJ_HIGH_HAZARD_COL: return adjHighHazard;
      case ProcessProbLogic.PROB_COL:            return prob;
      default:
        return super.getValueAt(col);
    }
  }
  /**
   * sets the column value based on column number.  
   * these are in order ADJ_PROCESS_COL,MPB_HAZARD_COL,ADJ_MOD_HAZARD_COL,ADJ_HIGH_HAZARD_COL,PROB_COL
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case ProcessProbLogic.ADJ_PROCESS_COL:     break; // set in table editor
      case ProcessProbLogic.MPB_HAZARD_COL:    break; // set in table editor
      case ProcessProbLogic.ADJ_MOD_HAZARD_COL:
        if (adjModHazard != (Boolean)value) {
          adjModHazard = (Boolean) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case ProcessProbLogic.ADJ_HIGH_HAZARD_COL:
        if (adjHighHazard != (Boolean)value) {
          adjHighHazard = (Boolean) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case ProcessProbLogic.PROB_COL:
        if (prob != (Integer)value) {
          prob = (Integer) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * writes to an external source.  Invokes the LogicData superclass, then writes out objects specific to this class
 * these are, in order: version, adjacent process list, adjacent process time steps, adjacent process description, default adjacent process description,
 * mountain pine beetle hazard list, adjacent moderate hazard, adjacent high hazard, and probability.  
 */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(version);

    process.writeExternalSimple(out);

    out.writeObject(adjProcessList);
    out.writeInt(adjProcessTSteps);
    out.writeObject(adjProcessDesc);
    out.writeBoolean(defaultAdjProcessDesc);

    out.writeObject(mpbHazardList);
    out.writeBoolean(adjModHazard);
    out.writeBoolean(adjHighHazard);
    out.writeInt(prob);
  }
/**
 * reads process probability logic data from external source.  Objects read in are, in order:
 * version, adjacent process list, adjacent process time steps, adjacent process description, default adjacent process description,
 * mountain pine beetle hazard list, adjacent moderate hazard, adjacent high hazard, and probability.
 * 
 */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    super.readExternal(in);
    int version = in.readInt();

    process = ProcessType.readExternalSimple(in);

    adjProcessList        = (ArrayList<SimpplleType>)in.readObject();
    adjProcessTSteps      = in.readInt();
    adjProcessDesc        = (String)in.readObject();
    defaultAdjProcessDesc = in.readBoolean();

    mpbHazardList = (ArrayList<MtnPineBeetleHazard.Hazard>)in.readObject();
    adjModHazard    = in.readBoolean();
    adjHighHazard   = in.readBoolean();
    prob            = in.readInt();
  }

  public ArrayList getMpbHazardList() {
    return mpbHazardList;
  }
/**
 * Gets the arraylist containing adjacent processes.  
 * @return
 */
  public ArrayList getAdjProcessList() {
    return adjProcessList;
  }

  public boolean adjProcessListHasData() {
    return (adjProcessList != null && adjProcessList.size() > 0);
  }

  public boolean isMatch(Evu evu) {
    if (super.isMatch(evu) == false) { return false; }

    int cTime = Simpplle.getCurrentSimulation().getCurrentTimeStep();
    boolean aMatch=false;

    // *** Adjacent Process ***
    AdjacentData[] adjData = evu.getNeighborhoodNotNull();

    if (adjProcessListHasData()) {
      aMatch = false;
      for (AdjacentData neighbor : adjData) {
        VegSimStateData adjState = neighbor.evu.getState(cTime - adjProcessTSteps);
        if (adjState == null) {
          continue;
        }
        ProcessType adjProcess = adjState.getProcess();
        if (adjProcessList.contains(adjProcess)) {
          aMatch = true;
          break;
        }
      }
      if (aMatch == false) {
        return false;
      }
    }

    if (process == ProcessType.LIGHT_LP_MPB ||
        process == ProcessType.SEVERE_LP_MPB ||
        process == ProcessType.PP_MPB)
    {
      if (mpbHazardList != null && mpbHazardList.size() > 0) {
        MtnPineBeetleHazard.Hazard hazard = evu.getLpMpbHazard();
        if (mpbHazardList.contains(hazard) == false) { return false; }
      }

      if (adjModHazard) {
        aMatch = false;
        for (int i = 0; i < adjData.length; i++) {
          MtnPineBeetleHazard.Hazard hazard = adjData[i].evu.getMpbHazard(process);
          if (hazard == MtnPineBeetleHazard.Hazard.MODERATE) { aMatch = true; } break;
        }
        if (!aMatch) { return false; }
      }

      if (adjHighHazard) {
        aMatch = false;
        for (int i = 0; i < adjData.length; i++) {
          MtnPineBeetleHazard.Hazard hazard = adjData[i].evu.getMpbHazard(process);
          if (hazard == MtnPineBeetleHazard.Hazard.HIGH) { aMatch = true; } break;
        }
        if (!aMatch) { return false; }
      }
    }


    return true;
  }

  public Integer getProbability(Evu evu) {
    if (isMatch(evu)) {
      return prob;
    }
    return null;
  }

}
