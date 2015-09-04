package simpplle.comcode;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.io.Externalizable;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Regeneration Delay Logic Data, a type of Logic Data. As with all the logic data classes
 * it provides methods to match logic data and logic, getters and setters, and methods to read and write the objects used by this class 
 * to and from external sources
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 * 
 */
public class RegenerationDelayLogicData extends LogicData implements Externalizable {
  static final long serialVersionUID = -8400707058472182663L;
  static final int  version          = 1;

  private Integer delay;
/**
 * Constructor for Regeneration Delay Logic Data.  Inherits from Logic Data superclass and initializes delay to 3, and system knoledge 
 * to regeneration delay logic.  
 */
  public RegenerationDelayLogicData() {
    super();
    delay = 3;
    sysKnowKind = SystemKnowledge.Kinds.REGEN_DELAY_LOGIC;
  }
/**
 * @return true if evu and life form are a match
 */
  public boolean isMatch(Evu evu, Lifeform life) {
    return super.isMatch(evu,life);
  }
  /**
   * gets the delay based on evu and lifeform.  
   * @param evu
   * @param lifeform
   * @return delay if there is a match, or 0 if no match 
   */
  public int getDelay(Evu evu, Lifeform lifeform) {
    if (isMatch(evu,lifeform)) {
      return delay;
    }
    return 0;
  }
/**
 * duplicates the regeneration logic delay data
 */
  public AbstractLogicData duplicate() {
    RegenerationDelayLogicData logicData = new RegenerationDelayLogicData();
    super.duplicate(logicData);

    logicData.delay  = delay;

    return logicData;
  }
/**
 * sets the delay at a designated column.  if the int column is not a delay column, defaults to the Logic Data superclass
 */
  public Object getValueAt(int col) {
    switch (col) {
      case RegenerationDelayLogic.DELAY_COL: return delay;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets value at a designated delay column.  if passed column int is not a delay column defaults to Logic Data superclass.
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case RegenerationDelayLogic.DELAY_COL:
        if (delay != (Integer)value) {
          delay = (Integer) value;
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * reads in the version and delay object from external source.  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    super.readExternal(in);

    delay = in.readInt();
  }
  /**
   * writes to an external location in order: version and delay
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    out.writeInt(delay);
  }
}
