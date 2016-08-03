/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * This class defines Fire Suppression Class A Logic Data, a type of Logic Data
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions
 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions
 * if stays at class A size due to weather->change process for evu to succession and record class A fire
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.LogicData
 */

public class FireSuppClassALogicData extends LogicData {
  static final long serialVersionUID = -6320463362205096880L;
  static final int  version          = 3;

  private Integer              prob;
//  public  ArrayList<Roads.Status> roadStatusList;
/**
 * Constructor for Fire Suppression Class A Logic Data.  Inherits from Logic Data superclass and initializes 
 * the probability to 100, and sets the system knowledge kind to Fire Suppression Class A Logic
 */
  public FireSuppClassALogicData() {
    super();

    prob = 100;
//    roadStatusList = new ArrayList<Roads.Status>();

    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_CLASS_A_LOGIC;
  }
/**
 * Creates new instance of FireSuppClassALogicData, inherit all instance variables from Logic Data superclass then set fire type, suppress, spread kind, prob to those of this class.
 * This makes a duplicate 
 */
  public AbstractLogicData duplicate() {
    FireSuppClassALogicData logicData = new FireSuppClassALogicData();
    super.duplicate(logicData);

    logicData.prob = prob;
//    logicData.roadStatusList = new ArrayList<Roads.Status>(roadStatusList);
    return logicData;
  }

  public Object getValueAt(int col) {
    switch (col) {
      case FireSuppClassALogic.PROB_COL:        return prob;
//      case FireSuppClassALogic.ROAD_STATUS_COL: return roadStatusList;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets the value at a particular column.  in this class probability is set if it is a new value.  The Fire Supp Class A Logic Data is mark changed.  
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireSuppClassALogic.PROB_COL:
        Integer newValue = (Integer)value;
        if (prob != newValue) {
          prob = newValue;
          markChanged();
        }
        break;
//      case FireSuppClassALogic.ROAD_STATUS_COL:
//        break;
      default:
        super.setValueAt(col,value);
    }
  }

//  public ArrayList<Roads.Status> getRoadStatusList() { return roadStatusList; }

//  public void addRoadStatus(Roads.Status roadStatus) {
//    if (roadStatusList.contains(roadStatus) == false) {
//      roadStatusList.add(roadStatus);
//      SystemKnowledge.markChanged(sysKnowKind);
//    }
//  }
//  public void removeRoadStatus(Roads.Status roadStatus) {
//    if (roadStatusList.contains(roadStatus)) {
//      roadStatusList.remove(roadStatus);
//      SystemKnowledge.markChanged(sysKnowKind);
//    }
//  }

/**
 * Is Match is similar to an equals() method in that it takes in an object, in this case a FireSuppClassALogicData object and compares it this one
 * 
 * @param evu
 * @param tStep
 * @param lifeform
 * @param vegType
 * @return
 */
  public boolean isMatch(Evu evu,Integer tStep,Lifeform lifeform,
                         VegetativeType vegType)
  {
    FireResistance resistance =
      FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    if (super.isMatch(resistance,evu,tStep,lifeform,vegType) == false) {
      return false;
    }

    return true;
  }
/**
 * reads in a FireSuppClassALogic object, for this class specifically the probability and road status list
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    prob = in.readInt();

    if (version == 1) {
      ArrayList<String> tmpList = (ArrayList<String>) in.readObject();
      roadStatusList = new ArrayList<Roads.Status>();
      for (String statusName : tmpList) {
        roadStatusList.add(Roads.Status.lookup(statusName));
      }
    }
    else if (version == 2) {
      roadStatusList = (ArrayList<Roads.Status>)in.readObject();
    }
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeInt(prob);
//    out.writeObject(roadStatusList);
  }
/**
 * 
 * @return true if instance random variable is <= instance rational probability
 */
  public Boolean isSuppressed() {
    int rand = Simulation.getInstance().random();
    int ratProb = Simulation.getInstance().getRationalProbability(prob);
    return (rand <= ratProb);
  }

}
