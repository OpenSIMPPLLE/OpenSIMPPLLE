package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class contains methods for a Producing Seed Logic Data, a type of Logic Data. 
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 */
public class ProducingSeedLogicData extends LogicData {
  static final long serialVersionUID = -962990575499495985L;
  static final int  version          = 2;

  private ArrayList<Evu.RegenTypes> regenTypes;
  private Integer                   prob;

  /**
   * Constructor for producing seed logic data.  Inherits from Logic Data superclass and initializes regeneration type
   * arraylist, the probability to 100 and sets system knowledge to producing seed logic
   */
  public ProducingSeedLogicData() {
    super();

    regenTypes     = new ArrayList<Evu.RegenTypes>();
    prob = 100;

    sysKnowKind = SystemKnowledge.Kinds.PRODUCING_SEED_LOGIC;
  }
/**
 * Duplicates this Producing Seed Logic Data object.   
 */
  public AbstractLogicData duplicate() {
    ProducingSeedLogicData logicData = new ProducingSeedLogicData();
    super.duplicate(logicData);

    return logicData;
  }
/**
 * Sets teh regeneration types for this Producing Seed Logic Data object.
 * @param newList the new arraylist of Evu regeneration types 
 */
  public void setRegenTypes(ArrayList<Evu.RegenTypes> newList) {
    regenTypes = new ArrayList<Evu.RegenTypes>(newList);
    markChanged();
  }
  public Object getValueAt(int col) {
    switch (col) {
      case ProducingSeedLogic.REGEN_TYPE_COL:     return regenTypes;
      case ProducingSeedLogic.PRODUCING_SEED_COL: return prob;
      default: return super.getValueAt(col);
    }
  }
  public void setValueAt(int col, Object value) {
    switch (col) {
      case ProducingSeedLogic.PRODUCING_SEED_COL:
        Integer newValue = (Integer)value;
        if (prob != newValue) {
          prob = newValue;
          markChanged();
        }
        break;
      case ProducingSeedLogic.REGEN_TYPE_COL:
        // set by editor
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * Logic data requires a match.  
 * @param evu
 * @param tStep
 * @param lifeform
 * @param vegType
 * @param regenType
 * @return
 */
  public boolean isMatch(Evu evu,Integer tStep,Lifeform lifeform,
                         VegetativeType vegType, Evu.RegenTypes regenType)
  {
    FireResistance resistance =
      FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    if (super.isMatch(resistance,evu,tStep,lifeform,vegType) == false) {
      return false;
    }

    if ((regenTypes != null && regenTypes.size() > 0 &&
         regenTypes.contains(regenType) == false)) {
      return false;
    }


    return true;
  }
/**
 * Reads the information for this Producing Seed Logic Data from an external source.  
 * The info read is in the following order an arraylist of Evu Regeneration types, probability (stored as ints) from an external source, then all the 
 * info from Logic Data superclass. 
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    regenTypes    = (ArrayList<Evu.RegenTypes>)in.readObject();
    if (version == 1) {
      Boolean value = in.readBoolean();
      prob = (value) ? 100 : 0;
    }
    else {
      prob = in.readInt();
    }
    super.readExternal(in);

  }
  /**
   * Writes to an external source this producing seed logic objects regeneration types arraylist and probability as well as calling to the Logic Data class to write out additional info. 
   *  
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    out.writeObject(regenTypes);
    out.writeInt(prob);

    super.writeExternal(out);

  }
/**
 * If probability of producing seed is greater than 0, does the monte carlo probability method to find out if the random variable is less than or equal to the rational probability.  
 * 
 * @return true if randomly generated number between 0 and 10000 is less than or equal to the simulation instances rational probability.  
 */
  public Boolean getProducingSeed() {
    if (prob == 0) { return false; }
    
    int rand = Simulation.getInstance().random();
    int ratProb = Simulation.getInstance().getRationalProbability(prob);
    return (rand <= ratProb);
  }

}
