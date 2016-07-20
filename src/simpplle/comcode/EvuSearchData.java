/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.util.*;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Externalizable;
import simpplle.comcode.Roads.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *<p>This class manages the search data for EVU, a type of logic data.
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 *@see simpplle.comcode.LogicData
 */
public class EvuSearchData extends LogicData implements Externalizable {
  static final long serialVersionUID = 274886327198642218L;
  static final int  version          = 3;

  private ArrayList<Integer>        timeStepList;
  private ArrayList<Integer>        ageList;
  private ArrayList<String>         fmzList;
  private ArrayList<String>         probList;
  
  /**
   * Constructor.  initializes variables process time steps, treatment time steps, and creates new arrayLists for incoming search data
   */
  public EvuSearchData() {
    super();

    processTSteps   = 0;
    treatmentTSteps = 0;

    timeStepList    = new ArrayList<Integer>();
    ageList         = new ArrayList<Integer>();
    fmzList         = new ArrayList<String>();
    probList        = new ArrayList<String>();

    sysKnowKind = SystemKnowledge.Kinds.EVU_SEARCH_LOGIC;
  }

  public AbstractLogicData duplicate() {
    EvuSearchData logicData = new EvuSearchData();
    super.duplicate(logicData);

    logicData.timeStepList    = new ArrayList<Integer>(timeStepList);
    logicData.ageList         = new ArrayList<Integer>(ageList);
    logicData.fmzList         = new ArrayList<String>(fmzList);
    logicData.probList        = new ArrayList<String>(probList);

    return logicData;
  }
  /**
   * Compares the fire management zone, time step, lifeforms and vegetative state age to evaluate whether it matches the passed EVU 
   */

  public boolean isMatch(Evu evu) {
    if (fmzList != null && fmzList.size() > 0) {
      if (fmzList.contains(evu.getFmz().getName()) == false) {
        return false;
      }
    }

    ArrayList<Integer> times;
    if (timeStepList.size() > 0) {
      times = new ArrayList<Integer>(timeStepList);
    }
    else {
      times = new ArrayList<Integer>();
      times.add(Simulation.getCurrentTimeStep());
    }

    Lifeform[] lives = Lifeform.getAllValues();
    for (int l = 0; l < lives.length; l++) {
      for (int tsIndex = 0; tsIndex < times.size(); tsIndex++) {
        int ts = times.get(tsIndex);

        if (evu.hasLifeform(lives[l],ts) == false) {
          continue;
        }

        if (super.isMatch(evu, ts, lives[l]) == false) {
          continue;
        }

        if (super.isLifeformMatch(lives[l]) == false) {
          continue;
        }

        VegSimStateData state = evu.getState(ts,lives[l]);

        if (ageList != null && ageList.size() > 0) {
          if (ageList.contains(state.getVegType().getAge()) == false) {
            continue;
          }
        }

        if (probList != null && probList.size() > 0) {
          if (probList.contains(state.getProbString()) == false) {
            continue;
          }
        }

        // If we made it here then we must haved passed so we have a match.
        // Since there are no more test we can return at this point.
        return true;
      }
    }

    return false;
  }
/**
 * Gets the time step, age, fire management zone, and probability value at specified column
 */
  public Object getValueAt(int col) {
    switch (col) {
      case EvuSearchLogic.TIME_STEP_COL:    return timeStepList;
      case EvuSearchLogic.AGE_COL:          return ageList;
      case EvuSearchLogic.FMZ_COL:          return fmzList;
      case EvuSearchLogic.PROB_COL:         return probList;
      default: return super.getValueAt(col);
    }
  }
  
  /**
   * Sets the column value of time step, age, fire management zone, and probability by calling the simpplle.comcode.LogicData superclass method setValueAt
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case EvuSearchLogic.TIME_STEP_COL:    break;
      case EvuSearchLogic.AGE_COL:          break;
      case EvuSearchLogic.FMZ_COL:          break;
      case EvuSearchLogic.PROB_COL:         break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * Read function that takes in object input. Currently version is read in, but nothing is done with it. Might be deprecated in V1.1
 * EviSearch objects are read in the following order.  time step list, age list, special area, ownership, fire management zone list, road status list, probability list.  
 */
  @SuppressWarnings("unchecked")
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    if (version == 1) {
      throw new IOException();
    }

    super.readExternal(in);

    timeStepList    = (ArrayList<Integer>)in.readObject();
    ageList         = (ArrayList<Integer>)in.readObject();
    
    if (version == 2) {
      ArrayList<String> tmpSpecialAreaList = (ArrayList<String>)in.readObject();
      for (String sa : tmpSpecialAreaList) {
        addListValueAt(BaseLogic.SPECIAL_AREA_COL,SpecialArea.get(sa,true));
      }
      
      ArrayList<String> tmpOwnershipList   = (ArrayList<String>)in.readObject();
      for (String owner : tmpOwnershipList) {
        addListValueAt(BaseLogic.OWNERSHIP_COL,Ownership.get(owner,true));
      }
    }
    fmzList         = (ArrayList<String>)in.readObject();
    
    if (version == 2) {
      roadStatusList  = (ArrayList<Roads.Status>) in.readObject();
    }
    
    probList        = (ArrayList<String>)in.readObject();
  }
  /**
   * Method to write time step list, age list, fire management zone list, probability list out.  
   * @throws IOException - caught in gui
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    out.writeObject(timeStepList);
    out.writeObject(ageList);
    out.writeObject(fmzList);
    out.writeObject(probList);
  }
/**
 * 
 */
  public ArrayList getPossibleValues(int col) {
    switch (col) {
      case EvuSearchLogic.TIME_STEP_COL:
        Simulation sim = Simulation.getInstance();
        int nSteps = (sim != null) ? sim.getNumTimeSteps() : 0;
        ArrayList values = new ArrayList(nSteps+1);
        for (int i=0; i<=nSteps; i++) {
          values.add(i);
        }
        return values;

      case EvuSearchLogic.AGE_COL:
        return HabitatTypeGroup.getAllAge();

//      case EvuSearchLogic.SPECIAL_AREA_COL:
//        return Simpplle.getCurrentArea().getAllSpecialArea();
//
//      case EvuSearchLogic.OWNERSHIP_COL:
//        return Simpplle.getCurrentArea().getAllOwnership();

      case EvuSearchLogic.FMZ_COL:
        return new ArrayList(Simpplle.getCurrentZone().getAllFmzNames());

//      case EvuSearchLogic.ROAD_STATUS_COL:
//        return new ArrayList<Roads.Status>(Arrays.asList(Roads.Status.values()));

      case EvuSearchLogic.PROB_COL:
        return Evu.getAllProbabilitySpecial();

      default:
        return null;
    }
  }

}
