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
import java.io.Externalizable;

/**
 * This class has methods pertaining to Competition Data, a type of LogicData.
 * 
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public class DoCompetitionData extends LogicData implements Externalizable {
  static final long serialVersionUID = 4884842034311116010L;
  static final int  version          = 2;

  public enum Actions { LOWEST_DENSITY,
                        DENSITY_DOWN_ONE,
                        DENSITY_UP_ONE
  }

  public enum DensityChange {INCREASED, DECREASED, ANY}

  public DensityChange ANY       = DensityChange.ANY;
  public DensityChange INCREASED = DensityChange.INCREASED;
  public DensityChange DECREASED = DensityChange.DECREASED;

  private Boolean              selected;
  private Lifeform             lifeform;
  private Integer              minCanopy;
  private Integer              maxCanopy;
  private DensityChange        densityChange;
  private ArrayList<Lifeform>  changeLifeforms;
  private Actions              action;
/**
 * constructor.  Inherits from LogicData superclass. Initiates lifeform to trees, adds shrubs, and herbacious.  Sets canopy max and min to 0
 */
  public DoCompetitionData(){
    super();
    lifeform        = Lifeform.TREES;
    changeLifeforms = new ArrayList<Lifeform>();
    changeLifeforms.add(Lifeform.SHRUBS);
    changeLifeforms.add(Lifeform.HERBACIOUS);

    selected        = true;
    minCanopy       = 0;
    maxCanopy       = 0;
    densityChange   = INCREASED;
    action          = Actions.DENSITY_DOWN_ONE;
    sysKnowKind = SystemKnowledge.Kinds.DOCOMPETITION_LOGIC;
  }
  /**
   * creates a duplicate of current DoCompetitionData by sending to the LogicData superclass constructor.  
   * @return a duplicate of current DoCompetitionData
   */
  public AbstractLogicData duplicate() {
    DoCompetitionData logicData = new DoCompetitionData();
    super.duplicate(logicData);

    logicData.lifeform  = lifeform;
    logicData.selected  = selected;
    logicData.minCanopy = minCanopy;
    logicData.maxCanopy = maxCanopy;
    logicData.densityChange = densityChange;
    logicData.changeLifeforms.clear();
    for (Lifeform lifeform : changeLifeforms) {
      logicData.changeLifeforms.add(lifeform);
    }
    logicData.action = action;

    return logicData;
  }

  /**
   * adds a new LifeForm into an arraylist if that list does not already contain the LifeForm, marks the system as changed
   * @param lifeform the Lifeform to be added
   */
  public void addChangeLifeform(Lifeform lifeform) {
    if (changeLifeforms.contains(lifeform) == false) {
      changeLifeforms.add(lifeform);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * 
   * @param lifeform the Lifeform to be removed from lifeform arrayList which was initialized in DoCompetitionData constructor, then marks the system as changed
   */
  public void removeChangeLifeform(Lifeform lifeform) {
    if (changeLifeforms.contains(lifeform)) {
      changeLifeforms.remove(lifeform);
      SystemKnowledge.markChanged(sysKnowKind);
    }
  }
  /**
   * clear all life forms from ArrayList of lifeforms and mark system changed
   */
  public void clearLifeforms() {
    changeLifeforms.clear();
    SystemKnowledge.markChanged(sysKnowKind);
  }

  /**
   * creates a LifeForm array 
   * @return LifeForm [] with [0]=trees [1]=shrubs
   */
  public static Lifeform[] getLifeformValues() {
    return new Lifeform[] { Lifeform.TREES, Lifeform.SHRUBS };
  }
  public void setLifeform(Lifeform lifeform) {
    if (this.lifeform == lifeform) { return; }

    this.lifeform = lifeform;
    changeLifeforms.clear();
    if (lifeform == Lifeform.TREES) {
      changeLifeforms.add(Lifeform.SHRUBS);
      changeLifeforms.add(Lifeform.HERBACIOUS);
    }
    else if (lifeform == Lifeform.SHRUBS) {
      changeLifeforms.add(Lifeform.HERBACIOUS);
    }
    SystemKnowledge.markChanged(sysKnowKind);
  }

  
 /**
  * @param evu existing vegetative unit 
  * @return false if lifeform is not in evu or density increases but the change is <=0 or density decreases and change>=0 or density of vegetative type is not between min and max canopy
  * else return true
  */
  public boolean isMatch(Evu evu) {
    if (!selected) { return false; }

    if (evu.hasLifeform(lifeform) == false) {
      return false;
    }

    if (super.isMatch(evu,lifeform) == false) {
      return false;
    }

    int cStep = Simulation.getCurrentTimeStep();
    VegSimStateData state = evu.getState(cStep,lifeform);
    VegetativeType  vt = state.getVegType();
    if (vt.getDensity().inPctCanopyRange(minCanopy,maxCanopy) == false) {
      return false;
    }

    int change = evu.getCanopyChange(lifeform);
    if (densityChange == DensityChange.INCREASED &&
        change <= 0) {
      return false;
    }
    else if (densityChange == DensityChange.DECREASED &&
        change >= 0) {
      return false;
    }

    return true;
  }
/**
 * 
 * Method for doing an action with vegetative unit.  based on habitat type group action variable an Action is chosen from the 
 * Action enum in this class.  Choices for action are LOWEST_DENSITY, DENSITY_DOWN_ONE, DENSITY_UP_ONE
 * @param evu ecological vegetative unit to do action with 
 */
  public void doAction(Evu evu) {
    int cStep = Simulation.getCurrentTimeStep();

    for (int l=0; l<changeLifeforms.size(); l++) {

      VegSimStateData state = evu.getState(cStep,changeLifeforms.get(l));
      if (state == null) { continue; }
      VegetativeType  vt = state.getVegType();

      HabitatTypeGroup group = evu.getHabitatTypeGroup();
      VegetativeType   veg = null;
      switch (action) {
        case LOWEST_DENSITY:
          veg = group.findLowestDensityVegetativeType(vt);
          break;
        case DENSITY_DOWN_ONE:
          veg = group.findLowerDensityVegetativeType(vt);
          break;
        case DENSITY_UP_ONE:
          veg = group.findHigherDensityVegetativeType(vt);
          break;
      }
      if (veg != null) {
        state.setVegType(veg);
        state.setProb((short) Evu.COMP);
      }
    }
  }
/**
 * Used to find the object type at a particular column found by column Id.  
 * Choices are selected, lifeform, minCanopy, maxCanopy, densityChange, changeLifeforms, action, or one of the superclass column objects.  
 */
  public Object getValueAt(int col) {
    switch (col) {
      case DoCompetitionLogic.SELECTED_COL:         return selected;
      case DoCompetitionLogic.LIFEFORM_COL:         return lifeform;
      case DoCompetitionLogic.MIN_CANOPY_COL:       return minCanopy;
      case DoCompetitionLogic.MAX_CANOPY_COL:       return maxCanopy;
      case DoCompetitionLogic.DENSITY_CHANGE_COL:   return densityChange;
      case DoCompetitionLogic.CHANGE_LIFEFORMS_COL: return changeLifeforms;
      case DoCompetitionLogic.ACTION_COL:           return action;
      default: return super.getValueAt(col);
    }
  }
  /**
   * sets a value.  what value depends on type of object passed and what column is used in switch 
   * the objects passed could be Boolean = column selected, Lifeform = lifeform, Integer = canopy min or max, DensityChange , or Action   
   */
  public void setValueAt(int col, Object value) {
    switch (col) {
      case DoCompetitionLogic.SELECTED_COL:
        if (selected != (Boolean)value) {
          selected = (Boolean) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case DoCompetitionLogic.LIFEFORM_COL:
        setLifeform((Lifeform)value);
        break;
      case DoCompetitionLogic.MIN_CANOPY_COL:
        if (minCanopy != (Integer)value) {
          minCanopy = (Integer) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case DoCompetitionLogic.MAX_CANOPY_COL:
        if (maxCanopy != (Integer)value) {
          maxCanopy = (Integer) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case DoCompetitionLogic.DENSITY_CHANGE_COL:
        if (densityChange != (DensityChange)value) {
          densityChange = (DensityChange) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      case DoCompetitionLogic.CHANGE_LIFEFORMS_COL: /* Set by dialog code */
        break;
      case DoCompetitionLogic.ACTION_COL:
        if (action != (Actions)value) {
          action = (Actions) value;
          SystemKnowledge.markChanged(sysKnowKind);
        }
        break;
      default:
        super.setValueAt(col,value);
    }
  }
/**
 * Reads from an external source the Do Competition data.  IN addition to logic data superclass info, this reads in lifeform, selected boolean,
 * minCanopy, maxCanopy, densityChange, changeLifeForms, and action.  
 */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    int version = in.readInt();

    super.readExternal(in);

    if (version > 1) {
      lifeform = (Lifeform)in.readObject();
    }
    selected        = in.readBoolean();
    minCanopy       = in.readInt();
    maxCanopy       = in.readInt();
    densityChange   = (DensityChange)in.readObject();
    changeLifeforms = (ArrayList<Lifeform>)in.readObject();
    action          = (Actions)in.readObject();

  }
  /**
   * Writes to an external source all the superclass logic data and the lifeform, selected boolean,
 * minCanopy, maxCanopy, densityChange, changeLifeForms, and action
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(version);

    super.writeExternal(out);

    out.writeObject(lifeform);
    out.writeBoolean(selected);
    out.writeInt(minCanopy);
    out.writeInt(maxCanopy);
    out.writeObject(densityChange);
    out.writeObject(changeLifeforms);
    out.writeObject(action);
  }
  /**
   * Sets the minimum canopy cover (density).
   * @return minCanopy minimum canopy cover (density)
   */
  public int getMaxCanopy() {
    return maxCanopy;
  }
  /**
   * Sets the minimum canopy cover (density).
   * @return minCanopy minimum canopy cover (density)
   */
  public int getMinCanopy() {
    return minCanopy;
  }
/**
 * True if selected
 * @return
 */
  public boolean isSelected() {
    return selected;
  }
/**
 * Sets the selected boolean for this docompetition object.
 * @param selected
 */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
/**
 * Sets the minimum canopy cover (density).
 * @param minCanopy minimum canopy cover (density)
 */
  public void setMinCanopy(int minCanopy) {
    this.minCanopy = minCanopy;
  }
  /**
   * Sets the maximum canopy cover (density).
   * @param minCanopy maximum canopy cover (density)
   */
  public void setMaxCanopy(int maxCanopy) {
    this.maxCanopy = maxCanopy;
  }
/**
 * Gets the lifeform.  Choices for lifeform are Trees, shrubs, herbacious, agriculture, of NA (no classification)
 * @return lifeform
 */
  public Lifeform getLifeform() {
    return lifeform;
  }
/**
 * Gets the arraylist of changed life forms.  
 * @return
 */
  public ArrayList getChangeLifeforms() {
    return changeLifeforms;
  }
}
