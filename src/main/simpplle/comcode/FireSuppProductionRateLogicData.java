/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.*;

/**
 * This class defines Fire Suppression Rate Logic, a type of Base Logic
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 */

public class FireSuppProductionRateLogicData extends LogicData {
  static final long serialVersionUID = 2946955323252050292L;
  static final int  version          = 2;
  // version 2 addes min/max event acres;

  private float slope;
  private int rate;
  
  private Range acresRange;

  public FireSuppProductionRateLogicData() {
    super();

    slope = 0.0f;
    rate  = 0;
    
    int minAcres = 0;
    int maxAcres = Area.getRationalAcres(1000000);
    
    acresRange = new Range(minAcres,maxAcres);

    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_PRODUCTION_RATE_LOGIC;
  }

  public AbstractLogicData duplicate() {
    FireSuppProductionRateLogicData logicData = new FireSuppProductionRateLogicData();
    super.duplicate(logicData);

    logicData.acresRange = new Range(acresRange.lower,acresRange.upper);
    logicData.slope = slope;
    logicData.rate  = rate;

    return logicData;
  }

  private int getNonRationalAcres(int acres) {
    float fAcres = Area.getFloatAcres(acres);
    return Math.round(fAcres);
  }
  public Object getValueAt(int col) {
    switch (col) {
      case FireSuppProductionRateLogic.MIN_EVENT_ACRES_COL:  return getNonRationalAcres(acresRange.getLower());
      case FireSuppProductionRateLogic.MAX_EVENT_ACRES_COL:  return getNonRationalAcres(acresRange.getUpper());
      case FireSuppProductionRateLogic.SLOPE_COL:            return slope;
      case FireSuppProductionRateLogic.RATE_COL:             return rate;
      default: return super.getValueAt(col);
    }
  }
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireSuppProductionRateLogic.MIN_EVENT_ACRES_COL:
      {
        int acres = Area.getRationalAcres((Integer)value);
        if (acres >= 0 && acresRange.getLower() != acres && acres < acresRange.getUpper()) {
          acresRange.setLower(acres);
          markChanged();
        }
        break;
      }
      case FireSuppProductionRateLogic.MAX_EVENT_ACRES_COL:
      {
        int acres = Area.getRationalAcres((Integer)value);
        if (acresRange.getUpper() != acres && acres > acresRange.getLower()) {
          acresRange.setUpper(acres);
          markChanged();
        }
        break;
      }
      case FireSuppProductionRateLogic.SLOPE_COL:
      {
        slope = (Float)value;
        markChanged();
        break;
      }
      case FireSuppProductionRateLogic.RATE_COL:
      {
        int newValue = (Integer) value;
        if (rate != newValue) {
          rate = newValue;
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
                         VegetativeType vegType, int eventAcres)
  {
    FireResistance resistance =
      FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    if (super.isMatch(resistance,evu,tStep,lifeform,vegType) == false) {
      return false;
    }
   
    if (!acresRange.inRange(eventAcres)) {
      return false;
    }
    
    float evuSlope = evu.getSlope();
    if (evuSlope < slope) {
      return false;
    }

    return true;
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    if (version > 1) {
      acresRange.setLower(in.readInt());
      acresRange.setUpper(in.readInt());
    }
    slope = in.readFloat();
    rate  = in.readInt();
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeInt(acresRange.getLower());
    out.writeInt(acresRange.getUpper());
    out.writeFloat(slope);
    out.writeInt(rate);
  }
/**
 * Gets the rate of fire suppression.
 * @return
 */
  public int getRate() {
    return rate;
  }
/**
 * Sets the rate of fire suppression.  
 * @param rate
 */
  public void setRate(int rate) {
    this.rate = rate;
  }
/**
 * Sets the slope.
 * @param slope
 */
  public void setSlope(float slope) {
    this.slope = slope;
  }

}
