
package simpplle.comcode.logic;

import simpplle.comcode.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 *  
 * <p>This class defines Fire Spotting Logic Data, a type of Logic Data.  Elements used in the decisions for whether a fire will spot from one evu to another
 * are start distance (fire will spot within this distance), rational end distance (too far for fire spotting).
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */
public class FireSpottingLogicData extends LogicData implements Externalizable {
  private static final long serialVersionUID = 7794891617742162873L;
  private static final int  version          = 1;

  public enum SpreadType { AVERAGE, EXTREME }
  
  private ProcessType fireProcess;
  private SpreadType  fireSpreadType;

  private double startDist;
  private long   ratStartDist;
  private double endDist;
  private long   ratEndDist;
  private int    prob;
  
  private static double maxDist;
  private static long   ratMaxDist;
  
  /**
   * constructor.  Initializes variables and inherits form LogicData superclass
   * Initiializes the fire variable to stand replacing fire and spread type to extreme.
   */
  public FireSpottingLogicData() {
    super();


    fireProcess = ProcessType.SRF;
    fireSpreadType = SpreadType.EXTREME;
    startDist = 0.0;
    ratStartDist = 0;
    endDist   = 0.0;
    ratEndDist = 0;
    maxDist = -1.0;
    ratMaxDist = -1;
    prob      = 0;
    
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SPOTTING_LOGIC;
  }


    /**
     * Makes a duplicate of this Fire Spotting Logic Data.
     * @return
     */
  public AbstractLogicData duplicate() {
    FireSpottingLogicData logicData = new FireSpottingLogicData();
    super.duplicate(logicData);

    logicData.fireProcess    = fireProcess;
    logicData.fireSpreadType = fireSpreadType;
    logicData.startDist      = startDist;
    logicData.endDist        = endDist;
    logicData.prob           = prob;
    
    return logicData;
  }
  
  public static SpreadType[] getSpreadTypes() {
    return SpreadType.values();
  }

    /**
     * Gets the fire spotting logic data from user at specified columns.  These are Fire Process (SRF, MSF, LSF), Spread type (Average, Extreme)
     * Start distance, end distance and probability.
     * @param col
     * @return
     */
  public Object getValueAt(int col) {
    switch (col) {
      case simpplle.comcode.logic.FireEventLogic.FIRE_PROCESS_COL:  return fireProcess.getShortName();
      case simpplle.comcode.logic.FireEventLogic.SPREAD_TYPE_COL:   return fireSpreadType;
      case simpplle.comcode.logic.FireEventLogic.START_DIST_COL:    return startDist;
      case simpplle.comcode.logic.FireEventLogic.END_DIST_COL:      return endDist;
      case simpplle.comcode.logic.FireEventLogic.PROB_COL:          return prob;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets the Fire process type, fire spread type, fire start and end distribution, and fire event probability in the columns  for the GUI
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case simpplle.comcode.logic.FireEventLogic.FIRE_PROCESS_COL:
        ProcessType newValue = ProcessType.get((String)value);
        if (!fireProcess.equals(newValue)) {
          fireProcess = newValue;
          markChanged();
        }
        break;
      case simpplle.comcode.logic.FireEventLogic.SPREAD_TYPE_COL:
        SpreadType newSpreadtype = (SpreadType)value;
        if (fireSpreadType != newSpreadtype) {
          fireSpreadType = newSpreadtype;
          markChanged();
        }
        break;
      case simpplle.comcode.logic.FireEventLogic.START_DIST_COL:
        startDist = (Double)value;
        ratStartDist = getRatDist(startDist);
        markChanged();
        break;
      case simpplle.comcode.logic.FireEventLogic.END_DIST_COL:
        endDist = (Double)value;
        ratEndDist = getRatDist(endDist);
        updateMaxDist(endDist);
        markChanged();
        break;
      case simpplle.comcode.logic.FireEventLogic.PROB_COL:
        Integer newProb = (Integer)value;
        if (newProb != null && prob != newProb) {
          prob = newProb;
          markChanged();
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * takes in a distribution compares it to current max distribution.  if it is greater updates the max distribution to current rational max distribution
 * @param dist
 */
  private static void updateMaxDist(double dist) {
    if (dist > maxDist) {
      maxDist = dist;
      ratMaxDist = getRatDist(maxDist);
    }
  }
  private static long getRatDist(double dist) {
    return Math.round(dist * 100.0);
  }

  public int getProbability() {
    return prob;
  }
/**
 * method to compare if a fire spotting logic is match with current logic 
 * @param fromEvu
 * @param toEvu
 * @param simFireProcess
 * @param isExtremeSpread
 * @return
 */
  public boolean isMatch(simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu toEvu, ProcessType simFireProcess, boolean isExtremeSpread) {
    SpreadType simFireSpreadType = isExtremeSpread ? SpreadType.EXTREME : SpreadType.AVERAGE;
    
    if (!super.isMatch(toEvu)) {
      return false;
    }
    if (!fireProcess.equals(simFireProcess)) {
      return false;
    }
    if (fireSpreadType != simFireSpreadType) {
      return false;
    }
    if (!isWithinDistanceZone(fromEvu,toEvu)) {
      return false;
    }
    
    return true;
  }

    /**
     * Check to see if the zone fire is going to is within accepted fire spotting parameters.
     * @param fromEvu
     * @param toEvu
     * @return
     */
  private boolean isWithinDistanceZone(simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu toEvu) {
    double distFeet = fromEvu.distanceToEvu(toEvu);
    double distMiles = distFeet / 5280.0;
    long   ratDistMiles = getRatDist(distMiles);
    
    return ((ratDistMiles >= ratStartDist) && (ratDistMiles <= ratEndDist));
    
  }
  
  /**
   * determines if the distance in miles from an originating evu to another is within the maximum distance meaning the distance in miles is less than the maximum distribution
   * @param fromEvu
   * @param toEvu
   * @return
   */
  public static boolean isWithinMaxDistance(simpplle.comcode.element.Evu fromEvu, simpplle.comcode.element.Evu toEvu) {
    double distFeet = fromEvu.distanceToEvu(toEvu);
    double distMiles = distFeet / 5280.0;
    long   ratDistMiles = getRatDist(distMiles);
    
    return (ratDistMiles < ratMaxDist);
    
  }
  /**
   * reads Fires Spotting logic data object : reads in version, fire process, fire spread type, starting distribution, end distribution, and probability.
   * Uses this to calculate rational starting distirbution, and rational end distribution and also update max distribution
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    @SuppressWarnings("unused")
    int version = in.readInt();
    super.readExternal(in);

    fireProcess = ProcessType.readExternalSimple(in);
    fireSpreadType = SpreadType.valueOf((String)in.readObject());
    
    startDist = in.readDouble();
    ratStartDist = getRatDist(startDist);
    endDist   = in.readDouble();
    updateMaxDist(endDist);
    ratEndDist = getRatDist(endDist);
    prob      = in.readInt();
  }
  /**
   * Writes to external source the Fire spotting Logic  data.  Methods pertinent to this class are fire spread type, starting distance, ending distance,
   * probability.  
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    fireProcess.writeExternalSimple(out);
    out.writeObject(fireSpreadType.toString());
    out.writeDouble(startDist);
    out.writeDouble(endDist);
    out.writeInt(prob);
  }  
}
