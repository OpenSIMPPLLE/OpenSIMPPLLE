/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class defines Fire Suppression Event Logic Data, a type of Logic Data
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.LogicData
 */

public class FireSuppEventLogicData extends LogicData implements Externalizable {
  
  private static final long serialVersionUID = -3888132919277111481L;

  static final int  version          = 2;

  private Season fireSeason;
  private Integer           prob;
/**
 * Constructor for Fire Suppression Event Logic Data.  Does not reference Logic Data superclass
 * and initializes fire season to YEAR and probability to 100, then sets the system knowledge kind to 
 * fire suppression event logic
 */
  public FireSuppEventLogicData() {
    fireSeason = Season.YEAR;
    prob = 100;
    
    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_EVENT_LOGIC;
  }
  
 /**
  * Duplicates this FireSuppEventLogicData object.  
  */
  public AbstractLogicData duplicate() {
    FireSuppEventLogicData logicData = new FireSuppEventLogicData();
    super.duplicate(logicData);

    logicData.fireSeason     = fireSeason;
    logicData.prob           = prob;

    return logicData;
  }
  /**
   * Gets the probability of Fire Suppression Event Logic Data
   * @return the probability.  
   */
  public int getProbabity() {
    return prob;
  }
  /**
   * 
   */
  public Object getValueAt(int col) {
    switch (col) {
      case FireSuppEventLogic.FIRE_SEASON_COL: return fireSeason;
      case FireSuppEventLogic.PROB_COL:        return prob;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets a value at a specified column.  Parameters handled in this class our fire season and probability.  
   * Both marks the logic data changed.  Other columns are handled by refering to superclass
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireSuppEventLogic.FIRE_SEASON_COL:
        Season newSeason = (Season)value;
        if (fireSeason != newSeason) {
          fireSeason = newSeason;
          markChanged();
        }
        break;
      case FireSuppEventLogic.PROB_COL:
        Integer newValue = (Integer)value;
        if (prob != newValue) {
          prob = newValue;
          markChanged();
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
  /**
   * reads in an Fire Suppression event logic object, this class specifically reads the fire season and probability,
   * fire season is read in for versions greater than one, and defaults to YEAR for those less than 1 
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    if (version > 1) {
      fireSeason = Season.valueOf((String)in.readObject());
    }
    else {
      fireSeason = Season.YEAR;
    }
    prob = in.readInt();
  }
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeObject(fireSeason.toString());
    out.writeInt(prob);
  }
  /**
   * Method to match an evu's Fire Suppression Event Logic Data with this one.  
   */
  public boolean isMatch(Evu evu) {
    if (!super.isMatch(evu)) {
      return false;
    }
    if (fireSeason == Season.YEAR) {
      return true;
    }

    ProcessOccurrenceSpreadingFire event = FireEvent.currentEvent;
    if (event == null) {
      return true;
    }
    
    Season season = event.getFireSeason();
    if (fireSeason != season) {
      return false;
    }
    
    return true;
  }
  /**
   * 
   * @param random
   * @return true if random instance variable is <= rational probability
   */
  
  public boolean isSuppressed(int random) {
    int ratProb = Simulation.getInstance().getRationalProbability(prob);
    return (random <= ratProb);
  }

}
