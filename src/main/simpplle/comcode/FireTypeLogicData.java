/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;

/**
 * This class defines Fire Type Logic Data, a type of Logic Data
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * @see simpplle.comcode.BaseLogic
 */

public class FireTypeLogicData extends LogicData implements Externalizable {
  static final long serialVersionUID = 868082718500978572L;
  static final int  version          = 1;

  public transient ProcessType              wetter;
  public transient ProcessType              normal;
  public transient ProcessType              drier;
/**
 * Constructor for fire type logic data.  This will have three variables which are ProcessType objects and makes them all to Light Severity Fire
 * processes.  Then sets the system knowledte to Fire Type.
 */
  public FireTypeLogicData() {
    super();
    wetter          = ProcessType.LSF;
    normal          = ProcessType.LSF;
    drier           = ProcessType.LSF;
    sysKnowKind     = SystemKnowledge.Kinds.FIRE_TYPE_LOGIC;
  }
  /**
   * Duplicates this FireTypeLogicData object by creating a new one and transfering the process values for wetter, normal, and drier to it.    
   */
  public FireTypeLogicData duplicate() {
    FireTypeLogicData logicData = new FireTypeLogicData();
    super.duplicate(logicData);

    logicData.wetter = wetter;
    logicData.normal = normal;
    logicData.drier  = normal;

    return logicData;
  }

  public Object getValueAt(int col) {
    switch (col) {
      case FireEventLogic.WETTER_COL:           return wetter.getShortName();
      case FireEventLogic.NORMAL_COL:           return normal.getShortName();
      case FireEventLogic.DRIER_COL:            return drier.getShortName();
      default: return super.getValueAt(col);
    }
  }
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireEventLogic.WETTER_COL:           wetter = ProcessType.get((String)value); break;
      case FireEventLogic.NORMAL_COL:           normal = ProcessType.get((String)value); break;
      case FireEventLogic.DRIER_COL:            drier  = ProcessType.get((String)value); break;
      default:
        super.setValueAt(col,value);
    }
  }
  /**
   * Reads from an external source the Fire Type Logic Data.  Along with the superclass logic data, the wetter, normal, drier processes will be read.  
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    super.readExternal(in);

    wetter = ProcessType.readExternalSimple(in);
    normal = ProcessType.readExternalSimple(in);
    drier = ProcessType.readExternalSimple(in);

  }
  /**
   * Writes to an external source the Fire Type Logic Data.  Along with the superclass logic data, the wetter, normal, drier processes will be written.  
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    wetter.writeExternalSimple(out);
    normal.writeExternalSimple(out);
    drier.writeExternalSimple(out);

  }

  public boolean rulesCompatible(FireTypeLogicData rule) {
    return (super.rulesCompatible(rule) &&
            (wetter == rule.wetter) &&
            (normal == rule.normal) &&
            (drier == rule.drier));
  }

  // *** Simulation code ***
  // ***********************

  /**
   * Returns a fire type if this rule applies to the resistance, evu, and lifeform.
   *
   * @param evu A vegetation unit
   * @return A process type if the rule applies, otherwise null
   */
  public ProcessType getFireTypeIfMatch(FireResistance resistance, Evu evu, Lifeform lifeform) {

    if (super.isMatch(resistance,evu,lifeform)) {

      Climate.Season currentSeason = Simpplle.getCurrentSimulation().getCurrentSeason();

      return getFireType(Simpplle.getClimate().getMoisture(currentSeason));

    }

    return null;

  }

  /**
   * Returns a fire process type for a relative moisture level.
   *
   * @param moisture A relative moisture level
   * @return A fire process type
   */
  private ProcessType getFireType(Moisture moisture) {

    if (moisture == Moisture.WETTER) {

      return wetter;

    } else if (moisture == Moisture.NORMAL) {

      return normal;

    } else if (moisture == Moisture.DRIER) {

      return drier;

    } else {

      return wetter;

    }
  }


}



