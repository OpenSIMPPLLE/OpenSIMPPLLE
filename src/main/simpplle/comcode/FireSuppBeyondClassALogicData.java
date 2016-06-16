package simpplle.comcode;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * 
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * <p>This class defines Fire Suppression Beyond Class A Logic Data, a type of Logic Data.
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
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 *
 * 
 * @see simpplle.comcode.LogicData
 * 
 */
public class FireSuppBeyondClassALogicData extends LogicData {
  static final long serialVersionUID = 7964392846510737066L;
  static final int  version          = 3;

  public enum SpreadKind { AVERAGE, EXTREME };
  public enum FireType   { SRF, MSF, LSF };

  private FireType          fireType;
  private boolean           suppress;
  private SpreadKind        spreadKind;
  private Integer           prob;
  /**
   * Constructor for Fire Suppression Beyond Class A Logic Data.  Inherits from Logic Data superclass.  
   */
  public FireSuppBeyondClassALogicData() {
    super();

    fireType       = FireType.SRF;
    suppress       = true;
    spreadKind     = SpreadKind.AVERAGE;
    prob           = 100;

    sysKnowKind = SystemKnowledge.Kinds.FIRE_SUPP_BEYOND_CLASS_A_LOGIC;
  }
/**
 * Creates new instance of FireSuppBeyondClassALogicData, inherit all instance variables from Logic Data superclass then set fire type, suppress, spread kind, prob to those of this class.
 * This makes a duplicate
 */
  public AbstractLogicData duplicate() {
    FireSuppBeyondClassALogicData logicData = new FireSuppBeyondClassALogicData();
    super.duplicate(logicData);

    logicData.fireType       = fireType;
    logicData.suppress       = suppress;
    logicData.spreadKind     = spreadKind;
    logicData.prob           = prob;

    return logicData;
  }
/**
 * Gets the probability for Fire Suppression Beyond Class A.
 * @return
 */
  public int getProbabity() {
    return prob;
  }
  /**
   * Gets the object type for a particular column.  The Choices are fire type, spread kind, suppress boolean, and probability.  
   */
  public Object getValueAt(int col) {
    switch (col) {
      case FireSuppBeyondClassALogic.FIRE_TYPE_COL:   return fireType;
      case FireSuppBeyondClassALogic.SPREAD_KIND_COL: return spreadKind;
      case FireSuppBeyondClassALogic.SUPPRESS_COL:    return suppress;
      case FireSuppBeyondClassALogic.PROB_COL:        return prob;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets the object at a particular cell.  Choices for column from this class are   FIRE_TYPE_COL, SUPPRESS_COL, SPREAD_KIND_COL, PROB_COL
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case FireSuppBeyondClassALogic.FIRE_TYPE_COL:
      {
        FireType newValue = (FireType) value;
        if (fireType != newValue) {
          fireType = newValue;
          markChanged();
        }
        break;
      }
      case FireSuppBeyondClassALogic.SUPPRESS_COL:
      {
        boolean newValue = (Boolean) value;
        if (suppress != newValue) {
          suppress = newValue;
          markChanged();
        }
        break;
      }
      case FireSuppBeyondClassALogic.SPREAD_KIND_COL:
      {
        SpreadKind newValue = (SpreadKind) value;
        if (spreadKind != newValue) {
          spreadKind = newValue;
          markChanged();
        }
        break;
      }
      case FireSuppBeyondClassALogic.PROB_COL:
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
 * Checks if this Fire Suppression Beyond Class A is the same as parameter Fire Suppression Beyond Class A
 * @param processType
 * @param isExtreme true if extreme, false if not
 * @param evu ecological vegetative unit
 * @param tStep time step
 * @param lifeform
 * @param vegType
 * @return
 */
  public boolean isMatch(ProcessType processType, boolean isExtreme, Evu evu,
                         Integer tStep,Lifeform lifeform,
                         VegetativeType vegType)
  {
    FireResistance resistance =
      FireEvent.getSpeciesResistance(Simpplle.getCurrentZone(),evu,lifeform);

    if (super.isMatch(resistance,evu,tStep,lifeform,vegType) == false) {
      return false;
    }

    ProcessType chosenFireType;
    switch (fireType) {
      case SRF: chosenFireType = ProcessType.SRF; break;
      case MSF: chosenFireType = ProcessType.MSF; break;
      case LSF: chosenFireType = ProcessType.LSF; break;
      default: return false;
    }

    if (processType != chosenFireType) {
      return false;
    }

    SpreadKind eventSpreadKind = (isExtreme ? SpreadKind.EXTREME : SpreadKind.AVERAGE);
    if (eventSpreadKind != spreadKind) {
      return false;
    }

    return true;
  }
/**
 * Reads from an external source the fire suppression beyond class A logic data object.  The variables specifically from this object are 
  * fire type, spread kind, suppress boolean, and probability (int form).
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();
    super.readExternal(in);

    fireType   = (FireType)in.readObject();
    spreadKind = (SpreadKind)in.readObject();
    suppress   = in.readBoolean();
    
    prob = 100;
    if (version > 2) {
      prob = in.readInt();
    }
    
    if (version == 1) {
      roadStatusList = (ArrayList<Roads.Status>)in.readObject();
    }
  }
  /**
   * Writes to an external source the fire suppression beyond class A logic data object.  The variables specifically from this object are 
   * fire type, spread kind, suppress boolean, and probability (int form).
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);
    super.writeExternal(out);

    out.writeObject(fireType);
    out.writeObject(spreadKind);
    out.writeBoolean(suppress);
    out.writeInt(prob);
  }
  /**
   * Method to predict if a fire is suppressed.  
   * @return
   */
  public boolean isSuppressed() {
    int rand = Simulation.getInstance().random();
    int ratProb = Simulation.getInstance().getRationalProbability(prob);
    return (suppress && (rand <= ratProb));
  }

}
