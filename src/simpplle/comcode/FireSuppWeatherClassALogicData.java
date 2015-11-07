package simpplle.comcode;

import java.io.*;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Weather Class A Logic Data, a type of Logic Data
 *
 * Fire Process logic
 * determine all process probabilities for each evu ->use probabilities to select process
 * if selected process is fire event->if fire suppresssion ->determine probability of staying class size A due to weather or fire suppression → if yes change process for evu to succession and record a class A fire with suppression costs
 * if not suppressed at Class A level → determine type of fire and fire spread → at end of simulation calculate fire suppression costs and emissions
 *
 * if selected process is fire and fire suppression is no, determine probability of staying class A size due to weather → if it spreads beyond class A size determine type of firefighter and fire → at end of simulation calculate emissions
 *
 * if stays at class A size due to weather->change process for evu to succession and record class A fire
 *
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 * 
 */

public class FireSuppWeatherClassALogicData extends LogicData {
//  static final long serialVersionUID = 2946955323252050292L;
  static final int  version          = 1;

  private int prob;

  public FireSuppWeatherClassALogicData() {
    super();

    prob  = 0;

    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_WEATHER_CLASS_A_LOGIC;
  }

  public AbstractLogicData duplicate() {
    FireSuppWeatherClassALogicData logicData = new FireSuppWeatherClassALogicData();
    super.duplicate(logicData);

    logicData.prob  = prob;

    return logicData;
  }

  public Object getValueAt(int col) {
    switch (col) {
      case FireSuppWeatherClassALogic.PROB_COL: return prob;
      default: return super.getValueAt(col);
    }
  }
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireSuppWeatherClassALogic.PROB_COL:
      {
        int newValue = (Integer) value;
        if (prob != newValue) {
          prob = newValue;
          markChanged();
        }
        break;
      }
      default:
        super.setValueAt(col,value);
    }
  }

  public boolean isMatch(Evu evu,
                         Integer tStep,Lifeform lifeform,
                         VegetativeType vegType)
  {
    FireResistance resistance =
      simpplle.comcode.process.FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    if (super.isMatch(resistance,evu,tStep,lifeform,vegType) == false) {
      return false;
    }

    return true;
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    prob  = in.readInt();
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeInt(prob);
  }

  public int getProb() {
    return prob;
  }

  public void setProb(int prob) {
    this.prob = prob;
  }

}
